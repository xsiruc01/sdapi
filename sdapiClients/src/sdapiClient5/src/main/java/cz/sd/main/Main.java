package cz.sd.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;
import org.glassfish.jersey.client.ClientConfig;

/**
 * Ukazkovy klient aplikacniho rozhrani pro service desky. Testovaci scenar c. 5
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class Main {

    private static final String GET_TICKET_RESOURCE = "tickets/getTicket";
    private static final String CREATE_TICKET_RESOURCE = "tickets/createTicket";
    private static final String ADD_MESSAGE_RESOURCE = "tickets/addMessage";
    private static final String COPY_TICKET_RESOURCE = "tickets/copyTicket";

    // cas cekani mezi jednotlivymi kroky
    private static final int SLEEP_TIME = 1;
    // pokud je priznak nastaven, ceka se mezi kroky na stisknuti klavesy uzivatelem
    private static boolean debug = false;

    // zdrojovy sd
    private static final String SOURCE_SYSTEM = "Alvao";
    // cilovy sd
    private static final String DEST_SYSTEM = "itop";

    // url webovych sluzeb api
    private static String url;

    /**
     * Hlavni program scenare.
     *
     * @param args Parametry pri spusteni.
     */
    public static void main(String[] args) {
        try {
            // nacteme jmeno systemu a parametr pro ladeni
            for (String arg : args) {
                if (arg.equals("-d")) {
                    debug = true;
                }
            }
            // nacteme url webovych sluzeb api
            if (getUrl() != 0) {
                System.out.println("Chyba pri cteni konfiguracniho souboru");
                System.exit(-1);
            }

            ClientConfig clientConfig = new ClientConfig();
            Client client = ClientBuilder.newClient(clientConfig);

            // vytvoreni incidentu /////////////////////////////////////////////////
            Map<String, Object> create = new HashMap<>();
            create.put("system", SOURCE_SYSTEM);
            create.put("ticketType", "Incident");
            create.put("subject", "Nedostupna sit");
            create.put("description", "Nelze se pripojit na internet ani na sdileny disk.");
            create.put("sla", "Incident SLA");
            create.put("service", "Incident management");
            create.put("status", "open");
            create.put("priority", "critical");
            create.put("requesterName", "Pavel S");
            create.put("requesterId", "4");
            create.put("technicianName", "Pavel S");
            create.put("technicianId", "4");

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonTicket = gson.toJson(create);

            WebTarget webTarget = client.target(url).path(CREATE_TICKET_RESOURCE);
            Response wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            CreateResult createResult = gson.fromJson(wsResponse.readEntity(String.class), CreateResult.class);
            Long ticketId = createResult.getTicketId();
            if (!createResult.isSuccess()) {
                System.out.println(">>> Nastala chyba pri vytvareni tiketu");
                return;
            }
            System.out.println(">>> Vytvoren tiket (incident) c. " + ticketId);
            ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // pridani poznamky o potrebe kopie tiketu /////////////////////////////
            Map<String, Object> addMessage = new HashMap<>();
            addMessage.put("system", SOURCE_SYSTEM);
            addMessage.put("ticketType", "Incident");
            addMessage.put("ticketId", ticketId);
            addMessage.put("message", "Tento tiket bude treba vytvorit v systemu iTop naseho dodavatele.");
            jsonTicket = gson.toJson(addMessage);

            webTarget = client.target(url).path(ADD_MESSAGE_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Pridana poznamka k tiketu v systemu ALVAO");
            ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // ziskani tiketu //////////////////////////////////////////////////////
            Map<String, Object> get = new HashMap<>();
            get.put("system", SOURCE_SYSTEM);
            get.put("ticketType", "Incident");
            get.put("ticketId", ticketId);
            jsonTicket = gson.toJson(get);

            webTarget = client.target(url).path(GET_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            GetResult getResult = gson.fromJson(wsResponse.readEntity(String.class), GetResult.class);

            System.out.println(">>> Ziskani informaci o tiketu.");
            ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // kopie tiketu ////////////////////////////////////////////////////////
            Map<String, Object> copy = new HashMap<>();
            copy.put("srcSystem", SOURCE_SYSTEM);
            copy.put("destSystem", DEST_SYSTEM);
            copy.put("ticketType", "Incident");
            copy.put("ticketId", ticketId);
            copy.put("organization", "Demo");
            copy.put("requesterId", 1);
            copy.put("technicianId", 1);
            jsonTicket = gson.toJson(copy);

            webTarget = client.target(url).path(COPY_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            createResult = gson.fromJson(wsResponse.readEntity(String.class), CreateResult.class);
            ticketId = createResult.getTicketId();
            if (!createResult.isSuccess()) {
                System.out.println(">>> Nastala chyba pri kopirovani tiketu");
                return;
            }
            System.out.println(">>> Vytvoren tiket (incident) c. " + ticketId);
            ////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // pridani poznamky o potrebe kopie tiketu /////////////////////////////
            addMessage = new HashMap<>();
            addMessage.put("system", DEST_SYSTEM);
            addMessage.put("ticketType", "Incident");
            addMessage.put("ticketId", ticketId);
            addMessage.put("message", "Tento tiket byl zkopirovan ze systemu ALVAO naseho odberatele.");
            jsonTicket = gson.toJson(addMessage);

            webTarget = client.target(url).path(ADD_MESSAGE_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Pridana poznamka k tiketu v systemu iTop");
            ////////////////////////////////////////////////////////////////////////
        } catch (Exception ex) {
            System.out.println("Pri behu programu nastala chyba.");
            System.exit(-1);
        }
    }

/**
     * Nacte z konfiguracniho souboru url webovych sluzeb.
     *
     * @return 0 pokud je vse v poradku, -1 v pripade chyby
     */
    public static int getUrl() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("api_config.properties");
            prop.load(input);
        } catch (IOException ex) {
            return -1;
        }

        url = prop.getProperty("url");
        return 0;
    }
    
    /**
     * Vytvoreni tiketu.
     */
    @Getter @Setter
    private static class CreateResult {        
        private Long ticketId;
        private boolean success;
    }
    
    /**
     * Ziskani tiketu.
     */
    @Getter
    @Setter
    private static class GetResult {

        /**
         * Identifikator ticketu.
         */
        
        private long ticketId;
        /**
         * Predmet ticketu.
         */
        private String subject;
        /**
         * Popis ticketu. Informace, poznamky.
         */
        private String detail;
        /**
         * Priorita.
         */
        private String priority;
        /**
         * Status.
         */
        private String status;
        /**
         * Typ ticketu.
         */
        private String type;
        /**
         * Poznamky.
         */
        private List<Note> notes;
    }
    
    /**
     * Poznamka.
     */
    @Getter
    @Setter
    private static class Note {
        private String text;
        private String date;
    }
}

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
 * Ukazkovy klient aplikacniho rozhrani pro service desky. Testovaci scenar c. 1
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class Main {
    
    private static final String GET_TICKET_RESOURCE = "tickets/getTicket";
    private static final String CREATE_TICKET_RESOURCE = "tickets/createTicket";
    private static final String ADD_MESSAGE_RESOURCE = "tickets/addMessage";
    private static final String SUSPEND_TICKET_RESOURCE = "tickets/suspendTicket";
    private static final String REOPEN_TICKET_RESOURCE = "tickets/reopenTicket";
    private static final String RESOLVE_TICKET_RESOURCE = "tickets/resolveTicket";
    private static final String UPDATE_TICKET_RESOURCE = "tickets/updateTicket";
    private static final String CLOSE_TICKET_RESOURCE = "tickets/closeTicket";
    private static final String GET_SYSTEM_RESOURCE = "systems/getSystem";
    
        // cas cekani mezi jednotlivymi kroky
    private static final int SLEEP_TIME = 1;
    // pokud je priznak nastaven, ceka se mezi kroky na stisknuti klavesy uzivatelem
    private static boolean debug = false;
    
    //private static final String system = "Alvao";
  
    // jmeno systemu
    private static String system;
    // url webovych sluzeb api
    private static String url;
    
    // priznak, zda se jedna o urcity service desk - kvuli nastaveni extra parametru apod.
    private static boolean isFreshservice = false;
    private static boolean isItop = false;
    private static boolean isAlvao = false;
    private static boolean isManageEngine = false;
    private static boolean isWebHelpDesk = false;
    
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
                } else {
                    system = arg;
                }
            }
            if (system == null) {
                System.out.println("Chybne zadane parametry");
                System.exit(-1);
            }
            // nacteme url webovych sluzeb api
            if (getUrl() != 0) {
                System.out.println("Chyba pri cteni konfiguracniho souboru");
                System.exit(-1);
            }

            ClientConfig clientConfig = new ClientConfig();
            Client client = ClientBuilder.newClient(clientConfig);

            // ziskani typu api, kvuli configuracim ////////////////////////////////
            Map<String, Object> getSystem = new HashMap<>();
            getSystem.put("system", system);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonTicket = gson.toJson(getSystem);

            WebTarget webTarget = client.target(url).path(GET_SYSTEM_RESOURCE);
            Response wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            SystemResponse sr = gson.fromJson(wsResponse.readEntity(String.class), SystemResponse.class);

            // konfigurace systemu - pridani konfiguracni polozky, se kterou se bude dale pracovat
            switch (sr.getSystemType()) {
                case "Alvao":
                    isAlvao = true;
                    break;
                case "iTop":
                    isItop = true;
                    break;
                case "Freshservice":
                    isFreshservice = true;
                    break;
                case "ManageEngine":
                    isManageEngine = true;
                    break;
                case "Web Help Desk":
                    isWebHelpDesk = true;
                    break;
                default:
                    System.out.println("Chybne zadany service desk");
                    System.exit(-1);
            }

            // vytvoreni incidentu /////////////////////////////////////////////////
            Map<String, Object> create = new HashMap<>();
            create.put("system", system);
            create.put("ticketType", "Incident");
            create.put("subject", "Nefunguje program pro kresleni diagramu");
            create.put("description", "Prestal fungovat program pro kresleni diagramu. Bez nej nemuzeme pokracovat v jednom z projektu. Prosime o napravu.");
            create.put("organization", "Demo");
            create.put("sla", "Incident SLA");
            create.put("service", "Incident management");
            create.put("status", "open");
            create.put("priority", "normal");
            if (isFreshservice) {
                create.put("requesterName", "schirda@gmail.com");
                create.put("requesterId", 4000215144L);
                create.put("technicianId", 4000215144L);
            } else if (isAlvao) {
                create.put("requesterName", "Pavel S");
                create.put("requesterId", "4");
                create.put("technicianName", "Pavel S");
                create.put("technicianId", "4");
            } else if (isItop) {
                create.put("requesterId", 1);
                create.put("technicianId", 1);
            } else if (isWebHelpDesk) {
                create.put("requesterId", 1);
                create.put("technicianId", 1);
            } else if (isManageEngine) {
                create.put("technicianName", "Howard Stern");
            }

            gson = new GsonBuilder().setPrettyPrinting().create();
            jsonTicket = gson.toJson(create);

            webTarget = client.target(url).path(CREATE_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

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

            // pridani poznamky a pozastaveni tiketu ///////////////////////////////
            Map<String, Object> suspend = new HashMap<>();
            suspend.put("system", system);
            suspend.put("ticketType", "Incident");
            suspend.put("ticketId", ticketId);
            suspend.put("status", "On hold");
            suspend.put("note", "Nevim o jaky program se jedna, nemuzu tak pokracovat v reseni. Napiste o co jde, do te doby pozastavuji.");
            jsonTicket = gson.toJson(suspend);

            webTarget = client.target(url).path(SUSPEND_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Tiket pozastaven");
        ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // pridani poznamky o nazvu programu ///////////////////////////////////
            Map<String, Object> addMessage = new HashMap<>();
            addMessage.put("system", system);
            addMessage.put("ticketType", "Incident");
            addMessage.put("ticketId", ticketId);
            addMessage.put("message", "Jde o program XYZ.");
            jsonTicket = gson.toJson(addMessage);

            webTarget = client.target(url).path(ADD_MESSAGE_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Pridana poznamka k tiketu");
        ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // nastaveni priority na vysokou ///////////////////////////////////
            Map<String, Object> priorityUpdate = new HashMap<>();
            priorityUpdate.put("system", system);
            priorityUpdate.put("ticketType", "Incident");
            priorityUpdate.put("ticketId", ticketId);
            priorityUpdate.put("priority", "high");
            jsonTicket = gson.toJson(priorityUpdate);

            webTarget = client.target(url).path(UPDATE_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Priorita tiketu nastavena na high");
        ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // znovuotevreni tiketu ////////////////////////////////////////////////
            Map<String, Object> reopen = new HashMap<>();
            reopen.put("system", system);
            reopen.put("ticketType", "Incident");
            reopen.put("ticketId", ticketId);
            reopen.put("status", "Open");
            reopen.put("note", "V poradku. Pokracuji v reseni.");
            jsonTicket = gson.toJson(reopen);

            webTarget = client.target(url).path(REOPEN_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Tiket znovuotevren (pokracovani prace)");
        ////////////////////////////////////////////////////////////////////////        

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // vyreseni tiketu s informaci o restartu sluzby ////////////////////////////////////////////////
            Map<String, Object> resolved = new HashMap<>();
            resolved.put("system", system);
            resolved.put("ticketType", "Incident");
            resolved.put("ticketId", ticketId);
            resolved.put("status", "Resolved");
            resolved.put("note", "Problem je ve sluzbe XYZ, prosim restartujte tuto sluzbu a melo by byt vse v poradku. OPERATION_RESTART=XYZ");
            jsonTicket = gson.toJson(resolved);

            webTarget = client.target(url).path(RESOLVE_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Tiket vyresen.");
        ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // ziskani nazvu programu, ktery je nutne restartovat //////////////////
            Map<String, Object> get = new HashMap<>();
            get.put("system", system);
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
            List<Note> notes = getResult.getNotes();

            String[] noteArray = notes.get(0).getText().split("=");
            String serviceName = noteArray[1];

            System.out.println(">>> Ziskani informaci o reseni tiketu.");
        ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // uzavreni tiketu //////////////////
            Map<String, Object> closed = new HashMap<>();
            closed.put("system", system);
            closed.put("ticketType", "Incident");
            closed.put("ticketId", ticketId);
            closed.put("status", "Closed");
            closed.put("note", "Dekuji za reseni. Sluzbu " + serviceName + " jsem restartoval a vse je v poradku. Uzaviram.");
            jsonTicket = gson.toJson(closed);

            webTarget = client.target(url).path(CLOSE_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Tiket uzavren.");
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
    @Getter
    @Setter
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

    /**
     * Ziskani informaci o service desku.
     */
    @Getter
    @Setter
    private class SystemResponse {

        /**
         * Jmeno service desku.
         */
        private String systemName;
        /**
         * Typ service desku.
         */
        private String systemType;
        /**
         * Priznak, zda api umoznuje praci s problemy.
         */
        private boolean problemApi;
        /**
         * Priznak, zda api umoznuje praci s konfiguracnimi polozkami.
         */
        private boolean cmdbApi;
    }
}

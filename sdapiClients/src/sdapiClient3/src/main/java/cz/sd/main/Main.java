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
 * Ukazkovy klient aplikacniho rozhrani pro service desky. Testovaci scenar c. 3
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class Main {

    private static final String CREATE_TICKET_RESOURCE = "tickets/createTicket";
    private static final String ADD_MESSAGE_RESOURCE = "tickets/addMessage";
    private static final String RESOLVE_TICKET_RESOURCE = "tickets/resolveTicket";
    private static final String CLOSE_TICKET_RESOURCE = "tickets/closeTicket";
    private static final String GET_SYSTEM_RESOURCE = "systems/getSystem";
    private static final String LINK_PROBLEM_RESOURCE = "problems/linkIncident";

    // cas cekani mezi jednotlivymi kroky
    private static final int SLEEP_TIME = 1;
    // pokud je priznak nastaven, ceka se mezi kroky na stisknuti klavesy uzivatelem
    private static boolean debug = false;

    // id vytvorene konfiguracni polozky
    private static Long configItemId;
    // jmeno systemu
    private static String system;
    // url webovych sluzeb api
    private static String url;

    // priznak, zda se jedna o urcity service desk - kvuli nastaveni extra parametru apod.
    private static boolean isItop = false;
    private static boolean isAlvao = false;

    /**
     * Hlavni program scenare.
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
                    configItemId = 152L;
                    break;
                case "iTop":
                    isItop = true;
                    break;
                default:
                    System.out.println("Chybne zadany service desk");
                    System.exit(-1);
            }

            // vytvoreni incidentu 1 /////////////////////////
            Map<String, Object> create = new HashMap<>();
            create.put("system", system);
            create.put("ticketType", "Incident");
            create.put("subject", "Nefunguje tisk");
            create.put("description", "Nelze tisknout na tiskarne.");
            create.put("organization", "Demo");
            create.put("sla", "Incident SLA");
            create.put("service", "Incident management");
            create.put("status", "open");
            create.put("priority", "normal");
            if (isAlvao) {
                create.put("requesterName", "Pavel S");
                create.put("requesterId", "4");
                create.put("technicianName", "Pavel S");
                create.put("technicianId", "4");
            } else if (isItop) {
                create.put("requesterId", 1);
                create.put("technicianId", 1);
            }

            gson = new GsonBuilder().setPrettyPrinting().create();
            jsonTicket = gson.toJson(create);

            webTarget = client.target(url).path(CREATE_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            CreateResult createResult1 = gson.fromJson(wsResponse.readEntity(String.class), CreateResult.class);
            Long ticketId1 = createResult1.getTicketId();
            if (!createResult1.isSuccess()) {
                System.out.println(">>> Nastala chyba pri vytvareni tiketu");
                return;
            }
            System.out.println(">>> Vytvoren tiket (incident) c. " + ticketId1);
        ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // vytvoreni incidentu 2 /////////////////////////        
            create = new HashMap<>();
            create.put("system", system);
            create.put("ticketType", "Incident");
            create.put("subject", "Nemuzu vytisknout projekt");
            create.put("description", "Snazil jsem se vytisknout projekt, ale nic se nedeje.");
            create.put("organization", "Demo");
            create.put("sla", "Incident SLA");
            create.put("service", "Incident management");
            create.put("status", "open");
            create.put("priority", "normal");
            if (isAlvao) {
                create.put("requesterName", "Pavel S");
                create.put("requesterId", "4");
                create.put("technicianName", "Pavel S");
                create.put("technicianId", "4");
            } else if (isItop) {
                create.put("requesterId", 1);
                create.put("technicianId", 1);
            }

            jsonTicket = gson.toJson(create);

            webTarget = client.target(url).path(CREATE_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            CreateResult createResult2 = gson.fromJson(wsResponse.readEntity(String.class), CreateResult.class);
            Long ticketId2 = createResult2.getTicketId();
            if (!createResult2.isSuccess()) {
                System.out.println(">>> Nastala chyba pri vytvareni tiketu");
                return;
            }
            System.out.println(">>> Vytvoren tiket (incident) c. " + ticketId2);
        ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // vytvoreni incidentu 3 /////////////////////////
            create = new HashMap<>();
            create.put("system", system);
            create.put("ticketType", "Incident");
            create.put("subject", "Tiskarna netiskne");
            create.put("description", "Nefunguje tisk na tiskarne Laserjet. Prosim o rychle zprovozneni.");
            create.put("organization", "Demo");
            create.put("sla", "Incident SLA");
            create.put("service", "Incident management");
            create.put("status", "open");
            create.put("priority", "normal");
            if (isAlvao) {
                create.put("requesterName", "Pavel S");
                create.put("requesterId", "4");
                create.put("technicianName", "Pavel S");
                create.put("technicianId", "4");
            } else if (isItop) {
                create.put("requesterId", 1);
                create.put("technicianId", 1);
            }

            jsonTicket = gson.toJson(create);

            webTarget = client.target(url).path(CREATE_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            CreateResult createResult3 = gson.fromJson(wsResponse.readEntity(String.class), CreateResult.class);
            Long ticketId3 = createResult3.getTicketId();
            if (!createResult3.isSuccess()) {
                System.out.println(">>> Nastala chyba pri vytvareni tiketu");
                return;
            }
            System.out.println(">>> Vytvoren tiket (incident) c. " + ticketId3);
        ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // pridani poznamky o pricine incidentu ////////////////////////////////
            Map<String, Object> addMessage = new HashMap<>();
            addMessage.put("system", system);
            addMessage.put("ticketType", "Incident");
            addMessage.put("ticketId", ticketId3);
            addMessage.put("message", "Problemem je naplanovana uloha, ktera blokuje tisk. Tento incident souvisi s incidenty " + ticketId1 + " a " + ticketId2);
            jsonTicket = gson.toJson(addMessage);

            webTarget = client.target(url).path(ADD_MESSAGE_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Pridana poznamka k pricine incidentu");
        ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // vytvoreni problemu //////////////////////////////////////////////////            
            create = new HashMap<>();
            create.put("system", system);
            create.put("ticketType", "Problem");
            create.put("subject", "Naplanovana uloha blokuje tisk");
            create.put("description", "Bylo zjisteno, ze na urcity cas je naplanovana uloha, ktera znemoznuje tisk dokumentu. Ulohu bude treba naplanovat na jiny cas.");
            create.put("organization", "Demo");
            create.put("sla", "Problem SLA");
            create.put("service", "Problem management");
            create.put("status", "open");
            create.put("priority", "high");
            if (isAlvao) {
                create.put("requesterName", "Pavel S");
                create.put("requesterId", "4");
                create.put("technicianName", "Pavel S");
                create.put("technicianId", "4");
            } else if (isItop) {
                create.put("requesterId", 1);
                create.put("technicianId", 1);
            }

            jsonTicket = gson.toJson(create);

            webTarget = client.target(url).path(CREATE_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            CreateResult createResult4 = gson.fromJson(wsResponse.readEntity(String.class), CreateResult.class);
            Long problemId = createResult4.getTicketId();
            if (!createResult4.isSuccess()) {
                System.out.println(">>> Nastala chyba pri vytvareni tiketu");
                return;
            }
            System.out.println(">>> Vytvoren tiket (problem) c. " + problemId);
        ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // slinkovani problemu a incidentu c. 1 ////////////////////////////////
            Map<String, Object> link = new HashMap<>();
            link.put("system", system);
            link.put("incidentId", ticketId1);
            link.put("problemId", problemId);
            jsonTicket = gson.toJson(link);

            webTarget = client.target(url).path(LINK_PROBLEM_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Propojeni incidentu " + ticketId1 + " s problemem.");
        ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // slinkovani problemu a incidentu c. 2 ////////////////////////////////
            link = new HashMap<>();
            link.put("system", system);
            link.put("incidentId", ticketId2);
            link.put("problemId", problemId);
            jsonTicket = gson.toJson(link);

            webTarget = client.target(url).path(LINK_PROBLEM_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Propojeni incidentu " + ticketId2 + " s problemem.");
        ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // slinkovani problemu a incidentu c. 3 ////////////////////////////////
            link = new HashMap<>();
            link.put("system", system);
            link.put("incidentId", ticketId3);
            link.put("problemId", problemId);
            jsonTicket = gson.toJson(link);

            webTarget = client.target(url).path(LINK_PROBLEM_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Propojeni incidentu " + ticketId3 + " s problemem.");
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
            resolved.put("ticketType", "Problem");
            resolved.put("ticketId", problemId);
            resolved.put("status", "Resolved");
            resolved.put("note", "Uloha byla naplanova na jiny cas, ktery nebude blokovat tisk uzivatelum.");
            jsonTicket = gson.toJson(resolved);

            webTarget = client.target(url).path(RESOLVE_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Problem vyresen.");
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
            closed.put("ticketType", "Problem");
            closed.put("ticketId", problemId);
            closed.put("status", "Closed");
            closed.put("note", "Uzaviram problem. Vse je vyreseno.");
            jsonTicket = gson.toJson(closed);

            webTarget = client.target(url).path(CLOSE_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Problem uzavren.");
        ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // uzavreni incidentu 1 //////////////////
            closed = new HashMap<>();
            closed.put("system", system);
            closed.put("ticketType", "Incident");
            closed.put("ticketId", ticketId1);
            closed.put("status", "Closed");
            closed.put("note", "Pricina incidentu je odstranena. Incident se jiz nevyskytuje. Uzaviram.");
            jsonTicket = gson.toJson(closed);

            webTarget = client.target(url).path(CLOSE_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Incident " + ticketId1 + "  uzavren.");
        ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // uzavreni incidentu 2 //////////////////
            closed = new HashMap<>();
            closed.put("system", system);
            closed.put("ticketType", "Incident");
            closed.put("ticketId", ticketId2);
            closed.put("status", "Closed");
            closed.put("note", "Pricina incidentu je odstranena. Incident se jiz nevyskytuje. Uzaviram.");
            jsonTicket = gson.toJson(closed);

            webTarget = client.target(url).path(CLOSE_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Incident " + ticketId2 + "  uzavren.");
        ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // uzavreni incidentu 3 //////////////////
            closed = new HashMap<>();
            closed.put("system", system);
            closed.put("ticketType", "Incident");
            closed.put("ticketId", ticketId3);
            closed.put("status", "Closed");
            closed.put("note", "Pricina incidentu je odstranena. Incident se jiz nevyskytuje. Uzaviram.");
            jsonTicket = gson.toJson(closed);

            webTarget = client.target(url).path(CLOSE_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Incident " + ticketId3 + "  uzavren.");
            ////////////////////////////////////////////////////////////////////////
        } catch (Exception ex) {
            System.out.println("Pri behu programu nastala chyba.");
            System.exit(-1);
        }

    }
    
    /**
     * Nacte z konfiguracniho souboru url webovych sluzeb.
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
        /**
         * Konfiguracni polozka.
         */
        private Long configItemId;
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
     * Ziskani CI.
     */
    @Getter
    @Setter
    private static class GetCiResult {

        /**
         * Nazev konfiguracni polozky.
         */
        private String name;
        /**
         * Popis konfiguracni polozky.
         */
        private String description;
        /**
         * Id konfiguracni polozky.
         */
        private long id;
        /**
         * Nazev typu konfiguracni polozky.
         */
        private String typeName;
        /**
         * Id typu konfiguracni polozky.
         */
        private Long typeId;
        /**
         * Datum vytvoreni.
         */
        private String createdDate;
        /**
         * Nazev produktu.
         */
        private String productName;
        /**
         * Stav pouzivani.
         */
        private String status;
        /**
         * Dopad na business.
         */
        private String impact;
        /**
         * Mapa detailu.
         */
        private Map<String, String> details;
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

package cz.sd.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

/**
 * Ukazkovy klient aplikacniho rozhrani pro service desky. Testovaci scenar c. 4
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class Main {

    private static final String GET_TICKET_RESOURCE = "tickets/getTicket";
    private static final String CREATE_TICKET_RESOURCE = "tickets/createTicket";
    private static final String RESOLVE_TICKET_RESOURCE = "tickets/resolveTicket";
    private static final String UPDATE_TICKET_RESOURCE = "tickets/updateTicket";
    private static final String CLOSE_TICKET_RESOURCE = "tickets/closeTicket";
    private static final String GET_SYSTEM_RESOURCE = "systems/getSystem";
    private static final String UPDATE_CI_RESOURCE = "cmdb/updateCi";

    // cas cekani mezi jednotlivymi kroky
    private static final int SLEEP_TIME = 1;
    // pokud je priznak nastaven, ceka se mezi kroky na stisknuti klavesy uzivatelem
    private static boolean debug = false;

    //private static final String system = "Alvao";
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
                    configItemId = 150L;
                    break;
                case "iTop":
                    isItop = true;
                    if (configureItop() != 0) {
                        System.out.println("Chyba pri konfiguraci iTop systemu");
                        System.exit(-1);
                    }
                    break;
                default:
                    System.out.println("Chybne zadany service desk");
                    System.exit(-1);
            }

            // vytvoreni incidentu s konfiguracni polozkou /////////////////////////
            Map<String, Object> create = new HashMap<>();
            create.put("system", system);
            create.put("ticketType", "Incident");
            create.put("subject", "Vadny tisk");
            create.put("description", "Tiskarna spatne tiskne. Jakoby dochazela barva, ale ta je v poradku.");
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
            create.put("configItemId", configItemId);

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

            // ziskani informaci o tiketu a id konfiguracni polozky ////////////////
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
            Long ciId = getResult.getConfigItemId();

            System.out.println(">>> Ziskani informaci o tiketu.");
        ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // vytvoreni tiketu typu zmena /////////////////////////////////////////
            create = new HashMap<>();
            create.put("system", system);
            create.put("ticketType", "Change");
            create.put("subject", "Pozadavek na vymenu tiskarny");
            create.put("description", "Tiskarna toho ma uz za sebou dost. Bude treba ji vymenit.");
            create.put("organization", "Demo");
            create.put("sla", "Change SLA");
            create.put("service", "Change management");
            create.put("status", "analysis");
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
            create.put("configItemId", ciId);

            jsonTicket = gson.toJson(create);

            webTarget = client.target(url).path(CREATE_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            createResult = gson.fromJson(wsResponse.readEntity(String.class), CreateResult.class);
            Long changeId = createResult.getTicketId();
            if (!createResult.isSuccess()) {
                System.out.println(">>> Nastala chyba pri vytvareni tiketu");
                return;
            }
            System.out.println(">>> Vytvoren tiket (zmena) c. " + changeId);
        ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // nastaveni stavu na planovani ////////////////////////////////////////
            Map<String, Object> statusUpdate = new HashMap<>();
            statusUpdate.put("system", system);
            statusUpdate.put("ticketType", "Change");
            statusUpdate.put("ticketId", changeId);
            statusUpdate.put("status", "planning");
            jsonTicket = gson.toJson(statusUpdate);

            webTarget = client.target(url).path(UPDATE_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Stav tiketu " + changeId + " nastaven na planning");
        ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // nastaveni stavu na implementaci /////////////////////////////////////
            statusUpdate = new HashMap<>();
            statusUpdate.put("system", system);
            statusUpdate.put("ticketType", "Change");
            statusUpdate.put("ticketId", changeId);
            statusUpdate.put("status", "implementation");
            jsonTicket = gson.toJson(statusUpdate);

            webTarget = client.target(url).path(UPDATE_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Stav tiketu " + changeId + " nastaven na implementation");
        ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // nastaveni stavu na monitoring /////////////////////////////////////
            statusUpdate = new HashMap<>();
            statusUpdate.put("system", system);
            statusUpdate.put("ticketType", "Change");
            statusUpdate.put("ticketId", changeId);
            statusUpdate.put("status", "monitoring");
            jsonTicket = gson.toJson(statusUpdate);

            webTarget = client.target(url).path(UPDATE_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Stav tiketu " + changeId + " nastaven na monitoring");
        ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // vyreseni tiketu /////////////////////////////////////////////////////
            Map<String, Object> resolved = new HashMap<>();
            resolved.put("system", system);
            resolved.put("ticketType", "Change");
            resolved.put("ticketId", changeId);
            resolved.put("status", "Resolved");
            resolved.put("note", "Harddisk byl vymenen. Vyreseno.");
            jsonTicket = gson.toJson(resolved);

            webTarget = client.target(url).path(RESOLVE_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Tiket " + changeId + " vyresen.");
        ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // uzavreni tiketu /////////////////////////////////////////////////////
            Map<String, Object> closed = new HashMap<>();
            closed.put("system", system);
            closed.put("ticketType", "Change");
            closed.put("ticketId", changeId);
            closed.put("status", "Closed");
            closed.put("note", "V poradku. Uzaviram.");
            jsonTicket = gson.toJson(closed);

            webTarget = client.target(url).path(CLOSE_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Tiket " + changeId + " uzavren.");
        ////////////////////////////////////////////////////////////////////////

            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }

            // uzavreni incidentu //////////////////////////////////////////////////
            closed = new HashMap<>();
            closed.put("system", system);
            closed.put("ticketType", "Incident");
            closed.put("ticketId", ticketId);
            closed.put("status", "Closed");
            closed.put("note", "V poradku. Uzaviram.");
            jsonTicket = gson.toJson(closed);

            webTarget = client.target(url).path(CLOSE_TICKET_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Tiket " + ticketId + " uzavren.");
            ////////////////////////////////////////////////////////////////////////
            
            if (debug) {
                System.out.println("Pro pokracovani stisknete Enter...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            } else {
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            }
            
            // uprava konfiguracni polozky - uprava velikosti pameti //////////////////
            Map<String, Object> updateCi = new HashMap<>();
            updateCi.put("system", system);
            updateCi.put("configItemId", ciId);
            List<String> list = new ArrayList<>();
            if (isItop) {
                list.add("status");
                list.add("obsolete");
            } else if (isAlvao) {
                list.add("Stav");
                list.add("zastarale");
            }
            updateCi.put("details", list);
            jsonTicket = gson.toJson(updateCi);

            webTarget = client.target(url).path(UPDATE_CI_RESOURCE);
            wsResponse = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

            if (wsResponse.getStatus() != 200) {
                System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                System.exit(-1);
            }

            System.out.println(">>> Konfiguracni polozka upravena - stav tiskarny nastaven mimo produkci (zastarale)");
            ////////////////////////////////////////////////////////////////////

            // pokud slo o Alvao, nastavime pamet na puvodni hodnotu ///////////////
            if (isAlvao) {
                if (debug) {
                    System.out.println("Pro pokracovani stisknete Enter...");
                    Scanner scanner = new Scanner(System.in);
                    scanner.nextLine();
                } else {
                    TimeUnit.SECONDS.sleep(SLEEP_TIME);
                }

                updateCi = new HashMap<>();
                updateCi.put("system", system);
                updateCi.put("configItemId", ciId);
                list = new ArrayList<>();
                list.add("Stav");
                list.add("V produkci");
                updateCi.put("details", list);
                jsonTicket = gson.toJson(updateCi);

                webTarget = client.target(url).path(UPDATE_CI_RESOURCE);
                wsResponse = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));

                if (wsResponse.getStatus() != 200) {
                    System.out.println("Chyba pri komunikaci s api. Http chyba " + wsResponse.getStatus());
                    System.exit(-1);
                }

                System.out.println(">>> Nastaveni stavu konfiguracni polozky na hodnotu pred spustenim klienta.");
            }
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
     * Nakonfiguruje iTop cmdb databazi pro potreby scenare. Vytvori jednu
     * konfiguracni polozku typu Printer.
     *
     * @return 0 v pripade uspechu, -1 v pripade nejake chyby
     */
    public static int configureItop() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("api_config.properties");
            prop.load(input);
        } catch (IOException ex) {
            return -1;
        }

        String endpoint = prop.getProperty("itop.endpoint");
        String login = prop.getProperty("itop.login");
        String password = prop.getProperty("itop.password");

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(HttpAuthenticationFeature.basic(login, password));

        Client client = ClientBuilder.newClient(clientConfig);

        String jsonTicket = "json_data={\n"
                + "   \"operation\": \"core/create\",\n"
                + "   \"comment\": \"Vytvoreni CI skrze rest api\",\n"
                + "   \"class\": \"Printer\",\n"
                + "   \"fields\":\n"
                + "   {\n"
                + "   	 \"name\": \"Test_tiskarna\",\n"
                + "         \"description\": \"Testovaci tiskarna pro potreby ServiceDeskApi.\",\n"
                + "         \"org_id\": \"3\",\n"
                + "         \"organization_name\": \"Demo\",\n"
                + "         \"business_criticity\": \"low\",\n"
                + "         \"move2production\": \"2016-05-18\",\n"
                + "         \"serialnumber\": \"2563\",\n"
                + "         \"location_id\": \"2\",\n"
                + "         \"location_name\": \"Grenoble\",\n"
                + "         \"status\": \"production\",\n"
                + "         \"brand_id\": \"2\",\n"
                + "         \"asset_number\": \"12\",\n"
                + "         \"purchase_date\": \"2016-05-06\",\n"
                + "         \"end_of_warranty\": \"2019-05-09\"\n"
                + "   }\n"
                + "}";

        WebTarget webTarget = client.target(endpoint);
        Response wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_FORM_URLENCODED));

        if (wsResponse.getStatus() != 200) {
            return -1;
        }

        String response = wsResponse.readEntity(String.class);

        // z odpovedi ziskame id konfiguracni polozky a nastavime globalni promennou
        Pattern pattern = Pattern.compile("\"Printer::(\\d+)\"");
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            configItemId = Long.valueOf(matcher.group(1));
        }
        if (configItemId == null) {
            return -1;
        }

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

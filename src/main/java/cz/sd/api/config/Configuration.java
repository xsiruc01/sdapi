package cz.sd.api.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Konfigurace sd systemu. Trida nacita konfigurace ze souboru a zpristupnuje
 * dostupne systemy, pokud maji v poradku konfiguraci.
 *
 * @author Pavel Širůček
 */
public class Configuration {

    /**
     * Mapa obsahujici konfigurace service desku pod jejich jmeny.
     */
    private final Map<String, SdSystem> systemsConfigs;

    /**
     * Vytvori instanci konfigurace ze souboru.
     *
     * @param filePath Jmeno souboru.
     * @throws IOException v pripade nejakeho problemu s konfiguracnim souborem.
     */
    public Configuration(String filePath) throws IOException {
        String configs = readFile(filePath);
        Type collectionType = new TypeToken<Collection<JsonSdSystem>>() {
        }.getType();
        Gson gson = new Gson();
        Collection<JsonSdSystem> jsonSdSystems = gson.fromJson(configs, collectionType);
        this.systemsConfigs = convertConfig(jsonSdSystems);
    }

    /**
     * Vytvori instanci konfigurace ze souboru, ktery hleda v projektu.
     *
     * @throws IOException v pripade nejakeho problemu s konfiguracnim souborem.
     */
    public Configuration() throws IOException {
        this("src/main/resources/sd_config.json");
    }

    /**
     * Vrati konfiguracni udaje o sd systemu podle zadaneho jmena.
     *
     * @param name Jmeno/identifikator service desku.
     * @return Konfiguracni udaje sd systemu.
     */
    public SdSystem getSystem(String name) {
        return this.systemsConfigs.get(name);
    }

    /**
     * Vraci mapu konfigracnich udaju sd systemu.
     *
     * @return Mapa konfiguracnich udaju sd systemu.
     */
    public Map<String, SdSystem> getAll() {
        return this.systemsConfigs;
    }

    /**
     * Prevede konfigurace ve formatu JSON, nactene z konfiguracniho souboru do
     * mapy, kde je vzdy pod jmenem service desku prislusna konfigurace. Do
     * konzole jsou vypsany v poradku nactene konfigurace systemu.
     *
     * @param jsonSdSystems Konfigurace ve formatu JSON.
     * @return Mapa konfiguraci.
     */
    private Map<String, SdSystem> convertConfig(Collection<JsonSdSystem> jsonSdSystems) {
        Map<String, SdSystem> systems = new HashMap<>();
        System.out.println("\nNacitam konfigurace...");
        int i = 1;
        for (JsonSdSystem sys : jsonSdSystems) {
            String type = sys.getType().toLowerCase();
            switch (type) {
                case "alvao":
                    Alvao alvao = new Alvao();
                    alvao.setDbstring(sys.getDbString());
                    alvao.setName(sys.getName());
                    systems.put(sys.getName(), alvao);
                    System.out.println(i + ": " + sys.getName() + " (ALVAO)");
                    break;
                case "itop":
                    Itop itop = new Itop();
                    itop.setEndpoint(sys.getEndpoint());
                    itop.setLogin(sys.getLogin());
                    itop.setPassword(sys.getPassword());
                    itop.setName(sys.getName());
                    systems.put(sys.getName(), itop);
                    System.out.println(i + ": " + sys.getName() + " (iTop)");
                    break;
                case "freshservice":
                case "fresh service":
                    Freshservice freshservice = new Freshservice();
                    freshservice.setEndpoint(sys.getEndpoint());
                    freshservice.setLogin(sys.getLogin());
                    freshservice.setPassword(sys.getPassword());
                    freshservice.setName(sys.getName());
                    systems.put(sys.getName(), freshservice);
                    System.out.println(i + ": " + sys.getName() + " (Freshservice)");
                    break;
                case "manageengine":
                case "manage engine":
                    ManageEngine manageEngine = new ManageEngine();
                    manageEngine.setEndpoint(sys.getEndpoint());
                    manageEngine.setApiKey(sys.getApiKey());
                    manageEngine.setName(sys.getName());
                    systems.put(sys.getName(), manageEngine);
                    System.out.println(i + ": " + sys.getName() + " (ManageEngine)");
                    break;
                case "webhelpdesk":
                case "web help desk":
                    WebHelpDesk webhelpdesk = new WebHelpDesk();
                    webhelpdesk.setEndpoint(sys.getEndpoint());
                    webhelpdesk.setLogin(sys.getLogin());
                    webhelpdesk.setPassword(sys.getPassword());
                    webhelpdesk.setName(sys.getName());
                    systems.put(sys.getName(), webhelpdesk);
                    System.out.println(i + ": " + sys.getName() + " (Web Help Desk)");
                    break;
                default:
                    return null;
            }
            i++;
        }
        return systems;
    }

    /**
     * Precte cely soubor a vrati jeho obsah.
     *
     * @param name Jmeno souboru.
     * @return Retezec s obsahem souboru.
     * @throws IOException v pripade nejakeho problemu se souborem.
     */
    public static String readFile(String name) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(name)), StandardCharsets.UTF_8);
        return text;
    }
}

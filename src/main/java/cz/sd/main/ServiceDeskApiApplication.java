package cz.sd.main;

import cz.sd.api.config.Configuration;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main trida aplikace se spustitelnou metodou main.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ServiceDeskApiApplication {

    /**
     * Defaultni URI Grizzly HTTP serveru.
     */
    public static final String DEFAULT_URL = "http://localhost:8088/ServiceDeskApi/";
    /**
     * Prectena URI z properties souboru.
     */
    public static String URL;

    /**
     * Spusti Grizzly HTTP server.
     *
     * @return Instance HTTp serveru.
     */
    public static HttpServer startServer() {
        //Umisteni webovych sluzeb.
        final ResourceConfig rc = new ResourceConfig().packages("cz.sd.ws");

        if (URL == null) {
            URL = DEFAULT_URL;
        }
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(URL), rc);

    }

    /**
     * Hlavni metoda aplikace.
     *
     * @param args Argumenty prikazove radky.
     */
    public static void main(String[] args) {
        // HTTP server
        final HttpServer server = startServer();
        
        try {
            readProperties();
        } catch (IOException ex) {
            System.out.println(".properties soubor nenalezen, bude pouzita defaultni URL: [" + DEFAULT_URL + "]");
        }
        
        try {
            ServiceDeskApiProvider.create(new Configuration("sd_config.json"));
            System.out.println(String.format("Webove sluzby ServiceDeskApi jsou spustene.\nWADL je dostupny na "
                + "%sapplication.wadl\nPro ukonceni aplikace stisknete libovolnou klavesu...", URL));
            System.in.read();
            server.shutdownNow();
        } catch (IOException ex) {
            System.err.println("Chyba pri nacitani konfiguracniho souboru. Soubor sd_config.json musi byt ve stejnem adresari jako aplikace.");
            System.exit(-1);
        }
    }
    
    /**
     * Precte properties soubor a nastavi URL.
     * @throws IOException V pripade problemu se stenim souboru.
     */
    private static void readProperties() throws IOException {
        Properties prop = new Properties();
        InputStream input = null;

        input = new FileInputStream("api_config.properties");
        prop.load(input);

        ServiceDeskApiApplication.URL = prop.getProperty("url");
    }
}

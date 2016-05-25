package cz.sd.api;

import cz.sd.api.IServiceDeskApi;
import cz.sd.api.alvao.AlvaoClient;
import cz.sd.api.config.Alvao;
import cz.sd.api.config.Configuration;
import cz.sd.api.config.Freshservice;
import cz.sd.api.config.Itop;
import cz.sd.api.config.ManageEngine;
import cz.sd.api.config.SdSystem;
import cz.sd.api.config.WebHelpDesk;
import cz.sd.api.freshservice.FreshserviceClient;
import cz.sd.api.itop.ItopClient;
import cz.sd.api.manageengine.ManageEngineClient;
import cz.sd.api.webhelpdesk.WebHelpDeskClient;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;

/**
 * Predek pro testy.
 * @author Pavel Sirucek (xsiruc01)
 */
public abstract class AbstractServiceDeskTest {
    protected Map<String, IServiceDeskApi> clients = new HashMap<>();
    
    @Before
    public void initialize() {
        try {
            Configuration config = new Configuration("src/test/resources/sd_config.json");
            
            Map<String, SdSystem> map = config.getAll();
            
            for (Map.Entry<String, SdSystem> entrySet : map.entrySet()) {
                String name = entrySet.getKey();
                SdSystem system = entrySet.getValue();
                
                IServiceDeskApi client;
                if (system instanceof Alvao) {
                    Alvao sys = (Alvao) system;
                    client = new AlvaoClient(sys.getDbstring(), sys.getName());
                } else if (system instanceof Freshservice) {
                    Freshservice sys = (Freshservice) system;
                    client = new FreshserviceClient(sys.getEndpoint(), sys.getLogin(), sys.getPassword(), sys.getName());
                } else if (system instanceof Itop) {
                    Itop sys = (Itop) system;
                    client = new ItopClient(sys.getEndpoint(), sys.getLogin(), sys.getPassword(), sys.getName());
                } else if (system instanceof ManageEngine) {
                    ManageEngine sys = (ManageEngine) system;
                    client = new ManageEngineClient(sys.getEndpoint(), sys.getApiKey(), sys.getName());
                } else {// WHD 
                    WebHelpDesk sys = (WebHelpDesk) system;
                    client = new WebHelpDeskClient(sys.getEndpoint(), sys.getLogin(), sys.getPassword(), sys.getName());
                }
                this.clients.put(name, client);
                
            }
            
        } catch (IOException ex) {
            Logger.getLogger(AbstractServiceDeskTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

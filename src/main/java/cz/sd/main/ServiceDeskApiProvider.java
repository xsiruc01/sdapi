package cz.sd.main;

import cz.sd.api.IConfigurationItemApi;
import cz.sd.api.IServiceDeskApi;
import cz.sd.api.IServiceDeskProblemApi;
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
import lombok.Getter;

/**
 * Provider api pro service desky.
 *
 * @author Pavel Širůček
 */
public class ServiceDeskApiProvider {

    /**
     * Konfigurace service desku.
     */
    @Getter
    private final Configuration config;
    /**
     * Provider. Singleton.
     */
    private static ServiceDeskApiProvider provider = null;

    /**
     * Vytvori instanci providera.
     *
     * @param config Konfigurace service desku.
     */
    public static void create(Configuration config) {
        provider = new ServiceDeskApiProvider(config);
    }

    /**
     * Privatne vytvori instanci providera.
     *
     * @param config Konfigurace service desku.
     */
    private ServiceDeskApiProvider(Configuration config) {
        this.config = config;
    }

    /**
     * Vrati klienta pro pozadovany service desk.
     *
     * @param name Nazev service desku.
     * @return Api pro pristup ke konkretnimu systemu.
     */
    public static IServiceDeskApi getApi(String name) {
        if (provider == null) {
            return null; // instance neni vytvorena
        }

        SdSystem system = provider.getConfig().getSystem(name);

        if (system == null) {
            return null; // system zadaneho jmena neexistuje
        }

        IServiceDeskApi sd;
        if (system instanceof Alvao) {
            Alvao sys = (Alvao) system;
            sd = new AlvaoClient(sys.getDbstring(), sys.getName());
        } else if (system instanceof Freshservice) {
            Freshservice sys = (Freshservice) system;
            sd = new FreshserviceClient(sys.getEndpoint(), sys.getLogin(), sys.getPassword(), sys.getName());
        } else if (system instanceof Itop) {
            Itop sys = (Itop) system;
            sd = new ItopClient(sys.getEndpoint(), sys.getLogin(), sys.getPassword(), sys.getName());
        } else if (system instanceof ManageEngine) {
            ManageEngine sys = (ManageEngine) system;
            sd = new ManageEngineClient(sys.getEndpoint(), sys.getApiKey(), sys.getName());
        } else {// WHD 
            WebHelpDesk sys = (WebHelpDesk) system;
            sd = new WebHelpDeskClient(sys.getEndpoint(), sys.getLogin(), sys.getPassword(), sys.getName());
        }

        return sd;
    }
    
    public static IServiceDeskProblemApi getProblemApi(String name) {
        IServiceDeskApi sdApi = getApi(name);
        if (sdApi instanceof IServiceDeskProblemApi) {
            return (IServiceDeskProblemApi) sdApi;
        } else {
            return null;
        }            
    }
    
    public static IConfigurationItemApi getCiApi(String name) {
        IServiceDeskApi sdApi = getApi(name);
        if (sdApi instanceof IConfigurationItemApi) {
            return (IConfigurationItemApi) sdApi;
        } else {
            return null;
        }            
    }
}

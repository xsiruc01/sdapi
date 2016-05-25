package cz.sd.api.freshservice;

import cz.sd.api.freshservice.cmdb.json.FreshserviceCIResponse;
import cz.sd.api.freshservice.cmdb.json.FreshserviceCITypeResponse;
import cz.sd.api.freshservice.cmdb.FreshserviceCIConvertor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cz.sd.api.ConfigurationItem;
import cz.sd.api.IConfigurationItemApi;
import cz.sd.api.IServiceDeskApi;
import cz.sd.api.SdSystemType;
import cz.sd.api.ServiceDeskCommunicationException;
import cz.sd.api.ServiceDeskTicket;
import cz.sd.api.freshservice.cmdb.json.FreshserviceCIUpdateRequest;
import cz.sd.api.freshservice.convertor.FreshserviceTicketConvertor;
import cz.sd.api.freshservice.ticket.incident.json.FreshserviceHelpDeskTicket;
import cz.sd.api.ticket.TicketCreateData;
import cz.sd.api.freshservice.ticket.incident.json.FreshserviceIncidentCreateRequest;
import cz.sd.api.freshservice.ticket.incident.json.FreshserviceIncidentCreateResponse;
import cz.sd.api.freshservice.ticket.incident.json.FreshserviceIncidentUpdateRequest;
import cz.sd.api.freshservice.ticket.incident.json.FreshserviceMessage;
import cz.sd.api.ticket.CiUpdateData;
import cz.sd.api.ticket.TicketStatus;
import cz.sd.api.ticket.TicketType;
import cz.sd.api.ticket.TicketUpdateData;
import java.lang.reflect.Type;
import java.util.Collection;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

/**
 * Klient pro praci s FreshService systemem. FreshService ve free verzi
 * podporuje nasledujici ITIL procesy.
 * <ul>
 * <li>Incident management</li>
 * <li>Configuration management</li>
 * </ul>
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class FreshserviceClient implements IServiceDeskApi, IConfigurationItemApi {

    /**
     * Jersey REST Api klient.
     */
    private final Client client;
    /**
     * Endpoint webove sluzby.
     */
    private final String endpoint;
    
    /**
     * Nazev/identifikator service desku.
     */
    private final String systemName;

    /**
     * Konstruktor.
     * @param endpoint Endpoint webove sluzby FreshService.
     * @param login Prihlasovaci jmeno.
     * @param password Heslo.
     * @param systemName Nazev systemu.
     */
    public FreshserviceClient(String endpoint, String login, String password, String systemName) {
        this.endpoint = endpoint;
        
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(HttpAuthenticationFeature.basic(login, password));
        
        this.client = ClientBuilder.newClient(clientConfig);
        
        this.systemName = systemName;
    }

    
    @Override
    public Long createTicket(TicketCreateData createInfo) throws ServiceDeskCommunicationException {
        String resource = "/helpdesk/tickets";

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FreshserviceIncidentCreateRequest request = new FreshserviceIncidentCreateRequest(createInfo);
        if (createInfo.getConfigurationItem() != null) {
            ConfigurationItem ci = this.getCI(createInfo.getConfigurationItem());
            request.setAssociatedItemName(ci.getName());
        }
        String jsonTicket = gson.toJson(request);
        
        WebTarget webTarget = this.client.target(this.endpoint).path(resource);
        Response wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));     
        
        if (wsResponse.getStatus() != 200) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem Freshservice. HTTP kod: " + wsResponse.getStatus());
        }
        FreshserviceIncidentCreateResponse response = gson.fromJson(wsResponse.readEntity(String.class), FreshserviceIncidentCreateResponse.class);
        Long id = response.getCreatedTicketId();
        
        
        return id;
    }
    
    
    @Override
    public ServiceDeskTicket getTicket(long id, TicketType type) throws ServiceDeskCommunicationException {
        String resource = "/helpdesk/tickets/" + id + ".json";
        
        WebTarget webTarget = this.client.target(this.endpoint).path(resource);
        Response wsResponse = webTarget.request(MediaType.APPLICATION_JSON).get(); 
        
        if (wsResponse.getStatus() != 200) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem Freshservice. HTTP kod: " + wsResponse.getStatus());
        }
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FreshserviceHelpDeskTicket response = gson.fromJson(wsResponse.readEntity(String.class), FreshserviceHelpDeskTicket.class);
        
        ServiceDeskTicket ticket = FreshserviceTicketConvertor.convertToTicket(response, this.systemName);
        
        return ticket;
    }
    
    
    @Override
    public boolean updateTicket(TicketUpdateData data) throws ServiceDeskCommunicationException {
        String resource = "/helpdesk/tickets/" + data.getTicketId() + ".json";
        
        FreshserviceIncidentUpdateRequest updateRequest = new FreshserviceIncidentUpdateRequest(data);
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonUpdate = gson.toJson(updateRequest);
        
        WebTarget webTarget = this.client.target(this.endpoint).path(resource);
        Response wsResponse = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(jsonUpdate, MediaType.APPLICATION_JSON));  
        
        if (wsResponse.getStatus() != 200) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem Freshservice. HTTP kod: " + wsResponse.getStatus());
        }
        
        return true;
    }

    
    @Override
    public boolean resolveTicket(long id, String note, TicketType type) throws ServiceDeskCommunicationException {
        TicketUpdateData updateData = new TicketUpdateData(id, null, null, null, TicketStatus.RESOLVED, null);
        this.updateTicket(updateData);
        if (note != null && !note.isEmpty()) {
            this.addMessage(id, note, type);
        }
        
        return true;
    }
    
    
    @Override
    public boolean closeTicket(long id, String note, TicketType type) throws ServiceDeskCommunicationException {
        TicketUpdateData updateData = new TicketUpdateData(id, null, null, null, TicketStatus.CLOSED, null);
        this.updateTicket(updateData);
        if (note != null && !note.isEmpty()) {
            this.addMessage(id, note, type);
        }
        
        return true;
    }
    
    @Override
    public boolean suspendTicket(long id, String note, TicketType type) throws ServiceDeskCommunicationException {
        TicketUpdateData updateData = new TicketUpdateData(id, null, null, null, TicketStatus.ON_HOLD, null);
        this.updateTicket(updateData);
        if (note != null && !note.isEmpty()) {
            this.addMessage(id, note, type);
        }
        
        return true;
    }
    
    @Override
    public boolean openTicket(long id, String note, TicketType type) throws ServiceDeskCommunicationException {
        TicketUpdateData updateData = new TicketUpdateData(id, null, null, null, TicketStatus.OPEN, null);
        this.updateTicket(updateData);
        if (note != null && !note.isEmpty()) {
            this.addMessage(id, note, type);
        }
        
        return true;
    }


    
    @Override
    public boolean deleteTicket(long id, TicketType type) throws ServiceDeskCommunicationException {
        String resource = "/helpdesk/tickets/" + id + ".json";
                        
        WebTarget webTarget = this.client.target(this.endpoint).path(resource);
        Response wsResponse = webTarget.request().delete(); 
        
        if (wsResponse.getStatus() != 200) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem Freshservice. HTTP kod: " + wsResponse.getStatus());
        }
        
        return true;
    }

    @Override
    public boolean addMessage(long id, String note, TicketType type) throws ServiceDeskCommunicationException {
        
        String resource = "/helpdesk/tickets/" + id + "/conversations/note.json";
        FreshserviceMessage fsNote = new FreshserviceMessage(note, false);
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonNote = gson.toJson(fsNote);
        
        WebTarget webTarget = this.client.target(this.endpoint).path(resource);
        Response wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonNote, MediaType.APPLICATION_JSON)); 
        
        if (wsResponse.getStatus() != 200) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem Freshservice. HTTP kod: " + wsResponse.getStatus());
        }        
        
        return true;       
    }

    
    public void getCITypes() throws ServiceDeskCommunicationException {
        String resource = "/cmdb/ci_types.json";
        
        WebTarget webTarget = this.client.target(this.endpoint).path(resource);
        Response wsResponse = webTarget.request(MediaType.APPLICATION_JSON).get(); 
        
        if (wsResponse.getStatus() != 200) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem Freshservice. HTTP kod: " + wsResponse.getStatus());
        }
        
        Type collectionType = new TypeToken<Collection<FreshserviceCITypeResponse>>() {}.getType();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Collection<FreshserviceCITypeResponse> response = gson.fromJson(wsResponse.readEntity(String.class), collectionType);
    }

    @Override
    public ConfigurationItem getCI(long id) throws ServiceDeskCommunicationException {
        String resource = "/cmdb/items/" + id + ".json" ;
         
        WebTarget webTarget = this.client.target(this.endpoint).path(resource);
        Response wsResponse = webTarget.request(MediaType.APPLICATION_JSON).get();
        
        if (wsResponse.getStatus() != 200) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem Freshservice. HTTP kod: " + wsResponse.getStatus());
        }
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FreshserviceCIResponse response = gson.fromJson(wsResponse.readEntity(String.class), FreshserviceCIResponse.class);
        
        ConfigurationItem ci = FreshserviceCIConvertor.convert(response);
        
        return ci;
    }

    @Override
    public boolean linkCiToTicket(long ciId, long ticketid, TicketType type) throws ServiceDeskCommunicationException {
        String resource = "/helpdesk/tickets/" + ticketid + ".json";
        
        FreshserviceIncidentUpdateRequest updateRequest = new FreshserviceIncidentUpdateRequest(new TicketUpdateData(-1, null, null, null, null, null));
        ConfigurationItem ci = this.getCI(ciId);
        updateRequest.setAssociatedItemName(ci.getName());
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonUpdate = gson.toJson(updateRequest);
        
        WebTarget webTarget = this.client.target(this.endpoint).path(resource);
        Response wsResponse = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(jsonUpdate, MediaType.APPLICATION_JSON));  
        
        if (wsResponse.getStatus() != 200) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem Freshservice. HTTP kod: " + wsResponse.getStatus());
        }
        
        return true;
    }
    
    @Override
    public boolean updateCI(CiUpdateData updateData) throws ServiceDeskCommunicationException {
        String resource = "/cmdb/items/" + updateData.getCiId() + ".json" ;
        
        // potrebujeme jmeno polozky
        String ciName = this.getCI(updateData.getCiId()).getName();
        updateData.setName(ciName);
        
        FreshserviceCIUpdateRequest updateRequest = new FreshserviceCIUpdateRequest(updateData);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonUpdate = gson.toJson(updateRequest);
         
        WebTarget webTarget = this.client.target(this.endpoint).path(resource);
        Response wsResponse = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(jsonUpdate, MediaType.APPLICATION_JSON));
        
        if (wsResponse.getStatus() != 200) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem Freshservice. HTTP kod: " + wsResponse.getStatus());
        }
        
        return true;
    }

    @Override
    public String getSystemName() {
        return this.systemName;
    }

    @Override
    public SdSystemType getSystemType() {
        return SdSystemType.FRESHSERVICE;
    }

}

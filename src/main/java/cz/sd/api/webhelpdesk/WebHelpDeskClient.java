package cz.sd.api.webhelpdesk;

import cz.sd.api.webhelpdesk.convertor.WebHelpDeskTicketConvertor;
import cz.sd.api.webhelpdesk.ticket.json.WebHelpDeskMessage;
import cz.sd.api.webhelpdesk.ticket.json.WebHelpDeskTicketGetResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.sd.api.IServiceDeskApi;
import cz.sd.api.SdSystemType;
import cz.sd.api.ServiceDeskCommunicationException;
import cz.sd.api.ServiceDeskTicket;
import cz.sd.api.ticket.TicketCreateData;
import cz.sd.api.ticket.TicketStatus;
import cz.sd.api.ticket.TicketType;
import cz.sd.api.ticket.TicketUpdateData;
import cz.sd.api.webhelpdesk.ticket.json.WebHelpDeskTicketCreateRequest;
import cz.sd.api.webhelpdesk.ticket.json.WebHelpDeskTicketCreateResponse;
import cz.sd.api.webhelpdesk.ticket.json.WebHelpDeskTicketUpdateRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

/**
 * Klient pro praci s WebHelpDesk systemem (SolarWinds). WebHelpDesk podporuje
 * nasledujici ITIL procesy.
 * <ul>
 * <li>Incident management</li>
 * </ul>
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class WebHelpDeskClient implements IServiceDeskApi {

    /**
     * Jersey REST Api klient.
     */
    private final Client client;
    /**
     * Endpoint webove sluzby.
     */
    private final String endpoint;
    /**
     * Prihlasovaci jmeno.
     */
    private final String username;
    /**
     * Prihlasovaci heslo.
     */
    private final String password;
    
    /**
     * Nazev/identifikator systemu.
     */
    private final String systemName;
    

    /**
     * Konstruktor.
     * @param endpoint Endpoint webove sluzby.
     * @param username Prihlasovaci jmeno.
     * @param password Heslo.
     */
    public WebHelpDeskClient(String endpoint, String username, String password, String systemName) {
        this.endpoint = endpoint;
        this.username = username;
        this.password = password;
        
        ClientConfig clientConfig = new ClientConfig();
        
        this.client = ClientBuilder.newClient(clientConfig);
        
        this.systemName = systemName;
    }
    

    @Override
    public Long createTicket(TicketCreateData createInfo) throws ServiceDeskCommunicationException {
        String resource = "/helpdesk/WebObjects/Helpdesk.woa/ra/Tickets";
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WebHelpDeskTicketCreateRequest request = new WebHelpDeskTicketCreateRequest(createInfo);
        String jsonTicket = gson.toJson(request);
        
        WebTarget webTarget = this.client.target(this.endpoint).path(resource).queryParam("username", this.username).queryParam("password", this.password);
        Response wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_JSON));  
        
        if (wsResponse.getStatus() != 201) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem Web Help Desk. HTTP kod: " + wsResponse.getStatus());
        }
        
        WebHelpDeskTicketCreateResponse response = gson.fromJson(wsResponse.readEntity(String.class), WebHelpDeskTicketCreateResponse.class);
        long id = response.getCreatedTicketId();
        
        return id;
    }
    

    @Override
    public ServiceDeskTicket getTicket(long id, TicketType type) throws ServiceDeskCommunicationException {
        String resource = "/helpdesk/WebObjects/Helpdesk.woa/ra/Tickets/" + id;
        
        WebTarget webTarget = this.client.target(this.endpoint).path(resource).queryParam("username", this.username).queryParam("password", this.password);
        Response wsResponse = webTarget.request(MediaType.APPLICATION_JSON).get(); 

        if (wsResponse.getStatus() != 200) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem Web Help Desk. HTTP kod: " + wsResponse.getStatus());
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WebHelpDeskTicketGetResponse response = gson.fromJson(wsResponse.readEntity(String.class), WebHelpDeskTicketGetResponse.class);
        
        ServiceDeskTicket ticket = WebHelpDeskTicketConvertor.convertToTicket(response, this.systemName);
        
        return ticket;
        
    }


    @Override
    public boolean updateTicket(TicketUpdateData data) throws ServiceDeskCommunicationException {
        String resource = "/helpdesk/WebObjects/Helpdesk.woa/ra/Tickets/" + data.getTicketId();
        
        WebHelpDeskTicketUpdateRequest updateRequest = new WebHelpDeskTicketUpdateRequest(data);
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonUpdate = gson.toJson(updateRequest);
        
        WebTarget webTarget = this.client.target(this.endpoint).path(resource).queryParam("username", this.username).queryParam("password", this.password);
        Response wsResponse = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(jsonUpdate, MediaType.APPLICATION_JSON)); 
        
        if (wsResponse.getStatus() != 200) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem Web Help Desk. HTTP kod: " + wsResponse.getStatus());
        }
        
        return true;
    }

    
    @Override
    public boolean resolveTicket(long id, String note, TicketType type) throws ServiceDeskCommunicationException {
        boolean result;        
        if (note != null && !note.isEmpty()) {
            result = this.addNoteWithNewStatus(new WebHelpDeskMessage(id, note, true, TicketStatus.RESOLVED));
        } else {
            TicketUpdateData data = new TicketUpdateData(id, null, null, null, TicketStatus.RESOLVED, null);
            result = this.updateTicket(data);
        }
        return result;
    }
    

    @Override
    public boolean closeTicket(long id, String note, TicketType type) throws ServiceDeskCommunicationException {
        boolean result;        
        if (note != null && !note.isEmpty()) {
            result = this.addNoteWithNewStatus(new WebHelpDeskMessage(id, note, true, TicketStatus.CLOSED));
        } else {
            TicketUpdateData data = new TicketUpdateData(id, null, null, null, TicketStatus.CLOSED, null);
            result = this.updateTicket(data);
        }
        return result;
    }
    

    @Override
    public boolean suspendTicket(long id, String note, TicketType type) throws ServiceDeskCommunicationException {
        boolean result;        
        if (note != null && !note.isEmpty()) {
            result = this.addNoteWithNewStatus(new WebHelpDeskMessage(id, note, true, TicketStatus.ON_HOLD));
        } else {
            TicketUpdateData data = new TicketUpdateData(id, null, null, null, TicketStatus.ON_HOLD, null);
            result = this.updateTicket(data);
        }
        return result;
    }
    

    @Override
    public boolean openTicket(long id, String note, TicketType type) throws ServiceDeskCommunicationException {
        boolean result;        
        if (note != null && !note.isEmpty()) {
            result = this.addNoteWithNewStatus(new WebHelpDeskMessage(id, note, true, TicketStatus.OPEN));
        } else {
            TicketUpdateData data = new TicketUpdateData(id, null, null, null, TicketStatus.OPEN, null);
            result = this.updateTicket(data);
        }
        return result;
    }
    

    @Override
    public boolean deleteTicket(long id, TicketType type) throws ServiceDeskCommunicationException {
        String resource = "/helpdesk/WebObjects/Helpdesk.woa/ra/Tickets/" + id;
        
        WebTarget webTarget = this.client.target(this.endpoint).path(resource).queryParam("username", this.username).queryParam("password", this.password);
        Response wsResponse = webTarget.request().delete(); 

        if (wsResponse.getStatus() != 200) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem Web Help Desk. HTTP kod: " + wsResponse.getStatus());
        }
        
        return true;
    }

    private boolean addNoteWithNewStatus(WebHelpDeskMessage note) throws ServiceDeskCommunicationException {
        String resource = "/helpdesk/WebObjects/Helpdesk.woa/ra/TechNotes";
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonNote = gson.toJson(note);
        
        WebTarget webTarget = this.client.target(this.endpoint).path(resource).queryParam("username", this.username).queryParam("password", this.password);
        Response wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonNote, MediaType.APPLICATION_JSON));  
        
        if (wsResponse.getStatus() != 201) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem Web Help Desk. HTTP kod: " + wsResponse.getStatus());
        }
        
        return true;
    }    
    
    @Override
    public boolean addMessage(long id, String note, TicketType type) throws ServiceDeskCommunicationException {
        String resource = "/helpdesk/WebObjects/Helpdesk.woa/ra/TechNotes";
        
        WebHelpDeskMessage sdNote = new WebHelpDeskMessage(id, note, true);
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonNote = gson.toJson(sdNote);
        
        WebTarget webTarget = this.client.target(this.endpoint).path(resource).queryParam("username", this.username).queryParam("password", this.password);
        Response wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonNote, MediaType.APPLICATION_JSON));
        
        if (wsResponse.getStatus() != 201) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem Web Help Desk. HTTP kod: " + wsResponse.getStatus());
        }
        
        return true;
    }

    @Override
    public String getSystemName() {
        return this.systemName;
    }

    @Override
    public SdSystemType getSystemType() {
        return SdSystemType.WEB_HELP_DESK;
    }

}

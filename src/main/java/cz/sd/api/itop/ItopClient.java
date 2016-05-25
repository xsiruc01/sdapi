package cz.sd.api.itop;

import cz.sd.api.itop.ticket.ItopTicketGetResponse;
import cz.sd.api.itop.ticket.ItopTicketCreateRequest;
import cz.sd.api.itop.ticket.problem.json.ItopProblemGetResponse;
import cz.sd.api.itop.ticket.incident.json.ItopIncidentCreateResponse;
import cz.sd.api.itop.ticket.json.ItopTicketGetRequest;
import cz.sd.api.itop.ticket.incident.json.ItopIncidentGetResponse;
import cz.sd.api.itop.ticket.incident.ItopIncidentUpdateData;
import cz.sd.api.itop.ticket.ItopTicketUpdateRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.sd.api.ConfigurationItem;
import cz.sd.api.IConfigurationItemApi;
import cz.sd.api.IServiceDeskApi;
import cz.sd.api.IServiceDeskProblemApi;
import cz.sd.api.SdSystemType;
import cz.sd.api.ServiceDeskTicket;
import cz.sd.api.ServiceDeskCommunicationException;
import cz.sd.api.itop.cmdb.ItopCIConvertor;
import cz.sd.api.itop.cmdb.json.ItopCIDefaultResponse;
import cz.sd.api.itop.cmdb.json.ItopCIDetailResponse;
import cz.sd.api.itop.cmdb.json.ItopCIGetRequest;
import cz.sd.api.itop.cmdb.json.ItopCIUpdateRequest;
import cz.sd.api.itop.cmdb.json.ItopLinkCiToTicketRequest;
import cz.sd.api.itop.convertor.ItopTicketConvertor;
import cz.sd.api.itop.ticket.change.ItopChangeUpdateData;
import cz.sd.api.itop.ticket.change.json.ItopChangeCreateResponse;
import cz.sd.api.itop.ticket.change.json.ItopChangeGetResponse;
import cz.sd.api.itop.ticket.problem.ItopProblem;
import cz.sd.api.itop.ticket.problem.ItopProblemUpdateData;
import cz.sd.api.itop.ticket.problem.json.ItopLinkIncidentToProblemRequest;
import cz.sd.api.itop.ticket.problem.json.ItopProblemCreateResponse;
import cz.sd.api.ticket.CiUpdateData;
import cz.sd.api.ticket.TicketCreateData;
import cz.sd.api.ticket.TicketCreateResponse;
import cz.sd.api.ticket.TicketStatus;
import cz.sd.api.ticket.TicketType;
import cz.sd.api.ticket.TicketUpdateData;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

/**
 * Klient pro praci s iTop service desk systemem. iTop service desk coomunity
 * edition podporuje nasledujici ITIL procesy.
 * <ul>
 * <li>Incident management</li>
 * <li>Problem management</li>
 * <li>Change management</li>
 * <li>Configuration management</li>
 * </ul>
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ItopClient implements IServiceDeskApi, IServiceDeskProblemApi, IConfigurationItemApi {

    /**
     * Jersey REST Api klient.
     */
    private final Client client;
    /**
     * Endpoint webove sluzby.
     */
    private final String endpoint;

    /**
     * Nazev/identifikator systemu.
     */
    private final String systemName;

    /**
     * Konstruktor.
     *
     * @param endpoint Endpoint webove sluzby.
     * @param username Prihlasovaci jmeno.
     * @param password Heslo.
     * @param systemName Nazev systemu.
     */
    public ItopClient(String endpoint, String username, String password, String systemName) {
        this.endpoint = endpoint;

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(HttpAuthenticationFeature.basic(username, password));

        this.client = ClientBuilder.newClient(clientConfig);

        this.systemName = systemName;
    }

    @Override
    public Long createTicket(TicketCreateData createInfo) throws ServiceDeskCommunicationException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ItopTicketCreateRequest request = new ItopTicketCreateRequest(createInfo);
        String jsonTicket = "json_data=" + gson.toJson(request);

        WebTarget webTarget = this.client.target(this.endpoint);
        Response wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_FORM_URLENCODED));

        if (wsResponse.getStatus() != 200) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem iTop. HTTP kod: " + wsResponse.getStatus());
        }

        TicketCreateResponse response;
        switch (createInfo.getType()) {
            case INCIDENT:
                response = gson.fromJson(wsResponse.readEntity(String.class), ItopIncidentCreateResponse.class);
                break;
            case PROBLEM:
                response = gson.fromJson(wsResponse.readEntity(String.class), ItopProblemCreateResponse.class);
                break;
            case CHANGE:
                response = gson.fromJson(wsResponse.readEntity(String.class), ItopChangeCreateResponse.class);
                break;
            default:
                return null;
        }
        Long id = response.getCreatedTicketId();

        // pokud je zadana, pripojime konfiguracni polozku
        if (createInfo.getConfigurationItem() != null) {
            this.linkCiToTicket(createInfo.getConfigurationItem(), id, createInfo.getType());
        }

        return id;
    }

    @Override
    public ServiceDeskTicket getTicket(long id, TicketType type) throws ServiceDeskCommunicationException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ItopTicketGetRequest request = new ItopTicketGetRequest(id, type);

        String jsonTicket = "json_data=" + gson.toJson(request);

        WebTarget webTarget = this.client.target(this.endpoint);
        Response wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_FORM_URLENCODED));

        if (wsResponse.getStatus() != 200) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem iTop. HTTP kod: " + wsResponse.getStatus());
        }

        ItopTicketGetResponse response;
        switch (type) {
            case INCIDENT:
                response = gson.fromJson(wsResponse.readEntity(String.class), ItopIncidentGetResponse.class);
                break;
            case PROBLEM:
                response = gson.fromJson(wsResponse.readEntity(String.class), ItopProblemGetResponse.class);
                break;
            case CHANGE:
                response = gson.fromJson(wsResponse.readEntity(String.class), ItopChangeGetResponse.class);
                break;
            default:
                return null;
        }

        // zadna nalezena data
        if (response.isNull()) {
            return null;
        }

        ServiceDeskTicket ticket = ItopTicketConvertor.convertToTicket(response, this.systemName);

        return ticket;
    }

    @Override
    public boolean updateTicket(TicketUpdateData data) throws ServiceDeskCommunicationException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ItopTicketUpdateRequest request = new ItopTicketUpdateRequest(data);
        String jsonTicket = "json_data=" + gson.toJson(request);

        WebTarget webTarget = this.client.target(this.endpoint);
        Response wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_FORM_URLENCODED));

        if (wsResponse.getStatus() != 200) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem iTop. HTTP kod: " + wsResponse.getStatus());
        }

        return true;
    }

    @Override
    public boolean resolveTicket(long id, String note, TicketType type) throws ServiceDeskCommunicationException {
        TicketUpdateData data;
        if (TicketType.INCIDENT.equals(type)) { // pokud jde o incident, muzeme primo vlozit poznamku o vyreseni do informaci o tiketu
            data = new ItopIncidentUpdateData(id, null, null, null, TicketStatus.RESOLVED, type, note);
        } else {
            data = new TicketUpdateData(id, null, null, null, TicketStatus.RESOLVED, type);
        }
        boolean result = this.updateTicket(data);
        if (!note.isEmpty()) {
            this.addMessage(id, note, type);
        }

        return result;
    }

    @Override
    public boolean closeTicket(long id, String note, TicketType type) throws ServiceDeskCommunicationException {
        TicketUpdateData data = new TicketUpdateData(id, null, null, null, TicketStatus.CLOSED, type);
        boolean result = this.updateTicket(data);
        if (!note.isEmpty()) {
            this.addMessage(id, note, type);
        }

        return result;
    }

    @Override
    public boolean suspendTicket(long id, String note, TicketType type) throws ServiceDeskCommunicationException {
        TicketUpdateData data;
        if (TicketType.INCIDENT.equals(type)) {// pokud jde o incident, muzeme primo vlozit poznamku o pozastaveni do informaci o tiketu
            data = new ItopIncidentUpdateData(id, null, null, null, TicketStatus.ON_HOLD, type, note);
        } else {
            data = new TicketUpdateData(id, null, null, null, TicketStatus.ON_HOLD, type);
        }
        boolean result = this.updateTicket(data);
        if (!note.isEmpty()) {
            this.addMessage(id, note, type);
        }

        return result;
    }

    @Override
    public boolean openTicket(long id, String note, TicketType type) throws ServiceDeskCommunicationException {
        TicketUpdateData data = new TicketUpdateData(id, null, null, null, TicketStatus.OPEN, type);
        boolean result = this.updateTicket(data);
        if (!note.isEmpty()) {
            this.addMessage(id, note, type);
        }

        return result;
    }

    @Override
    public boolean deleteTicket(long id, TicketType type) throws ServiceDeskCommunicationException {
        throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem iTop: Mazani tiketu neni podporovano");
    }

    @Override
    public boolean addMessage(long id, String note, TicketType type) throws ServiceDeskCommunicationException {
        TicketUpdateData data;
        switch (type) {
            case INCIDENT:
                data = new ItopIncidentUpdateData(id, type, note);
                break;
            case PROBLEM:
                data = new ItopProblemUpdateData(id, type, note);
                break;
            case CHANGE:
                data = new ItopChangeUpdateData(id, type, note);
                break;
            default:
                return false;
        }
        boolean result = this.updateTicket(data);

        return result;
    }

    @Override
    public boolean linkIncidentToProblem(long problemId, long incidentId) throws ServiceDeskCommunicationException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ItopLinkIncidentToProblemRequest request = new ItopLinkIncidentToProblemRequest(problemId, incidentId);
        String jsonTicket = "json_data=" + gson.toJson(request);

        WebTarget webTarget = this.client.target(this.endpoint);
        Response wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_FORM_URLENCODED));

        if (wsResponse.getStatus() != 200) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem iTop. HTTP kod: " + wsResponse.getStatus());
        }

        return true;
    }

    @Override
    public List<Long> getIncidentsFromProblem(long problemId) throws ServiceDeskCommunicationException {
        ItopProblem problem = (ItopProblem) this.getTicket(problemId, TicketType.PROBLEM);
        return problem.getRelatedIncidents();
    }

    @Override
    public boolean unlinkIncidentFromProblem(long problemId, long incidentId) throws ServiceDeskCommunicationException {
        boolean result = this.linkIncidentToProblem(0, incidentId);

        return result;
    }

    @Override
    public ConfigurationItem getCI(long id) throws ServiceDeskCommunicationException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        ItopCIGetRequest request = new ItopCIGetRequest(id, "FunctionalCI");

        String jsonTicket = "json_data=" + gson.toJson(request);

        WebTarget webTarget = this.client.target(this.endpoint);
        Response wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_FORM_URLENCODED));

        if (wsResponse.getStatus() != 200) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem iTop. HTTP kod: " + wsResponse.getStatus());
        }

        ItopCIDefaultResponse baseResponse = gson.fromJson(wsResponse.readEntity(String.class), ItopCIDefaultResponse.class);
        // druhe volani ws pro ziskani detailni odpovedi
        String finalClass = baseResponse.getFinalClass();
        request = new ItopCIGetRequest(id, finalClass);
        jsonTicket = "json_data=" + gson.toJson(request);

        webTarget = this.client.target(this.endpoint);
        wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_FORM_URLENCODED));

        if (wsResponse.getStatus() != 200) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem iTop. HTTP kod: " + wsResponse.getStatus());
        }

        ItopCIDetailResponse detailResponse = gson.fromJson(wsResponse.readEntity(String.class), ItopCIDetailResponse.class);

        ConfigurationItem item = ItopCIConvertor.convert(baseResponse, detailResponse);

        return item;
    }

    @Override
    public boolean linkCiToTicket(long ciId, long ticketid, TicketType type) throws ServiceDeskCommunicationException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ItopLinkCiToTicketRequest request = new ItopLinkCiToTicketRequest(ticketid, ciId);

        String jsonTicket = "json_data=" + gson.toJson(request);

        WebTarget webTarget = this.client.target(this.endpoint);
        Response wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_FORM_URLENCODED));

        if (wsResponse.getStatus() != 200) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem iTop. HTTP kod: " + wsResponse.getStatus());
        }

        return true;
    }

    @Override
    public boolean updateCI(CiUpdateData updateData) throws ServiceDeskCommunicationException {
        // prvni volani ws pro ziskani CI, ze ktere ziskame konkretni tridu CI, abychom vedeli co upravit
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ItopCIGetRequest getRequest = new ItopCIGetRequest(updateData.getCiId(), "FunctionalCI");
        String jsonTicket = "json_data=" + gson.toJson(getRequest);
        WebTarget webTarget = this.client.target(this.endpoint);
        Response wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_FORM_URLENCODED));
        if (wsResponse.getStatus() != 200) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem iTop. HTTP kod: " + wsResponse.getStatus());
        }
        ItopCIDefaultResponse baseResponse = gson.fromJson(wsResponse.readEntity(String.class), ItopCIDefaultResponse.class);
        // druhe volani ws pro upravu konkretni CI
        String finalClass = baseResponse.getFinalClass();
        ItopCIUpdateRequest updateRequest = new ItopCIUpdateRequest(updateData, finalClass);
        jsonTicket = "json_data=" + gson.toJson(updateRequest);
        webTarget = this.client.target(this.endpoint);
        wsResponse = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(jsonTicket, MediaType.APPLICATION_FORM_URLENCODED));
        if (wsResponse.getStatus() != 200) {
            throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem iTop. HTTP kod: " + wsResponse.getStatus());
        }

        return true;
    }

    @Override
    public String getSystemName() {
        return this.systemName;
    }

    @Override
    public SdSystemType getSystemType() {
        return SdSystemType.ITOP;
    }

}

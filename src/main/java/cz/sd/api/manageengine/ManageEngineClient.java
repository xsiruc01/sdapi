package cz.sd.api.manageengine;

import cz.sd.api.manageengine.ticket.incident.xml.ManageEngineTicketCreateResponse;
import cz.sd.api.manageengine.ticket.incident.xml.ManageEngineTicketGetResponse;
import cz.sd.api.manageengine.ticket.incident.xml.ManageEngineTicketUpdateRequest;
import cz.sd.api.manageengine.ticket.incident.xml.ManageEngineTicketCreateRequest;
import cz.sd.api.manageengine.ticket.xml.ManageEngineMessageGetResponse;
import cz.sd.api.manageengine.ticket.xml.ManageEngineMessageCreateRequest;
import cz.sd.api.manageengine.ticket.ManageEngineMessage;
import cz.sd.api.IServiceDeskApi;
import cz.sd.api.SdSystemType;
import cz.sd.api.ServiceDeskCommunicationException;
import cz.sd.api.ServiceDeskTicket;
import cz.sd.api.manageengine.convertor.ManageEngineTicketConvertor;
import cz.sd.api.ticket.TicketCreateData;
import cz.sd.api.ticket.TicketStatus;
import cz.sd.api.ticket.TicketType;
import cz.sd.api.ticket.TicketUpdateData;
import java.io.StringReader;
import java.io.StringWriter;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.glassfish.jersey.client.ClientConfig;

/**
 * Klient pro praci s Manage Engine Service desk plus systemem. Manage Engine
 * podporuje nasledujici ITIL procesy.
 * <ul>
 * <li>Incident management</li>
 * </ul>
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ManageEngineClient implements IServiceDeskApi {

    /**
     * Jersey REST Api klient.
     */
    private final Client client;
    /**
     * Endpoint webove sluzby.
     */
    private final String endpoint;
    /**
     * Prihlasovaci parametry.
     */
    private final String apiKey;

    /**
     * Nazev/identifikator systemu.
     */
    private final String systemName;

    /**
     * Konstruktor.
     *
     * @param endpoint Endpoint webove sluzby.
     * @param apiKey Vygenerovany API klic pro pristup k webovym sluzbam.
     * @param systemName Nazev systemu.
     */
    public ManageEngineClient(String endpoint, String apiKey, String systemName) {
        this.endpoint = endpoint;
        this.apiKey = apiKey;

        ClientConfig clientConfig = new ClientConfig();

        this.client = ClientBuilder.newClient(clientConfig);

        this.systemName = systemName;
    }

    @Override
    public Long createTicket(TicketCreateData createInfo) throws ServiceDeskCommunicationException {
        Long id;
        try {
            String resource = "/sdpapi/request/";

            ManageEngineTicketCreateRequest request = new ManageEngineTicketCreateRequest(createInfo);

            /* Zabaleni requestu do XML retezce */
            JAXBContext jc = JAXBContext.newInstance(ManageEngineTicketCreateRequest.class);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(request, stringWriter);
            String requestXML = "INPUT_DATA=" + stringWriter.toString();

            WebTarget webTarget = this.client.target(this.endpoint).path(resource).queryParam("OPERATION_NAME", "ADD_REQUEST").queryParam("TECHNICIAN_KEY", this.apiKey);
            Response wsResponse = webTarget.request(MediaType.APPLICATION_XML).post(Entity.entity(requestXML, MediaType.APPLICATION_FORM_URLENCODED));

            if (wsResponse.getStatus() != 200) {
                throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem ManageEngine. HTTP kod: " + wsResponse.getStatus());
            }

            /* Rozbaleni response z XML do objektu response. */
            String responseXML = wsResponse.readEntity(String.class);
            jc = JAXBContext.newInstance(ManageEngineTicketCreateResponse.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            StringReader sr = new StringReader(responseXML);
            ManageEngineTicketCreateResponse response = (ManageEngineTicketCreateResponse) unmarshaller.unmarshal(sr);
            id = response.getCreatedTicketId();

        } catch (JAXBException ex) {
            throw new ServiceDeskCommunicationException("Chyba pri zpracovani XML response z ManageEngine api");
        }
        return id;
    }

    @Override
    public ServiceDeskTicket getTicket(long id, TicketType type) throws ServiceDeskCommunicationException {
        ServiceDeskTicket ticket;
        try {
            String resource = "/sdpapi/request/" + id;

            WebTarget webTarget = this.client.target(this.endpoint).path(resource).queryParam("OPERATION_NAME", "GET_REQUEST").queryParam("TECHNICIAN_KEY", this.apiKey);
            Response wsResponse = webTarget.request(MediaType.APPLICATION_XML).post(null);

            if (wsResponse.getStatus() != 200) {
                throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem ManageEngine. HTTP kod: " + wsResponse.getStatus());
            }

            /* Rozbaleni response z XML do objektu response. */
            String responseXML = wsResponse.readEntity(String.class);
            JAXBContext jc = JAXBContext.newInstance(ManageEngineTicketGetResponse.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            StringReader sr = new StringReader(responseXML);
            ManageEngineTicketGetResponse response = (ManageEngineTicketGetResponse) unmarshaller.unmarshal(sr);

            // a ted poznamky //
            ManageEngineMessageGetResponse noteResponse = this.getNotes(id);
            ticket = ManageEngineTicketConvertor.convertToTicket(response, noteResponse, this.systemName);

        } catch (JAXBException ex) {
            throw new ServiceDeskCommunicationException("Chyba pri zpracovani XML response z ManageEngine api");
        }
        return ticket;
    }

    @Override
    public boolean updateTicket(TicketUpdateData data) throws ServiceDeskCommunicationException {
        try {
            String resource = "/sdpapi/request/" + data.getTicketId();

            ManageEngineTicketUpdateRequest request = new ManageEngineTicketUpdateRequest(data);

            /* Zabaleni requestu do XML retezce */
            JAXBContext jc = JAXBContext.newInstance(ManageEngineTicketUpdateRequest.class);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(request, stringWriter);
            String requestXML = "INPUT_DATA=" + stringWriter.toString();

            WebTarget webTarget = this.client.target(this.endpoint).path(resource).queryParam("OPERATION_NAME", "EDIT_REQUEST").queryParam("TECHNICIAN_KEY", this.apiKey);
            Response wsResponse = webTarget.request(MediaType.APPLICATION_XML).post(Entity.entity(requestXML, MediaType.APPLICATION_FORM_URLENCODED));

            if (wsResponse.getStatus() != 200) {
                throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem ManageEngine. HTTP kod: " + wsResponse.getStatus());
            }
            
        } catch (JAXBException ex) {
            throw new ServiceDeskCommunicationException("Chyba pri zpracovani XML response z ManageEngine api");
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
        throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem ManageEngine: Mazani tiketu neni podporovano");
    }

    @Override
    public boolean addMessage(long id, String note, TicketType type) throws ServiceDeskCommunicationException {
        try {
            String resource = "/sdpapi/request/" + id + "/notes";

            ManageEngineMessage meNote = new ManageEngineMessage(note, false);

            ManageEngineMessageCreateRequest request = new ManageEngineMessageCreateRequest(meNote);

            /* Zabaleni requestu do XML retezce */
            JAXBContext jc = JAXBContext.newInstance(ManageEngineMessageCreateRequest.class);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(request, stringWriter);
            String requestXML = "INPUT_DATA=" + stringWriter.toString();

            WebTarget webTarget = this.client.target(this.endpoint).path(resource).queryParam("OPERATION_NAME", "ADD_NOTE").queryParam("TECHNICIAN_KEY", this.apiKey);
            Response wsResponse = webTarget.request(MediaType.APPLICATION_XML).post(Entity.entity(requestXML, MediaType.APPLICATION_FORM_URLENCODED));

            if (wsResponse.getStatus() != 200) {
                throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem ManageEngine. HTTP kod: " + wsResponse.getStatus());
            }
            
        } catch (JAXBException ex) {
            throw new ServiceDeskCommunicationException("Chyba pri zpracovani XML response z ManageEngine api");
        }
        return true;
    }

    private ManageEngineMessageGetResponse getNotes(long id) throws ServiceDeskCommunicationException {
        ManageEngineMessageGetResponse response;
        try {
            String resource = "/sdpapi/request/" + id + "/notes";

            WebTarget webTarget = this.client.target(this.endpoint).path(resource).queryParam("OPERATION_NAME", "GET_NOTES").queryParam("TECHNICIAN_KEY", this.apiKey);
            Response wsResponse = webTarget.request(MediaType.APPLICATION_XML).post(null);
            
            if (wsResponse.getStatus() != 200) {
                throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem ManageEngine. HTTP kod: " + wsResponse.getStatus());
            }

            /* Rozbaleni response z XML do objektu response. */
            String responseXML = wsResponse.readEntity(String.class);
            JAXBContext jc = JAXBContext.newInstance(ManageEngineMessageGetResponse.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            StringReader sr = new StringReader(responseXML);
            response = (ManageEngineMessageGetResponse) unmarshaller.unmarshal(sr);

        } catch (JAXBException ex) {
            throw new ServiceDeskCommunicationException("Chyba pri zpracovani XML response z ManageEngine api");
        }
        return response;
    }

    @Override
    public String getSystemName() {
        return this.systemName;
    }

    @Override
    public SdSystemType getSystemType() {
        return SdSystemType.MANAGE_ENGINE;
    }

}

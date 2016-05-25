package cz.sd.ws.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.sd.api.IServiceDeskApi;
import cz.sd.api.ServiceDeskCommunicationException;
import cz.sd.api.ServiceDeskTicket;
import cz.sd.api.alvao.AlvaoClient;
import cz.sd.api.alvao.ticket.AlvaoTicketCreateData;
import cz.sd.api.itop.ItopClient;
import cz.sd.api.itop.ticket.ItopOrganization;
import cz.sd.api.itop.ticket.ItopTicketCreateData;
import cz.sd.api.itop.ticket.change.ItopChangeUpdateData;
import cz.sd.api.itop.ticket.incident.ItopIncidentUpdateData;
import cz.sd.api.itop.ticket.problem.ItopProblemUpdateData;
import cz.sd.api.ticket.TicketCreateData;
import cz.sd.api.ticket.TicketPriority;
import cz.sd.api.ticket.TicketStatus;
import cz.sd.api.ticket.TicketType;
import cz.sd.api.ticket.TicketUpdateData;
import cz.sd.api.users.ServiceDeskRequester;
import cz.sd.api.users.ServiceDeskTechnician;
import cz.sd.main.ServiceDeskApiProvider;
import cz.sd.ws.request.WsTicketChangeStatusRequest;
import cz.sd.ws.response.WsTicketCreateResponse;
import cz.sd.ws.request.WsTicketDeleteRequest;
import cz.sd.ws.response.WsResultResponse;
import cz.sd.ws.request.WsTicketAddMessageRequest;
import cz.sd.ws.request.WsTicketCopyRequest;
import cz.sd.ws.request.WsTicketCreateRequest;
import cz.sd.ws.request.WsTicketGetRequest;
import cz.sd.ws.response.WsTicketResponseConvertor;
import cz.sd.ws.request.WsTicketUpdateRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Resource webovych sluzeb pro praci s tikety.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Path("tickets")
public class Tickets {

    /**
     * Metoda webove sluzby pro vraceni informaci o tiketu.
     *
     * @param plainRequest Pozadavek na tiket.
     * @return Tiket ze service desku.
     */
    @POST
    @Path("/getTicket")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTicket(String plainRequest) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WsTicketGetRequest request = gson.fromJson(plainRequest, WsTicketGetRequest.class);

        IServiceDeskApi api = ServiceDeskApiProvider.getApi(request.getSystem());
        TicketType ticketType = TicketType.valueOfName(request.getTicketType());
        Long ticketId = request.getTicketId();
        if (api == null || ticketType == null || ticketId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        ServiceDeskTicket ticket;
        try {
            ticket = api.getTicket(ticketId, ticketType);
            String response = WsTicketResponseConvertor.convert(ticket);
            return Response.status(200).entity(response).build();
        } catch (ServiceDeskCommunicationException ex) {
            WsResultResponse error = new WsResultResponse(false, ex.getMessage());
            String response = gson.toJson(error);
            return Response.status(200).entity(response).build();
        }
    }

    /**
     * Metoda webove sluzby pro vytvoreni tiketu v prislusnem service desku.
     *
     * @param plainRequest Pozadavek na vytvoreni tiketu.
     * @return Odpoved s priznakem uspechu a identifikatorem noveho tiketu.
     */
    @POST
    @Path("/createTicket")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTicket(String plainRequest) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WsTicketCreateRequest request = gson.fromJson(plainRequest, WsTicketCreateRequest.class);

        IServiceDeskApi api = ServiceDeskApiProvider.getApi(request.getSystem());
        TicketType ticketType = TicketType.valueOfName(request.getTicketType());
        String subject = request.getSubject();
        String description = request.getDescription();
        TicketStatus status = TicketStatus.valueOfName(request.getStatus());
        TicketPriority priority = TicketPriority.valueOfName(request.getPriority());
        if (api == null || ticketType == null || subject == null || description == null || status == null || priority == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        TicketCreateData createData;
        // ruzne systemy vyzaduji navic nektera data
        if (api instanceof AlvaoClient) {
            AlvaoTicketCreateData alvaoCreateData = new AlvaoTicketCreateData();
            alvaoCreateData.setSla(request.getSla());
            alvaoCreateData.setService(request.getService());
            createData = alvaoCreateData;
        } else if (api instanceof ItopClient) {
            ItopTicketCreateData itopCreateData = new ItopTicketCreateData();
            String orgName = request.getOrganization();
            Long orgId = request.getOrganizationId();
            itopCreateData.setOrganization(new ItopOrganization(orgId, orgName));
            createData = itopCreateData;
        } else {
            createData = new TicketCreateData();
        }
        createData.setType(ticketType);
        createData.setSubject(subject);
        createData.setDetail(description);
        createData.setStatus(status);
        createData.setPriority(priority);
        createData.setConfigurationItem(request.getConfigItemId());
        // tvurce tiketu a technik
        String name = request.getRequesterName();
        Long id = request.getRequesterId();
        createData.setRequester(new ServiceDeskRequester(id, name));
        name = request.getTechnicianName();
        id = request.getTechnicianId();
        createData.setTechnician(new ServiceDeskTechnician(id, name));

        Long newTicketId;
        try {
            newTicketId = api.createTicket(createData);
            WsTicketCreateResponse ticketResponse = new WsTicketCreateResponse(newTicketId != null, newTicketId);
            String response = gson.toJson(ticketResponse);
            return Response.status(200).entity(response).build();
        } catch (ServiceDeskCommunicationException ex) {
            WsResultResponse error = new WsResultResponse(false, ex.getMessage());
            String response = gson.toJson(error);
            return Response.status(200).entity(response).build();
        }

    }

    /**
     * Metoda webove sluzby pro pridani poznamky k tiketu.
     *
     * @param plainRequest Pozadavek na pridani poznamky.
     * @return Odpoved s priznakem, zda se podarilo pridat poznamku.
     */
    @POST
    @Path("/addMessage")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMessage(String plainRequest) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WsTicketAddMessageRequest request = gson.fromJson(plainRequest, WsTicketAddMessageRequest.class);

        IServiceDeskApi api = ServiceDeskApiProvider.getApi(request.getSystem());
        TicketType ticketType = TicketType.valueOfName(request.getTicketType());
        Long ticketId = request.getTicketId();
        String message = request.getMessage();
        if (api == null || ticketType == null || ticketId == null || message == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        boolean success;
        try {
            success = api.addMessage(ticketId, message, ticketType);
            WsResultResponse addMessageResponse = new WsResultResponse(success, null);
            String response = gson.toJson(addMessageResponse);
            return Response.status(200).entity(response).build();
        } catch (ServiceDeskCommunicationException ex) {
            WsResultResponse error = new WsResultResponse(false, ex.getMessage());
            String response = gson.toJson(error);
            return Response.status(200).entity(response).build();
        }
    }

    /**
     * Metoda webove sluzby pro upravu tiketu v service desku.
     *
     * @param plainRequest Pozadavek na upravu tiketu.
     * @return Odpoved s priznakem, zda se podarilo upravit tiket.
     */
    @PUT
    @Path("/updateTicket")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTicket(String plainRequest) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WsTicketUpdateRequest request = gson.fromJson(plainRequest, WsTicketUpdateRequest.class);

        IServiceDeskApi api = ServiceDeskApiProvider.getApi(request.getSystem());
        TicketType ticketType = TicketType.valueOfName(request.getTicketType());
        TicketStatus status = TicketStatus.valueOfName(request.getStatus());
        TicketPriority priority = TicketPriority.valueOfName(request.getPriority());
        Long ticketId = request.getTicketId();
        if (api == null || ticketType == null || ticketId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        TicketUpdateData updateData;
        if (api instanceof ItopClient) {
            switch (ticketType) {
                case INCIDENT:
                    updateData = new ItopIncidentUpdateData(ticketId, request.getSubject(), request.getDescription(), priority, status, ticketType);
                    break;
                case PROBLEM:
                    updateData = new ItopProblemUpdateData(ticketId, request.getSubject(), request.getDescription(), priority, status, ticketType);
                    break;
                case CHANGE:
                    updateData = new ItopChangeUpdateData(ticketId, request.getSubject(), request.getDescription(), status, ticketType);
                    break;
                default:
                    updateData = null;
            }
        } else {
            updateData = new TicketUpdateData(ticketId, request.getSubject(), request.getDescription(), priority, status, ticketType);
        }
        boolean success;
        try {
            success = api.updateTicket(updateData);
            WsResultResponse updateTicketResponse = new WsResultResponse(success, null);
            String response = gson.toJson(updateTicketResponse);
            return Response.status(200).entity(response).build();
        } catch (ServiceDeskCommunicationException ex) {
            WsResultResponse error = new WsResultResponse(false, ex.getMessage());
            String response = gson.toJson(error);
            return Response.status(200).entity(response).build();
        }
    }

    /**
     * Metoda webove sluzby slouzici k vyreseni ticketu.
     *
     * @param plainRequest Pozadavek na vyreseni tiketu.
     * @return Priznak, zda se vyreseni podarilo.
     */
    @PUT
    @Path("/resolveTicket")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response resolveTicket(String plainRequest) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WsTicketChangeStatusRequest request = gson.fromJson(plainRequest, WsTicketChangeStatusRequest.class);

        IServiceDeskApi api = ServiceDeskApiProvider.getApi(request.getSystem());
        TicketType ticketType = TicketType.valueOfName(request.getTicketType());
        Long ticketId = request.getTicketId();
        if (api == null || ticketType == null || ticketId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        boolean success;
        try {
            success = api.resolveTicket(ticketId, request.getNote(), ticketType);
            WsResultResponse resolveTicketResponse = new WsResultResponse(success, null);
            String response = gson.toJson(resolveTicketResponse);
            return Response.status(200).entity(response).build();
        } catch (ServiceDeskCommunicationException ex) {
            WsResultResponse error = new WsResultResponse(false, ex.getMessage());
            String response = gson.toJson(error);
            return Response.status(200).entity(response).build();
        }
    }

    /**
     * Metoda webove sluzby slouzici k uzavreni ticketu.
     *
     * @param plainRequest Pozadavek na uzavreni tiketu.
     * @return Priznak, zda se uzavreni podarilo.
     */
    @PUT
    @Path("/closeTicket")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response closeTicket(String plainRequest) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WsTicketChangeStatusRequest request = gson.fromJson(plainRequest, WsTicketChangeStatusRequest.class);

        IServiceDeskApi api = ServiceDeskApiProvider.getApi(request.getSystem());
        TicketType ticketType = TicketType.valueOfName(request.getTicketType());
        Long ticketId = request.getTicketId();
        if (api == null || ticketType == null || ticketId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        boolean success;
        try {
            success = api.closeTicket(ticketId, request.getNote(), ticketType);
            WsResultResponse closeTicketResponse = new WsResultResponse(success, null);
            String response = gson.toJson(closeTicketResponse);
            return Response.status(200).entity(response).build();
        } catch (ServiceDeskCommunicationException ex) {
            WsResultResponse error = new WsResultResponse(false, ex.getMessage());
            String response = gson.toJson(error);
            return Response.status(200).entity(response).build();
        }

    }

    /**
     * Metoda webove sluzby slouzici k pozastaveni ticketu.
     *
     * @param plainRequest Pozadavek na pozastaveni tiketu.
     * @return Priznak, zda se pozastaveni podarilo.
     */
    @PUT
    @Path("/suspendTicket")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response suspendTicket(String plainRequest) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WsTicketChangeStatusRequest request = gson.fromJson(plainRequest, WsTicketChangeStatusRequest.class);

        IServiceDeskApi api = ServiceDeskApiProvider.getApi(request.getSystem());
        TicketType ticketType = TicketType.valueOfName(request.getTicketType());
        Long ticketId = request.getTicketId();
        if (api == null || ticketType == null || ticketId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        boolean success;
        try {
            success = api.suspendTicket(ticketId, request.getNote(), ticketType);
            WsResultResponse suspendTicketResponse = new WsResultResponse(success, null);
            String response = gson.toJson(suspendTicketResponse);
            return Response.status(200).entity(response).build();
        } catch (ServiceDeskCommunicationException ex) {
            WsResultResponse error = new WsResultResponse(false, ex.getMessage());
            String response = gson.toJson(error);
            return Response.status(200).entity(response).build();
        }

    }

    /**
     * Metoda webove sluzby slouzici k znovuotevreni ticketu.
     *
     * @param plainRequest Pozadavek na znovuotevreni tiketu.
     * @return Priznak, zda se znovuotevreni podarilo.
     */
    @PUT
    @Path("/reopenTicket")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response reopenTicket(String plainRequest) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WsTicketChangeStatusRequest request = gson.fromJson(plainRequest, WsTicketChangeStatusRequest.class);

        IServiceDeskApi api = ServiceDeskApiProvider.getApi(request.getSystem());
        TicketType ticketType = TicketType.valueOfName(request.getTicketType());
        Long ticketId = request.getTicketId();
        if (api == null || ticketType == null || ticketId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        boolean success;
        try {
            success = api.openTicket(ticketId, request.getNote(), ticketType);
            WsResultResponse reopenTicketResponse = new WsResultResponse(success, null);
            String response = gson.toJson(reopenTicketResponse);
            return Response.status(200).entity(response).build();
        } catch (ServiceDeskCommunicationException ex) {
            WsResultResponse error = new WsResultResponse(false, ex.getMessage());
            String response = gson.toJson(error);
            return Response.status(200).entity(response).build();
        }

    }

    /**
     * Metoda webove sluzby slouzici ke smazani ticketu.
     *
     * @param plainRequest Pozadavek na smazani tiketu.
     * @return Priznak, zda se smazani podarilo.
     */
    @DELETE
    @Path("/deleteTicket")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTicket(String plainRequest) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WsTicketDeleteRequest request = gson.fromJson(plainRequest, WsTicketDeleteRequest.class);

        IServiceDeskApi api = ServiceDeskApiProvider.getApi(request.getSystem());
        TicketType ticketType = TicketType.valueOfName(request.getTicketType());
        Long ticketId = request.getTicketId();
        if (api == null || ticketType == null || ticketId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        boolean success;
        try {
            success = api.deleteTicket(ticketId, ticketType);
            WsResultResponse deleteTicketResponse = new WsResultResponse(success, null);
            String response = gson.toJson(deleteTicketResponse);
            return Response.status(200).entity(response).build();
        } catch (ServiceDeskCommunicationException ex) {
            WsResultResponse error = new WsResultResponse(false, ex.getMessage());
            String response = gson.toJson(error);
            return Response.status(200).entity(response).build();
        }
    }

    /**
     * Metoda webove sluzby slouzici ke kopirovani tiketu.
     *
     * @param plainRequest Pozadavek na zkopirovani tiketu.
     * @return Priznak, zda se kopirovani podarilo.
     */
    @POST
    @Path("/copyTicket")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response copyTicket(String plainRequest) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WsTicketCopyRequest request = gson.fromJson(plainRequest, WsTicketCopyRequest.class);

        IServiceDeskApi sourceApi = ServiceDeskApiProvider.getApi(request.getSrcSystem());
        IServiceDeskApi destApi = ServiceDeskApiProvider.getApi(request.getDestSystem());
        TicketType ticketType = TicketType.valueOfName(request.getTicketType());
        Long ticketId = request.getTicketId();
        if (sourceApi == null || destApi == null || ticketType == null || ticketId == null || sourceApi.getSystemName().equals(destApi.getSystemName())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        ServiceDeskTicket ticket;
        try {
            ticket = sourceApi.getTicket(ticketId, ticketType);

            TicketCreateData createData;
            if (destApi instanceof AlvaoClient) {
                AlvaoTicketCreateData alvaoCreateData = new AlvaoTicketCreateData();
                alvaoCreateData.setService(request.getService());
                alvaoCreateData.setSla(request.getSla());
                createData = alvaoCreateData;
            } else if (destApi instanceof ItopClient) {
                ItopTicketCreateData itopCreateData = new ItopTicketCreateData();
                itopCreateData.setOrganization(new ItopOrganization(request.getOrganizationId(), request.getOrganization()));
                createData = itopCreateData;
            } else {
                createData = new TicketCreateData();
            }
            createData.setConfigurationItem(request.getConfigItemId());
            createData.setSubject(ticket.getSubject());
            createData.setDetail(ticket.getDetail());
            createData.setStatus(ticket.getStatus());
            createData.setPriority(ticket.getPriority());
            createData.setType(ticket.getType());
            // tvurce tiketu a technik
            String name = request.getRequesterName();
            Long id = request.getRequesterId();
            createData.setRequester(new ServiceDeskRequester(id, name));
            name = request.getTechnicianName();
            id = request.getTechnicianId();
            createData.setTechnician(new ServiceDeskTechnician(id, name));

            Long newTicketId = destApi.createTicket(createData);
            WsTicketCreateResponse copyTicketResponse = new WsTicketCreateResponse(newTicketId != null, newTicketId);
            String response = gson.toJson(copyTicketResponse);
            return Response.status(200).entity(response).build();
        } catch (ServiceDeskCommunicationException ex) {
            WsResultResponse error = new WsResultResponse(false, ex.getMessage());
            String response = gson.toJson(error);
            return Response.status(200).entity(response).build();
        }
    }
}

package cz.sd.ws.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.sd.api.ConfigurationItem;
import cz.sd.api.IConfigurationItemApi;
import cz.sd.api.ServiceDeskCommunicationException;
import cz.sd.api.ticket.CiUpdateData;
import cz.sd.api.ticket.TicketType;
import cz.sd.main.ServiceDeskApiProvider;
import cz.sd.ws.request.WsCiGetRequest;
import cz.sd.ws.request.WsCiLinkRequest;
import cz.sd.ws.request.WsCiUpdateRequest;
import cz.sd.ws.response.WsResultResponse;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Resource webovych sluzeb pro praci s konfiguracnimi polozkami.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Path("cmdb")
public class Configurations {

    /**
     * Metoda webove sluzby pro vraceni konfiguracni polozky.
     *
     * @param plainRequest Pozadavek na vraceni konfiguracni polozky.
     * @return Konfiguracni polozka.
     */
    @POST
    @Path("/getCi")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConfigurationItem(String plainRequest) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WsCiGetRequest request = gson.fromJson(plainRequest, WsCiGetRequest.class);

        IConfigurationItemApi api = ServiceDeskApiProvider.getCiApi(request.getSystem());
        Long ciId = request.getConfigItemId();
        if (api == null || ciId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        ConfigurationItem item;
        try {
            item = api.getCI(ciId);
            String response = gson.toJson(item);
            return Response.status(200).entity(response).build();
        } catch (ServiceDeskCommunicationException ex) {
            WsResultResponse error = new WsResultResponse(false, ex.getMessage());
            String response = gson.toJson(error);
            return Response.status(200).entity(response).build();
        }

    }

    /**
     * Metoda webove sluzby pro vytvoreni vazby mezi tiketem a konfiguracni
     * polozkou.
     *
     * @param plainRequest Pozadavek na vytvoreni vazby mezi tiketem a
     * konfiguracni polozkou.
     * @return Priznak, zda byla vazba vytvorena.
     */
    @POST
    @Path("/linkCi")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response linkCiToTicket(String plainRequest) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WsCiLinkRequest request = gson.fromJson(plainRequest, WsCiLinkRequest.class);

        IConfigurationItemApi api = ServiceDeskApiProvider.getCiApi(request.getSystem());
        Long ciId = request.getConfigItemId();
        Long ticketId = request.getTicketId();
        TicketType ticketType = TicketType.valueOfName(request.getTicketType());
        if (api == null || ciId == null || ticketId == null || ticketType == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        boolean success;
        try {
            success = api.linkCiToTicket(ciId, ticketId, ticketType);
            WsResultResponse wsResponse = new WsResultResponse(success, null);
            String response = gson.toJson(wsResponse);
            return Response.status(200).entity(response).build();
        } catch (ServiceDeskCommunicationException ex) {
            WsResultResponse error = new WsResultResponse(false, ex.getMessage());
            String response = gson.toJson(error);
            return Response.status(200).entity(response).build();
        }
    }

    /**
     * Metoda webove sluzby pro upravu konfiguracni polozky.
     *
     * @param plainRequest Pozadavek na upravu konfiguracni polozky.
     * @return Priznak, zda byla polozka upravena.
     */
    @PUT
    @Path("/updateCi")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCi(String plainRequest) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WsCiUpdateRequest request = gson.fromJson(plainRequest, WsCiUpdateRequest.class);

        IConfigurationItemApi api = ServiceDeskApiProvider.getCiApi(request.getSystem());
        Long ciId = request.getConfigItemId();
        if (api == null || ciId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        CiUpdateData updateData = new CiUpdateData();
        updateData.setCiId(ciId);
        updateData.setDescription(request.getDescription());
        updateData.setName(request.getName());
        updateData.setProductName(request.getProductName());
        updateData.setStatus(request.getStatus());
        updateData.setImpact(request.getImpact());

        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < request.getDetails().size(); i += 2) {
            String key = request.getDetails().get(i);
            String value = request.getDetails().get(i + 1);
            map.put(key, value);
        }
        updateData.setDetails(map);

        boolean success;
        try {
            success = api.updateCI(updateData);
            WsResultResponse wsResponse = new WsResultResponse(success, null);
            String response = gson.toJson(wsResponse);
            return Response.status(200).entity(response).build();
        } catch (ServiceDeskCommunicationException ex) {
            WsResultResponse error = new WsResultResponse(false, ex.getMessage());
            String response = gson.toJson(error);
            return Response.status(200).entity(response).build();
        }
    }
}

package cz.sd.ws.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.sd.api.IServiceDeskProblemApi;
import cz.sd.api.ServiceDeskCommunicationException;
import cz.sd.main.ServiceDeskApiProvider;
import cz.sd.ws.request.WsGetRelatedIncidentsRequest;
import cz.sd.ws.request.WsLinkIncidentRequest;
import cz.sd.ws.request.WsUnlinkIncidentRequest;
import cz.sd.ws.response.WsRelatedIncidentsResponse;
import cz.sd.ws.response.WsResultResponse;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Resource webovych sluzeb pro praci s tikety typu problem.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Path("problems")
public class Problems {

    /**
     * Metoda webove sluzby slinkovani incidentu s problemem.
     *
     * @param plainRequest Pozadavek na slinkovani incidentu s problemem.
     * @return Odpoved s priznakem, zda se podarilo incident slinkovat s
     * problemem.
     */
    @POST
    @Path("/linkIncident")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response linkincident(String plainRequest) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WsLinkIncidentRequest request = gson.fromJson(plainRequest, WsLinkIncidentRequest.class);

        IServiceDeskProblemApi api = ServiceDeskApiProvider.getProblemApi(request.getSystem());
        Long incidentId = request.getIncidentId();
        Long problemId = request.getProblemId();
        if (api == null || incidentId == null || problemId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        boolean success;
        try {
            success = api.linkIncidentToProblem(problemId, incidentId);
            WsResultResponse linkIncidentResponse = new WsResultResponse(success, null);
            String response = gson.toJson(linkIncidentResponse);
            return Response.status(200).entity(response).build();
        } catch (ServiceDeskCommunicationException ex) {
            WsResultResponse error = new WsResultResponse(false, ex.getMessage());
            String response = gson.toJson(error);
            return Response.status(200).entity(response).build();
        }

    }

    /**
     * Metoda webove sluzby pro smazani vazby mezi incidentem a problemem.
     *
     * @param plainRequest Pozadavek na smazani vazby mezi incidentem a
     * problemem.
     * @return Odpoved s priznakem, zda se podarilo vazmu smazat.
     */
    @POST
    @Path("/unlinkIncident")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response unlinkincident(String plainRequest) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WsUnlinkIncidentRequest request = gson.fromJson(plainRequest, WsUnlinkIncidentRequest.class);

        IServiceDeskProblemApi api = ServiceDeskApiProvider.getProblemApi(request.getSystem());
        Long incidentId = request.getIncidentId();
        Long problemId = request.getProblemId();
        if (api == null || incidentId == null || problemId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        boolean success;
        try {
            success = api.unlinkIncidentFromProblem(problemId, incidentId);
            WsResultResponse unlinkIncidentResponse = new WsResultResponse(success, null);
            String response = gson.toJson(unlinkIncidentResponse);
            return Response.status(200).entity(response).build();
        } catch (ServiceDeskCommunicationException ex) {
            WsResultResponse error = new WsResultResponse(false, ex.getMessage());
            String response = gson.toJson(error);
            return Response.status(200).entity(response).build();
        }

    }

    /**
     * Metoda webove sluzby pro vraceni seznamu incidentu, ktere souvisejici s
     * problemem (problem je jejich pricina).
     *
     * @param plainRequest Pozadavek na vraceni seznamu souvisejicich incidentu.
     * @return Seznam incidentu souvisejich s problemem.
     */
    @POST
    @Path("/getRelatedIncidents")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRelatedIncidents(String plainRequest) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WsGetRelatedIncidentsRequest request = gson.fromJson(plainRequest, WsGetRelatedIncidentsRequest.class);

        IServiceDeskProblemApi api = ServiceDeskApiProvider.getProblemApi(request.getSystem());
        Long problemId = request.getProblemId();
        if (api == null || problemId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<Long> incidents;
        try {
            incidents = api.getIncidentsFromProblem(problemId);
            WsRelatedIncidentsResponse relatedIncidentsResponse = new WsRelatedIncidentsResponse();
            relatedIncidentsResponse.setIncidents(incidents);
            String response = gson.toJson(relatedIncidentsResponse);
            return Response.status(200).entity(response).build();
        } catch (ServiceDeskCommunicationException ex) {
            WsResultResponse error = new WsResultResponse(false, ex.getMessage());
            String response = gson.toJson(error);
            return Response.status(200).entity(response).build();
        }

    }
}

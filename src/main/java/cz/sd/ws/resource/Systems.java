package cz.sd.ws.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.sd.api.IConfigurationItemApi;
import cz.sd.api.IServiceDeskApi;
import cz.sd.api.IServiceDeskProblemApi;
import cz.sd.main.ServiceDeskApiProvider;
import cz.sd.ws.request.WsSystemGetRequest;
import cz.sd.ws.response.WsSystemGetResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Resource webovych sluzeb pro praci se service desk systemy.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Path("systems")
public class Systems {

    /**
     * Metoda webove sluzby vracejici informace o service desku.
     *
     * @param plainRequest Pozadavek na ziskani ifnromaci o service desku.
     * @return Odpoved s informacemi service desku.
     */
    @POST
    @Path("/getSystem")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSystems(String plainRequest) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WsSystemGetRequest request = gson.fromJson(plainRequest, WsSystemGetRequest.class);

        String sdname = request.getSystem();
        IServiceDeskApi api = ServiceDeskApiProvider.getApi(sdname);

        if (api == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        boolean problems = api instanceof IServiceDeskProblemApi;
        boolean configs = api instanceof IConfigurationItemApi;

        WsSystemGetResponse systemResponse = new WsSystemGetResponse();
        systemResponse.setSystemName(api.getSystemName());
        systemResponse.setSystemType(api.getSystemType());
        systemResponse.setProblemApi(problems);
        systemResponse.setCmdbApi(configs);

        String response = gson.toJson(systemResponse);
        return Response.status(200).entity(response).build();
    }

}

package cz.sd.api.manageengine.ticket.incident.xml;

import cz.sd.api.manageengine.ticket.ManageEngineParameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 * Response na vraceni tiketu ze service desku.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@XmlRootElement(name = "API")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ManageEngineTicketGetResponse {

    @Getter
    @Setter
    private Response response;

    /**
     * Vytvori instanci.
     */
    public ManageEngineTicketGetResponse() {
    }

    /**
     * Vrati mapu parametru a hodnot.
     *
     * @return Mapa parametr-hodnota.
     */
    public Map<String, String> getMapParams() {
        List<ManageEngineParameter> parameters = this.getResponse().getOperation().getDetails().get(0).getParameter();
        Map<String, String> parametersMap = new HashMap<>();
        for (ManageEngineParameter parameter : parameters) {
            parametersMap.put(parameter.getName(), parameter.getValue());
        }
        return parametersMap;
    }

    @Getter
    @Setter
    public static class Response {

        private Operation operation;

        public Response() {
        }
    }

    @Getter
    @Setter
    public static class Operation {

        private Result result;

        @XmlElement // bez tohoto to zde bohuzel nefunguje, proc ale, kdo vi        
        private List<Detail> Details;

        public Operation() {
        }
    }

    @Getter
    @Setter
    private static class Result {

        private String statuscode;
        private String status;
        private String message;

        public Result() {
        }
    }

    @Getter
    @Setter
    public static class Detail {

        private List<ManageEngineParameter> parameter;
        private String Notes;
        private String Worklogs;

        public Detail() {
        }
    }

}

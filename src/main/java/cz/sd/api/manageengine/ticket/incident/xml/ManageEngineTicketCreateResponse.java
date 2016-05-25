package cz.sd.api.manageengine.ticket.incident.xml;

import cz.sd.api.manageengine.ticket.ManageEngineParameter;
import cz.sd.api.ticket.TicketCreateResponse;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 * Response pri vytvoreni tiketu.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@XmlRootElement(name = "API")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ManageEngineTicketCreateResponse extends TicketCreateResponse {

    @Getter
    @Setter
    private ManageEngineTicketGetResponse.Response response;

    /**
     * Vytvori instanci.
     */
    public ManageEngineTicketCreateResponse() {
    }

    @Override
    public Long getCreatedTicketId() {
        Long id = null;
        List<ManageEngineTicketGetResponse.Detail> details = response.getOperation().getDetails();
        for (ManageEngineTicketGetResponse.Detail detail : details) {
            if (detail != null && detail.getParameter() != null) {
                List<ManageEngineParameter> parameters = detail.getParameter();
                for (ManageEngineParameter parameter : parameters) {
                    if (parameter != null && parameter.getValue() != null) {
                        if ("workorderid".equals(parameter.getName())) {
                            id = Long.valueOf(parameter.getValue());
                        }
                    }
                }
            }
        }
        return id;
    }
}

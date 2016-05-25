package cz.sd.api.manageengine.ticket.incident.xml;

import cz.sd.api.manageengine.ticket.ManageEngineParameter;
import cz.sd.api.manageengine.convertor.ManageEnginePriorityConvertor;
import cz.sd.api.manageengine.convertor.ManageEngineStatusConvertor;
import cz.sd.api.ticket.TicketUpdateData;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 * Request na upravu tiketu.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@XmlRootElement(name = "Operation")
public class ManageEngineTicketUpdateRequest {

    @XmlElement(name = "Details")
    private Details details;

    /**
     * Vytvori instanci pozadavku na upravu tiketu.
     *
     * @param data Nova data.
     */
    public ManageEngineTicketUpdateRequest(TicketUpdateData data) {
        List<ManageEngineParameter> parameters = new ArrayList<>();
        ManageEngineParameter parameter;
        if (data.getSubject() != null) {
            parameter = new ManageEngineParameter();
            parameter.setName(ManageEngineParameter.SUBJECT);
            parameter.setValue(data.getSubject());
            parameters.add(parameter);
        }
        if (data.getDetail() != null) {
            parameter = new ManageEngineParameter();
            parameter.setName(ManageEngineParameter.DESCRIPTION);
            parameter.setValue(data.getDetail());
            parameters.add(parameter);
        }
        if (data.getStatus() != null) {
            parameter = new ManageEngineParameter();
            parameter.setName(ManageEngineParameter.STATUS);
            parameter.setValue(ManageEngineStatusConvertor.convertToManageEngineStatus(data.getStatus()));
            parameters.add(parameter);
        }
        if (data.getPriority() != null) {
            parameter = new ManageEngineParameter();
            parameter.setName(ManageEngineParameter.PRIORITY);
            parameter.setValue(ManageEnginePriorityConvertor.convertToManageEnginePriority(data.getPriority()));
            parameters.add(parameter);
        }
        this.details = new Details(parameters);
    }

    /**
     * Vytvori instanci.
     */
    public ManageEngineTicketUpdateRequest() {
    }

    /**
     * Konkretni hodnoty.
     */
    private static class Details {

        @Getter
        @Setter
        private List<ManageEngineParameter> parameter;

        public Details(List<ManageEngineParameter> parameters) {
            this.parameter = parameters;
        }
    }
}

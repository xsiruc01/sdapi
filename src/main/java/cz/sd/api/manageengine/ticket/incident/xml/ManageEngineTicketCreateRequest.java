package cz.sd.api.manageengine.ticket.incident.xml;

import cz.sd.api.manageengine.convertor.ManageEnginePriorityConvertor;
import cz.sd.api.manageengine.convertor.ManageEngineStatusConvertor;
import cz.sd.api.manageengine.ticket.ManageEngineTicketCreateData;
import cz.sd.api.ticket.TicketCreateData;
import cz.sd.api.ticket.TicketCreateRequest;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 * Request pro upravi tiketu.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@XmlRootElement(name = "Operation")
public class ManageEngineTicketCreateRequest extends TicketCreateRequest {

    @XmlElement(name = "Details")
    private final Details details;

    /**
     * Vytvori instanci pozadavku na vytvoreni.
     *
     * @param data Informace o ticketu.
     */
    public ManageEngineTicketCreateRequest(TicketCreateData data) {
        this.details = new Details();
        details.setSubject(data.getSubject());
        details.setDescription(data.getDetail());
        details.setStatus(ManageEngineStatusConvertor.convertToManageEngineStatus(data.getStatus()));
        details.setPriority(ManageEnginePriorityConvertor.convertToManageEnginePriority(data.getPriority()));
        
        if (data instanceof ManageEngineTicketCreateData) {
            ManageEngineTicketCreateData data2 = (ManageEngineTicketCreateData) data;
            details.setGroup(data2.getGroup());
        } else {
            details.setGroup("Hardware Problems");
        }
        
        details.setTechnician(data.getTechnician().getName());
    }

    /**
     * Vytvori instanci pozadavku na vytvoreni.
     */
    public ManageEngineTicketCreateRequest() {
        this.details = null;
    }

    /**
     * Konkretni hodnoty.
     */
    @Getter
    @Setter
    private static class Details {

        private String subject;
        private String description;
        private String priority;
        private String group;
        private String technician;
        private String status;
        //private String service; // nepouzito
    }
}

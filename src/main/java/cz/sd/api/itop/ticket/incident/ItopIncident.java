package cz.sd.api.itop.ticket.incident;

import cz.sd.api.itop.ticket.ItopTicket;
import cz.sd.api.ticket.TicketType;
import lombok.Getter;
import lombok.Setter;

/**
 * Incident v systemu iTop.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ItopIncident extends ItopTicket {

    /**
     * Duvod zastaveni incidentu.
     */
    @Getter
    @Setter
    private String pendingReason;
    /**
     * Poznamka k vyreseni incidentu.
     */
    @Getter
    @Setter
    private String resolvedNote;
    /**
     * Pricina incidentu - problem. Muze byl null.
     */
    @Getter
    @Setter
    private Long parentProblemId;

    /**
     * Vytvori instanci incidentu s nastavenim typu incident.
     */
    public ItopIncident() {
        super();
        setType(TicketType.INCIDENT);
    }

}

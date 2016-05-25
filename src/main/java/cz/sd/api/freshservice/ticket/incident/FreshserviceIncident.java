package cz.sd.api.freshservice.ticket.incident;

import cz.sd.api.freshservice.ticket.FreshserviceTicket;
import cz.sd.api.ticket.TicketType;

/**
 * Incident tiket v systemu Freshservice.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class FreshserviceIncident extends FreshserviceTicket {

    /**
     * Vytvori instanci tiketu s nastavenim typu na incident.
     */
    public FreshserviceIncident() {
        super();
        setType(TicketType.INCIDENT);
    }

}

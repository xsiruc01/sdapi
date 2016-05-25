package cz.sd.api.manageengine.ticket.incident;

import cz.sd.api.manageengine.ticket.ManageEngineTicket;
import cz.sd.api.ticket.TicketType;

/**
 * Incident tiket v systemu ManageEngine.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ManageEngineIncident extends ManageEngineTicket {

    /**
     * Vytvori instanci s nastavenim typu incident.
     */
    public ManageEngineIncident() {
        setType(TicketType.INCIDENT);
    }

}

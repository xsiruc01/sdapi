package cz.sd.api.alvao.ticket.incident;

import cz.sd.api.alvao.ticket.AlvaoTicket;
import cz.sd.api.ticket.TicketType;

/**
 * Incident tiket v systemu ALVAO service desk.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class AlvaoIncident extends AlvaoTicket {

    /**
     * Vytvori instanci s nastavenim typu tiketu na incident.
     */
    public AlvaoIncident() {
        super();
        setType(TicketType.INCIDENT);
    }

}

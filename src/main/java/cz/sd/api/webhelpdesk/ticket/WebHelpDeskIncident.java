package cz.sd.api.webhelpdesk.ticket;

import cz.sd.api.ticket.TicketType;

/**
 * Incident tiket v systemu Web Help Desk.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class WebHelpDeskIncident extends WebHelpDeskTicket {

    /**
     * Vytvori instanci s nastavenim tzpu na incident.
     */
    public WebHelpDeskIncident() {
        setType(TicketType.INCIDENT);
    }

}

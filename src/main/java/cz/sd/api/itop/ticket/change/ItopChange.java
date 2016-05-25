package cz.sd.api.itop.ticket.change;

import cz.sd.api.itop.ticket.ItopTicket;
import cz.sd.api.ticket.TicketType;

/**
 * Zmena v systemu iTop.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ItopChange extends ItopTicket {

    /**
     * Vytvori instanci s nastavenim typu na zmenu.
     */
    public ItopChange() {
        super();
        setType(TicketType.CHANGE);
    }

}

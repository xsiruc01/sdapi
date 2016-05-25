package cz.sd.api.alvao.ticket.change;

import cz.sd.api.alvao.ticket.AlvaoTicket;
import cz.sd.api.ticket.TicketType;

/**
 * Change tiket v systemu ALVAO service desk.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class AlvaoChange extends AlvaoTicket {

    /**
     * Vytvori instanci s nastaveni typu tiketu na zmenu.
     */
    public AlvaoChange() {
        super();
        setType(TicketType.CHANGE);
    }

}

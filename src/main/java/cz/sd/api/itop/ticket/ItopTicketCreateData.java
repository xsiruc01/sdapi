package cz.sd.api.itop.ticket;

import cz.sd.api.ticket.TicketCreateData;
import lombok.Getter;
import lombok.Setter;

/**
 * Doplnujici data nutna pro vytvoreni ticketu v iTop service desk systemu.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class ItopTicketCreateData extends TicketCreateData {

    /**
     * Organizace.
     */
    private ItopOrganization organization;
}

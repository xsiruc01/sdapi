package cz.sd.api.alvao.ticket;

import cz.sd.api.ticket.TicketCreateData;
import lombok.Getter;
import lombok.Setter;

/**
 * Doplnujici data nutna pro vytvoreni ticketu v Alvao service desk systemu.
 * ALVAO vyzaduje zadani sluzby a SLA.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class AlvaoTicketCreateData extends TicketCreateData {

    /**
     * Sluzba service desku.
     */
    @Getter
    @Setter
    private String service;
    /**
     * SLA sluzby.
     */
    @Getter
    @Setter
    private String sla;
}

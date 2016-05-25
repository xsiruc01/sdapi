package cz.sd.api.webhelpdesk.ticket;

import cz.sd.api.ticket.TicketCreateData;
import lombok.Getter;
import lombok.Setter;

/**
 * Doplnujici data nutna pro vytvoreni ticketu ve Web Help Desk service desk systemu.
 * Zadani lokace a typu sluzby/requestu. Oboji nepovinne.
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter @Setter
public class WebHelpDeskTicketCreateData extends TicketCreateData {
    /**
     * Id lokace.
     */
    private Long locationId;
    /**
     * Id typu requestu/sluzby.
     */
    private Long serviceId;
}

package cz.sd.api.manageengine.ticket;

import cz.sd.api.ticket.TicketCreateData;
import lombok.Getter;
import lombok.Setter;

/**
 * Doplnujici informace pro vytvoreni tiketu v ManageEngine. Parametr group.
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter @Setter
public class ManageEngineTicketCreateData extends TicketCreateData {
    /**
     * Skupina problemu - Hardware Problems, Network, Printer Problems, apod.
     */
    private String group;
}

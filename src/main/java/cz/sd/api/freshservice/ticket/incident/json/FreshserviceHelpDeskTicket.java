package cz.sd.api.freshservice.ticket.incident.json;

import lombok.Getter;
import lombok.Setter;

/**
 * Obalka na data ticketu v pripade volani metod pro vraceni konkretnich
 * ticketu.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class FreshserviceHelpDeskTicket {

    /**
     * Obalka na data tiketu.
     */
    private FreshserviceIncidentData helpdesk_ticket;

}

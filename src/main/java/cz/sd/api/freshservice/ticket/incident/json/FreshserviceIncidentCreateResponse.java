package cz.sd.api.freshservice.ticket.incident.json;

import cz.sd.api.ticket.TicketCreateResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * Obalka na odpoved od FreshService v pripade vytvoreni noveho ticketu.
 *
 * @author Pavel Sirucek
 */
@Getter
@Setter
public class FreshserviceIncidentCreateResponse extends TicketCreateResponse {

    /**
     * Status uspesnosti response.
     */
    private Boolean status;
    /**
     * Polozka response.
     */
    private FreshserviceHelpDeskTicket item;

    /**
     * Vrati id noveho tiketu.
     *
     * @return
     */
    @Override
    public Long getCreatedTicketId() {
        return item.getHelpdesk_ticket().getDisplay_id();
    }
}

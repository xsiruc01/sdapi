package cz.sd.api.webhelpdesk.ticket.json;

import cz.sd.api.ticket.TicketCreateResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * Odpoved Web Help Desku pri uspesnem vytvoreni ticketu. Obsahuje data o
 * vytvorenem ticketu.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class WebHelpDeskTicketCreateResponse extends TicketCreateResponse {

    /**
     * ID ticketu.
     */
    private Integer id;
    /**
     * Typ ticketu.
     */
    private String type;
    /**
     * Posledni update (cas vytvoreni).
     */
    private String lastUpdated;
    /**
     * ID lokace.
     */
    private Integer locationId;
    /**
     * ID stavu ticketu.
     */
    private Integer statusTypeId;
    /**
     * Predmet pozadavku.
     */
    private String subject;

    @Override
    public Long getCreatedTicketId() {
        return id.longValue();
    }
}

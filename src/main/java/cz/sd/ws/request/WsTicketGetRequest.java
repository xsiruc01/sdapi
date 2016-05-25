package cz.sd.ws.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Pozadavek na ziskani ticketu.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter @Setter
public class WsTicketGetRequest {
    /**
     * System.
     */
    private String system;
    /**
     * Typ ticketu.
     */
    private String ticketType;
    /**
     * Id ticketu.
     */
    private Long ticketId;
}

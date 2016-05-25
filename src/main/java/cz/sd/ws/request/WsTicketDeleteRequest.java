package cz.sd.ws.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Pozadavek na smazani tiketu.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class WsTicketDeleteRequest {

    /**
     * System.
     */
    private String system;
    /**
     * Typ ticketu.
     */
    private String ticketType;
    /**
     * Identifikator ticketu.
     */
    private Long ticketId;
}

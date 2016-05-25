package cz.sd.ws.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Request pro pridani zpravy k ticketu.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter @Setter
public class WsTicketAddMessageRequest {
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
    /**
     * Text zpravy.
     */
    private String message;
}

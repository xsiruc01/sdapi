package cz.sd.ws.response;

/**
 * Odpoved rozhrani na vytvoreni tiketu.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class WsTicketCreateResponse {

    /**
     * Priznak, zda bylo provedeni predesleho pozadavku uspesne.
     */
    private final boolean success;
    /**
     * Id noveho ticketu.
     */
    private final Long ticketId;

    public WsTicketCreateResponse(boolean success, Long ticketId) {
        this.success = success;
        this.ticketId = ticketId;
    }
}

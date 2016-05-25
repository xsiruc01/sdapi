package cz.sd.ws.response;

import lombok.Getter;

/**
 * Odpoved na ziskani tiketu, pokud tiket nebyl nalezen.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class WsTicketNotFoundResponse {

    @Getter
    private final String message = "Ticket not found.";
}

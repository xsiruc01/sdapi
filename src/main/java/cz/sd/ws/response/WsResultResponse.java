package cz.sd.ws.response;

/**
 * Response nesouci pouze informaci o uspesne akci.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class WsResultResponse {

    /**
     * Priznak, zda bylo provedeni predesleho pozadavku uspesne.
     */
    private final boolean success;
    /**
     * Zprava k neuspechu volani webovych sluzeb. Volitelne.
     */
    private final String message;

    /**
     * Vytvori instanci odpovedi.
     *
     * @param success Priznak uspechu.
     * @param message Zprava.
     */
    public WsResultResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    
}

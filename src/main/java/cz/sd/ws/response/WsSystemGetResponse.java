package cz.sd.ws.response;

import cz.sd.api.SdSystemType;
import lombok.Getter;
import lombok.Setter;

/**
 * Odpoved na ziskani informaci o service desku.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class WsSystemGetResponse {

    /**
     * Jmeno service desku.
     */
    private String systemName;
    /**
     * Typ service desku.
     */
    private SdSystemType systemType;
    /**
     * Priznak, zda api umoznuje praci s problemy.
     */
    private boolean problemApi;
    /**
     * Priznak, zda api umoznuje praci s konfiguracnimi polozkami.
     */
    private boolean cmdbApi;
}

package cz.sd.ws.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Pozadavek na vraceni inforamci o service desku.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class WsSystemGetRequest {

    /**
     * Jmeno service desku.
     */
    private String system;
}

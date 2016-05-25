package cz.sd.ws.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Pozadavek na vraceni konfiguracni polozky.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class WsCiGetRequest {

    /**
     * System.
     */
    private String system;
    /**
     * Id konfiguracni polozky.
     */
    private Long configItemId;
}

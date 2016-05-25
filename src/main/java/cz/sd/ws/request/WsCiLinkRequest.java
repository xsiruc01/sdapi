package cz.sd.ws.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Pozadavek na slinkovani tiketu a konfiguracni polozky.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class WsCiLinkRequest {

    /**
     * System.
     */
    private String system;
    /**
     * Identifikator ticketu.
     */
    private Long ticketId;
    /**
     * Typ ticketu.
     */
    private String ticketType;
    /**
     * Identifikator konfiguracni polozky.
     */
    private Long configItemId;
}

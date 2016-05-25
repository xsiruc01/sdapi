package cz.sd.ws.request;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Pozadavek na upravu konfiguracni polozky.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class WsCiUpdateRequest {

    /**
     * System.
     */
    private String system;
    /**
     * Id konfiguracni polozky.
     */
    private Long configItemId;
    /**
     * Nazev polozky.
     */
    private String name;
    /**
     * Nazev produktu.
     */
    private String productName;
    /**
     * Popis polozky.
     */
    private String description;
    /**
     * Stav uzivani polozky.
     */
    private String status;
    /**
     * Dopad CI na business;
     */
    private String impact;
    /**
     * Mapa nazev vlastnosti - hodnota. Umoznuje nastavit custom paramtry
     * konfiguracni polozky.
     */
    private List<String> details;
}

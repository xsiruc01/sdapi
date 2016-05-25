package cz.sd.api.ticket;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Data pro upravu konfiguracni polozky.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class CiUpdateData {

    /**
     * Id konfiguracni polozky.
     */
    private long ciId;
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
    private Map<String, Object> details;

}

package cz.sd.api;

import lombok.Getter;
import lombok.Setter;

/**
 * Predek vsech konfiguracnich polozek CMDB. Obsahuje zakladni informace, ktere
 * by mely byt zname ve vsech systemech (ALVAO bude asi vyjimka). Doplnujici
 * informace by mely byt implementovany v prislusnem predkovi.
 *
 * @author Pavel Širůček
 */
@Getter
@Setter
public abstract class ConfigurationItem {

    /**
     * Nazev konfiguracni polozky.
     */
    private String name;
    /**
     * Popis konfiguracni polozky.
     */
    private String description;
    /**
     * Id konfiguracni polozky.
     */
    private long id;
    /**
     * Nazev typu konfiguracni polozky.
     */
    private String typeName;
    /**
     * Id typu konfiguracni polozky.
     */
    private Long typeId;
    /**
     * Datum vytvoreni.
     */
    private String createdDate;
    /**
     * Nazev produktu.
     */
    private String productName;
    /**
     * Stav pouzivani.
     */
    private String status;
    /**
     * Dopad na business.
     */
    private String impact;
}

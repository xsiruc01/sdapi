package cz.sd.api.config;

import lombok.Getter;
import lombok.Setter;

/**
 * Obecna konfigurace sd systemu ve formatu JSON. Z duvodu jednoduchosti jsou
 * zde uvedeny vsechny mozne parametry konfigurace, i kdyz systemy nikdy vsechny
 * nemaji. Napr. Alvao obsahuje pouze dbstring. Parametry, ktere nejsou
 * nastaveny, jsou v pripade defaultniho zpracovani GSONem nastaveny na null.
 * Lze tak jednoduse zjistit, zda je nastavene to co potrebujeme a nepotrebne
 * veci ignorovat.
 *
 * @author Pavel Širůček
 */
@Getter
@Setter
public class JsonSdSystem {

    /**
     * Typ service desku.
     */
    private String type;
    /**
     * Jmeno/identifikator systemu.
     */
    private String name;
    /**
     * Endpoint webovych sluzeb.
     */
    private String endpoint;
    /**
     * Login k webovym sluzbam.
     */
    private String login;
    /**
     * Heslo k webovym sluzbam.
     */
    private String password;
    /**
     * API klic pro pouziti webovych sluzeb. Pouze Manage Engine.
     */
    private String apiKey;
    /**
     * Databazovy string. Pouze Alvao.
     */
    private String dbString;
}

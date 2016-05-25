package cz.sd.api.config;

import lombok.Getter;
import lombok.Setter;

/**
 * Konfigurace pro Alvao.
 *
 * @author Pavel Širůček
 */
@Getter
@Setter
public class Alvao extends SdSystem {

    /**
     * Databazovy string.
     */
    private String dbstring;
}

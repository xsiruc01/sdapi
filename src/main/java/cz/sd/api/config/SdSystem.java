package cz.sd.api.config;

import lombok.Getter;
import lombok.Setter;

/**
 * Predek konfiguraci sd systemu.
 *
 * @author Pavel Širůček
 */
@Getter
@Setter
public abstract class SdSystem {

    /**
     * Jedinecne jmeno/identifikator service desku.
     */
    private String name;
}

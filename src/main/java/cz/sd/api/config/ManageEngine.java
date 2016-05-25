package cz.sd.api.config;

import lombok.Getter;
import lombok.Setter;

/**
 * Konfigurace pro Manage Engine.
 *
 * @author Pavel Širůček
 */
@Getter
@Setter
public class ManageEngine extends SdSystem {

    /**
     * Endpoint webovych sluzeb.
     */
    private String endpoint;
    /**
     * API klic pro prihlaseni k webovym sluzbam.
     */
    private String apiKey;
}

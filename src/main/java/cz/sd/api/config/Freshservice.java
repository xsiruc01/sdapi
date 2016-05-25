package cz.sd.api.config;

import lombok.Getter;
import lombok.Setter;

/**
 * Konfigurace pro Freshservice.
 *
 * @author Pavel Širůček
 */
@Getter
@Setter
public class Freshservice extends SdSystem {

    /**
     * Endpoint k webovym sluzbam.
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

}

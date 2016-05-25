package cz.sd.api.config;

import lombok.Getter;
import lombok.Setter;

/**
 * Konfigurace pro Web Help Desk.
 *
 * @author Pavel Širůček
 */
@Getter
@Setter
public class WebHelpDesk extends SdSystem {

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
}

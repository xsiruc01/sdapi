package cz.sd.api;

import lombok.Getter;

/**
 * Vyjimka vyhazovana v pripade, ze z nejakeho duvodu selhala komunikukace
 * pomoci webovych sluzeb s konkretnim service deskem.
 *
 * @author Pavel Širůček
 */
public class ServiceDeskCommunicationException extends Exception {

    /**
     * Zprava vyjimky.
     */
    @Getter
    private final String message;

    /**
     * Vytvori instanci vyjimky.
     *
     * @param message Zprava vyjimky.
     */
    public ServiceDeskCommunicationException(String message) {
        this.message = message;
    }

}

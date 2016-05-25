package cz.sd.api;

import lombok.Getter;
import lombok.Setter;

/**
 * Predek vsech poznamek, zprav nebo logu k ticketu. Zprava je vytvorena pri
 * cteni tiketu z prislusneho service desku. Pomoci zprav muze prislusny service
 * desk nebo help desk komunikovat pres api s klientem.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter @Setter
public class ServiceDeskMessage {

    /**
     * Text zpravy.
     */
    private String text;
    
    /**
     * Datum vpridani zpravy.
     */
    private String date;

    /**
     * Vytvori instanci zpravy.
     * @param text Text zpravy.
     * @param date Datum zpravy.
     */
    public ServiceDeskMessage(String text, String date) {
        this.text = text;
        this.date = date;
    }    
}

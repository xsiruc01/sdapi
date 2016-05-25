package cz.sd.ws.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Pozadavek na zmenu stavu tiketu. Zmena je typu vyreseni tiketu, uzavreni
 * tiketu, pozastaveni tiketu a znovuotevreni tiketu. Ke zmene stavu je mozne
 * pridat poznamku doprovazejici tuto zmenu.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class WsTicketChangeStatusRequest {

    /**
     * System.
     */
    private String system;
    /**
     * Typ ticketu.
     */
    private String ticketType;
    /**
     * Identifikator ticketu.
     */
    private Long ticketId;
    /**
     * Status.
     */
    private String status;
    /**
     * Poznamka ke zmene stavu.
     */
    private String note;
}

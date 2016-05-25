package cz.sd.ws.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Pozadavek na upravu ticketu.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class WsTicketUpdateRequest {

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
     * Predmet ticketu.
     */
    private String subject;
    /**
     * Popis ticketu.
     */
    private String description;
    /**
     * Priorita.
     */
    private String priority;
    /**
     * Status.
     */
    private String status;
}

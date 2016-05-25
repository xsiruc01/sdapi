package cz.sd.api.ticket;

import lombok.Getter;

/**
 * Pozadavek na upravu ticketu.
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
public class TicketUpdateData {
    /**
     * Identifikator ticketu.
     */
    private final long ticketId;
    /**
     * Predmet ticketu.
     */
    private final String subject;
    /**
     * Popis ticketu. Informace, poznamky.
     */
    private final String detail;
    /**
     * Priorita.
     */
    private final TicketPriority priority;
    /**
     * Status.
     */
    private final TicketStatus status;
    /**
     * Typ ticketu.
     */
    private final TicketType type;

    /**
     * Konstruktor.
     * @param ticketId Id ticketu.
     * @param subject Novy predmet ticketu.
     * @param detail Novy detailni popis ticketu.
     * @param priority Nova priorita ticketu.
     * @param status Novy stav ticketu.
     * @param type Novy typ ticketu.
     */
    public TicketUpdateData(long ticketId, String subject, String detail, TicketPriority priority, TicketStatus status, TicketType type) {
        this.ticketId = ticketId;
        this.subject = subject;
        this.detail = detail;
        this.priority = priority;
        this.status = status;
        this.type = type;
    }
    
    

}

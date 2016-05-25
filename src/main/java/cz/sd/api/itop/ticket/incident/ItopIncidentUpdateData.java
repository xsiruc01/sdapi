package cz.sd.api.itop.ticket.incident;

import cz.sd.api.ticket.TicketPriority;
import cz.sd.api.ticket.TicketStatus;
import cz.sd.api.ticket.TicketType;
import cz.sd.api.ticket.TicketUpdateData;
import lombok.Getter;
import lombok.Setter;

/**
 * Struktura upravovaych dat incidentu.
 * @author Pavel Sirucek (xsiruc01)
 */
public class ItopIncidentUpdateData extends TicketUpdateData {
    
    /**
     * Duvod pro zastaveni incidentu.
     */
    @Getter @Setter
    private String pendingReason; // pending_reason
    /**
     * Popis vyreseni incidentu.
     */
    @Getter @Setter
    private String resolvedInfo; // solution
    /**
     * Poznamka/message.
     */
    @Getter @Setter
    private String note;

    /**
     * Vytvori instanci.
     * @param ticketId Id tiketu.
     * @param subject Predmet.
     * @param detail Popis.
     * @param priority Priorita.
     * @param status Stav.
     * @param type Typ tiketu.
     */
    public ItopIncidentUpdateData(long ticketId, String subject, String detail, TicketPriority priority, TicketStatus status, TicketType type) {
        super(ticketId, subject, detail, priority, status, type);
    }
    
    /**
     * Vytvori instanci.
     * @param ticketId Id tiketu.
     * @param subject predmet.
     * @param detail Popis.
     * @param priority Priorita.
     * @param status Stav.
     * @param type Typ tiketu.
     * @param statusChangeNote Poznamka ke zmene stavu. 
     */
    public ItopIncidentUpdateData(long ticketId, String subject, String detail, TicketPriority priority, TicketStatus status, TicketType type, String statusChangeNote) {
        this(ticketId, subject, detail, priority, status, type);
        if (TicketStatus.ON_HOLD.equals(status)) {
            this.pendingReason = statusChangeNote;
            this.resolvedInfo = null;
        } else if (TicketStatus.RESOLVED.equals(status)) {
            this.pendingReason = null;
            this.resolvedInfo = statusChangeNote;
        } else {
            // ignored
            this.pendingReason = null;
            this.resolvedInfo = null;
        }
    }
    
    /**
     * Vytvori instanci.
     * @param ticketId Id tiketu.
     * @param type Typ tiketu.
     * @param note Poznamka.
     */
    public ItopIncidentUpdateData(long ticketId, TicketType type, String note) {
        this(ticketId, null, null, null, null, type);
        this.note = note;
    }

}

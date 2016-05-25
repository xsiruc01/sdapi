package cz.sd.api.itop.ticket.problem;

import cz.sd.api.ticket.TicketPriority;
import cz.sd.api.ticket.TicketStatus;
import cz.sd.api.ticket.TicketType;
import cz.sd.api.ticket.TicketUpdateData;
import lombok.Getter;
import lombok.Setter;

/**
 * Struktura dat na upravu tiketu.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ItopProblemUpdateData extends TicketUpdateData {

    /**
     * Poznamka/message.
     */
    @Getter
    @Setter
    private String note;

    /**
     * Vytvori instanci.
     *
     * @param ticketId Id tiketu.
     * @param subject Predmet.
     * @param detail Popis.
     * @param priority Priorita.
     * @param status Stav.
     * @param type Typ tiketu.
     */
    public ItopProblemUpdateData(long ticketId, String subject, String detail, TicketPriority priority, TicketStatus status, TicketType type) {
        super(ticketId, subject, detail, priority, status, type);
    }

    /**
     * Vytvori instanci.
     *
     * @param ticketId Id tiketu.
     * @param type Typ tiketu.
     * @param note Poznamka.
     */
    public ItopProblemUpdateData(long ticketId, TicketType type, String note) {
        this(ticketId, null, null, null, null, type);
        this.note = note;
    }
}

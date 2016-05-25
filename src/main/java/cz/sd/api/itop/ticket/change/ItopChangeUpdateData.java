package cz.sd.api.itop.ticket.change;

import cz.sd.api.ticket.TicketStatus;
import cz.sd.api.ticket.TicketType;
import cz.sd.api.ticket.TicketUpdateData;
import lombok.Getter;
import lombok.Setter;

/**
 * Struktura pro zadani dat, ktera maji byt u zmeny upravena.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ItopChangeUpdateData extends TicketUpdateData {

    /**
     * Poznamka/message.
     */
    @Getter
    @Setter
    private String note;

    /**
     * Vytvori instanci.
     *
     * @param ticketId Id zmeny.
     * @param subject Predmet zmeny.
     * @param detail Popis zmeny.
     * @param status Stav zmeny
     * @param type Typ tiketu - zmena.
     */
    public ItopChangeUpdateData(long ticketId, String subject, String detail, TicketStatus status, TicketType type) {
        super(ticketId, subject, detail, null, status, type);
    }

    /**
     * Vytvori instanci struktury pro pridani poznamky.
     *
     * @param ticketId Id zmeny.
     * @param type Typ tiketu - zmena.
     * @param note Poznamka/zprava.
     */
    public ItopChangeUpdateData(long ticketId, TicketType type, String note) {
        this(ticketId, null, null, null, type);
        this.note = note;
    }
}

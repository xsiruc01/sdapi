package cz.sd.api.ticket;

/**
 * Odpoved ziskana pri vytvareni ticketu.
 * @author Pavel Sirucek (xsiruc01)
 */
public abstract class TicketCreateResponse {
    /**
     * Vrati id vytvoreneho ticketu.
     * @return Id vytvoreneho ticketu nebo null v pripade ze ticket nebyl vytvoren.
     */
    public abstract Long getCreatedTicketId();    
}

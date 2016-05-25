package cz.sd.api.itop.ticket.problem;

import cz.sd.api.itop.ticket.ItopTicket;
import cz.sd.api.ticket.TicketType;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Problem v systemu iTop.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ItopProblem extends ItopTicket {
    /**
     * Seznam souvisejicich incidentu, jichz je problem pricinou.
     */
    @Getter
    @Setter
    private List<Long> relatedIncidents;

    /**
     * Vytvori instanci s nastavenim typu na problem.
     */
    public ItopProblem() {
        super();
        setType(TicketType.PROBLEM);
    }
    
    
}

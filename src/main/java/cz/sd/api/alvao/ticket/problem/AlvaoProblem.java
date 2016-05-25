package cz.sd.api.alvao.ticket.problem;

import cz.sd.api.alvao.ticket.AlvaoTicket;
import cz.sd.api.ticket.TicketType;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Problem tiket v systemu ALVAO service desk.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class AlvaoProblem extends AlvaoTicket {

    /**
     * Seznam souvisejicich incidentu, jichz je problem pricinou.
     */
    @Getter
    @Setter
    private List<Long> relatedIncidents;

    /**
     * Vytvori instanci s nastavenim typu tiketu na problem.
     */
    public AlvaoProblem() {
        super();
        setType(TicketType.PROBLEM);
    }
    
    
}

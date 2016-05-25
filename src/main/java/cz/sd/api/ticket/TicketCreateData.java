package cz.sd.api.ticket;

import cz.sd.api.users.ServiceDeskUser;
import lombok.Getter;
import lombok.Setter;

/**
 * Data nutna pro vytvoreni ticketu v service desk (helpdesk) systemu. Jde o
 * sadu informaci ,ktera je zakladni a sdilena vsemi systemy. Pokud nejaky
 * system potrebuje obsahlejsi data, mela by byt vytvoreny prislusny potomek
 * teto tridy.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter @Setter
public class TicketCreateData {    
    /**
     * Predmet ticketu.
     */
    private String subject;
    /**
     * Popis ticketu. Informace, poznamky.
     */
    private String detail;
    /**
     * Priorita.
     */
    private TicketPriority priority;
    /**
     * Status.
     */
    private TicketStatus status;
    /**
     * Typ ticketu.
     */
    private TicketType type;
    /**
     * Konfiguracni polozka.
     */
    private Long configurationItem;
    /**
     * Tvurce ticketu.
     */
    private ServiceDeskUser requester;
    /**
     * Technik/agent/resitel.
     */
    private ServiceDeskUser technician; 
    /**
     * Deadline. Tohle nejspis nepujde moc zadavat, deadline se ridi pravdepodobne podle sla apod.
     */
    private String deadlineDate;
}

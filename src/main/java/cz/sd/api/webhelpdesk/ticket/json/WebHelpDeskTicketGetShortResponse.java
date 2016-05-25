package cz.sd.api.webhelpdesk.ticket.json;

import lombok.Getter;
import lombok.Setter;

/**
 * Kratka odpoved Web Help Desku dorucena v pripade, kdyz je pozadovana skupina
 * ticketu. Neobsahuje vsechna data o ticketu.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class WebHelpDeskTicketGetShortResponse {

    private long id;
    private String type;
    private String lastUpdated;
    private String shortSubject;
    private String shortDetail;
    private String displayClient;
    private Long updateFlagType;
    private String prettyLastUpdated;
    private Object latestNote;
}

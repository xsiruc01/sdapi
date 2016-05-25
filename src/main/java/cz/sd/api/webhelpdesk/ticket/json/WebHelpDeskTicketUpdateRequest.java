package cz.sd.api.webhelpdesk.ticket.json;

import cz.sd.api.ticket.TicketUpdateData;
import cz.sd.api.webhelpdesk.convertor.WebHelpDeskPriorityConvertor;
import cz.sd.api.webhelpdesk.convertor.WebHelpDeskStatusConvertor;
import lombok.Getter;
import lombok.Setter;

/**
 * Pozadavek na editaci ticketu Web Help Desku.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
public class WebHelpDeskTicketUpdateRequest {

    /**
     * Predmet ticketu.
     */
    private final String subject;
    /**
     * Popis ticketu.
     */
    private final String detail;
    /**
     * Stav - otevreny, cekajici apod.
     */
    private final StatusType statustype;
    /**
     * Priorita.
     */
    private final PriorityType prioritytype;
    /**
     * Typ problemu - nemenime.
     */
    private final ProblemType problemtype = null;

    /**
     * Konstruktor.
     *
     * @param data Updatovane informace o ticketu.
     */
    public WebHelpDeskTicketUpdateRequest(TicketUpdateData data) {
        this.detail = data.getDetail();
        this.subject = data.getSubject();
        // status
        if (data.getStatus() != null) {
            this.statustype = new StatusType();
            this.statustype.setId(WebHelpDeskStatusConvertor.convertToWebHelpDeskStatus(data.getStatus()));
        } else {
            this.statustype = null;
        }
        // priorita
        if (data.getPriority() != null) {
            this.prioritytype = new PriorityType();
            this.prioritytype.setId(WebHelpDeskPriorityConvertor.convertToWebHelpDeskPriority(data.getPriority()));
        } else {
            this.prioritytype = null;
        }
    }

    /**
     * Privatni trida pro nastaveni priority.
     */
    @Getter
    @Setter
    private static class PriorityType {

        public final String type = "PriorityType";
        public Integer id;

    }

    /**
     * Privatni trida pro nastaveni stavu.
     */
    @Getter
    @Setter
    private static class StatusType {

        public final String type = "StatusType";
        public Integer id;

    }

    /**
     * Privatni trida pro nastaveni typu problemu.
     */
    @Getter
    @Setter
    private static class ProblemType {

        public final String type = "ProblemType";
        public Integer id;

    }

}

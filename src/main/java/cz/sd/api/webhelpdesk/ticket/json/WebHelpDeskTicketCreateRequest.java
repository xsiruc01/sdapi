package cz.sd.api.webhelpdesk.ticket.json;

import cz.sd.api.ticket.TicketCreateData;
import cz.sd.api.ticket.TicketCreateRequest;
import cz.sd.api.webhelpdesk.convertor.WebHelpDeskPriorityConvertor;
import cz.sd.api.webhelpdesk.convertor.WebHelpDeskStatusConvertor;
import cz.sd.api.webhelpdesk.ticket.WebHelpDeskTicketCreateData;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Informace nutne pro zaslani requestu na vytvoreni ticketu. 1:1 s JSON
 * requestem
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class WebHelpDeskTicketCreateRequest extends TicketCreateRequest {

    /**
     * Pokud neni zadan tvurce - reporter, tak pouzijeme hodnotu 1.
     */
    private static final int DEFAULT_REQUESTER_ID = 1;

    /**
     * Defaultni typ requestu - sluzba. Id 3 je IT General/Other.
     */
    private static final int DEFAULT_REQUEST_TYPE_ID = 3;

    /**
     * Predmet ticketu.
     */
    private String subject;
    /**
     * Popis ticketu.
     */
    private String detail;
    /**
     * Priznak, zda ma byt ticket prirazen technikovi, ktery jej vytvoril.
     */
    private Boolean assignToCreatingTech = false;
    /**
     * Typ pozadavku - zda se jedna o Hardware, IT, software apod.
     */
    private Boolean sendEmail;
    /**
     * Lokace, ktere se ticket tyka.
     */
    private Location location;
    /**
     * Kdo vytvari ticket, asi.
     */
    private ClientReporter clientReporter;
    /**
     * Technik.
     */
    private ClientTech clientTech;
    /**
     * Stav - otevreny, cekajici apod.
     */
    private StatusType statustype;
    /**
     * Priorita.
     */
    private PriorityType prioritytype;
    /**
     * Typ problemu? Co to je?
     */
    private ProblemType problemtype;

    /**
     * Konstruktor. Z predanych informaci vytvori novy ticket.
     *
     * @param data Informace o vytvarenem ticketu.
     */
    public WebHelpDeskTicketCreateRequest(TicketCreateData data) {
        this.detail = data.getDetail();
        this.subject = data.getSubject();

        this.sendEmail = false;

        // client reporter...pokud je zadane id zadavatele, doplnime, jinak defaultne nastavime na 1
        if (data.getRequester() != null && data.getRequester().getId() != null) {
            this.clientReporter = new ClientReporter();
            this.clientReporter.setId(data.getRequester().getId().intValue());
        } else {
            this.clientReporter = new ClientReporter();
            this.clientReporter.setId(DEFAULT_REQUESTER_ID);
        }

        // technik...pokud je zadany, jinak priradime zadavateli
        if (data.getTechnician() != null && data.getTechnician().getId() != null) {
            this.clientTech = new ClientTech();
            this.clientTech.setId(data.getTechnician().getId().intValue());
        } else {
            this.assignToCreatingTech = true;
        }

        // status
        this.statustype = new StatusType();
        this.statustype.setId(WebHelpDeskStatusConvertor.convertToWebHelpDeskStatus(data.getStatus()));
        // priorita
        this.prioritytype = new PriorityType();
        this.prioritytype.setId(WebHelpDeskPriorityConvertor.convertToWebHelpDeskPriority(data.getPriority()));

        // dalsi informace vazane k Web Help Desku
        if (data instanceof WebHelpDeskTicketCreateData) {
            WebHelpDeskTicketCreateData data2 = (WebHelpDeskTicketCreateData) data;
            if (data2.getLocationId() != null) {
                this.location = new Location();
                this.location.setId(data2.getLocationId().intValue());
            }
            if (data2.getServiceId() != null) {
                this.problemtype = new ProblemType();
                this.problemtype.setId(data2.getServiceId().intValue());
            } else {
                this.problemtype = new ProblemType();
                problemtype.setId(3);
            }
        }

        // pokud by prece jen nebylo nastaveno z WebHelpDeskTicketCreateData
        if (this.problemtype == null) {
            this.problemtype = new ProblemType();
            problemtype.setId(3);
        }
    }

    @Getter
    @Setter
    private static class Location {

        public final String type = "Location";
        public Integer id;

    }

    @Getter
    @Setter
    private static class ClientReporter {

        public final String type = "Client";
        public Integer id;

    }

    @Getter
    @Setter
    private static class ClientTech {

        public final String type = "Tech";
        public Integer id;

    }

    @Getter
    @Setter
    private static class PriorityType {

        public final String type = "PriorityType";
        public Integer id;

    }

    @Getter
    @Setter
    private static class StatusType {

        public final String type = "StatusType";
        public Integer id;

    }

    @Data
    private static class ProblemType {

        public final String type = "ProblemType";
        public Integer id;

    }
}

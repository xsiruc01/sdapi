package cz.sd.api.freshservice.ticket.incident.json;

import cz.sd.api.freshservice.convertor.FreshservicePriorityConvertor;
import cz.sd.api.freshservice.convertor.FreshserviceStatusConvertor;
import cz.sd.api.ticket.TicketCreateData;
import cz.sd.api.ticket.TicketCreateRequest;
import cz.sd.api.ticket.TicketType;
import lombok.Getter;
import lombok.Setter;

/**
 * Trida reprezentujici pozadavek na vytvoreni incidentu. 1:1 s JSON requestem
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class FreshserviceIncidentCreateRequest extends TicketCreateRequest {

    /**
     * Request data.
     */
    @Getter
    @Setter
    private HelpDeskTicketData helpdesk_ticket;

    /**
     * Konfiguracni polozka.
     */
    private AssociatedAsset associate_ci = null;

    /**
     * Vytvori instanci pozadavku na vytvoreni incidentu.
     *
     * @param createData Informace o vytvarenem ticketu.
     */
    public FreshserviceIncidentCreateRequest(TicketCreateData createData) {
        this.helpdesk_ticket = new HelpDeskTicketData();
        this.helpdesk_ticket.setDescription(createData.getDetail());
        this.helpdesk_ticket.setSubject(createData.getSubject());
        // zadavatel
        if (createData.getRequester() != null && createData.getRequester().getId() != null) {
            this.helpdesk_ticket.setRequester_id(createData.getRequester().getId());
        }
        if (createData.getRequester() != null && createData.getRequester().getName() != null) {
            this.helpdesk_ticket.setEmail(createData.getRequester().getName());
        }
        // technik
        if (createData.getTechnician() != null && createData.getTechnician().getId() != null) {
            this.helpdesk_ticket.setResponder_id(createData.getTechnician().getId());
        }
        
        this.helpdesk_ticket.setPriority(FreshservicePriorityConvertor.convertToFreshServicePriority(createData.getPriority()));
        this.helpdesk_ticket.setStatus(FreshserviceStatusConvertor.convertToFreshserviceStatus(createData.getStatus()));
        this.helpdesk_ticket.setSource(2);
        // Freshservice podporuje pouze incidenty
        this.helpdesk_ticket.setTicket_type(TicketType.INCIDENT.getName());
    }

    public void setAssociatedItemName(String name) {
        this.associate_ci = new AssociatedAsset(name);
    }

    /**
     * Objekt obsahujici vlastni data pro vytvoreni ticketu.
     *
     * @author Pavel Sirucek
     */
    @Getter
    @Setter
    private static class HelpDeskTicketData {

        /**
         * Popis ticketu.
         */
        public String description;
        /**
         * Predmet ticketu.
         */
        public String subject;
        /**
         * Email zadavatele.
         */
        public String email;
        /**
         * Id zadavatele.
         */
        public Long requester_id;
        /**
         * Id technika.
         */
        public Long responder_id;
        /**
         * Priorita.
         */
        public Integer priority;
        /**
         * Status.
         */
        public Integer status;
        /**
         * Zdroj pozadavku.
         */
        public Integer source;
        /**
         * Typ ticketu. Zde bude "Incident".
         */
        public String ticket_type;
    }

    /**
     * Asociovana konfiguracni polozka.
     */
    private class AssociatedAsset {

        public AssociatedAsset(String name) {
            this.name = name;
        }

        private final String name;
    }
}

package cz.sd.api.freshservice.ticket.incident.json;

import cz.sd.api.freshservice.convertor.FreshservicePriorityConvertor;
import cz.sd.api.freshservice.convertor.FreshserviceStatusConvertor;
import cz.sd.api.ticket.TicketUpdateData;
import lombok.Getter;

/**
 * Update request pro FreshService. Je mozne nastavovat nasledujici hodnoty.
 * <ul>
 * <li>Subject - predmet ticketu</li>
 * <li>Priorita ticketu</li>
 * <li>Stav ticketu</li>
 * </ul>
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class FreshserviceIncidentUpdateRequest {

    /**
     * Vlastni data tiketu.
     */
    @Getter
    private final FreshserviceIncidentData helpdesk_ticket;
    /**
     * Konfiguracni polozka.
     */
    private AssociatedAsset associate_ci = null;

    public FreshserviceIncidentUpdateRequest(TicketUpdateData data) {
        this.helpdesk_ticket = new FreshserviceIncidentData();

        if (data.getTicketId() != -1) { // pokud je ticket id -1, tak se pravdepodobne jedna o slinkovani CI s ticketem, ale i tak by nebylo podle id -1 co upravovat :)
        /*
             * Ve FreshService neni mozne upravit popis ticketu.
             */
            //this.helpdesk_ticket.setDescription(data.getDetail());
            this.helpdesk_ticket.setSubject(data.getSubject());
            this.helpdesk_ticket.setPriority(data.getPriority() != null ? FreshservicePriorityConvertor.convertToFreshServicePriority(data.getPriority()) : null);
            this.helpdesk_ticket.setStatus(data.getStatus() != null ? FreshserviceStatusConvertor.convertToFreshserviceStatus(data.getStatus()) : null);
            //this.helpdesk_ticket.setTicket_type(data.getType().getName());
        }
    }

    /**
     * Nastavi konfigurani polozku.
     *
     * @param name Jmeno konfiguracni polozky.
     */
    public void setAssociatedItemName(String name) {
        this.associate_ci = new AssociatedAsset(name);
    }

    /**
     * Asociovana konfigracni polozka.
     */
    private class AssociatedAsset {

        /**
         * Jmeno konfiguracni polozky.
         */
        private final String name;

        /**
         * Vytvori instanci asociaovane konfiguracni polozky.
         *
         * @param name Jmeno konfiguracni polozky.
         */
        public AssociatedAsset(String name) {
            this.name = name;
        }
    }
}

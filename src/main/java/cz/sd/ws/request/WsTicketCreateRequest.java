package cz.sd.ws.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Request pro vytvoreni ticketu v service desku.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter @Setter
public class WsTicketCreateRequest {
    /**
     * System.
     */
    private String system;
    /**
     * Typ ticketu.
     */
    private String ticketType;
    /**
     * Predmet ticketu.
     */
    private String subject;
    /**
     * Popis ticketu.
     */
    private String description;    
    /**
     * Priorita.
     */
    private String priority;
    /**
     * Status.
     */
    private String status;
    /**
     * Id konfiguracni polozky.
     */
    private Long configItemId;    
    /**
     * Tvurce tiketu - jmeno.
     */
    private String requesterName;
    /**
     * Tvurce tiketu - id.
     */
    private Long requesterId;
    /**
     * Technik - jmeno.
     */
    private String technicianName;
    /**
     * Technik - id.
     */
    private Long technicianId;
    
    // pouze pro ALVAO /////////////////////////////////////////////////////////
    /**
     * Sluzba.
     */
    private String service;
    /**
     * SLA.
     */
    private String sla;
    
    // pouze pro iTop //////////////////////////////////////////////////////////
    /**
     * Organizace.
     */
    private String organization;
    /**
     * Id organizace.
     */
    private Long organizationId;
    
    // pouze pro ManageEngine //////////////////////////////////////////////////
    /**
     * Skupina problemu (Hardware Problems, Network, Printer Problems).
     */
    private String group;
}

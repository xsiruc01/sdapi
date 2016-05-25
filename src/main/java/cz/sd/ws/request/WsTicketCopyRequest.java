package cz.sd.ws.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Pozadavek na zkopirovani ticketu z jednoho systemu do druheho.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class WsTicketCopyRequest {

    /**
     * Zdrojovy system.
     */
    private String srcSystem;
    /**
     * Cilovy system.
     */
    private String destSystem;
    /**
     * Typ zdrojoveho ticketu.
     */
    private String ticketType;
    /**
     * Id ticketu.
     */
    private Long ticketId;
    /**
     * Id konfiguracni polozky.
     */
    private Long configItemId;

    // pouze ALVAO /////////////////////////////////////////////////////////////
    /**
     * Sluzba.
     */
    private String service;
    /**
     * SLA.
     */
    private String sla;

    // pouze iTop //////////////////////////////////////////////////////////////
    /**
     * Organizace.
     */
    private String organization;
    /**
     * Id organizace.
     */
    private Long organizationId;
    
    // pouze ManageEngine //////////////////////////////////////////////////////
    /**
     * Skupina problemu (Hardware Problems, Network, Printer Problems).
     */
    private String group;
    
    // uzivatele ///////////////////////////////////////////////////////////////
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
}

package cz.sd.api;

import cz.sd.api.ticket.TicketPriority;
import cz.sd.api.ticket.TicketStatus;
import cz.sd.api.ticket.TicketType;
import cz.sd.api.users.ServiceDeskUser;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Predek vsech ticketu service desk nebo helpdesk systemu pouzivanych v api.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public abstract class ServiceDeskTicket {

    /**
     * Nazev/identifikator service desku.
     */
    private String sdSystemName;
    /**
     * Typ service desku.
     */
    private SdSystemType sdType;
    /**
     * Identifikator ticketu.
     */
    private long ticketId;
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
     * Poznamky.
     */
    private List<ServiceDeskMessage> notes;
    /**
     * Konfiguracni polozka ke ktere je ticket pridruzen. Muze byt null.
     */
    private Long configItemId;
    /**
     * Datum vytvoreni.
     */
    private String createdDate;
    /**
     * Datum posledni upravy.
     */
    private String lastUpdatedDate;
    /**
     * Deadline ticketu.
     */
    private String deadlineDate;
    /**
     * Tvurce ticketu.
     */
    private ServiceDeskUser requester;
    /**
     * Prideleny technik/resitel/agent.
     */
    private ServiceDeskUser technician;    
    /**
     * Service Level Agreement - SLA.
     */
    private String sla;
    /**
     * Kategorie sluzeb.
     */
    private String category;
}

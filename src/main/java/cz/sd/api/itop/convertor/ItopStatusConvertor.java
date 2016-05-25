package cz.sd.api.itop.convertor;

import cz.sd.api.ticket.TicketStatus;
import cz.sd.api.ticket.TicketType;

/**
 * Konverze stavu sdapi do stavu v iTop sd a naopak.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ItopStatusConvertor {

    public static final String NEW = "new";
    public static final String ASSIGNED = "assigned";
    public static final String PENDING = "pending";
    public static final String RESOLVED = "resolved";
    public static final String CLOSED = "closed";
    // stavy u zmen
    public static final String PLANNING = "plannedscheduled";
    public static final String IMPLEMENTATION = "implemented";
    public static final String MONITORING = "monitored";

    /**
     * Zkonvertuje stav ticketu v api do stavu pouzivanemu v iTop sd.
     *
     * @param status Stav ticketu v sdapi.
     * @param ticketType Typ tiketu.
     * @return Nazev stavu incidentu v iTop.
     */
    public static TicketStatus convertFromItopStatus(String status, TicketType ticketType) {
        switch (status) {
            case NEW:
                return TicketStatus.OPEN;
            case ASSIGNED:
                if (ticketType == TicketType.CHANGE) {
                    return TicketStatus.ANALYSIS;
                }
                return TicketStatus.OPEN;
            case PENDING:
                return TicketStatus.ON_HOLD;
            case RESOLVED:
                return TicketStatus.RESOLVED;
            case CLOSED:
                return TicketStatus.CLOSED;
            // zmeny
            case PLANNING:
                return TicketStatus.PLANNING;
            case IMPLEMENTATION:
                return TicketStatus.IMPLEMENTATION;
            case MONITORING:
                return TicketStatus.MONITORING;

            default:
                return null;
        }
    }

    /**
     * Zkonvertuje stav ticketu v iTop do stavu pouzivanemu v sdapi.
     *
     * @param status Stav ticketu v iTop.
     * @param ticketType Typ tiketu.
     * @return Nazev stavu incidentu v sdapi.
     */
    public static String convertToItopStatus(TicketStatus status, TicketType ticketType) {
        if (status != null) {
            switch (status) {
                case OPEN:
                    return ASSIGNED;
                case ON_HOLD:
                    return PENDING;
                case RESOLVED:
                    if (ticketType == TicketType.CHANGE) {
                        return MONITORING;
                    }
                    return RESOLVED;
                case CLOSED:
                    return CLOSED;
                // zmeny
                case ANALYSIS:
                    return ASSIGNED;
                case PLANNING:
                    return PLANNING;
                case IMPLEMENTATION:
                    return IMPLEMENTATION;
                case MONITORING:
                    return MONITORING;
            }
        }
        return null;
    }
}

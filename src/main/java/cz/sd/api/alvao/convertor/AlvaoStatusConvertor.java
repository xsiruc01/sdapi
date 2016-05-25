package cz.sd.api.alvao.convertor;

import cz.sd.api.alvao.database.dao.IAlvaoDbTicketDao;
import cz.sd.api.ticket.TicketStatus;

/**
 * Konverzni trida pro stavy tiketu mezi rozhranim a systemem ALVAO.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class AlvaoStatusConvertor {

    // nazev stavu - v cestine
    private static final String INCIDENT_NEW_ID = "Nový";
    private static final String INCIDENT_IN_PROCESS_ID = "Řešení";
    private static final String INCIDENT_RESOLVED_ID = "Vyřešeno";
    private static final String INCIDENT_CLOSED_ID = "Uzavřeno";

    private static final String PROBLEM_NEW_ID = "Nový";
    private static final String PROBLEM_DIAGNOSTIC_ID = "Diagnostika";
    private static final String PROBLEM_KNOWN_ERROR_ID = "Známá chyba";
    private static final String PROBLEM_RESOLVED_ID = "Vyřešeno";
    private static final String PROBLEM_CLOSED_ID = "Uzavřeno";

    private static final String CHANGE_NEW_ID = "Nový";
    private static final String CHANGE_ANALYZE_ID = "Analýza";
    private static final String CHANGE_APPROVAL_ID = "Schvalování";
    private static final String CHANGE_PLANNING_ID = "Plánování";
    private static final String CHANGE_REALIZE_ID = "Realizace";
    private static final String CHANGE_TESTING_ID = "Testování";
    private static final String CHANGE_RESOLVED_ID = "Vyřešeno";
    private static final String CHANGE_CLOSED_ID = "Uzavřeno";

    /**
     * Prevadi stav pouzivany v rozhrani na stav ALVAO incidentu. Stav je
     * preveden na odpovidajici (pripadne co nejvice podobny) textovy retezec
     * pouzity v ALVAO sd.
     *
     * @param status Stav incident tiketu v rozhrani.
     * @param dao ALVAO tiket DAO.
     * @return Nazev stavu incidentu v ALVAO.
     */
    public static String convertToAlvaoIncidentStatus(TicketStatus status, IAlvaoDbTicketDao dao) {
        if (status != null) {
            switch (status) {
                case OPEN:
                    //alvaoStatus = dao.getStatusByStatusId(INCIDENT_NEW_ID);
                    return INCIDENT_NEW_ID;
                case ON_HOLD:
                    //alvaoStatus = dao.getStatusByStatusId(INCIDENT_NEW_ID);
                    return INCIDENT_NEW_ID;
                case RESOLVED:
                    //alvaoStatus = dao.getStatusByStatusId(INCIDENT_RESOLVED_ID);
                    return INCIDENT_RESOLVED_ID;
                case CLOSED:
                    //alvaoStatus = dao.getStatusByStatusId(INCIDENT_CLOSED_ID);
                    return INCIDENT_CLOSED_ID;
            }
        }
        return null;
    }

    /**
     * Prevadi stav pouzivany v rozhrani na stav ALVAO problemu. Stav je
     * preveden na odpovidajici (pripadne co nejvice podobny) textovy retezec
     * pouzity v ALVAO sd.
     *
     * @param status Stav problem ticketu v rozhrani.
     * @param dao ALVAO ticket DAO.
     * @return Nazev stavu problemu v ALVAO.
     */
    public static String convertToAlvaoProblemStatus(TicketStatus status, IAlvaoDbTicketDao dao) {
        if (status != null) {
            switch (status) {
                case OPEN:
                    //alvaoStatus = dao.getStatusByStatusId(PROBLEM_NEW_ID);
                    return PROBLEM_NEW_ID;
                case ON_HOLD:
                    //alvaoStatus = dao.getStatusByStatusId(PROBLEM_DIAGNOSTIC_ID);
                    return PROBLEM_DIAGNOSTIC_ID;
                case RESOLVED:
                    //alvaoStatus = dao.getStatusByStatusId(PROBLEM_RESOLVED_ID);
                    return PROBLEM_RESOLVED_ID;
                case CLOSED:
                    //alvaoStatus = dao.getStatusByStatusId(PROBLEM_CLOSED_ID);
                    return PROBLEM_CLOSED_ID;
            }
        }
        return null;
    }

    /**
     * Prevadi stav pouzivany v rozhrani na stav ALVAO zmeny. Stav je preveden
     * na odpovidajici (pripadne co nejvice podobny) textovy retezec pouzity v
     * ALVAO sd.
     *
     * @param status Stav zmeny ticketu v rozhrani.
     * @param dao ALVAO ticket DAO.
     * @return Nazev stavu zmeny v ALVAO.
     */
    public static String convertToAlvaoChangeStatus(TicketStatus status, IAlvaoDbTicketDao dao) {
        if (status != null) {
            switch (status) {
                case OPEN:
                    //alvaoStatus = dao.getStatusByStatusId(CHANGE_NEW_ID);
                    return CHANGE_NEW_ID;
                case ON_HOLD:
                    //alvaoStatus = dao.getStatusByStatusId(CHANGE_ANALYZE_ID);
                    return CHANGE_ANALYZE_ID;
                case RESOLVED:
                    //alvaoStatus = dao.getStatusByStatusId(CHANGE_RESOLVED_ID);
                    return CHANGE_RESOLVED_ID;
                case CLOSED:
                    //alvaoStatus = dao.getStatusByStatusId(CHANGE_CLOSED_ID);
                    return CHANGE_CLOSED_ID;
                case ANALYSIS:
                    return CHANGE_ANALYZE_ID;
                case PLANNING:
                    return CHANGE_PLANNING_ID;
                case IMPLEMENTATION:
                    return CHANGE_REALIZE_ID;
                case MONITORING:
                    return CHANGE_TESTING_ID;
            }
        }
        return null;
    }

    /**
     * Prevadi stav pouzivany v ALVAO sd na stav incidentu v rozhrani.
     *
     * @param status Stav incidentu v systemu ALVAO sd.
     * @param dao ALVAO ticket DAO.
     * @return Stav incidentu v rozhrani.
     */
    public static TicketStatus convertFromAlvaoIncidentStatus(String status, IAlvaoDbTicketDao dao) {
        switch (status) {
            case INCIDENT_NEW_ID:
                return TicketStatus.OPEN;
            case INCIDENT_IN_PROCESS_ID:
                return TicketStatus.OPEN;
            case INCIDENT_RESOLVED_ID:
                return TicketStatus.RESOLVED;
            case INCIDENT_CLOSED_ID:
                return TicketStatus.CLOSED;
            default:
                return null;
        }
    }

    /**
     * Prevadi stav pouzivany v ALVAO sd na stav problemu v rozhrani.
     *
     * @param status Stav problemu v systemu ALVAO sd.
     * @param dao ALVAO ticket DAO.
     * @return Stav problemu v rozhrani.
     */
    public static TicketStatus convertFromAlvaoProblemStatus(String status, IAlvaoDbTicketDao dao) {
        switch (status) {
            case PROBLEM_NEW_ID:
                return TicketStatus.OPEN;
            case PROBLEM_DIAGNOSTIC_ID:
                return TicketStatus.ON_HOLD;
            case PROBLEM_KNOWN_ERROR_ID:
                return TicketStatus.OPEN;
            case PROBLEM_RESOLVED_ID:
                return TicketStatus.RESOLVED;
            case PROBLEM_CLOSED_ID:
                return TicketStatus.CLOSED;
            default:
                return null;
        }
    }

    /**
     * Prevadi stav pouzivany v ALVAO sd na stav zmeny v rozhrani.
     *
     * @param status Stav zmeny v systemu ALVAO sd.
     * @param dao ALVAO ticket DAO.
     * @return Stav zmeny v rozhrani.
     */
    public static TicketStatus convertFromAlvaoChangeStatus(String status, IAlvaoDbTicketDao dao) {
        switch (status) {
            case CHANGE_NEW_ID:
                return TicketStatus.OPEN;
            case CHANGE_ANALYZE_ID:
                return TicketStatus.ANALYSIS;
            case CHANGE_APPROVAL_ID:
                return TicketStatus.ANALYSIS;
            case CHANGE_PLANNING_ID:
                return TicketStatus.PLANNING;
            case CHANGE_REALIZE_ID:
                return TicketStatus.IMPLEMENTATION;
            case CHANGE_TESTING_ID:
                return TicketStatus.MONITORING;
            case CHANGE_RESOLVED_ID:
                return TicketStatus.RESOLVED;
            case CHANGE_CLOSED_ID:
                return TicketStatus.CLOSED;
            default:
                return null;
        }
    }
}

package cz.sd.api.manageengine.convertor;

import cz.sd.api.ticket.TicketStatus;

/**
 * Konverzni trida pro prevod stavu mezi rozhranim a ManageEngine service
 * deskem.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ManageEngineStatusConvertor {

    public static final String MANAGE_ENGINE_OPEN = "Open";
    public static final String MANAGE_ENGINE_ON_HOLD_RESPONSE = "Onhold";
    public static final String MANAGE_ENGINE_ON_HOLD_REQUEST = "On Hold";
    public static final String MANAGE_ENGINE_RESOLVED = "Resolved";
    public static final String MANAGE_ENGINE_CLOSED = "Closed";

    /**
     * Prevadi stav v ManageEngine na stav v rozhrani.
     *
     * @param status Stav ManageEngine.
     * @return Stav v rozhrani.
     */
    public static TicketStatus convertFromManageEngineStatus(String status) {
        /*
         Open 	
         On Hold 	
         Resolved	
         Closed 	
         */
        switch (status) {
            case MANAGE_ENGINE_OPEN:
                return TicketStatus.OPEN;
            case MANAGE_ENGINE_ON_HOLD_RESPONSE:
                return TicketStatus.ON_HOLD;
            case MANAGE_ENGINE_RESOLVED:
                return TicketStatus.RESOLVED;
            case MANAGE_ENGINE_CLOSED:
                return TicketStatus.CLOSED;
            default:
                return null;
        }
    }

    /**
     * Prevadi stav v rozhrani na stav v ManageEngine.
     *
     * @param status Stav v rozhrani.
     * @return Stav v ManageEngine.
     */
    public static String convertToManageEngineStatus(TicketStatus status) {
        if (status != null) {
            switch (status) {
                case OPEN:
                    return MANAGE_ENGINE_OPEN;
                case ON_HOLD:
                    return MANAGE_ENGINE_ON_HOLD_REQUEST;
                case RESOLVED:
                    return MANAGE_ENGINE_RESOLVED;
                case CLOSED:
                    return MANAGE_ENGINE_CLOSED;
            }
        }
        return null;
    }
}

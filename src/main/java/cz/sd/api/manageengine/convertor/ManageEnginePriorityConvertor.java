package cz.sd.api.manageengine.convertor;

import cz.sd.api.ticket.TicketPriority;

/**
 * Konverzni trida pro prevod priority mezi rozhranim a ManageEngine service
 * deskem.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ManageEnginePriorityConvertor {

    public static final String MANAGE_ENGINE_LOW = "Low";
    public static final String MANAGE_ENGINE_NORMAL = "Normal";
    public static final String MANAGE_ENGINE_MEDIUM = "Medium";
    public static final String MANAGE_ENGINE_HIGH = "High";

    /**
     * Prevadi prioritu ManageEngine na prioritu v rozhrani.
     *
     * @param priority Priorita ManageEngine.
     * @return Priorita v rozhrani.
     */
    public static TicketPriority convertFromManageEnginePriority(String priority) {
        /*
         Low 	
         Normal 	
         Medium 	
         High 	
         */
        switch (priority) {
            case MANAGE_ENGINE_LOW:
                return TicketPriority.LOW;
            case MANAGE_ENGINE_NORMAL:
                return TicketPriority.NORMAL;
            case MANAGE_ENGINE_MEDIUM:
                return TicketPriority.HIGH;
            case MANAGE_ENGINE_HIGH:
                return TicketPriority.CRITICAL;
            default:
                return null;
        }
    }

    /**
     * Prevadi prioritu v rozhrani na prioritu ManageEngine.
     *
     * @param priority Priorita v rozhrani.
     * @return Priorita v ManageEngine.
     */
    public static String convertToManageEnginePriority(TicketPriority priority) {
        if (priority != null) {
            switch (priority) {
                case LOW:
                    return MANAGE_ENGINE_LOW;
                case NORMAL:
                    return MANAGE_ENGINE_NORMAL;
                case HIGH:
                    return MANAGE_ENGINE_MEDIUM;
                case CRITICAL:
                    return MANAGE_ENGINE_HIGH;
            }
        }
        return null;
    }
}

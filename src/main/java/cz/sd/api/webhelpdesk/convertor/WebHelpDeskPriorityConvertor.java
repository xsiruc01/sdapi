package cz.sd.api.webhelpdesk.convertor;

import cz.sd.api.ticket.TicketPriority;

/**
 * Konverzni trida pro prevod priority mezi rozhranim a Web Help Desk systemem.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class WebHelpDeskPriorityConvertor {

    /**
     * Prevede prioritu Web Help Desk an prioritu v rozhrani.
     *
     * @param priority Priorita Web Help Desk.
     * @return Priorita v rozhrani.
     */
    public static TicketPriority convertFromWebHelpDeskPriority(int priority) {
        /*
         Low 	4
         Medium 	3
         High 	2
         Urgent 	1
         */
        switch (priority) {
            case 1:
                return TicketPriority.CRITICAL;
            case 2:
                return TicketPriority.HIGH;
            case 3:
                return TicketPriority.NORMAL;
            case 4:
                return TicketPriority.LOW;
            default:
                return null;
        }
    }

    /**
     * Prevede prioritu rozhrani na prioritu Web Help Desk.
     *
     * @param priority Priorita v rozhrani.
     * @return Priorita Web Help Desk.
     */
    public static Integer convertToWebHelpDeskPriority(TicketPriority priority) {
        if (priority != null) {
            switch (priority) {
                case LOW:
                    return 4;
                case NORMAL:
                    return 3;
                case HIGH:
                    return 2;
                case CRITICAL:
                    return 1;
            }
        }
        return null;
    }
}

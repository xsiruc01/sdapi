package cz.sd.api.freshservice.convertor;

import cz.sd.api.ticket.TicketPriority;

/**
 * Trida pro konverzi priorit tiketu Freshservice na prioritu rozhrani a naopak.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class FreshservicePriorityConvertor {

    /**
     * Prevede prioritu Freshservice na prioritu pouzivanou v rozhrani.
     *
     * @param priority Priorita ve Freshservice.
     * @return Priorita pouzivana v rozhrani.
     */
    public static TicketPriority convertFromFreshServicePriority(int priority) {
        /*
         Low 	1
         Medium 2
         High 	3
         Urgent 4
         */
        switch (priority) {
            case 1:
                return TicketPriority.LOW;
            case 2:
                return TicketPriority.NORMAL;
            case 3:
                return TicketPriority.HIGH;
            case 4:
                return TicketPriority.CRITICAL;
            default:
                return null;
        }
    }

    /**
     * Prevede prioritu pouzivanou v rozhrani na prioritu Freshservice.
     *
     * @param priority Priorita pouzivana v rozhrani.
     * @return Priorita Freshservice.
     */
    public static Integer convertToFreshServicePriority(TicketPriority priority) {
        if (priority != null) {
            switch (priority) {
                case LOW:
                    return 1;
                case NORMAL:
                    return 2;
                case HIGH:
                    return 3;
                case CRITICAL:
                    return 4;
            }
        }
        return null;
    }
}

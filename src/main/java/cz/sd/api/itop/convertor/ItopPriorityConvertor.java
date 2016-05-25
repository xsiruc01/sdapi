package cz.sd.api.itop.convertor;

import cz.sd.api.ticket.TicketPriority;

/**
 * Trida pro konverzi priorit mezi systemem iTop a rozhranim. Pozor, prioritu
 * lze menit pomoci rest api itop pouze pres urgenci. Prioritu nelze nastavovat
 * u tiketu typu zmena.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ItopPriorityConvertor {

    /**
     * Prevede prioritu iTop na prioritu rozhrani.
     *
     * @param priority Priorita iTop.
     * @return Priorita rozhrani.
     */
    public static TicketPriority convertFromItopPriority(int priority) {
        /* Pouzivana nalehavost namisto priority, kterou nelze menit v GUI */
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
     * Prevede pruioritu v rozhrani na prioritu iTop.
     *
     * @param priority Priorita v rozhrani.
     * @return Priorita iTop.
     */
    public static Integer convertToItopPriority(TicketPriority priority) {
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

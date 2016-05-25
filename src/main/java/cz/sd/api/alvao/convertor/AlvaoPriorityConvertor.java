package cz.sd.api.alvao.convertor;

import cz.sd.api.ticket.TicketPriority;

/**
 * Konverzni trida pro priority mezi rozhranim a systemem ALVAO.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class AlvaoPriorityConvertor {

    /**
     * Prevadi prioritu systemu ALVAO na prioritu TicketPriority pouzivanou v
     * rozhrani.
     *
     * @param priority Identifikator priority ALVAO precteny z databaze.
     * @return Priorita pouzivana v rozhrani.
     */
    public static TicketPriority convertFromAlvaoPriority(int priority) {
        /*
         Planning 1
         Low 	 2
         Medium 	 3
         High 	 4
         Critical 5
         */
        switch (priority) {
            case 1:
                return TicketPriority.NORMAL;
            case 2:
                return TicketPriority.LOW;
            case 3:
                return TicketPriority.NORMAL;
            case 4:
                return TicketPriority.HIGH;
            case 5:
                return TicketPriority.CRITICAL;
            default:
                return null;
        }
    }

    /**
     * Prevadi prioritu pouzivanou v rozhrani na prioritu ALVAO. Priorita je
     * prevedena na odpovidajici (pripadne co nejvice podobnou) identifikator
     * priority v ALVAO sd.
     *
     * @param priority
     * @return Identifikator priority ALVAO.
     */
    public static Integer convertToAlvaoPriority(TicketPriority priority) {
        if (priority != null) {
            switch (priority) {
                case LOW:
                    return 2;
                case NORMAL:
                    return 3;
                case HIGH:
                    return 4;
                case CRITICAL:
                    return 5;
            }
        }
        return null;
    }
}

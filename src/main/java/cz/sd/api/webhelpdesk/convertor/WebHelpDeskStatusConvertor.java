package cz.sd.api.webhelpdesk.convertor;

import cz.sd.api.ticket.TicketStatus;

/**
 * Konverzni trida pro prevod stavu mezi rozhranim a Web Help Desk.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class WebHelpDeskStatusConvertor {

    /**
     * Prevede stav Web Help Desk na stav v rozhrani.
     *
     * @param status Stav Web Help Desk.
     * @return Stav v rozhrani.
     */
    public static TicketStatus convertFromWebHelpDeskStatus(int status) {
        /*
         Open      1
         Pending   2
         Cancelled 4
         Resolved  5
         Closed 	  3
         */
        switch (status) {
            case 1:
                return TicketStatus.OPEN;
            case 2:
                return TicketStatus.ON_HOLD;
            case 3:
                return TicketStatus.CLOSED;
            case 4:
                return TicketStatus.CLOSED; // 4 je Cancelled, vracime jako closed
            case 5:
                return TicketStatus.RESOLVED;
            default:
                return null;
        }
    }

    /**
     * Prevede stav v rozhrani na stav Web Help Desk.
     *
     * @param status Stav v rozhrani.
     * @return Stav Web Help Desk.
     */
    public static Integer convertToWebHelpDeskStatus(TicketStatus status) {
        if (status != null) {
            switch (status) {
                case OPEN:
                    return 1;
                case ON_HOLD:
                    return 2;
                case RESOLVED:
                    return 5;
                case CLOSED:
                    return 3;
            }
        }
        return null;
    }
}

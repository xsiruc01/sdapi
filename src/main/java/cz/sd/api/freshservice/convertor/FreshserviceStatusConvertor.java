package cz.sd.api.freshservice.convertor;

import cz.sd.api.ticket.TicketStatus;

/**
 * Trida pro konverzi stavu ticketu Freshservice na stav rozhrani a naopak.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class FreshserviceStatusConvertor {

    /**
     * Zkonvertuje stav Freshservice na stav rozhrani.
     *
     * @param status Stav ve Freshservice.
     * @return Stav v rozhrani.
     */
    public static TicketStatus convertFromFreshserviceStatus(int status) {
        /*
         Open      2
         Pending   3
         Resolved  4
         Closed    5
         */
        switch (status) {
            case 2:
                return TicketStatus.OPEN;
            case 3:
                return TicketStatus.ON_HOLD;
            case 4:
                return TicketStatus.RESOLVED;
            case 5:
                return TicketStatus.CLOSED;
            default:
                return null;
        }
    }

    /**
     * Zkonvertuje stav v rozhrani na stav FreshService.
     *
     * @param status Stav v rozhrani.
     * @return Stav ve Freshservice.
     */
    public static Integer convertToFreshserviceStatus(TicketStatus status) {
        if (status != null) {
            switch (status) {
                case OPEN:
                    return 2;
                case ON_HOLD:
                    return 3;
                case RESOLVED:
                    return 4;
                case CLOSED:
                    return 5;
            }
        }
        return null;
    }
}

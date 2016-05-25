package cz.sd.api.itop.ticket;

import cz.sd.api.SdSystemType;
import cz.sd.api.ServiceDeskTicket;

/**
 * Tiket v systemu iTop.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public abstract class ItopTicket extends ServiceDeskTicket {

    /**
     * Vytvori instanci s nastavenim systemu na iTop.
     */
    public ItopTicket() {
        setSdType(SdSystemType.ITOP);
    }

}

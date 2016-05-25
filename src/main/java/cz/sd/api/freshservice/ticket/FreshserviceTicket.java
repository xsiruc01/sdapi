package cz.sd.api.freshservice.ticket;

import cz.sd.api.SdSystemType;
import cz.sd.api.ServiceDeskTicket;

/**
 * Tiket systemu Freshservice.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public abstract class FreshserviceTicket extends ServiceDeskTicket {

    /**
     * Vytvori instanci tiketu s nastavenim systemu Freshservice.
     */
    public FreshserviceTicket() {
        setSdType(SdSystemType.FRESHSERVICE);
    }

}

package cz.sd.api.manageengine.ticket;

import cz.sd.api.SdSystemType;
import cz.sd.api.ServiceDeskTicket;

/**
 * Tiket v systemu ManageEngine.
 * @author Pavel Sirucek (xsiruc01)
 */
public abstract class ManageEngineTicket extends ServiceDeskTicket {
    /**
     * Vytvori instanci s nastaveni systemu ManageEngine.
     */
    public ManageEngineTicket() {
        setSdType(SdSystemType.MANAGE_ENGINE);
    }
}

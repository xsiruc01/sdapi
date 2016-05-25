package cz.sd.api.webhelpdesk.ticket;

import cz.sd.api.SdSystemType;
import cz.sd.api.ServiceDeskTicket;

/**
 * Tiket v systemu Web Help Desk.
 * @author Pavel Sirucek (xsiruc01)
 */
public class WebHelpDeskTicket extends ServiceDeskTicket {

    /**
     * Vytvori instanci s nastavenim ssytemu Web Help Desk.
     */
    public WebHelpDeskTicket() {
        setSdType(SdSystemType.WEB_HELP_DESK);
    }
    
}

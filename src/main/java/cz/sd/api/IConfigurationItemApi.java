package cz.sd.api;

import cz.sd.api.ticket.CiUpdateData;
import cz.sd.api.ticket.TicketType;

/**
 * Rozhrani umoznujici praci s konfiguracnimi polozkami v CMDB. Obsahuje metody
 * pro cteni polozek, jejich upravu a pripadne slinkovani s tikety.
 *
 * @author Pavel Širůček
 */
public interface IConfigurationItemApi {

    /**
     * Vrati informace o konfiguracni polozce.
     *
     * @param id Id konfiguracni polozky.
     * @return Konfiguracni polozka.
     * @throws cz.sd.api.ServiceDeskCommunicationException v pripade chyby pri
     * komunikaci se service deskem.
     */
    ConfigurationItem getCI(long id) throws ServiceDeskCommunicationException;

    /**
     * Priradi konfiguracni polozku k ticketu.
     *
     * @param ciId Id konfiguracni polozky.
     * @param ticketid Id ticketu.
     * @param type Typ ticketu.
     * @return Priznak, zda se prirazeni podarilo.
     * @throws cz.sd.api.ServiceDeskCommunicationException v pripade chyby pri
     * komunikaci se service deskem.
     */
    boolean linkCiToTicket(long ciId, long ticketid, TicketType type) throws ServiceDeskCommunicationException;

    /**
     * Upravi konfiguracni polozku.
     *
     * @param updateData Nova data.
     * @return Priznak, zda se uprava podarila.
     * @throws cz.sd.api.ServiceDeskCommunicationException v pripade chyby pri
     * komunikaci se service deskem.
     */
    public boolean updateCI(CiUpdateData updateData) throws ServiceDeskCommunicationException;
}

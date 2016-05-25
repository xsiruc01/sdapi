package cz.sd.api.alvao.database.dao;

import cz.sd.api.alvao.database.AlvaoDbConfigItem;
import cz.sd.api.ticket.CiUpdateData;

/**
 * DAO pro praci s konfiguracnimi polozkami/assety v ALVAO databazi.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public interface IAlvaoDbConfigItemDao {

    /**
     * Vrati databazovou konfiguracni polozku podle predaneho id.
     *
     * @param id Id konfiguracni polozky.
     * @return Konfiguracni polozka, tak jak je ulozena v ALVAO databazi. Vraci
     * null v pripade ze konfiguracni polozka neexsituje nebo v pripade chyby.
     */
    AlvaoDbConfigItem getCI(long id);

    /**
     * Priradi konfiguracni polozku k ticketu.
     *
     * @param ciId Id konfiguracni polozky.
     * @param ticketid Id ticketu.
     * @return Priznak, zda se prirazeni zdarilo. Priznak urcuje pouze, zda bylo
     * vykonani SQL v poradku.
     */
    boolean linkToTicket(long ciId, long ticketid);
    
    /**
     * Updateuje konfiguracni polozku.
     * @param updateData Nova data.
     * @return Priznak, zda se update podaril. Priznak urcuje pouze, zda bylo
     * vykonani SQL v poradku.
     */
    boolean updateCI(CiUpdateData updateData);
}

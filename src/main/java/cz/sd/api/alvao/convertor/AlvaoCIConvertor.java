package cz.sd.api.alvao.convertor;

import cz.sd.api.alvao.cmdb.AlvaoConfigurationItem;
import cz.sd.api.alvao.database.AlvaoDbConfigItem;

/**
 * Konverzni trida pro konfiguracni polozky. Prevadi databazove informace o
 * konfiguracni polozce do objektu rozhrani ConfigurationItem. // @TODO doplnit
 * cteni nejakych informaci
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class AlvaoCIConvertor {

    /**
     * Prevadi informace z databaze do objektu konfiguracni polozky
     * ConfigurationItem.
     *
     * @param dbCI Databazove informace o konfiguracni polozce.
     * @return Konfiguracni polozka.
     */
    public static AlvaoConfigurationItem convert(AlvaoDbConfigItem dbCI) {
        AlvaoConfigurationItem ci = new AlvaoConfigurationItem();

        ci.setName(dbCI.getName());
        ci.setId(dbCI.getNodeId());
        //ci.setTypeName(dbCI.getCITypeName());
        //ci.setTypeId(dbCI.getCITypeId());
        //ci.setProductName(dbCI.getProductName());
        //ci.setState(dbCI.getState());
        ci.setDetails(dbCI.getProperties());

        return ci;
    }
}

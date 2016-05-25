package cz.sd.api.itop.cmdb;

import cz.sd.api.ConfigurationItem;
import cz.sd.api.itop.cmdb.json.ItopCIDefaultResponse;
import cz.sd.api.itop.cmdb.json.ItopCIDetailResponse;

/**
 * Konverze odpovedi webovych sluzeb itop na ziskani konfigurani polozky do
 * objektu ConfigurationItem.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ItopCIConvertor {

    /**
     * Prevede odpoved webovych sluzeb na konfiguracni polozku.
     *
     * @param base Odpoved ws itop.
     * @param detail Detailni parametry vazane k typu konfiguracni polozky.
     * @return Konfiguracni polozka v rozhrani.
     */
    public static ConfigurationItem convert(ItopCIDefaultResponse base, ItopCIDetailResponse detail) {
        ItopConfigurationItem ci = new ItopConfigurationItem();

        ci.setName(base.getName());
        ci.setId(base.getId());
        ci.setDescription(base.getDescription());
        ci.setTypeName(base.getFinalClass());
        //ci.setTypeId(base.);
        ci.setProductName(detail.getDetails().get("model_name").toString());
        ci.setStatus((String) detail.getDetails().get("status"));
        ci.setDetails(detail.getDetails());

        return ci;
    }
}

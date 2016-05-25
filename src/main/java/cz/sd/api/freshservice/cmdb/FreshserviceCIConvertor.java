package cz.sd.api.freshservice.cmdb;

import cz.sd.api.freshservice.cmdb.json.FreshserviceCIResponse;
import cz.sd.api.ConfigurationItem;

/**
 * Konverzni trida pro prevod odpovedi z webovych sluzeb Freshservice na
 * konfiguracni polozku pouzivanou v rozhrani.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class FreshserviceCIConvertor {

    /**
     * Prevede odpoved Freshservice na ConfigurationItem.
     *
     * @param response Odpoved Freshservice pri ziskani konfiguracni polozky.
     * @return Konfiguracni polozka.
     */
    public static ConfigurationItem convert(FreshserviceCIResponse response) {
        FreshserviceConfigurationItem ci = new FreshserviceConfigurationItem();

        ci.setName(response.getName());
        ci.setDescription(response.getDescription());
        ci.setId(response.getId());
        ci.setTypeName(response.getCITypeName());
        ci.setTypeId(response.getCITypeId());
        ci.setProductName(response.getProductName());
        ci.setStatus(response.getState());
        ci.setDetails(response.getDetails());

        return ci;
    }
}

package cz.sd.api.freshservice.cmdb.json;

import cz.sd.api.ticket.CiUpdateData;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Request pro upravu konfiguracni polozky.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class FreshserviceCIUpdateRequest {

    /**
     * Obalka na upravovana data.
     */
    private ConfigItem cmdb_config_item;

    /**
     * Vytvori request pro upravu konfiguracni polozky.
     *
     * @param updateData Upravovana data konfiguracni polozky.
     */
    public FreshserviceCIUpdateRequest(CiUpdateData updateData) {
        this.cmdb_config_item = new ConfigItem();

        this.cmdb_config_item.setName(updateData.getName());
        this.cmdb_config_item.setDescription(updateData.getDescription());
        this.cmdb_config_item.setState_name(updateData.getStatus());
        this.cmdb_config_item.setProduct_name(updateData.getProductName());
        // custom parametry
        if (updateData.getDetails() != null) {
            for (Map.Entry<String, Object> entrySet : updateData.getDetails().entrySet()) {
                String key = entrySet.getKey();
                Object value = entrySet.getValue();
                this.addDetailParameter(key, value);
            }
        }

    }

    /**
     * Prida custom parametr k uprave.
     *
     * @param key Klic.
     * @param value Hodnota.
     */
    private void addDetailParameter(String key, Object value) {
        if (this.cmdb_config_item.getLevel_field_attributes()!= null) {
            this.cmdb_config_item.getLevel_field_attributes().put(key, value);
        }
    }

    /**
     * Data, ktera lze upravovat v konfiguracni polozce.
     */
    @Getter
    @Setter
    private class ConfigItem {

        private String name;
        private String description;
        private String product_name;
        private String state_name;
        //private String business_impact;
        private Long impact;
        private String vendor_name;
        /**
         * Mapa informaci vazanych ke konkretnimu typu polozky.
         */
        private Map<String, Object> level_field_attributes = new HashMap<>();
    }
}

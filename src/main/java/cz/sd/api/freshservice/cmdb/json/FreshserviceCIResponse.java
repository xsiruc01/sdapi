package cz.sd.api.freshservice.cmdb.json;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Response s informacemi o konfiguracni polozce. 1:1 s JSON odpovedi.
 *
 * @author Pavel Širůček
 */
public class FreshserviceCIResponse {

    /**
     * Informace o konfiguracni polozce.
     */
    @Getter
    @Setter
    private ConfigItem config_item;

    /**
     * Vrati informace vazane k typu polozky ve forme mapy.
     *
     * @return Mapa informaci o konfigurani polozce.
     */
    public Map<String, String> getDetails() {
        return this.config_item.getLevelfield_values();
    }

    /**
     * Vrati jmeno konfiguracni polozky.
     *
     * @return Jmeno konfiguracni polozky.
     */
    public String getName() {
        return this.config_item.getName();
    }

    /**
     * Vrati jemno typu konfiguracni polozky.
     *
     * @return Jmeno typu konfiguracni polozky.
     */
    public String getCITypeName() {
        return this.config_item.getCi_type_name();
    }

    /**
     * Vrati identifikator konfiguracni polozky.
     *
     * @return Identifikator konfiguracni polozky.
     */
    public Long getId() {
        return this.config_item.getDisplay_id();
    }

    /**
     * Vrati identifikator typu konfiguracni polozky.
     *
     * @return Identifikator konfiguracni polozky.
     */
    public Long getCITypeId() {
        return this.config_item.getCi_type_id();
    }

    /**
     * Vrati nazev produktu.
     *
     * @return Nazev produktu.
     */
    public String getProductName() {
        return this.config_item.getProduct_name();
    }

    /**
     * Vrati stav pouzivani konfiguracni polozky.
     *
     * @return Stav pouzivani konfiguracni polozky.
     */
    public String getState() {
        return this.config_item.getState_name();
    }

    /**
     * Vrati datum vytvoreni konfiguracni polozky.
     *
     * @return Datum vytvoreni konfiguracni polozky.
     */
    public String getCreatedDate() {
        return this.config_item.getCreated_at();
    }

    /**
     * Vrati popis konfiguracni polozky.
     *
     * @return Popis konfiguracni polozky.
     */
    public String getDescription() {
        return this.config_item.getDescription();
    }

    /**
     * Data v odpovedi.
     */
    @Getter
    @Setter
    private class ConfigItem {

        private Long agent_id;
        private String assigned_on;
        private Long ci_type_id;
        private String created_at;
        private Long department_id;
        private Long depreciation_id;
        private String description;
        private Long display_id;
        private Long id;
        private Long impact;
        private Long location_id;
        private String name;
        private String salvage;
        private String updated_at;
        private Long user_id;
        private String department_name;
        private String used_by;
        private String business_impact;
        private String agent_name;
        /**
         * Informace vazane primo ke konkretnimu typu konfiguracni polozky.
         */
        Map<String, String> levelfield_values;
        private String ci_type_name;
        private String product_name;
        private String vendor_name;
        private String state_name;
        private String location_name;
    }
}

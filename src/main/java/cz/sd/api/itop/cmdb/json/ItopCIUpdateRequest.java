package cz.sd.api.itop.cmdb.json;

import com.google.gson.annotations.SerializedName;
import cz.sd.api.ticket.CiUpdateData;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Request pro update CI v itop.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class ItopCIUpdateRequest {

    private final String operation = "core/update";
    private final String comment = "Updated via rest api";
    /**
     * Trida konfiguracni polozky.
     */
    @SerializedName("class")
    private final String _class;
    /**
     * Identifikator konfiguracni polozky.
     */
    private final String key;
    /**
     * Mapa parametru, ktere budou upraveny.
     */
    private Map<String, Object> fields = new HashMap<>();

    /**
     * Vytvori instanci pozadavku na upravu dat konfiguracni polozky.
     *
     * @param updateData Upravovana data.
     * @param finalClass Typ konfiguracni polozky.
     */
    public ItopCIUpdateRequest(CiUpdateData updateData, String finalClass) {
        this._class = finalClass;
        this.key = String.valueOf(updateData.getCiId());

        this.fields.put("name", updateData.getName());
        this.fields.put("description", updateData.getDescription());
        this.fields.put("business_criticity", updateData.getImpact()); // @TODO
        this.fields.put("status", updateData.getStatus());

        Map<String, Object> map = updateData.getDetails();
        for (Map.Entry<String, Object> entrySet : map.entrySet()) {
            String key1 = entrySet.getKey();
            Object value = entrySet.getValue();

            this.fields.put(key1, value.toString());

        }

    }

    /**
     * Update data CI.
     *
     * @author Pavel Sirucek
     */
    @Getter
    @Setter
    private class Fields {

        /**
         * Nazev polozky.
         */
        private String name;
        /**
         * Popis konfiguracni polozky.
         */
        private String description;
        /**
         * Dopad na obchod.
         */
        private String business_criticity;
        /**
         * Stav CI.
         */
        private String status;
    }
}

package cz.sd.api.itop.cmdb.json;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * Request pro ziskani konfiguracni polozky. 1:1 JSON.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ItopCIGetRequest {

    /**
     * Operace core/get.
     */
    private final String operation = "core/get";
    /**
     * Trida (typ) konfiguracni polozky.
     */
    @SerializedName("class")
    private final String _class;
    /**
     * Identifikator konfiguracni polozky.
     */
    @Getter
    private final String key;
    /**
     * Chceme vsechny informace, proto *.
     */
    private final String output_fields = "*";

    /**
     * Vytvori requset na konfiguracni polozku.
     *
     * @param id Id konfiguracni polozky.
     * @param configItemClass Trida konfiguracni polozky.
     */
    public ItopCIGetRequest(long id, String configItemClass) {
        this.key = String.valueOf(id);
        this._class = configItemClass;
    }
}

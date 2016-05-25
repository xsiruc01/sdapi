package cz.sd.api.itop.cmdb.json;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Detailni odpoved na ziskani konfiguracni polozky. Data se ziskavaji primo z
 * konkretni tridy CI (napr. PC, Tiskarna, OtherSoftware).
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ItopCIDetailResponse {

    private Map<String, FunctionalCIData> objects;
    private Integer code;
    private Object message;

    /**
     * Vrati identifikator konfiguracni polozky.
     *
     * @return Identifikator konfiguracni polozky.
     */
    public long getId() {
        return Long.valueOf(this.getFunctionalCIData().getKey());
    }

    private FunctionalCIData getFunctionalCIData() {
        FunctionalCIData ticketData = new FunctionalCIData();
        for (Map.Entry<String, FunctionalCIData> entrySet : this.objects.entrySet()) {
            ticketData = entrySet.getValue();
        }
        return ticketData;
    }

    /**
     * Vrati parametry konfiguracni polozky vazane k typu polozky.
     *
     * @return Mapa parametru informaci ke konfiguracni polozce.
     */
    public Map<String, Object> getDetails() {
        return this.getFunctionalCIData().getFields();
    }

    @Getter
    @Setter
    private class FunctionalCIData {

        private Integer code;
        private String message;
        private String key;
        private Map<String, Object> fields;
    }
}

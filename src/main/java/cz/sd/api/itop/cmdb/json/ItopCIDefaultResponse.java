package cz.sd.api.itop.cmdb.json;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Defaultni odpoved na ziskani konfiguracni polozky. Data se ziskavaji z
 * abstraktni tridy FunctionalCI, ktera obsahuje zakladni informace o CI.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ItopCIDefaultResponse {

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

    /**
     * Vrati nazev konfiguracni polozky.
     *
     * @return Nazev konfiguracni polozky.
     */
    public String getName() {
        return this.getFields().getName();
    }

    /**
     * Vrati popis konfiguracni polozky.
     *
     * @return Popis konfiguracni polozky.
     */
    public String getDescription() {
        return this.getFields().getDescription();
    }

    /**
     * Vrati tridu/typ konfiguracni polozky.
     *
     * @return Typ konfiguracni polozky.
     */
    public String getFinalClass() {
        return this.getFields().getFinalclass();
    }

    /**
     * Vrati vnitrni data konfiguracni polozky.
     *
     * @return
     */
    private FunctionalCIData getFunctionalCIData() {
        FunctionalCIData ticketData = new FunctionalCIData();
        for (Map.Entry<String, FunctionalCIData> entrySet : this.objects.entrySet()) {
            ticketData = entrySet.getValue();
        }
        return ticketData;
    }

    /**
     * Vrati parametry konfiguracni polozky.
     *
     * @return Vsechny parametry konfiguracni polozky.
     */
    private Fields getFields() {
        return this.getFunctionalCIData().getFields();
    }

    @Getter
    @Setter
    private class FunctionalCIData {

        private Integer code;
        private String message;
        private String key;
        private Fields fields;
    }

    @Getter
    @Setter
    private class Fields {

        private String name;
        private String description;
        private String org_id;
        private String organization_name;
        private String business_criticity;
        private String move2production;
        private List<Object> contacts_list;
        private List<Object> documents_list;
        private List<Object> applicationsolution_list;
        private List<Object> providercontracts_list;
        private List<Object> services_list;
        private List<Object> softwares_list;
        private List<Object> tickets_list;
        private String finalclass;
        private String friendlyname;
        private String org_id_friendlyname;
    }
}

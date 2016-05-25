package cz.sd.api.itop.cmdb;

import cz.sd.api.ConfigurationItem;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Konfiguracni polozka systemu iTop. Obsahuje detailni parametry polozky.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ItopConfigurationItem extends ConfigurationItem {

    /**
     * Mapa obsahujici detaily k prislusne konfiguracni polozce. Parametry muzou
     * byt jakekoliv, neni proto mozne vytvorit tridu, ktera by je
     * reprezentovala. V pripade Itop konfigracnich polozek obsahuje mapa
     * vsechny informace o CI. U disku lze z teto mapy vycist napr. informace o
     * velikosti a zbyvajicim miste na disku.
     */
    @Getter
    @Setter
    private Map<String, Object> details;
}

package cz.sd.api.freshservice.cmdb;

import cz.sd.api.ConfigurationItem;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Konfiguracni polozka v systemu FreshService.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class FreshserviceConfigurationItem extends ConfigurationItem {

    /**
     * Mapa obsahujici detaily k prislusne konfiguracni polozce. Parametry muzou
     * byt jakekoliv, neni proto mozne vytvorit tridu, ktera by je
     * reprezentovala. FreshService ma navic parametry doplneny o cislo (nejake
     * id, neprisel jsem na to podle ceho). U disku lze z teto mapy vycist napr.
     * informace o velikosti a zbyvajicim miste na disku.
     */
    @Getter
    @Setter
    private Map<String, String> details;
}

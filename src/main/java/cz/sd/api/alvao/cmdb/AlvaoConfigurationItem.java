package cz.sd.api.alvao.cmdb;

import cz.sd.api.ConfigurationItem;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Konfiguracni polozka systemu ALVAO Service Desk.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class AlvaoConfigurationItem extends ConfigurationItem {

    /**
     * Mapa obsahujici detaily k prislusne konfiguracni polozce. Parametry muzou
     * byt jakekoliv, neni proto mozne vytvorit tridu, ktera by je
     * reprezentovala.
     */
    private Map<String, String> details;
}

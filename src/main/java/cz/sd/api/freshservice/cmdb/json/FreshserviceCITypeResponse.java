package cz.sd.api.freshservice.cmdb.json;

import lombok.Getter;
import lombok.Setter;

/**
 * Odpoved s informacemi o typu konfiguracni polozky.
 * 
 * @author Pavel Širůček
 */
@Getter
@Setter
public class FreshserviceCITypeResponse {
    private String created_at;
    private String description;
    private String id;
    private String label;
    private String updated_at;

}

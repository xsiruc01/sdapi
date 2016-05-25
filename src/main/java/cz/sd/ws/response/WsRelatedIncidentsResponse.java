package cz.sd.ws.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Odpoved na pozadavek na vraceni seznamu incidentu souvisejicich s problemem.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class WsRelatedIncidentsResponse {

    /**
     * Seznam identifikatoru propojenych incidentu.
     */
    List<Long> incidents;
}

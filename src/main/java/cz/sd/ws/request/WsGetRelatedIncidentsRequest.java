package cz.sd.ws.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Pozadavek na vraceni seznamu incidentu, ktere jsou spojeny se zadanym
 * problemem jako jejich pricinou.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter @Setter
public class WsGetRelatedIncidentsRequest {

    /**
     * System.
     */
    private String system;
    /**
     * Identifikator ticketu.
     */
    private Long problemId;
}

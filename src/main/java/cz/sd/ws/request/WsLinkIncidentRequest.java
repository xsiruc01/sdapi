package cz.sd.ws.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Pozadavek na slinkovani incidentu s problemem.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class WsLinkIncidentRequest {

    /**
     * System.
     */
    private String system;
    /**
     * Typ ticketu.
     */
    private Long incidentId;
    /**
     * Identifikator ticketu.
     */
    private Long problemId;
}

package cz.sd.api;

import java.util.List;

/**
 * Rozhrani pro doplnujici praci s problemy (problem = pricina jednoho ci vice
 * incidentu), umoznuje linkovat incidenty a problemy a zjistit, jake incidenty
 * nalezi ke konkretnimu problemu.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public interface IServiceDeskProblemApi {

    /**
     * Propoji incident s problemem. Tedy problem bude chapan jako pricina
     * incidentu. Vazba by mela odpovidat vazbe "souvisi s".
     *
     * @param problemId Id problemu.
     * @param incidentId Id incidentu.
     * @return True, pokud se propojeni podarilo, jinak false.
     * @throws cz.sd.api.ServiceDeskCommunicationException v pripade chyby pri
     * komunikaci se service deskem.
     */
    boolean linkIncidentToProblem(long problemId, long incidentId) throws ServiceDeskCommunicationException;

    /**
     * Vrati seznam incidentu pridruzenych k zadanemu problemu.
     *
     * @param problemId Id problemu.
     * @return Seznam id incidentu, jichz je zadany problem pricinou.
     * @throws cz.sd.api.ServiceDeskCommunicationException v pripade chyby pri
     * komunikaci se service deskem.
     */
    List<Long> getIncidentsFromProblem(long problemId) throws ServiceDeskCommunicationException;

    /**
     * Odstrani vazbu mezi incidentem a problemem.
     *
     * @param problemId Id problemu.
     * @param incidentId Id incidentu.
     * @return True, pokud bylo propojeni odstraneno.
     * @throws cz.sd.api.ServiceDeskCommunicationException v pripade chyby pri
     * komunikaci se service deskem.
     */
    boolean unlinkIncidentFromProblem(long problemId, long incidentId) throws ServiceDeskCommunicationException;
}

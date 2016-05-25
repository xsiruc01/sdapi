package cz.sd.api.alvao.database.dao;

import cz.sd.api.alvao.ticket.AlvaoTicketCreateData;
import cz.sd.api.alvao.database.AlvaoDbTicket;
import cz.sd.api.ticket.TicketUpdateData;
import java.util.List;

/**
 * DAO pro praci s tickety v ALVAO databazi.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public interface IAlvaoDbTicketDao {
    /**
     * Vytvori v databazi novy ticket.
     *
     * @param createData Informace potrebne pro vytvoreni ticketu.
     * @return Id ticketu. Vraci null v pripade, ze se tiket nepodarilo
     * vytvorit.
     */
    Long createTicket(AlvaoTicketCreateData createData);

    /**
     * Vrati z databaze ticket.
     *
     * @param id Id ticketu.
     * @return Databazovy ticket. Vraci null v pripade ze se tiket nepodarilo dohledat.
     */
    AlvaoDbTicket getTicket(long id);

    /**
     * Vrati id stavu, podle predaneho nazvu.
     *
     * @param status Nazev stavu.
     * @return Id stavu. Vraci null v pripade, ze se stav nepodarilo dohledat.
     */
    Integer getStatusIdByStatusName(String status);

    /**
     * Updatuje ticket v databazi.
     *
     * @param data Nova data ticketu.
     * @return Priznak, zda se uprava podarila. Muze vracet true, i kdyz nova data
     * z nejakeho duvodu neprepsala stara data. Priznak tedy pouze urcuje, zda bylo vykonani
     * SQL v poradku.
     */
    boolean updateTicket(TicketUpdateData data);

    /**
     * Prida k ticketu poznamku.
     *
     * @param id Id ticketu.
     * @param note Text poznamky.
     * @return Priznak, zda se podarilo vlozit zpravu k tiketu. Priznak urcuje pouze,
     * zda bylo vykonani SQL v poradku.
     */
    boolean addNote(long id, String note);

    /**
     * Vrati typ ticketu.
     *
     * @param id Id ticketu.
     * @return Typ ticketu v Alvao databazi. Vraci null, pokud se tiket nepodarilo dohledat.
     */
    Integer getTicketType(long id);

    /**
     * Vrati podrazene incidenty problemu.
     *
     * @param problemId Id problemu.
     * @return Seznam podrazenych incidentu. V pripade ze problem neexistuje nebo nema
     * zadne podrazene incidenty, je vracen prazdny seznam. V pripade chyby vraci null.
     */
    List<Long> getRelatedIncidents(long problemId);

    /**
     * Priradi incident k problemu.
     *
     * @param problemId Id problemu.
     * @param incidentId Id incidentu.
     * @return Priznak, zda se prirazeni podarilo. Priznak urcuje pouze, zda bylo
     * provedeni SQL v poradku.
     */
    boolean linkIncidentWithProblem(long problemId, long incidentId);

    /**
     * Odstrani vazbu mezi incidentem a problemem.
     *
     * @param problemId Id problemu.
     * @param incidentId Id incidentu.
     * @return Priznak, zda se odstraneni vazby podarilo. Priznak urcuje pouze, zda bylo
     * provedeni SQL v poradku.
     */
    boolean unlinkIncidentWithProblem(long problemId, long incidentId);
}

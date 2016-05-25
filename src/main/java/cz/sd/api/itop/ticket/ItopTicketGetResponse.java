package cz.sd.api.itop.ticket;

import cz.sd.api.ServiceDeskMessage;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Predek pro tridy reprezentujici odpovedi na ziskani tiketu. Trida obsahuje
 * zakladni informace, ktere jsou u vsech odpovedi.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public abstract class ItopTicketGetResponse {

    /**
     * Vrati id zadatele.
     *
     * @return Id zadatele.
     */
    public abstract Long getRequestorId();

    /**
     * Vrati jmeno zadatele.
     *
     * @return Jmeno zadatele.
     */
    public abstract String getRequestorName();

    /**
     * Vrati id technika.
     *
     * @return Id technika.
     */
    public abstract Long getTechnicianId();

    /**
     * Vrati jmeno technika.
     *
     * @return Jmeno technika.
     */
    public abstract String getTechnicianName();

    /**
     * Vrati id souvisejici konfoguracni polozky.
     *
     * @return Id konfiguracni polozky nebo null, pokud s ticketem neni zadna
     * konfiguracni polozka propojena.
     */
    public abstract Long getFunctionalCiId();

    /**
     * Vrati datum vytvoreni tiketu.
     *
     * @return Datum vytvoreni tiketu.
     */
    public abstract String getCreatedDate();

    /**
     * Vrati datum posledni upravy tiketu.
     *
     * @return Datum posledni upravy tiketu.
     */
    public abstract String getLastUpdatedDate();

    /**
     * Vrati datum deadline tiketu.
     *
     * @return Datum deadline tiketu.
     */
    public abstract String getDeadlineDate();

    /**
     * Reprezentace konfiguracni polozky pripojene k ticketu.
     */
    @Getter
    @Setter
    protected static class FunctionalCI {

        private String functionalci_id;
        private String functionalci_name;
        private String impact;
        private String impact_code;
        private String friendlyname;
        private String functionalci_id_friendlyname;
        private String functionalci_id_finalclass_recall;
    }

    /**
     * Vrati priznak urcujici, ze nebyla vracena zadna data (napr. pokud tiket
     * neexistuje).
     *
     * @return Tru, pokud je tiket v poradku. False, pokud tiket neexistuje.
     */
    public abstract boolean isNull();

    /**
     * Vrati poznamky/zpravy tiketu.
     *
     * @return Seznam zprav.
     */
    public abstract List<ServiceDeskMessage> getNotes();
}

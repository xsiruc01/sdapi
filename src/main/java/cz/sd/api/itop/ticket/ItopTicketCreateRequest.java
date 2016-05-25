package cz.sd.api.itop.ticket;

import com.google.gson.annotations.SerializedName;
import cz.sd.api.itop.convertor.ItopPriorityConvertor;
import cz.sd.api.ticket.TicketCreateData;
import cz.sd.api.ticket.TicketCreateRequest;
import cz.sd.api.ticket.TicketType;
import lombok.Getter;
import lombok.Setter;

/**
 * Pozadavek na vytvoreni ticketu v systemu iTop.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class ItopTicketCreateRequest extends TicketCreateRequest {

    /**
     * Operace requestu - core/create pro vytvoreni objektu v itop.
     */
    private final String operation = "core/create";
    /**
     * Komentar.
     */
    private String comment;
    /**
     * Trida objektu - INCIDENT, PROBLEM, CHANGE apod.
     */
    @SerializedName("class")
    private String _class;
    /**
     * Pozadovana pole v odpovedi.
     */
    private final String output_fields = "ref";
    /**
     * Detaily pro vytvareny objekt.
     */
    private Fields fields;

    /**
     * Vytvori instanci pozadavku na vytvoreni tiketu.
     *
     * @param createData Data nutna pro vytvoreni tiketu.
     */
    public ItopTicketCreateRequest(TicketCreateData createData) {
        ItopTicketCreateData info = (ItopTicketCreateData) createData;
        if (info.getType() == TicketType.CHANGE) {
            this._class = "RoutineChange";
        } else {
            this._class = info.getType().getName();
        }
        this.fields = new Fields();
        this.fields.setTitle(info.getSubject());
        this.fields.setDescription(info.getDetail());
        if (info.getType() != TicketType.CHANGE) { // zmeny nepracuji s prioritou a urgenci
            this.fields.setPriority(ItopPriorityConvertor.convertToItopPriority(info.getPriority()));
            this.fields.setUrgency(ItopPriorityConvertor.convertToItopPriority(info.getPriority()));
        }
        // status je automaticky nastaven na new
        this.fields.setOrg_id("SELECT Organization WHERE name = '" + info.getOrganization().getName() + "'");
        this.fields.setCaller_id("SELECT Person WHERE id = " + info.getRequester().getId());
        this.fields.setAgent_id("SELECT Person WHERE id = " + info.getTechnician().getId());
        this.comment = "Created via Rest API";
    }

}

/**
 * Popis vytvareneho ticketu.
 *
 * @author Pavel Sirucek
 */
@Getter
@Setter
class Fields {

    /**
     * Id organizace.
     */
    private String org_id;
    /**
     * Predmet (titulek) ticketu.
     */
    private String title;
    /**
     * Popis ticketu.
     */
    private String description;
    /**
     * Priorita.
     */
    private Integer priority;
    /**
     * Nalehavost, narozdil od priority lze menit v GUI.
     */
    private Integer urgency;
    /**
     * Zakladatel pozadavku.
     */
    private String caller_id;
    /**
     * Resitel pozadavku.
     */
    private String agent_id;
}

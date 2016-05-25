package cz.sd.api.itop.ticket.problem.json;

import com.google.gson.annotations.SerializedName;
import cz.sd.api.ticket.TicketType;
import lombok.Getter;
import lombok.Setter;

/**
 * Pozadavek na slinkovani incidentu s problemem.
 * <pre>
    json_data={
        "operation": "core/update",
        "comment": "",
        "class": "Incident",
        "key": "ID_incidentu",
        "fields":
        {
            "parent_problem_id": "ID_problemu"
        }
    }
 * </pre>
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter @Setter
public class ItopLinkIncidentToProblemRequest {

    private final String operation = "core/update";
    private String comment;
    @SerializedName("class")
    private String _class;
    private final String key;
    private Fields fields;

    /**
     * Vytvori instanci pozadavku na slinkovani problemu a incidentu.
     *
     * @param problemId Id problemu.
     * @param incidentId Id incidentu.
     */
    public ItopLinkIncidentToProblemRequest(long problemId, long incidentId) {
        this._class = TicketType.INCIDENT.getName();
        this.comment = "Updated via Rest API";
        this.key = String.valueOf(incidentId);

        this.fields = new Fields();
        fields.setParent_problem_id(String.valueOf(problemId));

    }

    /**
     * Popis vytvareneho ticketu.
     *
     * @author Pavel Sirucek
     */
    @Getter
    @Setter
    private class Fields {

        /**
         * Predmet (titulek) ticketu.
         */
        String parent_problem_id;

    }
}

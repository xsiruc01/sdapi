package cz.sd.api.itop.ticket;

import com.google.gson.annotations.SerializedName;
import cz.sd.api.itop.ticket.incident.ItopIncidentUpdateData;
import cz.sd.api.itop.convertor.ItopPriorityConvertor;
import cz.sd.api.itop.convertor.ItopStatusConvertor;
import cz.sd.api.itop.ticket.change.ItopChangeUpdateData;
import cz.sd.api.itop.ticket.problem.ItopProblemUpdateData;
import cz.sd.api.ticket.TicketType;
import cz.sd.api.ticket.TicketUpdateData;
import lombok.Getter;
import lombok.Setter;

/**
 * Pozadavek na update tiketu.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class ItopTicketUpdateRequest {

    private final String operation = "core/update";

    private String comment;

    @SerializedName("class")
    private String _class;

    private final String key;

    private Fields fields;

    /**
     * Vytvori instanci pozadavku na upravu tiketu.
     *
     * @param data Upravovana data.
     */
    public ItopTicketUpdateRequest(TicketUpdateData data) {
        if (data.getType() == TicketType.CHANGE) {
            this._class = "RoutineChange";
        } else {
            this._class = data.getType().getName();
        }
        this.comment = "Updated via Rest API";
        this.key = String.valueOf(data.getTicketId());

        this.fields = new Fields();
        this.fields.setTitle(data.getSubject());
        this.fields.setDescription(data.getDetail());
        if (data.getType() != TicketType.CHANGE) { // zmeny nemaji prioritu nebo urgenci
            this.fields.setPriority(ItopPriorityConvertor.convertToItopPriority(data.getPriority()));
            this.fields.setUrgency(ItopPriorityConvertor.convertToItopPriority(data.getPriority()));
        }
        this.fields.setStatus(ItopStatusConvertor.convertToItopStatus(data.getStatus(), data.getType()));
        // dodatecna data incidentu, problemu nebo zmeny
        if (data instanceof ItopIncidentUpdateData) {
            ItopIncidentUpdateData info2 = (ItopIncidentUpdateData) data;
            this.fields.setPending_reason(info2.getPendingReason());
            this.fields.setSolution(info2.getResolvedInfo());
            this.fields.setPublic_log(info2.getNote());
        } else if (data instanceof ItopChangeUpdateData) {
            ItopChangeUpdateData info2 = (ItopChangeUpdateData) data;
            this.fields.setPrivate_log(info2.getNote());
        } else if (data instanceof ItopProblemUpdateData) {
            ItopProblemUpdateData info2 = (ItopProblemUpdateData) data;
            this.fields.setPrivate_log(info2.getNote());
        }

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
         * Status.
         */
        private String status;

        private String pending_reason;

        private String solution;

        private String public_log;

        private String private_log;
    }

}

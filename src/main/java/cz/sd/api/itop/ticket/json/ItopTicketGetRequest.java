package cz.sd.api.itop.ticket.json;

import com.google.gson.annotations.SerializedName;
import cz.sd.api.ticket.TicketType;
import lombok.Getter;

/**
 * Request pro ziskani konkretniho jednoho incidentu. 1:1 JSON.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ItopTicketGetRequest {

    private final String operation = "core/get";
    @SerializedName("class")
    private final String _class;
    @Getter
    private final String key;
    private final String output_fields = "*";

    /**
     * Konstruktor.
     *
     * @param ticketId, TicketType type Id pozadovaneho incidentu.
     * @param type Typ ticketu.
     */
    public ItopTicketGetRequest(long ticketId, TicketType type) {
        this.key = String.valueOf(ticketId);
        if (type.equals(TicketType.CHANGE)) {
            this._class = "RoutineChange";
        } else {
            this._class = type.getName();
        }
    }
}

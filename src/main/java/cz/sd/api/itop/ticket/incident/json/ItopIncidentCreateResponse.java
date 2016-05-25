package cz.sd.api.itop.ticket.incident.json;

import cz.sd.api.ticket.TicketCreateResponse;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Odpoved pri vytvoreni ticketu v systemu iTop. Format odpovedi:
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class ItopIncidentCreateResponse extends TicketCreateResponse {

    private Map<String, TicketData> objects;
    private Integer code;
    private Object message;

    @Override
    public Long getCreatedTicketId() {
        Long id = null;
        if (this.objects.size() == 1) {
            for (Map.Entry<String, TicketData> entrySet : objects.entrySet()) {
                String key = entrySet.getKey();
                TicketData value = entrySet.getValue();
                id = Long.valueOf(value.getKey());
            }
        }
        return id;
    }

    @Getter
    @Setter
    private class Fields {

        private String ref;
    }

    @Getter
    @Setter
    private class TicketData {

        private Integer code;
        private String message;
        private String key;
        private Fields fields;
    }
}

package cz.sd.api.itop.cmdb.json;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * Request pro vytvoreni vazby mezi konfiguracni polozkou a ticketem v systemu
 * Itop.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class ItopLinkCiToTicketRequest {

    private final String operation = "core/create";
    private final String comment = "Linked via rest api";
    @SerializedName("class")
    private final String _class = "lnkFunctionalCIToTicket";
    private final Fields fields;

    /**
     * Vytvori instanci pozadavku na slinkovani konfiguracni polozky a tiketu.
     *
     * @param ticketId
     * @param ciId
     */
    public ItopLinkCiToTicketRequest(long ticketId, long ciId) {
        this.fields = new Fields();
        this.fields.setTicket_id(String.valueOf(ticketId));
        this.fields.setFunctionalci_id(String.valueOf(ciId));
    }

    @Getter
    @Setter
    private class Fields {

        private String ticket_id;
        private String functionalci_id;
    }
}

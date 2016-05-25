package cz.sd.api.manageengine.ticket.xml;

import cz.sd.api.manageengine.ticket.ManageEngineMessage;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 * Response na vraceni zprav/poznamek.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@XmlRootElement(name = "API")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ManageEngineMessageGetResponse {

    /**
     * Data odpovedi.
     */
    @Getter
    @Setter
    private Response response;

    /**
     * Vytvori instanci.
     */
    public ManageEngineMessageGetResponse() {
    }

    /**
     * Vrati seznam zprav.
     *
     * @return Seznam zprav.
     */
    public List<ManageEngineMessage> getNotes() {
        if (this.getResponse().getOperation().getDetails() != null) {
            return this.getResponse().getOperation().getDetails().get(0).getNotes().getNote();
        }
        return null;
    }

    @Getter
    @Setter
    private static class Response {

        private Operation operation;

        public Response() {

        }
    }

    @Getter
    @Setter
    private static class Operation {

        private Result result;

        @XmlElement // bez tohoto to zde bohuzel nefunguje, proc ale, kdo vi
        private List<Detail> Details;

        public Operation() {
        }
    }

    @Getter
    @Setter
    private static class Result {

        private String statuscode;
        private String status;
        private String message;

        public Result() {
        }
    }

    @Getter
    @Setter
    private static class Detail {

        @XmlElement
        private NoteList Notes;

        public Detail() {

        }
    }

    @Getter
    @Setter
    private static class NoteList {

        @XmlElement
        private List<ManageEngineMessage> Note;

        public NoteList() {
        }
    }

}

package cz.sd.api.manageengine.ticket.xml;

import cz.sd.api.manageengine.ticket.ManageEngineMessage;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Request na vytvoreni zpravy.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@XmlRootElement(name = "Operation")
public class ManageEngineMessageCreateRequest {

    @XmlElement(name = "Details")
    private Details details;

    /**
     * Vytvori instanci.
     */
    public ManageEngineMessageCreateRequest() {
    }

    /**
     * Vytvori instanci s nastavenim zpravy.
     *
     * @param note Zprava.
     */
    public ManageEngineMessageCreateRequest(ManageEngineMessage note) {
        List<ManageEngineMessage> meNoteList = new ArrayList<>();
        meNoteList.add(note);
        ManageEngineMessageCreateRequest.InnerNotes innerNotes = new ManageEngineMessageCreateRequest.InnerNotes(meNoteList);
        // vytvorime detail a nastavime poznamky
        ManageEngineMessageCreateRequest.Details detail = new ManageEngineMessageCreateRequest.Details(innerNotes);
        this.details = detail;
    }

    private static class Details {

        @XmlElement(name = "Notes")
        private InnerNotes notes;

        public Details(InnerNotes Notes) {
            this.notes = Notes;
        }
    }

    private static class InnerNotes {

        @XmlElement(name = "Note")
        private List<ManageEngineMessage> note;

        public InnerNotes(List<ManageEngineMessage> Note) {
            this.note = Note;
        }
    }
}

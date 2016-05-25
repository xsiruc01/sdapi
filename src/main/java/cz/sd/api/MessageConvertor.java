package cz.sd.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Trida pro konverzi ServiceDeskMessage na String.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class MessageConvertor {

    /**
     * Konverze seznamu ServiceDeskMessage na seznam Stringu.
     *
     * @param notes Poznamky typu ServiceDeskMessage.
     * @return Seznam poznamek typu String.
     */
    public static List<String> convert(List<ServiceDeskMessage> notes) {
        List<String> newNotes = new ArrayList<>();
        for (ServiceDeskMessage note : notes) {
            newNotes.add(note.getText());
        }
        return newNotes;
    }
}

package cz.sd.api.freshservice.ticket.incident.json;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * Pridani zpravy k ticketu ve FreshService.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class FreshserviceMessage {

    /**
     * JSON note.
     */
    private final Message helpdesk_note;

    /**
     * Vytvori instanci zpravy.
     *
     * @param text Text zpravy.
     * @param isPrivate Viditelnost zpravy.
     */
    public FreshserviceMessage(String text, boolean isPrivate) {
        this.helpdesk_note = new Message(text, isPrivate);
    }

    /**
     * Vrati text zpravy.
     *
     * @return Text zpravy.
     */
    public String getText() {
        return helpdesk_note.getBody();
    }

    /**
     * Zprava.
     */
    private class Message {

        /**
         * Text zpravy.
         */
        @Getter
        private final String body;

        /**
         * Viditelnost zpravy.
         */
        @Getter
        @SerializedName("private")
        private final boolean isPrivate;

        /**
         * Vytvori instanci zpravy.
         *
         * @param text Text zpravy.
         * @param isPrivate Viditelnost zpravy.
         */
        public Message(String text, boolean isPrivate) {
            this.body = text;
            this.isPrivate = isPrivate;
        }
    }
}

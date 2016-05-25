package cz.sd.api.webhelpdesk.ticket.json;

import cz.sd.api.ticket.TicketStatus;
import cz.sd.api.webhelpdesk.convertor.WebHelpDeskStatusConvertor;
import lombok.Getter;
import lombok.Setter;

/**
 * Zprava k tiketu Web Help Desk.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
    @Setter
public class WebHelpDeskMessage {

    /**
     * Text zpravy.
     */    
    private String noteText;
    /**
     * Identifikator prislusneho ticketu.
     */
    private JobTicket jobticket;
    /**
     * Prizna, zda je poznamka skryta.
     */
    private boolean isHidden;
    /**
     * Priznak, zda jde o reseni ticketu.
     */
    private boolean isSolution;
    /**
     * Novy stav ticketu.
     */
    private Integer statusTypeId;

    /**
     * Konstruktor.
     *
     * @param ticketId Id ticketu.
     * @param text Text poznamky.
     * @param isVisible Priznak, zda bude poznamka viditelna.
     */
    public WebHelpDeskMessage(long ticketId, String text, boolean isVisible) {
        this(ticketId, text, isVisible, null);
    }

    /**
     * Konstruktor umoznujici nastavit novy stav ticketu.
     *
     * @param ticketId Id ticketu.
     * @param text Text poznamky.
     * @param isVisible Priznak, zda je poznamka viditelna.
     * @param newStatus Novy stav ticketu.
     */
    public WebHelpDeskMessage(long ticketId, String text, boolean isVisible, TicketStatus newStatus) {
        this.jobticket = new JobTicket();
        this.jobticket.setId(ticketId);
        this.noteText = text;
        this.isHidden = !isVisible;
        this.isSolution = false;
        if (newStatus != null) {
            this.statusTypeId = WebHelpDeskStatusConvertor.convertToWebHelpDeskStatus(newStatus);
        } else {
            this.statusTypeId = null;
        }
    }

    /**
     * Nastavi id ticketu.
     *
     * @param id Id ticketu.
     */
    public void setTicketId(long id) {
        this.jobticket.setId(id);
    }

    /**
     * Vrati text zpravy.
     * @return text zpravy.
     */
    public String getText() {
        return this.getNoteText();
    }

    /**
     * Privatni trida pro nastaveni id ticketu do prislusne JSON tridy.
     */
    @Getter
        @Setter
    private class JobTicket {

        private final String type = "JobTicket";        
        private long id;
    }
}

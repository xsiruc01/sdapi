package cz.sd.api.webhelpdesk.convertor;

import cz.sd.api.webhelpdesk.ticket.json.WebHelpDeskTicketGetResponse;
import cz.sd.api.ServiceDeskMessage;
import cz.sd.api.webhelpdesk.ticket.WebHelpDeskIncident;
import cz.sd.api.webhelpdesk.ticket.WebHelpDeskTicket;
import java.util.ArrayList;
import java.util.List;

/**
 * Konverzni trida pro prevod dat z odpovedi webovych sluzeb Web Help Desk na
 * tiket.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class WebHelpDeskTicketConvertor {

    /**
     * Prevede data z odpovedi webovych sluzeb na tiket.
     *
     * @param data Odpoved ws Web Help Desk.
     * @param systemName Jmeno systemu Web Heko Desk.
     * @return Tiket.
     */
    public static WebHelpDeskTicket convertToTicket(WebHelpDeskTicketGetResponse data, String systemName) {
        WebHelpDeskIncident ticket = new WebHelpDeskIncident();
        ticket.setSdSystemName(systemName);
        ticket.setTicketId(data.getId());
        ticket.setSubject(data.getSubject());
        ticket.setDetail(data.getDetail());
        ticket.setPriority(WebHelpDeskPriorityConvertor.convertFromWebHelpDeskPriority(data.getPriorityTypeId()));
        ticket.setStatus(WebHelpDeskStatusConvertor.convertFromWebHelpDeskStatus(data.getStatusTypeId()));
        ticket.setRequester(data.getRequester());
        ticket.setTechnician(data.getTechnician());
        ticket.setLastUpdatedDate(data.getLastUpdated());
        ticket.setCreatedDate(data.getReportDateUtc());
        ticket.setDeadlineDate(data.getDisplayDueDate());

        // poznamky
        List<ServiceDeskMessage> whdNotes = new ArrayList<>();
        List<WebHelpDeskTicketGetResponse.Note> notes = data.getNotes();
        for (WebHelpDeskTicketGetResponse.Note note : notes) {
            String text = note.getMobileNoteText();
            String date = note.getDate();
            ServiceDeskMessage newNote = new ServiceDeskMessage(text, date);
            whdNotes.add(newNote);
        }
        ticket.setNotes(whdNotes);
        //        
        return ticket;
    }

}

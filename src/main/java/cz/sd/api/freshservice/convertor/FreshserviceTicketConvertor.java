package cz.sd.api.freshservice.convertor;

import cz.sd.api.ServiceDeskMessage;
import cz.sd.api.freshservice.ticket.FreshserviceTicket;
import cz.sd.api.freshservice.ticket.incident.FreshserviceIncident;
import cz.sd.api.freshservice.ticket.incident.json.FreshserviceHelpDeskTicket;
import cz.sd.api.freshservice.ticket.incident.json.FreshserviceIncidentData;
import cz.sd.api.users.ServiceDeskRequester;
import cz.sd.api.users.ServiceDeskTechnician;
import java.util.ArrayList;
import java.util.List;

/**
 * Trida pro konverzi dat ziskanych z webovych sluzeb Freshservice na tiket
 * pouzivany v rozhrani.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class FreshserviceTicketConvertor {

    /**
     * Z objektu Incident data, ziskaneho z Freshservice vytvori domenovy objekt
     * FreshServiceIncident.
     *
     * @param data Data ziskana ze service desku.
     * @param systemName Nazev service desku.
     * @return Domenovy objekt FreshServiceIncident.
     */
    public static FreshserviceTicket convertToIncident(FreshserviceIncidentData data, String systemName) {
        FreshserviceIncident incident = new FreshserviceIncident();
        incident.setSdSystemName(systemName);
        incident.setTicketId(data.getDisplay_id());
        incident.setSubject(data.getSubject());
        incident.setDetail(data.getDescription());
        incident.setPriority(FreshservicePriorityConvertor.convertFromFreshServicePriority(data.getPriority()));
        incident.setStatus(FreshserviceStatusConvertor.convertFromFreshserviceStatus(data.getStatus()));
        incident.setConfigItemId(data.getAssoc_asset_id());
        incident.setCreatedDate(data.getCreated_at());
        incident.setLastUpdatedDate(data.getUpdated_at());
        incident.setDeadlineDate(data.getDue_by());
        incident.setRequester(new ServiceDeskRequester(data.getRequester_id(), data.getRequester_name()));
        incident.setTechnician(new ServiceDeskTechnician(data.getResponder_id(), data.getResponder_name()));

        // poznamky
        List<ServiceDeskMessage> fsNotes = new ArrayList<>();
        List<FreshserviceIncidentData.Note> notes = data.getNotes();
        if (notes != null) {
            for (int i = notes.size() - 1; i >= 0; i--) {
                FreshserviceIncidentData.Note note = notes.get(i);
                ServiceDeskMessage newNote = new ServiceDeskMessage(note.getNote().getBody(), note.getNote().getCreated_at());
                fsNotes.add(newNote);
            }
            incident.setNotes(fsNotes);
        }
        //

        return incident;
    }

    /**
     * Konverze dat z odpovedi na tiket Freshservice.
     *
     * @param data Data odpovedi.
     * @param systemName Nazev service desku.
     * @return Incident Freshservice.
     */
    public static FreshserviceTicket convertToTicket(FreshserviceHelpDeskTicket data, String systemName) {
        return convertToIncident(data.getHelpdesk_ticket(), systemName);
    }
}

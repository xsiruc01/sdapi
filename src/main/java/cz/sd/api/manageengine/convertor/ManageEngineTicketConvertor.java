package cz.sd.api.manageengine.convertor;

import cz.sd.api.SdSystemType;
import cz.sd.api.ServiceDeskMessage;
import cz.sd.api.manageengine.ticket.incident.ManageEngineIncident;
import cz.sd.api.manageengine.ticket.ManageEngineMessage;
import cz.sd.api.manageengine.ticket.xml.ManageEngineMessageGetResponse;
import cz.sd.api.manageengine.ticket.ManageEngineParameter;
import cz.sd.api.manageengine.ticket.ManageEngineTicket;
import cz.sd.api.manageengine.ticket.incident.xml.ManageEngineTicketGetResponse;
import cz.sd.api.ticket.TicketType;
import cz.sd.api.users.ServiceDeskRequester;
import cz.sd.api.users.ServiceDeskTechnician;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Konverzni trida pro prevod dat z odpovedi webovych sluzeb ManageEngine na
 * tiket v rozhrani.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ManageEngineTicketConvertor {

    /**
     * Prevede data z odpovedi webovych sluzeb na incidnet tiket.
     *
     * @param data Odpoved ws ManageEngine.
     * @param notes Poznamky tiketu.
     * @param systemName Jmeno systemu ManageEngine.
     * @return Tiket.
     */
    public static ManageEngineTicket convertToTicket(ManageEngineTicketGetResponse data, ManageEngineMessageGetResponse notes, String systemName) {
        ManageEngineIncident incident = new ManageEngineIncident();
        Map<String, String> parameters = data.getMapParams();
        
        // priorita a stav
        incident.setPriority(ManageEnginePriorityConvertor.convertFromManageEnginePriority(parameters.get(ManageEngineParameter.PRIORITY)));
        incident.setStatus(ManageEngineStatusConvertor.convertFromManageEngineStatus(parameters.get(ManageEngineParameter.STATUS)));

        // dalsi parametry
        incident.setTicketId(Integer.valueOf(parameters.get(ManageEngineParameter.ID)));
        incident.setSubject(parameters.get(ManageEngineParameter.SUBJECT));
        incident.setDetail(parameters.get(ManageEngineParameter.DESCRIPTION));
        incident.setSla(parameters.get(ManageEngineParameter.SLA));
        incident.setCreatedDate(parameters.get(ManageEngineParameter.CREATED_TIME));
        incident.setDeadlineDate(parameters.get(ManageEngineParameter.DUE_BY_TIME));
        incident.setCategory(parameters.get(ManageEngineParameter.CATEGORY));
        
        // tvurce a technik
        incident.setRequester(new ServiceDeskRequester(null, parameters.get(ManageEngineParameter.REQUESTER)));
        incident.setTechnician(new ServiceDeskTechnician(null, parameters.get(ManageEngineParameter.TECHNICIAN)));

        // poznamky
        List<ServiceDeskMessage> fsNotes = new ArrayList<>();
        List<ManageEngineMessage> meNotes = notes.getNotes();
        if (meNotes != null) {
            for (ManageEngineMessage meNote : meNotes) {
                String text = meNote.getText();
                String date = meNote.getDate();
                ServiceDeskMessage newNote = new ServiceDeskMessage(text, date);
                fsNotes.add(newNote);
            }
        }
        incident.setNotes(fsNotes);

        incident.setType(TicketType.INCIDENT);
        incident.setSdSystemName(systemName);
        incident.setSdType(SdSystemType.MANAGE_ENGINE);

        return incident;
    }
}

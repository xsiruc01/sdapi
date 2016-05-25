package cz.sd.api.itop.convertor;

import cz.sd.api.ServiceDeskMessage;
import cz.sd.api.itop.ticket.incident.ItopIncident;
import cz.sd.api.itop.ticket.incident.json.ItopIncidentGetResponse;
import cz.sd.api.itop.ticket.problem.json.ItopProblemGetResponse;
import cz.sd.api.itop.ticket.ItopTicket;
import cz.sd.api.itop.ticket.ItopTicketGetResponse;
import cz.sd.api.itop.ticket.change.ItopChange;
import cz.sd.api.itop.ticket.change.json.ItopChangeGetResponse;
import cz.sd.api.itop.ticket.problem.ItopProblem;
import cz.sd.api.ticket.TicketStatus;
import cz.sd.api.ticket.TicketType;
import cz.sd.api.users.ServiceDeskRequester;
import cz.sd.api.users.ServiceDeskTechnician;
import java.util.ArrayList;
import java.util.List;

/**
 * Trida pro konverzi odpovedi webovych sluzeb iTop na tiket v rozhrani.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ItopTicketConvertor {

    /**
     * Prevede odpoved webovych sluzeb na tiket. Rozlisuje se typ odpovedi,
     * protoze incidenty, problemy a zmeny obsahuji ruzna data.
     *
     * @param data odpoved ws iTop.
     * @param systemName Jmeno service desku iTop.
     * @return Tiket.
     */
    public static ItopTicket convertToTicket(ItopTicketGetResponse data, String systemName) {
        ItopTicket ticket = null;
        if (data instanceof ItopIncidentGetResponse) {
            ItopIncidentGetResponse response = (ItopIncidentGetResponse) data;
            ItopIncident incident = new ItopIncident();
            incident.setTicketId(response.getId());
            incident.setSubject(response.getSubject());
            incident.setDetail(response.getDetail());
            incident.setPriority(ItopPriorityConvertor.convertFromItopPriority(response.getPriority()));
            incident.setStatus(ItopStatusConvertor.convertFromItopStatus(response.getStatus(), TicketType.INCIDENT));
            if (response.getParentProblemId() > 0) {
                incident.setParentProblemId(response.getParentProblemId());
            }
            // poznamku k zastaveni tiketu doplnime pouze, pokud je tiket zastaven a poznamka neco obsahuje
            if (!response.getPendingReason().isEmpty() && incident.getStatus().equals(TicketStatus.ON_HOLD)) {
                incident.setPendingReason(response.getPendingReason()/*.replaceAll("\r\n", " ")*/);
            }
            // poznamku k vyreseni tiketu doplnime pouze, pokud je tiket vyresen a poznamka neco obsahuje
            if (!response.getResolvedNote().isEmpty() && incident.getStatus().equals(TicketStatus.RESOLVED)) {
                incident.setResolvedNote(response.getResolvedNote()/*.replaceAll("\r\n", " ")*/);
            }

            ticket = incident;
        } else if (data instanceof ItopProblemGetResponse) {
            ItopProblemGetResponse response = (ItopProblemGetResponse) data;
            ItopProblem problem = new ItopProblem();
            problem.setTicketId(response.getId());
            problem.setSubject(response.getSubject());
            problem.setDetail(response.getDetail());
            problem.setPriority(ItopPriorityConvertor.convertFromItopPriority(response.getPriority()));
            problem.setStatus(ItopStatusConvertor.convertFromItopStatus(response.getStatus(), TicketType.PROBLEM));
            problem.setRelatedIncidents(response.getRelatedIncidents());

            ticket = problem;
        } else if (data instanceof ItopChangeGetResponse) {
            ItopChangeGetResponse response = (ItopChangeGetResponse) data;
            ItopChange change = new ItopChange();
            change.setTicketId(response.getId());
            change.setSubject(response.getSubject());
            change.setDetail(response.getDetail());
            change.setStatus(ItopStatusConvertor.convertFromItopStatus(response.getStatus(), TicketType.CHANGE));

            ticket = change;
        } else {
            return null;
        }
        // zadavatel
        ticket.setRequester(new ServiceDeskRequester(data.getRequestorId(), data.getRequestorName()));
        // resitel ... mohl by byt teoreticky null nebo "" (i kdyz v iTop se pri vytvareni musi zadat)
        if (data.getTechnicianId() != null || (data.getTechnicianName() != null && !data.getTechnicianName().isEmpty())) {
            ticket.setTechnician(new ServiceDeskTechnician(data.getTechnicianId(), data.getTechnicianName()));
        }
        // konfiguracni polozka
        ticket.setConfigItemId(data.getFunctionalCiId());
        // jmeno systemu
        ticket.setSdSystemName(systemName);

        // datumy
        ticket.setCreatedDate(data.getCreatedDate());
        ticket.setLastUpdatedDate(data.getLastUpdatedDate());
        ticket.setDeadlineDate(data.getDeadlineDate());

        // poznamky
        List<ServiceDeskMessage> itopNotes = data.getNotes();
        List<ServiceDeskMessage> revertedNotes = new ArrayList<>();
        for (int i = itopNotes.size() - 1; i >= 0; i--) {
            revertedNotes.add(itopNotes.get(i));
        }
        ticket.setNotes(revertedNotes);

        return ticket;
    }
}

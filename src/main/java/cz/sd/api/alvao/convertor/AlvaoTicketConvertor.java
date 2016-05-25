package cz.sd.api.alvao.convertor;

import cz.sd.api.SdSystemType;
import cz.sd.api.alvao.ticket.incident.AlvaoIncident;
import cz.sd.api.alvao.database.AlvaoDbTicket;
import cz.sd.api.alvao.database.dao.IAlvaoDbTicketDao;
import cz.sd.api.alvao.ticket.AlvaoTicket;
import cz.sd.api.alvao.ticket.change.AlvaoChange;
import cz.sd.api.alvao.ticket.problem.AlvaoProblem;
import cz.sd.api.ticket.TicketType;
import cz.sd.api.users.ServiceDeskRequester;
import cz.sd.api.users.ServiceDeskTechnician;
import java.util.List;

/**
 * Konverzni trida pro informace o tiketech prectene z ALVAO databaze do tiketu
 * pouzivaneho v rozhrani.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class AlvaoTicketConvertor {

    /**
     * Prevadi data o tiketu z ALVAO databaze do objektu reprezentujici tiket v
     * rozhrani.
     *
     * @param data Informace o tiketu z dtabaze.
     * @param dao DAO pro praci s tikety v ALVAO databazi.
     * @param systemName Jmeno/identifikator ALVAO sd.
     * @return Tiket reprezentovany v rozhrani.
     */
    public static AlvaoTicket convertToTicket(AlvaoDbTicket data, IAlvaoDbTicketDao dao, String systemName) {
        AlvaoTicket ticket;
        if (data == null) {
            return null;
        }
        switch (data.getTicketTypeId()) {
            case AlvaoTicket.ALVAO_INCIDENT_TYPE_ID:
                ticket = new AlvaoIncident();
                ticket.setType(TicketType.INCIDENT);
                ticket.setPriority(AlvaoPriorityConvertor.convertFromAlvaoPriority(data.getPriority()));
                ticket.setStatus(AlvaoStatusConvertor.convertFromAlvaoIncidentStatus(data.getStateNotice(), dao));
                break;
            case AlvaoTicket.ALVAO_PROBLEM_TYPE_ID:
                ticket = new AlvaoProblem();
                ticket.setType(TicketType.PROBLEM);
                ticket.setPriority(AlvaoPriorityConvertor.convertFromAlvaoPriority(data.getPriority()));
                ticket.setStatus(AlvaoStatusConvertor.convertFromAlvaoProblemStatus(data.getStateNotice(), dao));
                List<Long> relatedIncidents = dao.getRelatedIncidents(data.getTicketId());
                ((AlvaoProblem) ticket).setRelatedIncidents(relatedIncidents);
                break;
            case AlvaoTicket.ALVAO_CHANGE_TYPE_ID:
                ticket = new AlvaoChange();
                ticket.setType(TicketType.CHANGE);
                ticket.setPriority(AlvaoPriorityConvertor.convertFromAlvaoPriority(data.getPriority()));
                ticket.setStatus(AlvaoStatusConvertor.convertFromAlvaoChangeStatus(data.getStateNotice(), dao));
                break;
            default:
                return null;
        }

        ticket.setSdSystemName(systemName);
        ticket.setSdType(SdSystemType.ALVAO);
        ticket.setTicketId(data.getTicketId());
        ticket.setSubject(data.getSubject());
        ticket.setDetail(data.getDetail());
        ticket.setNotes(data.getNotes());
        ticket.setConfigItemId(data.getConfigurationItemId());
        ticket.setCreatedDate(data.getCreatedDate().toString());
        ticket.setLastUpdatedDate(data.getLastActionDate().toString());
        ticket.setRequester(new ServiceDeskRequester((long) data.getCreatorId(), null));
        ticket.setTechnician(new ServiceDeskTechnician((long) data.getSolver(), null));

        return ticket;
    }
}

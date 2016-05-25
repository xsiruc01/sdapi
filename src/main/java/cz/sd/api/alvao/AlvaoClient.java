package cz.sd.api.alvao;

import cz.sd.api.alvao.ticket.AlvaoTicketCreateData;
import cz.sd.api.alvao.convertor.AlvaoCIConvertor;
import cz.sd.api.ConfigurationItem;
import cz.sd.api.IConfigurationItemApi;
import cz.sd.api.alvao.convertor.AlvaoTicketConvertor;
import cz.sd.api.IServiceDeskApi;
import cz.sd.api.IServiceDeskProblemApi;
import cz.sd.api.SdSystemType;
import cz.sd.api.ServiceDeskCommunicationException;
import cz.sd.api.ServiceDeskTicket;
import cz.sd.api.alvao.database.AlvaoDbConfigItem;
import cz.sd.api.alvao.database.dao.impl.AlvaoDbConfigItemDao;
import cz.sd.api.alvao.database.AlvaoDbTicket;
import cz.sd.api.alvao.database.dao.impl.AlvaoDbTicketDao;
import cz.sd.api.alvao.database.dao.IAlvaoDbConfigItemDao;
import cz.sd.api.alvao.database.dao.IAlvaoDbTicketDao;
import cz.sd.api.ticket.CiUpdateData;
import cz.sd.api.ticket.TicketCreateData;
import cz.sd.api.ticket.TicketStatus;
import cz.sd.api.ticket.TicketType;
import cz.sd.api.ticket.TicketUpdateData;
import java.util.List;
import lombok.Getter;

/**
 * Klient pro praci s ALVAO service desk systemem. ALVAO service desk podporuje
 * nasledujici ITIL procesy.
 * <ul>
 * <li>Incident management</li>
 * <li>Problem management</li>
 * <li>Change management</li>
 * <li>Configuration management</li>
 * </ul>
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class AlvaoClient implements IServiceDeskApi, IServiceDeskProblemApi, IConfigurationItemApi {    
    /**
     * ID pozadavku typu incident v systemu Alvao.
     */
    public static final int ALVAO_INCIDENT_ID = 2;
    /**
     * ID pozadavku typu problem v systemu Alvao.
     */
    public static final int ALVAO_PROBLEM_ID = 3;
    /**
     * ID pozadavku typu change v systemu Alvao.
     */
    public static final int ALVAO_CHANGE_ID = 4;
    
    /**
     * DAO pro praci s konfiguracnimi polozkami.
     */
    @Getter
    private final IAlvaoDbConfigItemDao ciDao;
    
    /**
     * DAO pro praci s tikety.
     */
    @Getter
    private final IAlvaoDbTicketDao ticketDao;
        
    /**
     * Nazev/identifikator service desku.
     */
    private final String systemName;
    
    /**
     * Konstruktor.
     * @param dbString Databazovy string.
     * @param systemName Nazev service desku.
     */
    public AlvaoClient(String dbString, String systemName) {
        ciDao = new AlvaoDbConfigItemDao(dbString);
        ticketDao = new AlvaoDbTicketDao(dbString);
        this.systemName = systemName;
    }

    @Override
    public Long createTicket(TicketCreateData ticketData) {
        Long id = null;
        if (ticketData instanceof AlvaoTicketCreateData) {
            AlvaoTicketCreateData data = (AlvaoTicketCreateData) ticketData;
            id = this.ticketDao.createTicket(data);
        }
        
        return id;
    }

    @Override
    public ServiceDeskTicket getTicket(long id, TicketType type) {
        ServiceDeskTicket ticket;
        AlvaoDbTicket dbTicket = this.ticketDao.getTicket(id);
        ticket = AlvaoTicketConvertor.convertToTicket(dbTicket, this.ticketDao, this.systemName);

        return ticket;
    }

    @Override
    public boolean updateTicket(TicketUpdateData data) {
        boolean result = this.ticketDao.updateTicket(data);
        
        return result;
    }

    @Override
    public boolean resolveTicket(long id, String note, TicketType type) {
        TicketUpdateData data = new TicketUpdateData(id, null, null, null, TicketStatus.RESOLVED, type);
        boolean result = this.updateTicket(data);
        if (note != null && !note.isEmpty()) {
            this.addMessage(id, note, type);
        }
        
        return result;
    }

    @Override
    public boolean closeTicket(long id, String note, TicketType type) {
        TicketUpdateData data = new TicketUpdateData(id, null, null, null, TicketStatus.CLOSED, type);
        boolean result = this.updateTicket(data);
        if (note != null && !note.isEmpty()) {
            this.addMessage(id, note, type);
        }
        
        return result;
    }

    @Override
    public boolean suspendTicket(long id, String note, TicketType type) {
        // ALVAO nema stav pro pozastaveni tiketu, tiket tedy nejde pozastavit
        // takze alespon pridame poznamku pozastaveni a nechame byt
        boolean result = addMessage(id, note, type);
        return result;
    }

    @Override
    public boolean openTicket(long id, String note, TicketType type) {
        TicketUpdateData data = new TicketUpdateData(id, null, null, null, TicketStatus.OPEN, type);
        boolean result = this.updateTicket(data);
        if (note != null && !note.isEmpty()) {
            this.addMessage(id, note, type);
        }
        
        return result;
    }

    @Override
    public boolean deleteTicket(long id, TicketType type) throws ServiceDeskCommunicationException {
        throw new ServiceDeskCommunicationException("Chyba pri komunikaci se service deskem ALVAO: Mazani tiketu neni podporovano");
    }

    @Override
    public boolean addMessage(long id, String note, TicketType type) {
        boolean result = this.ticketDao.addNote(id, note);
        
        return result;
    }

    @Override
    public boolean linkIncidentToProblem(long problemId, long incidentId) {
        boolean result = this.ticketDao.linkIncidentWithProblem(problemId, incidentId);
        
        return result;
    }

    @Override
    public List<Long> getIncidentsFromProblem(long problemId) {
        List<Long> result = this.ticketDao.getRelatedIncidents(problemId);
        
        return result;
    }

    @Override
    public boolean unlinkIncidentFromProblem(long problemId, long incidentId) {
        boolean result = this.ticketDao.unlinkIncidentWithProblem(problemId, incidentId);
        
        return result;
    }

    @Override
    public ConfigurationItem getCI(long id) {
        AlvaoDbConfigItem dbCI = this.ciDao.getCI(id);
        
        ConfigurationItem ci = AlvaoCIConvertor.convert(dbCI);

        return ci;
    }

    @Override
    public boolean linkCiToTicket(long ciId, long ticketid, TicketType type) {
        boolean result = this.ciDao.linkToTicket(ciId, ticketid);
        
        return result;
    }

    @Override
    public boolean updateCI(CiUpdateData updateData) {
        boolean result = this.ciDao.updateCI(updateData);
        
        return result;
    }

    @Override
    public String getSystemName() {
        return this.systemName;
    }

    @Override
    public SdSystemType getSystemType() {
        return SdSystemType.ALVAO;
    }

}

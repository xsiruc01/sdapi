package cz.sd.api.itop;

import cz.sd.api.itop.ticket.ItopOrganization;
import cz.sd.api.itop.ticket.ItopTicketCreateData;
import cz.sd.api.IServiceDeskApi;
import cz.sd.api.IServiceDeskProblemApi;
import cz.sd.api.ServiceDeskMessage;
import cz.sd.api.ServiceDeskTicket;
import cz.sd.api.AbstractServiceDeskTest;
import cz.sd.api.ServiceDeskCommunicationException;
import cz.sd.api.itop.ticket.change.ItopChange;
import cz.sd.api.itop.ticket.change.ItopChangeUpdateData;
import cz.sd.api.itop.ticket.incident.ItopIncident;
import cz.sd.api.itop.ticket.problem.ItopProblem;
import cz.sd.api.itop.ticket.problem.ItopProblemUpdateData;
import cz.sd.api.ticket.TicketPriority;
import cz.sd.api.ticket.TicketStatus;
import cz.sd.api.ticket.TicketType;
import cz.sd.api.ticket.TicketUpdateData;
import cz.sd.api.users.ServiceDeskRequester;
import cz.sd.api.users.ServiceDeskTechnician;
import cz.sd.api.users.ServiceDeskUser;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Testy pro testovani iTop api.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ItopClientTest extends AbstractServiceDeskTest {

    private IServiceDeskApi itopApi;

    // zadatel
    private static final ServiceDeskUser REQUESTER = new ServiceDeskRequester(1L, null);
    // technik
    private static final ServiceDeskUser TECHNICIAN = new ServiceDeskTechnician(1L, null);
    // organizace
    private static final ItopOrganization ORGANIZATION = new ItopOrganization(null, "Demo");

    @Before
    public void init() {
        itopApi = clients.get("itop");
    }

    /**
     * Test Itop incidentu.
     */
    @Test
    public void testItopIncidents() {
        System.out.println("Testing Itop incidents");
        try {
            // create ticket
            ItopTicketCreateData createData = new ItopTicketCreateData();
            createData.setDetail("Detailni popis ticketu, napr. Hori nam tiskarna, potrebujeme okamzitou pomoc!!!!!");
            createData.setSubject("Predmet ticketu.");
            createData.setPriority(TicketPriority.NORMAL);
            createData.setStatus(TicketStatus.OPEN);
            createData.setType(TicketType.INCIDENT);
            createData.setOrganization(ORGANIZATION);
            createData.setRequester(REQUESTER);
            createData.setTechnician(TECHNICIAN);
            long id = this.itopApi.createTicket(createData);

            // get ticket       
            ServiceDeskTicket result = this.itopApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertTrue(result instanceof ItopIncident);
            ItopIncident ticket = (ItopIncident) result;
            Assert.assertEquals("Ticket detail does not match", "Detailni popis ticketu, napr. Hori nam tiskarna, potrebujeme okamzitou pomoc!!!!!", ticket.getDetail());
            Assert.assertEquals("Ticket subject does not match", "Predmet ticketu.", ticket.getSubject());
            Assert.assertEquals(REQUESTER.getId(), ticket.getRequester().getId());
            Assert.assertEquals(TECHNICIAN.getId(), ticket.getTechnician().getId());

            // update ticket
            TicketUpdateData updateData = new TicketUpdateData(id, "Upraveny predmet", "Upraveny popis", TicketPriority.CRITICAL, null, TicketType.INCIDENT);
            boolean updated = this.itopApi.updateTicket(updateData);
            Assert.assertTrue(updated);
            result = this.itopApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertTrue(result instanceof ItopIncident);
            ticket = (ItopIncident) result;
            Assert.assertEquals("Ticket updated subject does not match", "Upraveny predmet", ticket.getSubject());
            Assert.assertEquals("Ticket updated detail does not match", "Upraveny popis", ticket.getDetail());
            Assert.assertEquals(TicketPriority.CRITICAL, ticket.getPriority());

        //@TODO udelat kontroly na navrat poznamek pri zastaveni nebo vyreseni
            // suspend ticket
            this.itopApi.suspendTicket(id, "Pozastavujeme, chybi nam vice informaci", TicketType.INCIDENT);
            result = this.itopApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertEquals("Statuses should be ON HOLD", TicketStatus.ON_HOLD, result.getStatus());
            Assert.assertEquals("Pozastavujeme, chybi nam vice informaci", ((ItopIncident) result).getPendingReason());

            // open ticket
            this.itopApi.openTicket(id, "Znovu muzeme pracovat", TicketType.INCIDENT);
            result = this.itopApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertEquals("Statuses should be ON HOLD", TicketStatus.OPEN, result.getStatus());

            // resolve ticket
            this.itopApi.resolveTicket(id, "Vyreseno, muzete uzavrit", TicketType.INCIDENT);
            result = this.itopApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertEquals("Statuses should be RESOLVED", TicketStatus.RESOLVED, result.getStatus());
            Assert.assertEquals("Vyreseno, muzete uzavrit", ((ItopIncident) result).getResolvedNote());

            // close ticket
            this.itopApi.closeTicket(id, "Dekujeme za vyreseni. Vse OK.", TicketType.INCIDENT);
            result = this.itopApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertEquals("Status should be CLOSED", TicketStatus.CLOSED, result.getStatus());

            itopApi.addMessage(id, "Prva verejna poznamka", TicketType.INCIDENT);
            itopApi.addMessage(id, "Druha verejna poznamka", TicketType.INCIDENT);
            itopApi.addMessage(id, "Tretija verejna poznamka", TicketType.INCIDENT);
            itopApi.addMessage(id, "Ctverjaja verejna poznamka", TicketType.INCIDENT);

            result = this.itopApi.getTicket(id, TicketType.INCIDENT);

            List<ServiceDeskMessage> notes = result.getNotes();

            Assert.assertEquals(8, notes.size()); // 8 protoze se pridavaji i poznamky ke zmenam stavu
            Assert.assertEquals("Prva verejna poznamka", notes.get(3).getText());
            Assert.assertEquals("Druha verejna poznamka", notes.get(2).getText());
            Assert.assertEquals("Tretija verejna poznamka", notes.get(1).getText());
            Assert.assertEquals("Ctverjaja verejna poznamka", notes.get(0).getText());
        } catch (ServiceDeskCommunicationException ex) {
            Logger.getLogger(ItopClientTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test Itop problemu.
     */
    @Test
    public void testItopProblems() {
        System.out.println("Testing Itop problems");
        try {
            // create problem
            ItopTicketCreateData createData = new ItopTicketCreateData();
            createData.setDetail("Tohle je problem vlozeny do iTop service desk.");
            createData.setSubject("Problem jako pricina.");
            createData.setPriority(TicketPriority.HIGH);
            createData.setStatus(TicketStatus.OPEN);
            createData.setType(TicketType.PROBLEM);
            createData.setOrganization(ORGANIZATION);
            createData.setRequester(REQUESTER);
            createData.setTechnician(TECHNICIAN);
            long problemId = this.itopApi.createTicket(createData);

            // get problem       
            ServiceDeskTicket result = this.itopApi.getTicket(problemId, TicketType.PROBLEM);
            Assert.assertTrue(result instanceof ItopProblem);
            ItopProblem problem = (ItopProblem) result;
            Assert.assertEquals("Ticket detail does not match", "Tohle je problem vlozeny do iTop service desk.", problem.getDetail());
            Assert.assertEquals("Ticket subject does not match", "Problem jako pricina.", problem.getSubject());
            Assert.assertEquals(true, problem.getRelatedIncidents().isEmpty());
            Assert.assertEquals(REQUESTER.getId(), problem.getRequester().getId());
            Assert.assertEquals(TECHNICIAN.getId(), problem.getTechnician().getId());

            // create incidents
            createData = new ItopTicketCreateData();
            createData.setDetail("Tohle je incident a souvisi s problemem c. " + problemId);
            createData.setSubject("Souvisejici incident c. 1");
            createData.setPriority(TicketPriority.HIGH);
            createData.setStatus(TicketStatus.OPEN);
            createData.setType(TicketType.INCIDENT);
            createData.setOrganization(ORGANIZATION);
            createData.setRequester(REQUESTER);
            createData.setTechnician(TECHNICIAN);
            long incident1 = this.itopApi.createTicket(createData);
            ((IServiceDeskProblemApi) itopApi).linkIncidentToProblem(problemId, incident1);

            createData = new ItopTicketCreateData();
            createData.setDetail("Tohle je dalsi incident a souvisi s problemem c. " + problemId);
            createData.setSubject("Souvisejici incident c. 2");
            createData.setPriority(TicketPriority.HIGH);
            createData.setStatus(TicketStatus.OPEN);
            createData.setType(TicketType.INCIDENT);
            createData.setOrganization(ORGANIZATION);
            createData.setRequester(REQUESTER);
            createData.setTechnician(TECHNICIAN);
            long incident2 = this.itopApi.createTicket(createData);
            ((IServiceDeskProblemApi) itopApi).linkIncidentToProblem(problemId, incident2);

            createData = new ItopTicketCreateData();
            createData.setDetail("Tohle je posledni incident a souvisi s problemem c. " + problemId);
            createData.setSubject("Souvisejici incident c. 3");
            createData.setPriority(TicketPriority.HIGH);
            createData.setStatus(TicketStatus.OPEN);
            createData.setType(TicketType.INCIDENT);
            createData.setOrganization(ORGANIZATION);
            createData.setRequester(REQUESTER);
            createData.setTechnician(TECHNICIAN);
            long incident3 = this.itopApi.createTicket(createData);
            ((IServiceDeskProblemApi) itopApi).linkIncidentToProblem(problemId, incident3);

            // get problem with incidents      
            result = this.itopApi.getTicket(problemId, TicketType.PROBLEM);
            Assert.assertTrue(result instanceof ItopProblem);
            problem = (ItopProblem) result;
            Assert.assertEquals("Ticket detail does not match", "Tohle je problem vlozeny do iTop service desk.", problem.getDetail());
            Assert.assertEquals("Ticket subject does not match", "Problem jako pricina.", problem.getSubject());
            Assert.assertEquals(3, problem.getRelatedIncidents().size());

            // get related incidents
            ServiceDeskTicket incidentTicket = this.itopApi.getTicket(problem.getRelatedIncidents().get(2), TicketType.INCIDENT);
            Assert.assertTrue(incidentTicket instanceof ItopIncident);
            ItopIncident incident = (ItopIncident) incidentTicket;
            Assert.assertEquals(incident1, incident.getTicketId());
            Assert.assertEquals("Ticket detail does not match", "Tohle je incident a souvisi s problemem c. " + problemId, incident.getDetail());
            Assert.assertEquals("Ticket subject does not match", "Souvisejici incident c. 1", incident.getSubject());

            incidentTicket = this.itopApi.getTicket(problem.getRelatedIncidents().get(1), TicketType.INCIDENT);
            Assert.assertTrue(incidentTicket instanceof ItopIncident);
            incident = (ItopIncident) incidentTicket;
            Assert.assertEquals(incident2, incident.getTicketId());
            Assert.assertEquals("Ticket detail does not match", "Tohle je dalsi incident a souvisi s problemem c. " + problemId, incident.getDetail());
            Assert.assertEquals("Ticket subject does not match", "Souvisejici incident c. 2", incident.getSubject());

            incidentTicket = this.itopApi.getTicket(problem.getRelatedIncidents().get(0), TicketType.INCIDENT);
            Assert.assertTrue(incidentTicket instanceof ItopIncident);
            incident = (ItopIncident) incidentTicket;
            Assert.assertEquals(incident3, incident.getTicketId());
            Assert.assertEquals("Ticket detail does not match", "Tohle je posledni incident a souvisi s problemem c. " + problemId, incident.getDetail());
            Assert.assertEquals("Ticket subject does not match", "Souvisejici incident c. 3", incident.getSubject());

            ((IServiceDeskProblemApi) itopApi).unlinkIncidentFromProblem(problemId, incident1);
            ((IServiceDeskProblemApi) itopApi).unlinkIncidentFromProblem(problemId, incident2);
            ((IServiceDeskProblemApi) itopApi).unlinkIncidentFromProblem(problemId, incident3);

            // get problem without incidents      
            result = this.itopApi.getTicket(problemId, TicketType.PROBLEM);
            Assert.assertTrue(result instanceof ItopProblem);
            problem = (ItopProblem) result;
            Assert.assertEquals(0, problem.getRelatedIncidents().size());

            // create ticket
            createData = new ItopTicketCreateData();
            createData.setDetail("Problem, detailni popis.");
            createData.setSubject("Problem na zkouseni uprav");
            createData.setPriority(TicketPriority.NORMAL); // priorita u zmeny nema vliv
            createData.setStatus(TicketStatus.OPEN);
            createData.setType(TicketType.PROBLEM);
            createData.setOrganization(ORGANIZATION);
            createData.setRequester(REQUESTER);
            createData.setTechnician(TECHNICIAN);
            long id = this.itopApi.createTicket(createData);

            // get ticket       
            result = this.itopApi.getTicket(id, TicketType.PROBLEM);
            Assert.assertTrue(result instanceof ItopProblem);
            ItopProblem ticket = (ItopProblem) result;
            Assert.assertEquals("Ticket detail does not match", "Problem, detailni popis.", ticket.getDetail());
            Assert.assertEquals("Ticket subject does not match", "Problem na zkouseni uprav", ticket.getSubject());

            // update ticket
            ItopProblemUpdateData updateData = new ItopProblemUpdateData(id, "Upraveny predmet problemu", "Upraveny popis problemu", TicketPriority.NORMAL, TicketStatus.RESOLVED, TicketType.PROBLEM);
            boolean updated = this.itopApi.updateTicket(updateData);
            Assert.assertTrue(updated);
            result = this.itopApi.getTicket(id, TicketType.PROBLEM);
            Assert.assertTrue(result instanceof ItopProblem);
            ticket = (ItopProblem) result;
            Assert.assertEquals("Ticket updated subject does not match", "Upraveny predmet problemu", ticket.getSubject());
            Assert.assertEquals("Ticket updated detail does not match", "Upraveny popis problemu", ticket.getDetail());
            Assert.assertEquals(TicketPriority.NORMAL, ticket.getPriority());
            Assert.assertEquals(TicketStatus.RESOLVED, ticket.getStatus());

            itopApi.addMessage(id, "Prva verejna poznamka", TicketType.PROBLEM);
            itopApi.addMessage(id, "Druha verejna poznamka", TicketType.PROBLEM);
            itopApi.addMessage(id, "Tretija verejna poznamka", TicketType.PROBLEM);
            itopApi.addMessage(id, "Ctverjaja verejna poznamka", TicketType.PROBLEM);

            result = itopApi.getTicket(id, TicketType.PROBLEM);

            List<ServiceDeskMessage> notes = result.getNotes();

            Assert.assertEquals(4, notes.size());
            Assert.assertEquals("Prva verejna poznamka", notes.get(3).getText());
            Assert.assertEquals("Druha verejna poznamka", notes.get(2).getText());
            Assert.assertEquals("Tretija verejna poznamka", notes.get(1).getText());
            Assert.assertEquals("Ctverjaja verejna poznamka", notes.get(0).getText());

            // close ticket
            this.itopApi.closeTicket(problemId, "Dekujeme za vyreseni. Vse OK.", TicketType.PROBLEM);
            result = this.itopApi.getTicket(problemId, TicketType.PROBLEM);
            Assert.assertEquals("Status should be CLOSED", TicketStatus.CLOSED, result.getStatus());
            
        } catch (ServiceDeskCommunicationException ex) {
            Logger.getLogger(ItopClientTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test Itop zmen.
     */
    @Test
    public void testItopChanges() {
        System.out.println("Testing Itop changes");
        try {
            // create ticket
            ItopTicketCreateData createData = new ItopTicketCreateData();
            createData.setDetail("Popis zmeny, napriklad potrebujeme vymenit nefunkcni sluzbu");
            createData.setSubject("Predmet zmeny");
            createData.setPriority(TicketPriority.NORMAL); // priorita u zmeny nema vliv
            createData.setStatus(TicketStatus.OPEN);
            createData.setType(TicketType.CHANGE);
            createData.setOrganization(ORGANIZATION);
            createData.setRequester(REQUESTER);
            createData.setTechnician(TECHNICIAN);
            long id = this.itopApi.createTicket(createData);

            // get ticket       
            ServiceDeskTicket result = this.itopApi.getTicket(id, TicketType.CHANGE);
            Assert.assertTrue(result instanceof ItopChange);
            ItopChange ticket = (ItopChange) result;
            Assert.assertEquals("Ticket detail does not match", "Popis zmeny, napriklad potrebujeme vymenit nefunkcni sluzbu", ticket.getDetail());
            Assert.assertEquals("Ticket subject does not match", "Predmet zmeny", ticket.getSubject());
            Assert.assertEquals(REQUESTER.getId(), ticket.getRequester().getId());
            Assert.assertEquals(TECHNICIAN.getId(), ticket.getTechnician().getId());

            // update ticket
            TicketUpdateData updateData = new TicketUpdateData(id, "Upraveny predmet", "Upraveny popis", null, null, TicketType.CHANGE);
            boolean updated = this.itopApi.updateTicket(updateData);
            Assert.assertTrue(updated);
            result = this.itopApi.getTicket(id, TicketType.CHANGE);
            Assert.assertTrue(result instanceof ItopChange);
            ticket = (ItopChange) result;
            Assert.assertEquals("Ticket updated subject does not match", "Upraveny predmet", ticket.getSubject());
            Assert.assertEquals("Ticket updated detail does not match", "Upraveny popis", ticket.getDetail());

            itopApi.addMessage(id, "Prva verejna poznamka", TicketType.CHANGE);
            itopApi.addMessage(id, "Druha verejna poznamka", TicketType.CHANGE);
            itopApi.addMessage(id, "Tretija verejna poznamka", TicketType.CHANGE);
            itopApi.addMessage(id, "Ctverjaja verejna poznamka", TicketType.CHANGE);

            result = itopApi.getTicket(id, TicketType.CHANGE);

            List<ServiceDeskMessage> notes = result.getNotes();

            Assert.assertEquals(4, notes.size());
            Assert.assertEquals("Prva verejna poznamka", notes.get(3).getText());
            Assert.assertEquals("Druha verejna poznamka", notes.get(2).getText());
            Assert.assertEquals("Tretija verejna poznamka", notes.get(1).getText());
            Assert.assertEquals("Ctverjaja verejna poznamka", notes.get(0).getText());
            
            // status change to implementaiton
            ItopChangeUpdateData update = new ItopChangeUpdateData(id, null, null, TicketStatus.IMPLEMENTATION, TicketType.CHANGE);
            itopApi.updateTicket(update);
            result = itopApi.getTicket(id, TicketType.CHANGE);
            Assert.assertEquals(TicketStatus.IMPLEMENTATION, result.getStatus());
            
            // status change to monitoring
            update = new ItopChangeUpdateData(id, null, null, TicketStatus.MONITORING, TicketType.CHANGE);
            itopApi.updateTicket(update);
            result = itopApi.getTicket(id, TicketType.CHANGE);
            Assert.assertEquals(TicketStatus.MONITORING, result.getStatus());
            
            // status change to planning
            update = new ItopChangeUpdateData(id, null, null, TicketStatus.PLANNING, TicketType.CHANGE);
            itopApi.updateTicket(update);
            result = itopApi.getTicket(id, TicketType.CHANGE);
            Assert.assertEquals(TicketStatus.PLANNING, result.getStatus());
            
            // status change to analysis
            update = new ItopChangeUpdateData(id, null, null, TicketStatus.ANALYSIS, TicketType.CHANGE);
            itopApi.updateTicket(update);
            result = itopApi.getTicket(id, TicketType.CHANGE);
            Assert.assertEquals(TicketStatus.ANALYSIS, result.getStatus());
            
            // resolve
            update = new ItopChangeUpdateData(id, null, null, TicketStatus.MONITORING, TicketType.CHANGE);
            itopApi.updateTicket(update);
            result = itopApi.getTicket(id, TicketType.CHANGE);
            Assert.assertEquals(TicketStatus.MONITORING, result.getStatus());
            
            //close
            update = new ItopChangeUpdateData(id, null, null, TicketStatus.CLOSED, TicketType.CHANGE);
            itopApi.updateTicket(update);
            result = itopApi.getTicket(id, TicketType.CHANGE);
            Assert.assertEquals(TicketStatus.CLOSED, result.getStatus());
            
        } catch (ServiceDeskCommunicationException ex) {
            Logger.getLogger(ItopClientTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

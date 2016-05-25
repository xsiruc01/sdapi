package cz.sd.api.alvao;

import cz.sd.api.alvao.ticket.AlvaoTicketCreateData;
import cz.sd.api.AbstractServiceDeskTest;
import cz.sd.api.alvao.ticket.incident.AlvaoIncident;
import cz.sd.api.IServiceDeskApi;
import cz.sd.api.IServiceDeskProblemApi;
import cz.sd.api.ServiceDeskCommunicationException;
import cz.sd.api.ServiceDeskMessage;
import cz.sd.api.ServiceDeskTicket;
import cz.sd.api.alvao.ticket.change.AlvaoChange;
import cz.sd.api.alvao.ticket.problem.AlvaoProblem;
import cz.sd.api.ticket.TicketPriority;
import cz.sd.api.ticket.TicketStatus;
import cz.sd.api.ticket.TicketType;
import cz.sd.api.ticket.TicketUpdateData;
import cz.sd.api.ticket.UnsupportedServiceDeskApiOperation;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Testy pro ALVAO api.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class AlvaoClientTest extends AbstractServiceDeskTest {

    private IServiceDeskApi alvaoApi;

    @Before
    public void init() {
        alvaoApi = clients.get("Alvao");
    }

    /**
     * Test FreshService klienta.
     */
    @Test
    public void testAlvaoIncidents() {
        System.out.println("Testing Alvao incidents");
        try {
            // create ticket
            AlvaoTicketCreateData createData = new AlvaoTicketCreateData();
            createData.setDetail("Detailni popis ticketu, napr. Hori nam tiskarna, potrebujeme okamzitou pomoc!!!!!");
            createData.setSubject("Predmet ticketu.");
            createData.setPriority(TicketPriority.NORMAL);
            createData.setStatus(TicketStatus.OPEN);
            createData.setType(TicketType.INCIDENT);
            createData.setService("Incident management");
            createData.setSla("Incident SLA");
            long id;

            id = this.alvaoApi.createTicket(createData);

            // get ticket       
            ServiceDeskTicket result = this.alvaoApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertTrue(result instanceof AlvaoIncident);
            AlvaoIncident incident = (AlvaoIncident) result;
            Assert.assertEquals("Ticket detail does not match", "Detailni popis ticketu, napr. Hori nam tiskarna, potrebujeme okamzitou pomoc!!!!!", incident.getDetail());
            Assert.assertEquals("Ticket subject does not match", "Predmet ticketu.", incident.getSubject());

            // update subject
            TicketUpdateData updateData = new TicketUpdateData(id, "Upraveny predmet", null, TicketPriority.CRITICAL, null, TicketType.INCIDENT);
            boolean updated = this.alvaoApi.updateTicket(updateData);
            Assert.assertTrue(updated);
            result = this.alvaoApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertTrue(result instanceof AlvaoIncident);
            incident = (AlvaoIncident) result;
            Assert.assertEquals("Ticket updated subject does not match", "Upraveny predmet", incident.getSubject());
            Assert.assertEquals(TicketPriority.CRITICAL, incident.getPriority());

            // suspend ticket
            try {
                this.alvaoApi.suspendTicket(id, "Pozastavujeme, chybi nam vice informaci", TicketType.INCIDENT);
            } catch (UnsupportedServiceDeskApiOperation ex) {
                // ok
            }

            // open ticket @TODO otevirani ticketu v Alvao???
            //this.alvaoApi.openTicket(id, "Znovu muzeme pracovat", null);
            //result = this.alvaoApi.getTicket(id, TicketType.INCIDENT);
            //Assert.assertEquals("Statuses should be ON HOLD", TicketStatus.OPEN, result.getStatus());
            // resolve ticket
            this.alvaoApi.resolveTicket(id, "Vyreseno, muzete uzavrit", TicketType.INCIDENT);
            result = this.alvaoApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertEquals("Statuses should be RESOLVED", TicketStatus.RESOLVED, result.getStatus());

            //close ticket
            this.alvaoApi.closeTicket(id, "Děkujeme za vyřešení. Vše OK.", TicketType.INCIDENT);
            result = this.alvaoApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertEquals("Status should be CLOSED", TicketStatus.CLOSED, result.getStatus());

            List<ServiceDeskMessage> notes = result.getNotes();

            // kontrola poznamek
            Assert.assertEquals(3, notes.size());
            Assert.assertEquals("Děkujeme za vyřešení. Vše OK.", notes.get(0).getText());
            Assert.assertEquals("Vyreseno, muzete uzavrit", notes.get(1).getText());
        } catch (ServiceDeskCommunicationException ex) {
            Logger.getLogger(AlvaoClientTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testAlvaoProblemApi() {
        System.out.println("Testing Alvao problems");
        try {
            // create problem
            AlvaoTicketCreateData createData = new AlvaoTicketCreateData();
            createData.setDetail("Tohle je problem vlozeny do Alvao service desk.");
            createData.setSubject("Problem jako pricina.");
            createData.setPriority(TicketPriority.HIGH);
            createData.setStatus(TicketStatus.OPEN);
            createData.setType(TicketType.PROBLEM);
            createData.setService("Problem management");
            createData.setSla("Problem SLA");
            long problemId = this.alvaoApi.createTicket(createData);

            // get problem       
            ServiceDeskTicket result = this.alvaoApi.getTicket(problemId, TicketType.PROBLEM);
            Assert.assertTrue(result instanceof AlvaoProblem);
            AlvaoProblem problem = (AlvaoProblem) result;
            Assert.assertEquals("Ticket detail does not match", "Tohle je problem vlozeny do Alvao service desk.", problem.getDetail());
            Assert.assertEquals("Ticket subject does not match", "Problem jako pricina.", problem.getSubject());
            Assert.assertEquals(true, problem.getRelatedIncidents().isEmpty());

            // create incidents
            createData = new AlvaoTicketCreateData();
            createData.setDetail("Tohle je incident a souvisi s problemem c. " + problemId);
            createData.setSubject("Souvisejici incident c. 1");
            createData.setPriority(TicketPriority.HIGH);
            createData.setStatus(TicketStatus.OPEN);
            createData.setType(TicketType.INCIDENT);
            createData.setService("Incident management");
            createData.setSla("Incident SLA");
            long incident1 = this.alvaoApi.createTicket(createData);
            ((IServiceDeskProblemApi) alvaoApi).linkIncidentToProblem(problemId, incident1);

            createData = new AlvaoTicketCreateData();
            createData.setDetail("Tohle je dalsi incident a souvisi s problemem c. " + problemId);
            createData.setSubject("Souvisejici incident c. 2");
            createData.setPriority(TicketPriority.HIGH);
            createData.setStatus(TicketStatus.OPEN);
            createData.setType(TicketType.INCIDENT);
            createData.setService("Incident management");
            createData.setSla("Incident SLA");
            long incident2 = this.alvaoApi.createTicket(createData);
            ((IServiceDeskProblemApi) alvaoApi).linkIncidentToProblem(problemId, incident2);

            createData = new AlvaoTicketCreateData();
            createData.setDetail("Tohle je posledni incident a souvisi s problemem c. " + problemId);
            createData.setSubject("Souvisejici incident c. 3");
            createData.setPriority(TicketPriority.HIGH);
            createData.setStatus(TicketStatus.OPEN);
            createData.setType(TicketType.INCIDENT);
            createData.setService("Incident management");
            createData.setSla("Incident SLA");
            long incident3 = this.alvaoApi.createTicket(createData);
            ((IServiceDeskProblemApi) alvaoApi).linkIncidentToProblem(problemId, incident3);

            // get problem with incidents      
            result = this.alvaoApi.getTicket(problemId, TicketType.PROBLEM);
            Assert.assertTrue(result instanceof AlvaoProblem);
            problem = (AlvaoProblem) result;
            Assert.assertEquals("Ticket detail does not match", "Tohle je problem vlozeny do Alvao service desk.", problem.getDetail());
            Assert.assertEquals("Ticket subject does not match", "Problem jako pricina.", problem.getSubject());
            Assert.assertEquals(3, problem.getRelatedIncidents().size());

            // get related incidents
            ServiceDeskTicket incidentTicket = this.alvaoApi.getTicket(problem.getRelatedIncidents().get(0), TicketType.INCIDENT);
            Assert.assertTrue(incidentTicket instanceof AlvaoIncident);
            AlvaoIncident incident = (AlvaoIncident) incidentTicket;
            Assert.assertEquals(incident1, incident.getTicketId());
            Assert.assertEquals("Ticket detail does not match", "Tohle je incident a souvisi s problemem c. " + problemId, incident.getDetail());
            Assert.assertEquals("Ticket subject does not match", "Souvisejici incident c. 1", incident.getSubject());

            incidentTicket = this.alvaoApi.getTicket(problem.getRelatedIncidents().get(1), TicketType.INCIDENT);
            Assert.assertTrue(incidentTicket instanceof AlvaoIncident);
            incident = (AlvaoIncident) incidentTicket;
            Assert.assertEquals(incident2, incident.getTicketId());
            Assert.assertEquals("Ticket detail does not match", "Tohle je dalsi incident a souvisi s problemem c. " + problemId, incident.getDetail());
            Assert.assertEquals("Ticket subject does not match", "Souvisejici incident c. 2", incident.getSubject());

            incidentTicket = this.alvaoApi.getTicket(problem.getRelatedIncidents().get(2), TicketType.INCIDENT);
            Assert.assertTrue(incidentTicket instanceof AlvaoIncident);
            incident = (AlvaoIncident) incidentTicket;
            Assert.assertEquals(incident3, incident.getTicketId());
            Assert.assertEquals("Ticket detail does not match", "Tohle je posledni incident a souvisi s problemem c. " + problemId, incident.getDetail());
            Assert.assertEquals("Ticket subject does not match", "Souvisejici incident c. 3", incident.getSubject());

            ((IServiceDeskProblemApi) alvaoApi).unlinkIncidentFromProblem(problemId, incident1);
            ((IServiceDeskProblemApi) alvaoApi).unlinkIncidentFromProblem(problemId, incident2);
            ((IServiceDeskProblemApi) alvaoApi).unlinkIncidentFromProblem(problemId, incident3);

            // get problem without incidents      
            result = this.alvaoApi.getTicket(problemId, TicketType.PROBLEM);
            Assert.assertTrue(result instanceof AlvaoProblem);
            problem = (AlvaoProblem) result;
            Assert.assertEquals(0, problem.getRelatedIncidents().size());
        } catch (ServiceDeskCommunicationException ex) {
            Logger.getLogger(AlvaoClientTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testAlvaoChanges() {
        System.out.println("Testing Alvao changes");
        try {
            // create ticket
            AlvaoTicketCreateData createData = new AlvaoTicketCreateData();
            createData.setDetail("Pozadujeme vymenu nejake sluzby a tedy zakladame pozadavek na zmenu.");
            createData.setSubject("Prosime o vymenu sluzby za nejakou lepsi.");
            createData.setPriority(TicketPriority.NORMAL);
            createData.setStatus(TicketStatus.OPEN);
            createData.setType(TicketType.CHANGE);
            createData.setService("Change management");
            createData.setSla("Change SLA");
            long id = this.alvaoApi.createTicket(createData);

            // get ticket       
            ServiceDeskTicket result = this.alvaoApi.getTicket(id, TicketType.CHANGE);
            Assert.assertTrue(result instanceof AlvaoChange);
            AlvaoChange change = (AlvaoChange) result;
            Assert.assertEquals("Ticket detail does not match", "Pozadujeme vymenu nejake sluzby a tedy zakladame pozadavek na zmenu.", change.getDetail());
            Assert.assertEquals("Ticket subject does not match", "Prosime o vymenu sluzby za nejakou lepsi.", change.getSubject());

            // update subject
            TicketUpdateData updateData = new TicketUpdateData(id, "Upraveny predmet zmeny", null, TicketPriority.CRITICAL, null, TicketType.CHANGE);
            boolean updated = this.alvaoApi.updateTicket(updateData);
            Assert.assertTrue(updated);
            result = this.alvaoApi.getTicket(id, TicketType.CHANGE);
            Assert.assertTrue(result instanceof AlvaoChange);
            change = (AlvaoChange) result;
            Assert.assertEquals("Ticket updated subject does not match", "Upraveny predmet zmeny", change.getSubject());
            Assert.assertEquals(TicketPriority.CRITICAL, change.getPriority());

            // suspend ticket
            try {
                this.alvaoApi.suspendTicket(id, "Pozastavujeme, chybi nam vice informaci", TicketType.CHANGE);
            } catch (UnsupportedServiceDeskApiOperation ex) {
                // ok
            }

        // open ticket @TODO otevirani ticketu v Alvao???
            //this.alvaoApi.openTicket(id, "Znovu muzeme pracovat", null);
            //result = this.alvaoApi.getTicket(id, TicketType.INCIDENT);
            //Assert.assertEquals("Statuses should be ON HOLD", TicketStatus.OPEN, result.getStatus());
            // resolve ticket
            this.alvaoApi.resolveTicket(id, "Vyreseno, muzete uzavrit", TicketType.CHANGE);
            result = this.alvaoApi.getTicket(id, TicketType.CHANGE);
            Assert.assertEquals("Statuses should be RESOLVED", TicketStatus.RESOLVED, result.getStatus());

            //close ticket
            this.alvaoApi.closeTicket(id, "Děkujeme za vyřešení. Vše OK.", TicketType.CHANGE);
            result = this.alvaoApi.getTicket(id, TicketType.CHANGE);
            Assert.assertEquals("Status should be CLOSED", TicketStatus.CLOSED, result.getStatus());

            List<ServiceDeskMessage> notes = result.getNotes();

            // kontrola poznamek
            Assert.assertEquals(3, notes.size());
            Assert.assertEquals("Děkujeme za vyřešení. Vše OK.", notes.get(0).getText());
            Assert.assertEquals("Vyreseno, muzete uzavrit", notes.get(1).getText());
        } catch (ServiceDeskCommunicationException ex) {
            Logger.getLogger(AlvaoClientTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

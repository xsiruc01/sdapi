package cz.sd.api.manageengine;

import cz.sd.api.IServiceDeskApi;
import cz.sd.api.ServiceDeskMessage;
import cz.sd.api.ServiceDeskTicket;
import cz.sd.api.AbstractServiceDeskTest;
import cz.sd.api.ServiceDeskCommunicationException;
import cz.sd.api.manageengine.ticket.ManageEngineTicketCreateData;
import cz.sd.api.manageengine.ticket.incident.ManageEngineIncident;
import cz.sd.api.ticket.TicketPriority;
import cz.sd.api.ticket.TicketStatus;
import cz.sd.api.ticket.TicketType;
import cz.sd.api.ticket.TicketUpdateData;
import cz.sd.api.users.ServiceDeskTechnician;
import cz.sd.api.users.ServiceDeskUser;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Testy pro api ManageEngine.
 * @author Pavel Sirucek (xsiruc01)
 */
public class ManageEngineClientTest extends AbstractServiceDeskTest {

    private static final ServiceDeskUser TECHNICIAN = new ServiceDeskTechnician(null, "Howard Stern");
    
    private IServiceDeskApi manageEngineApi;

    @Before
    public void init() {
        manageEngineApi = clients.get("Manage engine");
    }

    /**
     * Test ManageEngine klienta.
     */
    @Test
    public void testManageEngineApi() {
        System.out.println("Testing ManageEngine api");
        try {
            // create ticket
            ManageEngineTicketCreateData createData = new ManageEngineTicketCreateData();
            createData.setDetail("Detailni popis ticketu, napr. Hori nam tiskarna, potrebujeme okamzitou pomoc!!!!!");
            createData.setSubject("Predmet ticketu.");
            createData.setPriority(TicketPriority.NORMAL);
            createData.setStatus(TicketStatus.OPEN);
            createData.setType(TicketType.INCIDENT);
            createData.setTechnician(TECHNICIAN);
            
            createData.setGroup("Network");
            
            long id = this.manageEngineApi.createTicket(createData);

            // get ticket       
            ServiceDeskTicket result = this.manageEngineApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertTrue(result instanceof ManageEngineIncident);
            ManageEngineIncident ticket = (ManageEngineIncident) result;
            Assert.assertEquals("Ticket detail does not match", "Detailni popis ticketu, napr. Hori nam tiskarna, potrebujeme okamzitou pomoc!!!!!", ticket.getDetail());
            Assert.assertEquals("Ticket subject does not match", "Predmet ticketu.", ticket.getSubject());
            Assert.assertEquals(TECHNICIAN.getName(), ticket.getTechnician().getName());

            // update ticket
            TicketUpdateData updateData = new TicketUpdateData(id, "Upraveny predmet", "Upraveny popis", TicketPriority.CRITICAL, null, null);
            boolean updated = this.manageEngineApi.updateTicket(updateData);
            Assert.assertTrue(updated);
            result = this.manageEngineApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertTrue(result instanceof ManageEngineIncident);
            ticket = (ManageEngineIncident) result;
            Assert.assertEquals("Ticket updated subject does not match", "Upraveny predmet", ticket.getSubject());
            Assert.assertEquals("Ticket updated detail does not match", "Upraveny popis", ticket.getDetail());
            Assert.assertEquals(TicketPriority.CRITICAL, ticket.getPriority());

            // suspend ticket
            this.manageEngineApi.suspendTicket(id, "Pozastavujeme, chybi nam vice informaci", null);
            result = this.manageEngineApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertEquals("Statuses should be ON HOLD", TicketStatus.ON_HOLD, result.getStatus());

            // open ticket
            this.manageEngineApi.openTicket(id, "Znovu muzeme pracovat", null);
            result = this.manageEngineApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertEquals("Statuses should be ON HOLD", TicketStatus.OPEN, result.getStatus());

            // resolve ticket
            this.manageEngineApi.resolveTicket(id, "Vyreseno, muzete uzavrit", null);
            result = this.manageEngineApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertEquals("Statuses should be RESOLVED", TicketStatus.RESOLVED, result.getStatus());

            // close ticket
            this.manageEngineApi.closeTicket(id, "Dekujeme za vyreseni. Vse OK.", null);
            result = this.manageEngineApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertEquals("Status should be CLOSED", TicketStatus.CLOSED, result.getStatus());

            List<ServiceDeskMessage> notes = result.getNotes();

        // kontrola poznamek
            // @TODO poznamky jsou v opacnem poradi nez u freshservice
            Assert.assertEquals(4, notes.size());
            Assert.assertEquals("Pozastavujeme, chybi nam vice informaci", notes.get(3).getText());
            Assert.assertEquals("Znovu muzeme pracovat", notes.get(2).getText());
            Assert.assertEquals("Vyreseno, muzete uzavrit", notes.get(1).getText());
            Assert.assertEquals("Dekujeme za vyreseni. Vse OK.", notes.get(0).getText());
        } catch (ServiceDeskCommunicationException ex) {
            Logger.getLogger(ManageEngineClientTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

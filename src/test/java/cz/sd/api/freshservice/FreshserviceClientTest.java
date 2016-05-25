package cz.sd.api.freshservice;

import cz.sd.api.freshservice.ticket.incident.FreshserviceIncident;
import cz.sd.api.IServiceDeskApi;
import cz.sd.api.ServiceDeskMessage;
import cz.sd.api.ServiceDeskTicket;
import cz.sd.api.AbstractServiceDeskTest;
import cz.sd.api.ServiceDeskCommunicationException;
import cz.sd.api.ticket.TicketCreateData;
import cz.sd.api.ticket.TicketPriority;
import cz.sd.api.ticket.TicketStatus;
import cz.sd.api.ticket.TicketType;
import cz.sd.api.ticket.TicketUpdateData;
import cz.sd.api.users.ServiceDeskRequester;
import cz.sd.api.users.ServiceDeskUser;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Testovaci trida pro FreshService.
 *
 * @author Pavel Sirucek
 */
public class FreshserviceClientTest extends AbstractServiceDeskTest {

    private IServiceDeskApi freshServiceApi;

    // zadatel
    private static final ServiceDeskUser REQUESTER = new ServiceDeskRequester(3000923664L, "sirucek@sirucek.freshservice.com");
    
    private static final ServiceDeskUser TECHNICIAN = new ServiceDeskRequester(3000890215L, null);

    @Before
    public void init() {
        freshServiceApi = clients.get("FreshService");
    }

    /**
     * Test FreshService klienta.
     */
    @Test
    public void testFreshServiceApi() {
        System.out.println("Testing FreshService api");
        try {
            // create ticket
            TicketCreateData createData = new TicketCreateData();
            createData.setDetail("Detailni popis ticketu, napr. Hori nam tiskarna, potrebujeme okamzitou pomoc!!!!!");
            createData.setSubject("Predmet ticketu");
            createData.setPriority(TicketPriority.NORMAL);
            createData.setStatus(TicketStatus.OPEN);
            createData.setType(TicketType.INCIDENT);
            createData.setRequester(REQUESTER);
            createData.setTechnician(TECHNICIAN);
            
            long id = this.freshServiceApi.createTicket(createData);

            // get ticket       
            ServiceDeskTicket result = this.freshServiceApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertTrue(result instanceof FreshserviceIncident);
            FreshserviceIncident incident = (FreshserviceIncident) result;
            Assert.assertEquals("Ticket detail does not match", "Detailni popis ticketu, napr. Hori nam tiskarna, potrebujeme okamzitou pomoc!!!!!", incident.getDetail());
            Assert.assertEquals("Ticket subject does not match", "Predmet ticketu", incident.getSubject());
            Assert.assertEquals(REQUESTER.getId(), result.getRequester().getId());
            Assert.assertEquals(TECHNICIAN.getId(), result.getTechnician().getId());

            // update subject
            TicketUpdateData updateData = new TicketUpdateData(id, "Upraveny predmet", null, TicketPriority.CRITICAL, null, null);
            boolean updated = this.freshServiceApi.updateTicket(updateData);
            Assert.assertTrue(updated);
            result = this.freshServiceApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertTrue(result instanceof FreshserviceIncident);
            incident = (FreshserviceIncident) result;
            Assert.assertEquals("Ticket updated subject does not match", "Upraveny predmet", incident.getSubject());
            Assert.assertEquals(TicketPriority.CRITICAL, incident.getPriority());

            // suspend ticket
            this.freshServiceApi.suspendTicket(id, "Pozastavujeme, chybi nam vice informaci", null);
            result = this.freshServiceApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertEquals("Statuses should be ON HOLD", TicketStatus.ON_HOLD, result.getStatus());

            // open ticket
            this.freshServiceApi.openTicket(id, "Znovu muzeme pracovat", null);
            result = this.freshServiceApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertEquals("Statuses should be ON HOLD", TicketStatus.OPEN, result.getStatus());

            // resolve ticket
            this.freshServiceApi.resolveTicket(id, "Vyreseno, muzete uzavrit", null);
            result = this.freshServiceApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertEquals("Statuses should be RESOLVED", TicketStatus.RESOLVED, result.getStatus());

            // close ticket
            this.freshServiceApi.closeTicket(id, "Dekujeme za vyreseni. Vse OK.", null);
            result = this.freshServiceApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertEquals("Status should be CLOSED", TicketStatus.CLOSED, result.getStatus());

            List<ServiceDeskMessage> notes = result.getNotes();

            // kontrola poznamek
            Assert.assertEquals(4, notes.size());
            Assert.assertEquals("Pozastavujeme, chybi nam vice informaci", notes.get(3).getText());
            Assert.assertEquals("Znovu muzeme pracovat", notes.get(2).getText());
            Assert.assertEquals("Vyreseno, muzete uzavrit", notes.get(1).getText());
            Assert.assertEquals("Dekujeme za vyreseni. Vse OK.", notes.get(0).getText());
        } catch (ServiceDeskCommunicationException ex) {
            Logger.getLogger(FreshserviceClientTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

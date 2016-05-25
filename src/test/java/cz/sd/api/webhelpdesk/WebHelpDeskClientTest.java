package cz.sd.api.webhelpdesk;

import cz.sd.api.IServiceDeskApi;
import cz.sd.api.ServiceDeskMessage;
import cz.sd.api.ServiceDeskTicket;
import cz.sd.api.AbstractServiceDeskTest;
import cz.sd.api.ServiceDeskCommunicationException;
import cz.sd.api.ticket.TicketPriority;
import cz.sd.api.ticket.TicketStatus;
import cz.sd.api.ticket.TicketType;
import cz.sd.api.ticket.TicketUpdateData;
import cz.sd.api.users.ServiceDeskRequester;
import cz.sd.api.users.ServiceDeskTechnician;
import cz.sd.api.users.ServiceDeskUser;
import cz.sd.api.webhelpdesk.ticket.WebHelpDeskIncident;
import cz.sd.api.webhelpdesk.ticket.WebHelpDeskTicketCreateData;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Testy pro Web Help Desk api.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class WebHelpDeskClientTest extends AbstractServiceDeskTest {
    
    // zadatel
    private static final ServiceDeskUser REQUESTER = new ServiceDeskRequester(2L, null);
    // technik
    private static final ServiceDeskUser TECHNICIAN = new ServiceDeskTechnician(1L, null);

    private IServiceDeskApi webHelpDeskApi;

    @Before
    public void init() {
        webHelpDeskApi = clients.get("Web help desk");
    }

    /**
     * Test WebHelpDesk klienta.
     */
    @Test
    public void testWebHelpDeskApi() {
        System.out.println("Testing WebHelpDesk api");
        try {
            // create ticket
            WebHelpDeskTicketCreateData createData = new WebHelpDeskTicketCreateData();
            createData.setDetail("Detailni popis ticketu, napr. Hori nam tiskarna, potrebujeme okamzitou pomoc!!!!!");
            createData.setSubject("Predmet ticketu.");
            createData.setPriority(TicketPriority.NORMAL);
            createData.setStatus(TicketStatus.OPEN);
            createData.setType(TicketType.INCIDENT);
            createData.setRequester(REQUESTER);
            createData.setTechnician(TECHNICIAN);
            createData.setServiceId(2L);
            
            
            long id = this.webHelpDeskApi.createTicket(createData);

            // get ticket       
            ServiceDeskTicket result = this.webHelpDeskApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertTrue(result instanceof WebHelpDeskIncident);
            WebHelpDeskIncident ticket = (WebHelpDeskIncident) result;
            Assert.assertEquals("Ticket detail does not match", "Detailni popis ticketu, napr. Hori nam tiskarna, potrebujeme okamzitou pomoc!!!!!", ticket.getDetail());
            Assert.assertEquals("Ticket subject does not match", "Predmet ticketu.", ticket.getSubject());
            Assert.assertEquals(REQUESTER.getId(), ticket.getRequester().getId());
            Assert.assertEquals(TECHNICIAN.getId(), ticket.getTechnician().getId());

            // update subject
            TicketUpdateData updateData = new TicketUpdateData(id, "Upraveny predmet", "Upraveny popis", TicketPriority.CRITICAL, null, null);
            boolean updated = this.webHelpDeskApi.updateTicket(updateData);
            Assert.assertTrue(updated);
            result = this.webHelpDeskApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertTrue(result instanceof WebHelpDeskIncident);
            ticket = (WebHelpDeskIncident) result;
            Assert.assertEquals("Ticket updated subject does not match", "Upraveny predmet", ticket.getSubject());
            Assert.assertEquals("Ticket updated detail does not match", "Upraveny popis", ticket.getDetail());
            Assert.assertEquals(TicketPriority.CRITICAL, ticket.getPriority());

            // suspend ticket
            this.webHelpDeskApi.suspendTicket(id, "Pozastavujeme, chybi nam vice informaci", null);
            result = this.webHelpDeskApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertEquals("Statuses should be ON HOLD", TicketStatus.ON_HOLD, result.getStatus());

            // open ticket
            this.webHelpDeskApi.openTicket(id, "Znovu muzeme pracovat", null);
            result = this.webHelpDeskApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertEquals("Statuses should be ON HOLD", TicketStatus.OPEN, result.getStatus());

            // resolve ticket
            this.webHelpDeskApi.resolveTicket(id, "Vyreseno, muzete uzavrit", null);
            result = this.webHelpDeskApi.getTicket(id, TicketType.INCIDENT);
            Assert.assertEquals("Statuses should be RESOLVED", TicketStatus.RESOLVED, result.getStatus());

            // close ticket
            this.webHelpDeskApi.closeTicket(id, "Dekujeme za vyreseni. Vse OK.", null);
            result = this.webHelpDeskApi.getTicket(id, TicketType.INCIDENT);
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
            Logger.getLogger(WebHelpDeskClientTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

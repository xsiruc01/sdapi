package cz.sd.api.freshservice;

import cz.sd.api.AbstractServiceDeskTest;
import cz.sd.api.ConfigurationItem;
import cz.sd.api.ServiceDeskCommunicationException;
import cz.sd.api.ServiceDeskTicket;
import cz.sd.api.freshservice.cmdb.FreshserviceConfigurationItem;
import cz.sd.api.freshservice.ticket.incident.FreshserviceIncident;
import cz.sd.api.ticket.CiUpdateData;
import cz.sd.api.ticket.TicketCreateData;
import cz.sd.api.ticket.TicketPriority;
import cz.sd.api.ticket.TicketStatus;
import cz.sd.api.ticket.TicketType;
import cz.sd.api.users.ServiceDeskRequester;
import cz.sd.api.users.ServiceDeskUser;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Testy pro konfiguracni polozky Freshservice.
 *
 * @author Pavel Širůček
 */
public class FreshserviceConfigTest extends AbstractServiceDeskTest {

    private FreshserviceClient freshServiceApi;

    // zadatel
    private static final ServiceDeskUser REQUESTER = new ServiceDeskRequester(null, "sirucek@sirucek.freshservice.com");

    @Before
    public void init() {
        freshServiceApi = (FreshserviceClient) clients.get("FreshService");
    }

    @Test
    public void testConfigurationItems() {
        try {
            TicketCreateData createData = new TicketCreateData();
            createData.setSubject("Problem s tiskarnou");
            createData.setDetail("Tiskarna se zasekla a nechce tisknout uz treti den. Divne to v ni chrci a jiskri.");
            createData.setPriority(TicketPriority.LOW);
            createData.setType(TicketType.INCIDENT);
            createData.setConfigurationItem(4L);
            createData.setStatus(TicketStatus.OPEN);
            createData.setRequester(REQUESTER);

            long ticketId = freshServiceApi.createTicket(createData);

            ServiceDeskTicket ticket = freshServiceApi.getTicket(ticketId, TicketType.INCIDENT);

            Assert.assertEquals(true, ticket instanceof FreshserviceIncident);

            FreshserviceIncident incident = (FreshserviceIncident) ticket;

            freshServiceApi.addMessage(ticketId, "Mrknu na to. Pavel", TicketType.INCIDENT);

            ConfigurationItem item = freshServiceApi.getCI(incident.getConfigItemId());
            Assert.assertEquals(true, item instanceof FreshserviceConfigurationItem);

            Assert.assertNotNull(item.getId());
            Assert.assertNotNull(item.getName());
        //Assert.assertNotNull(item.getCreatedDate());

            FreshserviceConfigurationItem freshserviceCi = (FreshserviceConfigurationItem) item;

            Assert.assertNotNull(freshserviceCi.getDetails());
            Assert.assertEquals(false, freshserviceCi.getDetails().isEmpty());
        } catch (ServiceDeskCommunicationException ex) {
            Logger.getLogger(FreshserviceConfigTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testLinkCiToTicket() {
        try {
            TicketCreateData createData = new TicketCreateData();
            createData.setSubject("Problem s tiskarnou (linkovani CI a ticketu)");
            createData.setDetail("Tiskarna se zasekla a nechce tisknout uz treti den. Divne to v ni chrci a jiskri.");
            createData.setPriority(TicketPriority.NORMAL);
            createData.setType(TicketType.INCIDENT);
            createData.setStatus(TicketStatus.OPEN);
            createData.setRequester(REQUESTER);

            long ticketId = freshServiceApi.createTicket(createData);
            ServiceDeskTicket ticket = freshServiceApi.getTicket(ticketId, TicketType.INCIDENT);
            Assert.assertNull(ticket.getConfigItemId());

            this.freshServiceApi.linkCiToTicket(4L, ticketId, TicketType.INCIDENT);

            ticket = freshServiceApi.getTicket(ticketId, TicketType.INCIDENT);
            Assert.assertSame(4L, ticket.getConfigItemId());
        } catch (ServiceDeskCommunicationException ex) {
            Logger.getLogger(FreshserviceConfigTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testUpdateConfigurationItem() {
        try {
            FreshserviceConfigurationItem item = (FreshserviceConfigurationItem) freshServiceApi.getCI(4L);

            CiUpdateData updateData = new CiUpdateData();
            updateData.setCiId(4L);
            updateData.setName("Tiskarna");
            updateData.setDescription("Tiskarna je velka.");

            Map<String, Object> map = new HashMap<>();
            map.put("ip_address_3000881717", "192.168.100.25");
            map.put("printer_type_3000881717", "Laser");
            map.put("cost_3000881706", "4523");
            updateData.setDetails(map);

            freshServiceApi.updateCI(updateData);

            ConfigurationItem updatedItem = freshServiceApi.getCI(4L);
            Assert.assertTrue(updatedItem instanceof FreshserviceConfigurationItem);
            FreshserviceConfigurationItem fsItem = (FreshserviceConfigurationItem) updatedItem;
            Assert.assertEquals("Tiskarna", fsItem.getName());
            Assert.assertEquals("Tiskarna je velka.", fsItem.getDescription());
            Assert.assertEquals("192.168.100.25", fsItem.getDetails().get("ip_address_3000881717"));
            Assert.assertEquals("Laser", fsItem.getDetails().get("printer_type_3000881717"));
            Assert.assertEquals("4523.0", fsItem.getDetails().get("cost_3000881706"));

            // nastavime nazpet puvodni hodnoty
            updateData.setName(item.getName());
            updateData.setDescription(item.getDescription());
            map = new HashMap<>();
            map.put("ip_address_3000881717", item.getDetails().get("ip_address_3000881717"));
            map.put("printer_type_3000881717", item.getDetails().get("printer_type_3000881717"));
            map.put("cost_3000881706", item.getDetails().get("cost_3000881706"));
            updateData.setDetails(map);
            freshServiceApi.updateCI(updateData);
            fsItem = (FreshserviceConfigurationItem) freshServiceApi.getCI(4L);
            Assert.assertEquals(item.getName(), fsItem.getName());
            Assert.assertEquals(item.getDescription(), fsItem.getDescription());
            Assert.assertEquals(item.getDetails().get("ip_address_3000881717"), fsItem.getDetails().get("ip_address_3000881717"));
            Assert.assertEquals(item.getDetails().get("printer_type_3000881717"), fsItem.getDetails().get("printer_type_3000881717"));
            Assert.assertEquals(item.getDetails().get("cost_3000881706"), fsItem.getDetails().get("cost_3000881706"));
        } catch (ServiceDeskCommunicationException ex) {
            Logger.getLogger(FreshserviceConfigTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

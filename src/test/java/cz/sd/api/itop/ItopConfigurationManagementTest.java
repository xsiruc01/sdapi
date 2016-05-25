package cz.sd.api.itop;

import cz.sd.api.itop.ticket.ItopOrganization;
import cz.sd.api.AbstractServiceDeskTest;
import cz.sd.api.ConfigurationItem;
import cz.sd.api.ServiceDeskCommunicationException;
import cz.sd.api.ServiceDeskTicket;
import cz.sd.api.itop.cmdb.ItopConfigurationItem;
import cz.sd.api.itop.ticket.ItopTicketCreateData;
import cz.sd.api.itop.ticket.incident.ItopIncident;
import cz.sd.api.ticket.CiUpdateData;
import cz.sd.api.ticket.TicketPriority;
import cz.sd.api.ticket.TicketStatus;
import cz.sd.api.ticket.TicketType;
import cz.sd.api.users.ServiceDeskRequester;
import cz.sd.api.users.ServiceDeskTechnician;
import cz.sd.api.users.ServiceDeskUser;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Testy pro konfiguracni polozky iTop.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ItopConfigurationManagementTest extends AbstractServiceDeskTest {

    private ItopClient itopApi;

    // zadatel
    private static final ServiceDeskUser REQUESTER = new ServiceDeskRequester(1L, null);
    // technik
    private static final ServiceDeskUser TECHNICIAN = new ServiceDeskTechnician(1L, null);
    // organizace
    private static final ItopOrganization ORGANIZATION = new ItopOrganization(null, "Demo");

    @Before
    public void init() {
        itopApi = (ItopClient) clients.get("itop");
    }

    @Test
    public void testConfigurationItems() {
        try {
            ItopTicketCreateData createData = new ItopTicketCreateData();
            createData.setSubject("Problem s tiskarnou");
            createData.setDetail("Tiskarna se zasekla a nechce tisknout uz treti den. Divne to v ni chrci a jiskri.");
            createData.setPriority(TicketPriority.LOW);
            createData.setType(TicketType.INCIDENT);
            createData.setConfigurationItem(33L);
            createData.setStatus(TicketStatus.OPEN);
            createData.setOrganization(ORGANIZATION);
            createData.setRequester(REQUESTER);
            createData.setTechnician(TECHNICIAN);

            long ticketId = itopApi.createTicket(createData);

            ServiceDeskTicket ticket = itopApi.getTicket(ticketId, TicketType.INCIDENT);

            Assert.assertEquals(true, ticket instanceof ItopIncident);

            ItopIncident incident = (ItopIncident) ticket;

            itopApi.addMessage(ticketId, "Mrknu na to. Pavel", TicketType.INCIDENT);

            ConfigurationItem item = itopApi.getCI(incident.getConfigItemId());
            Assert.assertEquals(true, item instanceof ItopConfigurationItem);

            Assert.assertNotNull(item.getId());
            Assert.assertNotNull(item.getName());
        //Assert.assertNotNull(item.getCreatedDate());

            ItopConfigurationItem itopItem = (ItopConfigurationItem) item;

            Assert.assertNotNull(itopItem.getDetails());
            Assert.assertEquals(false, itopItem.getDetails().isEmpty());
        } catch (ServiceDeskCommunicationException ex) {
            Logger.getLogger(ItopConfigurationManagementTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testUpdateConfigurationItem() {
        try {
            ItopConfigurationItem item = (ItopConfigurationItem) itopApi.getCI(34L);

            CiUpdateData updateData = new CiUpdateData();
            updateData.setCiId(34L);
            updateData.setName("Notasek");
            updateData.setDescription("Notebook pro vedouciho.");
            updateData.setImpact("high");
            updateData.setStatus("implementation");

            Map<String, Object> map = new HashMap<>();
            map.put("cpu", "2.5GHZ");
            map.put("ram", "1024MB");
            updateData.setDetails(map);

            itopApi.updateCI(updateData);

            ConfigurationItem updatedItem = itopApi.getCI(34L);
            org.junit.Assert.assertTrue(updatedItem instanceof ItopConfigurationItem);
            ItopConfigurationItem itopItem = (ItopConfigurationItem) updatedItem;
            org.junit.Assert.assertEquals("Notasek", itopItem.getName());
            org.junit.Assert.assertEquals("Notebook pro vedouciho.", itopItem.getDescription());
            org.junit.Assert.assertEquals("2.5GHZ", itopItem.getDetails().get("cpu"));
            org.junit.Assert.assertEquals("1024MB", itopItem.getDetails().get("ram"));

            // nastavime nazpet puvodni hodnoty
            updateData.setName(item.getName());
            updateData.setDescription(item.getDescription());
            updateData.setImpact(item.getImpact());
            updateData.setStatus(item.getStatus());
            map = new HashMap<>();
            map.put("cpu", item.getDetails().get("cpu"));
            map.put("ram", item.getDetails().get("ram"));
            updateData.setDetails(map);
            itopApi.updateCI(updateData);
            itopItem = (ItopConfigurationItem) itopApi.getCI(34L);
            org.junit.Assert.assertEquals(item.getName(), itopItem.getName());
            org.junit.Assert.assertEquals(item.getDescription(), itopItem.getDescription());
            org.junit.Assert.assertEquals(item.getDetails().get("cpu"), itopItem.getDetails().get("cpu"));
            org.junit.Assert.assertEquals(item.getDetails().get("ram"), itopItem.getDetails().get("ram"));
        } catch (ServiceDeskCommunicationException ex) {
            Logger.getLogger(ItopConfigurationManagementTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

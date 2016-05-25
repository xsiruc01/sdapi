package cz.sd.api.alvao;

import cz.sd.api.alvao.ticket.AlvaoTicketCreateData;
import cz.sd.api.AbstractServiceDeskTest;
import cz.sd.api.alvao.cmdb.AlvaoConfigurationItem;
import cz.sd.api.ConfigurationItem;
import cz.sd.api.ServiceDeskTicket;
import cz.sd.api.alvao.ticket.incident.AlvaoIncident;
import cz.sd.api.ticket.TicketPriority;
import cz.sd.api.ticket.TicketStatus;
import cz.sd.api.ticket.TicketType;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Testy pro cmdb Alvao.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class AlvaoConfigurationManagementTest extends AbstractServiceDeskTest {

    private AlvaoClient alvaoApi;

    @Before
    public void init() {
        alvaoApi = (AlvaoClient) clients.get("Alvao_2");
    }

    @Test
    public void testConfigurationItems() {

        AlvaoTicketCreateData createData = new AlvaoTicketCreateData();
        createData.setSubject("Pc hlasi malo mista");
        createData.setDetail("Pc mi hlasi, ze nemuze pracovat, protoze ma malo mista. Co s tim mam delat, vubec tomu nerozumim.");
        createData.setPriority(TicketPriority.LOW);
        createData.setService("Incident Management");
        createData.setSla("SLA+");
        createData.setType(TicketType.INCIDENT);
        createData.setConfigurationItem(150L);
        createData.setStatus(TicketStatus.OPEN);

        long ticketId = alvaoApi.createTicket(createData);

        ServiceDeskTicket ticket = alvaoApi.getTicket(ticketId, TicketType.INCIDENT);

        Assert.assertEquals(true, ticket instanceof AlvaoIncident);

        AlvaoIncident incident = (AlvaoIncident) ticket;

        alvaoApi.addMessage(ticketId, "Mrknu na to. Pavel", TicketType.INCIDENT);

        ConfigurationItem item = alvaoApi.getCI(incident.getConfigItemId());
        Assert.assertEquals(true, item instanceof AlvaoConfigurationItem);

        Assert.assertNotNull(item.getId());
        Assert.assertNotNull(item.getName());
        //Assert.assertNotNull(item.getCreatedDate());

        AlvaoConfigurationItem alvaoItem = (AlvaoConfigurationItem) item;

        Assert.assertNotNull(alvaoItem.getDetails());
        Assert.assertEquals(false, alvaoItem.getDetails().isEmpty());
    }
}

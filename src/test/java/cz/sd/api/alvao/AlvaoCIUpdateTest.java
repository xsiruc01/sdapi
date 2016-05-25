package cz.sd.api.alvao;

import cz.sd.api.AbstractServiceDeskTest;
import cz.sd.api.alvao.cmdb.AlvaoConfigurationItem;
import cz.sd.api.ticket.CiUpdateData;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class AlvaoCIUpdateTest extends AbstractServiceDeskTest {
    private AlvaoClient alvaoApi;
    
    @Before
    public void init() {
        alvaoApi = (AlvaoClient) clients.get("Alvao");
    }
    
    /**
     * Test FreshService klienta.
     */
    @Test
    public void testCI() {
        AlvaoConfigurationItem item = (AlvaoConfigurationItem) alvaoApi.getCI(146);
        
        Map<String, Object> map = new HashMap<>();
        map.put("Sériové číslo", "123456");
        map.put("Rozhraní disku", "SATA");
        map.put("Název", "Western digital AE456");
        map.put("Výrobce", "Western digital");
        map.put("Volné místo na disku", "15 GB");
        map.put("Velikost", "250 GB");
        
        CiUpdateData updateData = new CiUpdateData();
        updateData.setCiId(146);
        updateData.setDetails(map);
        
        alvaoApi.updateCI(updateData);
        
        AlvaoConfigurationItem updatedItem = (AlvaoConfigurationItem)alvaoApi.getCI(146);
        
        Assert.assertEquals("123456", updatedItem.getDetails().get("Sériové číslo"));
        Assert.assertEquals("SATA", updatedItem.getDetails().get("Rozhraní disku"));
        Assert.assertEquals("Western digital AE456", updatedItem.getDetails().get("Název"));
        Assert.assertEquals("Western digital", updatedItem.getDetails().get("Výrobce"));
        Assert.assertEquals("15 GB", updatedItem.getDetails().get("Volné místo na disku"));
        Assert.assertEquals("250 GB", updatedItem.getDetails().get("Velikost"));
        
        updateData = new CiUpdateData();
        updateData.setCiId(146);       
        map = new HashMap<>();
        map.put("Sériové číslo", item.getDetails().get("Sériové číslo"));
        map.put("Rozhraní disku", item.getDetails().get("Rozhraní disku"));
        map.put("Název", item.getDetails().get("Název"));
        map.put("Výrobce", item.getDetails().get("Výrobce"));
        map.put("Volné místo na disku", item.getDetails().get("Volné místo na disku"));
        map.put("Velikost", item.getDetails().get("Velikost"));
        updateData.setDetails(map);
        
        alvaoApi.updateCI(updateData);
        
        updatedItem = (AlvaoConfigurationItem)alvaoApi.getCI(146);
        
        Assert.assertEquals(item.getDetails().get("Sériové číslo"), updatedItem.getDetails().get("Sériové číslo"));
        Assert.assertEquals(item.getDetails().get("Rozhraní disku"), updatedItem.getDetails().get("Rozhraní disku"));
        Assert.assertEquals(item.getDetails().get("Název"), updatedItem.getDetails().get("Název"));
        Assert.assertEquals(item.getDetails().get("Výrobce"), updatedItem.getDetails().get("Výrobce"));
        Assert.assertEquals(item.getDetails().get("Volné místo na disku"), updatedItem.getDetails().get("Volné místo na disku"));
        Assert.assertEquals(item.getDetails().get("Velikost"), updatedItem.getDetails().get("Velikost"));
    }
}

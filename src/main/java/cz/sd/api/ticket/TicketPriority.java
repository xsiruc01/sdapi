package cz.sd.api.ticket;

import com.google.gson.annotations.SerializedName;

/**
 * Priorita ticketu. Ruzne systemy mohou podporovat ruzna oznaceni. Tahle trida
 * sdruzuje nejpouzivanejsi oznaceni priorit. V pripade ze system podporuje
 * jina oznaceni priorit, mel by byt tento typ zkonvertovan na prioritu pouzitelnou
 * v prislusnem systemu.
 * @author Pavel Sirucek
 */
public enum TicketPriority {
    @SerializedName("Critical")
    CRITICAL(1, "Critical"),
    
    @SerializedName("High")
    HIGH(2, "High"),
    
    @SerializedName("Normal")
    NORMAL(3, "Normal"),
    
    @SerializedName("Low")
    LOW(4, "Low");
    
    /**
     * Identifikator priority. Cim vetsi cislo, tim nizsi priorita.
     */
    private final int id;
    /**
     * Pojmenovani priority.
     */
    private final String name;
    
    /**
     * Konstruktor.
     * @param id Id priority. Cim vetsi, tim nizsi priorita.
     * @param name Pojmenovani priority.
     */
    private TicketPriority(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    /**
     * Getter pro identifikator priority.
     * @return Id priority.
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * Getter pro pojmenovani priority.
     * @return Pojmenovani priority.
     */
    public String getName() {
        return this.name;
    }
    
    
    public static TicketPriority getPriorityById(int id) {
        switch(id) {
            case 1:
                return CRITICAL;
            case 2:
                return HIGH;
            case 3:
                return NORMAL;
            case 4:
                return LOW;
            default:
                return null;
        }
    }
    
    public static TicketPriority valueOfName(String priority) {
        for (TicketPriority value : values()) {
            if (value.getName().equalsIgnoreCase(priority)) {
                return value;
            }
        }
        return null;
    }
}

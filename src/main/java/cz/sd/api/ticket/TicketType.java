package cz.sd.api.ticket;

import com.google.gson.annotations.SerializedName;

/**
 * Typ ticketu. Ruzne systemy nemusi podporovat vsechny typy. Obecne ale kazdy
 * system podporuje minimalne Incidenty (Incident management).
 * <ul>
 *     <li>Incident</li>
 *     <li>Problem</li>
 *     <li>Change</li>
 *     <li>Configuration</li>
 * </ul>
 * @author Pavel Sirucek
 */
public enum TicketType {
    
    @SerializedName("Incident")
    INCIDENT("Incident"),
    
    @SerializedName("Problem")
    PROBLEM("Problem"),
    
    @SerializedName("Change")
    CHANGE("Change");
    
    /**
     * Pojmenovani typu ticketu.
     */
    private final String name;
    
    /**
     * Konstruktor.
     * @param name Pojmenovani typu ticketu.
     */
    private TicketType(String name) {
        this.name = name;
    }
    
    /**
     * Getter pro pojmenovani typy ticketu.
     * @return Typ ticketu - pojmenovani.
     */
    public String getName() {
        return this.name;
    }
    
    public static TicketType valueOfName(String type) {
        for (TicketType value : values()) {
            if (value.getName().equalsIgnoreCase(type)) {
                return value;
            }
        }
        return null;
    }
}

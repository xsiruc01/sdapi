package cz.sd.api.ticket;

import com.google.gson.annotations.SerializedName;

/**
 * Stav ticketu. Ruzne systemy podporuji ruzne stavy. Tato trida sdruzuje nejvice
 * pouzivana oznaceni stavu. Pokud system podporuje jina oznaceni stavu, mel
 * by byt tento stav zkonvertovan na prislusny podporovany stav.
 * @author Pavel Sirucek (xsiruc01)
 */
public enum TicketStatus {
    
    @SerializedName("Open")
    OPEN(1, "Open", "Tiket je otevřený/nově založený/probíhá řešení"),
    
    @SerializedName("On hold")
    ON_HOLD(2, "On hold", "Tiket je pozastavený"),
    
    @SerializedName("Resolved")
    RESOLVED(3, "Resolved", "Ticket je vyřešený"),
    
    @SerializedName("Closed")
    CLOSED(4, "Closed", "Ticket je uzavřený"),
    
    // stavy zmen
    @SerializedName("Analysis")
    ANALYSIS(11, "Analysis", "Probíhá analýza změny"),
    
    @SerializedName("Planning")
    PLANNING(12, "Planning", "Probíhá plánování změny/změna je naplánovaná"),
    
    @SerializedName("Implementation")
    IMPLEMENTATION(13, "Implementation", "Probíhá implementace změny"),
    
    @SerializedName("Monitoring")
    MONITORING(14, "Monitoring", "Probíhá monitorování/testování změny")
    ;
    
    /**
     * Identifikator stavu.
     */
    private final int id;
    /**
     * Pojmenovani stavu.
     */
    private final String name;
    /**
     * Popis stavu.
     */
    private final String description;

    /**
     * Konstruktor.
     * @param id Identifikator stavu.
     * @param name Pojmenovani stavu.
     */
    private TicketStatus(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    
    /**
     * Vrati identifikator stavu.
     * @return Id stavu.
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * Vrati pojmenovani stavu.
     * @return Pojmenovani stavu.
     */
    public String getName() {
        return this.name;
    }
    
    public static TicketStatus getStatusById(int id) {
        switch(id) {
            case 1:
                return OPEN;
            case 2:
                return ON_HOLD;
            case 3:
                return RESOLVED;
            case 4:
                return CLOSED;
            // stavy zmen    
            case 11:
                return ANALYSIS;
            case 12:
                return PLANNING;
            case 13:
                return IMPLEMENTATION;
            case 14:
                return MONITORING;
            default:
                return null;
        }
    }
    
    public static TicketStatus valueOfName(String status) {
        for (TicketStatus value : values()) {
            if (value.getName().equalsIgnoreCase(status)) {
                return value;
            }
        }
        return null;
    }
            
}

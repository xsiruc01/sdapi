package cz.sd.api.ticket;

import lombok.Getter;

/**
 * Vyjimka vyhozena v pripade, kdy api nepodporuje volanou operaci.
 * @author Pavel Sirucek (xsiruc01)
 */
public class UnsupportedServiceDeskApiOperation extends UnsupportedOperationException {
    
    /**
     * Text vyujimky.
     */
    @Getter
    private final String text;

    /**
     * Konstruktor.
     * @param text Text vyjimky.
     */
    public UnsupportedServiceDeskApiOperation(String text) {
        super();
        this.text = text;
    }
}

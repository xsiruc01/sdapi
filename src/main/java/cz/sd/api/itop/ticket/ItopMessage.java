package cz.sd.api.itop.ticket;

/**
 * Poznamka/log v systemu itop.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ItopMessage {

    /**
     * Text poznamky.
     */
    private final String text;
    /**
     * Priznak, zda je poznamka verejna.
     */
    private final boolean isPublic;

    /**
     * Vytvori poznamku.
     *
     * @param text Text poznamky.
     * @param isPublic Priznak, zda je poznamka verejna.
     */
    public ItopMessage(String text, boolean isPublic) {
        this.text = text;
        this.isPublic = isPublic;
    }

    /**
     * Vrati text poznamky.
     *
     * @return Text poznamky.
     */
    public String getText() {
        return this.text;
    }

}

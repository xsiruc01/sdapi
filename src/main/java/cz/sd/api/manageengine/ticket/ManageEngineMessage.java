package cz.sd.api.manageengine.ticket;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Zprava/poznamka k tiketu v ManageEngine.
 * @author Pavel Sirucek (xsiruc01)
 */
public class ManageEngineMessage {
    // nazvy parametru u poznamek
    public static String IS_PUBLIC = "isPublic";
    public static String NOTE_TEXT = "notesText";
    public static String NOTE_DATE = "notesDate";
    
    @Getter @Setter
    private List<ManageEngineParameter> parameter;

    /**
     * Vytvori instanci.
     */
    public ManageEngineMessage() {
    }

    /**
     * Vytvori instanci s nastavenim textu a viditelnosti.
     * @param text Text zpravy.
     * @param isPrivate Viditelnost zpravy.
     */
    public ManageEngineMessage(String text, boolean isPrivate){
        // vytvorime dva parametry - ispublic a text poznamky
        List<ManageEngineParameter> parameters = new ArrayList<>();
        ManageEngineParameter param = new ManageEngineParameter();
        param.setName(IS_PUBLIC);
        param.setValue(Boolean.toString(!isPrivate));
        parameters.add(param);

        param = new ManageEngineParameter();
        param.setName(NOTE_TEXT);
        param.setValue(text);
        parameters.add(param);
     
        this.parameter = parameters;        
    }

    /**
     * Vrati text zpravy.
     * @return Text zpravy.
     */
    public String getText() {
        List<ManageEngineParameter> params = this.getParameter();
        for (ManageEngineParameter param : params) {
            if (param.getName().equals(NOTE_TEXT)) {
                return param.getValue();
            }
        }
        return null;
    }
    
    /**
     * Vrati datum vlozeni zpravy.
     * @return Datum zpravy.
     */
    public String getDate() {
        List<ManageEngineParameter> params = this.getParameter();
        for (ManageEngineParameter param : params) {
            if (param.getName().equals(NOTE_DATE)) {
                return param.getValue();
            }
        }
        return null;
    }
}

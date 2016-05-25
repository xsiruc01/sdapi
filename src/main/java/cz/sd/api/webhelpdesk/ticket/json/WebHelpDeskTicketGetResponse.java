package cz.sd.api.webhelpdesk.ticket.json;

import cz.sd.api.users.ServiceDeskRequester;
import cz.sd.api.users.ServiceDeskTechnician;
import cz.sd.api.users.ServiceDeskUser;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Odpoved Web Help Desku na ziskani konkretniho ticketu. Obsahuje data o
 * ticketu.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter @Setter
public class WebHelpDeskTicketGetResponse {

    private Integer id;
    private String type;
    private Object bccAddresses;
    private Object ccAddressesForTech;
    private Object closeDate;
    private Object departmentId;
    private String lastUpdated;
    private Integer locationId;
    private Integer priorityTypeId;
    private Object room;
    private Integer statusTypeId;
    private String subject;
    private ClientReporter clientReporter;
    private ClientTech clientTech;
    private Object department;
    private Location location;
    private Prioritytype prioritytype;
    private Problemtype problemtype;
    private Statustype statustype;
    private TechGroupLevel techGroupLevel;
    private List<Object> assets;
    private String detail;
    private String reportDateUtc;
    private String displayDueDate;
    private String displayClient;
    private Boolean emailClient;
    private Boolean emailTech;
    private Boolean emailTechGroupLevel;
    private Boolean emailGroupManager;
    private Boolean emailCc;
    private Boolean emailBcc;
    private Boolean needsApproval;
    private Boolean ticketEditable;
    private Integer techId;
    private String levelNumber;
    private Integer clientId;
    private Boolean flaggedByTech;
    private Boolean isPublic;
    private Boolean canEscalate;
    private String bookmarkableLink;
    private Boolean isDeleted;
    private List<Note> notes;
    private List<Object> ticketCustomFields;
    private List<EnabledStatusType> enabledStatusTypes;
    private List<Object> attachments;
    
    /**
     * Z dat odpovedi vytvori uzivatele - tvurce tiketu.
     * @return Tvurce tiketu.
     */
    public ServiceDeskUser getRequester() {
        ClientReporter ticketReporter = this.getClientReporter();
        ServiceDeskRequester requester = new ServiceDeskRequester(ticketReporter.getId().longValue(), ticketReporter.getFirstName() + " " + ticketReporter.getLastName());
        return requester;
    }
    
    /**
     * Z dat odpovedi vytvori uzivatele - technika.
     * @return Resitel/technik.
     */
    public ServiceDeskUser getTechnician() {
        ClientTech ticketTech = this.getClientTech();
        ServiceDeskTechnician technician = new ServiceDeskTechnician(ticketTech.getId().longValue(), ticketTech.getDisplayName());
        return technician;
    }

    @Getter
    @Setter
    public class Note {

        private int id;
        private String type;
        private String date;
        private boolean isSolution;
        private String prettyUpdatedString;
        private String mobileNoteText;
        private boolean isTechNote;
        private boolean isHidden;
        private List<Object> attachments;
    }

    @Getter
    @Setter
    private class ClientReporter {

        private Integer id;
        private String type;
        private String email;
        private String firstName;
        private String lastName;
        private Object notes;
        private Object phone;
        private Object phone2;
        private Object department;
        private Object location;
        private Object room;
        private Object companyName;
    }

    @Getter
    @Setter
    private class ClientTech {

        private Integer id;
        private String type;
        private String email;
        private String displayName;
    }

    @Getter
    @Setter
    class EnabledStatusType {

        private Integer id;
        private String type;
        private String statusTypeName;
    }

    @Getter
    @Setter
    private class Location {

        private Integer id;
        private String type;
        private Object address;
        private Object city;
        private String locationName;
        private Object postalCode;
        private Object state;
        private List<Object> priorityTypes;
        private Object defaultPriorityTypeId;
    }

    @Getter
    @Setter
    private class Prioritytype {

        private Integer id;
        private String type;
        private String priorityTypeName;
    }

    @Getter
    @Setter
    private class Problemtype {

        private Integer id;
        private String type;
        private String detailDisplayName;
    }

    @Getter
    @Setter
    private class Statustype {

        private Integer id;
        private String type;
        private String statusTypeName;
    }

    @Getter
    @Setter
    private class TechGroupLevel {

        private Integer id;
        private String type;
        private Integer level;
        private String levelName;
        private String shortLevelName;
    }
}

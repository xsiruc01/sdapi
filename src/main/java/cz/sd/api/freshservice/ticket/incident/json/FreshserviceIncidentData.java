package cz.sd.api.freshservice.ticket.incident.json;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Vlastni data incidentu.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class FreshserviceIncidentData {

    /**
     * Emailove adresy k preposilani informaci o ticketu.
     */
    private CcEmail cc_email;
    /**
     * Datum vytvoreni ticketu.
     */
    private String created_at;
    /**
     * Priznak, zda je ticket smazan.
     */
    private Boolean deleted;
    /**
     * Delta.
     */
    private Boolean delta;
    /**
     * Popis ticketu.
     */
    private String description;
    /**
     * Html popis ticketu.
     */
    private String description_html;
    /**
     * Id ticketu.
     */
    private Long display_id;
    /**
     * Datum dueBy.
     */
    private String due_by;
    /**
     * Email config id.
     */
    private Long email_config_id;
    /**
     * frDueBy.
     */
    private String frDueBy;
    /**
     * frEscalated.
     */
    private Boolean fr_escalated;
    /**
     * Group id.
     */
    private Long groupId;
    /**
     * Id.
     */
    private Long id;
    /**
     * isescalated.
     */
    private Boolean isescalated;
    /**
     * Poznamky.
     */
    private List<Note> notes;
    /**
     * owner_id.
     */
    private Object owner_id;
    /**
     * Priorita.
     */
    private Integer priority;
    /**
     * Id tvurce pozadavku (ticketu).
     */
    private Long requester_id;
    /**
     * responderId.
     */
    private Long responder_id;
    /**
     * Zdroj vytvoreni ticketu.
     */
    private Long source;
    /**
     * spam.
     */
    private Boolean spam;
    /**
     * Status.
     */
    private Integer status;
    /**
     * Predmet ticketu.
     */
    private String subject;
    /**
     * Typ ticketu.
     */
    private String ticket_type;
    /**
     * toEmail.
     */
    private Object to_email;
    /**
     * trained.
     */
    private Boolean trained;
    /**
     * Datum upravy ticketu.
     */
    private String updated_at;
    /**
     * urgent.
     */
    private Boolean urgent;
    /**
     * Nazev stavu.
     */
    private String status_name;
    /**
     * Nazev stavu od zadavatele.
     */
    private String requester_status_name;
    /**
     * Nazev priority.
     */
    private String priority_name;
    /**
     * Nazev zdroje.
     */
    private String source_name;
    /**
     * Jmeno zadavatele.
     */
    private String requester_name;
    /**
     * responderName.
     */
    private String responder_name;
    /**
     * to_emails.
     */
    private Object to_emails;
    /**
     * department_name.
     */
    private String department_name;
    /**
     * Problem, se kterym je tento ticket asociovan.
     */
    private Object assoc_problem_id;
    /**
     * Zmena, se kterou je tento ticket asociovan.
     */
    private Object assoc_change_id;
    /**
     * assoc_change_cause_id.
     */
    private Object assoc_change_cause_id;
    /**
     * Asset, se kterym je tento ticket asociovan.
     */
    private Long assoc_asset_id;
    /**
     * attachments.
     */
    private List<Object> attachments;
    /**
     * custom_field.
     */
    private CustomField custom_field;

    /**
     * Poznamka/zprava u tiketu/
     */
    @Getter
    @Setter
    public class Note {

        private InnerNote note;

        @Getter
        @Setter
        public class InnerNote {

            private String body;
            private String body_html;
            private String created_at;
            private boolean deleted;
            private long id;
            private boolean incoming;
            @SerializedName("private")
            private boolean isPrivate;
            private int source;
            private String updated_at;
            private long user_id;
            List<Object> attachments;
        }
    }

    /**
     * Seznamy emailu pro notifikace.
     *
     * @author Pavel Sirucek
     */
    @Getter
    @Setter
    private class CcEmail {

        /**
         * Seznam cc emailu.
         */
        private List<String> cc_emails;
        /**
         * Seznam fwd emailu.
         */
        private List<String> fwd_emails;
        /**
         * Seznam reply emailu.
         */
        private List<String> reply_cc;
        /**
         * Seznam tkt emailu.
         */
        private List<String> tkt_cc;
    }

    /**
     * Vlastni poznamky.
     *
     * @author Pavel Sirucek
     */
    private class CustomField {

    }
}

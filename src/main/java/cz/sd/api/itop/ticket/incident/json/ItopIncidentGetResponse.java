package cz.sd.api.itop.ticket.incident.json;

import cz.sd.api.ServiceDeskMessage;
import cz.sd.api.itop.ticket.ItopTicketGetResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Kompletni odpoved pri ziskani incidentu ze systemu iTop. 1:1 s JSON daty.
 * Format odpovedi:
 * <pre>
 * {
 * "objects": {
 * "Incident::25": {
 * "code": 0,
 * "message": "created",
 * "key": "25",
 * "fields": {
 * "ref": "I-000025"
 * ...
 * ...
 * ...
 * }
 * }
 * },
 * "code": 0,
 * "message": null
 * }
 * </pre>
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class ItopIncidentGetResponse extends ItopTicketGetResponse {

    private Map<String, TicketData> objects;
    private Integer code;
    private Object message;

    public List<ServiceDeskMessage> getNotes() {
        List<ServiceDeskMessage> notes = new ArrayList<>();
        List<LogEntry> logEntries;
        logEntries = this.getFields().getPublic_log().getEntries();
        for (LogEntry logEntry : logEntries) {
            String note = logEntry.getMessage().replaceAll("\r\n", "\n");
            String date = logEntry.getDate();
            ServiceDeskMessage newNote = new ServiceDeskMessage(note, date);
            notes.add(newNote);
        }

        return notes;
    }

    /**
     * Vrati id tiketu.
     *
     * @return Id tiketu.
     */
    public long getId() {
        return Long.valueOf(this.getTicketData().getKey());
    }

    /**
     * Vrati predmet tiketu.
     *
     * @return Predmet tiketu.
     */
    public String getSubject() {
        return this.getFields().getTitle();
    }

    /**
     * Vrati popis tiketu.
     *
     * @return Popis tiketu.
     */
    public String getDetail() {
        return this.getFields().getDescription();
    }

    /**
     * Vrati stav tiketu.
     *
     * @return Stav tiketu.
     */
    public String getStatus() {
        return this.getFields().getStatus();
    }

    /**
     * Vrati prioritu tiketu.
     *
     * @return Priorita tiketu.
     */
    public int getPriority() {
        return Integer.valueOf(this.getFields().getUrgency());
    }

    /**
     * Vrati duvod pozastaveni tiketu.
     *
     * @return Duvod pozastaveni tiketu.
     */
    public String getPendingReason() {
        return this.getFields().getPending_reason();
    }

    /**
     * Vrati poznamku k vyreseni tiketu.
     *
     * @return Poznamka k vyreseni tiketu.
     */
    public String getResolvedNote() {
        return this.getFields().getSolution();
    }

    /**
     * Vrati id nadrazeneho problemu.
     *
     * @return Id nadrazeneho problemu.
     */
    public Long getParentProblemId() {
        return Long.valueOf(this.getFields().getParent_problem_id());
    }

    @Override
    public Long getRequestorId() {
        return Long.valueOf(this.getFields().getCaller_id());
    }

    @Override
    public String getRequestorName() {
        return this.getFields().getCaller_name();
    }

    @Override
    public Long getTechnicianId() {
        return Long.valueOf(this.getFields().getAgent_id());
    }

    @Override
    public String getTechnicianName() {
        return this.getFields().getAgent_name();
    }

    private TicketData getTicketData() {
        TicketData ticketData = new TicketData();
        for (Map.Entry<String, TicketData> entrySet : this.objects.entrySet()) {
            ticketData = entrySet.getValue();
        }
        return ticketData;
    }

    private Fields getFields() {
        return this.getTicketData().getFields();
    }

    @Override
    public Long getFunctionalCiId() {
        List<ItopTicketGetResponse.FunctionalCI> list = this.getFields().getFunctionalcis_list();
        if (list == null || list.isEmpty()) {
            return null;
        }
        String id = list.get(0).getFunctionalci_id();

        return Long.valueOf(id);
    }

    @Override
    public boolean isNull() {
        return this.objects == null || this.objects.isEmpty();
    }

    @Override
    public String getCreatedDate() {
        return this.getFields().getStart_date();
    }

    @Override
    public String getLastUpdatedDate() {
        return this.getFields().getLast_update();
    }

    @Override
    public String getDeadlineDate() {
        return this.getFields().getEnd_date();
    }

    @Getter
    @Setter
    private class TicketData {

        private Integer code;
        private String message;
        private String key;
        private Fields fields;
    }

    @Getter
    @Setter
    private class Fields {

        private String ref;
        private String org_id;
        private String org_name;
        private String caller_id;
        private String caller_name;
        private String team_id;
        private String team_name;
        private String agent_id;
        private String agent_name;
        private String title;
        private String description;
        private String start_date;
        private String end_date;
        private String last_update;
        private String close_date;
        private PrivateLog private_log;
        private List<Object> contacts_list;
        private List<ItopTicketGetResponse.FunctionalCI> functionalcis_list;
        private List<Object> workorders_list;
        private String status;
        private String impact;
        private String priority;
        private String urgency;
        private String origin;
        private String service_id;
        private String service_name;
        private String servicesubcategory_id;
        private String servicesubcategory_name;
        private String escalation_flag;
        private String escalation_reason;
        private String assignment_date;
        private String resolution_date;
        private String last_pending_date;
        private Integer cumulatedpending;
        private Integer tto;
        private Integer ttr;
        private String tto_escalation_deadline;
        private String sla_tto_passed;
        private Object sla_tto_over;
        private String ttr_escalation_deadline;
        private String sla_ttr_passed;
        private Object sla_ttr_over;
        private String time_spent;
        private String resolution_code;
        private String solution;
        private String pending_reason;
        private String parent_incident_id;
        private String parent_incident_ref;
        private String parent_problem_id;
        private String parent_problem_ref;
        private String parent_change_id;
        private String parent_change_ref;
        private List<Object> related_request_list;
        private List<Object> child_incidents_list;
        private PublicLog public_log;
        private String user_satisfaction;
        private String user_comment;
        private String finalclass;
        private String friendlyname;
        private String org_id_friendlyname;
        private String caller_id_friendlyname;
        private String team_id_friendlyname;
        private String agent_id_friendlyname;
        private String service_id_friendlyname;
        private String servicesubcategory_id_friendlyname;
        private String parent_incident_id_friendlyname;
        private String parent_problem_id_friendlyname;
        private String parent_change_id_friendlyname;
        private String parent_change_id_finalclass_recall;
    }

    @Getter
    @Setter
    private class PrivateLog {

        private List<LogEntry> entries;
    }

    @Getter
    @Setter
    private class PublicLog {

        private List<LogEntry> entries;
    }

    @Getter
    @Setter
    private class LogEntry {

        private String date;
        private String user_login;
        private String user_id;
        private String message;
    }

}

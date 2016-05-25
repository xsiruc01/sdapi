package cz.sd.api.itop.ticket.problem.json;

import cz.sd.api.ServiceDeskMessage;
import cz.sd.api.itop.ticket.ItopTicketGetResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Response na ziskani problemu.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class ItopProblemGetResponse extends ItopTicketGetResponse {

    private Map<String, TicketData> objects;

    private Integer code;

    private Object message;

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
     * Vrati seznam incidentu, jichz je problem pricinou.
     *
     * @return Seznam id souvisejicich incidentu.
     */
    public List<Long> getRelatedIncidents() {
        List<Long> relatedIncidentIds = new ArrayList<>();
        List<RelatedIncident> relatedIncidentList = this.getFields().getRelated_incident_list();
        for (RelatedIncident relatedIncident : relatedIncidentList) {
            String ref = relatedIncident.getRef();
            String[] splitRef = ref.split("-"); // tvar I-000025
            Long incidentId = Long.valueOf(splitRef[1]);
            relatedIncidentIds.add(incidentId);
        }
        return relatedIncidentIds;
    }

    public List<ServiceDeskMessage> getNotes() {
        List<ServiceDeskMessage> notes = new ArrayList<>();
        List<LogEntry> logEntries;
        logEntries = this.getFields().getPrivate_log().getEntries();
        for (LogEntry logEntry : logEntries) {
            String note = logEntry.getMessage().replaceAll("\r\n", "\n");
            String date = logEntry.getDate();
            ServiceDeskMessage newNote = new ServiceDeskMessage(note, date);
            notes.add(newNote);
        }

        return notes;
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

        private String message;

        private String code;

        private String key;

        private Fields fields;
    }

    @Getter
    @Setter
    private class Fields {

        private String org_name;

        private String service_id;

        private String org_id_friendlyname;

        private String caller_id;

        private String servicesubcategory_id;

        private String caller_id_friendlyname;

        private String resolution_date;

        private String close_date;

        private List<String> workorders_list;

        private String service_name;

        private String servicesubcategory_name;

        private String impact;

        private String urgency;

        private PrivateLog private_log;

        private String title;

        private String end_date;

        private String last_update;

        private String related_change_id_finalclass_recall;

        private String service_id_friendlyname;

        private String priority;

        private String description;

        private String finalclass;

        private List<RelatedIncident> related_incident_list;

        private String assignment_date;

        private String team_id_friendlyname;

        private String caller_name;

        private String agent_id_friendlyname;

        private String status;

        private String team_name;

        private List<ItopTicketGetResponse.FunctionalCI> functionalcis_list;

        private String agent_id;

        private String related_change_id_friendlyname;

        private String product;

        private String org_id;

        private String related_change_id;

        private String ref;

        private List<String> knownerrors_list;

        private String servicesubcategory_id_friendlyname;

        private String friendlyname;

        private List<String> related_request_list;

        private String related_change_ref;

        private String start_date;

        private String agent_name;

        private String team_id;

        private List<String> contacts_list;
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
    public class LogEntry {

        private String date;

        private String user_login;

        private String user_id;

        private String message;
    }

    @Getter
    @Setter
    private class RelatedIncident {

        private String service_id;

        private String org_id_friendlyname;

        private String parent_change_id_finalclass_recall;

        private String servicesubcategory_id;

        private String caller_id_friendlyname;

        private List<String> workorders_list;

        private String parent_change_id;

        private String service_name;

        private String sla_tto_over;

        private String servicesubcategory_name;

        private String impact;

        private String parent_change_ref;

        private String urgency;

        private PrivateLog private_log;

        private String service_id_friendlyname;

        private String priority;

        private String description;

        private String ttr_escalation_deadline;

        private String sla_ttr_passed;

        private String escalation_reason;

        private String assignment_date;

        private String parent_change_id_friendlyname;

        private List<String> child_incidents_list;

        private String tto_escalation_deadline;

        private String agent_id_friendlyname;

        private String status;

        private String user_satisfaction;

        private String sla_tto_passed;

        private String parent_incident_id;

        private String agent_id;

        private String parent_incident_ref;

        private String resolution_code;

        private String last_pending_date;

        private String escalation_flag;

        private String time_spent;

        private String user_comment;

        private String sla_ttr_over;

        private String team_id;

        private String agent_name;

        private String cumulatedpending;

        private List<String> contacts_list;

        private String org_name;

        private String caller_id;

        private String close_date;

        private String resolution_date;

        private String solution;

        private String last_update;

        private String end_date;

        private String title;

        private String finalclass;

        private String team_id_friendlyname;

        private String parent_incident_id_friendlyname;

        private String caller_name;

        private String team_name;

        private String origin;

        private List<String> functionalcis_list;

        private String tto;

        private String org_id;

        private String ref;

        private String pending_reason;

        private String servicesubcategory_id_friendlyname;

        private String friendlyname;

        private String ttr;

        private PublicLog public_log;

        private List<String> related_request_list;

        private String start_date;
    }
}

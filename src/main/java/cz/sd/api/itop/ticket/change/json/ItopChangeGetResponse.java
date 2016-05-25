package cz.sd.api.itop.ticket.change.json;

import cz.sd.api.ServiceDeskMessage;
import cz.sd.api.itop.ticket.ItopTicketGetResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Odpoved na ziskani tiketu typu zmena.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class ItopChangeGetResponse extends ItopTicketGetResponse {

    private Map<String, TicketData> objects;

    private Integer code;

    private Object message;

    /**
     * Vrati seznam zprav tiketu.
     *
     * @return Seznam poznamek/zprav.
     */
    public List<ServiceDeskMessage> getNotes() {
        List<ServiceDeskMessage> notes = new ArrayList<>();
        List<LogEntry> logEntries;
        logEntries = this.getFields().getPrivate_log().getEntries();
        for (LogEntry logEntry : logEntries) {
            String note = logEntry.getMessage().replaceAll("\r\n", "\n");;
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
     * Vrati data tiketu.
     *
     * @return Data tiketu.
     */
    private TicketData getTicketData() {
        TicketData ticketData = new TicketData();
        for (Map.Entry<String, TicketData> entrySet : this.objects.entrySet()) {
            ticketData = entrySet.getValue();
        }
        return ticketData;
    }

    /**
     * Vrati informace o tiketu.
     *
     * @return Informace o tiketu.
     */
    private Fields getFields() {
        return this.getTicketData().getFields();
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

        private String reason;

        private String requestor_id;

        private String requestor_email;

        private String creation_date;

        private String impact;

        private String supervisor_group_id;

        private String supervisor_group_name;

        private String supervisor_id;

        private String supervisor_email;

        private String manager_group_id;

        private String manager_group_name;

        private String manager_id;

        private String manager_email;

        private String outage;

        private String fallback;

        private String parent_id;

        private String parent_name;

        private ArrayList<Object> related_request_list;

        private ArrayList<Object> related_incident_list;

        private ArrayList<Object> related_problems_list;

        private ArrayList<Object> child_changes_list;

        private String finalclass;

        private String friendlyname;

        private String org_id_friendlyname;

        private String caller_id_friendlyname;

        private String team_id_friendlyname;

        private String agent_id_friendlyname;

        private String requestor_id_friendlyname;

        private String supervisor_group_id_friendlyname;

        private String supervisor_id_friendlyname;

        private String manager_group_id_friendlyname;

        private String manager_id_friendlyname;

        private String parent_id_friendlyname;

        private String parent_id_finalclass_recall;
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

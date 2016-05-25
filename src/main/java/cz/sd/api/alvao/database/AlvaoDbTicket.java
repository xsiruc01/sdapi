package cz.sd.api.alvao.database;

import cz.sd.api.ServiceDeskMessage;
import java.sql.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Popis ticketu v ALVAO databazi. Tabulka tHdTicket a pripadne dalsi spojene
 * tabulky. Informace nepouzivane v aplikaci jsou typu Object a jsou
 * zakomentovane. V pripade jejich pouziti je nutne je odkomentovat a v DAO
 * doplnit jejich nastaveni (volani set metody).
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class AlvaoDbTicket {

    /**
     * Predmet ticketu. Sloupec sHdTicket.
     */
    private String subject;
    /**
     * Poznamka. Sloupec mHdTicketNotice.
     */
    private String notice;
    /**
     * Id zadavatele ticketu. Sloupec liHdTicketUserPersonId.
     */
    private int creatorId;
    /**
     * Datum vytvoreni ticketu. Sloupec dHdTicket.
     */
    private Date createdDate;
    /**
     * Deadline ticketu. Sloupec dHdTicketDeadline.
     */
    private Date deadlineDate;
    /*
     private Object category;
     */
    /**
     * Oznaceni stavu. Sloupec sHdTicketStateNotice.
     */
    private String stateNotice;
    /*
     private Object group;
     */
    /**
     * Resitel ticketu. Sloupec liHdTicketSolverPersonId.
     */
    private int solver;
    /**
     * Datum doruceni ticketu. Sloupec dHdTicketReceived.
     */
    private Date receivedDate;
    /**
     * Datum vyreseni ticketu. Sloupec dHdTicketResolved.
     */
    private Date resolvedDate;
    /**
     * Deadline usera. Sloupec dHdTicketUserDeadline.
     */
    private Date userDeadlineDate;
    /**
     * Kod zarizeni. Sloupec sHdTicketDeviceCode.
     */
    private String deviceCode;
    /**
     * Identifikator priority. Sloupec liHdTicketPriorityId.
     */
    private int priority;
    /**
     * Jmeno zadavajiciho. Sloupec sHdTicketUser
     */
    private String ticketUser;
    /*
     private Object userEmail;
     private Object userPhone;
     private Object userContact;
     */
    /**
     * Identifikator sekce - managementu. Sloupec liHdTicketHdSectionId.
     */
    private int section;
    /*
     private Object userMobile;
     private Object userOffice;
     private Object userDepartment;
     private Object userWorkPosition;
     private Object userCompany;
     */
    /**
     * Starting act, kdoi co to je. Sloupec liHdTicketStartingActId.
     */
    private int startingActId;
    /*
     private Object roomId;
     private Object branch;
     */
    /**
     * Textovy identifikator ticketu. Sloupec sHdTicketMessageTag.
     */
    private String messageTag;
    /**
     * Souvisejici asset (konfiguracni polozka). Sloupce liHdTicketNodeId.
     */
    private Long configurationItemId;
    /**
     * Datum odstraneni ticketu, asi. Sloupec dHdTicketRemoved.
     */
    private Object removedDate;
    /**
     * Identifikator SLA. Sloupec liHdTicketSlaId.
     */
    private int slaId;
    /**
     * Identifikator osoby, ktera ticket vyresila. Sloupec
     * liHdTicketResolvedPersonId.
     */
    private int resolvedPersonId;
    /*
     private Object solverOrder;
     private Object userCloseLastAdvised;
     */
    /**
     * Predpokladany pocet hodin na reseni. Sloupec nHdTicketEstimatedHours.
     */
    private int estimatedHours;
    /*
     private Object queueOrder;
     private Object queueDeadline;
     private Object approvalItemId;
     private Object waitingForEmail;
     */
    /**
     * Dopad. Sloupec Impact.
     */
    private int impact;
    /**
     * Urgence. Sloupec Urgency.
     */
    private int urgency;
    /*
     private Object userEmail2;
     private Object userPhone2;
     private Object relatedAccountId;
     private Object feedbackLinkId;
     private Object feedbackSolveSpeed;
     private Object feedbackProfessionality;
     private Object feedbackExpertise;
     private Object feedbackComment;
     */
    /**
     * Datum uzavreni. Sloupec ClosedDate.
     */
    private Date closedDate;
    /**
     * Id uzavirajici osoby. Sloupec ClosedByPersonId.
     */
    private int closedByPersonId;
    /*
     private Object firstReactionDeadline;
     */
    /**
     * Datum posledni aktualizace. Sloupec LastActionDate.
     */
    private Date lastActionDate;
    /*
     private Object waitingActId;
     private Object waitedForUserHours;
     private Object hasReaction;
     */
    /**
     * Pocet odpracovanych hodin. Sloupec WorkHours.
     */
    private int workHours;
    /*
     private Object travelHours;
     private Object travelKm;
     */
    /**
     * Deadline dalsi akce. Sloupec NextActionDeadline.
     */
    private Date nextActionDeadline;
    /**
     * Identifikator ticketu. Sloupec iHdTicketId.
     */
    private int ticketId;
    /**
     * Predmet vytvoreneho ticketu. Tabulka tAct, sloupec mActNotice, id 1.
     */
    private String detail;
    /**
     * Poznamky. Tabulka tAct. sloupec mActNotice, id 7.
     */
    private List<ServiceDeskMessage> notes;
    /**
     * Typ ticketu. Tabulka TicketType.
     */
    private int ticketTypeId;
}

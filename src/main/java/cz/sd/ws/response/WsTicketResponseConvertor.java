package cz.sd.ws.response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.sd.api.ServiceDeskTicket;
import cz.sd.api.alvao.ticket.AlvaoTicket;
import cz.sd.api.alvao.ticket.change.AlvaoChange;
import cz.sd.api.alvao.ticket.incident.AlvaoIncident;
import cz.sd.api.alvao.ticket.problem.AlvaoProblem;
import cz.sd.api.freshservice.ticket.FreshserviceTicket;
import cz.sd.api.freshservice.ticket.incident.FreshserviceIncident;
import cz.sd.api.itop.ticket.ItopTicket;
import cz.sd.api.itop.ticket.change.ItopChange;
import cz.sd.api.itop.ticket.incident.ItopIncident;
import cz.sd.api.itop.ticket.problem.ItopProblem;
import cz.sd.api.manageengine.ticket.ManageEngineTicket;
import cz.sd.api.manageengine.ticket.incident.ManageEngineIncident;
import cz.sd.api.webhelpdesk.ticket.WebHelpDeskIncident;
import cz.sd.api.webhelpdesk.ticket.WebHelpDeskTicket;

/**
 * Konverzni trida pro prevod tiketu na String ve formatu JSON, ktery je vracen
 * klientu.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class WsTicketResponseConvertor {

    /**
     * Prevede tiket na String.
     *
     * @param ticket Tiket.
     * @return Data tiketu jako JSON String.
     */
    public static String convert(ServiceDeskTicket ticket) {

        if (ticket == null) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(new WsTicketNotFoundResponse());
        }

        switch (ticket.getSdType()) {
            case ALVAO:
                return convertAlvao((AlvaoTicket) ticket);
            case ITOP:
                return convertItop((ItopTicket) ticket);
            case FRESHSERVICE:
                return convertFreshservice((FreshserviceTicket) ticket);
            case MANAGE_ENGINE:
                return convertManageEngine((ManageEngineTicket) ticket);
            case WEB_HELP_DESK:
                return convertWebHelpDesk((WebHelpDeskTicket) ticket);
            default:
                return null;

        }
    }

    /**
     * Prevede ALVAO tiket na String.
     *
     * @param ticket ALVAO tiket.
     * @return Tiket jako JSON String.
     */
    private static String convertAlvao(AlvaoTicket ticket) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (ticket instanceof AlvaoIncident) {
            AlvaoIncident incident = (AlvaoIncident) ticket;
            return gson.toJson(incident);
        } else if (ticket instanceof AlvaoProblem) {
            AlvaoProblem problem = (AlvaoProblem) ticket;
            return gson.toJson(problem);
        } else {
            AlvaoChange change = (AlvaoChange) ticket;
            return gson.toJson(change);
        }
    }

    /**
     * Prevede iTop tiket na String.
     *
     * @param ticket iTop tiket.
     * @return Tiket jako JSON String.
     */
    private static String convertItop(ItopTicket ticket) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (ticket instanceof ItopIncident) {
            ItopIncident incident = (ItopIncident) ticket;
            return gson.toJson(incident);
        } else if (ticket instanceof ItopProblem) {
            ItopProblem problem = (ItopProblem) ticket;
            return gson.toJson(problem);
        } else {
            ItopChange change = (ItopChange) ticket;
            return gson.toJson(change);
        }
    }

    /**
     * Prevede Freshservice tiket na String.
     *
     * @param ticket Freshservice tiket.
     * @return Tiket jako JSON String.
     */
    private static String convertFreshservice(FreshserviceTicket ticket) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FreshserviceIncident incident = (FreshserviceIncident) ticket;
        return gson.toJson(incident);
    }

    /**
     * Prevede ManageEngine tiket na String.
     *
     * @param ticket ManageEngine tiket.
     * @return Tiket jako JSON String.
     */
    private static String convertManageEngine(ManageEngineTicket ticket) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ManageEngineIncident incident = (ManageEngineIncident) ticket;
        return gson.toJson(incident);
    }

    /**
     * Prevede WebHelpDesk tiket na String.
     *
     * @param ticket WebHelpDesk tiket.
     * @return Tiket jako JSON String.
     */
    private static String convertWebHelpDesk(WebHelpDeskTicket ticket) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WebHelpDeskIncident incident = (WebHelpDeskIncident) ticket;
        return gson.toJson(incident);
    }
}

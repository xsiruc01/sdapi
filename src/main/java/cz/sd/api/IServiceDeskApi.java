package cz.sd.api;

import cz.sd.api.ticket.TicketCreateData;
import cz.sd.api.ticket.TicketType;
import cz.sd.api.ticket.TicketUpdateData;

/**
 * Rozhrani service desku nebo help desku. Obsahuje metody pro komunikaci a pro
 * praci s tickety. Pro praci se stavy tiketu (workflow) jsou dostupne 4 metody
 * (open, suspend, resolve a close).
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public interface IServiceDeskApi {

    /**
     * Vrati jedinecny nazev service desk systemu.
     *
     * @return Jedinecny nazev/identifikator service desku.
     */
    String getSystemName();
    
    /**
     * Vrati typ service desku.
     * @return Typ service desku.
     */
    SdSystemType getSystemType();

    /**
     * Vytvori ticket v service desku. Vytvareny ticket muze byt ruznych typu,
     * podle dat v {@code data}.
     *
     * @param data Informace nutné pro vytvoření ticketu v service desku.
     * @return Odpoved od systemu o vytvoreni ticketu.
     * @throws cz.sd.api.ServiceDeskCommunicationException v pripade chyby pri
     * komunikaci se service deskem.
     */
    Long createTicket(TicketCreateData data) throws ServiceDeskCommunicationException;

    /**
     * Vyhledá ticket v service desku a vrátí jej.
     *
     * @param id Identifikator ticketu.
     * @param type Typ ticketu.
     * @return Nalezeny ticket nebo {@code null} v pripade, ze ticket nebyl
     * nalezen.
     * @throws cz.sd.api.ServiceDeskCommunicationException v pripade chyby pri
     * komunikaci se service deskem.
     */
    ServiceDeskTicket getTicket(long id, TicketType type) throws ServiceDeskCommunicationException;

    /**
     * Updatuje ticket.
     *
     * @param data Data, ktera maji byt upravena v ticketu.
     * @return true, pokud se update zdaril, false pokud ne
     * @throws cz.sd.api.ServiceDeskCommunicationException v pripade chyby pri
     * komunikaci se service deskem.
     */
    boolean updateTicket(TicketUpdateData data) throws ServiceDeskCommunicationException;

    /**
     * Vyresi ticket.
     *
     * @param id Id ticketu.
     * @param note Poznamka resitele.
     * @param type Typ ticketu.
     * @return True, pokud probehlo vyreseni v poradku, jinak false.
     * @throws cz.sd.api.ServiceDeskCommunicationException v pripade chyby pri
     * komunikaci se service deskem.
     */
    boolean resolveTicket(long id, String note, TicketType type) throws ServiceDeskCommunicationException;

    /**
     * Uzavre ticket.
     *
     * @param id Id ticketu.
     * @param note Poznamka uzaviratele.
     * @param type Typ ticketu.
     * @return True, pokud probehlo uzavreni v poradku, jinak false.
     * @throws cz.sd.api.ServiceDeskCommunicationException v pripade chyby pri
     * komunikaci se service deskem.
     */
    boolean closeTicket(long id, String note, TicketType type) throws ServiceDeskCommunicationException;

    /**
     * Pozastavi reseni ticketu.
     *
     * @param id Id ticketu.
     * @param note Poznamka pozastavujiciho.
     * @param type Typ ticketu.
     * @return True, pokud probehlo pozastaveni v poradku, jinak false.
     * @throws cz.sd.api.ServiceDeskCommunicationException v pripade chyby pri
     * komunikaci se service deskem.
     */
    boolean suspendTicket(long id, String note, TicketType type) throws ServiceDeskCommunicationException;

    /**
     * Zmeni stav ticketu na OPEN.
     *
     * @param id Id ticketu.
     * @param note Poznamka pokracujiciho.
     * @param type Typ ticketu.
     * @return True, pokud probehlo otevreni v poradku, jinak false.
     * @throws cz.sd.api.ServiceDeskCommunicationException v pripade chyby pri
     * komunikaci se service deskem.
     */
    boolean openTicket(long id, String note, TicketType type) throws ServiceDeskCommunicationException;

    /**
     * Smaze ticket. Ticket muze zustat v systemu s nastavenym priznakem (pak se
     * napr. nezobrazi v GUI Service Desku).
     *
     * @param id Id mazaneho ticketu.
     * @param type Typ ticketu.
     * @return True, pokud byl ticket smazan. False pokud ne.
     * @throws cz.sd.api.ServiceDeskCommunicationException v pripade chyby pri
     * komunikaci se service deskem.
     */
    boolean deleteTicket(long id, TicketType type) throws ServiceDeskCommunicationException;

    /**
     * Prida zpravu/poznamku k prislusnemu ticketu. Pomoci zprav lze komunikovat
     * mezi klientem a service desk systemem pres ServiceDeskApi.
     *
     * @param id Id ticketu.
     * @param note Poznamka.
     * @param type Typ ticketu.
     * @return True, pokud byla poznamka uspesne pridana, jinak false.
     * @throws cz.sd.api.ServiceDeskCommunicationException v pripade chyby pri
     * komunikaci se service deskem.
     */
    boolean addMessage(long id, String note, TicketType type) throws ServiceDeskCommunicationException;
}

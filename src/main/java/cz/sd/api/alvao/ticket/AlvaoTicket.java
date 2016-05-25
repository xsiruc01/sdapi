package cz.sd.api.alvao.ticket;

import cz.sd.api.SdSystemType;
import cz.sd.api.ServiceDeskTicket;

/**
 * Ticket v systemu ALVAO service desk.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public abstract class AlvaoTicket extends ServiceDeskTicket {

    /**
     * Identifikator ticketu typu incident v systemu Alvao.
     */
    public static final int ALVAO_INCIDENT_TYPE_ID = 2;
    /**
     * Identifikator ticketu typu problem v systemu Alvao.
     */
    public static final int ALVAO_PROBLEM_TYPE_ID = 5;
    /**
     * Identifikator ticketu typu problem v systemu Alvao.
     */
    public static final int ALVAO_CHANGE_TYPE_ID = 4;

    /**
     * Vytvori instaci objektu s nastaveni typu systemu ALVAO.
     */
    public AlvaoTicket() {
        setSdType(SdSystemType.ALVAO);
    }

}

package cz.sd.api.users;

/**
 * Tvurce ticketu, zadatel, requester.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ServiceDeskRequester extends ServiceDeskUser {

    /**
     * Konstruktor.
     * @param id Identifikator uzivatele.
     * @param name Jmeno uzivatele.
     */
    public ServiceDeskRequester(Long id, String name) {
        super(id, name);
    }

}

package cz.sd.api.users;

/**
 * Technik, resitel, agent.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class ServiceDeskTechnician extends ServiceDeskUser {

    /**
     * Konstruktor.
     *
     * @param id Identifikator uzivatele.
     * @param name Jmeno uzivatele.
     */
    public ServiceDeskTechnician(Long id, String name) {
        super(id, name);
    }

}

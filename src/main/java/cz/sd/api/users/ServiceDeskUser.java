package cz.sd.api.users;

import lombok.Getter;

/**
 * Obecny uzivatel service desku.
 * 
 * @author Pavel Sirucek (xsiruc01)
 */
public abstract class ServiceDeskUser {
    /**
     * Identifikator uzivatele.
     */
    @Getter
    private final Long id;
    /**
     * Jmeno uzivatele.
     */
    @Getter
    private final String name;
    /**
     * Konstruktor.
     * @param id Identifikator uzivatele.
     * @param name Jmeno uzivatele.
     */
    public ServiceDeskUser(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    
}

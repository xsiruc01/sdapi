package cz.sd.api.itop.ticket;

import lombok.Getter;
import lombok.Setter;

/**
 * Organizace v systemu iTop.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class ItopOrganization {

    /**
     * Identifikator organizace.
     */
    private Long id;
    /**
     * Jmeno organizace.
     */
    private String name;

    /**
     * Konstruktor. Udaje mohou byt null.
     *
     * @param id Id organizace.
     * @param name Jmeno organizace.
     */
    public ItopOrganization(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}

package cz.sd.api.alvao.database.dao;

import lombok.Getter;

/**
 * Predek pro DAO tridy obsahujici informace, vyuzitelne pro vsechny potomky.
 * Mimo jine database string a nazev driveru.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public abstract class AlvaoAbstractDao {

    /**
     * Jtds driver.
     */
    protected final static String DRIVER = "net.sourceforge.jtds.jdbc.Driver";

    /**
     * Databazovy string obsahujici informace nutne pro pripojeni k databazi.
     */
    @Getter
    private final String connectionString;

    /**
     * Inicializuje objekt databazovym stringem.
     *
     * @param connectionString Databazovy string.
     */
    public AlvaoAbstractDao(String connectionString) {
        this.connectionString = connectionString;
    }

}

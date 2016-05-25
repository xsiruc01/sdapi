package cz.sd.api.alvao.database;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Popis konfiguracni polozky/assetu v ALVAO databazi. Informace nepouzivane v
 * aplikaci jsou typu Object a jsou zakomentovane. V pripade jejich pouziti je
 * nutne je odkomentovat a v DAO doplnit jejich nastaveni (volani set metody).
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class AlvaoDbConfigItem {

    /**
     * Id konfiguracni polozky. Sloupec intNodeId.
     */
    private int nodeId;
    /**
     * Id rodice. Sloupec lintParentid.
     */
    private Long parentId;
    /*
     private Object lintIconId;
     */
    /**
     * Status. Sloupec intState.
     */
    private int state;
    /**
     * Nazev polozky. Sloupec txtName.
     */
    private String name;
    /*
     private boolean hidden;
     */
    /**
     * Id tridy polozky. Sloupec lintClassid.
     */
    private long classId;
    /*
     private boolean autoUpdate;
     private String LDAPGUID;
     private Object computerStateOn;
     */
    /**
     * Cesta k polozce. Sloupec txtPath.
     */
    private String path;
    /*
     private Object ignoreDifferences;
     private Object swProfileId;
     private Object swAllAllowed;
     private Object lastAgentWsContact;
     */
    /**
     * Vlastnoti polozky. Ukladame do mapy, protoze muzou byt ruzne. Ziskano z
     * pohledu vPropertyKind.
     */
    private Map<String, String> properties;
}

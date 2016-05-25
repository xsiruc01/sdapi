package cz.sd.api.manageengine.ticket;

import lombok.Getter;
import lombok.Setter;

/**
 * Struktura parametru v xml requestu/response.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
@Getter
@Setter
public class ManageEngineParameter {

    /**
     * Parametr XML.
     */
    public static String ID = "workorderid";
    /**
     * Parametr XML.
     */
    public static String REQUESTER = "requester";
    /**
     * Parametr XML.
     */
    public static String CREATOR = "createdby";
    /**
     * Parametr XML.
     */
    public static String SUBJECT = "subject";
    /**
     * Parametr XML.
     */
    public static String SLA = "sla";
    /**
     * Parametr XML.
     */
    public static String STATUS = "status";
    /**
     * Parametr XML.
     */
    public static String PRIORITY = "priority";
    /**
     * Parametr XML.
     */
    public static String GROUP = "group";
    /**
     * Parametr XML.
     */
    public static String DESCRIPTION = "description";
    /**
     * Parametr XML.
     */
    public static String TECHNICIAN = "technician";
    /**
     * Parametr XML.
     */
    public static String CREATED_TIME = "createdtime";
    /**
     * Parametr XML.
     */
    public static String DUE_BY_TIME = "duebytime";
    /**
     * Parametr XML.
     */
    public static String CATEGORY = "category";
    
    
    
    /**
     * Jmeno parametru.
     */
    private String name;
    /**
     * Hodnota paramtru.
     */
    private String value;
}

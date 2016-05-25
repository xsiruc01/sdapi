package cz.sd.api;

import com.google.gson.annotations.SerializedName;

/**
 * Vycet podporovanych service desku.
 *
 * @author Pavel Širůček
 */
public enum SdSystemType {

    @SerializedName("Alvao")
    ALVAO("Alvao"),
    
    @SerializedName("iTop")
    ITOP("iTop"),
    
    @SerializedName("Freshservice")
    FRESHSERVICE("Freshservice"),
    
    @SerializedName("ManageEngine")
    MANAGE_ENGINE("ManageEngine"),
    
    @SerializedName("Web Help Desk")
    WEB_HELP_DESK("Web help desk");

    /**
     * Jmeno service desku.
     */
    private final String systemName;

    /**
     * Vytvori instanci.
     *
     * @param name Jmeno service desku.
     */
    private SdSystemType(String name) {
        this.systemName = name;
    }
    
    /**
     * Vrati jmeno service desku.
     * @return Jmeno sd systemu.
     */
    public String getName() {
        return this.systemName;
    }

}

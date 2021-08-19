package com.sbsc.convertee.entities.unittypes;

import com.sbsc.convertee.entities.unittypes.generic.UnitType;

public class BraSize extends UnitType {

    // SINGLETON
    private static BraSize unitTypeInstance;

    public static final String id = "brasize";

    /**
     * Lazy initialization singleton
     * @return instance of UnitType
     */
    public static UnitType getInstance(){
        if (unitTypeInstance == null){
            unitTypeInstance = new BraSize();
        }
        return unitTypeInstance;
    }

    // Private constructor
    private BraSize(){
        addUnit("eubrasize","brasize_unit_eu",         "0", "75B");
        addUnit("usbrasize","brasize_unit_us",        "1", "34B");
        addUnit("ukbrasize","brasize_unit_uk",        "2", "34B");
        addUnit("frspbrasize","brasize_unit_frsp",      "3", "90B");
        addUnit("jpbrasize","brasize_unit_jp",         "4");
        addUnit("aubrasize","brasize_unit_au",         "5");
    }

    /**
     * Simple getter to reach ID in a non static way
     * @return String
     */
    public String getId(){ return id; }

}
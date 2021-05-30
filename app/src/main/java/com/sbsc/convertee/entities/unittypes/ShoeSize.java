package com.sbsc.convertee.entities.unittypes;

import com.sbsc.convertee.entities.unittypes.generic.UnitType;

public class ShoeSize extends UnitType {

    // SINGLETON
    private static ShoeSize unitTypeInstance;

    public static String id = "shoesize";

    /**
     * Lazy initialization singleton
     * @return instance of UnitType
     */
    public static UnitType getInstance(){
        if (unitTypeInstance == null){
            unitTypeInstance = new ShoeSize();
        }
        return unitTypeInstance;
    }

    // Private constructor
    private ShoeSize(){
        addUnit("eushoesize","shoesize_unit_eu",         "0", "40");
        addUnit("ukshoesize","shoesize_unit_uk",        "1", "7");
        addUnit("mondoshoesize","shoesize_unit_mon",        "2", "255");
        addUnit("usmshoesize","shoesize_unit_usm",      "3", "8");
        addUnit("uswshoesize","shoesize_unit_usw",         "4", "9");
    }

    /**
     * Simple getter to reach ID in a non static way
     * @return String
     */
    public String getId(){ return id; }

}

package com.sbsc.convertee.entities.unittypes;

import com.sbsc.convertee.entities.unittypes.generic.UnitType;

public class ShoeSize  extends UnitType {

    // SINGLETON
    private static ShoeSize unitTypeInstance;

    public static String id = "shoesize";

    /**
     * Lazy initialization singleton
     * @return instance of UnitType
     */
    public static UnitType getInstance(){
        if (unitTypeInstance == null){ //if there is no instance available... create new one
            unitTypeInstance = new ShoeSize();
        }
        return unitTypeInstance;
    }

    // Private constructor
    private ShoeSize(){
        addUnit("eushoesize","shoesize_unit_eu",         "0");
        addUnit("ukshoesize","shoesize_unit_uk",        "1");
        addUnit("mondoshoesize","shoesize_unit_mon",        "2");
        addUnit("usmshoesize","shoesize_unit_usm",      "3");
        addUnit("uswshoesize","shoesize_unit_usw",         "4");
    }

    /**
     * Simple getter to reach ID in a non static way
     * @return String
     */
    public String getId(){ return id; }

}

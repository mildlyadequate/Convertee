package com.sbsc.convertee.entities.unittypes;

import com.sbsc.convertee.entities.unittypes.generic.UnitType;

public class Numerative extends UnitType {

    // SINGLETON
    private static Numerative unitTypeInstance;

    public static String id = "numerative";

    /**
     * Lazy initialization singleton
     * @return instance of UnitType
     */
    public static UnitType getInstance(){
        if (unitTypeInstance == null){ //if there is no instance available... create new one
            unitTypeInstance = new Numerative();
        }
        return unitTypeInstance;
    }

    // Private constructor
    private Numerative(){
        addMainUnit( "decimal","num_unit_dec" );
        addUnit("binary","num_unit_bin","");
        addUnit("octal","num_unit_oct","");
        addUnit("hex","num_unit_hex","");
    }

    /**
     * Simple getter to reach ID in a non static way
     * @return String
     */
    public String getId(){ return id; }
}

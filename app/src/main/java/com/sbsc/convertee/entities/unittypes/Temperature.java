package com.sbsc.convertee.entities.unittypes;

import com.sbsc.convertee.entities.unittypes.generic.UnitType;

public class Temperature extends UnitType {

    // SINGLETON
    private static Temperature unitTypeInstance;

    public static final String id = "temperature";

    /**
     * Lazy initialization singleton
     * @return instance of UnitType
     */
    public static UnitType getInstance(){
        if (unitTypeInstance == null){ //if there is no instance available... create new one
            unitTypeInstance = new Temperature();
        }
        return unitTypeInstance;
    }

    // Private constructor
    private Temperature(){
        addMainUnit( "celsius","temperature_unit_c", "20");
        addUnit("kelvin","temperature_unit_k",          "VAL-273.15__FROM__TO__VAL+273.15", "293");
        addUnit("fahreinheit","temperature_unit_f", "((VAL-32)*5)/9__FROM__TO__9*VAL/5+32", "68");
    }

    /**
     * Simple getter to reach ID in a non static way
     * @return String
     */
    public String getId(){ return id; }
}

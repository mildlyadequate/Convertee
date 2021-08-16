package com.sbsc.convertee.entities.unittypes;

import com.sbsc.convertee.entities.unittypes.generic.UnitType;

public class Pressure extends UnitType {

    // SINGLETON
    private static Pressure unitTypeInstance;

    public static final String id = "pressure";

    /**
     * Lazy initialization singleton
     *
     * @return instance of UnitType
     */
    public static UnitType getInstance() {
        if (unitTypeInstance == null) { //if there is no instance available... create new one
            unitTypeInstance = new Pressure();
        }
        return unitTypeInstance;
    }

    // Private constructor
    private Pressure() {
        setUnitTypeSampleInput("10");
        addMainUnit("pascal", "pressure_unit_pasc");
        addUnit("kilopascal", "pressure_unit_kpasc", "1000");
        addUnit("bar", "pressure_unit_bar", "100000");
        addUnit("psi", "pressure_unit_psi", "6894.7572932");
        addUnit("ksi", "pressure_unit_ksi", "6894757.2932");
        addUnit("standardatmosphere", "pressure_unit_statm", "101325");
        addUnit("torr", "pressure_unit_torr", "133.32236842");
        addUnit("cmwater", "pressure_unit_cmwat", "98.0638");
    }

    /**
     * Simple getter to reach ID in a non static way
     *
     * @return String
     */
    public String getId() {
        return id;
    }
}

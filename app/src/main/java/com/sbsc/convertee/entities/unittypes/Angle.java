package com.sbsc.convertee.entities.unittypes;

import com.sbsc.convertee.entities.unittypes.generic.UnitType;

public class Angle extends UnitType {

    // SINGLETON
    private static Angle unitTypeInstance;

    public static String id = "angle";

    /**
     * Lazy initialization singleton
     *
     * @return instance of UnitType
     */
    public static UnitType getInstance() {
        if (unitTypeInstance == null) { //if there is no instance available... create new one
            unitTypeInstance = new Angle();
        }
        return unitTypeInstance;
    }

    // Private constructor
    private Angle() {
        setUnitTypeSampleInput("180");
        addMainUnit("degree", "angle_unit_deg");
        addUnit("radian", "angle_unit_rad", "VAL*57.295779513__FROM__TO__VAL/57.295779513");
        addUnit("grad", "angle_unit_gra", "VAL*0.9__FROM__TO__VAL/0.9");
        addUnit("turn", "angle_unit_tur", "VAL*360__FROM__TO__VAL/360");
        addUnit("minute", "angle_unit_min", "VAL/60__FROM__TO__VAL*60");
        addUnit("second", "angle_unit_sec", "VAL/3600__FROM__TO__VAL*3600");
        addUnit("rightangle", "angle_unit_ra", "VAL*90__FROM__TO__VAL/90");
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
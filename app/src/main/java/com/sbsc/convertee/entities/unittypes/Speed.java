package com.sbsc.convertee.entities.unittypes;

import com.sbsc.convertee.entities.unittypes.generic.UnitType;

public class Speed  extends UnitType {

    // SINGLETON
    private static Speed unitTypeInstance;

    public static final String id = "speed";

    /**
     * Lazy initialization singleton
     * @return instance of UnitType
     */
    public static UnitType getInstance(){
        if (unitTypeInstance == null){ //if there is no instance available... create new one
            unitTypeInstance = new Speed();
        }
        return unitTypeInstance;
    }

    // Private constructor
    private Speed(){
        setUnitTypeSampleInput("100");
        addUnit("kilometerperhour","speed_unit_km2hr","VAL/3.6__FROM__TO__VAL*3.6");
        addMainUnit("meterpersecond","speed_unit_m2s"                                                       );
        addUnit("milesperhour","speed_unit_mi2hr",           "VAL*0.44704__FROM__TO__VAL/0.44704");
        addUnit("footpersecond","speed_unit_ft2s",           "VAL*0.3048__FROM__TO__VAL/0.3048");
        addUnit("knot","speed_unit_knot",           "VAL/1.9438444924__FROM__TO__VAL*1.9438444924");
        addUnit("earthvelocity","speed_unit_earth",           "VAL*29765__FROM__TO__VAL/29765");
        addUnit("soundspeed","speed_unit_sound",           "VAL*343.2__FROM__TO__VAL/343.2");
        addUnit("lightspeed","speed_unit_light",           "VAL*299792458__FROM__TO__VAL/299792458");
    }

    /**
     * Simple getter to reach ID in a non static way
     * @return String
     */
    public String getId(){ return id; }

}
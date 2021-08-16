package com.sbsc.convertee.entities.unittypes;

import com.sbsc.convertee.entities.unittypes.generic.UnitType;

public class Time extends UnitType {

    // SINGLETON
    private static Time unitTypeInstance;

    public static final String id = "time";

    /**
     * Lazy initialization singleton
     * @return instance of UnitType
     */
    public static UnitType getInstance(){
        if (unitTypeInstance == null){ //if there is no instance available... create new one
            unitTypeInstance = new Time();
        }
        return unitTypeInstance;
    }

    // Private constructor
    private Time(){
        setFirstSelectedUnit("minute");
        addUnit("century","time_unit_c",                  "VAL*52560000__FROM__TO__VAL/52560000", "1");
        addUnit("decade","time_unit_dec",                   "VAL*5256000__FROM__TO__VAL/5256000", "1");
        addUnit("year","time_unit_y",                         "VAL*525600__FROM__TO__VAL/525600", "1");
        addUnit("month","time_unit_mon",                        "VAL*43800__FROM__TO__VAL/43800", "12");
        addUnit("week","time_unit_w",                           "VAL*10080__FROM__TO__VAL/10080", "4");
        addUnit("day","time_unit_d",                              "VAL*1440__FROM__TO__VAL/1440", "30");
        addUnit("hour","time_unit_h",                                 "VAL*60__FROM__TO__VAL/60", "24");
        addMainUnit( "minute","time_unit_min" , "60");
        addUnit("second","time_unit_s",                               "VAL/60__FROM__TO__VAL*60", "60");
        addUnit("millisecond","time_unit_ms",                   "VAL/60000__FROM__TO__VAL*60000", "1000");
        addUnit("microsecond","time_unit_mics",           "VAL/60000000__FROM__TO__VAL*60000000", "1000");
        addUnit("nanosecond","time_unit_ns",        "VAL/60000000000__FROM__TO__VAL*60000000000", "1000");
    }

    /**
     * Simple getter to reach ID in a non static way
     * @return String
     */
    public String getId(){ return id; }
}

package com.sbsc.convertee.entities.unittypes;

import com.sbsc.convertee.entities.unittypes.generic.UnitType;

public class Force extends UnitType {

    // SINGLETON
    private static Force unitTypeInstance;

    public static String id = "force";

    /**
     * Lazy initialization singleton
     * @return instance of UnitType
     */
    public static UnitType getInstance(){
        if (unitTypeInstance == null){ //if there is no instance available... create new one
            unitTypeInstance = new Force();
        }
        return unitTypeInstance;
    }

    // Private constructor
    private Force(){
        setUnitTypeSampleInput("5");
        addMainUnit( "newton","force_unit_nw" );
        addUnit("kilonewton","force_unit_knw",           "VAL*1000__FROM__TO__VAL/1000");
        addUnit("joulemeter","force_unit_jm",           "VAL__FROM__TO__VAL");
        addUnit("joulecentimeter","force_unit_jcm",           "VAL/0.01__FROM__TO__VAL*0.01");
        addUnit("dyne","force_unit_dyn",           "VAL/100000__FROM__TO__VAL*100000");
        addUnit("poundal","force_unit_pdl",           "VAL/7.2330138512__FROM__TO__VAL*7.2330138512");
        addUnit("kilogramforce","force_unit_kgf",           "VAL*9.80665__FROM__TO__VAL/9.80665");
        addUnit("gramforce","force_unit_gf",           "VAL/101.9716213__FROM__TO__VAL*101.9716213");

    }

    /**
     * Simple getter to reach ID in a non static way
     * @return String
     */
    public String getId(){ return id; }
}

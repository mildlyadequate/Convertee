package com.sbsc.convertee.entities.unittypes;

import com.sbsc.convertee.entities.unittypes.generic.UnitType;

public class Weight extends UnitType {

    // SINGLETON
    private static Weight unitTypeInstance;

    public static final String id = "weight";

    /**
     * Lazy initialization singleton
     * @return instance of UnitType
     */
    public static UnitType getInstance(){
        if (unitTypeInstance == null){ //if there is no instance available... create new one
            unitTypeInstance = new Weight();
        }
        return unitTypeInstance;
    }

    // Private constructor
    private Weight(){
        setUnitTypeSampleInput("100");
        setFirstSelectedUnit("gram");
        addUnit("tonnemetric","weight_unit_tonnemetric",    "1000000");
        addUnit("kilogram","weight_unit_kg",             "1000");
        addMainUnit( "gram","weight_unit_g" );
        addUnit("centigram","weight_unit_cg",            "0.01");
        addUnit("milligram","weight_unit_mg",           "0.001");
        addUnit("microgram","weight_unit_micg",      "0.000001");
        addUnit("stone","weight_unit_st",           "6350.2934");
        addUnit("pound","weight_unit_lbs",          "453.59237");
        addUnit("ounce","weight_unit_oz",            "28.34952");
        addUnit("carat","weight_unit_ct",                 "0.2");
        addUnit("longton","weight_unit_longton",          "984297.1");
        addUnit("shortton","weight_unit_shortton",        "1102312.0");
    }

    /**
     * Simple getter to reach ID in a non static way
     * @return String
     */
    public String getId(){ return id; }
}

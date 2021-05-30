package com.sbsc.convertee.entities.unittypes;

import com.sbsc.convertee.entities.unittypes.generic.UnitType;

public class Distance extends UnitType {

    // SINGLETON
    private static Distance unitTypeInstance;

    public static String id = "distance";

    /**
     * Lazy initialization singleton
     * @return instance of UnitType
     */
    public static UnitType getInstance(){
        if (unitTypeInstance == null){ //if there is no instance available... create new one
            unitTypeInstance = new Distance();
        }
        return unitTypeInstance;
    }

    // Private constructor
    private Distance(){
        setUnitTypeSampleInput("100");
        setFirstSelectedUnit( "meter" );
        addUnit("kilometer","distance_unit_km",         "1000");
        addMainUnit("meter","distance_unit_m"                                  );
        addUnit("centimeter","distance_unit_cm",        "0.01");
        addUnit("millimeter","distance_unit_mm",        "0.001");
        addUnit("micrometer","distance_unit_micm",      "0.000001");
        addUnit("nanometer","distance_unit_nm",         "0.000000001");
        addUnit("mile","distance_unit_mi",              "1609.344");
        addUnit("yard","distance_unit_yd",              "0.9144");
        addUnit("foot","distance_unit_ft",              "0.3048");
        addUnit("inch","distance_unit_in",              "0.0254");
    }

    /**
     * Simple getter to reach ID in a non static way
     * @return String
     */
    public String getId(){ return id; }

}

package com.sbsc.convertee.entities.unittypes;

import com.sbsc.convertee.entities.unittypes.generic.UnitType;

public class Area extends UnitType {

    // SINGLETON
    private static Area unitTypeInstance;

    public static String id = "area";

    /**
     * Lazy initialization singleton
     *
     * @return instance of UnitType
     */
    public static UnitType getInstance() {
        if (unitTypeInstance == null) { //if there is no instance available... create new one
            unitTypeInstance = new Area();
        }
        return unitTypeInstance;
    }

    // Private constructor
    private Area() {
        setUnitTypeSampleInput("50");
        setFirstSelectedUnit("squaremeter");
        addUnit("squarekilometer", "area_unit_sqkm", "1000000");
        addMainUnit("squaremeter", "area_unit_sqm");
        addUnit("squarecentimeter", "area_unit_sqcm", "0.0001");
        addUnit("squaremillimeter", "area_unit_sqmm", "0.000001");
        addUnit("squaremicrometer", "area_unit_sqmicm", "0.000000000001");
        addUnit("hectare", "area_unit_hec", "10000");
        addUnit("acre", "area_unit_acre", "4046.8564224");
        addUnit("squaremile", "area_unit_sqmi", "2589988.1103");
        addUnit("squareyard", "area_unit_sqyd", "0.83612736");
        addUnit("squarefoot", "area_unit_sqft", "0.09290304");
        addUnit("squareinch", "area_unit_sqin", "0.00064516");

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
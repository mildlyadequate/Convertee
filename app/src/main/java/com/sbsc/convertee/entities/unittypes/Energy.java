package com.sbsc.convertee.entities.unittypes;

import com.sbsc.convertee.entities.unittypes.generic.UnitType;

public class Energy extends UnitType {

    // SINGLETON
    private static Energy unitTypeInstance;

    public static String id = "energy";

    /**
     * Lazy initialization singleton
     * @return instance of UnitType
     */
    public static UnitType getInstance(){
        if (unitTypeInstance == null){ //if there is no instance available... create new one
            unitTypeInstance = new Energy();
        }
        return unitTypeInstance;
    }

    // Private constructor
    private Energy(){
        setUnitTypeSampleInput("100");
        setFirstSelectedUnit("watthour");
        addUnit("gigawatthour","energy_unit_gwtth",           "3600000000000");
        addUnit("megawatthour","energy_unit_mwtth",           "3600000000");
        addUnit("kilowatthour","energy_unit_kwtth",           "3600000");
        addUnit("watthour","energy_unit_wtth",           "3600");
        addUnit("kilojoule","energy_unit_kj",           "1000");
        addMainUnit( "joule","energy_unit_j" );
        addUnit("millijoule","energy_unit_mj",           "0.001");
        addUnit("calorienut","energy_unit_caln",           "4186.8");
        addUnit("horsepowerm","energy_unit_hpm",           "2647795.5");
        addUnit("horsepoweri","energy_unit_hpi",           "2684519.5369");

    }

    /**
     * Simple getter to reach ID in a non static way
     * @return String
     */
    public String getId(){ return id; }
}

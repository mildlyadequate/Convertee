package com.sbsc.convertee.entities.unittypes;

import com.sbsc.convertee.entities.unittypes.generic.UnitType;

public class FuelEconomy extends UnitType {

    // SINGLETON
    private static FuelEconomy unitTypeInstance;

    public static String id = "fueleconomy";

    /**
     * Lazy initialization singleton
     * @return instance of UnitType
     */
    public static UnitType getInstance(){
        if (unitTypeInstance == null){ //if there is no instance available... create new one
            unitTypeInstance = new FuelEconomy();
        }
        return unitTypeInstance;
    }

    // Private constructor
    private FuelEconomy(){
        setUnitTypeSampleInput("1");
        addMainUnit( "kilometerperliter","fuelcons_unit_km2l" );
        addUnit("meterperliter","fuelcons_unit_m2l",           "0.001");
        addUnit("milepergallonuk","fuelcons_unit_m2guk",           "0.35400619");
        addUnit("milepergallonus","fuelcons_unit_m2gus",           "0.4251437075");
        addUnit("kilometerpergallonus","fuelcons_unit_km2gus",           "0.2641720524");
        addUnit("kilometerpergallonuk","fuelcons_unit_km2guk",           "0.2199688");
    }

    /**
     * Simple getter to reach ID in a non static way
     * @return String
     */
    public String getId(){ return id; }
}

package com.sbsc.convertee.entities.unittypes;

import com.sbsc.convertee.entities.unittypes.generic.UnitType;

public class Volume extends UnitType {

    // SINGLETON
    private static Volume unitTypeInstance;

    public static String id = "volume";

    /**
     * Lazy initialization singleton
     * @return instance of UnitType
     */
    public static UnitType getInstance(){
        if (unitTypeInstance == null){ //if there is no instance available... create new one
            unitTypeInstance = new Volume();
        }
        return unitTypeInstance;
    }

    // Private constructor
    private Volume(){
        setUnitTypeSampleInput("5");
        addMainUnit( "liter","volume_unit_l" );
        addUnit("milliliter","volume_unit_ml",          "0.001");
        addUnit("cubickilometer","volume_unit_ckm", "1000000000000");
        addUnit("cubicmeter","volume_unit_cm", "1000");
        addUnit("cubiccentimeter","volume_unit_ccm", "0.001");
        addUnit("cubicmillimeter","volume_unit_cmm", "0.000001");
        addUnit("teaspoonmet","volume_unit_tspm", "0.005");
        addUnit("teaspoonuk","volume_unit_tspuk", "0.005919388");
        addUnit("tablespoonmet","volume_unit_tbspm", "0.015");
        addUnit("tablespoonuk","volume_unit_tbspuk", "0.0177581641");
        addUnit("cubicmile","volume_unit_cmi", "4168181825441");
        addUnit("cubicyard","volume_unit_cyd", "764.55485798");
        addUnit("cubicfoot","volume_unit_cft", "28.316846592");
        addUnit("cubicinch","volume_unit_cin", "0.016387064");
        addUnit("gallonuk","volume_unit_galuk", "4.54609");
        addUnit("quartuk","volume_unit_quaruk", "1.1365225");
        addUnit("pintuk","volume_unit_piuk", "0.56826125");
        addUnit("cupuk","volume_unit_cupuk", "0.284130625");
        addUnit("gallonus","volume_unit_galus", "3.785411784");
        addUnit("quartus","volume_unit_quarus", "0.946352946");
        addUnit("pintus","volume_unit_pius", "0.473176473");
        addUnit("cupus","volume_unit_cupus", "0.2365882365");
    }

    /**
     * Simple getter to reach ID in a non static way
     * @return String
     */
    public String getId(){ return id; }
}
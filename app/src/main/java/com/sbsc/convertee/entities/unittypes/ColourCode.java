package com.sbsc.convertee.entities.unittypes;

import com.sbsc.convertee.entities.unittypes.generic.UnitType;

public class ColourCode extends UnitType {

    // SINGLETON
    private static ColourCode unitTypeInstance;

    public static String id = "colour";

    /**
     * Lazy initialization singleton
     * @return instance of UnitType
     */
    public static UnitType getInstance(){
        if (unitTypeInstance == null){
            unitTypeInstance = new ColourCode();
        }
        return unitTypeInstance;
    }

    // Private constructor
    private ColourCode(){
        addUnit("colourrgb","colour_unit_rgb",           "");
        addUnit("colourhex","colour_unit_hex",           "");
        addUnit("colourhsl","colour_unit_hsl",           "");
        addUnit("colourhsv","colour_unit_hsv",           "");
        addUnit("colourcmyk","colour_unit_cmyk",           "");
        addUnit("colourname","colour_unit_name",           "");
    }

    /**
     * Simple getter to reach ID in a non static way
     * @return String
     */
    public String getId(){ return id; }
}

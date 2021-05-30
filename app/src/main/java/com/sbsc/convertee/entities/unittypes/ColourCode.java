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
        addUnit("colourrgb","colour_unit_rgb",           "", "255,255,255");
        addUnit("colourhex","colour_unit_hex",           "", "#FFFFFF");
        addUnit("colourhsl","colour_unit_hsl",           "", "360,100,100");
        addUnit("colourhsv","colour_unit_hsv",           "", "360,100,100");
        addUnit("colourcmyk","colour_unit_cmyk",           "", "100,100,100,100");
        addUnit("colourname","colour_unit_name",           "", "lime green");
    }

    /**
     * Simple getter to reach ID in a non static way
     * @return String
     */
    public String getId(){ return id; }
}

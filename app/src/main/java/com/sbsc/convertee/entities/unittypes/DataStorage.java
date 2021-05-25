package com.sbsc.convertee.entities.unittypes;

import com.sbsc.convertee.entities.unittypes.generic.UnitType;

public class DataStorage extends UnitType {

    // SINGLETON
    private static DataStorage unitTypeInstance;

    public static String id = "datastorage";

    /**
     * Lazy initialization singleton
     * @return instance of UnitType
     */
    public static UnitType getInstance(){
        if (unitTypeInstance == null){ //if there is no instance available... create new one
            unitTypeInstance = new DataStorage();
        }
        return unitTypeInstance;
    }

    // Private constructor
    private DataStorage(){

        addUnit("exabyte","datastorage_unit_ebyte",           "1099511627776");
        addUnit("exabit","datastorage_unit_ebit",           "137438953472");

        addUnit("petabyte","datastorage_unit_pbyte",           "1073741824");
        addUnit("petabit","datastorage_unit_pbit",           "134217728");

        addUnit("terabyte","datastorage_unit_tbyte",           "1048576");
        addUnit("terabit","datastorage_unit_tbit",           "131072");

        addUnit("gigabyte","datastorage_unit_gbyte",           "1024");
        addUnit("gigabit","datastorage_unit_gbit",           "128");

        addMainUnit( "megabyte","datastorage_unit_mbyte" );
        addUnit("megabit","datastorage_unit_mbit",           "0.125");

        addUnit("kilobyte","datastorage_unit_kbyte",           "0.0009765625");
        addUnit("kilobit","datastorage_unit_kbit",           "0.0001220703");

        addUnit("character","datastorage_unit_char",           "0.0000009536743164");

        addUnit("byte","datastorage_unit_byte",           "0.0000009536743164");
        addUnit("bit","datastorage_unit_bit",           "0.000001192092895");
    }

    /**
     * Simple getter to reach ID in a non static way
     * @return String
     */
    public String getId(){ return id; }
}

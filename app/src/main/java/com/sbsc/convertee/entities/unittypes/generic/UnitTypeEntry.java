package com.sbsc.convertee.entities.unittypes.generic;

import android.content.Context;

import com.sbsc.convertee.entities.adapteritems.LocalizedUnit;

/**
 * Simple class to contain to Strings (used instead of Entry for better performance)
 */
public class UnitTypeEntry{
    private String name;
    private String value;

    public UnitTypeEntry(){
        name = "";
        value = "";
    }

    public UnitTypeEntry(String name, String value) {
        setContent(name,value);
    }

    public void setContent(String name, String value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Localize this Entry using Android String Resources
     * @param context required
     * @return LocalizedUnit object of this Entry
     */
    public LocalizedUnit localize( Context context ){
        // Get Name from string.xml using value of unit as key
        String localizedName = context.getResources().getString(
                context.getResources().getIdentifier( value,"string", context.getPackageName() )
        );

        // Get ShortName from string.xml and adding _short to key of unit
        String localizedNameShort = context.getResources().getString(
                context.getResources().getIdentifier( value+"_short", "string", context.getPackageName() )
        );
        return new LocalizedUnit( name , localizedName , localizedNameShort );
    }
}
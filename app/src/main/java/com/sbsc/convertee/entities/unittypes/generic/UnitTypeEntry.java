package com.sbsc.convertee.entities.unittypes.generic;

import android.content.Context;

import com.sbsc.convertee.entities.adapteritems.LocalizedUnit;

/**
 * Simple class to contain to Strings (used instead of Entry for better performance)
 */
public class UnitTypeEntry{
    private String unitKey;
    private String resourceId;
    private String sampleInput;

    public UnitTypeEntry(){
        unitKey = "";
        resourceId = "";
        sampleInput = "";
    }

    public UnitTypeEntry( String unitKey , String resourceId , String sampleInput ) {
        setContent( unitKey, resourceId , sampleInput );
    }

    public void setContent( String name , String value , String sampleInput ){
        this.unitKey = name;
        this.resourceId = value;
        this.sampleInput = sampleInput;
    }

    /**
     * Localize this Entry using Android String Resources
     * @param context required
     * @return LocalizedUnit object of this Entry
     */
    public LocalizedUnit localize( Context context ){
        // Get Name from string.xml using value of unit as key
        String localizedName = context.getResources().getString(
                context.getResources().getIdentifier(resourceId,"string", context.getPackageName() )
        );

        // Get ShortName from string.xml and adding _short to key of unit
        String localizedNameShort = context.getResources().getString(
                context.getResources().getIdentifier( resourceId +"_short", "string", context.getPackageName() )
        );

        return new LocalizedUnit(unitKey, localizedName , localizedNameShort , sampleInput);
    }
}
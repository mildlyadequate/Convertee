package com.sbsc.convertee.entities.unittypes.generic;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
    Parent for all other UnitType classes (such as weight, distance, ...)
 */
@SuppressWarnings("unused")
public class UnitType{

    private final Map<String, RawUnit> rawUnitList = new LinkedHashMap<>();

    /**
     * unitName of the mainUnit which the calculator converts everything into which the unitFactors
     * are relevant to
     */
    protected String mainUnitName;


    /**
     * Used to identify UnitTypes easily when switching
     * Will also be used to retrieve specific String resources from android depending on which unit
     * is selected: It will be attached to the base resource name eg. "UNITTYPEID_base_resource"
     * This means this id should be the exact same value as the one being used in the android resources
     * -> NOT DONE in code, as I can't retrieve the context in UnitType thus can't access getString()
     */
    public static String id = "unitType";

    /**
     * First selected unit is the default selected when opening a unit type
     */
    private String firstSelectedUnit = "";

    /**
     * This is meant as a replacement for sample input in all raw units of this unit type, in case
     * individual sample inputs are not needed
     */
    private String unitTypeSampleInput = "";

    /**
     * Add normal unit to this type
     * @param key String of UnitName
     * @param value String of Android String resource
     * @param factor double used for calculation of the MainUnit, from/to this one
     */
    protected void addUnit( String key , String value , String factor , String sampleInput ){
        rawUnitList.put( key , new RawUnit( key , value , factor , sampleInput) );
    }
    protected void addUnit( String key , String value , String factor ){
        rawUnitList.put( key , new RawUnit( key , value , factor , "") );
    }


    /**
     * Add Main Unit to this type (no factor needed)
     * @param key String of UnitName
     * @param value String of Android String resource
     */
    protected void addMainUnit( String key , String value , String sampleInput ){
        addUnit( key , value , "" , sampleInput );
        mainUnitName = key;
    }
    protected void addMainUnit( String key , String value ){
        addUnit( key , value , "" , "" );
        mainUnitName = key;
    }

    /**
     * Returns an array of all entries
     * @return entry array
     */
    public UnitTypeEntry[] entrySetAsArray(){
        List<UnitTypeEntry> list = new ArrayList<>();
        for( RawUnit e : rawUnitList.values()){
            list.add( new UnitTypeEntry( e.getKey() , e.getResourceId() , e.getSampleInput() ) );
        }
        return list.toArray( new UnitTypeEntry[rawUnitList.size()] );
    }

    /**
     * @return the amount of units of this type as int
     */
    public int getSize(){
        return rawUnitList.size();
    }

    /**
     * The MainUnit is the measurement that this UnitType converts into eg. for distance its meters
     * @return String
     */
    public String getMainUnitName() { return mainUnitName; }

    /**
     * UnitFactorList is a list using unitNames to store their factors into the mainUnit
     * @param unitKey Which unit to look up
     * @return factor as double
     */
    public String getUnitFactor( String unitKey ){
        RawUnit rawUnit = rawUnitList.get( unitKey );
        return ( rawUnit != null ) ? rawUnit.getFactor() : "";
    }

    /**
     * Filter units using all available units and the given filter
     * @param filter - set of values to filter out of the array
     * @return array of filtered units
     */
    public static UnitTypeEntry[] filterUnits( Set<String> filter , UnitType unitType){
        // This should be the only time Length.getInstance().entrySet() is called, to save performance
        ArrayList<UnitTypeEntry> filteredList = new ArrayList<>();

        // Iterate Set containing all values, if value is NOT inside filter, add it to filteredList
        for( RawUnit rawUnit : unitType.getRawUnitList() ){
            if ( !filter.contains( rawUnit.getKey() ) ){
                filteredList.add( new UnitTypeEntry( rawUnit.getKey() , rawUnit.getResourceId() , rawUnit.getSampleInput() ) );
            }
        }

        // Turn filteredList into an Array of Entry as its not going to be changed anymore
        return filteredList.toArray(new UnitTypeEntry[0]);
    }

    /**
     * Localized name of this unit type
     * @param ctx Context
     * @return String of localized name
     */
    public String getUnitTypeLocalizedName( Context ctx ){
        return ctx.getResources().getString(
                ctx.getResources().getIdentifier( "unit_type_name_"+getId() , "string", ctx.getPackageName() )
        );
    }

    /**
     * Simple getter to reach ID in a non static way
     * NEEDS TO BE OVERRIDDEN IN EVERY CHILD
     * Otherwise the static value from UnitType will be returned
     * @return String
     */
    public String getId(){ return id; }

    /**
     * First selected unit is the default selected when opening a unit type
     * @return unit key
     */
    public String getFirstSelectedUnit() { return firstSelectedUnit; }
    public void setFirstSelectedUnit(String firstSelectedUnit) { this.firstSelectedUnit = firstSelectedUnit; }

    /**
     * This is meant as a replacement for sample input in all raw units of this unit type, in case
     * individual sample inputs are not needed
     * @return String of example input for this type
     */
    public String getUnitTypeSampleInput() { return unitTypeSampleInput; }
    public void setUnitTypeSampleInput(String unitTypeSampleInput) { this.unitTypeSampleInput = unitTypeSampleInput; }

    public Collection<RawUnit> getRawUnitList() { return rawUnitList.values(); }
}

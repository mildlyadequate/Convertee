package com.sbsc.convertee.entities.unittypes.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
    Parent for all other UnitType classes (such as weight, distance, ...)
 */
@SuppressWarnings("unused")
public class UnitType{

    /**
        Map contains units of a specific type
     */
    private final Map<String, String> unitList = new LinkedHashMap<>();
    /**
        Map contains double values of the units factor into the mainUnit (doesn't contain
        the main unit as there is no need for a factor)
     */
    private final Map<String, String> unitFactorList = new HashMap<>();
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
     * Add normal unit to this type
     * @param key String of UnitName
     * @param value String of Android String resource
     * @param factor double used for calculation of the MainUnit, from/to this one
     */
    protected void addUnit( String key , String value , String factor){
        unitList.put(key,value);
        unitFactorList.put(key,factor);
    }

    /**
     * Add Main Unit to this type (no factor needed)
     * @param key String of UnitName
     * @param value String of Android String resource
     */
    protected void addMainUnit( String key , String value){
        unitList.put(key,value);
        mainUnitName = key;
    }

    /**
     * @return an array of all values of this UnitType
     */
    public String[] values(){
        return unitList.values().toArray(new String[0]);
    }

    /**
     * @return an array of all names of this UnitType
     */
    public String[] names(){
        return unitList.keySet().toArray(new String[0]);
    }

    /**
     *  Returns the entire unitList as set
     * @return entrySet of Strings
     */
    public Set<Entry<String, String>> entrySet(){
        return unitList.entrySet();
    }

    /**
     * Returns an array of all entries
     * @return entry array
     */
    public UnitTypeEntry[] entrySetAsArray(){
        List<UnitTypeEntry> list = new ArrayList<>();
        for( Map.Entry<String,String> e : unitList.entrySet()){
            list.add( new UnitTypeEntry( e.getKey() , e.getValue() ) );
        }
        return list.toArray( new UnitTypeEntry[unitList.size()] );
    }

    /**
     * Get a specific Entry by its name
     * @param name as a String
     * @return Entry containing value and name
     */
    public UnitTypeEntry getEntryByName( String name ) {
        final UnitTypeEntry result = new UnitTypeEntry();
        for( Map.Entry<String,String> e : unitList.entrySet()){
            if(e.getKey().equals(name)) result.setContent(e.getKey(),e.getValue());
        }
        return null;
    }

    /**
     * Get a specific Entry by its value
     * @param value as a String
     * @return Entry containing value and name
     */
    public UnitTypeEntry getEntryByValue( String value ) {
        final UnitTypeEntry result = new UnitTypeEntry();
        for( Map.Entry<String,String> e : unitList.entrySet()){
            if(e.getValue().equals(value)) result.setContent(e.getKey(),e.getValue());
        }
        return null;
    }

    /**
     * @return the amount of units of this type as int
     */
    public int getSize(){
        return unitList.size();
    }

    /**
     * The MainUnit is the measurement that this UnitType converts into eg. for distance its meters
     * @return String
     */
    public String getMainUnitName() { return mainUnitName; }

    /**
     * UnitFactorList is a list using unitNames to store their factors into the mainUnit
     * @param unitName Which unit to look up
     * @return factor as double
     */
    public String getUnitFactor( String unitName ){ return unitFactorList.get( unitName ); }

    /**
     * Filter units using all available units and the given filter
     * @param filter - set of values to filter out of the array
     * @return array of filtered units
     */
    public static UnitTypeEntry[] filterUnits( Set<String> filter , UnitType unitType){
        // This should be the only time Length.getInstance().entrySet() is called, to save performance
        Set<Map.Entry<String, String>> arr = unitType.entrySet();
        ArrayList<UnitTypeEntry> filteredList = new ArrayList<>();

        // Iterate Set containing all values, if value is NOT inside filter, add it to filteredList
        for( Map.Entry<String,String> e : arr){
            if ( !filter.contains( e.getKey() ) ){
                filteredList.add( new UnitTypeEntry( e.getKey(),e.getValue() ) );
            }
        }

        // Turn filteredList into an Array of Entry as its not going to be changed anymore
        return filteredList.toArray(new UnitTypeEntry[0]);
    }

    /**
     * Simple getter to reach ID in a non static way
     * NEEDS TO BE OVERRIDDEN IN EVERY CHILD
     * Otherwise the static value from UnitType will be returned
     * @return String
     */
    public String getId(){ return id; }

}

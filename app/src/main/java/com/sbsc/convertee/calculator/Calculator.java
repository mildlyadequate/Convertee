package com.sbsc.convertee.calculator;

import android.content.Context;

import com.sbsc.convertee.R;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnit;
import com.sbsc.convertee.entities.calc.CalculatedUnitItem;
import com.sbsc.convertee.entities.unittypes.generic.UnitType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Static class to calculate to and from units
 */
public class Calculator {

    /**
        The Locale decides how the number will be formatted using comma, dot, ...
     */
    public static Locale locale = Locale.US;
    /**
        Digits to round to
     */
    public static int roundToDigits = 0;

    /**
     * Private constructor as we don't need any objects of this class
     */
    private Calculator(){}

    /**
     * Set the number formatting locale of the entire app
     * @param localeText String containing locale or none
     * @param context required
     */
    public static void setLocale(String localeText , Context context){
        if(localeText.equalsIgnoreCase(context.getString(R.string.pref_number_locale_none))){
            Calculator.locale = null;
        }else{
            Calculator.locale = Locale.forLanguageTag(localeText);
        }
    }

    /**
     * Calculate a list of the value converted into all OTHER (filtered) units besides the original one
     * @param originalValue String of value
     * @param originalUnitName String, contains the unitName which is also the Key of the unit in UnitType
     * @param filteredUnits List containing units the user wants to see
     * @param type UnitType used as a parameter so it can be determined which UnitType (eg. Weight) is being calculated
     * @return List of calculated items
     */
    public static List<CalculatedUnitItem> getResultList( String originalValue , String originalUnitName , LocalizedUnit[] filteredUnits , UnitType type ){

        List<CalculatedUnitItem> list = new ArrayList<>();
        boolean isSpecial = CalcSpecial.isSpecial( type );

        // Iterate through filteredUnit List
        for(LocalizedUnit unit : filteredUnits){

            // Make sure the unit being looked at right now is NOT the original one
            if( !unit.getUnitKey().equalsIgnoreCase(originalUnitName)){

                list.add( getSingleResult( originalValue , originalUnitName , unit , type , isSpecial ) );

            } // Check main unit
        } // loop
        return list;
    }

    public static CalculatedUnitItem getSingleResult( String originalValue , String originalUnitName , LocalizedUnit targetUnit , UnitType unitType , boolean isSpecial ){

        CalculatedUnitItem calcItem;

        // Check for special units such as temperature
        String result;
        if( !isSpecial ){

            // If not a special unit
            result = CalcDefault.getResultFor(
                    originalValue,
                    originalUnitName,
                    targetUnit.getUnitKey(),
                    unitType
            );

        }else{

            result = CalcSpecial.getResultFor(originalValue, originalUnitName, targetUnit.getUnitKey(), unitType);

        } // isSpecial

        calcItem = new CalculatedUnitItem( result , targetUnit );
        return calcItem;
    }

    public static CalculatedUnitItem getSingleResult( String originalValue , String originalUnitName , LocalizedUnit targetUnit , UnitType unitType ){
        return getSingleResult( originalValue , originalUnitName , targetUnit , unitType , CalcSpecial.isSpecial( unitType ) );
    }

}

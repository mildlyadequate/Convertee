package com.sbsc.convertee.calculator;

import android.util.Log;

import com.sbsc.convertee.entities.unittypes.generic.UnitType;
import com.sbsc.convertee.tools.HelperUtil;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class CalcDefault {

    /**
     * Converts a given value from the originalUnit to the targetUnit and returns it
     * @param originalValue String of value
     * @param originalUnitName String, unitName (which is also the Key of the unit in UnitTyp)
     *                         of the originalUnit
     * @param targetUnitName String, unitName of the unit to convert the value into
     * @param type UnitType used as a parameter so it can be determined which UnitType (eg. Weight) is being calculated
     * @return String of converted value
     */
    public static String getResultFor( String originalValue , String originalUnitName , String targetUnitName , UnitType type ){

        // Check if originalValue is a zero double ('0.0') as we don't need to calculate anything in that case
        if(originalValue.equals("0.0")) return "0";

        String result;

        // If HighPrecision-Mode is active we use BigDecimal from now on
        BigDecimal value;
        value = getValueInMain( originalValue , originalUnitName , type );
        value = getValueInTargetUnit( value , targetUnitName , type );

        // If the locale is null return a plain String of the result, if not format the number
        // according to the Locales Region Format
        if(Calculator.locale == null){
            value = value.setScale( Calculator.roundToDigits , RoundingMode.HALF_UP).stripTrailingZeros();
            result = value.toPlainString();
        }else{
            result = HelperUtil.formatNumberString( value , Calculator.locale , Calculator.roundToDigits );
        }

        return result;
    }

    /**
     * Convert the value into the mainUnit using BigDecimal ( HighPrecision )
     * @param value String of value
     * @param unitName String, unitName of the originalUnit
     * @param type UnitType used as a parameter so it can be determined which UnitType (eg. Weight) is being calculated
     * @return BigDecimal value of originalValue converted into mainUnit
     */
    private static BigDecimal getValueInMain( String value , String unitName , UnitType type ){
        BigDecimal accurateVal = new BigDecimal(value);
        // If unit is mainUnit return value
        if(unitName.equals(type.getMainUnitName())){ return accurateVal; }
        return (
                accurateVal.multiply(
                        new BigDecimal( type.getUnitFactor( unitName ) ),
                        MathContext.DECIMAL128
                )
        );
    }

    /**
     * Now use the normalized value to convert from mainUnit into the targetUnit using BigDecimal ( HighPrecision )
     * @param valueInMain BigDecimal value in mainUnit
     * @param targetUnit String, unitName of the targetUnit
     * @param type UnitType used as a parameter so it can be determined which UnitType (eg. Weight) is being calculated
     * @return BigDecimal value of normalized value converted into targetUnit
     */
    private static BigDecimal getValueInTargetUnit(BigDecimal valueInMain , String targetUnit , UnitType type ){
        // If targetUnit is the mainUnit return normalized value
        if(targetUnit.equals(type.getMainUnitName())) return valueInMain;
        return (
                valueInMain.divide(
                        new BigDecimal( type.getUnitFactor( targetUnit )),
                        MathContext.DECIMAL128
                )
        );
    }

}

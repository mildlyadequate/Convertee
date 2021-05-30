package com.sbsc.convertee.calculator;

import com.sbsc.convertee.entities.unittypes.Angle;
import com.sbsc.convertee.entities.unittypes.BraSize;
import com.sbsc.convertee.entities.unittypes.ColourCode;
import com.sbsc.convertee.entities.unittypes.Currency;
import com.sbsc.convertee.entities.unittypes.Force;
import com.sbsc.convertee.entities.unittypes.Numerative;
import com.sbsc.convertee.entities.unittypes.ShoeSize;
import com.sbsc.convertee.entities.unittypes.Speed;
import com.sbsc.convertee.entities.unittypes.Temperature;
import com.sbsc.convertee.entities.unittypes.Time;
import com.sbsc.convertee.entities.unittypes.generic.UnitType;
import com.sbsc.convertee.tools.HelperUtil;
import com.sbsc.convertee.tools.StringCalculationParser;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class CalcSpecial {

    private static final String[] specialTypes = { Temperature.id , Time.id , Numerative.id , ShoeSize.id , Angle.id , Speed.id , Force.id , Currency.id , ColourCode.id , BraSize.id };

    /**
     * Check if the given unitType is special and requires a unique way of calculating values
     * @param unitType eg. Temperature
     * @return true if special false if not
     */
    public static boolean isSpecial( UnitType unitType ){
        return Arrays.asList(specialTypes).contains(unitType.getId());
    }

    /**
     * Find out if any special calculation classes are needed and use them to return the result
     * else use the default one making use of the CalculationStringParser
     * @param originalValue String
     * @param originalUnitName String
     * @param targetUnitName String
     * @param type UnitType
     * @return String result
     */
    public static String getResultFor( String originalValue , String originalUnitName , String targetUnitName , UnitType type ){

        // Case Numerative:
        if(type.getId().equals(Numerative.id)) return CalcNumerative.getResultFor( originalValue , originalUnitName , targetUnitName );

        // Case ShoeSize:
        if(type.getId().equals(ShoeSize.id)) return CalcShoeSize.getInstance().getResultFor( originalValue , originalUnitName , targetUnitName );

        // Case Currency:
        if(type.getId().equals(Currency.id)) return CalcCurrency.getInstance().getResultFor( originalValue , originalUnitName , targetUnitName );

        // Case ColourCode:
        if(type.getId().equals(ColourCode.id)) return CalcColourCode.getInstance().getResultFor( originalValue , originalUnitName , targetUnitName );

        // Case BraSize:
        if(type.getId().equals(BraSize.id)) return CalcBraSize.getInstance().getResultFor( originalValue , originalUnitName , targetUnitName );

        // Default
        return getResultFromCalculation( originalValue , originalUnitName , targetUnitName , type );
    }

    /**
     * Return a result for given parameters using the StringCalculationParser class
     * @param originalValue String
     * @param originalUnitName String
     * @param targetUnitName String
     * @param type UnitType
     * @return String result
     */
    private static String getResultFromCalculation(String originalValue , String originalUnitName , String targetUnitName , UnitType type ){

        // Check if originalValue is a zero double ('0.0') as we don't need to calculate anything in that case
        if(originalValue.equals("0.0")) return "0";

        String result;
        String calculationFromUnit = getCalculation( originalUnitName , type , true );
        String calculationToUnit = getCalculation( targetUnitName , type , false );

        // If HighPrecision-Mode is active we use BigDecimal from now on
        BigDecimal tempValueBD;
        tempValueBD = getValueInMain( originalValue , originalUnitName , type , calculationFromUnit );
        tempValueBD = getValueInTargetUnit( tempValueBD , targetUnitName , type , calculationToUnit );

        // If the locale is null return a plain String of the result, if not format the number
        // according to the Locales Region Format
        if(Calculator.locale == null){
            tempValueBD = tempValueBD.setScale( Calculator.roundToDigits , RoundingMode.HALF_UP).stripTrailingZeros();
            result = tempValueBD.toPlainString();
        }else{
            result = HelperUtil.formatNumberString( tempValueBD , Calculator.locale , Calculator.roundToDigits );
        }

        return result;
    }

    /**
     * Convert the value into the mainUnit using BigDecimal ( HighPrecision )
     * @param value String of value
     * @param unitName String, unitName of the originalUnit
     * @param type UnitType used as a parameter so it can be determined which UnitType (eg. Weight) is being calculated
     * @param calculation String of calculation
     * @return BigDecimal value of originalValue converted into mainUnit
     */
    private static BigDecimal getValueInMain(String value , String unitName , UnitType type , String calculation ){
        if(unitName.equals(type.getMainUnitName())){ return new BigDecimal(value); }
        calculation = placeValue( calculation , value );
        return StringCalculationParser.evalPrecise(calculation);
    }

    /**
     * Now use the normalized value to convert from mainUnit into the targetUnit using BigDecimal ( HighPrecision )
     * @param valueInMain BigDecimal value in mainUnit
     * @param targetUnit String, unitName of the targetUnit
     * @param type UnitType used as a parameter so it can be determined which UnitType (eg. Weight) is being calculated
     * @param calculation String of calculation
     * @return BigDecimal value of normalized value converted into targetUnit
     */
    private static BigDecimal getValueInTargetUnit(BigDecimal valueInMain , String targetUnit , UnitType type , String calculation){
        if(targetUnit.equals(type.getMainUnitName())) return valueInMain;
        calculation = placeValue( calculation , valueInMain.toPlainString() );
        return StringCalculationParser.evalPrecise(calculation);
    }

    /**
     * Parse the string of the calculation
     * @param unit String key of unit
     * @param type UnitType containing calculation
     * @param isFrom Boolean
     *               true: return the first calculation FROM unit TO MAIN
     *               false: return the second calculation TO unit FROM MAIN
     * @return String either to or from calculation of given unit
     */
    private static String getCalculation( String unit , UnitType type , boolean isFrom ){
        String calculationRaw = type.getUnitFactor( unit );
        if( calculationRaw == null ) return "";
        calculationRaw = calculationRaw.replace("_","");
        String[] calculationArr = calculationRaw.split("FROMTO");
        if( calculationArr.length != 2 ) return "";
        return (isFrom) ? calculationArr[0] : calculationArr[1];
    }

    /**
     * Helper, replace placeholder in calculation with the actual value
     * @param calculation String
     * @param value String
     * @return String calculation with value inserted
     */
    private static String placeValue( String calculation , String value ) {
        return calculation.replace("VAL",value);
    }
}

package com.sbsc.convertee.calculator;
import java.math.BigInteger;

/**
 * Special Calculator Class for the UnitType Numerative
 */
public class CalcNumerative {

    /**
     * Simply returns a result String for given parameters
     * @param originalValue String
     * @param originalUnitName String
     * @param targetUnitName String
     * @return String result
     */
    public static String getResultFor( String originalValue , String originalUnitName , String targetUnitName ){

        // Check if originalValue is a zero double ('0.0') as we don't need to calculate anything in that case
        if( originalValue.equals("0.0") || isNotAllowedInSystem(originalValue, originalUnitName)) return "0";

        // Turn into decimal number
        BigInteger val = toDecimal( originalValue , getSystem( originalUnitName ));

        // Turn into target system and return
        return toTargetSystem( val , getSystem( targetUnitName ) ).toUpperCase();
    }

    /**
     * Returns the integer representation of the given unitName
     * @param unitName String
     * @return int
     */
    private static int getSystem( String unitName ){
        switch ( unitName ){
            case "decimal":
                return 10;
            case "binary":
                return 2;
            case "hex":
                return 16;
            case "octal":
                return 8;
            default:
                return 0;
        }
    }

    /**
     * Turns a value from given system into decimal
     * @param value String
     * @param system int from this into decimal
     * @return BigInteger result value
     */
    private static BigInteger toDecimal( String value , int system ){
        if( system == 10 ) return new BigInteger(value);
        return new BigInteger( value , system );
    }

    /**
     * Turns a value from decimal into system
     * @param value String
     * @param system int from decimal into this
     * @return BigInteger result value
     */
    private static String toTargetSystem( BigInteger value , int system ){
        return value.toString( system );
    }

    /**
     * Regex check if the given string is allowed in the given system
     * @param s String
     * @param unitTypeKey String
     * @return true if allowed / false if not
     */
    public static boolean isNotAllowedInSystem(String s , String unitTypeKey ){
        switch ( unitTypeKey ){
            case "decimal":
                return !s.matches("[0-9]+");
            case "binary":
                return !s.matches("[0-1]+");
            case "hex":
                return !s.matches("[0-9a-fA-F]+");
            case "octal":
                return !s.matches("[0-7]+");
            default:
                return true;
        }
    }

}

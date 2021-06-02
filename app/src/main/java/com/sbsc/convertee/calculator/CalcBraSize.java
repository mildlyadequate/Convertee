package com.sbsc.convertee.calculator;

import com.sbsc.convertee.entities.unittypes.BraSize;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalcBraSize {

    // SINGLETON
    private static CalcBraSize calcBraSize;

    /**
     * Lazy initialization singleton
     * @return instance of UnitType
     */
    public static CalcBraSize getInstance(){
        if (calcBraSize == null){ //if there is no instance available... create new one
            calcBraSize = new CalcBraSize();
        }
        return calcBraSize;
    }

    // Delete singleton instance
    public static void deleteInstance(){
        calcBraSize = null;
    }

    public String getResultFor( String valueString , String originUnit , String targetUnit){

        if( valueString.trim().isEmpty() ) return "...";
        if( !valueString.matches("\\d+[a-zA-Z]+") ) return getWrongInputReturn();

        // Get BandSize from String
        int bandSize = 0;
        Matcher matcherBandSize = Pattern.compile("\\d+").matcher( valueString );
        if( matcherBandSize.find() ){
            bandSize = Integer.parseInt(matcherBandSize.group());
        }

        // Get CupSize from String
        String cupSize = "";
        Matcher matcherCupSize = Pattern.compile("[a-zA-Z]+").matcher( valueString );
        if( matcherCupSize.find() ){
            cupSize = matcherCupSize.group();
        }

        int originUnitId = getUnitId( originUnit );
        int targetUnitId = getUnitId( targetUnit );

        int originBandSizeIndex = getBandSizeOriginalIndex( bandSize , originUnitId );
        int originCupSizeIndex =  getCupSizeOriginalIndex( cupSize , originUnitId );

        if( originBandSizeIndex == 1 || originCupSizeIndex == -1 ||
                bandSizes.length <= targetUnitId || cupSizes.length <= targetUnitId ||
                bandSizes[targetUnitId].length <= originBandSizeIndex ||
                cupSizes[targetUnitId].length <= originCupSizeIndex
        ) return getWrongInputReturn();
        return ""+bandSizes[targetUnitId][originBandSizeIndex]+cupSizes[targetUnitId][originCupSizeIndex];
    }

    private String getWrongInputReturn(){ return "N/A"; }

    private int getUnitId( String unitKey ){
        return Integer.parseInt( BraSize.getInstance().getUnitFactor(unitKey) );
    }

    private int getBandSizeOriginalIndex( int bandSize , int originUnitId ){

        int resultIndex = -1;
        for( int i=0;i<bandSizes[originUnitId].length; i++){
            if( bandSizes[originUnitId][i] == bandSize ) resultIndex = i;
        }
        return resultIndex;
    }

    private int getCupSizeOriginalIndex( String cupSize , int originUnitId ){
        int resultIndex = -1;
        for( int i=0;i<cupSizes[originUnitId].length; i++){
            if( cupSizes[originUnitId][i].equalsIgnoreCase(cupSize) ) resultIndex = i;
        }
        return resultIndex;
    }

    private final int[][] bandSizes = {
            {60,65,70,75,80, 85,90,95,100,105, 110,115,120,125,130, 135,140}, // EU
            {28,30,32,34,36, 38,40,42,44,46, 48,50,52,54,56, 58,60,62,64}, // US
            {28,30,32,34,36, 38,40,42,44,46, 48,50,52,54,56, 58,60,62,64}, // UK
            {75,80,85,90,95, 100,105,110,115,120, 125,130,135,140,145, 150,155}, // FRA, BE , ES
            {  6,8,10,12,14, 16,18,20,22,24, 26,28,30,32,34, 36,38}, // AUS, NZ
    };

    private final String[][] cupSizes = {
            {"AA","A","B","C","D", "E","F","G","H","I", "J","K","L","M","N"}, // EU, FR, SP, BE
            {"AA","A","B","C","D", "DD/E","DDD/F","DDD/G","H","I" }, // US
            {"AA","A","B","C","D", "DD","E","F","FF","G", "GG","H","HH","J","JJ"}, // UK
            {"AA","A","B","C","D", "DD","E","F","FF","G", "GG","H","HH","J","JJ"} // AUS
    };

    private static class Range{
        private final int min;
        private final int max;

        public Range(int min, int max) {
            this.min = min;
            this.max = max;
        }
        public int getMin() { return min; }
        public int getMax() { return max; }
        public boolean isInsideRange( double value ){
            return (min>=value && max <= value);
        }
    }

}

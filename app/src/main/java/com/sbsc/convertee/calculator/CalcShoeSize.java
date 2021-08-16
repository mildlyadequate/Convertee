package com.sbsc.convertee.calculator;

import com.sbsc.convertee.entities.unittypes.ShoeSize;

import org.jetbrains.annotations.NotNull;

/**
 * Helper Class to look up shoe sizes in array
 */
public class CalcShoeSize {

    // Index used in ShoeSize as factor
    private static final int euIndex = 0;
    private static final int ukIndex = 1;
    private static final int mondoIndex = 2;
    private static final int usmIndex = 3;
    private static final int uswIndex = 4;

    // SINGLETON
    private static CalcShoeSize calcShoeSize;

    /**
     * Lazy initialization singleton
     * @return instance of UnitType
     */
    public static CalcShoeSize getInstance(){
        if (calcShoeSize == null){ //if there is no instance available... create new one
            calcShoeSize = new CalcShoeSize();
        }
        return calcShoeSize;
    }

    // Delete singleton instance
    public static void deleteInstance(){
        calcShoeSize = null;
    }

    // A table of shoe sizes converted
    private final ShoeSizeItem[] shoeSizeItems = {
            new ShoeSizeItem(34, 2, 215),
            new ShoeSizeItem(34.5, 2.5, 215),
            new ShoeSizeItem(35, 3, 220),
            new ShoeSizeItem(35.5, 3.5, 225),
            new ShoeSizeItem(36, 4, 225),
            new ShoeSizeItem(36.5, 4, 230),
            new ShoeSizeItem(37, 4.5, 235),
            new ShoeSizeItem(37.5, 5, 235),
            new ShoeSizeItem(38, 5.5, 240),
            new ShoeSizeItem(38.5, 5.5, 245),
            new ShoeSizeItem(39, 6, 245),
            new ShoeSizeItem(39.5, 6.5, 250),
            new ShoeSizeItem(40, 7, 255),
            new ShoeSizeItem(40.5, 7.5, 255),
            new ShoeSizeItem(41, 7.5, 260),
            new ShoeSizeItem(41.5, 8, 265),
            new ShoeSizeItem(42, 8.5, 265),
            new ShoeSizeItem(42.5, 9, 270),
            new ShoeSizeItem(43, 9.5, 275),
            new ShoeSizeItem(43.5, 9.5, 275),
            new ShoeSizeItem(44, 10, 280),
            new ShoeSizeItem(44.5, 10.5, 285),
            new ShoeSizeItem(45, 11, 285),
            new ShoeSizeItem(45.5, 11.5, 290),
            new ShoeSizeItem(46, 11.5, 295),
            new ShoeSizeItem(46.5, 12, 295),
            new ShoeSizeItem(47, 12.5, 300),
            new ShoeSizeItem(47.5, 13, 305),
            new ShoeSizeItem(48, 13, 305),
            new ShoeSizeItem(48.5, 13.5, 310),
            new ShoeSizeItem(49, 14, 315),
            new ShoeSizeItem(49.5, 14.5, 315),
            new ShoeSizeItem(50, 15, 320)
    };

    /**
     * Get shoe size with value, values original size, and target size
     * @param valueString String size value
     * @param originUnit String original size
     * @param targetUnit String target size
     * @return value in target size
     */
    public String getResultFor( String valueString , String originUnit , String targetUnit){

        // Factor contains the index
        int originUnitIndex = Integer.parseInt(ShoeSize.getInstance().getUnitFactor(  originUnit ));
        int targetUnitIndex = Integer.parseInt(ShoeSize.getInstance().getUnitFactor(  targetUnit ));
        double value = Double.parseDouble(valueString);

        // Build a result string
        StringBuilder result = new StringBuilder();
        // Iterate all ShoeSizeItems
        for( ShoeSizeItem entry : shoeSizeItems){

            // If current items value in column of origin unit matches the input value
            if( entry.get( originUnitIndex ) == value ) {

                // Check for duplicate entry
                if( !result.toString().equals( ""+entry.get(targetUnitIndex)) && !result.toString().equals( ""+(int)entry.get(targetUnitIndex)) ){

                    // If more than one size is the result add a dash inbetween
                    if ( result.length()>0 ) result.append(" - ");
                    // Format number first
                    double resultNumber = entry.get(targetUnitIndex);
                    // If number has a .5 just append to result
                    if( resultNumber % 1 != 0 ) {
                        result.append(entry.get(targetUnitIndex));
                    }else{
                        // Else remove trailing .0
                        result.append( (int) entry.get(targetUnitIndex));
                    }

                }
            }
            // If current item value in origin unit is bigger than the value we are looking for
            // stop iterating as there's no possible additional result (sorted array)
            if( entry.get( originUnitIndex ) > value ) break;
        }
        // If no result was found put place holder
        if(result.length()==0) result.append("N/A");
        return result.toString();
    }

    /**
     * Contains EU, UK, Mondo sizes relative to each other and calculates the US sizes from the UK one
     */
    public static class ShoeSizeItem {

        final double eu;
        final double uk;
        final double mondo;

        public ShoeSizeItem(double eu, double uk, double mondo ) {
            this.eu = eu;
            this.uk = uk;
            this.mondo = mondo;
        }

        double get(int type ){
            switch(type){
                case euIndex:
                    return eu;
                case ukIndex:
                    return uk;
                case mondoIndex:
                    return mondo;
                case usmIndex:
                    return getUSM();
                case uswIndex:
                    return getUSW();
            }
            return 0;
        }

        public double getUSM() { return uk+1; }
        public double getUSW() { return uk+2; }

        @NotNull
        @Override
        public String toString() {
            return "ShoeSize{" +
                    "eu=" + eu +
                    ", uk=" + uk +
                    ", mondo=" + mondo +
                    ", usm=" + getUSM() +
                    ", usw=" + getUSW() +
                    '}';
        }
    }

}

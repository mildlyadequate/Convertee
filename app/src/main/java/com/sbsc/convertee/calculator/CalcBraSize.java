package com.sbsc.convertee.calculator;

public class CalcBraSize {

    // Index used in ShoeSize as factor
    private static final int euIndex = 0;
    private static final int ukIndex = 1;
    private static final int mondoIndex = 2;
    private static final int usmIndex = 3;
    private static final int uswIndex = 4;

    // SINGLETON
    private static CalcBraSize calcShoeSize;

    /**
     * Lazy initialization singleton
     * @return instance of UnitType
     */
    public static CalcBraSize getInstance(){
        if (calcShoeSize == null){ //if there is no instance available... create new one
            calcShoeSize = new CalcBraSize();
        }
        return calcShoeSize;
    }

    // Delete singleton instance
    public static void deleteInstance(){
        calcShoeSize = null;
    }

    public String getResultFor( String valueString , String originUnit , String targetUnit){
        return "Kek";
    }

}

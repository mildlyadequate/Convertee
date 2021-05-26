package com.sbsc.convertee.calculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.text.format.Time;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sbsc.convertee.entities.unittypes.Currency;
import com.sbsc.convertee.entities.unittypes.generic.UnitType;
import com.sbsc.convertee.tools.CompatibilityHandler;
import com.sbsc.convertee.tools.CurrencyApiHandler;
import com.sbsc.convertee.tools.HelperUtil;
import com.sbsc.convertee.ui.converter.UnitConverterViewModel;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;

/**
 * Helper Class to look up shoe sizes in array
 */
public class CalcCurrency {

    private SharedPreferences sharedPref;

    private final String baseCurrency = "usdollar";
    private JsonObject currencyRates;

    // SINGLETON
    private static CalcCurrency calcCurrency;

    private CalcCurrency(){ }

    /**
     * Lazy initialization singleton
     * @return instance of UnitType
     */
    public static CalcCurrency getInstance(){
        if (calcCurrency == null){ //if there is no instance available... create new one
            calcCurrency = new CalcCurrency();
        }
        return calcCurrency;
    }

    public void updateCurrency(Context ctx, SharedPreferences sharedPref, UnitConverterViewModel unitConverterViewModel){
        this.sharedPref = sharedPref;

        // Check if last update has been > 1 day ago
        long lastUpdate = sharedPref.getLong("currency_rates_last_update", 0);
        long currentTime = CompatibilityHandler.getCurrentTime();

        currencyRates = new Gson().fromJson( sharedPref.getString("currency_rates","") , JsonObject.class);

        // Difference more than a day ( 86400000 millis )
        if( currentTime - lastUpdate > 86400000L || currencyRates == null ){
            RequestQueue queue = Volley.newRequestQueue(ctx);
            CurrencyApiHandler.getUpdatedCurrencies(queue, sharedPref , unitConverterViewModel );
            currencyRates = new Gson().fromJson( sharedPref.getString("currency_rates","") , JsonObject.class);

            // Check if data is actually available
            if( currencyRates != null && currencyRates.get("USD") != null ){
                sharedPref.edit().putLong("currency_rates_last_update",currentTime).apply();
                unitConverterViewModel.setCurrencyRatesLastUpdated(currentTime);
            }
        }
    }

    // Delete singleton instance
    public static void deleteInstance(){
        calcCurrency = null;
    }

    /**
     * Get shoe size with value, values original size, and target size
     * @param valueString String size value
     * @param originUnit String original size
     * @param targetUnit String target size
     * @return value in target size
     */
    public String getResultFor( String valueString , String originUnit , String targetUnit){
        if( valueString.isEmpty() || valueString.trim().isEmpty() ) return "0";
        if( currencyRates == null || currencyRates.get("USD") == null ) return "NaN";

        BigDecimal result;
        result = getValueInMain( valueString , originUnit );
        result = getValueInTarget( result , targetUnit );

        if(result==null) return "NaN";

        String resultString;
        // If the locale is null return a plain String of the result, if not format the number
        // according to the Locales Region Format
        if(Calculator.locale == null){
            result = result.setScale( Calculator.roundToDigits , RoundingMode.HALF_UP).stripTrailingZeros();
            resultString = result.toPlainString();
        }else{
            resultString = HelperUtil.formatNumberString( result , Calculator.locale , Calculator.roundToDigits );
        }

       return resultString;
    }

    private BigDecimal getValueInMain( String value , String unitName ){
        if( unitName.equals(baseCurrency )) return new BigDecimal(value);

        // Check if targetUnit is correct, if not return null
        String targetUnitFactor = String.valueOf(currencyRates.get(Currency.getInstance().getUnitFactor(unitName)));
        if( targetUnitFactor.equals("null")) return null;

        BigDecimal accurateVal = new BigDecimal(value);
        return accurateVal.divide(
                new BigDecimal( targetUnitFactor ),
                MathContext.DECIMAL128
        );
    }

    private BigDecimal getValueInTarget( BigDecimal value , String targetUnit ){
        if( targetUnit.equals(baseCurrency )) return value;

        // Check if targetUnit is correct, if not return null
        String targetUnitFactor = String.valueOf(currencyRates.get(Currency.getInstance().getUnitFactor(targetUnit)));
        if( targetUnitFactor.equals("null")) return null;

        return value.multiply(
                new BigDecimal( targetUnitFactor ),
                MathContext.DECIMAL128
        );
    }

}

package com.sbsc.convertee.tools;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class HelperUtil {

    /**
     * Round double to set amount of decimals
     * @param value double to round
     * @param places int allowed trailing digits
     * @return double rounded value
     */
    public static Double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP).stripTrailingZeros();
        return bd.doubleValue();
    }

    /**
     * Rounds digits off a number string and formats it using the given locale
     * @param number String
     * @param locale Localization eg. UK
     * @param trailingDigits Int number of decimal digits
     * @return formatted String
     */
    public static String formatNumberString( double number , Locale locale , int trailingDigits ){
        return getNumberStringFormatter( locale, trailingDigits ).format(number);
    }
    public static String formatNumberString( BigDecimal number , Locale locale , int trailingDigits ){
        return getNumberStringFormatter( locale, trailingDigits ).format(number);
    }
    private static DecimalFormat getNumberStringFormatter( Locale locale , int trailingDigits ){
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance( locale );
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        formatter.setMaximumFractionDigits( trailingDigits );
        return formatter;
    }

    /**
     * Hide the keyboard if shown
     * @param activity root activity needed
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) view = new View(activity);
        if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * For use with Android Bundles, checks if Bundle has a specific String. If it exists return it,
     * if not return the default value
     * @param b - Bundle
     * @param key - String
     * @param defaultValue - String
     * @return a String value
     */
    public static String getBundleString(Bundle b, String key, String defaultValue) {
        String value = b.getString(key);
        if (value == null)
            value = defaultValue;
        return value;
    }

    /**
     * Needed to get Strings from android using custom names during runtime
     * @param name - String name
     * @return Corresponding String resource
     */
    public static String getStringResourceByName( String name , Activity activity ) {
        String packageName = activity.getPackageName();
        int resId = activity.getResources().getIdentifier(name, "string", packageName);
        return activity.getString(resId);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static boolean isParsableInt( String text ){
        try{
            if( NumberUtils.isParsable( text ) ){
                int parsedVal = Integer.parseInt( text );
                return true;
            }else{
                return false;
            }
        }catch( NumberFormatException e ){
            return false;
        }
    }
}

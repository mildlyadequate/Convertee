package com.sbsc.convertee.tools;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.icu.util.Calendar;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class CompatibilityHandler {

    public static Locale defaultLocale;

    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    public static void setLocaleDefault( Activity activity ) {
        if( defaultLocale == null ) return;
        Locale.setDefault(defaultLocale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(defaultLocale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    public static boolean shouldUseCustomKeyboard(){
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    public static int getColor(Context ctx , @ColorRes int id){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.getColor( ctx , id );
        } else {
            return ctx.getResources().getColor( id );
        }
    }

    public static String convertNumberFormatDEtoSystem( String input ){
        input = input.replaceAll("\\.","");
        return input.replace(",",".");
    }

    @SuppressWarnings("deprecation")
    public static long getCurrentTime(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Calendar.getInstance().getTimeInMillis();

        }else{
            android.text.format.Time time = new android.text.format.Time();
            time.setToNow();
            return time.toMillis(false);
        }
    }

    public static boolean isOldDevice(){
        return !(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N);
    }

    public static void setTextViewAppearance( TextView textView , Context context , int resId ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            textView.setTextAppearance( context , resId );
        } else {
            textView.setTextAppearance( resId );
        }
    }

    /**
     * Check if a lists strings contain the queried string; Doesn't need Java8
     * @param list List of Strings
     * @param query String
     * @return true if List contains a string that contains the query
     */
    public static boolean containsIgnoreCase( List<String> list, String query ) {
        for (String current : list) {
            if ( StringUtils.containsIgnoreCase( current , query ) ) {
                return true;
            }
        }
        return false;
    }

}

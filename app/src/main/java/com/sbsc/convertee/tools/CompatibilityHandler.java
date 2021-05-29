package com.sbsc.convertee.tools;

import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.text.format.Time;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import java.util.Objects;
import java.util.function.Consumer;

public class CompatibilityHandler {

    public static int getColor(Context ctx , @ColorRes int id){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.getColor( ctx , id );
        } else {
            return ctx.getResources().getColor( id );
        }
    }

    @SuppressWarnings("deprecation")
    public static long getCurrentTime(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Calendar.getInstance().getTimeInMillis();

        }else{
            Time time = new Time();
            time.setToNow();
            return time.toMillis(false);
        }
    }

    public static boolean isOldDevice(){
        return !(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N);
    }

    public static String formatDateTime( long millis ){
        return null;
    }


}

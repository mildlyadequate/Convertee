package com.sbsc.convertee.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.sbsc.convertee.R;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DefaultHiddenUnits {

    public static void setDefaultHiddenUnits( Context ctx , SharedPreferences pref ){
        setDefaultHiddenVolume( ctx , pref );
    }

    private static void setDefaultHiddenVolume(Context ctx , SharedPreferences pref ){
        final Set<String> values = new HashSet<>(Arrays.asList( "gallonus", "quartus" , "pintus" , "cupus" ));
        pref.edit().putStringSet( ctx.getString(R.string.preference_volume_hidden),values ).apply();
    }

}
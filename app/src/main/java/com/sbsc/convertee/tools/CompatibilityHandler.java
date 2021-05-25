package com.sbsc.convertee.tools;

import android.content.Context;
import android.os.Build;

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

}

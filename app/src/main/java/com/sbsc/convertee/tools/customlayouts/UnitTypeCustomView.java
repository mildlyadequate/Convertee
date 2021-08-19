package com.sbsc.convertee.tools.customlayouts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UnitTypeCustomView extends LinearLayout {

    private boolean forcePressed = false;

    public UnitTypeCustomView(@NonNull Context context) {
        super(context);
    }

    public UnitTypeCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UnitTypeCustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public UnitTypeCustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setPressed(boolean pressed) {
        if( forcePressed ) pressed = true;
        super.setPressed(pressed);
    }

    public void setForcePressed(boolean forcePressed){
        this.forcePressed = forcePressed;
        setPressed(forcePressed);
    }
}

package com.sbsc.convertee.tools.customlayouts;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class UnitTypeCustomView extends ConstraintLayout {

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

package com.sbsc.convertee.tools.customlayouts;


import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class ClickableCustomTextView extends AppCompatTextView {

    private boolean forcePressed = false;

    public ClickableCustomTextView(@NonNull Context context) {
        super(context);
    }

    public ClickableCustomTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickableCustomTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

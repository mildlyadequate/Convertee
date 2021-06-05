package com.sbsc.convertee.tools.keyboards;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.InputConnection;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.sbsc.convertee.R;

import java.util.ArrayList;
import java.util.List;

public class CustomKeyboard extends LinearLayout {

    protected InputConnection inputConnection;
    private final List<CustomKeyboardCloseListener> eventCloseListeners;

    public CustomKeyboard(Context context) {
        this(context, null, 0);
    }

    public CustomKeyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        eventCloseListeners = new ArrayList<>();
    }

    public void setInputConnection(InputConnection ic) {
        this.inputConnection = ic;
    }

    public void addKeyboardClosingListener(CustomKeyboardCloseListener evtListener ) {
        this.eventCloseListeners.add(evtListener);
    }

    public void handleKeyboardButtonClose(){
        ImageButton btnClose = findViewById(R.id.button_close);
        LinearLayout llKeyboard = findViewById(R.id.llKeyboard);
        btnClose.setOnClickListener(view -> {
            llKeyboard.setVisibility(GONE);
            for( CustomKeyboardCloseListener listener : eventCloseListeners ) listener.onKeyboardButtonClose();
        });
    }
}

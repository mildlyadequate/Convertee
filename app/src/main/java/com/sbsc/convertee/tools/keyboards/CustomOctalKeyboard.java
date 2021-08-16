package com.sbsc.convertee.tools.keyboards;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.ExtractedTextRequest;
import android.widget.Button;
import android.widget.ImageButton;

import com.sbsc.convertee.R;

public class CustomOctalKeyboard extends CustomKeyboard implements View.OnClickListener, View.OnLongClickListener {

    // constructors
    public CustomOctalKeyboard(Context context) {
        this(context, null, 0);
    }

    public CustomOctalKeyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomOctalKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init( context );
        handleKeyboardButtonClose();
    }

    // This will map the button resource id to the String value that we want to
    // input when that button is clicked.
    SparseArray<String> keyValues = new SparseArray<>();

    private void init( Context context ) {

        // initialize buttons
        LayoutInflater.from(context).inflate(R.layout.keyboard_octal, this, true);
        // keyboard keys (buttons)
        Button mButton1 = findViewById(R.id.button_1);
        Button mButton2 = findViewById(R.id.button_2);
        Button mButton3 = findViewById(R.id.button_3);
        Button mButton4 = findViewById(R.id.button_4);
        Button mButton5 = findViewById(R.id.button_5);
        Button mButton6 = findViewById(R.id.button_6);
        Button mButton7 = findViewById(R.id.button_7);
        Button mButton0 = findViewById(R.id.button_0);
        ImageButton mButtonDelete = findViewById(R.id.button_delete);

        // set button click listeners
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        mButton5.setOnClickListener(this);
        mButton6.setOnClickListener(this);
        mButton7.setOnClickListener(this);
        mButton0.setOnClickListener(this);
        mButtonDelete.setOnClickListener(this);

        // Long clicks
        mButtonDelete.setOnLongClickListener(this);

        // map buttons IDs to input strings
        keyValues.put(R.id.button_1, "1");
        keyValues.put(R.id.button_2, "2");
        keyValues.put(R.id.button_3, "3");
        keyValues.put(R.id.button_4, "4");
        keyValues.put(R.id.button_5, "5");
        keyValues.put(R.id.button_6, "6");
        keyValues.put(R.id.button_7, "7");
        keyValues.put(R.id.button_8, "8");
        keyValues.put(R.id.button_9, "9");
        keyValues.put(R.id.button_0, "0");

    }

    @Override
    public void onClick(View v) {

        // do nothing if the InputConnection has not been set yet
        if (inputConnection == null) return;

        // Delete text or input key value
        // All communication goes through the InputConnection
        if (v.getId() == R.id.button_delete) {
            CharSequence selectedText = inputConnection.getSelectedText(0);
            if (TextUtils.isEmpty(selectedText)) {
                // no selection, so delete previous character
                inputConnection.deleteSurroundingText(1, 0);
            } else {
                // delete the selection
                inputConnection.commitText("", 1);
            }
        } else {
            String value = keyValues.get(v.getId());
            inputConnection.commitText(value, 1);
        }
    }

    @Override
    public boolean onLongClick(View v) {

        if (v.getId() == R.id.button_delete) {
            CharSequence currentText = inputConnection.getExtractedText(new ExtractedTextRequest(), 0).text;
            CharSequence beforeCursorText = inputConnection.getTextBeforeCursor(currentText.length(), 0);
            CharSequence afterCursorText = inputConnection.getTextAfterCursor(currentText.length(), 0);
            inputConnection.deleteSurroundingText(beforeCursorText.length(), afterCursorText.length());
        }

        return false;
    }

}
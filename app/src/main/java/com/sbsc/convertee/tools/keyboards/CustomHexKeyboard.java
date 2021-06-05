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
import com.sbsc.convertee.calculator.Calculator;

import java.util.Locale;

public class CustomHexKeyboard extends CustomKeyboard implements View.OnClickListener, View.OnLongClickListener {

    // constructors
    public CustomHexKeyboard(Context context) {
        this(context, null, 0);
    }

    public CustomHexKeyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomHexKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        handleKeyboardButtonClose();
    }

    // keyboard keys (buttons)
    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;
    private Button mButton5;
    private Button mButton6;
    private Button mButton7;
    private Button mButton8;
    private Button mButton9;
    private Button mButton10;
    private Button mButton11;
    private Button mButton12;
    private Button mButton13;
    private Button mButton14;
    private Button mButton15;
    private Button mButton0;
    private ImageButton mButtonDelete;

    // This will map the button resource id to the String value that we want to
    // input when that button is clicked.
    SparseArray<String> keyValues = new SparseArray<>();

    private void init(Context context, AttributeSet attrs) {

        // initialize buttons
        LayoutInflater.from(context).inflate(R.layout.keyboard_hex, this, true);
        mButton1 = (Button) findViewById(R.id.button_1);
        mButton2 = (Button) findViewById(R.id.button_2);
        mButton3 = (Button) findViewById(R.id.button_3);
        mButton4 = (Button) findViewById(R.id.button_4);
        mButton5 = (Button) findViewById(R.id.button_5);
        mButton6 = (Button) findViewById(R.id.button_6);
        mButton7 = (Button) findViewById(R.id.button_7);
        mButton8 = (Button) findViewById(R.id.button_8);
        mButton9 = (Button) findViewById(R.id.button_9);
        mButton10 = (Button) findViewById(R.id.button_10);
        mButton11 = (Button) findViewById(R.id.button_11);
        mButton12 = (Button) findViewById(R.id.button_12);
        mButton13 = (Button) findViewById(R.id.button_13);
        mButton14 = (Button) findViewById(R.id.button_14);
        mButton15 = (Button) findViewById(R.id.button_15);
        mButton0 = (Button) findViewById(R.id.button_0);
        mButtonDelete = (ImageButton) findViewById(R.id.button_delete);

        // set button click listeners
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        mButton5.setOnClickListener(this);
        mButton6.setOnClickListener(this);
        mButton7.setOnClickListener(this);
        mButton8.setOnClickListener(this);
        mButton9.setOnClickListener(this);
        mButton10.setOnClickListener(this);
        mButton11.setOnClickListener(this);
        mButton12.setOnClickListener(this);
        mButton13.setOnClickListener(this);
        mButton14.setOnClickListener(this);
        mButton15.setOnClickListener(this);
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
        keyValues.put(R.id.button_10, "A");
        keyValues.put(R.id.button_11, "B");
        keyValues.put(R.id.button_12, "C");
        keyValues.put(R.id.button_13, "D");
        keyValues.put(R.id.button_14, "E");
        keyValues.put(R.id.button_15, "F");
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
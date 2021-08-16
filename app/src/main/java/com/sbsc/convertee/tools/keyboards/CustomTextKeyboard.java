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

public class CustomTextKeyboard extends CustomKeyboard implements View.OnClickListener, View.OnLongClickListener {

    // constructors
    public CustomTextKeyboard(Context context) {
        this(context, null, 0);
    }

    public CustomTextKeyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init( context );
        handleKeyboardButtonClose();
    }

    // This will map the button resource id to the String value that we want to
    // input when that button is clicked.
    SparseArray<String> keyValues = new SparseArray<>();

    private void init( Context context ) {

        // initialize buttons
        LayoutInflater.from(context).inflate(R.layout.keyboard_text, this, true);
        // keyboard keys (buttons)
        Button mButton1 = findViewById(R.id.button_1);
        Button mButton2 = findViewById(R.id.button_2);
        Button mButton3 = findViewById(R.id.button_3);
        Button mButton4 = findViewById(R.id.button_4);
        Button mButton5 = findViewById(R.id.button_5);
        Button mButton6 = findViewById(R.id.button_6);
        Button mButton7 = findViewById(R.id.button_7);
        Button mButton8 = findViewById(R.id.button_8);
        Button mButton9 = findViewById(R.id.button_9);
        Button mButton0 = findViewById(R.id.button_0);

        Button mButtonQ = findViewById(R.id.button_q);
        Button mButtonW = findViewById(R.id.button_w);
        Button mButtonE = findViewById(R.id.button_e);
        Button mButtonR = findViewById(R.id.button_r);
        Button mButtonT = findViewById(R.id.button_t);
        Button mButtonZ = findViewById(R.id.button_z);
        Button mButtonU = findViewById(R.id.button_u);
        Button mButtonI = findViewById(R.id.button_i);
        Button mButtonO = findViewById(R.id.button_o);
        Button mButtonP = findViewById(R.id.button_p);

        Button mButtonA = findViewById(R.id.button_a);
        Button mButtonS = findViewById(R.id.button_s);
        Button mButtonD = findViewById(R.id.button_d);
        Button mButtonF = findViewById(R.id.button_f);
        Button mButtonG = findViewById(R.id.button_g);
        Button mButtonH = findViewById(R.id.button_h);
        Button mButtonJ = findViewById(R.id.button_j);
        Button mButtonK = findViewById(R.id.button_k);
        Button mButtonL = findViewById(R.id.button_l);

        Button mButtonY = findViewById(R.id.button_y);
        Button mButtonX = findViewById(R.id.button_x);
        Button mButtonC = findViewById(R.id.button_c);
        Button mButtonV = findViewById(R.id.button_v);
        Button mButtonB = findViewById(R.id.button_b);
        Button mButtonN = findViewById(R.id.button_n);
        Button mButtonM = findViewById(R.id.button_m);

        ImageButton mButtonDelete = findViewById(R.id.button_delete);
        Button mButtonComma = findViewById(R.id.button_comma);
        Button mButtonPeriod = findViewById(R.id.button_period);
        Button mButtonMinus = findViewById(R.id.button_minus);
        Button mButtonSpace = findViewById(R.id.button_space);

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
        mButton0.setOnClickListener(this);
        mButtonQ.setOnClickListener(this);
        mButtonW.setOnClickListener(this);
        mButtonE.setOnClickListener(this);
        mButtonR.setOnClickListener(this);
        mButtonT.setOnClickListener(this);
        mButtonZ.setOnClickListener(this);
        mButtonU.setOnClickListener(this);
        mButtonI.setOnClickListener(this);
        mButtonO.setOnClickListener(this);
        mButtonP.setOnClickListener(this);
        mButtonA.setOnClickListener(this);
        mButtonS.setOnClickListener(this);
        mButtonD.setOnClickListener(this);
        mButtonF.setOnClickListener(this);
        mButtonG.setOnClickListener(this);
        mButtonH.setOnClickListener(this);
        mButtonJ.setOnClickListener(this);
        mButtonK.setOnClickListener(this);
        mButtonL.setOnClickListener(this);
        mButtonY.setOnClickListener(this);
        mButtonX.setOnClickListener(this);
        mButtonC.setOnClickListener(this);
        mButtonV.setOnClickListener(this);
        mButtonB.setOnClickListener(this);
        mButtonN.setOnClickListener(this);
        mButtonM.setOnClickListener(this);

        mButtonDelete.setOnClickListener(this);
        mButtonComma.setOnClickListener(this);
        mButtonPeriod.setOnClickListener(this);
        mButtonMinus.setOnClickListener(this);
        mButtonSpace.setOnClickListener(this);

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

        keyValues.put(R.id.button_q, "Q");
        keyValues.put(R.id.button_w, "W");
        keyValues.put(R.id.button_e, "E");
        keyValues.put(R.id.button_r, "R");
        keyValues.put(R.id.button_t, "T");
        keyValues.put(R.id.button_z, "Z");
        keyValues.put(R.id.button_u, "U");
        keyValues.put(R.id.button_i, "I");
        keyValues.put(R.id.button_o, "O");
        keyValues.put(R.id.button_p, "P");

        keyValues.put(R.id.button_a, "A");
        keyValues.put(R.id.button_s, "S");
        keyValues.put(R.id.button_d, "D");
        keyValues.put(R.id.button_f, "F");
        keyValues.put(R.id.button_g, "G");
        keyValues.put(R.id.button_h, "H");
        keyValues.put(R.id.button_j, "J");
        keyValues.put(R.id.button_k, "K");
        keyValues.put(R.id.button_l, "L");

        keyValues.put(R.id.button_y, "Y");
        keyValues.put(R.id.button_x, "X");
        keyValues.put(R.id.button_c, "C");
        keyValues.put(R.id.button_v, "V");
        keyValues.put(R.id.button_b, "B");
        keyValues.put(R.id.button_n, "N");
        keyValues.put(R.id.button_m, "M");

        keyValues.put(R.id.button_comma, ",");
        keyValues.put(R.id.button_period, ".");
        keyValues.put(R.id.button_minus, "-");
        keyValues.put(R.id.button_space, " ");

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
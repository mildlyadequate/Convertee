package com.sbsc.convertee.tools.keyboards;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputConnection;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.sbsc.convertee.entities.unittypes.BraSize;
import com.sbsc.convertee.entities.unittypes.ColourCode;
import com.sbsc.convertee.entities.unittypes.Numerative;

public class KeyboardHandler {

    private enum KeyboardType { NUMBER , NUMBER_BINARY , NUMBER_HEX , NUMBER_OCTAL , TEXT }

    public static int KeyboardId = 0;

    public static CustomKeyboard getKeyboardByType( String unitTypeKey , String unitKey , InputConnection inputConnection , Context context ) {

        CustomKeyboard customKeyboard = null;

        if( unitTypeKey.equals(Numerative.id) ){

            switch (unitKey) {
                case "decimal":
                    customKeyboard = getKeyboardByType(KeyboardType.NUMBER, inputConnection, context);
                    break;
                case "hex":
                    customKeyboard = getKeyboardByType(KeyboardType.NUMBER_HEX, inputConnection, context);
                    break;
                case "octal":
                    customKeyboard = getKeyboardByType(KeyboardType.NUMBER_OCTAL, inputConnection, context);
                    break;
                case "binary":
                    customKeyboard = getKeyboardByType(KeyboardType.NUMBER_BINARY, inputConnection, context);
                    break;
            }


        }else if ( unitTypeKey.equals(ColourCode.id) ||
                   unitTypeKey.equals(BraSize.id) ){

            customKeyboard = getKeyboardByType( KeyboardType.TEXT , inputConnection , context );
        }else{
            customKeyboard = getKeyboardByType( KeyboardType.NUMBER , inputConnection , context );
        }

        CustomKeyboard.isOpen = true;

        return customKeyboard;
    }

    private static CustomKeyboard getKeyboardByType( KeyboardType type , InputConnection inputConnection , Context context ){

        CustomKeyboard keyboard;

        switch (type){
            case NUMBER:
                keyboard = new CustomNumberKeyboard( context );
                break;
            case NUMBER_HEX:
                keyboard = new CustomHexKeyboard( context );
                break;
            case NUMBER_BINARY:
                keyboard = new CustomBinaryKeyboard( context );
                break;
            case NUMBER_OCTAL:
                keyboard = new CustomOctalKeyboard( context );
                break;
            case TEXT:
                keyboard = new CustomTextKeyboard( context );
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }

        keyboard.setId( View.generateViewId() );
        keyboard.setLayoutParams( new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT )
        );

        keyboard.setInputConnection(inputConnection);
        KeyboardId = keyboard.getId();

        return keyboard;
    }

}

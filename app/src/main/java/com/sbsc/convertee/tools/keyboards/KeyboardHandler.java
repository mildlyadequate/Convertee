package com.sbsc.convertee.tools.keyboards;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputConnection;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.sbsc.convertee.entities.unittypes.BraSize;
import com.sbsc.convertee.entities.unittypes.ColourCode;
import com.sbsc.convertee.entities.unittypes.Numerative;
import com.sbsc.convertee.entities.unittypes.ShoeSize;
import com.sbsc.convertee.entities.unittypes.generic.UnitType;

public class KeyboardHandler {

    private enum KeyboardType { NUMBER , NUMBER_BINARY , NUMBER_HEX , NUMBER_OCTAL , TEXT };


    public static CustomKeyboard getKeyboardByType( UnitType unitType , String unitKey , InputConnection inputConnection , Context context ) {

        if( unitType.getId().equals(Numerative.id) ){

            if( unitKey.equals("decimal") ){
                return getKeyboardByType( KeyboardType.NUMBER , inputConnection , context );
            }else if( unitKey.equals("hex") ){
                return getKeyboardByType( KeyboardType.NUMBER_HEX , inputConnection , context );
            }else if( unitKey.equals("octal") ){
                return getKeyboardByType( KeyboardType.NUMBER_OCTAL , inputConnection , context );
            }else if( unitKey.equals("binary") ){
                return getKeyboardByType( KeyboardType.NUMBER_BINARY , inputConnection , context );
            }

        }else if (
                unitType.getId().equals(ColourCode.id) ||
                unitType.getId().equals(BraSize.id)
        ){
            return getKeyboardByType( KeyboardType.TEXT , inputConnection , context );
        }else{
            return getKeyboardByType( KeyboardType.NUMBER , inputConnection , context );
        }

        return null;
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

        keyboard.setId(View.generateViewId());
        keyboard.setLayoutParams( new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT )
        );

        keyboard.setInputConnection(inputConnection);

        return keyboard;
    }

}

package com.sbsc.convertee.tools;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.sbsc.convertee.R;

import org.jetbrains.annotations.NotNull;

public class HighlightArrayAdapter extends ArrayAdapter<Object> {

    private int mSelectedIndex = -1;

    public void setSelection(int position) {
        mSelectedIndex =  position;
        notifyDataSetChanged();
    }

    public HighlightArrayAdapter(Context context, int resource, Object[] objects) {
        super(context, resource, objects);
    }


    @Override
    public View getDropDownView(int position, View convertView, @NotNull ViewGroup parent) {
        View itemView =  super.getDropDownView(position, convertView, parent);

        if (position == mSelectedIndex) {
            itemView.setBackgroundColor(CompatibilityHandler.getColor( getContext() , R.color.themeDaySoftPrimary ));
        } else {
            itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        return itemView;
    }

}
package com.sbsc.convertee.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.sbsc.convertee.R;
import com.sbsc.convertee.entities.calc.CalculatedUnitItem;
import com.sbsc.convertee.calculator.Calculator;
import com.sbsc.convertee.tools.CompatibilityHandler;
import com.sbsc.convertee.tools.customlayouts.ClickableCustomTextView;
import com.sbsc.convertee.ui.converter.UnitConverterFragment;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  Adapter Class for a RecyclerView of CalculatedUnitItems
 */
public class CalculatedUnitItemAdapter extends RecyclerView.Adapter<CalculatedUnitItemAdapter.ViewHolder>{

    /**
     *  List containing all items to be displayed in this RecyclerView
     */
    private final List<CalculatedUnitItem> calculatedUnitItems;
    /**
     *  Tracks the currently selected position, which by default is no position (-1)
     */
    private int selectedPos = RecyclerView.NO_POSITION;
    /**
     * Tracks whether ProMode is active, relevant for drawing TextViews later
     */
    private boolean proModeActive = false;

    /**
     * Keep fragment here to callback onClick
     */
    private UnitConverterFragment fragment;

    /**
     * Create new RecyclerView adapter for CalculatedUnitItems
     * @param calculatedUnitItems list to fill RecyclerView with
     */
    public CalculatedUnitItemAdapter(List<CalculatedUnitItem> calculatedUnitItems) {
        this.calculatedUnitItems = new ArrayList<>();
        setDistanceItems(calculatedUnitItems, false);
    }

    /**
     * Replaces current list inside this Adapter with new one, sets ProMode, applys changes
     * @param calculatedUnitItems new List to replace current
     * @param proModeActive boolean
     */
    public void setDistanceItems(List<CalculatedUnitItem> calculatedUnitItems, boolean proModeActive) {

        // Reverse list as we are drawing from bottom to top which would result in a wrong order
        Collections.reverse(calculatedUnitItems);

        // Set ProMode
        this.proModeActive = proModeActive;
        // Remove selection
        this.selectedPos = RecyclerView.NO_POSITION;

        // Clear previous list and add new
        this.calculatedUnitItems.clear();
        this.calculatedUnitItems.addAll(calculatedUnitItems);

        this.notifyDataSetChanged();
    }

    //TODO ORDER
    public void orderByAlphabet(boolean desc){
        //calculatedUnitItems.sort((CalculatedUnitItem s1, CalculatedUnitItem s2)->s1.getLocalizedUnitName().compareTo(s2.getLocalizedUnitName()));
        if(desc) Collections.reverse(calculatedUnitItems);
        this.notifyDataSetChanged();
    }

    //TODO ORDER
    public void orderByDefault(boolean byMetric){
        calculatedUnitItems.clear();
        // calculatedUnitItems.addAll(defaultOrderCalculatedUnitItems);
        if(!byMetric) Collections.reverse(calculatedUnitItems);
        this.notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_converted_unit_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Context context = viewHolder.itemView.getContext();

        // Get element from your data set at this position and replace the
        // contents of the view with that element
        CalculatedUnitItem item = calculatedUnitItems.get(position);
        viewHolder.setUnitName(item.getUnitKey());

        // Only draw this TextView containing the full name if ProMode is not active
        if(!proModeActive){
            viewHolder.getTextViewUnitName().setText(item.getLocalizedUnitName());
        }else{
            viewHolder.getTextViewUnitName().setText("");
            viewHolder.getTextViewUnitName().setVisibility(View.GONE);
        }

        ClickableCustomTextView textUnitShort = (ClickableCustomTextView) viewHolder.getTextViewUnitShort();

        // Draw everything else
        viewHolder.getTextViewUnitValue().setText(item.getValue());
        textUnitShort.setText(item.getUnitShort());


        // User wants to highlight current row
        if( selectedPos == position ){
            // viewHolder.getTextViewUnitValue().setTextColor( CompatibilityHandler.getColor( context , R.color.themeBackground) );
            viewHolder.getTextViewUnitValue().setTextAppearance( context , R.style.Theme_Convertee_TextAppearance_UnitValue_Selected );
            viewHolder.getTextViewUnitName().setTextAppearance( context , R.style.Theme_Convertee_TextAppearance_UnitNameSmall_Selected );
            // Using a custom class we can make sure the view stays pressed (doesn't lose its 'pressed' state / styling)
            textUnitShort.setForcePressed(true);
        }else{
            viewHolder.getTextViewUnitValue().setTextAppearance( context , R.style.Theme_Convertee_TextAppearance_UnitValue );
            viewHolder.getTextViewUnitName().setTextAppearance( context , R.style.Theme_Convertee_TextAppearance_UnitNameSmall );
            textUnitShort.setForcePressed(false);
        }
        viewHolder.itemView.setBackgroundColor(selectedPos == position ? CompatibilityHandler.getColor( context , R.color.themePrimary ) : Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() { return calculatedUnitItems.size(); }

    public void setProModeActive(boolean proModeActive) {
        this.proModeActive = proModeActive;
    }

    public void setFragment(UnitConverterFragment fragment) { this.fragment = fragment; }

    /**
     * ViewHolder represents one single row inside the RecyclerView
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        // Keep all views from inside the rows layout
        private final TextView textUnitValue;
        private final TextView textUnitName;
        private final TextView btnUnitShort;

        private String unitName;

        public ViewHolder(View view) {
            super(view);

            // Get all views from row layout
            textUnitValue = view.findViewById(R.id.textDistanceItemValue);
            textUnitName = view.findViewById(R.id.textDistanceItemUnit);
            btnUnitShort = view.findViewById(R.id.textDistanceItemUnitShort);
            LinearLayout llValueContainer = view.findViewById(R.id.llDistanceValueContainer);

            // Click listener for marking a row
            btnUnitShort.setOnClickListener(this);

            // Long Click listener only on specific parts for copying the value
            llValueContainer.setOnLongClickListener(this);
            btnUnitShort.setOnLongClickListener(this);
        }

        public TextView getTextViewUnitValue() { return textUnitValue; }

        public TextView getTextViewUnitName() { return textUnitName; }

        public TextView getTextViewUnitShort(){ return btnUnitShort; }

        public void setUnitName(String unitName) { this.unitName = unitName; }

        @Override
        public void onClick(View v) {

            // Move on if this row is not selected
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

            // Update selected position
            if(selectedPos == getAdapterPosition()){
                notifyItemChanged(selectedPos);
                selectedPos = RecyclerView.NO_POSITION;
            }else{
                notifyItemChanged(selectedPos);
                selectedPos = getAdapterPosition();
                notifyItemChanged(selectedPos);
            }
        }

        @Override
        public boolean onLongClick(View v) {

            // Listen for long click on the unit short to switch active main unit
            if(v.getId() == R.id.textDistanceItemUnitShort) {
                fragment.selectUnitByName( unitName );
                return true;
            }

            // Implement long click to copy value
            String value = ((TextView) v.findViewById(R.id.textDistanceItemValue)).getText().toString();
            String unit = ((TextView) v.findViewById(R.id.textDistanceItemUnit)).getText().toString();
            Context ctx = v.getContext();

            // Remove number localisation from string so user gets plain number copied into clipboard
            if (Calculator.locale != null) {
                NumberFormat nf = NumberFormat.getInstance(Calculator.locale);
                nf.setMaximumFractionDigits(3);
                try {
                    value = nf.parse(value).toString();
                } catch (ParseException e) {
                    e.printStackTrace();
                    Snackbar.make( v ,value+" " +ctx.getString(R.string.adapter_clipboard_toast_error) , Snackbar.LENGTH_SHORT ).show();
                    return false;
                }
            }

            // Put value into clipboard
            ClipboardManager clipboard = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(ctx.getString(R.string.adapter_clipboard_label)+" "+unit,value);
            if (clipboard != null) clipboard.setPrimaryClip(clip);

            // Show message
            Snackbar.make( v ,value+" " +ctx.getString(R.string.adapter_clipboard_toast) , Snackbar.LENGTH_SHORT ).show();
            return true;
        }
    }
}

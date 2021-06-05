package com.sbsc.convertee.ui.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.sbsc.convertee.R;
import com.sbsc.convertee.entities.UnitTypeContainer;
import com.sbsc.convertee.calculator.CalcCurrency;
import com.sbsc.convertee.calculator.Calculator;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnit;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnitType;
import com.sbsc.convertee.entities.adapteritems.QuickConvertUnit;
import com.sbsc.convertee.entities.unittypes.BraSize;
import com.sbsc.convertee.entities.unittypes.ColourCode;
import com.sbsc.convertee.entities.unittypes.Currency;
import com.sbsc.convertee.entities.unittypes.Numerative;
import com.sbsc.convertee.entities.unittypes.generic.UnitType;
import com.sbsc.convertee.tools.CompatibilityHandler;
import com.sbsc.convertee.tools.HighlightArrayAdapter;
import com.sbsc.convertee.ui.quickconverter.QuickConverterFragment;

import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class QuickConvertAdapter extends RecyclerView.Adapter<QuickConvertAdapter.ViewHolder> {

    private final List<QuickConvertUnit> quickConvertUnits;
    private final QuickConverterFragment quickConverterFragment;
    private final Context context;

    public QuickConvertAdapter(List<QuickConvertUnit> quickConvertUnits , QuickConverterFragment quickConverterFragment , Context ctx ){
        this.quickConvertUnits = quickConvertUnits;
        this.quickConverterFragment = quickConverterFragment;
        this.context = ctx;
    }

    public void addItem( QuickConvertUnit quickConvertUnit ){
        this.quickConvertUnits.add( quickConvertUnit );
        notifyItemInserted(quickConvertUnits.size()-1);
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate(R.layout.adapter_quickconvert_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        // Get current unit item
        QuickConvertUnit item = quickConvertUnits.get(position);
        UnitType unitType = UnitTypeContainer.getUnitType( item.getUnitTypeId() );
        Spinner spQuickConvertFrom = holder.getSpQuickConvertFrom();
        Spinner spQuickConvertTo = holder.getSpQuickConvertTo();

        // Title
        String localizedUnitTypeName = unitType.getUnitTypeLocalizedName( context );
        holder.getTvQuickConvertTitle().setText( localizedUnitTypeName );
        holder.getTilQuickConvertInput().setHintAnimationEnabled(false);
        // Edit Text Input Default
        EditText etInput = holder.getEtQuickConvertInput();
        etInput.setText( item.getDefaultValue() );
        holder.getTilQuickConvertInput().setHintAnimationEnabled(true);
        etInput.setShowSoftInputOnFocus(false);
        etInput.setOnFocusChangeListener((view, b) -> {
            if( b ){
                LocalizedUnit currentUnit = (LocalizedUnit) spQuickConvertFrom.getSelectedItem();
                quickConverterFragment.initCustomKeyboard( unitType , currentUnit.getUnitKey() , etInput );
            }
        });
        etInput.setOnClickListener(view -> {
            LocalizedUnit currentUnit = (LocalizedUnit) spQuickConvertFrom.getSelectedItem();
            quickConverterFragment.initCustomKeyboard( unitType , currentUnit.getUnitKey() , etInput );
        });

        if(position % 2 == 0) {
            //holder.rootView.setBackgroundColor(Color.BLACK);
            holder.getClRoot().setBackgroundResource(R.color.themeDayDarkBackground);
        } else {
            //holder.rootView.setBackgroundColor(Color.WHITE);
            holder.getClRoot().setBackgroundResource(R.color.themeDayWhiteBackground);
        }

        // Icon
        LocalizedUnitType localizedUnitType = UnitTypeContainer.getLocalizedUnitType( unitType.getId() );
        if( localizedUnitType != null ) holder.getIvQuickConvertIcon().setImageDrawable(ContextCompat.getDrawable( context , localizedUnitType.getIconId()));

        // ---------------------------- Fill spinners with units -----------------------------------
        LocalizedUnit[] units = item.getArrayUnitType();

        int indexFrom = -1;
        int indexTo = -1;

        for( int i=0; i<units.length; i++ ){
            if( units[i].getUnitKey().equals(item.getIdUnitFrom()) ) indexFrom = i;
            if( units[i].getUnitKey().equals(item.getIdUnitTo()) ) indexTo = i;
            if( indexFrom != -1 && indexTo != -1 ) break;
        }

        HighlightArrayAdapter adapterFrom = new HighlightArrayAdapter( context , android.R.layout.simple_spinner_item , units){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if( convertView == null ){
                    final View v = vi.inflate(android.R.layout.simple_spinner_item, null);
                    final TextView t = v.findViewById(android.R.id.text1);
                    t.setText(units[position].getNameShort());
                    return v;
                }else{
                    return convertView;
                }
            }
        };
        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        HighlightArrayAdapter adapterTo = new HighlightArrayAdapter( context , android.R.layout.simple_spinner_item , units){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if( convertView == null ){
                    final View v = vi.inflate(android.R.layout.simple_spinner_item, null);
                    final TextView t = v.findViewById(android.R.id.text1);
                    t.setText(units[position].getNameShort());
                    return v;
                }else{
                    return convertView;
                }

            }
        };
        adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spQuickConvertFrom.setAdapter( adapterFrom );
        spQuickConvertTo.setAdapter( adapterTo );

        if( indexFrom >= 0 ) spQuickConvertFrom.setSelection( indexFrom , false );
        if( indexTo >= 0 ) spQuickConvertTo.setSelection( indexTo , false );

        // -------------------------------- Convert values -----------------------------------------
        TextView tvQuickConvertResult = holder.getTvQuickConvertResult();

        spQuickConvertFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvQuickConvertResult.setText( getResultValue( etInput.getText().toString() , holder , unitType ) );
                adapterFrom.setSelection( position );
                setTextInputHint( holder , unitType );

                // Change input method based on selected unit
                if( unitType.getId().equals(Numerative.id) ){
                    LocalizedUnit currentUnit = (LocalizedUnit) spQuickConvertFrom.getSelectedItem();
                    quickConverterFragment.initCustomKeyboard( unitType , currentUnit.getUnitKey() , etInput );
                    etInput.setText("");
                    etInput.setSelection(etInput.getText().length());
                    etInput.requestFocus();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spQuickConvertTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapterTo.setSelection( position );
                tvQuickConvertResult.setText( getResultValue( etInput.getText().toString() , holder , unitType ) );
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        etInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvQuickConvertResult.setText( getResultValue( s.toString() , holder , unitType ) );
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
        //Clear focus here from edittext
        etInput.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId== EditorInfo.IME_ACTION_DONE) etInput.clearFocus();
            return false;
        });

        // -------------------------------- Edit Text Hint -----------------------------------------

        setTextInputHint( holder , unitType );

        // ------------------------------------ Buttons  -------------------------------------------

        // Delete
        holder.getBtnQuickConvertDelete().setOnClickListener(view -> {

            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("Delete favourite");
            alert.setMessage("Are you sure you want to delete '"+localizedUnitTypeName+"'?");
            alert.setPositiveButton("Yes", (dialog, which) -> {
                quickConvertUnits.remove( item );
                notifyItemRemoved( position );
                notifyItemRangeChanged(position,getItemCount()-position);
                quickConverterFragment.removeQuickConvertUnit( item );
            });

            alert.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

            alert.show();

        });

        // Edit
        holder.getBtnQuickConvertEdit().setOnClickListener(view -> quickConverterFragment.startEditingQuickConvertUnit( item ));

        // Open
        holder.getBtnQuickConvertOpen().setOnClickListener(view -> quickConverterFragment.openUnitTypeExtended( item.getUnitTypeId() ));

    }

    /**
     * Input Hint relative to current From Unit
     * @param holder ViewHolder
     * @param unitType unitType
     */
    private void setTextInputHint( ViewHolder holder , UnitType unitType ){
        String unitSample = ( (LocalizedUnit) holder.getSpQuickConvertFrom().getSelectedItem() ).getSampleInput();
        String unitTypeSample = unitType.getUnitTypeSampleInput();
        if( !unitSample.isEmpty() ){
            holder.getTilQuickConvertInput().setHint(context.getString(R.string.unit_type_value_text_hint)+" "+unitSample);
        }else if( !unitTypeSample.isEmpty() ){
            holder.getTilQuickConvertInput().setHint(context.getString(R.string.unit_type_value_text_hint)+" "+unitTypeSample);
        }else{
            holder.getTilQuickConvertInput().setHint(context.getString(R.string.unit_type_value_text_hint_short));
        }
    }

    /**
     * Calculate result based on parameters
     * @param input value
     * @param holder ViewHolder
     * @param unitType UnitType
     * @return String result
     */
    private String getResultValue( String input , ViewHolder holder , UnitType unitType){
        input = input.trim();

        if(!unitType.getId().equals(Numerative.id) && !unitType.getId().equals(ColourCode.id) && !unitType.getId().equals(BraSize.id) ){

            if( Calculator.locale == Locale.GERMANY ) input = CompatibilityHandler.convertNumberFormatDEtoSystem( input );
            if( !NumberUtils.isCreatable(input) ) return "...";

        }

        if( unitType.getId().equals(Currency.id) ){
            CalcCurrency.getInstance();
            CalcCurrency.getInstance().initializeCurrency( context , quickConverterFragment.getRoot() , quickConverterFragment.getSharedPref() , quickConverterFragment.getQuickConverterViewModel() );
        }

        LocalizedUnit selectedFromUnit = (LocalizedUnit) holder.getSpQuickConvertFrom().getSelectedItem();
        if( selectedFromUnit == null ) return "";
        LocalizedUnit selectedToUnit = (LocalizedUnit) holder.getSpQuickConvertTo().getSelectedItem();
        if( selectedToUnit == null ) return "";

        return Calculator.getSingleResult( input , selectedFromUnit.getUnitKey() , selectedToUnit , unitType ).getValue();
    }

    public void refreshCurrency(){
        for( int i=0;i<quickConvertUnits.size();i++ ){
            if( quickConvertUnits.get(i).getUnitTypeId().equals(Currency.id) ){
                notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return quickConvertUnits.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final ConstraintLayout clRoot;

        private final ImageView ivQuickConvertIcon;
        private final TextView tvQuickConvertTitle;
        private final Spinner spQuickConvertFrom;
        private final Spinner spQuickConvertTo;
        private final EditText etQuickConvertInput;
        private final TextInputLayout tilQuickConvertInput;
        private final TextView tvQuickConvertResult;

        private final ImageButton btnQuickConvertEdit;
        private final ImageButton btnQuickConvertDelete;
        private final ImageButton btnQuickConvertOpen;

        public ViewHolder(View view) {
            super(view);

            clRoot = view.findViewById( R.id.clQuickConvertRoot );
            ivQuickConvertIcon = view.findViewById( R.id.ivQuickConvertIcon );
            tvQuickConvertTitle = view.findViewById( R.id.tvQuickConvertTitle );
            spQuickConvertFrom = view.findViewById( R.id.spQuickConvertFrom );
            spQuickConvertTo = view.findViewById( R.id.spQuickConvertTo );
            etQuickConvertInput = view.findViewById( R.id.etQuickConvertInput );
            tilQuickConvertInput = view.findViewById( R.id.tilQuickConvertInput );
            tvQuickConvertResult = view.findViewById( R.id.tvQuickConvertResult );
            btnQuickConvertEdit = view.findViewById( R.id.btnQuickConvertEdit );
            btnQuickConvertDelete = view.findViewById( R.id.btnQuickConvertDelete );
            btnQuickConvertOpen = view.findViewById( R.id.btnQuickConvertOpen );

        }

        @Override
        public void onClick(View view) {

        }

        public ConstraintLayout getClRoot() { return clRoot; }
        public ImageView getIvQuickConvertIcon() { return ivQuickConvertIcon; }
        public TextView getTvQuickConvertTitle() { return tvQuickConvertTitle; }
        public Spinner getSpQuickConvertFrom() { return spQuickConvertFrom; }
        public Spinner getSpQuickConvertTo() { return spQuickConvertTo; }
        public EditText getEtQuickConvertInput() { return etQuickConvertInput; }
        public TextInputLayout getTilQuickConvertInput() { return tilQuickConvertInput; }
        public TextView getTvQuickConvertResult() { return tvQuickConvertResult; }
        public ImageButton getBtnQuickConvertEdit() { return btnQuickConvertEdit; }
        public ImageButton getBtnQuickConvertDelete() { return btnQuickConvertDelete; }
        public ImageButton getBtnQuickConvertOpen() { return btnQuickConvertOpen; }
    }
}
package com.sbsc.convertee.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.sbsc.convertee.R;
import com.sbsc.convertee.UnitTypeContainer;
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
import com.sbsc.convertee.tools.HighlightArrayAdapter;
import com.sbsc.convertee.ui.quickconverter.QuickConverterFragment;

import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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

        // Title
        holder.getTvQuickConvertTitle().setText( unitType.getUnitTypeLocalizedName( context ) );

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

        Spinner spQuickConvertFrom = holder.getSpQuickConvertFrom();
        Spinner spQuickConvertTo = holder.getSpQuickConvertTo();

        HighlightArrayAdapter adapterFrom = new HighlightArrayAdapter( context , android.R.layout.simple_spinner_item , units){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View v = vi.inflate(android.R.layout.simple_spinner_item, null);
                final TextView t = (TextView)v.findViewById(android.R.id.text1);
                t.setText(units[position].getNameShort());
                return v;
            }
        };
        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        HighlightArrayAdapter adapterTo = new HighlightArrayAdapter( context , android.R.layout.simple_spinner_item , units){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View v = vi.inflate(android.R.layout.simple_spinner_item, null);
                final TextView t = (TextView)v.findViewById(android.R.id.text1);
                t.setText(units[position].getNameShort());
                return v;
            }
        };
        adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spQuickConvertFrom.setAdapter( adapterFrom );
        spQuickConvertTo.setAdapter( adapterTo );

        if( indexFrom >= 0 ) spQuickConvertFrom.setSelection( indexFrom );
        if( indexTo >= 0 ) spQuickConvertTo.setSelection( indexTo );

        // -------------------------------- Convert values -----------------------------------------
        EditText etQuickConvert = holder.getEtQuickConvertInput();
        TextView tvQuickConvertResult = holder.getTvQuickConvertResult();

        spQuickConvertFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvQuickConvertResult.setText( getResultValue( etQuickConvert.getText().toString() , item , holder , unitType ) );
                adapterFrom.setSelection( position );
                setTextInputHint( holder , unitType );
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spQuickConvertTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapterTo.setSelection( position );
                tvQuickConvertResult.setText( getResultValue( etQuickConvert.getText().toString() , item , holder , unitType ) );
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        etQuickConvert.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvQuickConvertResult.setText( getResultValue( s.toString() , item , holder , unitType ) );
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
        //Clear focus here from edittext
        etQuickConvert.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId== EditorInfo.IME_ACTION_DONE) etQuickConvert.clearFocus();
            return false;
        });

        // -------------------------------- Edit Text Hint -----------------------------------------

        setTextInputHint( holder , unitType );

        // ------------------------------------ Buttons  -------------------------------------------

        holder.getBtnQuickConvertDelete().setOnClickListener(view -> {
            quickConvertUnits.remove( item );
            notifyItemRemoved( position );
            quickConverterFragment.removeQuickConvertUnit( item );
        });

        holder.getBtnQuickConvertEdit().setOnClickListener(view -> {
            quickConverterFragment.startEditingQuickConvertUnit( item );
        });

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
     * @param item QuickConvertItem containing Unit information
     * @param holder ViewHolder
     * @param unitType UnitType
     * @return String result
     */
    private String getResultValue( String input , QuickConvertUnit item , ViewHolder holder , UnitType unitType){
        input = input.trim();
        if( !NumberUtils.isCreatable(input) &&
                !item.getUnitTypeId().equals(Numerative.id) &&
                !item.getUnitTypeId().equals(ColourCode.id) &&
                !item.getUnitTypeId().equals(BraSize.id)
        ){
            return "";
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

        private final ImageView ivQuickConvertIcon;
        private final TextView tvQuickConvertTitle;
        private final Spinner spQuickConvertFrom;
        private final Spinner spQuickConvertTo;
        private final EditText etQuickConvertInput;
        private final TextInputLayout tilQuickConvertInput;
        private final TextView tvQuickConvertResult;

        private final ImageButton btnQuickConvertEdit;
        private final ImageButton btnQuickConvertDelete;


        public ViewHolder(View view) {
            super(view);

            ivQuickConvertIcon = view.findViewById( R.id.ivQuickConvertIcon );
            tvQuickConvertTitle = view.findViewById( R.id.tvQuickConvertTitle );
            spQuickConvertFrom = view.findViewById( R.id.spQuickConvertFrom );
            spQuickConvertTo = view.findViewById( R.id.spQuickConvertTo );
            etQuickConvertInput = view.findViewById( R.id.etQuickConvertInput );
            tilQuickConvertInput = view.findViewById( R.id.tilQuickConvertInput );
            tvQuickConvertResult = view.findViewById( R.id.tvQuickConvertResult );
            btnQuickConvertEdit = view.findViewById( R.id.btnQuickConvertEdit );
            btnQuickConvertDelete = view.findViewById( R.id.btnQuickConvertDelete );

        }

        @Override
        public void onClick(View view) {

        }

        public ImageView getIvQuickConvertIcon() { return ivQuickConvertIcon; }
        public TextView getTvQuickConvertTitle() { return tvQuickConvertTitle; }
        public Spinner getSpQuickConvertFrom() { return spQuickConvertFrom; }
        public Spinner getSpQuickConvertTo() { return spQuickConvertTo; }
        public EditText getEtQuickConvertInput() { return etQuickConvertInput; }
        public TextInputLayout getTilQuickConvertInput() { return tilQuickConvertInput; }
        public TextView getTvQuickConvertResult() { return tvQuickConvertResult; }
        public ImageButton getBtnQuickConvertEdit() { return btnQuickConvertEdit; }
        public ImageButton getBtnQuickConvertDelete() { return btnQuickConvertDelete; }
    }
}
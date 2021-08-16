package com.sbsc.convertee.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.sbsc.convertee.R;
import com.sbsc.convertee.entities.UnitTypeContainer;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnit;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnitType;
import com.sbsc.convertee.entities.adapteritems.QuickConvertUnit;
import com.sbsc.convertee.entities.unittypes.generic.UnitType;
import com.sbsc.convertee.entities.unittypes.generic.UnitTypeEntry;
import com.sbsc.convertee.tools.CompatibilityHandler;
import com.sbsc.convertee.tools.HighlightArrayAdapter;
import com.sbsc.convertee.ui.appsetup.AppSetupUnitDetailFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SetupUnitTypeQuickConvertAdapter extends RecyclerView.Adapter<SetupUnitTypeQuickConvertAdapter.ViewHolder>{

        private final List<QuickConvertUnit> quickConvertUnits;

        // References
        private final Context context;
        private final AppSetupUnitDetailFragment fragment;

        public SetupUnitTypeQuickConvertAdapter(String[] quickConvertUnits, Context ctx, AppSetupUnitDetailFragment appSetupUnitFragment){
            this.context = ctx;
            this.quickConvertUnits = createList(quickConvertUnits);
            this.fragment = appSetupUnitFragment;
        }

        private List<QuickConvertUnit> createList(String[] unitTypes ){

            List<QuickConvertUnit> list = new ArrayList<>();

            for( String unitTypeId : unitTypes ){
                LocalizedUnitType unitTypeLocalized = UnitTypeContainer.getLocalizedUnitType(unitTypeId);
                List<LocalizedUnit> units = new ArrayList<>();

                if (unitTypeLocalized == null) break;

                for( UnitTypeEntry entry : UnitType.filterUnits( new HashSet<>(), unitTypeLocalized.getUnitTypeObject() ) ){
                    units.add( entry.localize(context) );
                }

                list.add( new QuickConvertUnit( unitTypeId , "1" , units.get(0).getUnitKey() , units.get(1).getUnitKey() , units.toArray(new LocalizedUnit[0]) ));
            }

            return list;
        }

        @NonNull
        @Override
        public SetupUnitTypeQuickConvertAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from( viewGroup.getContext() ).inflate(R.layout.adapter_setup_quickconvert_item, viewGroup, false);
            return new ViewHolder(view);
        }

        @SuppressLint("RestrictedApi")
        @Override
        public void onBindViewHolder(@NonNull SetupUnitTypeQuickConvertAdapter.ViewHolder holder, int position) {

            // Get current unit item
            QuickConvertUnit item = quickConvertUnits.get(position);
            UnitType unitType = UnitTypeContainer.getUnitType( item.getUnitTypeId() );

            // Title
            holder.getTextUnitTypeName().setText( unitType.getUnitTypeLocalizedName( context ) );

            // Icon
            LocalizedUnitType localizedUnitType = UnitTypeContainer.getLocalizedUnitType( unitType.getId() );
            if( localizedUnitType != null ) holder.getImageUnitIcon().setImageDrawable(ContextCompat.getDrawable( context , localizedUnitType.getIconId()));

            // From
            Spinner spFrom = holder.getSpinnerFrom();
            HighlightArrayAdapter adapterFrom = new HighlightArrayAdapter( context , android.R.layout.simple_spinner_item , item.getArrayUnitType() ){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    if( convertView == null ){
                        final View v = vi.inflate(android.R.layout.simple_spinner_item, null);
                        final TextView t = v.findViewById(android.R.id.text1);
                        t.setText(item.getArrayUnitType()[position].getNameShort());
                        return v;
                    }else{
                        return convertView;
                    }
                }
            };
            adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spFrom.setAdapter(adapterFrom);
            spFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    adapterFrom.setSelection( position );
                    item.setIdUnitFrom( item.getArrayUnitType()[position].getUnitKey() );
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) { }
            });
            spFrom.setSelection(0,false);

            // To
            Spinner spTo = holder.getSpinnerTo();
            HighlightArrayAdapter adapterTo = new HighlightArrayAdapter( context , android.R.layout.simple_spinner_item , item.getArrayUnitType() ){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    if( convertView == null ){
                        final View v = vi.inflate(android.R.layout.simple_spinner_item, null);
                        final TextView t = v.findViewById(android.R.id.text1);
                        t.setText(item.getArrayUnitType()[position].getNameShort());
                        return v;
                    }else{
                        return convertView;
                    }
                }
            };
            adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spTo.setAdapter(adapterTo);
            spTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    adapterTo.setSelection( position );
                    item.setIdUnitTo( item.getArrayUnitType()[position].getUnitKey() );
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) { }
            });
            spTo.setSelection(1,false);

            // EDIT TEXT
            TextInputLayout inputLayout = holder.getTextInputLayout();
            EditText etInput = holder.getEditTextValue();
            inputLayout.setHintAnimationEnabled(false);
            inputLayout.setHint( context.getText(R.string.quickconvert_editor_default_value) );
            // Sample Input
            String unitSample = ( (LocalizedUnit) spFrom.getSelectedItem() ).getSampleInput();
            etInput.setText( unitSample );
            inputLayout.setHintAnimationEnabled(true);
            // Listener
            etInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    item.setDefaultValue( s.toString() );
                }
            });

            // KEYBOARD STUFF
            if ( CompatibilityHandler.shouldUseCustomKeyboard() ) {
                etInput.setShowSoftInputOnFocus(false);
                etInput.setOnFocusChangeListener((view, b) -> {
                    if( b ){
                        LocalizedUnit currentUnit = (LocalizedUnit) spFrom.getSelectedItem();
                        fragment.initCustomKeyboard( unitType , currentUnit.getUnitKey() , etInput );
                    }
                });
                etInput.setOnClickListener(view -> {
                    LocalizedUnit currentUnit = (LocalizedUnit) spFrom.getSelectedItem();
                    fragment.initCustomKeyboard( unitType , currentUnit.getUnitKey() , etInput );
                });
            } else {
                etInput.setOnFocusChangeListener((view, b) -> {
                    if( b ){
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etInput, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                etInput.setOnClickListener(view -> {
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(etInput, InputMethodManager.SHOW_IMPLICIT);
                });
                etInput.setShowSoftInputOnFocus(true);
                etInput.clearFocus();
            }

            holder.setUnitTypeKey( item.getUnitTypeId() );
        }

        @Override
        public int getItemCount() { return quickConvertUnits.size(); }

        public List<QuickConvertUnit> getQuickConvertUnits(){ return this.quickConvertUnits; }

        public static class ViewHolder extends RecyclerView.ViewHolder{

            private final TextView textUnitTypeName;
            private final ImageView imageUnitIcon;
            private final TextInputLayout textInputLayout;
            private final EditText editTextValue;
            private final Spinner spinnerFrom;
            private final Spinner spinnerTo;

            private String unitTypeKey;

            public ViewHolder(View view) {
                super(view);
                textUnitTypeName = view.findViewById(R.id.tvSetupQuickConvertTitle);
                imageUnitIcon = view.findViewById(R.id.ivSetupQuickConvertIcon);
                spinnerFrom = view.findViewById(R.id.spSetupQuickConvertFrom);
                spinnerTo = view.findViewById(R.id.spSetupQuickConvertTo);
                editTextValue = view.findViewById(R.id.etSetupQuickConvertInput);
                textInputLayout = view.findViewById(R.id.tilSetupQuickConvertInput);
            }

            // Setter
            public void setUnitTypeKey( String unitTypeKey ){ this.unitTypeKey = unitTypeKey; }

            // Getter
            public TextView getTextUnitTypeName() { return textUnitTypeName; }
            public ImageView getImageUnitIcon() { return imageUnitIcon; }
            public String getUnitTypeKey() { return unitTypeKey; }
            public Spinner getSpinnerFrom() { return spinnerFrom; }
            public Spinner getSpinnerTo() { return spinnerTo; }
            public EditText getEditTextValue() { return editTextValue; }
            public TextInputLayout getTextInputLayout() { return textInputLayout; }
        }

}

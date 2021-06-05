package com.sbsc.convertee.ui.quickconverter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.sbsc.convertee.R;
import com.sbsc.convertee.entities.UnitTypeContainer;
import com.sbsc.convertee.calculator.CalcColourCode;
import com.sbsc.convertee.calculator.CalcNumerative;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnit;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnitType;
import com.sbsc.convertee.entities.adapteritems.QuickConvertUnit;
import com.sbsc.convertee.entities.unittypes.ColourCode;
import com.sbsc.convertee.entities.unittypes.Numerative;
import com.sbsc.convertee.entities.unittypes.generic.UnitType;
import com.sbsc.convertee.ui.converter.UnitConverterFragment;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuickConvertEditorFragment extends Fragment {

    // Shared Preferences
    private SharedPreferences sharedPref;

    private EditText etQuickConvertEditorValue;

    private Spinner spQuickConvertEditorUnitType;
    private Spinner spQuickConvertEditorUnitFrom;
    private Spinner spQuickConvertEditorUnitTo;

    private Button btnQuickConvertEditorSave;

    private List<LocalizedUnit> unitArr;
    private List<LocalizedUnit> unitFromArr;
    private List<LocalizedUnit> unitToArr;

    private ArrayAdapter<LocalizedUnit> adapterFrom;
    private ArrayAdapter<LocalizedUnit> adapterTo;

    private QuickConvertUnit editingUnitType;
    private boolean validValue = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = PreferenceManager
                .getDefaultSharedPreferences(requireContext());

        // TODO use resource instead

        editingUnitType = null;
        if (getArguments() != null) {
            String arg = getArguments().getString("QuickConvertEditorItem");
            if( arg != null ){
                String[] argArr = arg.split("::");
                if( argArr.length == 4 ){
                    editingUnitType = new QuickConvertUnit( argArr , null);
                }else if( argArr.length == 3){
                    editingUnitType = new QuickConvertUnit( argArr , null);
                }
            }
        }

        unitArr = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_quick_convert_editor, container, false);

        initEditTextValue( root );
        initFinishButton( root );
        initSpinners( root );

        // Disable unit type spinner if user is editing a favourite
        if(editingUnitType != null) spQuickConvertEditorUnitType.setEnabled(false);

        // Inflate the layout for this fragment
        return root;
    }

    private void initEditTextValue( View root ){

        etQuickConvertEditorValue = root.findViewById( R.id.etQuickConvertEditorValue );

        // Set default value if exists
        if( editingUnitType != null ){
            etQuickConvertEditorValue.setText( editingUnitType.getDefaultValue() );
        }

        etQuickConvertEditorValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LocalizedUnitType type = (LocalizedUnitType) spQuickConvertEditorUnitType.getSelectedItem();
                if( !isCorrectInput( charSequence.toString() , type.getUnitTypeKey() )){
                    etQuickConvertEditorValue.setError(getString(R.string.quickconvert_editor_error_hint));
                    validValue = false;
                }else{
                    validValue = true;
                }
                checkFinishButtonStatus();
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        // Clear focus here from edittext
        etQuickConvertEditorValue.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId== EditorInfo.IME_ACTION_DONE) etQuickConvertEditorValue.clearFocus();
            return false;
        });

    }

    private void initSpinners( View root ){

        spQuickConvertEditorUnitType = root.findViewById(R.id.spQuickConvertEditorUnitType);
        spQuickConvertEditorUnitFrom = root.findViewById(R.id.spQuickConvertEditorUnitFrom);
        spQuickConvertEditorUnitTo = root.findViewById(R.id.spQuickConvertEditorUnitTo);

        spQuickConvertEditorUnitType.setAdapter( new ArrayAdapter<>( requireContext() , android.R.layout.simple_list_item_1 , loadUnitTypes() ) );

        adapterTo = new ArrayAdapter<>( requireContext() , android.R.layout.simple_list_item_1, new ArrayList<>());
        adapterFrom = new ArrayAdapter<>( requireContext() , android.R.layout.simple_list_item_1, new ArrayList<>());

        unitFromArr = new ArrayList<>();
        unitToArr = new ArrayList<>();

        // Listen to unit type changes
        spQuickConvertEditorUnitType.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LocalizedUnit[] arr = loadUnitTypeUnits( UnitTypeContainer.getUnitType( ( (LocalizedUnitType) spQuickConvertEditorUnitType.getSelectedItem() ).getUnitTypeKey() ) );
                unitArr.clear();
                unitArr.addAll( Arrays.asList(arr) );

                unitFromArr.clear();
                unitFromArr.addAll(unitArr);
                unitToArr.clear();
                unitToArr.addAll(unitArr);

                adapterFrom = new ArrayAdapter<>( requireContext() , android.R.layout.simple_list_item_1 , unitFromArr );
                adapterTo = new ArrayAdapter<>( requireContext() , android.R.layout.simple_list_item_1 , unitToArr);

                spQuickConvertEditorUnitFrom.setAdapter( adapterFrom );
                spQuickConvertEditorUnitTo.setAdapter( adapterTo );

                // If editing existing type use existing default units
                if( editingUnitType != null ){

                    for( int i=0;i<unitFromArr.size();i++ ){
                        if( unitFromArr.get(i).getUnitKey().equals(editingUnitType.getIdUnitFrom()) ){
                            spQuickConvertEditorUnitFrom.setSelection(i);
                            break;
                        }
                    }

                    for( int i=0;i<unitToArr.size();i++ ){
                        if( unitToArr.get(i).getUnitKey().equals(editingUnitType.getIdUnitTo()) ){
                            spQuickConvertEditorUnitTo.setSelection(i);
                            break;
                        }
                    }

                }else{
                    spQuickConvertEditorUnitFrom.setSelection(0);
                    spQuickConvertEditorUnitTo.setSelection(1);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spQuickConvertEditorUnitFrom.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkFinishButtonStatus();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spQuickConvertEditorUnitTo.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkFinishButtonStatus();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

    }


    private void initFinishButton(View root ){
        btnQuickConvertEditorSave = root.findViewById( R.id.btnQuickConvertEditorSave);

        btnQuickConvertEditorSave.setOnClickListener(view -> {

            Set<String> quickConverterItems = new HashSet<>(sharedPref.getStringSet("QuickConvertItems", new HashSet<>()));

            LocalizedUnitType type = (LocalizedUnitType) spQuickConvertEditorUnitType.getSelectedItem();
            LocalizedUnit unitFrom = (LocalizedUnit) spQuickConvertEditorUnitFrom.getSelectedItem();
            LocalizedUnit unitTo = (LocalizedUnit) spQuickConvertEditorUnitTo.getSelectedItem();

            // If unit type is editing, exists already
            boolean contains = false;
            for( String s : quickConverterItems )
                if ( s.startsWith(type.getUnitTypeKey()) ) {
                    quickConverterItems.remove(s);
                    quickConverterItems.add(
                            type.getUnitTypeKey() + "::" + unitFrom.getUnitKey() + "::" + unitTo.getUnitKey() + "::" + etQuickConvertEditorValue.getText().toString()
                    );
                    contains = true;
                    break;
                }

            // If adding new unit type
            if( !contains ) quickConverterItems.add(
                    type.getUnitTypeKey() + "::" + unitFrom.getUnitKey() + "::" + unitTo.getUnitKey() + "::" + etQuickConvertEditorValue.getText().toString()
            );

            sharedPref.edit().remove( "QuickConvertItems" ).apply();
            sharedPref.edit().putStringSet( "QuickConvertItems" , quickConverterItems ).apply();

            requireActivity().onBackPressed();
        });

    }

    /**
     * Set the "Save" button either enabled or disabled depending on some conditions
     */
    private void checkFinishButtonStatus(){

        LocalizedUnitType localizedUnitType = (LocalizedUnitType) spQuickConvertEditorUnitType.getSelectedItem();
        LocalizedUnit localizedFromUnit = (LocalizedUnit) spQuickConvertEditorUnitFrom.getSelectedItem();
        LocalizedUnit localizedToUnit = (LocalizedUnit) spQuickConvertEditorUnitTo.getSelectedItem();

        btnQuickConvertEditorSave.setEnabled(
                localizedUnitType != null &&
                !localizedFromUnit.getUnitKey().equals(localizedToUnit.getUnitKey()) &&
                validValue
        );
    }

    /**
     * Load unit types from unit type container, filter out existing favourite ones, if user is
     * editing a specific one -> add only that unit type
     * @return Array of unit types
     */
    private LocalizedUnitType[] loadUnitTypes(){
        Set<String> quickConverterItems = new HashSet<>(sharedPref.getStringSet("QuickConvertItems", new HashSet<>()));
        LocalizedUnitType[] allUnitTypes = UnitTypeContainer.localizedUnitTypes;
        List<LocalizedUnitType> result = new ArrayList<>();

        if( editingUnitType == null ){

            // Case: Creating new one
            for( LocalizedUnitType unitType : allUnitTypes ){
                boolean isHidden = false;
                for( String hiddenType : quickConverterItems ){
                    if( unitType.getUnitTypeKey().startsWith( hiddenType.split("::")[0] ) ){
                        isHidden = true;
                        break;
                    }
                }
                if( !isHidden ) result.add( unitType );
            }

            requireActivity().setTitle( "Create favourite" );

        }else{

            // Case: Editing existing one
            for( LocalizedUnitType unitType : allUnitTypes ){
                if( unitType.getUnitTypeKey().startsWith( editingUnitType.getUnitTypeId() ) ){
                    result.add(unitType);
                    break;
                }
            }

            requireActivity().setTitle( "Edit "+result.get(0).getUnitTypeName());
        }
        Collections.sort(result);
        return result.toArray(new LocalizedUnitType[0]);
    }

    /**
     * Get the localized units for this specific unit type
     * @param unitType Units of this type
     * @return Array
     */
    private LocalizedUnit[] loadUnitTypeUnits(UnitType unitType ){

        // Get string set of hidden units of this type from preferences
        Set<String> hiddenUnits = sharedPref.getStringSet(("preference_"+ unitType.getId() +"_hidden"), new HashSet<>());

        return UnitConverterFragment.loadLocalizedUnitNames(
                getContext(),
                UnitType.filterUnits( hiddenUnits , unitType )
        );
    }

    private boolean isCorrectInput( String input , String unitTypeId ){
        LocalizedUnit unit = (LocalizedUnit) spQuickConvertEditorUnitFrom.getSelectedItem();
        if( input.isEmpty() ) return true;
        if( unitTypeId.equals(Numerative.id) ) return !CalcNumerative.isNotAllowedInSystem( input , unit.getUnitKey() );
        if( unitTypeId.equals(ColourCode.id) ) return CalcColourCode.getInstance().isCorrectInput( input , unit.getUnitKey() );
        return ( NumberUtils.isCreatable(input)); // TODO BraSize isnt checked yet
    }
}
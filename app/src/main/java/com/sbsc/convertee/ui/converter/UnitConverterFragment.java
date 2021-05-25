package com.sbsc.convertee.ui.converter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.solver.widgets.Helper;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.sbsc.convertee.R;
import com.sbsc.convertee.adapter.CalculatedUnitItemAdapter;
import com.sbsc.convertee.calculator.CalcShoeSize;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnit;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnitType;
import com.sbsc.convertee.calculator.CalcNumerative;
import com.sbsc.convertee.calculator.Calculator;
import com.sbsc.convertee.entities.unittypes.Numerative;
import com.sbsc.convertee.entities.unittypes.ShoeSize;
import com.sbsc.convertee.entities.unittypes.generic.UnitType;
import com.sbsc.convertee.entities.unittypes.generic.UnitTypeEntry;
import com.sbsc.convertee.tools.CompatibilityHandler;
import com.sbsc.convertee.tools.HelperUtil;
import com.sbsc.convertee.UnitTypeContainer;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashSet;
import java.util.Set;

/**
 *  Conversion Fragment offers UI and Utility to convert specific UnitType
 */
public class UnitConverterFragment extends Fragment {

    /**
     * ViewModel contains UI relevant data
     */
    private UnitConverterViewModel unitConverterViewModel;

    // Views
    private Spinner spUnitSelector;
    private EditText etValue;
    private SeekBar sbUnitSelector;
    private SharedPreferences sharedPref;

    // UnitType
    private UnitType unitType;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        unitConverterViewModel = new ViewModelProvider(this).get(UnitConverterViewModel.class);
        unitConverterViewModel.getUnitItemAdapterValue().setFragment(this);
        View root = inflater.inflate(R.layout.fragment_unit_converter, container, false);

        unitType = UnitTypeContainer.initializeUnitType( getArguments() , requireContext() );
        updateTitle();

        // Initialize preferences
        sharedPref = PreferenceManager
                .getDefaultSharedPreferences( requireContext() );

        // Initialize potentially needed classes
        if (unitType.getId().equals(ShoeSize.id)){
            CalcShoeSize.getInstance();
        }

        // Initialize UI Views
        initSpinnerUnitSelector( root );
        initEditTextValue( root );
        initSeekBarUnitSelector( root );
        initRecyclerViewCalcUnitList( root );

        // Observes handle UI changes
        initViewModelObserves();

        return root;
    }

    /*
     * ======================================== INITIALIZE =========================================
     */

    /**
     * Update the title shown in the activities toolbar
     */
    private void updateTitle(){
        LocalizedUnitType[] unitTypes = UnitTypeContainer.localizedUnitTypes;
        for(LocalizedUnitType localizedUnitType : unitTypes){
            if( localizedUnitType.getUnitTypeKey().equalsIgnoreCase( unitType.getId() ) )
                requireActivity().setTitle( localizedUnitType.getUnitTypeName() );
        }
    }

    /*
     * ======================================== UI ELEMENTS ========================================
     */

    /**
     * Initialize the Spinner view element used to select the base unit from which everything else
     * will be converted
     * @param root View of inflated hierarchy of fragment
     */
    private void initSpinnerUnitSelector( View root ){
        spUnitSelector = root.findViewById(R.id.spDistanceUnit);
        spUnitSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unitConverterViewModel.setSelectedUnitIndex(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    /**
     * Initialize the EditText view element which takes the users value to be converted
     * @param root View of inflated hierarchy of fragment
     */
    private void initEditTextValue( View root ){
        etValue = root.findViewById(R.id.etDistanceValue);

        // Change hint in the layout rather than the EditText, otherwise it will duplicate
        TextInputLayout etValueLayout = root.findViewById(R.id.tilDistanceInput);

        etValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if( before == 0 && count == 0 ) return;
                updateCalculatedValues(s.toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if( length == 0 ) return;

                // Numerative block input if not allowed
                if ( unitType.getId().equals( Numerative.id ) ){
                    if( CalcNumerative.isNotAllowedInSystem( "" + s.charAt(length - 1), unitConverterViewModel.getSelectedLocalizedUnitValue().getUnitName() ) ){
                        etValue.getText().delete(length - 1, length);
                    }
                }
            }
        });
        // Close keyboard when pressing enter/done and make TextView lose focus
        etValue.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                HelperUtil.hideKeyboard(requireActivity());
                etValue.clearFocus();
                spUnitSelector.requestFocus();
                return true;
            }
            return false;
        });
    }

    /**
     * Initialize the SeekBar view element which will be used alongside the Spinner to select a
     * base unit. Changing selection in one will result in both, Spinner and SeekBar being changed.
     * @param root View of inflated hierarchy of fragment
     */
    private void initSeekBarUnitSelector( View root ){
        sbUnitSelector = root.findViewById(R.id.sbDistanceUnit);
        sbUnitSelector.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) unitConverterViewModel.setSelectedUnitIndex(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    /**
     * Initialize the RecyclerView element which displays a list of every unit (except the one that
     * is picked by the user in Spinner/SeekBar), the converted value, and the units short name
     * @param root View of inflated hierarchy of fragment
     */
    private void initRecyclerViewCalcUnitList( View root ){
        // LayoutManager
        LinearLayoutManager rvListLayoutManager = new LinearLayoutManager(getContext());
        rvListLayoutManager.setStackFromEnd(true);
        rvListLayoutManager.setReverseLayout(true);

        // RecyclerView
        RecyclerView rvCalculatedUnitList = root.findViewById(R.id.rvDistanceItems);
        rvCalculatedUnitList.setAdapter(unitConverterViewModel.getUnitItemAdapterValue());
        rvCalculatedUnitList.setLayoutManager(rvListLayoutManager);
        rvCalculatedUnitList.addItemDecoration(new DividerItemDecoration(requireContext() ,LinearLayoutManager.VERTICAL));

    }

    /*
     * =========================================== EVENTS ==========================================
     */

    /**
     * Set selection by using the units short name, called from adapter when the visual button of
     * short name was clicked
     * @param name - Unit short name
     */
    public void selectUnitByName( String name ){
        Adapter adapter = spUnitSelector.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            String itemName = ((LocalizedUnit)adapter.getItem(i)).getUnitName();
            if( name.equalsIgnoreCase(itemName)) {
                spUnitSelector.setSelection(i);
                break;
            }
        }
    }

    /*
     * ========================================= VIEW MODEL ========================================
     */

    private void initViewModelObserves(){

        // Observe UnitList in ViewModel for changes then update spinner and SeekBar
        unitConverterViewModel.getLocalizedUnits().observe(getViewLifecycleOwner(), distances -> {

            // Change spinner
            ArrayAdapter<LocalizedUnit> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_list_item, distances);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spUnitSelector.setAdapter(adapter);

            // Change SeekBar
            sbUnitSelector.setMax(distances.length-1);
            sbUnitSelector.setProgress(spUnitSelector.getSelectedItemPosition());
        });

        // Observe integer representing selected unit then update selected item in spinner and SeekBar
        unitConverterViewModel.getSelectedUnitIndex().observe(getViewLifecycleOwner(), integer -> {

            // Change input method based on selected unit
            if( unitType.getId().equals(Numerative.id) ){
                LocalizedUnit unit = unitConverterViewModel.getSelectedLocalizedUnitValue();
                if( unit != null ){
                    if( unit.getUnitName().equals("hex") ){
                        etValue.setInputType( InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS );
                    }else{
                        etValue.setInputType( InputType.TYPE_CLASS_NUMBER );
                    }
                    etValue.setText("");
                    etValue.setSelection(etValue.getText().length());
                }
            }

            sbUnitSelector.setProgress(integer);
            spUnitSelector.setSelection(integer);

            // Update calculated values
            updateCalculatedValues(etValue.getText().toString());
        });

        // Observe boolean value whether ProMode is active or not
        unitConverterViewModel.getProModeActive().observe(getViewLifecycleOwner(), bool -> {
                    unitConverterViewModel.getUnitItemAdapterValue().setProModeActive(bool);
                    
            }
        );
    }

    /*
     * =========================================== LOGIC ===========================================
     */

    /**
     * Called when either the input value OR the selected unit change
     * Calculate and update shown values
     * @param currentValue - typed in value by user
     */
    private void updateCalculatedValues( String currentValue ){

        // If input is not a valid number
        // Also check if its a numerative, as Hex Numbers require A-F
        if( !NumberUtils.isCreatable(currentValue) && !unitType.getId().equals(Numerative.id)) currentValue = "0.0";

        // Get currently selected item in spinner, if null return without updating
        LocalizedUnit selectedUnit = (LocalizedUnit) spUnitSelector.getSelectedItem();
        if( selectedUnit == null ) return;

        // Set adapter items
        CalculatedUnitItemAdapter adapter = unitConverterViewModel.getUnitItemAdapterValue();
        adapter.setDistanceItems(
                Calculator.getResultList(
                        currentValue,
                        selectedUnit.getUnitName(),
                        unitConverterViewModel.getLocalizedUnitsValue(),
                        unitType
                ),
                unitConverterViewModel.getProModeActiveValue()
        );

    }

    /**
     * Turns array of UnitTypeEntry objects into array of LocalizedUnits containing the actual Strings to be displayed in the UI
     * @param context required
     * @param units Array of UnitTypeEntry to be localized
     * @return Array of LocalizedUnit objects
     */
    public static LocalizedUnit[] loadLocalizedUnitNames(Context context , UnitTypeEntry[] units){
        LocalizedUnit[] arr = new LocalizedUnit[units.length];
        for(int i=0;i<arr.length;i++){
            arr[i] = units[i].localize( context );
        }
        return arr;
    }

    @Override
    public void onStart() {
        super.onStart();

        // SETTINGS

        // Get string set of hidden units of this type from preferences
        Set<String> rawUnits = sharedPref.getStringSet(("preference_"+ unitType.getId() +"_hidden"), new HashSet<>());
        // If LocalizedUnitsValue = null (list hasn't been loaded at all yet) OR if the hashCodes of the current and saved hiddenUnits lists are different
        if( unitConverterViewModel.getLocalizedUnitsValue() == null || rawUnits.hashCode() != unitConverterViewModel.getLocalizedUnitsHashValue() ){
            // Update HashCode
            unitConverterViewModel.setLocalizedUnitsHash( rawUnits.hashCode() );
            // Process rawUnits to localize them
            LocalizedUnit[] arr = UnitConverterFragment.loadLocalizedUnitNames(
                    getContext(),
                    UnitType.filterUnits( rawUnits , unitType )
            );
            unitConverterViewModel.setLocalizedUnits(arr);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Load and set the bool ProModeActive from preferences
        boolean proModeActive = sharedPref.getBoolean(getString(R.string.preference_pro_mode),false);
        if( proModeActive != unitConverterViewModel.getProModeActiveValue() ) unitConverterViewModel.setProModeActive(proModeActive);

    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove potentially initialized needed classes
        if (unitType.getId().equals(ShoeSize.id)){
            CalcShoeSize.deleteInstance();
        }

    }

    public void log(Object text){
        Log.d("UnitConverterFragment",""+text);
    }
}
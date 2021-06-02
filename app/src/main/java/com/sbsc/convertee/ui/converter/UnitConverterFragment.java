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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.sbsc.convertee.R;
import com.sbsc.convertee.UnitTypeContainer;
import com.sbsc.convertee.adapter.CalculatedUnitItemAdapter;
import com.sbsc.convertee.calculator.CalcColourCode;
import com.sbsc.convertee.calculator.CalcCurrency;
import com.sbsc.convertee.calculator.CalcNumerative;
import com.sbsc.convertee.calculator.CalcShoeSize;
import com.sbsc.convertee.calculator.Calculator;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnit;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnitType;
import com.sbsc.convertee.entities.calc.CalculatedUnitItem;
import com.sbsc.convertee.entities.unittypes.BraSize;
import com.sbsc.convertee.entities.unittypes.ColourCode;
import com.sbsc.convertee.entities.unittypes.Currency;
import com.sbsc.convertee.entities.unittypes.Numerative;
import com.sbsc.convertee.entities.unittypes.ShoeSize;
import com.sbsc.convertee.entities.unittypes.generic.UnitType;
import com.sbsc.convertee.entities.unittypes.generic.UnitTypeEntry;
import com.sbsc.convertee.tools.HelperUtil;

import org.apache.commons.lang3.math.NumberUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
    private TextInputLayout etValueLayout;
    private EditText etValue;
    private SharedPreferences sharedPref;
    private ImageButton btnUnitInfo;

    private LinearLayout colourDisplay;
    private TextView colourDisplayText;

    // UnitType
    private UnitType unitType;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        unitConverterViewModel = new ViewModelProvider(this).get(UnitConverterViewModel.class);
        unitConverterViewModel.getUnitItemAdapterValue().setFragment(this);
        View root = inflater.inflate(R.layout.fragment_unit_converter, container, false);

        // Get unit type object
        String unitTypeId = HelperUtil.getBundleString(
                requireArguments() ,
                requireActivity().getString(R.string.bundle_selected_unittype),
                UnitType.id
        );
        unitType = UnitTypeContainer.getUnitType( unitTypeId );
        updateTitle();

        // Initialize preferences
        sharedPref = PreferenceManager
                .getDefaultSharedPreferences( requireContext() );

        // Initialize potentially needed classes
        if (unitType.getId().equals(ShoeSize.id)){
            CalcShoeSize.getInstance();
        }else if(unitType.getId().equals(Currency.id)){
            CalcCurrency.getInstance();
            CalcCurrency.getInstance().initializeCurrency( requireContext() , root , sharedPref , unitConverterViewModel );
        }else if (unitType.getId().equals(ColourCode.id)) {
            CalcColourCode.getInstance().setViewModel( unitConverterViewModel );
        }

        // Initialize UI Views
        initSpinnerUnitSelector( root );
        initEditTextValue( root );
        initRecyclerViewCalcUnitList( root );
        initInfoButton( root );
        initColourDisplay( root );

        // Observes handle UI changes
        initViewModelObserves( root );

        setInputMethod();

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

    /**
     * Change the input method at the beginning of the fragment based on unittype
     */
    private void setInputMethod(){
        if( unitType.getId().equals(ColourCode.id) || unitType.getId().equals(BraSize.id) ){
            etValue.setInputType( InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS );
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
        etValueLayout = root.findViewById(R.id.tilDistanceInput);

        try {
            Field field = TextInputLayout.class.getDeclaredField("defaultStrokeColor");
            field.setAccessible(true);
            field.set(etValueLayout, ContextCompat.getColor( requireContext() , R.color.white));
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            Log.e("TAG", "Failed to change box color, item might look wrong");
        }

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
                    if( CalcNumerative.isNotAllowedInSystem( "" + s.charAt(length - 1), unitConverterViewModel.getSelectedLocalizedUnitValue().getUnitKey() ) ){
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

    /**
     * Info button shows information related to the active unit
     * @param root View of inflated hierarchy of fragment
     */
    private void initInfoButton( View root ){
        btnUnitInfo = root.findViewById(R.id.btnUnitInfo);
        btnUnitInfo.setOnClickListener(view -> {

            int identifier;

            if( unitType.getId().equals(Currency.id) ){

                identifier = R.string.currency_info;

            }else{

                // Build info identifier for the selected unit ( format: "shoesize_eushoesize_info")
                identifier = getResources().getIdentifier(
                        unitType.getId() + "_" + unitConverterViewModel.getSelectedLocalizedUnitValue().getUnitKey() + "_info" ,
                        "string" ,
                        requireContext().getPackageName()
                );

            }

            // If there is no text info to this unit available, don't react to clicks
            if( identifier != 0 ){
                new AlertDialog.Builder( requireContext() )
                        .setTitle( unitConverterViewModel.getSelectedLocalizedUnitValue().getLocalizedName() )
                        .setMessage( getString(identifier) )
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> { })
                        .setIcon(R.drawable.ic_info)
                        .show();
            }

        });
    }

    /**
     * This is only visible when the unit type is ColourCode; shows the currently entered colour as actual colour
     * @param root View of inflated hierarchy of fragment
     */
    private void initColourDisplay( View root ){
        if(!unitType.getId().equals(ColourCode.id)) return;
        colourDisplay = root.findViewById( R.id.colourDisplay );
        colourDisplay.setVisibility(View.GONE);
        colourDisplayText = root.findViewById( R.id.tvColourDisplayText );
    }

    /*
     * =========================================== EVENTS ==========================================
     */

    /**
     * Change visibility of the InfoButton based on currently selected unit
     */
    private void setInfoButtonVisibility(){

        // On launch the selection may be null
        if(unitConverterViewModel.getSelectedLocalizedUnitValue() == null ){
            btnUnitInfo.setVisibility(View.GONE);
            return;
        }

        // Info button for currency is always displayed, always the same
        if( unitType.getId().equals(Currency.id)){
            btnUnitInfo.setVisibility(View.VISIBLE);
            return;
        }

        // Build info identifier for the selected unit
        int identifier = getResources().getIdentifier(
                unitType.getId() + "_" + unitConverterViewModel.getSelectedLocalizedUnitValue().getUnitKey() + "_info" ,
                "string" ,
                requireContext().getPackageName()
        );

        // If there is no text info to this unit available, remove the button
        if( identifier != 0 ){
            btnUnitInfo.setVisibility(View.VISIBLE);
        }else{
            btnUnitInfo.setVisibility(View.GONE);
        }
    }

    /**
     * Unit sample is the example value being shown as hint in the edit text layout
     * and changes with every unit / unit type
     */
    private void showUnitSample(){
       String unitSample = unitConverterViewModel.getSelectedLocalizedUnitValue().getSampleInput();
       String unitTypeSample = unitType.getUnitTypeSampleInput();
       if( !unitSample.isEmpty() ){
           etValueLayout.setHint(getString(R.string.unit_type_value_text_hint)+" "+unitSample);
       }else if( !unitTypeSample.isEmpty() ){
           etValueLayout.setHint(getString(R.string.unit_type_value_text_hint)+" "+unitTypeSample);
       }else{
           etValueLayout.setHint(getString(R.string.unit_type_value_text_hint_short));
       }
    }


    /**
     * Set selection by using the units short name, called from adapter when the visual button of
     * short name was clicked
     * @param name - Unit short name
     */
    public void selectUnitByName( String name ){
        Adapter adapter = spUnitSelector.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            String itemName = ((LocalizedUnit)adapter.getItem(i)).getUnitKey();
            if( name.equalsIgnoreCase(itemName)) {
                spUnitSelector.setSelection(i);
                break;
            }
        }
    }

    /*
     * ========================================= VIEW MODEL ========================================
     */

    private void initViewModelObserves( View root ){

        // Only needed if the current unit is currency
        if(unitType.getId().equals(Currency.id)){

            // Listen to updates
            unitConverterViewModel.getCurrencyRatesUpdated().observe(getViewLifecycleOwner(), bool -> updateCalculatedValues(etValue.getText().toString()));

            TextView tvLastUpdate = root.findViewById(R.id.tvLastCurrencyUpdate);
            tvLastUpdate.setVisibility(View.VISIBLE);

            // Listen to the Currency LastUpdateTime being changed
            unitConverterViewModel.getCurrencyRatesLastUpdated().observe(getViewLifecycleOwner(), timeInMillis -> {
                String formatted = "";
                Date date = new Date(timeInMillis);

                if( timeInMillis == 0 ){
                    formatted += getString( R.string.currency_last_update_never );
                }else{
                    formatted += android.text.format.DateFormat.getDateFormat( requireContext() ).format( date );
                    formatted += " ";
                    formatted += android.text.format.DateFormat.getTimeFormat( requireContext() ).format( date );

                    // When the last update time changes an update has happened, so we have to reload displayed UI data to refresh
                    updateCalculatedValues(etValue.getText().toString());
                }
                String tvLastUpdateContent = getString( R.string.currency_last_update_title ) + " " + formatted;
                tvLastUpdate.setText(tvLastUpdateContent);
            });
            unitConverterViewModel.setCurrencyRatesLastUpdated(sharedPref.getLong("currency_rates_last_update", 0));

        // Only needed when ColourCode is unit type
        }else if ( unitType.getId().equals(ColourCode.id) ){

            // Listen to the colour to be displayed
            unitConverterViewModel.getDisplayedColour().observe(getViewLifecycleOwner(), colour -> {

                // If the colour is valid, display it in the colourDisplay, otherwise hide colourDisplay
                if( colour == null || etValue.getText().toString().isEmpty() ){
                    colourDisplay.setVisibility(View.GONE);
                }else{
                    colourDisplay.setVisibility(View.VISIBLE);
                    colourDisplay.setBackgroundColor( Color.parseColor( colour.toString() ) );

                    // Change text colour based on background for readability
                    if ( (colour.getRed()*0.299 + colour.getGreen()*0.587 + colour.getBlue()*0.114) > 186 ){
                        colourDisplayText.setTextColor(Color.BLACK);
                    }else{
                        colourDisplayText.setTextColor(Color.WHITE);
                    }
                }
            });
        }

        // Observe UnitList in ViewModel for changes then update spinner and SeekBar
        unitConverterViewModel.getLocalizedUnits().observe(getViewLifecycleOwner(), distances -> {

            // Now that localized units are loaded, check if the selected one (most likely 0) has info to be displayed
            setInfoButtonVisibility();

            // Change spinner
            ArrayAdapter<LocalizedUnit> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_list_item, distances);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spUnitSelector.setAdapter(adapter);
        });

        // Observe integer representing selected unit then update selected item in spinner
        unitConverterViewModel.getSelectedUnitIndex().observe(getViewLifecycleOwner(), integer -> {

            // Change input method based on selected unit
            if( unitType.getId().equals(Numerative.id) ){
                LocalizedUnit unit = unitConverterViewModel.getSelectedLocalizedUnitValue();
                if( unit != null ){
                    if( unit.getUnitKey().equals("hex") ){
                        etValue.setInputType( InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS );
                    }else{
                        etValue.setInputType( InputType.TYPE_CLASS_NUMBER );
                    }
                    etValue.setText("");
                    etValue.setSelection(etValue.getText().length());
                }
            }

            // Set progress
            if(spUnitSelector.getSelectedItemPosition() != integer) spUnitSelector.setSelection(integer);

            // Decide whether or not to show Info Button
            setInfoButtonVisibility();

            // Unit sample shows an example value to type in as a hint in the edit text
            showUnitSample();

            // Update calculated values
            updateCalculatedValues(etValue.getText().toString());
        });

        // Observe boolean value whether ProMode is active or not
        unitConverterViewModel.getProModeActive().observe(getViewLifecycleOwner(), bool -> unitConverterViewModel.getUnitItemAdapterValue().setProModeActive(bool)
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

        // If input is not a valid number // SETS THE VALUE TO 0.0 IF NOT CREATABLE AS NUMBER
        // Also check if its a numerative, as Hex Numbers require A-F
        if( !NumberUtils.isCreatable(currentValue) &&
                !unitType.getId().equals(Numerative.id) &&
                !unitType.getId().equals(ColourCode.id) &&
                !unitType.getId().equals(BraSize.id)
        ) currentValue = "0.0";

        // Get currently selected item in spinner, if null return without updating
        LocalizedUnit selectedUnit = (LocalizedUnit) spUnitSelector.getSelectedItem();
        if( selectedUnit == null ) return;

        // Set adapter items
        CalculatedUnitItemAdapter adapter = unitConverterViewModel.getUnitItemAdapterValue();
        List<CalculatedUnitItem> resultList = Calculator.getResultList(
                currentValue,
                selectedUnit.getUnitKey(),
                unitConverterViewModel.getLocalizedUnitsValue(),
                unitType
        );
        adapter.setDistanceItems( resultList , unitConverterViewModel.getProModeActiveValue() );


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
        Set<String> hiddenUnits = sharedPref.getStringSet(("preference_"+ unitType.getId() +"_hidden"), new HashSet<>());
        // If LocalizedUnitsValue = null (list hasn't been loaded at all yet) OR if the hashCodes of the current and saved hiddenUnits lists are different
        if( unitConverterViewModel.getLocalizedUnitsValue() == null || hiddenUnits.hashCode() != unitConverterViewModel.getLocalizedUnitsHashValue() ){
            // Update HashCode
            unitConverterViewModel.setLocalizedUnitsHash( hiddenUnits.hashCode() );
            // Process hiddenUnits to localize them
            LocalizedUnit[] arr = UnitConverterFragment.loadLocalizedUnitNames(
                    getContext(),
                    UnitType.filterUnits( hiddenUnits , unitType )
            );
            // Had to change post to set, post: add to queue; set: do it right away
            unitConverterViewModel.setLocalizedUnits(arr);
        }

        Bundle bundle = getArguments();
        if( bundle != null ){
            String previousValue = bundle.getString("activeUnitConverterTextValue");
            if( previousValue != null && !previousValue.isEmpty() ){
                etValue.setText(previousValue);
            }
            int previousSelected = bundle.getInt("activeUnitConverterSelectedIndex",-1);
            if( previousSelected >= 0 )
                unitConverterViewModel.setSelectedUnitIndex(previousSelected);
            else{

                LocalizedUnit[] arr = unitConverterViewModel.getLocalizedUnits().getValue();
                if( arr != null )
                    for( int i=0;i<arr.length;i++){
                        if(arr[i].getUnitKey().equals(unitType.getFirstSelectedUnit())){
                            unitConverterViewModel.setSelectedUnitIndex( i );
                            break;
                        }
                    }
            }
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
        }else if(unitType.getId().equals(Currency.id)){
            CalcCurrency.deleteInstance();
        }else if(unitType.getId().equals(ColourCode.id)){
            CalcColourCode.deleteInstance();
        }

    }

    public String getCurrentTextValue(){
        return etValue.getText().toString();
    }
    public int getCurrentSelectedUnitIndex(){
        return spUnitSelector.getSelectedItemPosition();
    }

    public void log(Object text){
        Log.d("UnitConverterFragment",""+text);
    }
}
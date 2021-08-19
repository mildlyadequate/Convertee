package com.sbsc.convertee.ui.quickconverter;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sbsc.convertee.MainActivity;
import com.sbsc.convertee.R;
import com.sbsc.convertee.entities.UnitTypeContainer;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnit;
import com.sbsc.convertee.entities.adapteritems.QuickConvertUnit;
import com.sbsc.convertee.entities.unittypes.generic.UnitType;
import com.sbsc.convertee.tools.CompatibilityHandler;
import com.sbsc.convertee.tools.HelperUtil;
import com.sbsc.convertee.tools.keyboards.CustomKeyboard;
import com.sbsc.convertee.tools.keyboards.KeyboardHandler;
import com.sbsc.convertee.ui.adapter.QuickConvertAdapter;
import com.sbsc.convertee.ui.converter.UnitConverterFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class QuickConverterFragment extends Fragment {

    // ViewModel
    private QuickConverterViewModel quickConverterViewModel;

    // Shared Preferences
    private SharedPreferences sharedPref;

    protected QuickConvertAdapter rvQuickConvertAdapter;
    private Set<String> quickConverterItems;

    // View
    private View root;
    private ConstraintLayout rlQuickConverterRoot;
    private ImageButton btnQuickConvertAdd;
    private FrameLayout flQuickConvertListContainer;
    private LinearLayout llEmptyListPlaceholder;

    // Keyboard
    private CustomKeyboard currentKeyboard;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        sharedPref = PreferenceManager
                .getDefaultSharedPreferences(requireContext());

        quickConverterViewModel = new ViewModelProvider(this).get(QuickConverterViewModel.class);

        requireActivity().setTitle( getString(R.string.app_name) );

        root = inflater.inflate(R.layout.fragment_quickconverter, container, false);
        rlQuickConverterRoot = root.findViewById(R.id.rlQuickConverterRoot);
        flQuickConvertListContainer = root.findViewById(R.id.flQuickConvertListContainer);
        llEmptyListPlaceholder = root.findViewById(R.id.llEmptyListPlaceholder);

        initRecyclerViewQuickConvertList( root );
        loadQuickConvertUnits();
        initAddQuickConvertUnitButton( root );
        setupViewModelBindings();

        shouldDisplayListPlaceholder( rvQuickConvertAdapter.getItemCount() );

        return root;
    }

    /**
     * Initialize recycler view
     */
    private void initRecyclerViewQuickConvertList( View root ){
        // QuickConversion
        RecyclerView rvQuickConvert = root.findViewById(R.id.rvQuickConvert);

        // LayoutManager
        LinearLayoutManager rvListLayoutManager = new LinearLayoutManager( getContext() );
        // RecyclerView
        rvQuickConvert.setLayoutManager( rvListLayoutManager );
        rvQuickConvert.addItemDecoration( new DividerItemDecoration(requireContext() , LinearLayoutManager.VERTICAL ));

        rvQuickConvertAdapter = new QuickConvertAdapter( new ArrayList<>() , this , requireContext() );

        rvQuickConvert.setAdapter( rvQuickConvertAdapter );
    }

    /**
     * Initialize Image Button "Add" that opens the editor fragment
     * @param root View
     */
    private void initAddQuickConvertUnitButton( View root ){
        btnQuickConvertAdd = root.findViewById(R.id.btnQuickConvertAdd);
        btnQuickConvertAdd.setColorFilter(Color.argb(255, 255, 255, 255));
        btnQuickConvertAdd.setOnClickListener(view -> {
            MainActivity mainActivity = (MainActivity) requireActivity();
            mainActivity.openQuickConvertEditor( null );
        });
    }

    /**
     * Initialize the keyboard, called from adapter
     * @param unitType of clicked Element
     * @param unitKey of selected from unit in spinner
     * @param inputSource input text field of this quick convert item
     */
    public void initCustomKeyboard( UnitType unitType , String unitKey , EditText inputSource ){

        // If keyboard is already defined, remove it from root view first
        if( currentKeyboard != null ){
            rlQuickConverterRoot.removeView( currentKeyboard );
        }

        // Get required keyboard from KeyboardHandler based on unittype and unit
        InputConnection ic = inputSource.onCreateInputConnection(new EditorInfo());
        CustomKeyboard newKeyboard = KeyboardHandler.getKeyboardByType( unitType.getId() , unitKey , ic , requireContext() );

        // Return if keyboard stayed the same
        if( newKeyboard == currentKeyboard ) return; else currentKeyboard = newKeyboard;

        // If keyboard is null return
        if( currentKeyboard == null ){
            inputSource.setShowSoftInputOnFocus(false);
            return;
        }

        // OnKeyboardCloseEvent remove focus from edit text
        currentKeyboard.addKeyboardClosingListener( () -> rlQuickConverterRoot.requestFocus() );

        // Add keyboard to root
        rlQuickConverterRoot.addView( currentKeyboard );

        // Set constraints for keyboard
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(rlQuickConverterRoot);
        constraintSet.connect(currentKeyboard.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constraintSet.connect(currentKeyboard.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
        constraintSet.connect(currentKeyboard.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);

        // Set constraints for other views
        constraintSet.connect(flQuickConvertListContainer.getId(), ConstraintSet.BOTTOM, currentKeyboard.getId(), ConstraintSet.TOP, 0);
        constraintSet.connect(btnQuickConvertAdd.getId(), ConstraintSet.BOTTOM, currentKeyboard.getId(), ConstraintSet.TOP, 40);

        constraintSet.applyTo(rlQuickConverterRoot);
    }

    private void shouldDisplayListPlaceholder( int itemCount ){
        if( itemCount > 0 ) {
            llEmptyListPlaceholder.setVisibility(View.GONE);
        }else{
            llEmptyListPlaceholder.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Get all quick convert units from shared preferences
     */
    private void loadQuickConvertUnits(){
        quickConverterItems = sharedPref.getStringSet( "QuickConvertItems" , new HashSet<>() );
        for ( String s : quickConverterItems ) {
            String[] values = s.split("::");
            String defaultVal = "";
            if(values.length>=4) defaultVal = values[3];
            addQuickConvertUnit( s , values[0] , values[1] , values[2] , defaultVal );
        }
        commitQuickConvertItems();
    }

    /**
     * Set the binding for currency rate updated event
     */
    private void setupViewModelBindings(){
        quickConverterViewModel.getCurrencyRatesUpdated().observe( getViewLifecycleOwner() , aBoolean -> rvQuickConvertAdapter.refreshCurrency());
    }

    /**
     * Add a specific unit to shared preferences and adapter
     * @param unitTypeId unit type ID to convert in
     * @param unitFromId unit ID to convert from
     * @param unitToId unit ID to convert to
     */
    private void addQuickConvertUnit( String packagedString , String unitTypeId , String unitFromId , String unitToId , String fromValue ){
        QuickConvertUnit newItem = new QuickConvertUnit( unitTypeId , fromValue , unitFromId , unitToId , loadUnitTypeUnits(UnitTypeContainer.getUnitType( unitTypeId )) );
        rvQuickConvertAdapter.addItem( newItem );
        quickConverterItems.add( packagedString );
    }

    /**
     * Open new fragment to edit the selected quick convert unit
     * @param quickConvertUnit selected item
     */
    public void startEditingQuickConvertUnit( QuickConvertUnit quickConvertUnit ){
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.openQuickConvertEditor( quickConvertUnit );
    }

    /**
     * Remove a specific unit from shared preferences
     * @param quickConvertUnit unit to remove
     */
    public void removeQuickConvertUnit( QuickConvertUnit quickConvertUnit ){
        hideCustomKeyboard();
        for ( String s : quickConverterItems ) {
            if( s.startsWith(quickConvertUnit.getUnitTypeId()) ){
                quickConverterItems.remove( s );
                commitQuickConvertItems();
                return;
            }
        }
    }

    /**
     * Save current String Set to shared preferences
     */
    private void commitQuickConvertItems(){
        sharedPref.edit().remove("QuickConvertItems").apply();
        sharedPref.edit().putStringSet("QuickConvertItems" , quickConverterItems ).apply();
        shouldDisplayListPlaceholder( rvQuickConvertAdapter.getItemCount() );
    }

    /**
     * Get the localized units for this specific unit type
     * @param unitType Units of this type
     * @return Array
     */
    private LocalizedUnit[] loadUnitTypeUnits( UnitType unitType ){

        // Get string set of hidden units of this type from preferences
        Set<String> hiddenUnits = sharedPref.getStringSet(("preference_"+ unitType.getId() +"_hidden"), new HashSet<>());

        return UnitConverterFragment.loadLocalizedUnitNames(
                getContext(),
                UnitType.filterUnits( hiddenUnits , unitType )
        );
    }

    /**
     * Open fragment of extended conversion of clicked unit type
     * @param unitTypeKey String
     */
    public void openUnitTypeExtended( String unitTypeKey ){
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.openUnitConverter( unitTypeKey );
    }

    @Override
    public void onPause() {
        super.onPause();
        hideCustomKeyboard();
    }

    @Override
    public void onResume() {
        super.onResume();
        HelperUtil.hideKeyboard(requireActivity());
    }

    /**
     * Shift away to root, remove keyboard from root
     */
    private void hideCustomKeyboard(){

        if( CompatibilityHandler.shouldUseCustomKeyboard() ){
            rlQuickConverterRoot.requestFocus();
            if( currentKeyboard != null ){
                CustomKeyboard.isOpen = false;
                rlQuickConverterRoot.removeView( currentKeyboard );
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(rlQuickConverterRoot);
                constraintSet.connect(btnQuickConvertAdd.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 40);
                constraintSet.applyTo(rlQuickConverterRoot);
            }
        }

    }

    public SharedPreferences getSharedPref() { return sharedPref; }
    public View getRoot() { return root; }
    public QuickConverterViewModel getQuickConverterViewModel() { return quickConverterViewModel; }
}
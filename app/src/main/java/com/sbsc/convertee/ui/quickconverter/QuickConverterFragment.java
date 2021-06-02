package com.sbsc.convertee.ui.quickconverter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sbsc.convertee.MainActivity;
import com.sbsc.convertee.R;
import com.sbsc.convertee.UnitTypeContainer;
import com.sbsc.convertee.adapter.QuickConvertAdapter;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnit;
import com.sbsc.convertee.entities.adapteritems.QuickConvertUnit;
import com.sbsc.convertee.entities.unittypes.generic.UnitType;
import com.sbsc.convertee.ui.converter.UnitConverterFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class QuickConverterFragment extends Fragment {

    // ViewModel
    private QuickConverterViewModel quickConverterViewModel;

    // Shared Preferences
    private SharedPreferences sharedPref;

    // QuickConversion
    private RecyclerView rvQuickConvert;
    protected QuickConvertAdapter rvQuickConvertAdapter;
    private Set<String> quickConverterItems;

    // View
    private View root;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        sharedPref = PreferenceManager
                .getDefaultSharedPreferences(requireContext());

        quickConverterViewModel = new ViewModelProvider(this).get(QuickConverterViewModel.class);

        requireActivity().setTitle( getString(R.string.app_name) );

        root = inflater.inflate(R.layout.fragment_quickconverter, container, false);

        initRecyclerViewQuickConvertList( root );
        loadQuickConvertUnits();
        initAddQuickConvertUnitButton( root );
        setupViewModelBindings();

        return root;
    }

    /**
     * Initialize recycler view
     */
    private void initRecyclerViewQuickConvertList( View root ){
        rvQuickConvert = root.findViewById(R.id.rvQuickConvert);

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
        ImageButton btnQuickConvertAdd = root.findViewById(R.id.btnQuickConvertAdd);
        btnQuickConvertAdd.setOnClickListener(view -> {
            MainActivity mainActivity = (MainActivity) requireActivity();
            mainActivity.openQuickConvertEditor( null );
        });
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
    }

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
        commitQuickConvertItems();
    }

    public void startEditingQuickConvertUnit( QuickConvertUnit quickConvertUnit ){
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.openQuickConvertEditor( quickConvertUnit );
    }

    /**
     * Remove a specific unit from shared preferences
     * @param quickConvertUnit unit to remove
     */
    public void removeQuickConvertUnit( QuickConvertUnit quickConvertUnit ){
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

    public void openUnitTypeExtended( String unitTypeKey ){
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.openUnitConverter( unitTypeKey );
    }

    public SharedPreferences getSharedPref() { return sharedPref; }
    public View getRoot() { return root; }
    public QuickConverterViewModel getQuickConverterViewModel() { return quickConverterViewModel; }
}
package com.sbsc.convertee.ui.overview;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sbsc.convertee.MainActivity;
import com.sbsc.convertee.R;
import com.sbsc.convertee.UnitTypeContainer;
import com.sbsc.convertee.adapter.UnitTypeAdapter;
import com.sbsc.convertee.adapter.UnitTypeSectionedAdapter;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnitType;
import com.sbsc.convertee.tools.HelperUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class UnitOverviewFragment extends OverviewFragment {

    // List
    private RecyclerView rvCalculatedUnitList;

    // Amount of displayed favourites
    private int favouriteItemCountOnLoad;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        requireActivity().setTitle( getString(R.string.app_name) );
        favouriteItemCountOnLoad = 0;

        // Get preferences
        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(requireContext());
        favouriteUnitTypes = sharedPreferences.getStringSet( getString(R.string.preference_favourite_unit_types) , new HashSet<>());

        View root = inflater.inflate(R.layout.fragment_unit_overview, container, false);
        rvCalculatedUnitList = root.findViewById(R.id.rvUnitType);

        initRecyclerViewCalcUnitList();

        return root;
    }

    /**
     * Get a list of all unit types, hidden ones are filtered out and favourites are marked as favourite
     * @return List of LocalizedUnitTypes
     */
    private List<LocalizedUnitType> getFilteredUnitTypes(){

        List<LocalizedUnitType> tempList = new ArrayList<>(Arrays.asList(UnitTypeContainer.localizedUnitTypes));
        favouriteItemCountOnLoad = 0;

        // Iterate list of all existing unit types
        for (Iterator<LocalizedUnitType> iterator = tempList.iterator(); iterator.hasNext(); ) {
            LocalizedUnitType unitType = iterator.next();

            // If the current unit type is favourite
            if( favouriteUnitTypes.contains(unitType.getUnitTypeKey()) ){
                unitType.setFavourite( true );
                favouriteItemCountOnLoad++;
            }else{
                unitType.setFavourite( false );
            }

        }
        return tempList;
    }

    /*
     * ======================================== UI ELEMENTS ========================================
     */

    private void initRecyclerViewCalcUnitList(){
        // LayoutManager
        LinearLayoutManager rvListLayoutManager = new LinearLayoutManager( getContext() );
        // RecyclerView
        rvCalculatedUnitList.setLayoutManager(rvListLayoutManager);
        rvCalculatedUnitList.addItemDecoration(new DividerItemDecoration(requireContext() ,LinearLayoutManager.VERTICAL));

        // Item Adapter
        rvUnitTypeAdapter = new UnitTypeAdapter( getFilteredUnitTypes() , this , false ,requireContext() );

        // Section Adapter
        List<UnitTypeSectionedAdapter.Section> sections = new ArrayList<>();
        sections.add( new UnitTypeSectionedAdapter.Section(0, getString(R.string.unit_type_categories_favourites) ));
        sections.add( new UnitTypeSectionedAdapter.Section( favouriteItemCountOnLoad , getString(R.string.unit_type_categories_all) ));

        UnitTypeSectionedAdapter.Section[] dummy = new UnitTypeSectionedAdapter.Section[sections.size()];
        rvUnitTypeSectionedAdapter = new UnitTypeSectionedAdapter( requireContext() , R.layout.adapter_section, R.id.section_text , rvUnitTypeAdapter );
        rvUnitTypeSectionedAdapter.setSections(sections.toArray(dummy));
        rvCalculatedUnitList.setAdapter( rvUnitTypeSectionedAdapter );
    }

    /*
     * =========================================== EVENTS ==========================================
     */

    /**
     * Set a unit type to favourites
     * @param unitTypeKey String
     */
    @Override
    public void favouriteUnitType( String unitTypeKey ){
        favouriteUnitTypes.add(unitTypeKey);
        rvUnitTypeSectionedAdapter.addFavourite();
        // Remove before we add the new list, otherwise it doesn't write
        sharedPreferences.edit().remove(getString(R.string.preference_favourite_unit_types)).apply();
        sharedPreferences.edit().putStringSet( getString(R.string.preference_favourite_unit_types) , favouriteUnitTypes ).apply();
    }

    /**
     * Remove a unit type from favourites
     * @param unitTypeKey String
     */
    @Override
    public void unfavouriteUnitType( String unitTypeKey ){
        favouriteUnitTypes.remove(unitTypeKey);
        rvUnitTypeSectionedAdapter.removeFavourite();
        // Remove before we add the new list, otherwise it doesn't write
        sharedPreferences.edit().remove(getString(R.string.preference_favourite_unit_types)).apply();
        sharedPreferences.edit().putStringSet( getString(R.string.preference_favourite_unit_types) , favouriteUnitTypes ).apply();
    }

    /**
     * Handle click on a unit type -> Open that unit type in converter
     * @param unitTypeKey String
     */
    @Override
    public void handleUnitTypeClick(String unitTypeKey ){
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.openUnitConverter( unitTypeKey );
    }

    @Override
    public void onResume() {
        super.onResume();
        HelperUtil.hideKeyboard( requireActivity() );

        if(wasPaused){
            wasPaused = false;

            Set<String> tempHidden = sharedPreferences.getStringSet( getString(R.string.preference_hidden_unit_types) , new HashSet<>());
            Set<String> tempFave = sharedPreferences.getStringSet( getString(R.string.preference_favourite_unit_types) , new HashSet<>());

            if( tempFave != favouriteUnitTypes ){
                favouriteUnitTypes = tempFave;

                // initRecyclerViewCalcUnitList();
                rvUnitTypeAdapter.setUnitTypes( getFilteredUnitTypes() );
                // Section Adapter
                List<UnitTypeSectionedAdapter.Section> sections = new ArrayList<>();
                sections.add( new UnitTypeSectionedAdapter.Section(0, getString(R.string.unit_type_categories_favourites) ));
                sections.add( new UnitTypeSectionedAdapter.Section( favouriteItemCountOnLoad , getString(R.string.unit_type_categories_all) ));
                UnitTypeSectionedAdapter.Section[] dummy = new UnitTypeSectionedAdapter.Section[sections.size()];
                rvUnitTypeSectionedAdapter.setSections(sections.toArray(dummy));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        wasPaused = true;
    }

    public void log(Object text){
        Log.d("Log:HomeFragment",""+text);
    }

    // Getter
    @Override
    public UnitTypeSectionedAdapter getRvUnitTypeSectionedAdapter() { return rvUnitTypeSectionedAdapter; }
}

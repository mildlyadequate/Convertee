package com.sbsc.convertee.ui.typeslist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sbsc.convertee.MainActivity;
import com.sbsc.convertee.R;
import com.sbsc.convertee.entities.UnitTypeContainer;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnit;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnitType;
import com.sbsc.convertee.entities.unittypes.generic.UnitType;
import com.sbsc.convertee.entities.unittypes.generic.UnitTypeEntry;
import com.sbsc.convertee.tools.HelperUtil;
import com.sbsc.convertee.ui.adapter.UnitTypeAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UnitTypesFragment extends Fragment{

    // Preferences
    private SharedPreferences sharedPreferences;

    // Whether the tab has already been initialized, was paused, and is now opened again
    private boolean wasPaused = false;

    // RecyclerViewAdapters
    private UnitTypeAdapter rvUnitTypeAdapter;

    private List<String> favouriteUnitTypeKeys;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        requireActivity().setTitle( getString(R.string.app_name) );

        // Get preferences
        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(requireContext());

        // Load favourites
        favouriteUnitTypeKeys = new ArrayList<>();

        View root = inflater.inflate(R.layout.fragment_unit_types, container, false);

        initRecyclerViewCalcUnitList( root );

        return root;
    }

    /**
     * Get a list of all unit types, hidden ones are filtered out and favourites are marked as favourite
     * @return List of LocalizedUnitTypes
     */
    private List<LocalizedUnitType> getFilteredUnitTypes(){
        List<LocalizedUnitType> tempList = new ArrayList<>(Arrays.asList(UnitTypeContainer.localizedUnitTypes));
        loadFavouriteUnitTypes();

        // Iterate list of all existing unit types
        for (LocalizedUnitType unitType : tempList) {

            // If the current unit type is favourite
            unitType.setFavourite(favouriteUnitTypeKeys.contains(unitType.getUnitTypeKey()));

            List<String> searchTagList = new ArrayList<>();
            for( UnitTypeEntry entry : UnitType.filterUnits( new HashSet<>() , unitType.getUnitTypeObject() )){
                LocalizedUnit localizedUnit = entry.localize(requireContext());
                searchTagList.add( localizedUnit.getLocalizedName() );
                searchTagList.add( localizedUnit.getNameShort() );
            }
            unitType.setTags( searchTagList );
        }
        return tempList;
    }

    /**
     * Get all quick convert units as favourites
     */
    private void loadFavouriteUnitTypes(){
        Set<String> quickConverterItems = sharedPreferences.getStringSet( "QuickConvertItems" , new HashSet<>() );
        favouriteUnitTypeKeys.clear();

        for ( String s : quickConverterItems ) {
            String[] values = s.split("::");
            favouriteUnitTypeKeys.add(values[0]);
        }
    }

    /*
     * ======================================== UI ELEMENTS ========================================
     */

    private void initRecyclerViewCalcUnitList( View root ){
        // LayoutManager
        LinearLayoutManager rvListLayoutManager = new LinearLayoutManager( getContext() );
        // RecyclerView
        RecyclerView rvCalculatedUnitList = root.findViewById(R.id.rvUnitType);
        rvCalculatedUnitList.setLayoutManager(rvListLayoutManager);
        rvCalculatedUnitList.addItemDecoration(new DividerItemDecoration(requireContext() ,LinearLayoutManager.VERTICAL));

        // Item Adapter
        rvUnitTypeAdapter = new UnitTypeAdapter( getFilteredUnitTypes() , this , requireContext() );

        // Section Adapter
        // The correct order is set in UnitTypeContainer
        //List<UnitTypeSectionedAdapter.Section> sections = new ArrayList<>();
        //sections.add( new UnitTypeSectionedAdapter.Section(0, getString(R.string.unit_type_categories_basics) ));
        //sections.add( new UnitTypeSectionedAdapter.Section( 4 , getString(R.string.unit_type_categories_living) ));
        //sections.add( new UnitTypeSectionedAdapter.Section( 10 , getString(R.string.unit_type_categories_science) ));
        //sections.add( new UnitTypeSectionedAdapter.Section( 14 , getString(R.string.unit_type_categories_maths) ));
        //sections.add( new UnitTypeSectionedAdapter.Section( 15 , getString(R.string.unit_type_categories_technology) ));

        //UnitTypeSectionedAdapter.Section[] dummy = new UnitTypeSectionedAdapter.Section[sections.size()];
        //rvUnitTypeSectionedAdapter = new UnitTypeSectionedAdapter(requireContext(), R.layout.adapter_section, R.id.section_text, rvUnitTypeAdapter);
        //rvUnitTypeSectionedAdapter.setSections(sections.toArray(dummy));

        rvCalculatedUnitList.setAdapter(rvUnitTypeAdapter);
    }

    /*
     * =========================================== EVENTS ==========================================
     */

    /**
     * Handle click on a unit type -> Open that unit type in converter
     * @param unitTypeKey String
     */
    public void handleUnitTypeClick(String unitTypeKey ){
        MainActivity mainActivity = (MainActivity) requireActivity();
        HelperUtil.hideKeyboard(mainActivity);
        mainActivity.openUnitConverter( unitTypeKey );
    }

    @Override
    public void onResume() {
        super.onResume();
        HelperUtil.hideKeyboard( requireActivity() );

        if(wasPaused){
            wasPaused = false;
            rvUnitTypeAdapter.setUnitTypes( getFilteredUnitTypes() );
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        wasPaused = true;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main_options_search, menu);

        final MenuItem searchItem = menu.findItem(R.id.sv_unit_type_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //TODO implement search

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rvUnitTypeAdapter.getFilter().filter( newText );
                return false;
            }
        });

    }

}

package com.sbsc.convertee.ui.settings;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.google.android.material.snackbar.Snackbar;
import com.sbsc.convertee.R;
import com.sbsc.convertee.entities.UnitTypeContainer;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnit;
import com.sbsc.convertee.entities.unittypes.generic.UnitType;
import com.sbsc.convertee.tools.HelperUtil;
import com.sbsc.convertee.ui.converter.UnitConverterFragment;

import java.util.Set;

public class UnitSettingsFragment extends PreferenceFragmentCompat {

    private String unitTypeId = "";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.unit_preferences, rootKey);

        Bundle bundle = getArguments();
        if (bundle != null) unitTypeId = bundle.getString("selected_unit_type","");

        requireActivity().setTitle( HelperUtil.getStringResourceByName( "unit_type_name_" + unitTypeId , requireActivity() ) + " Preferences" );

        PreferenceScreen preferenceScreen = this.getPreferenceScreen();

        // HIDDEN
        MultiSelectListPreference prefHiddenUnitsPicker = new MultiSelectListPreference( requireContext() );
        prefHiddenUnitsPicker.setKey( "preference_" + unitTypeId + "_hidden");
        prefHiddenUnitsPicker.setTitle( getString(R.string.unit_settings_hidden_title) );
        makeMultiSelectUnitPref( prefHiddenUnitsPicker , UnitTypeContainer.getUnitType(unitTypeId) );
        preferenceScreen.addPreference(prefHiddenUnitsPicker);

        updateUnitPickerSummary( prefHiddenUnitsPicker.getValues().size() , prefHiddenUnitsPicker);


    }

    private void makeMultiSelectUnitPref(MultiSelectListPreference multiSelect , UnitType unitType){
        LocalizedUnit[] locDistances = UnitConverterFragment.loadLocalizedUnitNames( getContext(), unitType.entrySetAsArray() );
        String[] values = new String[locDistances.length];
        String[] entries = new String[locDistances.length];
        for( int i=0; i<locDistances.length;i++){
            values[i] = ""+locDistances[i].getUnitKey();
            entries[i] = locDistances[i].getLocalizedName();
        }
        multiSelect.setEntries(entries);
        multiSelect.setEntryValues(values);

        multiSelect.setOnPreferenceChangeListener((preference, newValue) -> {
            final int maxSelected = locDistances.length-1;
            int itemSelectedCount = maxSelected;
            if( newValue instanceof Set<?>) itemSelectedCount = ((Set<?>) newValue).size();

            // At least 2 units must be available
            if( itemSelectedCount >= maxSelected){
                Snackbar.make( requireView() , getString(R.string.pref_select_units_hidden_error) , Snackbar.LENGTH_SHORT).show();
                return false;
            }

            updateUnitPickerSummary( itemSelectedCount , preference );
            return true;
        });
    }

    /**
     * Update Summary of UnitPicker
     * @param newValue - amount of hidden units
     */
    private void updateUnitPickerSummary(int newValue , Preference pref ){
        pref.setSummary( newValue+" "+getString(R.string.pref_select_units_hidden_subtitle));
    }

    @Override
    public void onResume() {
        super.onResume();

        // Hide keyboard if open
        HelperUtil.hideKeyboard(requireActivity());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        addPreferencesFromResource(R.xml.unit_preferences);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

}
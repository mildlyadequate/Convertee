package com.sbsc.convertee.ui.settings;

import android.os.Bundle;

import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.snackbar.Snackbar;
import com.sbsc.convertee.R;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnit;
import com.sbsc.convertee.entities.unittypes.Distance;
import com.sbsc.convertee.entities.unittypes.Numerative;
import com.sbsc.convertee.entities.unittypes.ShoeSize;
import com.sbsc.convertee.entities.unittypes.Temperature;
import com.sbsc.convertee.entities.unittypes.Time;
import com.sbsc.convertee.entities.unittypes.Volume;
import com.sbsc.convertee.entities.unittypes.Weight;
import com.sbsc.convertee.entities.unittypes.generic.UnitType;
import com.sbsc.convertee.tools.HelperUtil;
import com.sbsc.convertee.ui.converter.UnitConverterFragment;

import java.util.Set;

public class UnitSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.unit_preferences, rootKey);

        // DISTANCE PREFERENCES
        MultiSelectListPreference prefDistancePicker = findPreference(getString(R.string.preference_distance_hidden));
        makeMultiSelectUnitPref( prefDistancePicker , Distance.getInstance() );
        // WEIGHT PREFERENCES
        MultiSelectListPreference prefWeightPicker = findPreference(getString(R.string.preference_weight_hidden));
        makeMultiSelectUnitPref( prefWeightPicker , Weight.getInstance() );
        // TEMPERATURE PREFERENCES
        MultiSelectListPreference prefTemperaturePicker = findPreference(getString(R.string.preference_temperature_hidden));
        makeMultiSelectUnitPref( prefTemperaturePicker , Temperature.getInstance() );
        // TIME PREFERENCES
        MultiSelectListPreference prefTimePicker = findPreference(getString(R.string.preference_time_hidden));
        makeMultiSelectUnitPref( prefTimePicker , Time.getInstance() );
        // SHOE SIZE PREFERENCES
        MultiSelectListPreference prefShoeSizePicker = findPreference(getString(R.string.preference_shoesize_hidden));
        makeMultiSelectUnitPref( prefShoeSizePicker , ShoeSize.getInstance() );
        // NUMBER SYSTEM PREFERENCES
        MultiSelectListPreference prefNumerativePicker = findPreference(getString(R.string.preference_numerative_hidden));
        makeMultiSelectUnitPref( prefNumerativePicker , Numerative.getInstance() );
        // VOLUME PREFERENCES
        MultiSelectListPreference prefVolumePicker = findPreference(getString(R.string.preference_volume_hidden));
        makeMultiSelectUnitPref( prefVolumePicker , Volume.getInstance() );
    }

    private void makeMultiSelectUnitPref(MultiSelectListPreference multiSelect , UnitType unitType){
        LocalizedUnit[] locDistances = UnitConverterFragment.loadLocalizedUnitNames( getContext(), unitType.entrySetAsArray() );
        String[] values = new String[locDistances.length];
        String[] entries = new String[locDistances.length];
        for( int i=0; i<locDistances.length;i++){
            values[i] = ""+locDistances[i].getUnitName();
            entries[i] = locDistances[i].getLocalizedName();
        }
        multiSelect.setEntries(entries);
        multiSelect.setEntryValues(values);
        updateUnitPickerSummary( multiSelect.getValues().size() , multiSelect);

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
}
package com.sbsc.convertee.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Toast;

import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;
import com.sbsc.convertee.R;
import com.sbsc.convertee.UnitTypeContainer;
import com.sbsc.convertee.calculator.Calculator;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnit;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnitType;
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

import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SharedPreferences sharedPref;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.app_preferences, rootKey);

        requireActivity().setTitle( getString(R.string.app_settings) );

        // Preferences
        sharedPref = PreferenceManager.getDefaultSharedPreferences( requireContext() );

        // ROUND VALUE PREFERENCE
        EditTextPreference prefRoundValue = findPreference(getString(R.string.preference_round_value));
        if( prefRoundValue != null ) makeRoundValuePref( prefRoundValue );

        // NUMBER LOCALE PREFERENCE
        ListPreference prefNumberLocale = findPreference(getString(R.string.preference_locale));
        if( prefNumberLocale != null ) makeNumberLocalePref( prefNumberLocale );

        // HIDDEN UNIT TYPES PREFERENCE
        MultiSelectListPreference prefHiddenUnitTypes = (MultiSelectListPreference) findPreference( getString(R.string.preference_hidden_unit_types) );
        makeMultiSelectUnitTypePref( prefHiddenUnitTypes );



        // ACTION RESET
        EditTextPreference prefActionReset = (EditTextPreference) findPreference(getString(R.string.preference_action_reset));
        if( prefActionReset != null ) makeResetButton( prefActionReset );

        // CREDITS
        Preference prefCredits = (Preference) findPreference(getString(R.string.preference_credits));
        if( prefCredits != null ) makeCredits( prefCredits );
    }

    /*
     * ======================================= UI Factory ==========================================
     */

    private void makeRoundValuePref( EditTextPreference roundValuePref ){
        roundValuePref.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER));
        roundValuePref.setOnPreferenceChangeListener((preference, newValue) -> {
            if(Integer.parseInt((String)newValue) > 15 || Integer.parseInt((String)newValue) < 1){
                Toast.makeText(getContext(),getString(R.string.pref_round_value_picker_error),Toast.LENGTH_SHORT).show();
                return false;
            }
            Calculator.roundToDigits = Integer.parseInt((String) newValue);
            updateRoundValueSummary( ""+newValue , preference);
            return true;
        });
        updateRoundValueSummary( roundValuePref.getText() , roundValuePref);
    }

    private void makeNumberLocalePref(ListPreference numberLocalePref ){
        numberLocalePref.setOnPreferenceChangeListener((preference, newValue) -> {
            String selection = (String) newValue;
            Calculator.setLocale( selection , requireContext() );
            updateNumberLocalePickerSummary( selection , preference );
            return true;
        });
        updateNumberLocalePickerSummary( numberLocalePref.getValue() , numberLocalePref );
    }

    private void makeMultiSelectUnitTypePref( MultiSelectListPreference multiSelect ){
        LocalizedUnitType[] unitTypes = UnitTypeContainer.localizedUnitTypes;
        String[] values = new String[unitTypes.length];
        String[] entries = new String[unitTypes.length];
        for( int i=0; i<unitTypes.length;i++){
            values[i] = ""+unitTypes[i].getUnitTypeKey();
            entries[i] = unitTypes[i].getUnitTypeName();
        }
        multiSelect.setEntries(entries);
        multiSelect.setEntryValues(values);
        multiSelect.setOnPreferenceChangeListener((preference, newValue) -> {
            final int maxSelected = unitTypes.length;
            int itemSelectedCount = maxSelected;
            if( newValue instanceof Set<?>) itemSelectedCount = ((Set<?>) newValue).size();

            // At least 2 units must be available
            if( itemSelectedCount >= maxSelected){
                Snackbar.make( requireView() , getString(R.string.pref_hidden_unit_types_error) , Snackbar.LENGTH_SHORT).show();
                return false;
            }

            updateUnitTypeHiddenPickerSummary( itemSelectedCount , preference );
            return true;
        });
        updateUnitTypeHiddenPickerSummary( multiSelect.getValues().size() , multiSelect );
    }

    private void makeResetButton( EditTextPreference resetButton ){
        resetButton.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_TEXT));
        resetButton.setSummary(
                getString(R.string.pref_action_reset_subtitle)+
                getString(R.string.pref_action_reset_targetword)+
                getString(R.string.pref_action_reset_subtitle2)
        );
        resetButton.setOnPreferenceChangeListener((preference, newValue) -> {
            if( StringUtils.equalsIgnoreCase( (String)newValue , getString(R.string.pref_action_reset_targetword) ) ){
                Toast.makeText( getContext() , getString(R.string.pref_action_reset_success) , Toast.LENGTH_SHORT ).show();
                sharedPref.edit().clear().apply();
                requireActivity().getIntent().setFlags((Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
                return false;
            }
            Toast.makeText( getContext() , getString(R.string.pref_action_reset_error) , Toast.LENGTH_SHORT ).show();
            return false;
        });
    }

    private void makeCredits( Preference pref ){
        pref.setOnPreferenceClickListener(preference -> {

            // Open Link in Browser (My twitter Link currently)
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.pref_credits_link)));
            startActivity(browserIntent);
            return false;
        });
    }

    /*
     * ======================================= Update Values =======================================
     */

    /**
     * Update Summary of RoundValue
     * @param newValue - amount of digits being rounded towards
     */
    private void updateRoundValueSummary( String newValue , Preference pref){
        pref.setSummary( getString(R.string.pref_round_value_picker_subtitle)+" "+newValue+" "+getString(R.string.pref_round_value_picker_subtitle2));
    }

    /**
     * Update Summary of NumberLocale Picker
     * @param newValue - amount of hidden units
     */
    private void updateNumberLocalePickerSummary( String newValue , Preference pref ){
        pref.setSummary(getString(R.string.pref_locale_subtitle) + " " + newValue);
    }

    /**
     * Update Summary of DistancePicker
     * @param newValue - amount of hidden units
     */
    private void updateUnitTypeHiddenPickerSummary( int newValue , Preference pref ){
        pref.setSummary( newValue+" "+getString(R.string.pref_hidden_unit_types_subtitle));
    }

    @Override
    public void onResume() {
        super.onResume();

        // Hide keyboard if open
        HelperUtil.hideKeyboard(requireActivity());
    }
}
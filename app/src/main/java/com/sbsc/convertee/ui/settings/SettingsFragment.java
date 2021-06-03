package com.sbsc.convertee.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;

import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;
import com.sbsc.convertee.MainActivity;
import com.sbsc.convertee.R;
import com.sbsc.convertee.calculator.Calculator;
import com.sbsc.convertee.tools.HelperUtil;
import com.sbsc.convertee.ui.intro.AppIntroActivity;

import org.apache.commons.lang3.StringUtils;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SharedPreferences sharedPref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.app_preferences, rootKey);

        requireActivity().setTitle( getString(R.string.app_settings) );

        // Preferences
        sharedPref = PreferenceManager.getDefaultSharedPreferences( requireContext() );

        // ROUND VALUE PREFERENCE
        EditTextPreference prefRoundValue = findPreference(getString(R.string.preference_round_value));
        if( prefRoundValue != null ) makeRoundValuePref( prefRoundValue );

        // LANGUAGE PREFERENCE
        ListPreference prefLanguage = findPreference(getString(R.string.preference_language));
        if( prefLanguage != null ) makeLanguagePref( prefLanguage );

        // NUMBER LOCALE PREFERENCE
        ListPreference prefNumberLocale = findPreference(getString(R.string.preference_locale));
        if( prefNumberLocale != null ) makeNumberLocalePref( prefNumberLocale );

        // ACTION RESET
        EditTextPreference prefActionReset = findPreference(getString(R.string.preference_action_reset));
        if( prefActionReset != null ) makeResetButton( prefActionReset );

        // CREDITS
        Preference prefCredits = findPreference(getString(R.string.preference_credits));
        if( prefCredits != null ) makeCredits( prefCredits );
    }

    /*
     * ======================================= UI Factory ==========================================
     */

    private void makeRoundValuePref( EditTextPreference roundValuePref ){
        roundValuePref.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER));
        roundValuePref.setOnPreferenceChangeListener((preference, newValue) -> {
            if(Integer.parseInt((String)newValue) > 15 || Integer.parseInt((String)newValue) < 1){
                Snackbar.make( requireView() , getString(R.string.pref_round_value_picker_error) , Snackbar.LENGTH_SHORT ).show();
                return false;
            }
            Calculator.roundToDigits = Integer.parseInt((String) newValue);
            updateRoundValueSummary( ""+newValue , preference);
            return true;
        });
        updateRoundValueSummary( roundValuePref.getText() , roundValuePref);
    }

    private void makeLanguagePref( ListPreference languagePref ){
        languagePref.setOnPreferenceChangeListener((preference, newValue) -> {
            Intent intent = new Intent(requireContext(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
            return true;
        });
        int index = 0;
        for( int i=0; i<languagePref.getEntryValues().length;i++ ){
            if( languagePref.getEntryValues()[i].equals(languagePref.getValue()) ){
                index = i; break;
            }
        }
        languagePref.setSummary( getString(R.string.pref_language_subtitle)+" "+languagePref.getEntries()[index].toString());
    }

    private void makeNumberLocalePref( ListPreference numberLocalePref ){
        numberLocalePref.setOnPreferenceChangeListener((preference, newValue) -> {
            String selection = (String) newValue;
            Calculator.setLocale( selection , requireContext() );
            int index = 0;
            for( int i=0; i<numberLocalePref.getEntryValues().length;i++ ){
                if( numberLocalePref.getEntryValues()[i].equals(newValue) ){
                    index = i; break;
                }
            }
            updateNumberLocalePickerSummary( numberLocalePref.getEntries()[index].toString(), preference );
            return true;
        });
        int index = 0;
        for( int i=0; i<numberLocalePref.getEntryValues().length;i++ ){
            if( numberLocalePref.getEntryValues()[i].equals(numberLocalePref.getValue()) ){
                index = i; break;
            }
        }
        updateNumberLocalePickerSummary( numberLocalePref.getEntries()[index].toString(), numberLocalePref );
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
                Snackbar.make( requireView() , getString(R.string.pref_action_reset_success) , Snackbar.LENGTH_SHORT ).show();
                sharedPref.edit().clear().apply();
                requireActivity().getIntent().setFlags((Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
                return false;
            }
            Snackbar.make( requireView() , getString(R.string.pref_action_reset_error) , Snackbar.LENGTH_SHORT ).show();
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
        pref.setSummary(getString(R.string.pref_number_locale_subtitle) + " " + newValue);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Hide keyboard if open
        HelperUtil.hideKeyboard(requireActivity());
    }
}
package com.sbsc.convertee.ui.appsetup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.sbsc.convertee.R;
import com.sbsc.convertee.calculator.Calculator;
import com.sbsc.convertee.databinding.FragmentSetup2FormatBinding;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.Locale;

/**
 * Step 2 in the Setup, set the number formatting and whether or not promode should be activated
 */
public class AppSetupFormatFragment extends Fragment {

    private FragmentSetup2FormatBinding binding;
    private SharedPreferences sharedPref;

    private NumberPicker formatPicker;
    private SwitchCompat swtProMode;

    private boolean firstStart = true;

    public static AppSetupFormatFragment newInstance() {
        return new AppSetupFormatFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentSetup2FormatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Shared Preferences
        sharedPref = PreferenceManager.getDefaultSharedPreferences( requireContext() );

        // Switch
        swtProMode = binding.swtProMode;
        swtProMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updatePreview();
            sharedPref.edit().putBoolean( getString(R.string.preference_pro_mode) , isChecked ).apply();
        });
        sharedPref.edit().putBoolean( getString(R.string.preference_pro_mode) , false ).apply();

        // Picker
        formatPicker = binding.picker;
        String[] formatsArr = getResources().getStringArray(R.array.setup_format_entries);
        formatPicker.setMinValue(1);
        formatPicker.setMaxValue(formatsArr.length);
        formatPicker.setValue(0);
        formatPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        formatPicker.setDisplayedValues(formatsArr);
        formatPicker.setOnValueChangedListener( (obj, oldVal, newVal) -> {
            updatePreview();
            saveSelectedFormat( newVal - 1 );
        } );

        initializeFormat();
        updateLocale();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if( firstStart ){
            firstStart = false;
        }else{
            initializeFormat();
            updateLocale();
        }

    }

    private void updateLocale(){
        binding.tvInitialSetup.setText( getString(R.string.setup_label_title) );
        binding.tvSelectFormat.setText( R.string.setup_label_select_format );
        binding.tvFormatPreviewLabel.setText( R.string.setup_format_previewlabel );
        binding.swtProMode.setText( R.string.setup_format_promode_desc );
        updatePreview();
    }

    /**
     * Set the Format based on the picked language
     */
    private void initializeFormat(){
        String language = sharedPref.getString( getString(R.string.preference_language) , "system" );
        if( language.equals("en") ){
            formatPicker.setValue(1);
        }else if( language.equals("de")){
            formatPicker.setValue(2);
        }else{
            formatPicker.setValue(0);
        }
        saveSelectedFormat( formatPicker.getValue() - 1 );
    }

    /**
     * Save the format to preferences and into Calculator Object
     * @param index of the picked format inside R.array.pref_number_format_locales_values
     */
    private void saveSelectedFormat( int index ){
        String[] values = getResources().getStringArray(R.array.pref_number_format_locales_values);
        if( values[index].equalsIgnoreCase( getString(R.string.pref_number_locale_none) ) )
            Calculator.locale = null;
        else
            Calculator.locale = Locale.forLanguageTag( values[index] );

        sharedPref.edit().putString( getString(R.string.preference_locale) , values[index] ).apply();
    }

    /**
     * Preview shows what a String of a number formatted with current settings would look like
     */
    private void updatePreview(){
        String text = getResources().getStringArray(R.array.setup_format_previews)[formatPicker.getValue() - 1];
        if(swtProMode.isChecked())
            text += " "+getString(R.string.distance_unit_km_short);
        else
            text += " "+getString(R.string.distance_unit_km);
        binding.tvFormatPreview.setText( text );
    }

}
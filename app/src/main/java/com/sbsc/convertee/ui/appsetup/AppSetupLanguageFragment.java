package com.sbsc.convertee.ui.appsetup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.sbsc.convertee.AppSetupActivity;
import com.sbsc.convertee.R;
import com.sbsc.convertee.databinding.FragmentSetup1LanguageBinding;
import com.sbsc.convertee.tools.CompatibilityHandler;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.Locale;

/**
 * Step 1 in the Setup, set the language the app should be displayed in
 */
public class AppSetupLanguageFragment extends Fragment {

    private FragmentSetup1LanguageBinding binding;

    private NumberPicker langPicker;
    private TextView tvSelectLanguage;

    public static AppSetupLanguageFragment newInstance() {
        return new AppSetupLanguageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentSetup1LanguageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Label
        tvSelectLanguage = binding.tvSelectLanguage;

        // Default Locale
        if( CompatibilityHandler.defaultLocale == null ) CompatibilityHandler.defaultLocale = Locale.getDefault();
        String systemLocale = CompatibilityHandler.defaultLocale.getLanguage();

        // Shared Preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences( requireContext() );

        // Picker
        langPicker = binding.picker;
        String[] supportedLanguageCodes = getResources().getStringArray(R.array.pref_language_values);
        String[] languagesArr = getResources().getStringArray(R.array.setup_languages);
        langPicker.setMinValue(1);
        langPicker.setMaxValue(languagesArr.length);
        langPicker.setValue(0);
        langPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        langPicker.setDisplayedValues(languagesArr);
        langPicker.setOnValueChangedListener( (obj, oldVal, newVal) -> {

            CompatibilityHandler.setLocale( requireActivity() , supportedLanguageCodes[newVal-1] );
            updateResourcesLocale();
            sharedPref.edit().putString( getString(R.string.preference_language) , supportedLanguageCodes[newVal-1] ).apply();

        });

        // Select default locale in picker
        int initialSelection = 0;
        for( int i=0; i<supportedLanguageCodes.length; i++ ){
            if( supportedLanguageCodes[i].equalsIgnoreCase( systemLocale ) ) initialSelection = i;
        }
        langPicker.setValue(initialSelection+1);
        sharedPref.edit().putString( getString(R.string.preference_language) , supportedLanguageCodes[initialSelection] ).apply();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Update resources text, use when language was changed or to initialize
     */
    private void updateResourcesLocale(){

        binding.tvInitialSetup.setText( getString(R.string.setup_label_title) );
        tvSelectLanguage.setText(R.string.setup_label_select_language);

        String[] languagesArr = getResources().getStringArray(R.array.setup_languages);
        langPicker.setDisplayedValues(languagesArr);

        AppSetupActivity activity = (AppSetupActivity) getActivity();
        if (activity != null) {
            activity.updateButtonLocale();
        }

    }

}
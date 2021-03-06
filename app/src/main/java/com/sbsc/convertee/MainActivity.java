package com.sbsc.convertee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.sbsc.convertee.calculator.Calculator;
import com.sbsc.convertee.entities.UnitTypeContainer;
import com.sbsc.convertee.entities.adapteritems.QuickConvertUnit;
import com.sbsc.convertee.tools.CompatibilityHandler;
import com.sbsc.convertee.tools.DefaultHiddenUnits;
import com.sbsc.convertee.tools.keyboards.CustomKeyboard;
import com.sbsc.convertee.tools.keyboards.KeyboardHandler;
import com.sbsc.convertee.ui.TabbedMenuFragment;
import com.sbsc.convertee.ui.converter.UnitConverterFragment;
import com.sbsc.convertee.ui.intro.AppIntroActivity;
import com.sbsc.convertee.ui.quickconverter.QuickConvertEditorFragment;
import com.sbsc.convertee.ui.settings.SettingsFragment;
import com.sbsc.convertee.ui.settings.UnitSettingsFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private String activeUnitConverterKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load Shared Pref
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(this);

        // Set language to preference
        if( CompatibilityHandler.defaultLocale == null ) CompatibilityHandler.defaultLocale = Locale.getDefault();
        String language = sharedPref.getString( getString(R.string.preference_language) , "system");
        if( language.equals("system") )
            CompatibilityHandler.setLocaleDefault( this );
        else
            CompatibilityHandler.setLocale( this , language );

        // TODO Remove, only for testing purposes
        // sharedPref.edit().putBoolean( "showTutorialOnStart" , true ).apply();
        // if( !sharedPref.getBoolean( "showTutorialOnStart" ,true ) ) throw new EmptyStackException();

        // Make sure application is NOT Re-Rendering for some reason eg. change of theme
        if( savedInstanceState == null ){

            // Show App Setup if its the first start
            if( sharedPref.getBoolean( "showTutorialOnStart" ,true ) ){
                Intent intent = new Intent(this, AppSetupActivity.class);
                startActivity(intent);
                finish();
                return;
            }

            // Check if this is the first time the user starts the application
            if( !sharedPref.getBoolean( getString(R.string.preference_first_start ) ,false) ){
                DefaultHiddenUnits.setDefaultHiddenUnits( this , sharedPref );
                sharedPref.edit().putBoolean( getString(R.string.preference_first_start ) , true ).apply();
            }

            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportFragmentManager().addOnBackStackChangedListener(this);

            // Load preferences
            int DEFAULT_ROUND_VALUE = 4;
            Calculator.roundToDigits = Integer.parseInt(sharedPref.getString( getString(R.string.preference_round_value) , ""+ DEFAULT_ROUND_VALUE));
            Calculator.setLocale( sharedPref.getString( getString(R.string.preference_locale) , getString(R.string.preference_number_locale_default_UK)),this );

            // Load unit types with localization
            UnitTypeContainer.getLocalizedUnitTypeArray( this );

            // Open Home Fragment as Default Starting Page
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TabbedMenuFragment() , "mainFrag").commit();

        }else{

            // Get previously opened unit type from savedInstanceState
            activeUnitConverterKey = savedInstanceState.getString("activeUnitConverter");

            // If application is Re-Rendering then restart itself and clear old stacks
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            intent.putExtra("activeUnitConverter",activeUnitConverterKey);
            intent.putExtra("activeUnitConverterTextValue",savedInstanceState.getString("activeUnitConverterTextValue"));
            intent.putExtra("activeUnitConverterSelectedIndex",savedInstanceState.getInt("activeUnitConverterSelectedIndex"));

            startActivity(intent);
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save currently opened unit type
        outState.putString("activeUnitConverter",activeUnitConverterKey);

        UnitConverterFragment frag = (UnitConverterFragment) getSupportFragmentManager().findFragmentByTag("converter");
        int selectedIndex = -1;
        String textValue = "";
        if(frag!=null) {
            textValue = frag.getCurrentTextValue();
            selectedIndex = frag.getCurrentSelectedUnitIndex();
        }
        outState.putString("activeUnitConverterTextValue", textValue );
        outState.putInt( "activeUnitConverterSelectedIndex", selectedIndex );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_options, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if( CustomKeyboard.isOpen ){

            CustomKeyboard keyboard = findViewById( KeyboardHandler.KeyboardId );
            if( keyboard != null ) keyboard.closeKeyboard();

        }else{

            int fragments = getSupportFragmentManager().getBackStackEntryCount();
            if (fragments == 0) {
                finish();
                return;
            }else if( fragments == 1){
                activeUnitConverterKey = "";
            }
            super.onBackPressed();

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getSupportFragmentManager().popBackStack();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( item.getItemId() == R.id.mni_app_settings){

            activeUnitConverterKey = "options_app_settings";
            changeFragment( new SettingsFragment() , null , "appsettings");

        }else if( item.getItemId() == R.id.mni_app_tutorial){

            Intent i = new Intent(getApplicationContext(), AppIntroActivity.class);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canGoBack = getSupportFragmentManager().getBackStackEntryCount()>0;
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(canGoBack);
    }

    private void changeFragment( Fragment fragment , Bundle bundle , String fragmentKey ){
        if( bundle != null ) fragment.setArguments( bundle );

        // I don't know how but this works exactly as I wanted it to work, backstack is limited
        getSupportFragmentManager().popBackStack( fragmentKey , FragmentManager.POP_BACK_STACK_INCLUSIVE );

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, fragmentKey);
        transaction.addToBackStack( fragmentKey );
        transaction.commit();
    }

    /*
     * =========================================== EVENTS ==========================================
     */

    /**
     * Open the unit converter of given unit type key
     * @param unitTypeKey converter type to open
     */
    public void openUnitConverter( String unitTypeKey ){
        Bundle bundle = new Bundle();
        bundle.putString( "selected_unit_type" , unitTypeKey );
        activeUnitConverterKey = unitTypeKey;
        changeFragment( new UnitConverterFragment() , bundle , "converter");
    }

    /**
     * Open the unit converter of given unit type key
     * @param unitTypeKey converter type to open
     */
    public void openUnitTypeSettings( String unitTypeKey ){
        Bundle bundle = new Bundle();
        bundle.putString( "selected_unit_type" , unitTypeKey );
        changeFragment( new UnitSettingsFragment() , bundle , "unitsettings");
    }

    /**
     * Open the Editor for QuickConvertUnits
     * @param quickConvertUnit edit this specific unit
     */
    public void openQuickConvertEditor( QuickConvertUnit quickConvertUnit ){
        Bundle bundle = new Bundle();
        if ( quickConvertUnit!= null )
            bundle.putString(
                    "QuickConvertEditorItem" ,
                    quickConvertUnit.getUnitTypeId()+"::"+quickConvertUnit.getIdUnitFrom()+"::"+quickConvertUnit.getIdUnitTo()+"::"+quickConvertUnit.getDefaultValue()
            );
        changeFragment( new QuickConvertEditorFragment() , bundle , "quickConverterEditor");
    }

    /*
     * =========================================== Getter Setter ===================================
     */
}
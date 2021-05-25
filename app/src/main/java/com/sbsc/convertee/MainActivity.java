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

import com.github.appintro.AppIntro;
import com.sbsc.convertee.calculator.Calculator;
import com.sbsc.convertee.tools.DefaultHiddenUnits;
import com.sbsc.convertee.ui.converter.UnitConverterFragment;
import com.sbsc.convertee.ui.intro.AppIntroActivity;
import com.sbsc.convertee.ui.settings.SettingsFragment;
import com.sbsc.convertee.ui.settings.UnitSettingsFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    /**TODO LIST
      - Search through unit types
      - Add two / more input edit texts for pixel to cm converter / color codes / cup size eg. https://www.pixelto.net/px-to-cm-converter
      - Make more Unit Types (Next: ,,,Brasize,Color codes,ElectricityCurrent/Charge)
     https://www.unitconverters.net/pressure-converter.html
      - Add option to special formula calculation -> instead of VAR use VAR(-someunitname-) to convert through other units first

     - TUTORIAL
     https://stackoverflow.com/questions/40440366/android-how-to-create-tutorial-at-app-start-like-in-google-analytics-app
     https://developer.android.com/training/tv/playback/onboarding
     https://stackoverflow.com/questions/56224100/how-to-create-onboarding-walkthrough-inside-first-show-screens

    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO LOOK INTO VIEWPAGER https://stackoverflow.com/questions/38877202/android-activity-with-many-fragments-the-correct-way-to-handle-lifecycle-change

        // Make sure application is NOT Re-Rendering for some reason eg. change of theme
        if( savedInstanceState == null ){

            // Load Shared Pref
            SharedPreferences sharedPref = PreferenceManager
                    .getDefaultSharedPreferences(this);

            if( sharedPref.getBoolean( "showTutorialOnStart" ,true ) ){
                sharedPref.edit().putBoolean( "showTutorialOnStart" , true ).apply();
                Intent intent = new Intent(this, AppIntroActivity.class);
                startActivity(intent);
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
            Calculator.roundToDigits = Integer.parseInt(sharedPref.getString( getString(R.string.preference_round_value) , "4"));
            Calculator.setLocale( sharedPref.getString( getString(R.string.preference_locale) , getString(R.string.preference_locale_default_UK)),this );

            // Load unit types with localization
            UnitTypeContainer.getLocalizedUnitTypeArray( this );

            // Open Home Fragment as Default Starting Page
            // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UnitOverviewFragment()).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();

        }else{
            // If application is Re-Rendering then restart itself and clear old stacks
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 0) {
            finish();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getSupportFragmentManager().popBackStack();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( item.getItemId() == R.id.app_settings){
            changeFragment( new SettingsFragment() , null , "appsettings");
        }else if( item.getItemId() == R.id.unit_settings){
            changeFragment( new UnitSettingsFragment() , null , "unitsettings");
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
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack( fragmentKey );
        transaction.commit();
    }

    /*
     * =========================================== EVENTS ==========================================
     */

    public void openUnitConverterWith( String unitTypeKey ){
        Bundle bundle = new Bundle();
        bundle.putString( getString(R.string.bundle_selected_unittype) , unitTypeKey );
        changeFragment( new UnitConverterFragment() , bundle , "converter");
    }

    /*
     * =========================================== Getter Setter ===================================
     */

}
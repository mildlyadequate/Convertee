package com.sbsc.convertee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sbsc.convertee.databinding.ActivityAppSetupBinding;
import com.sbsc.convertee.entities.unittypes.generic.UnitType;
import com.sbsc.convertee.tools.CompatibilityHandler;
import com.sbsc.convertee.tools.keyboards.CustomKeyboard;
import com.sbsc.convertee.tools.keyboards.KeyboardHandler;
import com.sbsc.convertee.ui.appsetup.AppSetupPagerAdapter;
import com.sbsc.convertee.ui.appsetup.AppSetupUnitDetailFragment;

public class AppSetupActivity extends AppCompatActivity {

    private ActivityAppSetupBinding binding;
    private TabLayout tabLayout;

    private Button btnSkip;
    private Button btnNext;

    // Selected units in step 3
    private String[] selectedUnits;

    // Keyboard
    private CustomKeyboard currentKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        binding = ActivityAppSetupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppSetupPagerAdapter appSetupPagerAdapter = new AppSetupPagerAdapter( getSupportFragmentManager() , getLifecycle() );
        ViewPager2 viewPager = binding.appSetupViewpager;
        viewPager.setAdapter(appSetupPagerAdapter);
        tabLayout = binding.appSetupTabs;

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout,viewPager,(tab, position) -> { });
        tabLayoutMediator.attach();

        btnSkip = binding.btnSetupSkip;
        btnNext = binding.btnSetupNext;

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateButtonLocale();
                hideCustomKeyboard();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        btnSkip.setOnClickListener(v -> {
            int index = tabLayout.getSelectedTabPosition();
            if( index == 0 ){
                AlertDialog.Builder alert = new AlertDialog.Builder( this );
                alert.setTitle( getString(R.string.setup_skip_title) );
                alert.setMessage( getString(R.string.setup_skip_message) );
                alert.setPositiveButton( getString(R.string.confirmation_dialog_confirm) , (dialog , which) -> {
                    sharedPref.edit().putBoolean( "showTutorialOnStart" , false ).apply();
                    Intent intent = new Intent( this , MainActivity.class);
                    startActivity(intent);
                    finish();
                });
                alert.setNegativeButton( getString(R.string.confirmation_dialog_cancel) , (dialog, which) -> dialog.dismiss());
                alert.show();

            }else{

                TabLayout.Tab tab = tabLayout.getTabAt(index-1 );
                if (tab != null) {
                    tab.select();
                }
            }
        });

        btnNext.setOnClickListener(v -> {
            int index = tabLayout.getSelectedTabPosition();
            if( index == tabLayout.getTabCount()-1 ){
                if( binding.appSetupViewpager.getCurrentItem() == 3 ){
                    AppSetupUnitDetailFragment fragment = (AppSetupUnitDetailFragment) getSupportFragmentManager()
                            .findFragmentByTag("f" + binding.appSetupViewpager.getCurrentItem() );
                    if( fragment != null ) fragment.getCurrentQuickConvertItems();
                }
                sharedPref.edit().putBoolean( "showTutorialOnStart" , false ).apply();
                Intent intent = new Intent( this , MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                TabLayout.Tab tab = tabLayout.getTabAt(index+1 );
                if (tab != null) {
                    tab.select();
                }
            }
        });
        updateButtonLocale();
    }

    @Override
    public void onBackPressed() {
        if( CustomKeyboard.isOpen ){
            CustomKeyboard keyboard = findViewById( KeyboardHandler.KeyboardId );
            keyboard.closeKeyboard();
        }else if( binding.appSetupViewpager.getCurrentItem() > 0 ){
            binding.appSetupViewpager.setCurrentItem( binding.appSetupViewpager.getCurrentItem()-1 , true);
        }else{
            super.onBackPressed();
        }
    }

    public void updateButtonLocale(){
        int index = tabLayout.getSelectedTabPosition();
        // Back Button
        if( index == 0 )
            btnSkip.setText( getString(R.string.setup_btn_skip) );
        else
            btnSkip.setText( getString(R.string.setup_btn_back) );

        // Next Button
        if( index == tabLayout.getTabCount()-1 )
            btnNext.setText( getString(R.string.setup_btn_finish) );
        else
            btnNext.setText( getString(R.string.setup_btn_next) );
    }


    /**
     * Initialize the keyboard, called from adapter
     * @param unitType of clicked Element
     * @param unitKey of selected from unit in spinner
     * @param inputSource input text field of this quick convert item
     */
    public void initCustomKeyboard( UnitType unitType , String unitKey , EditText inputSource ){

        ConstraintLayout layout = binding.clSetupContainer;

        // If keyboard is already defined, remove it from root view first
        if( currentKeyboard != null ){
            layout.removeView( currentKeyboard );
        }

        // Get required keyboard from KeyboardHandler based on unittype and unit
        InputConnection ic = inputSource.onCreateInputConnection(new EditorInfo());
        CustomKeyboard newKeyboard = KeyboardHandler.getKeyboardByType( unitType.getId() , unitKey , ic , this );

        // Return if keyboard stayed the same
        if( newKeyboard == currentKeyboard ) return; else currentKeyboard = newKeyboard;

        // If keyboard is null return
        if( currentKeyboard == null ){
            inputSource.setShowSoftInputOnFocus(false);
            return;
        }

        // OnKeyboardCloseEvent remove focus from edit text
        currentKeyboard.addKeyboardClosingListener( () -> {
            layout.requestFocus();
            hideCustomKeyboard();
        } );

        // Add keyboard to root
        layout.addView( currentKeyboard );

        // Set constraints for keyboard
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);
        constraintSet.connect(currentKeyboard.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constraintSet.connect(currentKeyboard.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
        constraintSet.connect(currentKeyboard.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);

        // Set constraints for other views
        constraintSet.connect(binding.appSetupViewpager.getId(), ConstraintSet.BOTTOM, currentKeyboard.getId(), ConstraintSet.TOP, 0);

        constraintSet.applyTo(layout);
    }

    /**
     * Shift away to root, remove keyboard from root
     */
    public void hideCustomKeyboard(){

        ConstraintLayout layout = binding.clSetupContainer;

        if( CompatibilityHandler.shouldUseCustomKeyboard() ){
            layout.requestFocus();
            if( currentKeyboard != null ){
                layout.removeView( currentKeyboard );
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(layout);
                constraintSet.connect(binding.appSetupViewpager.getId(), ConstraintSet.BOTTOM, binding.appSetupTabs.getId() , ConstraintSet.TOP, 0 );
                constraintSet.applyTo(layout);
                currentKeyboard = null;
            }
        }

    }

    public void setSelectedUnits( String[] selectedUnits ){ this.selectedUnits = selectedUnits; }
    public String[] getSelectedUnits() { return selectedUnits; }
}
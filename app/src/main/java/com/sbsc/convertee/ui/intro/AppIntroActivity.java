package com.sbsc.convertee.ui.intro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroFragment;
import com.sbsc.convertee.MainActivity;
import com.sbsc.convertee.R;
import com.sbsc.convertee.tools.CompatibilityHandler;

import org.jetbrains.annotations.Nullable;

public class AppIntroActivity extends AppIntro2 {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            addSlide(AppIntroFragment.newInstance(
                    getString(R.string.tutorial_screen1_title),
                    getString(R.string.tutorial_screen1_description),
                    R.drawable.intro_step1,
                    CompatibilityHandler.getColor(this, R.color.themePrimary)
            ));

            addSlide(AppIntroFragment.newInstance(
                    getString(R.string.tutorial_screen2_title),
                    getString(R.string.tutorial_screen2_description),
                    R.drawable.intro_step2,
                    CompatibilityHandler.getColor(this, R.color.themeBackgroundDark)
            ));

            addSlide(AppIntroFragment.newInstance(
                    getString(R.string.tutorial_screen3_title),
                    getString(R.string.tutorial_screen3_description),
                    R.drawable.intro_step3,
                    CompatibilityHandler.getColor(this, R.color.themeTertiary)
            ));

            addSlide(AppIntroFragment.newInstance(
                    getString(R.string.tutorial_screen4_title),
                    getString(R.string.tutorial_screen4_description),
                    R.drawable.intro_step4,
                    CompatibilityHandler.getColor(this, R.color.themePrimary)
            ));

            if( !CompatibilityHandler.isOldDevice() ) setColorTransitionsEnabled(true);

        }catch(OutOfMemoryError e){
            Log.d("Intro","Cringe");
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }

    }

    @Override
    protected void onSkipPressed(@Nullable Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        // TODO add AppSetup here
        // Intent i = new Intent(getApplicationContext(), AppSetupActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onDonePressed(@Nullable Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // TODO add AppSetup here
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        finish();
    }
}

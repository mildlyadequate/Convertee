package com.sbsc.convertee.ui.appsetup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class AppSetupPagerAdapter extends FragmentStateAdapter {


    public AppSetupPagerAdapter( FragmentManager fm , Lifecycle lifecycle) {
        super( fm , lifecycle );
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if( position == 0 ){
            return AppSetupLanguageFragment.newInstance();
        }else if( position == 1){
            return AppSetupFormatFragment.newInstance();
        }else if( position == 2){
            return AppSetupUnitFragment.newInstance();
        }else if( position == 3){
            return AppSetupUnitDetailFragment.newInstance();
        }
        return AppSetupLanguageFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
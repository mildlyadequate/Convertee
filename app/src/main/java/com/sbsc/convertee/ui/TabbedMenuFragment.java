package com.sbsc.convertee.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sbsc.convertee.R;
import com.sbsc.convertee.tools.HelperUtil;
import com.sbsc.convertee.ui.typeslist.UnitTypesFragment;
import com.sbsc.convertee.ui.quickconverter.QuickConverterFragment;

import org.jetbrains.annotations.NotNull;


public class TabbedMenuFragment extends Fragment {

    public TabbedMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        // Setting ViewPager for each Tabs
        ViewPager2 viewPager = root.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        // Set Tabs inside Toolbar
        TabLayout tabs = root.findViewById(R.id.mainTabs);
        new TabLayoutMediator(tabs, viewPager, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText( getString(R.string.tabbed_pane_tab_customized) ); break;
                case 1:
                    tab.setText( getString(R.string.tabbed_pane_tab_categories) ); break;
            }
        }).attach();

        // Remove the keyboard from the searchbar when switching while its open
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                HelperUtil.hideKeyboard(requireActivity());
            }
        });

        return root;
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager2 viewPager) {


        Adapter adapter = new Adapter( getChildFragmentManager() , getLifecycle() );
        adapter.createFragment(0);
        viewPager.setAdapter(adapter);

    }

    static class Adapter extends FragmentStateAdapter {

        public Adapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle ) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 0:
                    return new QuickConverterFragment();
                case 1:
                    return new UnitTypesFragment();
            }
            return new UnitTypesFragment();
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

}
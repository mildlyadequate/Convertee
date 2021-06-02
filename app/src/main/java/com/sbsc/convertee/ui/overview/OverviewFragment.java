package com.sbsc.convertee.ui.overview;

import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;

import com.sbsc.convertee.adapter.UnitTypeAdapter;
import com.sbsc.convertee.adapter.UnitTypeSectionedAdapter;

import java.util.Set;

/**
 * This is the parent class for the available tabs inside the tabbed pane, it contains variables all
 * tabs use and makes it possible to access the functions inside it without knowing which actual
 * TabbedFragment is being called.
 */
public class OverviewFragment extends Fragment {

    // Preferences
    protected SharedPreferences sharedPreferences;

    // Whether the tab has already been initialized, was paused, and is now opened again
    protected boolean wasPaused = false;

    // Preference lists
    protected Set<String> favouriteUnitTypes;

    // RecyclerViewAdapters
    protected UnitTypeAdapter rvUnitTypeAdapter;
    protected UnitTypeSectionedAdapter rvUnitTypeSectionedAdapter;

    public void favouriteUnitType( String unitTypeKey ){ }

    public void unfavouriteUnitType( String unitTypeKey ){ }

    public UnitTypeSectionedAdapter getRvUnitTypeSectionedAdapter() { return null; }

    public void handleUnitTypeClick(String unitTypeKey ){ }

}

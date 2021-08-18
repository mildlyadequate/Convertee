package com.sbsc.convertee.ui.appsetup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sbsc.convertee.AppSetupActivity;
import com.sbsc.convertee.R;
import com.sbsc.convertee.databinding.FragmentSetup3UnitsBinding;
import com.sbsc.convertee.entities.UnitTypeContainer;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnitType;
import com.sbsc.convertee.ui.adapter.SetupUnitTypeAdapter;

import java.util.Arrays;

/**
 * Step 2 in the Setup, set the number formatting and whether or not promode should be activated
 */
@SuppressWarnings("unused")
public class AppSetupUnitFragment extends Fragment {

    @SuppressWarnings("unused")
    private FragmentSetup3UnitsBinding binding;


    public static AppSetupUnitFragment newInstance() {
        return new AppSetupUnitFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSetup3UnitsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView rvUnits = binding.rvSetupUnitList;
        LocalizedUnitType[] allUnitTypes = UnitTypeContainer.getLocalizedUnitTypeArray( getActivity() );

        LinearLayoutManager rvListLayoutManager = new LinearLayoutManager( getContext() );
        rvUnits.setLayoutManager( rvListLayoutManager );
        rvUnits.addItemDecoration( new DividerItemDecoration(requireContext() , LinearLayoutManager.VERTICAL ));

        SetupUnitTypeAdapter rvUnitAdapter = new SetupUnitTypeAdapter( Arrays.asList(allUnitTypes) , requireContext() , this );
        rvUnits.setAdapter( rvUnitAdapter );

        updateLocale();
        updateSelectedCount(0);

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

        LocalizedUnitType[] allUnitTypes = UnitTypeContainer.getLocalizedUnitTypeArray( getActivity() );
        SetupUnitTypeAdapter rvUnitAdapter = new SetupUnitTypeAdapter( Arrays.asList(allUnitTypes) , requireContext() , this );
        binding.rvSetupUnitList.setAdapter( rvUnitAdapter );
        // Get selected units from activity
        AppSetupActivity activity = (AppSetupActivity) getActivity();
        if (activity != null) {
            rvUnitAdapter.setSelectedRows( activity.getSelectedUnits() );
        }

        updateLocale();
    }

    private void updateLocale(){
        binding.tvInitialSetup.setText( getString(R.string.setup_label_title) );
        binding.tvSelectUnits.setText( getString(R.string.setup_label_select_units));
        binding.tvSelectedUnitsLabel.setText( getString(R.string.setup_units_favourites_label) );
    }

    private void updateSelectedCount( int count ){
        String text = count+"/3";
        binding.tvSelectedUnits.setText( text );
    }

    public void setSelectedUnitTypes( String[] selectedUnitTypeKeys ){
        updateSelectedCount( selectedUnitTypeKeys.length );

        // Send to Activity
        AppSetupActivity activity = (AppSetupActivity) getActivity();
        if (activity != null) {
            activity.setSelectedUnits( selectedUnitTypeKeys );
        }
    }

}
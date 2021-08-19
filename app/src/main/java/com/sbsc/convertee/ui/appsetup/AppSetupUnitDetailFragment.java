package com.sbsc.convertee.ui.appsetup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sbsc.convertee.AppSetupActivity;
import com.sbsc.convertee.R;
import com.sbsc.convertee.databinding.FragmentSetup4UnitDetailsBinding;
import com.sbsc.convertee.entities.adapteritems.QuickConvertUnit;
import com.sbsc.convertee.entities.unittypes.generic.UnitType;
import com.sbsc.convertee.ui.adapter.SetupUnitTypeQuickConvertAdapter;

import java.util.HashSet;
import java.util.Set;

/**
 * Step 2 in the Setup, set the number formatting and whether or not promode should be activated
 */
public class AppSetupUnitDetailFragment extends Fragment {

    private FragmentSetup4UnitDetailsBinding binding;

    private SharedPreferences sharedPref;
    private boolean firstStart = true;

    public static AppSetupUnitDetailFragment newInstance() {
        return new AppSetupUnitDetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSetup4UnitDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Shared Preferences
        sharedPref = PreferenceManager.getDefaultSharedPreferences( requireContext() );

        RecyclerView rvUnits = binding.rvSetupUnitList;

        LinearLayoutManager rvListLayoutManager = new LinearLayoutManager( getContext() );
        rvUnits.setLayoutManager( rvListLayoutManager );
        rvUnits.addItemDecoration( new DividerItemDecoration(requireContext() , LinearLayoutManager.VERTICAL ));

        updateSelectedUnitTypes();
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
            updateSelectedUnitTypes();
            updateLocale();
        }

    }

    private void updateLocale(){
        binding.tvSetupUnitDetailsInfo.setText( getText(R.string.setup_favourite_details_info));
        binding.tvInitialSetup.setText( getString(R.string.setup_label_title) );
        binding.tvSelectUnits.setText( getString(R.string.setup_label_select_unit_details));
    }

    public void updateSelectedUnitTypes(){

        AppSetupActivity activity = (AppSetupActivity) getActivity();
        if (activity != null) {
            String[] selectedUnitTypes = activity.getSelectedUnits();
            if( selectedUnitTypes != null ){

                if( selectedUnitTypes.length == 0 ) binding.tvSetupUnitListEmpty.setVisibility(View.VISIBLE);
                else binding.tvSetupUnitListEmpty.setVisibility(View.GONE);

                binding.rvSetupUnitList.setVisibility(View.VISIBLE);
                SetupUnitTypeQuickConvertAdapter rvUnitAdapter = new SetupUnitTypeQuickConvertAdapter(selectedUnitTypes, requireContext() , this );
                binding.rvSetupUnitList.setAdapter( rvUnitAdapter );
            }else{
                binding.rvSetupUnitList.setVisibility(View.GONE);
                binding.tvSetupUnitListEmpty.setVisibility(View.VISIBLE);
            }
        }

    }

    public void getCurrentQuickConvertItems(){
        SetupUnitTypeQuickConvertAdapter adapter = (SetupUnitTypeQuickConvertAdapter) binding.rvSetupUnitList.getAdapter();
        if( adapter==null ) return;
        Set<String> quickConvertSharedPref = new HashSet<>();
        for( QuickConvertUnit item : adapter.getQuickConvertUnits() ){
            quickConvertSharedPref.add( item.toString() );
        }
        sharedPref.edit().remove( "QuickConvertItems" ).apply();
        sharedPref.edit().putStringSet( "QuickConvertItems" , quickConvertSharedPref ).apply();
    }

    public void initCustomKeyboard( UnitType unitType , String unitKey , EditText inputSource ){
        AppSetupActivity activity = (AppSetupActivity) getActivity();
        if (activity != null) {
            activity.initCustomKeyboard( unitType , unitKey , inputSource );
        }
    }

}
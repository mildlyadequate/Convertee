package com.sbsc.convertee.ui.quickconverter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.sbsc.convertee.R;
import com.sbsc.convertee.UnitTypeContainer;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnit;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnitType;
import com.sbsc.convertee.entities.unittypes.generic.UnitType;
import com.sbsc.convertee.ui.converter.UnitConverterFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuickConvertEditorFragment extends Fragment {

    // Shared Preferences
    private SharedPreferences sharedPref;

    private Spinner spQuickConvertEditorUnitType;
    private Spinner spQuickConvertEditorUnitFrom;
    private Spinner spQuickConvertEditorUnitTo;

    private Button btnQuickConvertEditorSave;

    private List<LocalizedUnit> unitArr;
    private List<LocalizedUnit> unitFromArr;
    private List<LocalizedUnit> unitToArr;

    private ArrayAdapter<LocalizedUnit> adapterFrom;
    private ArrayAdapter<LocalizedUnit> adapterTo;

    private String editingUnitType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = PreferenceManager
                .getDefaultSharedPreferences(requireContext());

        // TODO use resource instead

        editingUnitType = "";
        if (getArguments() != null) {
            editingUnitType = getArguments().getString("QuickConvertEditorItem");
            if(editingUnitType == null ) editingUnitType = "";
        }

        unitArr = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_quick_convert_editor, container, false);


        initFinishButtons( root );
        initSpinners( root );

        // Disable unit type spinner if user is editing a favourite
        if(!editingUnitType.isEmpty()) spQuickConvertEditorUnitType.setEnabled(false);


        // Inflate the layout for this fragment
        return root;
    }

    private void initSpinners( View root ){

        spQuickConvertEditorUnitType = root.findViewById(R.id.spQuickConvertEditorUnitType);
        spQuickConvertEditorUnitFrom = root.findViewById(R.id.spQuickConvertEditorUnitFrom);
        spQuickConvertEditorUnitTo = root.findViewById(R.id.spQuickConvertEditorUnitTo);

        spQuickConvertEditorUnitType.setAdapter( new ArrayAdapter<>( requireContext() , android.R.layout.simple_list_item_1 , loadUnitTypes() ) );

        adapterTo = new ArrayAdapter<>( requireContext() , android.R.layout.simple_list_item_1, new ArrayList<>());
        adapterFrom = new ArrayAdapter<>( requireContext() , android.R.layout.simple_list_item_1, new ArrayList<>());

        unitFromArr = new ArrayList<>();
        unitToArr = new ArrayList<>();

        // Listen to unit type changes
        spQuickConvertEditorUnitType.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LocalizedUnit[] arr = loadUnitTypeUnits( UnitTypeContainer.getUnitType( ( (LocalizedUnitType) spQuickConvertEditorUnitType.getSelectedItem() ).getUnitTypeKey() ) );
                unitArr.clear();
                unitArr.addAll( Arrays.asList(arr) );

                unitFromArr.clear();
                unitFromArr.addAll(unitArr);
                unitToArr.clear();
                unitToArr.addAll(unitArr);

                adapterFrom = new ArrayAdapter<>( requireContext() , android.R.layout.simple_list_item_1 , unitFromArr );
                adapterTo = new ArrayAdapter<>( requireContext() , android.R.layout.simple_list_item_1 , unitToArr);

                spQuickConvertEditorUnitFrom.setAdapter( adapterFrom );
                spQuickConvertEditorUnitTo.setAdapter( adapterTo );

                spQuickConvertEditorUnitFrom.setSelection(0);
                spQuickConvertEditorUnitTo.setSelection(1);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spQuickConvertEditorUnitFrom.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkFinishButtonStatus();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spQuickConvertEditorUnitTo.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkFinishButtonStatus();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

    }


    private void initFinishButtons( View root ){
        btnQuickConvertEditorSave = root.findViewById( R.id.btnQuickConvertEditorSave);

        btnQuickConvertEditorSave.setOnClickListener(view -> {

            Set<String> quickConverterItems = new HashSet<>(sharedPref.getStringSet("QuickConvertItems", new HashSet<>()));

            LocalizedUnitType type = (LocalizedUnitType) spQuickConvertEditorUnitType.getSelectedItem();
            LocalizedUnit unitFrom = (LocalizedUnit) spQuickConvertEditorUnitFrom.getSelectedItem();
            LocalizedUnit unitTo = (LocalizedUnit) spQuickConvertEditorUnitTo.getSelectedItem();

            // If unit type is editing, exists already
            boolean contains = false;
            for( String s : quickConverterItems )
                if ( s.startsWith(type.getUnitTypeKey()) ) {
                    quickConverterItems.remove(s);
                    quickConverterItems.add(type.getUnitTypeKey() + "::" + unitFrom.getUnitKey() + "::" + unitTo.getUnitKey());
                    contains = true;
                    break;
                }

            // If adding new unit type
            if( !contains ) quickConverterItems.add( type.getUnitTypeKey() + "::" + unitFrom.getUnitKey() + "::" + unitTo.getUnitKey() );

            sharedPref.edit().remove( "QuickConvertItems" ).apply();
            sharedPref.edit().putStringSet( "QuickConvertItems" , quickConverterItems ).apply();

            requireActivity().onBackPressed();
        });

    }

    /**
     * Set the "Save" button either enabled or disabled depending on some conditions
     */
    private void checkFinishButtonStatus(){

        LocalizedUnitType localizedUnitType = (LocalizedUnitType) spQuickConvertEditorUnitType.getSelectedItem();
        LocalizedUnit localizedFromUnit = (LocalizedUnit) spQuickConvertEditorUnitFrom.getSelectedItem();
        LocalizedUnit localizedToUnit = (LocalizedUnit) spQuickConvertEditorUnitTo.getSelectedItem();

        if( localizedUnitType == null || localizedFromUnit.getUnitKey().equals(localizedToUnit.getUnitKey()) ){
            btnQuickConvertEditorSave.setEnabled(false);
        }else{
            btnQuickConvertEditorSave.setEnabled(true);
        }
    }

    /**
     * Load unit types from unit type container, filter out existing favourite ones, if user is
     * editing a specific one -> add only that unit type
     * @return Array of unit types
     */
    private LocalizedUnitType[] loadUnitTypes(){
        Set<String> quickConverterItems = new HashSet<>(sharedPref.getStringSet("QuickConvertItems", new HashSet<>()));
        LocalizedUnitType[] allUnitTypes = UnitTypeContainer.localizedUnitTypes;
        List<LocalizedUnitType> result = new ArrayList<>();

        if( editingUnitType.isEmpty() ){

            // Case: Creating new one
            for( LocalizedUnitType unitType : allUnitTypes ){
                boolean isHidden = false;
                for( String hiddenType : quickConverterItems ){
                    if( unitType.getUnitTypeKey().startsWith( hiddenType.split("::")[0] ) ){
                        isHidden = true;
                        break;
                    }
                }
                if( !isHidden ) result.add( unitType );
            }

            requireActivity().setTitle( "Create favourite" );

        }else{

            // Case: Editing existing one
            for( LocalizedUnitType unitType : allUnitTypes ){
                if( unitType.getUnitTypeKey().startsWith(editingUnitType.split("::")[0]) ){
                    result.add(unitType);
                    break;
                }
            }

            requireActivity().setTitle( "Edit "+result.get(0).getUnitTypeName());
        }

        return result.toArray(new LocalizedUnitType[0]);
    }

    /**
     * Get the localized units for this specific unit type
     * @param unitType Units of this type
     * @return Array
     */
    private LocalizedUnit[] loadUnitTypeUnits(UnitType unitType ){

        // Get string set of hidden units of this type from preferences
        Set<String> hiddenUnits = sharedPref.getStringSet(("preference_"+ unitType.getId() +"_hidden"), new HashSet<>());

        return UnitConverterFragment.loadLocalizedUnitNames(
                getContext(),
                UnitType.filterUnits( hiddenUnits , unitType )
        );
    }
}
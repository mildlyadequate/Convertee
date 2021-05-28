package com.sbsc.convertee.ui.converter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sbsc.convertee.adapter.CalculatedUnitItemAdapter;
import com.sbsc.convertee.calculator.CalcColourCode;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnit;

import java.util.ArrayList;

public class UnitConverterViewModel extends ViewModel {

    private final MutableLiveData<CalculatedUnitItemAdapter> unitItemAdapter;
    private final MutableLiveData<Integer> selectedUnitIndex;

    private final MutableLiveData<LocalizedUnit[]> localizedUnits;
    private final MutableLiveData<Integer> localizedUnitsHash;

    private final MutableLiveData<Boolean> proModeActive;

    private final boolean defaultProModeValue = false;

    // Currency related
    private final MutableLiveData<Boolean> currencyRatesUpdated;
    private final MutableLiveData<Long> currencyRatesLastUpdated;

    // Colour related
    private final MutableLiveData<CalcColourCode.HEX> displayedColour;

    public UnitConverterViewModel() {
        unitItemAdapter = new MutableLiveData<>( new CalculatedUnitItemAdapter(new ArrayList<>()) );
        selectedUnitIndex = new MutableLiveData<>(0);

        localizedUnits = new MutableLiveData<>();
        localizedUnitsHash = new MutableLiveData<>(0);

        proModeActive = new MutableLiveData<>(defaultProModeValue);

        currencyRatesUpdated = new MutableLiveData<>(false);
        currencyRatesLastUpdated = new MutableLiveData<>(0L);

        displayedColour = new MutableLiveData<>(new CalcColourCode.HEX("#fff"));
    }

    /*
     * ========================================== SETTER ===========================================
     */

    public void setLocalizedUnits(LocalizedUnit[] localizedUnit){ localizedUnits.postValue(localizedUnit); }

    /**
     * Update the selected unit index, check for null, if the index didn't actually change, then
     * don't post it again as there is no redraw needed
     * @param selectedUnitIndex Integer of selected unit index
     */
    public void setSelectedUnitIndex(int selectedUnitIndex) {
        if( this.selectedUnitIndex.getValue() == null ) this.selectedUnitIndex.setValue(0);
        if( this.selectedUnitIndex.getValue() == selectedUnitIndex ) return;
        this.selectedUnitIndex.postValue(selectedUnitIndex);
    }

    public void setProModeActive(boolean proModeActive) { this.proModeActive.postValue(proModeActive); }

    public void setLocalizedUnitsHash(int localizedUnitsHash) { this.localizedUnitsHash.postValue(localizedUnitsHash); }

    public void setCurrencyRatesUpdated(){
        if( this.currencyRatesUpdated.getValue() != null )
        this.currencyRatesUpdated.postValue(!currencyRatesUpdated.getValue());
    }

    public void setCurrencyRatesLastUpdated(long time){ this.currencyRatesLastUpdated.postValue(time); }

    public void setDisplayedColour( CalcColourCode.HEX hexColour ){ this.displayedColour.postValue(hexColour); }

    /*
     * ==================================== GETTER FOR VALUES  =====================================
     * With null check if needed
     */

    public int getLocalizedUnitsHashValue() {
        if( this.localizedUnitsHash.getValue() != null )
            return localizedUnitsHash.getValue();
        else
            return 0;
    }

    public LocalizedUnit[] getLocalizedUnitsValue(){
        return localizedUnits.getValue();
    }

    public CalculatedUnitItemAdapter getUnitItemAdapterValue() {
        return unitItemAdapter.getValue();
    }

    public LocalizedUnit getSelectedLocalizedUnitValue(){
        if ( localizedUnits.getValue() == null || selectedUnitIndex.getValue() == null ) return null;
        return localizedUnits.getValue()[ selectedUnitIndex.getValue() ];
    }

    public boolean getProModeActiveValue() {
        if( proModeActive.getValue() == null ) proModeActive.setValue(defaultProModeValue);
        return proModeActive.getValue();
    }

    /*
     * ====================================== GETTER FOR LIVE DATA =================================
     */

    public LiveData<LocalizedUnit[]> getLocalizedUnits(){
        return localizedUnits;
    }

    public LiveData<Integer> getSelectedUnitIndex() { return selectedUnitIndex; }

    public LiveData<Boolean> getProModeActive() { return proModeActive; }

    public LiveData<Boolean> getCurrencyRatesUpdated() { return currencyRatesUpdated; }

    public LiveData<Long> getCurrencyRatesLastUpdated() { return currencyRatesLastUpdated; }

    public LiveData<CalcColourCode.HEX> getDisplayedColour() { return displayedColour; }

}
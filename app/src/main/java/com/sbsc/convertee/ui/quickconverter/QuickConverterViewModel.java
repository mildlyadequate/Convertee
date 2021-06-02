package com.sbsc.convertee.ui.quickconverter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class QuickConverterViewModel extends ViewModel {

    private final MutableLiveData<Boolean> currencyRatesUpdated;

    public QuickConverterViewModel(){
        currencyRatesUpdated = new MutableLiveData<>(false);
    }

    public void setCurrencyRatesUpdated(){
        if( this.currencyRatesUpdated.getValue() != null )
            this.currencyRatesUpdated.postValue(!currencyRatesUpdated.getValue());
    }

    public LiveData<Boolean> getCurrencyRatesUpdated() { return currencyRatesUpdated; }
}
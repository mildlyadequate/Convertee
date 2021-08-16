package com.sbsc.convertee.entities.calc;

import androidx.annotation.NonNull;

import com.sbsc.convertee.entities.adapteritems.LocalizedUnit;

public class CalculatedUnitItem {

    private final String unitKey;
    private String value;
    private final String localizedUnitName;
    private final String unitShort;

    public CalculatedUnitItem(String value, LocalizedUnit localizedUnitName) {
        this.unitKey = localizedUnitName.getUnitKey();
        this.value = value;
        this.localizedUnitName = localizedUnitName.getLocalizedName();
        this.unitShort = localizedUnitName.getNameShort();
    }

    public String getValue() {
        return value;
    }

    public String getLocalizedUnitName() {
        return localizedUnitName;
    }

    public String getUnitShort() {
        return unitShort;
    }

    public String getUnitKey() { return unitKey; }

    public void setValue(String value) { this.value = value; }

    @NonNull
    @Override
    public String toString() {
        return value + " " + localizedUnitName + " - " + unitShort + "[" + unitKey + "]";
    }
}

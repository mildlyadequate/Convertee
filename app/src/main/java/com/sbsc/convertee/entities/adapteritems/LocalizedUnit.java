package com.sbsc.convertee.entities.adapteritems;

public class LocalizedUnit {

    private final String unitName;
    private final String localizedName;
    private final String nameShort;

    public LocalizedUnit(String unitName, String localizedName, String nameShort) {
        this.unitName = unitName;
        this.localizedName = localizedName;
        this.nameShort = nameShort;
    }

    public String getUnitName() { return unitName; }

    public String getLocalizedName() {
        return localizedName;
    }

    public String getNameShort() {
        return nameShort;
    }

    @Override
    public String toString() {
        return localizedName;
    }
}

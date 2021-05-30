package com.sbsc.convertee.entities.adapteritems;

public class LocalizedUnit {

    private final String unitKey;
    private final String localizedName;
    private final String nameShort;
    private final String sampleInput;

    public LocalizedUnit( String unitKey , String localizedName , String nameShort , String sampleInput ) {
        this.unitKey = unitKey;
        this.localizedName = localizedName;
        this.nameShort = nameShort;
        this.sampleInput = sampleInput;
    }

    public String getUnitKey() { return unitKey; }
    public String getLocalizedName() {
        return localizedName;
    }
    public String getNameShort() {
        return nameShort;
    }
    public String getSampleInput() { return sampleInput; }

    @Override
    public String toString() {
        return localizedName;
    }
}

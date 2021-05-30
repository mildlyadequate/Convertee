package com.sbsc.convertee.entities.adapteritems;

import androidx.annotation.NonNull;

public class LocalizedUnitType implements Comparable<LocalizedUnitType>{

    private String unitTypeKey;
    private String unitTypeName;
    private int iconId;
    private boolean favourite;
    private boolean hidden;

    public LocalizedUnitType(String unitTypeKey, String unitTypeName, int iconId) {
        this.unitTypeKey = unitTypeKey;
        this.unitTypeName = unitTypeName;
        this.iconId = iconId;
        this.favourite = false;
        this.hidden = false;
    }

    public LocalizedUnitType() {
        this.unitTypeKey = "";
        this.unitTypeName = "";
        this.iconId = 0;
    }

    // Getter
    public String getUnitTypeKey() {
        return unitTypeKey;
    }
    public String getUnitTypeName() {
        return unitTypeName;
    }
    public int getIconId() {
        return iconId;
    }
    public boolean isFavourite() { return favourite; }
    public boolean isHidden() { return hidden; }

    // Setter
    public void setUnitTypeKey(String unitTypeKey) { this.unitTypeKey = unitTypeKey; }
    public void setUnitTypeName(String unitTypeName) { this.unitTypeName = unitTypeName; }
    public void setIconId(int iconId) { this.iconId = iconId; }
    public void setFavourite(boolean favourite) { this.favourite = favourite; }
    public void setHidden(boolean hidden) { this.hidden = hidden; }

    @NonNull
    @Override
    public String toString() {
        return ( "["+unitTypeKey+"] "+ unitTypeName + " | ImageId: "+iconId);
    }

    @Override
    public int compareTo(LocalizedUnitType o) {

        int faveCompare = Boolean.compare( !favourite, !o.isFavourite() );
        if( faveCompare == 0){
            return unitTypeName.compareTo( o.getUnitTypeName() );
        }
        return faveCompare;
    }
}

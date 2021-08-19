package com.sbsc.convertee.entities.adapteritems;

import androidx.annotation.NonNull;

import com.sbsc.convertee.entities.UnitTypeContainer;
import com.sbsc.convertee.entities.unittypes.generic.UnitType;

import java.util.List;

public class LocalizedUnitType implements Comparable<LocalizedUnitType>{

    private String unitTypeKey;
    private String unitTypeName;
    private int iconId;
    private boolean favourite;
    private List<String> tags;

    public LocalizedUnitType(String unitTypeKey, String unitTypeName, int iconId) {
        this.unitTypeKey = unitTypeKey;
        this.unitTypeName = unitTypeName;
        this.iconId = iconId;
        this.favourite = false;
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
    public void setTags(List<String> tags) { this.tags = tags; }

    // Setter
    public void setUnitTypeKey(String unitTypeKey) { this.unitTypeKey = unitTypeKey; }
    public void setUnitTypeName(String unitTypeName) { this.unitTypeName = unitTypeName; }
    public void setIconId(int iconId) { this.iconId = iconId; }
    public void setFavourite(boolean favourite) { this.favourite = favourite; }
    public List<String> getTags() { return tags; }

    @NonNull
    @Override
    public String toString() {
        return unitTypeName;
    }

    @Override
    public int compareTo(LocalizedUnitType o) {

        int faveCompare = Boolean.compare( !favourite, !o.isFavourite() );
        if( faveCompare == 0){
            return unitTypeName.compareTo( o.getUnitTypeName() );
        }
        return faveCompare;
    }

    public UnitType getUnitTypeObject(){
        return UnitTypeContainer.getUnitType(unitTypeKey);
    }
}

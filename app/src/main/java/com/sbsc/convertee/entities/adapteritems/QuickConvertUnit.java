package com.sbsc.convertee.entities.adapteritems;


public class QuickConvertUnit {

    private final String unitTypeId;

    private final String defaultValue;

    private final String idUnitFrom;
    private final String idUnitTo;

    private final LocalizedUnit[] arrayUnitType;

    public QuickConvertUnit( String unitTypeId, String defaultValue, String idUnitFrom, String idUnitTo, LocalizedUnit[] arrayUnitType ) {
        this.unitTypeId = unitTypeId;
        this.defaultValue = defaultValue;
        this.idUnitFrom = idUnitFrom;
        this.idUnitTo = idUnitTo;
        this.arrayUnitType = arrayUnitType;
    }

    public QuickConvertUnit( String[] values , LocalizedUnit[] arrayUnitType ) {
        if( values.length >= 3 ){
            this.unitTypeId = values[0];
            this.idUnitFrom = values[1];
            this.idUnitTo = values[2];
        }else{
            this.unitTypeId = "";
            this.idUnitFrom = "";
            this.idUnitTo = "";
        }

        if( values.length >= 4 )
            this.defaultValue = values[3];
        else
            this.defaultValue = "";

        this.arrayUnitType = arrayUnitType;
    }

    public String getUnitTypeId() { return unitTypeId; }
    public String getIdUnitFrom() { return idUnitFrom; }
    public String getIdUnitTo() { return idUnitTo; }
    public String getDefaultValue() { return defaultValue; }

    public LocalizedUnit[] getArrayUnitType() { return arrayUnitType; }
}

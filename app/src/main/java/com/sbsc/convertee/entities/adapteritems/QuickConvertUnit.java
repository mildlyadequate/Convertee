package com.sbsc.convertee.entities.adapteritems;


public class QuickConvertUnit {

    private String unitTypeId;
    private String idUnitFrom;
    private String idUnitTo;

    private LocalizedUnit[] arrayUnitType;

    public QuickConvertUnit( String unitTypeId, String idUnitFrom, String idUnitTo, LocalizedUnit[] arrayUnitType ) {
        this.unitTypeId = unitTypeId;
        this.idUnitFrom = idUnitFrom;
        this.idUnitTo = idUnitTo;
        this.arrayUnitType = arrayUnitType;
    }

    public String getUnitTypeId() { return unitTypeId; }
    public String getIdUnitFrom() { return idUnitFrom; }
    public String getIdUnitTo() { return idUnitTo; }

    public LocalizedUnit[] getArrayUnitType() { return arrayUnitType; }
}

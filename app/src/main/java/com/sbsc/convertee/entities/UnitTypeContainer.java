package com.sbsc.convertee.entities;

import android.app.Activity;
import android.util.Log;

import com.sbsc.convertee.R;
import com.sbsc.convertee.entities.adapteritems.LocalizedUnitType;
import com.sbsc.convertee.entities.unittypes.Angle;
import com.sbsc.convertee.entities.unittypes.Area;
import com.sbsc.convertee.entities.unittypes.BraSize;
import com.sbsc.convertee.entities.unittypes.ColourCode;
import com.sbsc.convertee.entities.unittypes.Currency;
import com.sbsc.convertee.entities.unittypes.DataStorage;
import com.sbsc.convertee.entities.unittypes.Distance;
import com.sbsc.convertee.entities.unittypes.Energy;
import com.sbsc.convertee.entities.unittypes.Force;
import com.sbsc.convertee.entities.unittypes.FuelEconomy;
import com.sbsc.convertee.entities.unittypes.Numerative;
import com.sbsc.convertee.entities.unittypes.Pressure;
import com.sbsc.convertee.entities.unittypes.ShoeSize;
import com.sbsc.convertee.entities.unittypes.Speed;
import com.sbsc.convertee.entities.unittypes.Temperature;
import com.sbsc.convertee.entities.unittypes.Time;
import com.sbsc.convertee.entities.unittypes.Volume;
import com.sbsc.convertee.entities.unittypes.Weight;
import com.sbsc.convertee.entities.unittypes.generic.UnitType;
import com.sbsc.convertee.tools.HelperUtil;

import org.apache.commons.lang3.StringUtils;

public class UnitTypeContainer {

    public static LocalizedUnitType[] localizedUnitTypes;

    private static final String[][] unitTypeKeys = {

            // BASICS
            {Area.id, String.valueOf(R.drawable.ic_ut_area)},
            {Distance.id, String.valueOf(R.drawable.ic_ut_distance)},
            {Volume.id, String.valueOf(R.drawable.ic_ut_volume)},
            {Weight.id, String.valueOf(R.drawable.ic_ut_weight)},

            // LIVING
            {BraSize.id, String.valueOf(R.drawable.ic_ut_brasize)},
            {Currency.id, String.valueOf(R.drawable.ic_ut_currency)},
            {FuelEconomy.id, String.valueOf(R.drawable.ic_ut_fueleconomy)},
            {ShoeSize.id, String.valueOf(R.drawable.ic_ut_shoesize)},
            {Temperature.id, String.valueOf(R.drawable.ic_ut_temperature)},
            {Time.id, String.valueOf(R.drawable.ic_ut_time)},

            // SCIENCE
            {Energy.id, String.valueOf(R.drawable.ic_ut_energy)},
            {Force.id, String.valueOf(R.drawable.ic_ut_force)},
            {Pressure.id, String.valueOf(R.drawable.ic_ut_pressure)},
            {Speed.id, String.valueOf(R.drawable.ic_ut_speed)},

            // MATHS
            {Angle.id, String.valueOf(R.drawable.ic_ut_angle)},

            // TECHNOLOGY
            {ColourCode.id, String.valueOf(R.drawable.ic_ut_colour)},
            {DataStorage.id, String.valueOf(R.drawable.ic_ut_datastorage)},
            {Numerative.id, String.valueOf(R.drawable.ic_ut_numerative)},
    };

    public static LocalizedUnitType[] getLocalizedUnitTypeArray(Activity activity){
        LocalizedUnitType[] localizedArr = new LocalizedUnitType[ unitTypeKeys.length ];

        for( int i=0 ; i < localizedArr.length ; i++ ){
            String[] unitTypeRow = unitTypeKeys[i];
            LocalizedUnitType tempUT = new LocalizedUnitType();
            tempUT.setUnitTypeKey( unitTypeRow[0] );
            tempUT.setIconId( Integer.parseInt(unitTypeRow[1]) );
            tempUT.setUnitTypeName( HelperUtil.getStringResourceByName( ("unit_type_name_" + unitTypeRow[0]) , activity ) );
            localizedArr[i] = tempUT;
        }
        return localizedUnitTypes = localizedArr;
    }

    public static LocalizedUnitType getLocalizedUnitType( String id ){
        for( LocalizedUnitType unitType : localizedUnitTypes ) if( unitType.getUnitTypeKey().equals(id) ) return unitType;
        return null;
    }

    /**
     * Sets the unitType for this instance of the Fragment from the bundle sent when it was called
     * by the activity
     */
    public static UnitType getUnitType( String unitTypeId ){

        UnitType unitType = null;

        // Find out which UnitType the ID belongs to
        if( StringUtils.equalsIgnoreCase( unitTypeId , Distance.id ) ){
            unitType = Distance.getInstance();
        }else if( StringUtils.equalsIgnoreCase( unitTypeId , Weight.id ) ){
            unitType = Weight.getInstance();
        }else if( StringUtils.equalsIgnoreCase( unitTypeId , Temperature.id ) ){
            unitType = Temperature.getInstance();
        }else if( StringUtils.equalsIgnoreCase( unitTypeId , Time.id ) ){
            unitType = Time.getInstance();
        }else if( StringUtils.equalsIgnoreCase( unitTypeId , Numerative.id ) ){
            unitType = Numerative.getInstance();
        }else if( StringUtils.equalsIgnoreCase( unitTypeId , ShoeSize.id ) ){
            unitType = ShoeSize.getInstance();
        }else if( StringUtils.equalsIgnoreCase( unitTypeId , Volume.id ) ){
            unitType = Volume.getInstance();
        }else if( StringUtils.equalsIgnoreCase( unitTypeId , Energy.id ) ){
            unitType = Energy.getInstance();
        }else if( StringUtils.equalsIgnoreCase( unitTypeId , FuelEconomy.id ) ){
            unitType = FuelEconomy.getInstance();
        }else if( StringUtils.equalsIgnoreCase( unitTypeId , DataStorage.id ) ){
            unitType = DataStorage.getInstance();
        }else if( StringUtils.equalsIgnoreCase( unitTypeId , Pressure.id ) ){
            unitType = Pressure.getInstance();
        }else if( StringUtils.equalsIgnoreCase( unitTypeId , Area.id ) ){
            unitType = Area.getInstance();
        }else if( StringUtils.equalsIgnoreCase( unitTypeId , Angle.id ) ){
            unitType = Angle.getInstance();
        }else if( StringUtils.equalsIgnoreCase( unitTypeId , Speed.id ) ){
            unitType = Speed.getInstance();
        }else if( StringUtils.equalsIgnoreCase( unitTypeId , Force.id ) ){
            unitType = Force.getInstance();
        }else if( StringUtils.equalsIgnoreCase( unitTypeId , Currency.id ) ){
            unitType = Currency.getInstance();
        }else if( StringUtils.equalsIgnoreCase( unitTypeId , ColourCode.id ) ){
            unitType = ColourCode.getInstance();
        }else if( StringUtils.equalsIgnoreCase( unitTypeId , BraSize.id ) ){
            unitType = BraSize.getInstance();
        }else{
            // Should technically never happen unless unitType IDs get messed up in either code or
            // Android resources
            Log.e("UnitTypeContainer","UnitType ID was not found during initialization.");
        }

        return unitType;
    }
}

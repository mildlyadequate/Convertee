package com.sbsc.convertee.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sbsc.convertee.BuildConfig;
import com.sbsc.convertee.R;
import com.sbsc.convertee.calculator.CalcCurrency;
import com.sbsc.convertee.ui.converter.UnitConverterViewModel;
import com.sbsc.convertee.ui.quickconverter.QuickConverterViewModel;

public class CurrencyApiHandler {

    public static void getUpdatedCurrencies( RequestQueue queue, SharedPreferences sharedPref, UnitConverterViewModel unitConverterViewModel, View view , Context ctx ){
        String url = "https://openexchangerates.org/api/latest.json?app_id="+BuildConfig.CURRENCY_API_KEY;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    JsonObject convertedObject = new Gson().fromJson(response, JsonObject.class);
                    sharedPref.edit().putString("currency_rates", String.valueOf(convertedObject.get("rates"))).apply();
                    CalcCurrency.getInstance().wasUpdated();
                    unitConverterViewModel.setCurrencyRatesUpdated();
                    Snackbar.make( view , ctx.getString(R.string.currency_snackbar_update_finished) , Snackbar.LENGTH_SHORT ).show();
                }, error -> Snackbar.make( view , ctx.getString(R.string.currency_snackbar_update_error)+ " " +error.getLocalizedMessage() , Snackbar.LENGTH_SHORT ).show());
        queue.add(stringRequest);
    }

    public static void getUpdatedCurrencies(RequestQueue queue, SharedPreferences sharedPref, QuickConverterViewModel unitConverterViewModel, View view , Context ctx ){
        String url = "https://openexchangerates.org/api/latest.json?app_id="+BuildConfig.CURRENCY_API_KEY;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    JsonObject convertedObject = new Gson().fromJson(response, JsonObject.class);
                    sharedPref.edit().putString("currency_rates", String.valueOf(convertedObject.get("rates"))).apply();
                    CalcCurrency.getInstance().wasUpdated();
                    unitConverterViewModel.setCurrencyRatesUpdated();
                    Snackbar.make( view , ctx.getString(R.string.currency_snackbar_update_finished) , Snackbar.LENGTH_SHORT ).show();
                }, error -> Snackbar.make( view , ctx.getString(R.string.currency_snackbar_update_error)+ " " +error.getLocalizedMessage() , Snackbar.LENGTH_SHORT ).show() );
        queue.add(stringRequest);
    }

}

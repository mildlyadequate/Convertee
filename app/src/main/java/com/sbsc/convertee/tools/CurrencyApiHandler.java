package com.sbsc.convertee.tools;

import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sbsc.convertee.BuildConfig;
import com.sbsc.convertee.ui.converter.UnitConverterViewModel;

public class CurrencyApiHandler {

    public static void getUpdatedCurrencies(RequestQueue queue, SharedPreferences sharedPref, UnitConverterViewModel unitConverterViewModel){
        String url = "https://openexchangerates.org/api/latest.json?app_id="+BuildConfig.CURRENCY_API_KEY;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    JsonObject convertedObject = new Gson().fromJson(response, JsonObject.class);
                    sharedPref.edit().putString("currency_rates", String.valueOf(convertedObject.get("rates"))).apply();
                    unitConverterViewModel.setCurrencyRatesUpdated();
                }, error -> {
                    // TODO Handle errors such as no internet connection
                    Log.d("ERROR","Couldn't get result from API");
                });

        // Access the RequestQueue through your singleton class.
        queue.add(stringRequest);
    }

}

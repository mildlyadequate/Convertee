package com.sbsc.convertee.entities.unittypes;

import com.sbsc.convertee.entities.unittypes.generic.UnitType;

public class Currency extends UnitType {

    // SINGLETON
    private static Currency unitTypeInstance;

    public static String id = "currency";

    /**
     * Lazy initialization singleton
     * @return instance of UnitType
     */
    public static UnitType getInstance(){
        if (unitTypeInstance == null){ //if there is no instance available... create new one
            unitTypeInstance = new Currency();
        }
        return unitTypeInstance;
    }

    // Private constructor
    private Currency(){

        addUnit("euro","currency_unit_eur",                         "EUR");
        addUnit("britishpound","currency_unit_gbp",                 "GBP");
        addUnit("usdollar","currency_unit_usd",                     "USD");
        addUnit("ausdollar","currency_unit_aud",                     "AUD");
        addUnit("russianruble","currency_unit_rub",                     "RUB");
        addUnit("dirham","currency_unit_aed",                     "AED");
        addUnit("bitcoin","currency_unit_btc",                     "BTC");
        addUnit("swissfranc","currency_unit_chf",                     "CHF");
        addUnit("chineseyuan","currency_unit_cny",                     "CNY");
        addUnit("chineseyuanoffshore","currency_unit_cnh",                     "CNH");
        addUnit("japaneseyen","currency_unit_jpy",                     "JPY");
        addUnit("southkoreanwon","currency_unit_krw",                     "KRW");
        addUnit("saudiriyal","currency_unit_sar",                     "SAR");
        addUnit("newzealanddollar","currency_unit_nzd",                     "NZD");
        addUnit("mexicanpeso","currency_unit_mxn",                     "MXN");
        addUnit("philippinepeso","currency_unit_php",                     "PHP");
        addUnit("polishzloty","currency_unit_pln",                     "PLN");
        addUnit("southafricanrand","currency_unit_zar",                     "ZAR");
        addUnit("silverounce","currency_unit_xag",                     "XAG");
        addUnit("goldounce","currency_unit_xau",                     "XAU");
        addUnit("turkishlira","currency_unit_try",                     "TRY");
        addUnit("bulgarianlev","currency_unit_bgn",                     "BGN");
        addUnit("brazilreal","currency_unit_brl",                     "BRL");
        addUnit("canadadollar","currency_unit_cad",                     "CAD");
        addUnit("colombianpeso","currency_unit_cop",                     "COP");
        addUnit("czechkoruna","currency_unit_czk",                     "CZK");
        addUnit("algeriadinar","currency_unit_dzd",                     "DZD");
        addUnit("egyptpound","currency_unit_egp",                     "EGP");
        addUnit("danishkrone","currency_unit_dkk",                     "DKK");

    }

    /**
     * Simple getter to reach ID in a non static way
     * @return String
     */
    public String getId(){ return id; }

}
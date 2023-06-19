package com.ios.widget.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {
    private SharedPreferences preferences;

    public static String CALCULATOR_AGE = "CalculatorAge";
    public static String COUNT_TIMER = "CounterTimer";
    public static String REST_TIMER = "RestTimer";
    public static String IS_SOUND = "IsSound";
    public static int AGE = 25;

    public static final String AD_BACK = "AD_BACK";
    public static final String AD_BANNER = "AD_BANNER";
    public static final String AD_INTER = "AD_INTER";
    public static final String AD_INTER1 = "AD_INTER1";
    public static final String AD_NATIVE = "AD_NATIVE";
    public static final String AD_OPEN = "AD_OPEN";
    public static final String SHOW = "AdShow";
    public static final String CLICK = "CLICK";
    public static String openads;

    public Pref(Context context) {
        preferences = context.getSharedPreferences("work_out_pref", Context.MODE_PRIVATE);
    }

    public int getInt(String str, int i) {
        return preferences.getInt(str, i);
    }

    public String getString(String str, String s) {
        return preferences.getString(str, s);
    }

    public void putString(String key, String value) {
        preferences.edit().putString(key, value).commit();
    }

    public void putInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public void putBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String str, boolean i) {
        return preferences.getBoolean(str, i);
    }
}

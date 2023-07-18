package com.ios.widget.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class Pref {
    private SharedPreferences preferences;

    public static String CALCULATOR_AGE = "CalculatorAge";
    public static String COUNT_TIMER = "CounterTimer";
    public static String REST_TIMER = "RestTimer";
    public static String IS_SOUND = "IsSound";
    public static String IS_DATE = "IsDate";
    public static String IS_BATTERY = "IsBattery";
    public static String IS_BATTERY_ALARM = "IsBatteryAlarm";
    public static String IS_X_PANEL_1_ALARM = "IsXPanel1Alarm";
    public static String IS_X_PANEL_3_ALARM = "IsXPanel3Alarm";
    public static int AGE = 25;
    static final String MyPref = "widgetPref";
    public static final String SHOW_NEVER = "SHOW_NEVER";
    static final String Widget_LIST = "WIDGET_LIST";
    static final String Widget_LISTInterval = "WIDGET_LISTInterval";

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

    public void putLong(String key, long value) {
        preferences.edit().putLong(key, value).commit();
    }

    public long getLong(String key, long i) {
        return preferences.getLong(key, i);
    }


    public static void setWidgetLists(Context context, List<String> list) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putString(Widget_LIST, new Gson().toJson((Object) list));
        edit.apply();
    }

    public static List<String> getWidgetLists(Context context) {
        return (List) new Gson().fromJson(context.getApplicationContext().getSharedPreferences(MyPref, 0).getString(Widget_LIST, ""), new TypeToken<List<String>>() {
        }.getType());
    }

    public static void setWidgetInterval(Context context, int list) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putInt(Widget_LISTInterval, list);
        edit.apply();
    }

    public static int getWidgetInterval(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getInt(Widget_LISTInterval, 0);
    }
}

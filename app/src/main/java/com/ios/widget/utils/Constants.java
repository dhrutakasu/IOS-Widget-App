package com.ios.widget.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ios.widget.Model.WidgetModel;
import com.ios.widget.R;

import java.util.ArrayList;

public class Constants {

    public static final String ITEM_POSITION = "ITEM_POSITION";
    public static String ItemName = "ItemName";
    public static String ItemIcon = "ItemIcon";
    public static String NOTIFICATION_HOUR = "NotificationHour";
    public static String NOTIFICATION_MINUTES = "NotificationMinutes";
    public static String ExerciseSetTime;
    public static String BASE_URL="https://7starinnovation.com/meditationmusic/";
    public static String BASE_URL_EXTENSION=".mp3";
    public static final String TAG_WIDGET_NOTE_ID = "WIDGET_NOTE_ID";
    public static final String TAG_WIDGET_TYPE = "WIDGET_TYPE";
    public static int Widget_Id;
    public static String WidgetClick="WidgetClick";

    public static boolean isConnectingToInternet(Context con) {
        ConnectivityManager connectivity = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo state : info) {
                    if (state.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static ArrayList<WidgetModel>  getWidgetLists() {
        ArrayList<WidgetModel> modelArrayList = new ArrayList<>();
        WidgetModel widgetModel = new WidgetModel(R.drawable.img_photo_s_xxhdpi, R.drawable.img_photo_m_xxhdpi, R.drawable.img_photo_l_xxhdpi);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_1_xxhdpi, R.drawable.img_clock_medium_1_xxhdpi, R.drawable.img_clock_large_1_xxhdpi);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_2_xxhdpi, R.drawable.img_clock_medium_2_xxhdpi, R.drawable.img_clock_large_2_xxhdpi);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_3_xxhdpi, R.drawable.img_clock_medium_3_xxhdpi, R.drawable.img_clock_large_3_xxhdpi);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_4_xxhdpi, R.drawable.img_clock_medium_4_xxhdpi, R.drawable.img_clock_large_4_xxhdpi);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_5_xxhdpi, R.drawable.img_clock_medium_5_xxhdpi, R.drawable.img_clock_large_5_xxhdpi);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_6_xxhdpi, R.drawable.img_clock_medium_6_xxhdpi, R.drawable.img_clock_large_6_xxhdpi);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_7_xxhdpi, R.drawable.img_clock_medium_7_xxhdpi, R.drawable.img_clock_large_7_xxhdpi);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_8_xxhdpi, R.drawable.img_clock_medium_8_xxhdpi, R.drawable.img_clock_large_8_xxhdpi);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_9_xxhdpi, R.drawable.img_clock_medium_9_xxhdpi, R.drawable.img_clock_large_9_xxhdpi);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_weather_small_1_xxhdpi, R.drawable.img_weather_medium_1_xxhdpi, R.drawable.img_weather_large_1_xxhdpi);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_weather_small_2_xxhdpi, R.drawable.img_weather_medium_2_xxhdpi, R.drawable.img_weather_large_2_xxhdpi);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_weather_small_3_xxhdpi, R.drawable.img_weather_medium_3_xxhdpi, R.drawable.img_weather_large_3_xxhdpi);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_calendar_small_1_xxhdpi, R.drawable.img_calendar_medium_1_xxhdpi, R.drawable.img_calendar_large_1_xxhdpi);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_calendar_small_2_xxhdpi, R.drawable.img_calendar_medium_2_xxhdpi, R.drawable.img_calendar_large_2_xxhdpi);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_calendar_small_3_xxhdpi, R.drawable.img_calendar_medium_3_xxhdpi, R.drawable.img_calendar_large_3_xxhdpi);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_calendar_small_4_xxhdpi, R.drawable.img_calendar_medium_4_xxhdpi, R.drawable.img_calendar_large_4_xxhdpi);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_xpanel_small_1_xxhdpi, R.drawable.img_xpanel_medium_1_xxhdpi, R.drawable.img_xpanel_large_1_xxhdpi);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_xpanel_small_2_xxhdpi, R.drawable.img_xpanel_medium_2_xxhdpi, R.drawable.img_xpanel_large_2_xxhdpi);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_xpanel_small_3_xxhdpi, R.drawable.img_xpanel_medium_3_xxhdpi, R.drawable.img_xpanel_large_3_xxhdpi);
        modelArrayList.add(widgetModel);

        return modelArrayList;
    }
}

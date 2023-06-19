package com.ios.widget.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Constants {

    public static String ItemName = "ItemName";
    public static String ItemIcon = "ItemIcon";
    public static String NOTIFICATION_HOUR = "NotificationHour";
    public static String NOTIFICATION_MINUTES = "NotificationMinutes";
    public static String ExerciseSetTime;
    public static String BASE_URL="https://7starinnovation.com/meditationmusic/";
    public static String BASE_URL_EXTENSION=".mp3";

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
}

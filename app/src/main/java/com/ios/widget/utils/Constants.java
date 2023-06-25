package com.ios.widget.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
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
    public static String BASE_URL = "https://7starinnovation.com/meditationmusic/";
    public static String BASE_URL_EXTENSION = ".mp3";
    public static final String TAG_WIDGET_NOTE_ID = "WIDGET_NOTE_ID";
    public static final String TAG_WIDGET_TYPE = "WIDGET_TYPE";
    public static int Widget_Id;
    public static int Widget_Type_Id;
    public static String WidgetClick = "WidgetClick";
    public static String TabPos = "TabPos";

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

    public static ArrayList<WidgetModel> getInsideWidgetLists() {
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

    public static ArrayList<WidgetModel> getTrendyWidgetLists() {
        ArrayList<WidgetModel> modelArrayList = new ArrayList<>();
        WidgetModel widgetModel = new WidgetModel(R.drawable.img_calendar_small_4_xxhdpi, R.drawable.img_clock_medium_3_xxhdpi, R.drawable.img_xpanel_large_1_xxhdpi, "Trendy");
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_weather_small_1_xxhdpi, R.drawable.img_calendar_medium_4_xxhdpi, R.drawable.img_clock_large_8_xxhdpi, "Trendy");
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_9_xxhdpi, R.drawable.img_xpanel_medium_3_xxhdpi, R.drawable.img_weather_large_2_xxhdpi, "Trendy");
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_photo_s_xxhdpi, R.drawable.img_photo_m_xxhdpi, R.drawable.img_photo_l_xxhdpi, "Trendy");
        modelArrayList.add(widgetModel);

        return modelArrayList;
    }

    public static ArrayList<WidgetModel> getCalendarWidgetLists() {
        ArrayList<WidgetModel> modelArrayList = new ArrayList<>();
        WidgetModel widgetModel = new WidgetModel(R.drawable.img_calendar_small_1_xxhdpi, R.drawable.img_calendar_medium_1_xxhdpi, R.drawable.img_calendar_large_1_xxhdpi, "Calendar");
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_calendar_small_2_xxhdpi, R.drawable.img_calendar_medium_2_xxhdpi, R.drawable.img_calendar_large_2_xxhdpi, "Calendar");
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_calendar_small_3_xxhdpi, R.drawable.img_calendar_medium_3_xxhdpi, R.drawable.img_calendar_large_3_xxhdpi, "Calendar");
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_calendar_small_4_xxhdpi, R.drawable.img_calendar_medium_4_xxhdpi, R.drawable.img_calendar_large_4_xxhdpi, "Calendar");
        modelArrayList.add(widgetModel);

        return modelArrayList;
    }

    public static ArrayList<WidgetModel> getWeatherWidgetLists() {
        ArrayList<WidgetModel> modelArrayList = new ArrayList<>();
        WidgetModel widgetModel = new WidgetModel(R.drawable.img_weather_small_1_xxhdpi, R.drawable.img_weather_medium_1_xxhdpi, R.drawable.img_weather_large_1_xxhdpi, "Weather");
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_weather_small_2_xxhdpi, R.drawable.img_weather_medium_2_xxhdpi, R.drawable.img_weather_large_2_xxhdpi, "Weather");
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_weather_small_3_xxhdpi, R.drawable.img_weather_medium_3_xxhdpi, R.drawable.img_weather_large_3_xxhdpi, "Weather");
        modelArrayList.add(widgetModel);

        return modelArrayList;
    }

    public static ArrayList<WidgetModel> getClockWidgetLists() {
        ArrayList<WidgetModel> modelArrayList = new ArrayList<>();
        WidgetModel widgetModel = new WidgetModel(R.drawable.img_clock_small_1_xxhdpi, R.drawable.img_clock_medium_1_xxhdpi, R.drawable.img_clock_large_1_xxhdpi, "Clock");
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_2_xxhdpi, R.drawable.img_clock_medium_2_xxhdpi, R.drawable.img_clock_large_2_xxhdpi, "Clock");
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_3_xxhdpi, R.drawable.img_clock_medium_3_xxhdpi, R.drawable.img_clock_large_3_xxhdpi, "Clock");
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_4_xxhdpi, R.drawable.img_clock_medium_4_xxhdpi, R.drawable.img_clock_large_4_xxhdpi, "Clock");
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_5_xxhdpi, R.drawable.img_clock_medium_5_xxhdpi, R.drawable.img_clock_large_5_xxhdpi, "Clock");
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_6_xxhdpi, R.drawable.img_clock_medium_6_xxhdpi, R.drawable.img_clock_large_6_xxhdpi, "Clock");
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_7_xxhdpi, R.drawable.img_clock_medium_7_xxhdpi, R.drawable.img_clock_large_7_xxhdpi, "Clock");
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_8_xxhdpi, R.drawable.img_clock_medium_8_xxhdpi, R.drawable.img_clock_large_8_xxhdpi, "Clock");
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_9_xxhdpi, R.drawable.img_clock_medium_9_xxhdpi, R.drawable.img_clock_large_9_xxhdpi, "Clock");
        modelArrayList.add(widgetModel);

        return modelArrayList;
    }

    public static ArrayList<WidgetModel> getXPanelWidgetLists() {
        ArrayList<WidgetModel> modelArrayList = new ArrayList<>();
        WidgetModel widgetModel = new WidgetModel(R.drawable.img_xpanel_small_1_xxhdpi, R.drawable.img_xpanel_medium_1_xxhdpi, R.drawable.img_xpanel_large_1_xxhdpi, "X-Panel");
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_xpanel_small_2_xxhdpi, R.drawable.img_xpanel_medium_2_xxhdpi, R.drawable.img_xpanel_large_2_xxhdpi, "X-Panel");
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_xpanel_small_3_xxhdpi, R.drawable.img_xpanel_medium_3_xxhdpi, R.drawable.img_xpanel_large_3_xxhdpi, "X-Panel");
        modelArrayList.add(widgetModel);

        return modelArrayList;
    }

    public static ArrayList<WidgetModel> getPhotoWidgetLists() {
        ArrayList<WidgetModel> modelArrayList = new ArrayList<>();
        WidgetModel widgetModel = new WidgetModel(R.drawable.img_photo_s_xxhdpi, R.drawable.img_photo_m_xxhdpi, R.drawable.img_photo_l_xxhdpi, "Trendy");
        modelArrayList.add(widgetModel);

        return modelArrayList;
    }

    public static ArrayList<Integer> getWidgetCategoryLists() {
        ArrayList<Integer> TypesArrayList = new ArrayList<>();
        TypesArrayList.add(R.drawable.btn_trendy);
        TypesArrayList.add(R.drawable.btn_calendar);
        TypesArrayList.add(R.drawable.btn_weather);
        TypesArrayList.add(R.drawable.btn_alarm);
        TypesArrayList.add(R.drawable.btn_x_panel);
        TypesArrayList.add(R.drawable.btn_photo);

        return TypesArrayList;
    }


    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}

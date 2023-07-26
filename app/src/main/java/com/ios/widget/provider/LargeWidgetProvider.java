package com.ios.widget.provider;

import static com.ios.widget.utils.Constants.Widget_Id;
import static com.ios.widget.utils.Constants.Widget_Type_Id;
import static com.ios.widget.utils.Pref.IS_BATTERY;
import static com.ios.widget.utils.Pref.IS_DATE_LARGE_1;
import static com.ios.widget.utils.Pref.IS_DATE_LARGE_4;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ios.widget.Model.WidgetData;
import com.ios.widget.R;
import com.ios.widget.helper.DatabaseHelper;
import com.ios.widget.utils.Constants;
import com.ios.widget.utils.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class LargeWidgetProvider extends AppWidgetProvider {
//    private Handler handler;
//    private Runnable runnable;
    private boolean IsTorchOn;


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        DatabaseHelper helper = new DatabaseHelper(context);
        WidgetData widgetData = new WidgetData(2, Constants.Widget_Type_Id, -1,"");
//            WidgetData widgetData = new WidgetData("0", "L", Constants.getWidgetLists().get(Constants.Widget_Type_Id).getSmall(), Constants.getWidgetLists().get(Constants.Widget_Type_Id).getMedium(), Constants.getWidgetLists().get(Constants.Widget_Type_Id).getLarge(), String.valueOf(Constants.Widget_Type_Id), String.valueOf(Widget_Id));
        System.out.println("_*_*_*_*_*_*_ 11 :: " + helper.getWidgetCount());
        int insert = helper.InsertWidget(widgetData);
        System.out.println("_*_*_*_*_*_*_ insert : " + insert);
        for (int id : appWidgetIds) {
            Widget_Id = id;
            System.out.println("_*_*_*_*_*_*_ uuid " + id);
        }
        if (helper.getWidgetCount() != 0) {
            WidgetData widgetsId = helper.getWidgetsId(insert);
            System.out.println("_*_*_*_*_*_*_ 33 :: " + widgetsId);
            widgetsId.setNumber(Widget_Id);
            helper.updateWidget(widgetsId);
        }
        for (int i = 0; i < helper.getWidgets().size(); ++i) {
            RemoteViews rv = null;
            long startMillis = 0;
            Uri.Builder builder = null;
            Intent intent = null;
            Intent intent1 = null;
            PendingIntent configPendingIntent = null;
            Calendar calendar;
            int currentDay ;
            int currentMonth;
            int currentYear;
//            handler = new Handler();
            switch (helper.getWidgets().get(i).getPosition()) {
                case 0:
                case 20:
                    //todo x-panel 1 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel1_large);
                    RemoteViews finalRv = rv;
                    int finalI = i;

                    CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cameraManager.registerTorchCallback(new CameraManager.TorchCallback() {
                            @Override
                            public void onTorchModeUnavailable(@NonNull String cameraId) {
                                super.onTorchModeUnavailable(cameraId);
                            }

                            @Override
                            public void onTorchModeChanged(@NonNull String cameraId, boolean enabled) {
                                super.onTorchModeChanged(cameraId, enabled);
                                IsTorchOn = enabled;
                            }

                            @Override
                            public void onTorchStrengthLevelChanged(@NonNull String cameraId, int newStrengthLevel) {
                                super.onTorchStrengthLevelChanged(cameraId, newStrengthLevel);
                            }
                        }, null);
                    }
                    if (Constants.IsWIfiConnected(context)) {
                        System.out.println("************ WIFI RECEIVE  ON ");
                        rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi1_selected);
                    } else {
                        System.out.println("************ WIFI RECEIVE  Off ");
                        rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi1);
                    }
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter != null) {
                        if (mBluetoothAdapter.isEnabled()) {
                            System.out.println("************  Bluetooth RECEIVE  ON ");
                            rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluethooth1_selected);
                        } else if (!mBluetoothAdapter.isEnabled()) {
                            System.out.println("************  Bluetooth RECEIVE  else ");
                            rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluethooth1);
                        }
                    }

                    System.out.println("********* ON / OFF : " + IsTorchOn);
                    if (IsTorchOn) {
                        rv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1_selected);
                    } else {
                        rv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1);
                    }
                   /* appWidgetManager.notifyAppWidgetViewDataChanged(helper.getWidgets().get(finalI).getNumber(), R.id.IvWifi);
                    appWidgetManager.notifyAppWidgetViewDataChanged(helper.getWidgets().get(finalI).getNumber(), R.id.IvTorch);

                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("************ WIFI RECEIVE  ");
                            if (Constants.IsWIfiConnected(context)) {
                                System.out.println("************ WIFI RECEIVE  ON ");
                                finalRv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi1_selected);
                            } else {
                                System.out.println("************ WIFI RECEIVE  Off ");
                                finalRv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi1);
                            }

                            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                            if (mBluetoothAdapter != null) {
                                if (mBluetoothAdapter.isEnabled()) {
                                    System.out.println("************  Bluetooth RECEIVE  ON ");
                                    finalRv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluethooth1_selected);
                                } else if (!mBluetoothAdapter.isEnabled()) {
                                    System.out.println("************  Bluetooth RECEIVE  else ");
                                    finalRv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluethooth1);
                                }
                            }

                            CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                cameraManager.registerTorchCallback(new CameraManager.TorchCallback() {
                                    @Override
                                    public void onTorchModeUnavailable(@NonNull String cameraId) {
                                        super.onTorchModeUnavailable(cameraId);
                                    }

                                    @Override
                                    public void onTorchModeChanged(@NonNull String cameraId, boolean enabled) {
                                        super.onTorchModeChanged(cameraId, enabled);
                                        IsTorchOn = enabled;
                                    }

                                    @Override
                                    public void onTorchStrengthLevelChanged(@NonNull String cameraId, int newStrengthLevel) {
                                        super.onTorchStrengthLevelChanged(cameraId, newStrengthLevel);
                                    }
                                }, null);
                            }

                            System.out.println("********* ON / OFF : " + IsTorchOn);
                            if (IsTorchOn) {
                                finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1_selected);
                            } else {
                                finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1);
                            }
                            appWidgetManager.updateAppWidget(helper.getWidgets().get(finalI).getNumber(), finalRv);

                            handler.postDelayed(this, 2000);
                        }
                    };
                    handler.postDelayed(runnable, 0);*/

                    Intent intentWifi = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intentWifi, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvWifi, configPendingIntent);

                    Intent intentBluetooth = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intentBluetooth, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvBluetooth, configPendingIntent);

                    Intent intent2 = new Intent(context, XPanelFlashlightWidgetReceiver.class);
                    intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, Widget_Id);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvTorch, pendingIntent);
                    break;
                case 2:
                case 9:
                    //todo weather 2 large

                    break;
                case 4:
                    //todo calender 1 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar1_large);
                    rv.setImageViewBitmap(R.id.iv_background, Constants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.img_calendar1_small_bg), 30));
                    rv.setCharSequence(R.id.TClockYear, "setFormat12Hour", "yyyy");
                    rv.setCharSequence(R.id.TClockYear, "setFormat24Hour", "yyyy");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMMM");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMMM");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                    intent = new Intent(context, LargeWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, Widget_Type_Id);
                    intent.putExtra("TypeId", helper.getWidgets().get(i).getPosition());

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(Widget_Id, R.id.GridCalendarLargeView, intent);
                 calendar = Calendar.getInstance();
                    currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                    currentMonth = calendar.get(Calendar.MONTH);
                    currentYear = calendar.get(Calendar.YEAR);
                   new Pref(context).putString(IS_DATE_LARGE_1, currentDay + "/" + currentMonth + "/" + currentYear);
                      /*  runnable = new Runnable() {
                        @Override
                        public void run() {
                            Calendar NewCalendar = Calendar.getInstance();
                            int currentDay = NewCalendar.get(Calendar.DAY_OF_MONTH);
                            int currentMonth = NewCalendar.get(Calendar.MONTH);
                            int currentYear = NewCalendar.get(Calendar.YEAR);
                            if (!new Pref(context).getString(IS_DATE, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                                appWidgetManager.notifyAppWidgetViewDataChanged(Widget_Id, R.id.GridCalendarLargeView);
                                new Pref(context).putString(IS_DATE, currentDay + "/" + currentMonth + "/" + currentYear);
                            }
                            handler.postDelayed(this, 5000);
                        }
                    };
                    handler.postDelayed(runnable, 5000);*/
                    if (!new Pref(context).getBoolean(Pref.IS_CALENDAR_1_ALARM, false)) {
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        new Pref(context).putBoolean(Pref.IS_CALENDAR_1_ALARM, true);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, broadcast);
                    }
                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlLargeCal, configPendingIntent);
                    break;
                case 5:
                    //todo calender 2 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar3_large);
                    rv.setImageViewBitmap(R.id.iv_background, Constants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.img_calendar2_large_bg), 30));
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "EEE, yyyy");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "EEE, yyyy");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlLargeCal, configPendingIntent);
                    break;
                case 6:
                    //todo calender 3 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar2_large);
//                    rv.setImageViewResource(R.id.iv_background, R.drawable.shape_app_widget_ffffff_r25_bg);

                    intent = new Intent(context, LargeWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, Widget_Type_Id);
                    intent.putExtra("TypeId", helper.getWidgets().get(i).getPosition());

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(Widget_Id, R.id.GridCalendarLargeView, intent);
                 calendar = Calendar.getInstance();
                    currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                    currentMonth = calendar.get(Calendar.MONTH);
                    currentYear = calendar.get(Calendar.YEAR);
                    new Pref(context).putString(Pref.IS_DATE_LARGE_3, currentDay + "/" + currentMonth + "/" + currentYear);
                   /*    runnable = new Runnable() {
                        @Override
                        public void run() {
                            Calendar NewCalendar = Calendar.getInstance();
                            int currentDay = NewCalendar.get(Calendar.DAY_OF_MONTH);
                            int currentMonth = NewCalendar.get(Calendar.MONTH);
                            int currentYear = NewCalendar.get(Calendar.YEAR);
                            if (!new Pref(context).getString(IS_DATE, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                                appWidgetManager.notifyAppWidgetViewDataChanged(Widget_Id, R.id.GridCalendarLargeView);
                                new Pref(context).putString(IS_DATE, currentDay + "/" + currentMonth + "/" + currentYear);
                            }
                            handler.postDelayed(this, 5000);
                        }
                    };
                    handler.postDelayed(runnable, 5000);*/
                    if (!new Pref(context).getBoolean(Pref.IS_CALENDAR_3_ALARM, false)) {
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        new Pref(context).putBoolean(Pref.IS_CALENDAR_3_ALARM, true);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, broadcast);
                    }
                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlLargeCal, configPendingIntent);
                    break;
                case 7:
                    //todo calender 4 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar4_large);
//                    rv.setImageViewBitmap(R.id.iv_background,Constants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.shape_app_widget_1c1c1e_r25_bg),30));
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMMM, yyyy");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMMM, yyyy");
                    rv.setCharSequence(R.id.TClockDay, "setFormat12Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockDay, "setFormat24Hour", "EEEE");

                    intent = new Intent(context, LargeWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, Widget_Type_Id);
                    intent.putExtra("TypeId", helper.getWidgets().get(i).getPosition());

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(Widget_Id, R.id.GridCalendarLargeView, intent);
                 calendar = Calendar.getInstance();
                    currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                    currentMonth = calendar.get(Calendar.MONTH);
                    currentYear = calendar.get(Calendar.YEAR);
                    new Pref(context).putString(IS_DATE_LARGE_4, currentDay + "/" + currentMonth + "/" + currentYear);
                    /*   runnable = new Runnable() {
                        @Override
                        public void run() {
                            Calendar NewCalendar = Calendar.getInstance();
                            int currentDay = NewCalendar.get(Calendar.DAY_OF_MONTH);
                            int currentMonth = NewCalendar.get(Calendar.MONTH);
                            int currentYear = NewCalendar.get(Calendar.YEAR);
                            if (!new Pref(context).getString(IS_DATE, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                                appWidgetManager.notifyAppWidgetViewDataChanged(Widget_Id, R.id.GridCalendarLargeView);
                                new Pref(context).putString(IS_DATE, currentDay + "/" + currentMonth + "/" + currentYear);
                            }
                            handler.postDelayed(this, 5000);
                        }
                    };
                    handler.postDelayed(runnable, 5000);*/
                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlLargeCal, configPendingIntent);
                    break;
                case 8:
                    //todo weather 1 large
                    LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    System.out.println("------- catch Out permission location: " + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        if (ActivityCompat.checkSelfPermission(
                                context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            System.out.println("------- catch Out permission: ");
                        } else {
                            String city = "";

                            rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_weather1_large);
                            System.out.println("------- catch Out: " + "Your Location: else ");
//
                            List<String> providers = locationManager.getProviders(true);
                            for (String provider : providers) {
                                Location locationGPS = locationManager.getLastKnownLocation(provider);
                                if (locationGPS != null) {
                                    double lat = locationGPS.getLatitude();
                                    double longi = locationGPS.getLongitude();

                                    try {
                                        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                                        List<Address> addresses = geocoder.getFromLocation(locationGPS.getLatitude(), locationGPS.getLongitude(), 1);
                                        city = addresses.get(0).getLocality();

                                        WidgetData widgetsId = helper.getWidgetsId(insert);
                                        System.out.println("_*_*_*_*_*_*_ 33 :: " + widgetsId);
                                        widgetsId.setCity(city);
                                        helper.updateWidget(widgetsId);

                                        RequestQueue queue = Volley.newRequestQueue(context);
                                        String url = "https://api.openweathermap.org/data/2.5/forecast/daily?q=" + city + "&cnt=5&units=metric&APPID=b7fc383a06f2e5b385f2f811e18192f6";
                                        RemoteViews finalRv1 = rv;
                                        StringRequest stringReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                System.out.println("------- catch Out response: " + response.toString());
                                                try {
                                                    JSONObject obj = new JSONObject(response);

                                                    JSONArray WeatherArray = obj.getJSONArray("list");
                                                    for (int j = 0; j < WeatherArray.length(); j++) {
                                                        JSONObject WeatherObject = WeatherArray.getJSONObject(j);

                                                        WeatherObject.get("main");
                                                        WeatherObject.get("icon");
                                                        System.out.println("------- catch Out WeatherObject: " + WeatherObject.getString("description"));
                                                        finalRv1.setTextViewText(R.id.TvDesc, WeatherObject.get("description").toString());
                                                        finalRv1.setImageViewResource(R.id.IvWeatherIcon, Constants.getWeatherIcons(WeatherObject.getString("icon")));
                                                    }
                                                    JSONObject MainObject = obj.getJSONObject("main");

                                                    JSONObject SysObject = obj.getJSONObject("sys");
                                                    finalRv1.setTextViewText(R.id.TvCity, obj.get("name") + "," + SysObject.get("country"));
                                                    String Temp = MainObject.get("temp").toString();
                                                    System.out.println("------- catch Out response Temp: " + Temp.substring(0, Temp.lastIndexOf(".")));
                                                    finalRv1.setTextViewText(R.id.TvTemp, Temp.substring(0, Temp.lastIndexOf(".")) + "°C");
                                                    String MinTemp = MainObject.get("temp_min").toString();
                                                    String MaxTemp = MainObject.get("temp_max").toString();
                                                    System.out.println("------- catch Out response TempMin: " + MinTemp.substring(0, MinTemp.lastIndexOf(".")) + " -- " + MaxTemp.substring(0, MaxTemp.lastIndexOf(".")));
                                                    finalRv1.setTextViewText(R.id.TvTempMaxMin, "H:" + MaxTemp.substring(0, MaxTemp.lastIndexOf(".")) + "°C L:" + MinTemp.substring(0, MinTemp.lastIndexOf(".")) + "°C");

//                                                    if (!new Pref(context).getBoolean(Pref.IS_WEATHER_1 _ALARM, false)) {
                                                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                                                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                                    new Pref(context).putBoolean(Pref.IS_WEATHER_1_ALARM, true);
                                                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, broadcast);
//                                                    }
                                                    appWidgetManager.updateAppWidget(Widget_Id, finalRv1);
                                                } catch (JSONException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                //displaying the error in toast if occur
                                                System.out.println("------- catch Out errrr: " + error.getMessage());
                                            }
                                        });
                                        queue.add(stringReq);
                                    } catch (Exception e) {
                                        Log.d("------- catch cityEx", "Error to find the city." + e.getMessage());
                                    }
                                    System.out.println("------- catch Out: " + "Your Location: " + " " + "Latitude: " + lat + " " + "Longitude: " + longi);
                                } else {
                                    System.out.println("------- catch Out: GPS " + locationGPS);

                                }
                            }

                        }
                    }
                    break;
                case 10:
                    //todo weather 3 large
                    break;
                    case 1:
                case 11:
                    //todo clock 1 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple1_large);

                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.analog_clock, configPendingIntent);
                    break;
                case 12:
                    //todo clock 2 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple2_large);

                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.analog_clock, configPendingIntent);
                    break;
                case 13:
                    //todo clock 3 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple3_large);

                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.analog_clock, configPendingIntent);
                    break;
                case 14:
                    //todo clock 4 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_text1_large);

                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlLargeClock, configPendingIntent);
                    break;
                case 15:
                    //todo clock 5 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_realism2_large);

                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.analog_clock, configPendingIntent);
                    break;
                case 16:
                    //todo clock 6 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_realism3_large);

                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.analog_clock, configPendingIntent);
                    break;
                case 17:
                    //todo clock 7 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_text1_large);

                    rv.setImageViewBitmap(R.id.iv_background, Constants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.img_clock_text1_bg_large), 30));
                    rv.setCharSequence(R.id.TClockHour, "setFormat12Hour", "HH");
                    rv.setCharSequence(R.id.TClockHour, "setFormat24Hour", "HH");
                    rv.setCharSequence(R.id.TClockMinutes, "setFormat12Hour", "mm");
                    rv.setCharSequence(R.id.TClockMinutes, "setFormat24Hour", "mm");
                    rv.setCharSequence(R.id.TClockDayMonthDate, "setFormat12Hour", "EEEE, MMM d");
                    rv.setCharSequence(R.id.TClockDayMonthDate, "setFormat24Hour", "EEEE, MMM d");

                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlLargeClock, configPendingIntent);
                    break;
                case 19:
                    //todo clock 9 large

                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_text3_large);

                    rv.setCharSequence(R.id.TClockHour, "setFormat12Hour", "HH");
                    rv.setCharSequence(R.id.TClockHour, "setFormat24Hour", "HH");
                    rv.setCharSequence(R.id.TClockMinutes, "setFormat12Hour", "mm");
                    rv.setCharSequence(R.id.TClockMinutes, "setFormat24Hour", "mm");
                    rv.setCharSequence(R.id.TClockDayMonthDate, "setFormat12Hour", "d EEEE");
                    rv.setCharSequence(R.id.TClockDayMonthDate, "setFormat24Hour", "d EEEE");

                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlLargeClock, configPendingIntent);
                    break;
                case 21:
                    //todo x-panel 2 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel2_large);

                    BatteryManager bm = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
                    int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                    new Pref(context).putInt(IS_BATTERY, batLevel);
                    RemoteViews finalrv = rv;

                    finalrv.setTextViewText(R.id.progress_text, batLevel + "%");
                    finalrv.setProgressBar(R.id.progress_bar, 100, batLevel, false);
//                    runnable = new Runnable() {
//                        @Override
//                        public void run() {
//                            BatteryManager bm = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
//                            int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
//                            System.out.println("************ WIFI RECEIVE LLLLL 100/" + batLevel + " -- : 100/" + new Pref(context).getInt(IS_BATTERY, -1));
//                            if (new Pref(context).getInt(IS_BATTERY, -1) != batLevel) {
//                                finalrv.setTextViewText(R.id.progress_text, batLevel + "%");
//                                finalrv.setProgressBar(R.id.progress_bar, 100, batLevel, false);
//                                new Pref(context).putInt(IS_BATTERY, batLevel);
//                                appWidgetManager.updateAppWidget(Widget_Id, finalrv);
//                            }
//                            handler.postDelayed(this, 2000);
//                        }
//                    };
//                    handler.postDelayed(runnable, 0);

                    Intent intentBattery = new Intent(Settings.EXTRA_BATTERY_SAVER_MODE_ENABLED);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intentBattery, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.RlBattery, configPendingIntent);
                    if (!new Pref(context).getBoolean(Pref.IS_BATTERY_ALARM,false)) {
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        new Pref(context).putBoolean(Pref.IS_BATTERY_ALARM, true);
                        long repeatInterval = TimeUnit.MILLISECONDS.toSeconds(1);
                        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, (System.currentTimeMillis() + TimeUnit.MILLISECONDS.toSeconds(1)), repeatInterval, broadcast);
                    }
                    break;
                case 22:
                    //todo x-panel 3 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel3_large);

                    rv.setCharSequence(R.id.TClockHr, "setFormat12Hour", "HH");
                    rv.setCharSequence(R.id.TClockHr, "setFormat24Hour", "HH");
                    rv.setCharSequence(R.id.TClockMin, "setFormat12Hour", "mm");
                    rv.setCharSequence(R.id.TClockMin, "setFormat24Hour", "mm");
                    rv.setCharSequence(R.id.TClockDay, "setFormat12Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockDay, "setFormat24Hour", "EEEE");

                    BatteryManager systemService = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
                    int property = systemService.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

                    CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        manager.registerTorchCallback(new CameraManager.TorchCallback() {
                            @Override
                            public void onTorchModeUnavailable(@NonNull String cameraId) {
                                super.onTorchModeUnavailable(cameraId);
                            }

                            @Override
                            public void onTorchModeChanged(@NonNull String cameraId, boolean enabled) {
                                super.onTorchModeChanged(cameraId, enabled);
                                IsTorchOn = enabled;
                            }

                            @Override
                            public void onTorchStrengthLevelChanged(@NonNull String cameraId, int newStrengthLevel) {
                                super.onTorchStrengthLevelChanged(cameraId, newStrengthLevel);
                            }
                        }, null);
                    }
                    if (Constants.IsWIfiConnected(context)) {
                        System.out.println("************ WIFI RECEIVE  ON ");
                        rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi1_selected);
                    } else {
                        System.out.println("************ WIFI RECEIVE  Off ");
                        rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi1);
                    }
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (bluetoothAdapter != null) {
                        if (bluetoothAdapter.isEnabled()) {
                            System.out.println("************  Bluetooth RECEIVE  ON ");
                            rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluethooth1_selected);
                        } else if (!bluetoothAdapter.isEnabled()) {
                            System.out.println("************  Bluetooth RECEIVE  else ");
                            rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluethooth1);
                        }
                    }

                    System.out.println("********* ON / OFF : " + IsTorchOn);
                    if (IsTorchOn) {
                        rv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1_selected);
                    } else {
                        rv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1);
                    }

                    final float totalSpace = Constants.DeviceMemory.getInternalStorageSpace();
                    final float occupiedSpace = Constants.DeviceMemory.getInternalUsedSpace();
                    final float freeSpace = Constants.DeviceMemory.getInternalFreeSpace();

                    rv.setProgressBar(R.id.progressBarCharge, 100, property, false);
                    rv.setProgressBar(R.id.progressBarStorage, (int) totalSpace, (int) occupiedSpace, false);
                    if (!new Pref(context).getBoolean(Pref.IS_X_PANEL_3_ALARM, false)) {
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        new Pref(context).putBoolean(Pref.IS_X_PANEL_3_ALARM, true);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, broadcast);
                    }
                    Intent intentWifi3 = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intentWifi3, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvWifi, configPendingIntent);

                    Intent intentBluetooth3 = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intentBluetooth3, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvBluetooth, configPendingIntent);

                    Intent intentCellular3 = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
//                    intentCellular.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
//                    intentCellular.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intentCellular3, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvCellular, configPendingIntent);

                    Intent intentTorch3 = new Intent(context, XPanelFlashlight3WidgetReceiver.class);
                    configPendingIntent = PendingIntent.getBroadcast(context, 0, intentTorch3, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvTorch, configPendingIntent);
                    break;
                case 18:
                    //todo x-panel 4 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel4_large);

                    rv.setCharSequence(R.id.TClockHr, "setFormat12Hour", "HH");
                    rv.setCharSequence(R.id.TClockHr, "setFormat24Hour", "HH");
                    rv.setCharSequence(R.id.TClockMin, "setFormat12Hour", "mm");
                    rv.setCharSequence(R.id.TClockMin, "setFormat24Hour", "mm");
                    rv.setCharSequence(R.id.TClockDay, "setFormat12Hour", "d EEEE");
                    rv.setCharSequence(R.id.TClockDay, "setFormat24Hour", "d EEEE");

                    BatteryManager batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
                    int managerIntProperty = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

                    long KILOBYTE = 1024;
                    StatFs internalStatFs = new StatFs( Environment.getRootDirectory().getAbsolutePath() );
                    long internalTotal;
                    long internalFree;

                    StatFs externalStatFs = new StatFs( Environment.getExternalStorageDirectory().getAbsolutePath() );
                    long externalTotal;
                    long externalFree;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        internalTotal = ( internalStatFs.getBlockCountLong() * internalStatFs.getBlockSizeLong() ) / ( KILOBYTE * KILOBYTE );
                        internalFree = ( internalStatFs.getAvailableBlocksLong() * internalStatFs.getBlockSizeLong() ) / ( KILOBYTE * KILOBYTE );
                        externalTotal = ( externalStatFs.getBlockCountLong() * externalStatFs.getBlockSizeLong() ) / ( KILOBYTE * KILOBYTE );
                        externalFree = ( externalStatFs.getAvailableBlocksLong() * externalStatFs.getBlockSizeLong() ) / ( KILOBYTE * KILOBYTE );
                    }
                    else {
                        internalTotal = ( (long) internalStatFs.getBlockCount() * (long) internalStatFs.getBlockSize() ) / ( KILOBYTE * KILOBYTE );
                        internalFree = ( (long) internalStatFs.getAvailableBlocks() * (long) internalStatFs.getBlockSize() ) / ( KILOBYTE * KILOBYTE );
                        externalTotal = ( (long) externalStatFs.getBlockCount() * (long) externalStatFs.getBlockSize() ) / ( KILOBYTE * KILOBYTE );
                        externalFree = ( (long) externalStatFs.getAvailableBlocks() * (long) externalStatFs.getBlockSize() ) / ( KILOBYTE * KILOBYTE );
                    }

                    long total = internalTotal + externalTotal;
                    long free = internalFree + externalFree;
                    long used = total - free;
                    System.out.println("-----------store TTT : " + Constants.bytes2String(total) + "/" + Constants.bytes2String(free)+ "/" + Constants.bytes2String(used));
                    rv.setTextViewText(R.id.progress_text, managerIntProperty + "%");
                    rv.setTextViewText(R.id.storage_text, Constants.bytes2String(used) + "/" + Constants.bytes2String(total));
                    if (!new Pref(context).getBoolean(Pref.IS_X_PANEL_4_ALARM, false)) {
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        new Pref(context).putBoolean(Pref.IS_X_PANEL_4_ALARM, true);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, broadcast);
                    }
                    break;
            }

            appWidgetManager.updateAppWidget(Widget_Id, rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] iArr) {
        for (int id : iArr) {
            System.out.println("_*_*_*_*_*_*_ uuid " + id);
            DatabaseHelper helper = new DatabaseHelper(context);
//            WidgetData widgetsId = helper.getWidgetsNumber(id);
//            if (widgetsId!=null) {
//                System.out.println("_*_*_*_*_*_*_ 33 :: " + widgetsId);
//                widgetsId.setNumber(-1);
//            }
//            helper.updateWidget(widgetsId);
            helper.getDeleteWidgets(id);
        }
    }
}

package com.ios.widget.Appprovider;

import static com.ios.widget.AppUtils.MyAppConstants.CreateWidget;
import static com.ios.widget.AppUtils.MyAppConstants.Widget_Id;

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
import android.hardware.camera2.CameraManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ios.widget.ImageModel.AppWidgetData;
import com.ios.widget.R;
import com.ios.widget.Apphelper.AppDatabaseHelper;
import com.ios.widget.Appui.Activity.MainActivity;
import com.ios.widget.AppUtils.MyAppConstants;
import com.ios.widget.AppUtils.MyAppPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MediumWidgetProvider extends AppWidgetProvider {
    private boolean IsTorchOn;


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        AppDatabaseHelper helper = new AppDatabaseHelper(context);
        int insert = -1;
        AppWidgetData appWidgetData = null;
        if (CreateWidget) {
            appWidgetData = new AppWidgetData(1, MyAppConstants.Widget_Type_Id, -1, "", MyAppConstants.Temp_Id, MyAppConstants.IS_SIM_CARD);
            insert = helper.InsertWidget(appWidgetData);
            for (int id : appWidgetIds) {
                Widget_Id = id;
            }
            if (helper.getWidgetCount() != 0) {
                AppWidgetData widgetsId = helper.getWidgetsId(insert);
                widgetsId.setNumber(Widget_Id);
                helper.updateWidget(widgetsId);
            }
        }
        for (int i = 0; i < helper.getWidgets().size(); ++i) {
            RemoteViews rv = null;
            long startMillis = 0;
            Uri.Builder builder = null;
            Intent intent = null;
            Intent intent1 = null;
            PendingIntent configPendingIntent = null;
            Calendar calendar;
            int currentDay;
            int currentMonth;
            int currentYear;
            if (helper.getWidgets().get(i).getType() == 1) {
                switch (helper.getWidgets().get(i).getPosition()) {
                    case 0:
                    case 13:
                        //todo clock 3 medium
                        rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple3_medium);
                        intent = new Intent(context, MediumWidgetService.class);
                        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, Widget_Id);
                        intent.putExtra("TypeId", helper.getWidgets().get(i).getPosition());
                        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                        rv.setRemoteAdapter(Widget_Id, R.id.GridCalendarMediumView, intent);

                        calendar = Calendar.getInstance();
                        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                        currentMonth = calendar.get(Calendar.MONTH);
                        currentYear = calendar.get(Calendar.YEAR);
                        new MyAppPref(context).putString(MyAppPref.IS_DATE_4, currentDay + "/" + currentMonth + "/" + currentYear);

                        if (!new MyAppPref(context).getBoolean(MyAppPref.IS_CLOCK_3_ALARM, false)) {
                            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                            PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                            new MyAppPref(context).putBoolean(MyAppPref.IS_CLOCK_3_ALARM, true);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, broadcast);
                        }
                        startMillis = Calendar.getInstance().getTimeInMillis();
                        builder = CalendarContract.CONTENT_URI.buildUpon();
                        builder.appendPath("time");
                        ContentUris.appendId(builder, startMillis);
                        intent1 = new Intent(Intent.ACTION_VIEW)
                                .setData(builder.build());
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                        rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);

                        intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.LlMediumWidget, configPendingIntent);
                        break;
                    case 1:
                    case 7:
                        //todo calender 4 medium
                        rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar4_medium);
                        rv.setCharSequence(R.id.TClockDay, "setFormat12Hour", "EEEE");
                        rv.setCharSequence(R.id.TClockDay, "setFormat24Hour", "EEEE");
                        rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMMM");
                        rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMMM");
                        rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                        rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                        RemoteViews finalRv3 = rv;
                        intent = new Intent(context, MediumWidgetService.class);
                        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, Widget_Id);
                        intent.putExtra("TypeId", helper.getWidgets().get(i).getPosition());
                        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                        finalRv3.setRemoteAdapter(Widget_Id, R.id.GridCalendarMediumView, intent);

                        calendar = Calendar.getInstance();
                        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                        currentMonth = calendar.get(Calendar.MONTH);
                        currentYear = calendar.get(Calendar.YEAR);
                        new MyAppPref(context).putString(MyAppPref.IS_DATE_4, currentDay + "/" + currentMonth + "/" + currentYear);

                        if (!new MyAppPref(context).getBoolean(MyAppPref.IS_CALENDAR_4_ALARM, false)) {
                            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                            PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                            new MyAppPref(context).putBoolean(MyAppPref.IS_CALENDAR_4_ALARM, true);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, broadcast);
                        }
                        startMillis = Calendar.getInstance().getTimeInMillis();
                        builder = CalendarContract.CONTENT_URI.buildUpon();
                        builder.appendPath("time");
                        ContentUris.appendId(builder, startMillis);
                        intent1 = new Intent(Intent.ACTION_VIEW)
                                .setData(builder.build());
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                        rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);

                        intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.LlMediumWidget, configPendingIntent);
                        break;
                    case 2:
                    case 22:
                        //todo x-panel 3 medium
                        rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel3_medium);

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
                        if (MyAppConstants.IsWIfiConnected(context)) {
                            rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_xpanel_medium_2_wifi_selected);
                        } else {
                            rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_xpanel_medium_2_wifi);
                        }

                        if (helper.getWidgets().get(i).getSim()) {
                            rv.setImageViewResource(R.id.IvCellular, R.drawable.ic_xpanel_medium_2_mobiledata_selected);
                        } else {
                            rv.setImageViewResource(R.id.IvCellular, R.drawable.ic_xpanel_medium_2_mobiledata);
                        }
                        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (bluetoothAdapter != null) {
                            if (bluetoothAdapter.isEnabled()) {
                                rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_xpanel_medium_2_bluetooth_selected);
                            } else if (!bluetoothAdapter.isEnabled()) {
                                rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_xpanel_medium_2_bluetooth);
                            }
                        }

                        if (IsTorchOn) {
                            rv.setImageViewResource(R.id.IvTorch, R.drawable.ic_xpanel_medium_2_flashlight_selected);
                        } else {
                            rv.setImageViewResource(R.id.IvTorch, R.drawable.ic_xpanel_medium_2_flashlight);
                        }

                        final float totalSpace = MyAppConstants.DeviceMemory.getInternalStorageSpace();
                        final float occupiedSpace = MyAppConstants.DeviceMemory.getInternalUsedSpace();
                        final float freeSpace = MyAppConstants.DeviceMemory.getInternalFreeSpace();

                        rv.setProgressBar(R.id.progressBarCharge, 100, property, false);
                        rv.setProgressBar(R.id.progressBarStorage, (int) totalSpace, (int) occupiedSpace, false);
                        if (!new MyAppPref(context).getBoolean(MyAppPref.IS_X_PANEL_3_ALARM, false)) {
                            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                            PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                            new MyAppPref(context).putBoolean(MyAppPref.IS_X_PANEL_3_ALARM, true);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, broadcast);
                        }
                        Intent intentWifi3 = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intentWifi3, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.IvWifi, configPendingIntent);

                        Intent intentBluetooth3 = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intentBluetooth3, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.IvBluetooth, configPendingIntent);

                        Intent intentCellular3 = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                        if (intentCellular3.resolveActivity(context.getPackageManager()) != null) {
                            configPendingIntent = PendingIntent.getActivity(context, 0, intentCellular3, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                            rv.setOnClickPendingIntent(R.id.IvCellular, configPendingIntent);
                        }

                        Intent intentTorch3 = new Intent(context, XPanelFlashlight3WidgetReceiver.class);
                        configPendingIntent = PendingIntent.getBroadcast(context, 0, intentTorch3, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.IvTorch, configPendingIntent);

                        intent = new Intent(Settings.ACTION_INTERNAL_STORAGE_SETTINGS);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.progressBarStorage, configPendingIntent);

                        intent = new Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.progressBarCharge, configPendingIntent);

                        intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.LlTime, configPendingIntent);

                        intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.LlMediumWidget, configPendingIntent);
                        break;
                    case 5:
                        //todo calender 2 medium
                        rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar3_medium);
                        rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "EEE, yyyy");
                        rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "EEE, yyyy");
                        rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                        rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                        RemoteViews finalRv1 = rv;
                        int finalI1 = i;
                        intent = new Intent(context, MediumWidgetService.class);
                        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, Widget_Id);
                        intent.putExtra("TypeId", helper.getWidgets().get(i).getPosition());
                        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                        finalRv1.setRemoteAdapter(R.id.GridCalendarMediumView, intent);
                        calendar = Calendar.getInstance();
                        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                        currentMonth = calendar.get(Calendar.MONTH);
                        currentYear = calendar.get(Calendar.YEAR);
                        new MyAppPref(context).putString(MyAppPref.IS_DATE_3, currentDay + "/" + currentMonth + "/" + currentYear);
                        if (!new MyAppPref(context).getBoolean(MyAppPref.IS_CALENDAR_2_ALARM, false)) {
                            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                            PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                            new MyAppPref(context).putBoolean(MyAppPref.IS_CALENDAR_2_ALARM, true);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, broadcast);
                        }
                        startMillis = Calendar.getInstance().getTimeInMillis();
                        builder = CalendarContract.CONTENT_URI.buildUpon();
                        builder.appendPath("time");
                        ContentUris.appendId(builder, startMillis);
                        intent1 = new Intent(Intent.ACTION_VIEW).setData(builder.build());
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                        rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);

                        intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.LlMediumWidget, configPendingIntent);
                        break;
                    case 6:
                        //todo calender 3 medium
                        rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar2_medium);
                        rv.setCharSequence(R.id.TClockDay, "setFormat12Hour", "EEEE");
                        rv.setCharSequence(R.id.TClockDay, "setFormat24Hour", "EEEE");
                        rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMMM yyyy");
                        rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMMM yyyy");
                        rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                        rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                        RemoteViews finalRv = rv;
                        int finalI2 = i;
                        intent = new Intent(context, MediumWidgetService.class);
                        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, Widget_Id);
                        intent.putExtra("TypeId", helper.getWidgets().get(i).getPosition());
                        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                        finalRv.setRemoteAdapter(Widget_Id, R.id.GridCalendarMediumView, intent);
                        calendar = Calendar.getInstance();
                        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                        currentMonth = calendar.get(Calendar.MONTH);
                        currentYear = calendar.get(Calendar.YEAR);
                        new MyAppPref(context).putString(MyAppPref.IS_DATE_2, currentDay + "/" + currentMonth + "/" + currentYear);

                        if (!new MyAppPref(context).getBoolean(MyAppPref.IS_CALENDAR_3_ALARM, false)) {
                            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                            PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                            new MyAppPref(context).putBoolean(MyAppPref.IS_CALENDAR_3_ALARM, true);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, broadcast);
                        }
                        startMillis = Calendar.getInstance().getTimeInMillis();
                        builder = CalendarContract.CONTENT_URI.buildUpon();
                        builder.appendPath("time");
                        ContentUris.appendId(builder, startMillis);
                        intent1 = new Intent(Intent.ACTION_VIEW)
                                .setData(builder.build());
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                        finalRv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);

                        intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.LlMediumWidget, configPendingIntent);
                        break;
                    case 8:
                        //todo weather 1 medium
                        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            if (ActivityCompat.checkSelfPermission(
                                    context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                    context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            } else {
                                String city = "";

                                rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_weather1_medium);
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

                                            AppWidgetData widgetsId = helper.getWidgetsId(insert);
                                            widgetsId.setCity(city);
                                            helper.updateWidget(widgetsId);

                                            RemoteViews finalRv4 = rv;
                                            RequestQueue queue = Volley.newRequestQueue(context);
                                            String url, tempExt;
                                            if (appWidgetData.getTemp() == 0) {
                                                url = MyAppConstants.BASE_URL_WEATHER + city + "&units=metric&APPID=" + context.getString(R.string.str_weather_key);
                                                tempExt = "°C";
                                            } else {
                                                url = MyAppConstants.BASE_URL_WEATHER + city + "&units=imperial&APPID=" + context.getString(R.string.str_weather_key);
                                                tempExt = "°F";
                                            }
                                            StringRequest stringReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject obj = new JSONObject(response);

                                                        JSONArray WeatherArray = obj.getJSONArray("weather");
                                                        for (int j = 0; j < WeatherArray.length(); j++) {
                                                            JSONObject WeatherObject = WeatherArray.getJSONObject(j);

                                                            WeatherObject.get("main");
                                                            WeatherObject.get("icon");
                                                            finalRv4.setTextViewText(R.id.TvDesc, WeatherObject.get("main").toString());
                                                            finalRv4.setImageViewResource(R.id.IvWeatherIcon, MyAppConstants.getWeatherIcons(WeatherObject.getString("icon")));
                                                        }
                                                        JSONObject MainObject = obj.getJSONObject("main");

                                                        JSONObject SysObject = obj.getJSONObject("sys");
                                                        finalRv4.setTextViewText(R.id.TvCity, obj.get("name") + "," + SysObject.get("country"));
                                                        String Temp = MainObject.get("temp").toString();
                                                        finalRv4.setTextViewText(R.id.TvTemp, Temp.substring(0, Temp.lastIndexOf(".")) + tempExt);
                                                        String MinTemp = MainObject.get("temp_min").toString();
                                                        String MaxTemp = MainObject.get("temp_max").toString();
                                                        finalRv4.setTextViewText(R.id.TvTempMaxMin, "H:" + MaxTemp.substring(0, MaxTemp.lastIndexOf(".")) + tempExt + " / L:" + MinTemp.substring(0, MinTemp.lastIndexOf(".")) + tempExt);

                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                }
                                            });
                                            queue.add(stringReq);

                                            RequestQueue queue1 = Volley.newRequestQueue(context);
                                            String url1, tempExt1;
                                            if (appWidgetData.getTemp() == 0) {
                                                url1 = MyAppConstants.BASE_URL_FORECAST + city + "&cnt=6&units=metric&APPID=" + context.getString(R.string.str_weather_key);
                                                tempExt1 = "°C";
                                            } else {
                                                url1 = MyAppConstants.BASE_URL_FORECAST + city + "&cnt=6&units=imperial&APPID=" + context.getString(R.string.str_weather_key);
                                                tempExt1 = "°F";
                                            }
                                            int finalI3 = i;
                                            StringRequest stringReq1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject MainObject = null;
                                                        JSONArray IconObject = null;
                                                        String Temp = null;
                                                        String res = null;

                                                        JSONObject obj = new JSONObject(response);

                                                        JSONArray WeatherArray = obj.getJSONArray("list");
                                                        for (int j = 0; j < WeatherArray.length(); j++) {
                                                            JSONObject WeatherObject = WeatherArray.getJSONObject(j);
                                                            String dateStr = WeatherObject.getString("dt_txt").substring(WeatherObject.getString("dt_txt").lastIndexOf(" "));
                                                            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                                                            try {
                                                                Date date = format.parse(dateStr);
                                                                format = new SimpleDateFormat("HH:mm");
                                                                res = format.format(date);
                                                            } catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }
                                                            MainObject = WeatherObject.getJSONObject("main");
                                                            switch (j) {
                                                                case 0:
                                                                    finalRv4.setTextViewText(R.id.TvTimeFirst, res.toString());
                                                                    MainObject = WeatherObject.getJSONObject("main");
                                                                    Temp = MainObject.getString("temp").toString();
                                                                    if (Temp.contains(".")) {
                                                                        finalRv4.setTextViewText(R.id.TvTempFirst, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                    } else {
                                                                        finalRv4.setTextViewText(R.id.TvTempFirst, Temp + "°");
                                                                    }
                                                                    IconObject = WeatherObject.getJSONArray("weather");
                                                                    finalRv4.setImageViewResource(R.id.IvWeatherIconFirst, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                    break;
                                                                case 1:
                                                                    finalRv4.setTextViewText(R.id.TvTimeSecond, res.toString());
                                                                    MainObject = WeatherObject.getJSONObject("main");
                                                                    Temp = MainObject.getString("temp").toString();
                                                                    if (Temp.contains(".")) {
                                                                        finalRv4.setTextViewText(R.id.TvTempSecond, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                    } else {
                                                                        finalRv4.setTextViewText(R.id.TvTempSecond, Temp + "°");
                                                                    }
                                                                    IconObject = WeatherObject.getJSONArray("weather");
                                                                    finalRv4.setImageViewResource(R.id.IvWeatherIconSecond, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                    break;
                                                                case 2:
                                                                    finalRv4.setTextViewText(R.id.TvTimeThird, res.toString());
                                                                    MainObject = WeatherObject.getJSONObject("main");
                                                                    Temp = MainObject.getString("temp").toString();
                                                                    if (Temp.contains(".")) {
                                                                        finalRv4.setTextViewText(R.id.TvTempThird, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                    } else {
                                                                        finalRv4.setTextViewText(R.id.TvTempThird, Temp + "°");
                                                                    }
                                                                    IconObject = WeatherObject.getJSONArray("weather");
                                                                    finalRv4.setImageViewResource(R.id.IvWeatherIconThird, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                    break;
                                                                case 3:
                                                                    finalRv4.setTextViewText(R.id.TvTimeForth, res.toString());
                                                                    MainObject = WeatherObject.getJSONObject("main");
                                                                    Temp = MainObject.getString("temp").toString();
                                                                    if (Temp.contains(".")) {
                                                                        finalRv4.setTextViewText(R.id.TvTempForth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                    } else {
                                                                        finalRv4.setTextViewText(R.id.TvTempForth, Temp + "°");
                                                                    }
                                                                    IconObject = WeatherObject.getJSONArray("weather");
                                                                    finalRv4.setImageViewResource(R.id.IvWeatherIconForth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                    break;
                                                                case 4:
                                                                    finalRv4.setTextViewText(R.id.TvTimeFifth, res.toString());
                                                                    MainObject = WeatherObject.getJSONObject("main");
                                                                    Temp = MainObject.getString("temp").toString();
                                                                    if (Temp.contains(".")) {
                                                                        finalRv4.setTextViewText(R.id.TvTempFifth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                    } else {
                                                                        finalRv4.setTextViewText(R.id.TvTempFifth, Temp + "°");
                                                                    }
                                                                    IconObject = WeatherObject.getJSONArray("weather");
                                                                    finalRv4.setImageViewResource(R.id.IvWeatherIconFifth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                    break;
                                                                case 5:
                                                                    finalRv4.setTextViewText(R.id.TvTimeSixth, res.toString());
                                                                    MainObject = WeatherObject.getJSONObject("main");
                                                                    Temp = MainObject.getString("temp").toString();
                                                                    if (Temp.contains(".")) {
                                                                        finalRv4.setTextViewText(R.id.TvTempSixth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                    } else {
                                                                        finalRv4.setTextViewText(R.id.TvTempSixth, Temp + "°");
                                                                    }
                                                                    IconObject = WeatherObject.getJSONArray("weather");
                                                                    finalRv4.setImageViewResource(R.id.IvWeatherIconSixth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                    break;
                                                            }
                                                        }

                                                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                                        Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                                                        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                                        new MyAppPref(context).putBoolean(MyAppPref.IS_WEATHER_1_ALARM, true);
                                                        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, broadcast);
                                                        appWidgetManager.updateAppWidget(helper.getWidgets().get(finalI3).getNumber(), finalRv4);
                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    //displaying the error in toast if occur
                                                }
                                            });
                                            queue1.add(stringReq1);
                                        } catch (Exception e) {
                                        }

                                        intent = new Intent(context, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                        rv.setOnClickPendingIntent(R.id.LlMediumWidget, configPendingIntent);
                                    } else {

                                    }
                                }

                            }
                        }
                        break;
                    case 9:
                        //todo weather 2 medium
                        LocationManager service1 = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                        if (service1.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            if (ActivityCompat.checkSelfPermission(
                                    context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                    context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            } else {
                                String city = "";

                                rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_weather2_medium);
                                List<String> providers = service1.getProviders(true);
                                for (String provider : providers) {
                                    Location locationGPS = service1.getLastKnownLocation(provider);
                                    if (locationGPS != null) {
                                        double lat = locationGPS.getLatitude();
                                        double longi = locationGPS.getLongitude();

                                        try {
                                            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                                            List<Address> addresses = geocoder.getFromLocation(locationGPS.getLatitude(), locationGPS.getLongitude(), 1);
                                            city = addresses.get(0).getLocality();

                                            AppWidgetData widgetsId = helper.getWidgetsId(insert);
                                            widgetsId.setCity(city);
                                            helper.updateWidget(widgetsId);

                                            RemoteViews finalRv4 = rv;
                                            RequestQueue queue = Volley.newRequestQueue(context);
                                            String url, tempExt;
                                            if (appWidgetData.getTemp() == 0) {
                                                url = MyAppConstants.BASE_URL_WEATHER + city + "&units=metric&APPID=" + context.getString(R.string.str_weather_key);
                                                tempExt = "°C";
                                            } else {
                                                url = MyAppConstants.BASE_URL_WEATHER + city + "&units=imperial&APPID=" + context.getString(R.string.str_weather_key);
                                                tempExt = "°F";
                                            }
                                            StringRequest stringReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject obj = new JSONObject(response);

                                                        JSONArray WeatherArray = obj.getJSONArray("weather");
                                                        for (int j = 0; j < WeatherArray.length(); j++) {
                                                            JSONObject WeatherObject = WeatherArray.getJSONObject(j);

                                                            WeatherObject.get("main");
                                                            WeatherObject.get("icon");
                                                            finalRv4.setTextViewText(R.id.TvDesc, WeatherObject.get("main").toString());
                                                            finalRv4.setImageViewResource(R.id.IvWeatherIcon, MyAppConstants.getWeatherIcons(WeatherObject.getString("icon")));
                                                        }
                                                        JSONObject MainObject = obj.getJSONObject("main");

                                                        JSONObject SysObject = obj.getJSONObject("sys");
                                                        finalRv4.setTextViewText(R.id.TvCity, obj.get("name") + "," + SysObject.get("country"));
                                                        String Temp = MainObject.get("temp").toString();
                                                        finalRv4.setTextViewText(R.id.TvTemp, Temp.substring(0, Temp.lastIndexOf(".")) + tempExt);
                                                        String MinTemp = MainObject.get("temp_min").toString();
                                                        String MaxTemp = MainObject.get("temp_max").toString();
                                                        finalRv4.setTextViewText(R.id.TvTempMaxMin, "H:" + MaxTemp.substring(0, MaxTemp.lastIndexOf(".")) + tempExt + " L:" + MinTemp.substring(0, MinTemp.lastIndexOf(".")) + tempExt);

                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    //displaying the error in toast if occur
                                                }
                                            });
                                            queue.add(stringReq);

                                            RequestQueue queue1 = Volley.newRequestQueue(context);
                                            String url1, tempExt1;
                                            if (appWidgetData.getTemp() == 0) {
                                                url1 = MyAppConstants.BASE_URL_FORECAST + city + "&cnt=6&units=metric&APPID=" + context.getString(R.string.str_weather_key);
                                                tempExt1 = "°C";
                                            } else {
                                                url1 = MyAppConstants.BASE_URL_FORECAST + city + "&cnt=6&units=imperial&APPID=" + context.getString(R.string.str_weather_key);
                                                tempExt1 = "°F";
                                            }
                                            int finalI4 = i;
                                            StringRequest stringReq1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject MainObject = null;
                                                        JSONArray IconObject = null;
                                                        String Temp = null;
                                                        String res = null;

                                                        JSONObject obj = new JSONObject(response);

                                                        JSONArray WeatherArray = obj.getJSONArray("list");
                                                        for (int j = 0; j < WeatherArray.length(); j++) {
                                                            JSONObject WeatherObject = WeatherArray.getJSONObject(j);
                                                            String dateStr = WeatherObject.getString("dt_txt").substring(WeatherObject.getString("dt_txt").lastIndexOf(" "));
                                                            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                                                            try {
                                                                Date date = format.parse(dateStr);
                                                                format = new SimpleDateFormat("HH:mm");
                                                                res = format.format(date);
                                                            } catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }
                                                            switch (j) {
                                                                case 0:
                                                                    finalRv4.setTextViewText(R.id.TvTimeFirst, res.toString());
                                                                    MainObject = WeatherObject.getJSONObject("main");
                                                                    Temp = MainObject.getString("temp").toString();
                                                                    if (Temp.contains(".")) {
                                                                        finalRv4.setTextViewText(R.id.TvTempFirst, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                    } else {
                                                                        finalRv4.setTextViewText(R.id.TvTempFirst, Temp + "°");
                                                                    }
                                                                    IconObject = WeatherObject.getJSONArray("weather");
                                                                    finalRv4.setImageViewResource(R.id.IvWeatherIconFirst, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                    break;
                                                                case 1:
                                                                    finalRv4.setTextViewText(R.id.TvTimeSecond, res.toString());
                                                                    MainObject = WeatherObject.getJSONObject("main");
                                                                    Temp = MainObject.getString("temp").toString();
                                                                    if (Temp.contains(".")) {
                                                                        finalRv4.setTextViewText(R.id.TvTempSecond, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                    } else {
                                                                        finalRv4.setTextViewText(R.id.TvTempSecond, Temp + "°");
                                                                    }
                                                                    IconObject = WeatherObject.getJSONArray("weather");
                                                                    finalRv4.setImageViewResource(R.id.IvWeatherIconSecond, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                    break;
                                                                case 2:
                                                                    finalRv4.setTextViewText(R.id.TvTimeThird, res.toString());
                                                                    MainObject = WeatherObject.getJSONObject("main");
                                                                    Temp = MainObject.getString("temp").toString();
                                                                    if (Temp.contains(".")) {
                                                                        finalRv4.setTextViewText(R.id.TvTempThird, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                    } else {
                                                                        finalRv4.setTextViewText(R.id.TvTempThird, Temp + "°");
                                                                    }
                                                                    IconObject = WeatherObject.getJSONArray("weather");
                                                                    finalRv4.setImageViewResource(R.id.IvWeatherIconThird, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                    break;
                                                                case 3:
                                                                    finalRv4.setTextViewText(R.id.TvTimeForth, res.toString());
                                                                    MainObject = WeatherObject.getJSONObject("main");
                                                                    Temp = MainObject.getString("temp").toString();
                                                                    if (Temp.contains(".")) {
                                                                        finalRv4.setTextViewText(R.id.TvTempForth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                    } else {
                                                                        finalRv4.setTextViewText(R.id.TvTempForth, Temp + "°");
                                                                    }
                                                                    IconObject = WeatherObject.getJSONArray("weather");
                                                                    finalRv4.setImageViewResource(R.id.IvWeatherIconForth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                    break;
                                                                case 4:
                                                                    finalRv4.setTextViewText(R.id.TvTimeFifth, res.toString());
                                                                    MainObject = WeatherObject.getJSONObject("main");
                                                                    Temp = MainObject.getString("temp").toString();
                                                                    if (Temp.contains(".")) {
                                                                        finalRv4.setTextViewText(R.id.TvTempFifth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                    } else {
                                                                        finalRv4.setTextViewText(R.id.TvTempFifth, Temp + "°");
                                                                    }
                                                                    IconObject = WeatherObject.getJSONArray("weather");
                                                                    finalRv4.setImageViewResource(R.id.IvWeatherIconFifth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                    break;
                                                                case 5:
                                                                    finalRv4.setTextViewText(R.id.TvTimeSixth, res.toString());
                                                                    MainObject = WeatherObject.getJSONObject("main");
                                                                    Temp = MainObject.getString("temp").toString();
                                                                    if (Temp.contains(".")) {
                                                                        finalRv4.setTextViewText(R.id.TvTempSixth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                    } else {
                                                                        finalRv4.setTextViewText(R.id.TvTempSixth, Temp + "°");
                                                                    }
                                                                    IconObject = WeatherObject.getJSONArray("weather");
                                                                    finalRv4.setImageViewResource(R.id.IvWeatherIconSixth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                    break;
                                                            }
                                                        }

                                                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                                        Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                                                        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                                        new MyAppPref(context).putBoolean(MyAppPref.IS_WEATHER_1_ALARM, true);
                                                        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, broadcast);
                                                        appWidgetManager.updateAppWidget(helper.getWidgets().get(finalI4).getNumber(), finalRv4);
                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    //displaying the error in toast if occur
                                                }
                                            });
                                            queue1.add(stringReq1);
                                        } catch (Exception e) {
                                        }

                                        intent = new Intent(context, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                        rv.setOnClickPendingIntent(R.id.LlMediumWidget, configPendingIntent);
                                    } else {

                                    }
                                }

                            }
                        }
                        break;
                    case 10:
                        //todo weather 3 medium
                        LocationManager service2 = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                        if (service2.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            if (ActivityCompat.checkSelfPermission(
                                    context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                    context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            } else {
                                String city = "";

                                rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_weather3_medium);
                                List<String> providers = service2.getProviders(true);
                                for (String provider : providers) {
                                    Location locationGPS = service2.getLastKnownLocation(provider);
                                    if (locationGPS != null) {
                                        double lat = locationGPS.getLatitude();
                                        double longi = locationGPS.getLongitude();

                                        try {
                                            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                                            List<Address> addresses = geocoder.getFromLocation(locationGPS.getLatitude(), locationGPS.getLongitude(), 1);
                                            city = addresses.get(0).getLocality();

                                            AppWidgetData widgetsId = helper.getWidgetsId(insert);
                                            widgetsId.setCity(city);
                                            helper.updateWidget(widgetsId);

                                            RemoteViews finalRv4 = rv;
                                            RequestQueue queue = Volley.newRequestQueue(context);
                                            String url, tempExt;
                                            if (appWidgetData.getTemp() == 0) {
                                                url = MyAppConstants.BASE_URL_WEATHER + city + "&units=metric&APPID=" + context.getString(R.string.str_weather_key);
                                                tempExt = "°C";
                                            } else {
                                                url = MyAppConstants.BASE_URL_WEATHER + city + "&units=imperial&APPID=" + context.getString(R.string.str_weather_key);
                                                tempExt = "°F";
                                            }
                                            StringRequest stringReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject obj = new JSONObject(response);

                                                        JSONArray WeatherArray = obj.getJSONArray("weather");
                                                        for (int j = 0; j < WeatherArray.length(); j++) {
                                                            JSONObject WeatherObject = WeatherArray.getJSONObject(j);

                                                            WeatherObject.get("main");
                                                            WeatherObject.get("icon");
                                                            finalRv4.setTextViewText(R.id.TvDesc, WeatherObject.get("main").toString());
                                                            finalRv4.setImageViewResource(R.id.IvWeatherIcon, MyAppConstants.getWeatherIcons(WeatherObject.getString("icon")));
                                                        }
                                                        JSONObject MainObject = obj.getJSONObject("main");

                                                        JSONObject SysObject = obj.getJSONObject("sys");
                                                        finalRv4.setTextViewText(R.id.TvCity, obj.get("name") + "," + SysObject.get("country"));
                                                        String Temp = MainObject.get("temp").toString();
                                                        finalRv4.setTextViewText(R.id.TvTemp, Temp.substring(0, Temp.lastIndexOf(".")) + tempExt);
                                                        String MinTemp = MainObject.get("temp_min").toString();
                                                        String MaxTemp = MainObject.get("temp_max").toString();
                                                        finalRv4.setTextViewText(R.id.TvTempMaxMin, "H:" + MaxTemp.substring(0, MaxTemp.lastIndexOf(".")) + tempExt + " ~ L:" + MinTemp.substring(0, MinTemp.lastIndexOf(".")) + tempExt);

                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    //displaying the error in toast if occur
                                                }
                                            });
                                            queue.add(stringReq);

                                            RequestQueue queue1 = Volley.newRequestQueue(context);
                                            String url1, tempExt1;
                                            if (appWidgetData.getTemp() == 0) {
                                                url1 = MyAppConstants.BASE_URL_FORECAST + city + "&cnt=6&units=metric&APPID=" + context.getString(R.string.str_weather_key);
                                                tempExt1 = "°C";
                                            } else {
                                                url1 = MyAppConstants.BASE_URL_FORECAST + city + "&cnt=6&units=imperial&APPID=" + context.getString(R.string.str_weather_key);
                                                tempExt1 = "°F";
                                            }
                                            int finalI = i;
                                            StringRequest stringReq1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject MainObject = null;
                                                        JSONArray IconObject = null;
                                                        String Temp = null;
                                                        String res = null;

                                                        JSONObject obj = new JSONObject(response);

                                                        JSONArray WeatherArray = obj.getJSONArray("list");
                                                        for (int j = 0; j < WeatherArray.length(); j++) {
                                                            JSONObject WeatherObject = WeatherArray.getJSONObject(j);
                                                            String dateStr = WeatherObject.getString("dt_txt").substring(WeatherObject.getString("dt_txt").lastIndexOf(" "));
                                                            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                                                            try {
                                                                Date date = format.parse(dateStr);
                                                                format = new SimpleDateFormat("HH:mm");
                                                                res = format.format(date);
                                                            } catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }
                                                            switch (j) {
                                                                case 0:
                                                                    finalRv4.setTextViewText(R.id.TvTimeFirst, res.toString());
                                                                    MainObject = WeatherObject.getJSONObject("main");
                                                                    Temp = MainObject.getString("temp").toString();
                                                                    if (Temp.contains(".")) {
                                                                        finalRv4.setTextViewText(R.id.TvTempFirst, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                    } else {
                                                                        finalRv4.setTextViewText(R.id.TvTempFirst, Temp + "°");
                                                                    }
                                                                    IconObject = WeatherObject.getJSONArray("weather");
                                                                    finalRv4.setImageViewResource(R.id.IvWeatherIconFirst, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                    break;
                                                                case 1:
                                                                    finalRv4.setTextViewText(R.id.TvTimeSecond, res.toString());
                                                                    MainObject = WeatherObject.getJSONObject("main");
                                                                    Temp = MainObject.getString("temp").toString();
                                                                    if (Temp.contains(".")) {
                                                                        finalRv4.setTextViewText(R.id.TvTempSecond, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                    } else {
                                                                        finalRv4.setTextViewText(R.id.TvTempSecond, Temp + "°");
                                                                    }
                                                                    IconObject = WeatherObject.getJSONArray("weather");
                                                                    finalRv4.setImageViewResource(R.id.IvWeatherIconSecond, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                    break;
                                                                case 2:
                                                                    finalRv4.setTextViewText(R.id.TvTimeThird, res.toString());
                                                                    MainObject = WeatherObject.getJSONObject("main");
                                                                    Temp = MainObject.getString("temp").toString();
                                                                    if (Temp.contains(".")) {
                                                                        finalRv4.setTextViewText(R.id.TvTempThird, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                    } else {
                                                                        finalRv4.setTextViewText(R.id.TvTempThird, Temp + "°");
                                                                    }
                                                                    IconObject = WeatherObject.getJSONArray("weather");
                                                                    finalRv4.setImageViewResource(R.id.IvWeatherIconThird, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                    break;
                                                                case 3:
                                                                    finalRv4.setTextViewText(R.id.TvTimeForth, res.toString());
                                                                    MainObject = WeatherObject.getJSONObject("main");
                                                                    Temp = MainObject.getString("temp").toString();
                                                                    if (Temp.contains(".")) {
                                                                        finalRv4.setTextViewText(R.id.TvTempForth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                    } else {
                                                                        finalRv4.setTextViewText(R.id.TvTempForth, Temp + "°");
                                                                    }
                                                                    IconObject = WeatherObject.getJSONArray("weather");
                                                                    finalRv4.setImageViewResource(R.id.IvWeatherIconForth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                    break;
                                                                case 4:
                                                                    finalRv4.setTextViewText(R.id.TvTimeFifth, res.toString());
                                                                    MainObject = WeatherObject.getJSONObject("main");
                                                                    Temp = MainObject.getString("temp").toString();
                                                                    if (Temp.contains(".")) {
                                                                        finalRv4.setTextViewText(R.id.TvTempFifth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                    } else {
                                                                        finalRv4.setTextViewText(R.id.TvTempFifth, Temp + "°");
                                                                    }
                                                                    IconObject = WeatherObject.getJSONArray("weather");
                                                                    finalRv4.setImageViewResource(R.id.IvWeatherIconFifth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                    break;
                                                                case 5:
                                                                    finalRv4.setTextViewText(R.id.TvTimeSixth, res.toString());
                                                                    MainObject = WeatherObject.getJSONObject("main");
                                                                    Temp = MainObject.getString("temp").toString();
                                                                    if (Temp.contains(".")) {
                                                                        finalRv4.setTextViewText(R.id.TvTempSixth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                    } else {
                                                                        finalRv4.setTextViewText(R.id.TvTempSixth, Temp + "°");
                                                                    }
                                                                    IconObject = WeatherObject.getJSONArray("weather");
                                                                    finalRv4.setImageViewResource(R.id.IvWeatherIconSixth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                    break;
                                                            }
                                                        }

                                                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                                        Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                                                        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                                        new MyAppPref(context).putBoolean(MyAppPref.IS_WEATHER_1_ALARM, true);
                                                        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, broadcast);
                                                        appWidgetManager.updateAppWidget(helper.getWidgets().get(finalI).getNumber(), finalRv4);
                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    //displaying the error in toast if occur
                                                }
                                            });
                                            queue1.add(stringReq1);
                                        } catch (Exception e) {
                                        }

                                        intent = new Intent(context, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                        rv.setOnClickPendingIntent(R.id.LlMediumWidget, configPendingIntent);
                                    } else {

                                    }
                                }
                            }
                        }
                        break;
                    case 11:
                        //todo clock 1 medium
                        rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple1_medium);
                        intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                        rv.setOnClickPendingIntent(R.id.RlMediumClock, configPendingIntent);

                        startMillis = Calendar.getInstance().getTimeInMillis();
                        builder = CalendarContract.CONTENT_URI.buildUpon();
                        builder.appendPath("time");
                        ContentUris.appendId(builder, startMillis);
                        intent1 = new Intent(Intent.ACTION_VIEW)
                                .setData(builder.build());
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                        rv.setOnClickPendingIntent(R.id.LlMediumCalendar, configPendingIntent);

                        intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.LlMediumWidget, configPendingIntent);
                        break;
                    case 12:
                        //todo clock 2 medium
                        rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple2_medium);

                        intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                        rv.setOnClickPendingIntent(R.id.RlMediumClock, configPendingIntent);

                        startMillis = Calendar.getInstance().getTimeInMillis();
                        builder = CalendarContract.CONTENT_URI.buildUpon();
                        builder.appendPath("time");
                        ContentUris.appendId(builder, startMillis);
                        intent1 = new Intent(Intent.ACTION_VIEW)
                                .setData(builder.build());
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                        rv.setOnClickPendingIntent(R.id.LlMediumCalendar, configPendingIntent);

                        intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.LlMediumWidget, configPendingIntent);
                        break;
                    case 14:
                        //todo clock 4 medium
                        rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_text1_medium);

                        intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                        rv.setOnClickPendingIntent(R.id.RlMediumClock, configPendingIntent);

                        intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.LlMediumWidget, configPendingIntent);
                        break;
                    case 19:
                        //todo clock 9 medium
                        rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_text3_medium);

                        rv.setCharSequence(R.id.TClockHour, "setFormat12Hour", "HH");
                        rv.setCharSequence(R.id.TClockHour, "setFormat24Hour", "HH");
                        rv.setCharSequence(R.id.TClockMinutes, "setFormat12Hour", "mm");
                        rv.setCharSequence(R.id.TClockMinutes, "setFormat24Hour", "mm");
                        rv.setCharSequence(R.id.TClockDayMonthDate, "setFormat12Hour", "d EEEE");
                        rv.setCharSequence(R.id.TClockDayMonthDate, "setFormat24Hour", "d EEEE");

                        intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                        rv.setOnClickPendingIntent(R.id.RlMediumClock, configPendingIntent);

                        intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.LlMediumWidget, configPendingIntent);
                        break;
                    case 20:
                        //todo x-panel 1 medium
                        rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel1_medium);

                        CameraManager service = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            service.registerTorchCallback(new CameraManager.TorchCallback() {
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
                        if (MyAppConstants.IsWIfiConnected(context)) {
                            rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi1_selected);
                        } else {
                            rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi1);
                        }
                        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (defaultAdapter != null) {
                            if (defaultAdapter.isEnabled()) {
                                rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluethooth1_selected);
                            } else if (!defaultAdapter.isEnabled()) {
                                rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluethooth1);
                            }
                        }

                        if (helper.getWidgets().get(i).getSim()) {
                            rv.setImageViewResource(R.id.IvCellular, R.drawable.ic_celluer1_selected);
                        } else {
                            rv.setImageViewResource(R.id.IvCellular, R.drawable.ic_celluer1);
                        }
                        if (IsTorchOn) {
                            rv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1_selected);
                        } else {
                            rv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1);
                        }
                        if (!new MyAppPref(context).getBoolean(MyAppPref.IS_X_PANEL_1_ALARM, false)) {
                            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                            PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                            new MyAppPref(context).putBoolean(MyAppPref.IS_X_PANEL_1_ALARM, true);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, broadcast);
                        }

                        Intent intentWifi1 = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intentWifi1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.IvWifi, configPendingIntent);

                        Intent intentBluetooth1 = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intentBluetooth1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.IvBluetooth, configPendingIntent);

                        Intent intentCellular = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                        if (intentCellular.resolveActivity(context.getPackageManager()) != null) {
                            configPendingIntent = PendingIntent.getActivity(context, 0, intentCellular, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                            rv.setOnClickPendingIntent(R.id.IvCellular, configPendingIntent);
                        }

                        Intent intentTorch = new Intent(context, XPanelFlashlightWidgetReceiver.class);
                        PendingIntent broadcast1 = PendingIntent.getBroadcast(context, 0, intentTorch, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.IvTorch, broadcast1);

                        intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.LlMediumWidget, configPendingIntent);
                        break;
                    case 21:
                        //todo xpanel 2 medium
                        rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel4_medium);

                        rv.setCharSequence(R.id.TClockHr, "setFormat12Hour", "HH");
                        rv.setCharSequence(R.id.TClockHr, "setFormat24Hour", "HH");
                        rv.setCharSequence(R.id.TClockMin, "setFormat12Hour", "mm");
                        rv.setCharSequence(R.id.TClockMin, "setFormat24Hour", "mm");
                        rv.setCharSequence(R.id.TClockDay, "setFormat12Hour", "d EEEE");
                        rv.setCharSequence(R.id.TClockDay, "setFormat24Hour", "d EEEE");

                        BatteryManager batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
                        int managerIntProperty = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

                        long KILOBYTE = 1024;
                        StatFs internalStatFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
                        long internalTotal;
                        long internalFree;

                        StatFs externalStatFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
                        long externalTotal;
                        long externalFree;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            internalTotal = (internalStatFs.getBlockCountLong() * internalStatFs.getBlockSizeLong()) / (KILOBYTE * KILOBYTE);
                            internalFree = (internalStatFs.getAvailableBlocksLong() * internalStatFs.getBlockSizeLong()) / (KILOBYTE * KILOBYTE);
                            externalTotal = (externalStatFs.getBlockCountLong() * externalStatFs.getBlockSizeLong()) / (KILOBYTE * KILOBYTE);
                            externalFree = (externalStatFs.getAvailableBlocksLong() * externalStatFs.getBlockSizeLong()) / (KILOBYTE * KILOBYTE);
                        } else {
                            internalTotal = ((long) internalStatFs.getBlockCount() * (long) internalStatFs.getBlockSize()) / (KILOBYTE * KILOBYTE);
                            internalFree = ((long) internalStatFs.getAvailableBlocks() * (long) internalStatFs.getBlockSize()) / (KILOBYTE * KILOBYTE);
                            externalTotal = ((long) externalStatFs.getBlockCount() * (long) externalStatFs.getBlockSize()) / (KILOBYTE * KILOBYTE);
                            externalFree = ((long) externalStatFs.getAvailableBlocks() * (long) externalStatFs.getBlockSize()) / (KILOBYTE * KILOBYTE);
                        }

                        long total = internalTotal + externalTotal;
                        long free = internalFree + externalFree;
                        long used = total - free;
                        rv.setTextViewText(R.id.TvProgressText, managerIntProperty + "%");
                        rv.setTextViewText(R.id.storage_text, MyAppConstants.bytes2String(used) + "/" + MyAppConstants.bytes2String(total));

                        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            RemoteViews finalRv5 = rv;
                            cameraManager.registerTorchCallback(new CameraManager.TorchCallback() {
                                @Override
                                public void onTorchModeUnavailable(@NonNull String cameraId) {
                                    super.onTorchModeUnavailable(cameraId);
                                }

                                @Override
                                public void onTorchModeChanged(@NonNull String cameraId, boolean enabled) {
                                    super.onTorchModeChanged(cameraId, enabled);
                                    IsTorchOn = enabled;
                                    if (IsTorchOn) {
                                        finalRv5.setImageViewResource(R.id.IvTorch, R.drawable.ic_torch4_selected);
                                    } else {
                                        finalRv5.setImageViewResource(R.id.IvTorch, R.drawable.ic_torch4);
                                    }
                                }

                                @Override
                                public void onTorchStrengthLevelChanged(@NonNull String cameraId, int newStrengthLevel) {
                                    super.onTorchStrengthLevelChanged(cameraId, newStrengthLevel);
                                }
                            }, null);
                        }
                        if (MyAppConstants.IsWIfiConnected(context)) {
                            rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi4_selected);
                        } else {
                            rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi4);
                        }
                        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (mBluetoothAdapter != null) {
                            if (mBluetoothAdapter.isEnabled()) {
                                rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluetooth4_selected);
                            } else if (!mBluetoothAdapter.isEnabled()) {
                                rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluetooth4);
                            }
                        }

                        Intent intentWifi = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intentWifi, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.IvWifi, configPendingIntent);

                        Intent intentBluetooth = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intentBluetooth, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.IvBluetooth, configPendingIntent);

                        Intent intent2 = new Intent(context, XPanelFlashlight4WidgetReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.IvTorch, pendingIntent);

                        intent = new Intent(Settings.ACTION_INTERNAL_STORAGE_SETTINGS);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.LlStorage, configPendingIntent);

                        intent = new Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.LlStorage, configPendingIntent);

                        intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.LlTime, configPendingIntent);

                        if (!new MyAppPref(context).getBoolean(MyAppPref.IS_X_PANEL_4_ALARM, false)) {
                            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                            PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                            new MyAppPref(context).putBoolean(MyAppPref.IS_X_PANEL_4_ALARM, true);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, broadcast);
                        }

                        intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.LlMediumWidget, configPendingIntent);
                        break;

                }
                if (!(MyAppConstants.Widget_Type_Id == 8 || MyAppConstants.Widget_Type_Id == 9 || MyAppConstants.Widget_Type_Id == 10)) {
                    appWidgetManager.updateAppWidget(Widget_Id, rv);
                }
                MyAppConstants.CreateWidget = false;

            }
            super.onUpdate(context, appWidgetManager, appWidgetIds);
        }

        Intent widget_create = new Intent("Widget_create");
        LocalBroadcastManager.getInstance(context).sendBroadcast(widget_create);
    }

    @Override
    public void onDeleted(Context context, int[] iArr) {
        for (int id : iArr) {
            AppDatabaseHelper helper = new AppDatabaseHelper(context);
            helper.getDeleteWidgets(id);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);

    }
}

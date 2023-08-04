package com.ios.widget.provider;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
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
import android.os.StatFs;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.ios.widget.utils.MyAppConstants;
import com.ios.widget.utils.NotificationHelper;
import com.ios.widget.utils.MyAppPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BetteryBroadcastReceiver extends BroadcastReceiver {
    private boolean IsTorchOn;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intentReceiver) {
        NotificationHelper.showNonCancelableNotification(context, context.getString(R.string.app_name) + "Now", context.getString(R.string.app_name));
        DatabaseHelper helper = new DatabaseHelper(context);
        ArrayList<WidgetData> widgetData = helper.getWidgets();
        RemoteViews rv = null;
        PendingIntent configPendingIntent = null;

        long startMillis = 0;
        Uri.Builder builder = null;
        Intent intent = null;
        Intent intent1 = null;
        int currentDay;
        int currentMonth;
        int currentYear;
        for (int i = 0; i < widgetData.size(); i++) {
            if (widgetData.get(i).getPosition() == 0) {
                if (widgetData.get(i).getType() == 1) {
                    //todo clock 3 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple3_medium);

                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetData.get(i).getNumber());
                    intent.putExtra("TypeId", widgetData.get(i).getPosition());

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView, intent);
                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);

                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                    Calendar NewCalendar = Calendar.getInstance();
                    currentDay = NewCalendar.get(Calendar.DAY_OF_MONTH);
                    currentMonth = NewCalendar.get(Calendar.MONTH);
                    currentYear = NewCalendar.get(Calendar.YEAR);
                    if (!new MyAppPref(context).getString(MyAppPref.IS_TIME_3, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView);
                        new MyAppPref(context).putString(MyAppPref.IS_TIME_3, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                } else if (widgetData.get(i).getType() == 2) {
                    //todo x-panel 1 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel1_large);

                    if (MyAppConstants.IsWIfiConnected(context)) {
                        rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi1_selected);
                    } else {
                        rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi1);
                    }

                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter != null) {
                        if (mBluetoothAdapter.isEnabled()) {
                            rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluethooth1_selected);
                        } else if (!mBluetoothAdapter.isEnabled()) {
                            rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluethooth1);
                        }
                    }


                    if (MyAppConstants.isNetworkAvailable(context)) {
                        rv.setImageViewResource(R.id.IvCellular, R.drawable.ic_celluer1_selected);
                    } else {
                        rv.setImageViewResource(R.id.IvCellular, R.drawable.ic_celluer1);
                    }
                    CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        RemoteViews finalRv = rv;
                        int finalI = i;
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
                                    finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1_selected);
                                } else {
                                    finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1);
                                }

                                AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                                appWidgetManager.updateAppWidget(widgetData.get(finalI).getNumber(), finalRv);
                            }

                            @Override
                            public void onTorchStrengthLevelChanged(@NonNull String cameraId, int newStrengthLevel) {
                                super.onTorchStrengthLevelChanged(cameraId, newStrengthLevel);
                            }
                        }, null);
                    }

                    Intent intentWifi = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intentWifi, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvWifi, configPendingIntent);

                    Intent intentBluetooth = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intentBluetooth, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvBluetooth, configPendingIntent);

                    Intent intentCellular = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                    if (intentCellular.resolveActivity(context.getPackageManager()) != null) {
                        configPendingIntent = PendingIntent.getActivity(context, 0, intentCellular, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.IvCellular, configPendingIntent);
                    }

                    Intent intent2 = new Intent(context, XPanelFlashlightWidgetReceiver.class);
                    intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetData.get(i).getNumber());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvTorch, pendingIntent);

                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                }
            } else if (widgetData.get(i).getPosition() == 1) {
                if (widgetData.get(i).getType() == 0) {

                    //todo weather 1 small
                    LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        if (ActivityCompat.checkSelfPermission(
                                context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        } else {
                            String city = "";

                            rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_weather1_small);
                            List<String> providers = locationManager.getProviders(true);
                            for (String provider : providers) {
                                Location locationGPS = locationManager.getLastKnownLocation(provider);
                                if (locationGPS != null) {
                                    try {
                                        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                                        List<Address> addresses = geocoder.getFromLocation(locationGPS.getLatitude(), locationGPS.getLongitude(), 1);
                                        city = addresses.get(0).getLocality();

                                        RequestQueue queue = Volley.newRequestQueue(context);
                                        String url, tempExt;
                                        if (widgetData.get(i).getTemp() == 0) {
                                            url = MyAppConstants.BASE_URL_WEATHER + city + "&units=metric&APPID=" + context.getString(R.string.str_weather_key);
                                            tempExt = "°C";
                                        } else {
                                            url = MyAppConstants.BASE_URL_WEATHER + city + "&units=imperial&APPID=" + context.getString(R.string.str_weather_key);
                                            tempExt = "°F";
                                        }
                                        RemoteViews finalRv1 = rv;
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
                                                        String Url = "https://openweathermap.org/img/wn/" + WeatherObject.get("icon") + "@2x.png";
                                                        finalRv1.setTextViewText(R.id.TvDesc, WeatherObject.get("description").toString());

                                                    }
                                                    JSONObject MainObject = obj.getJSONObject("main");

                                                    JSONObject SysObject = obj.getJSONObject("sys");
                                                    finalRv1.setTextViewText(R.id.TvCity, obj.get("name") + "," + SysObject.get("country"));
                                                    finalRv1.setTextViewText(R.id.TvTemp, MainObject.get("temp") + tempExt);

                                                    String MinTemp = MainObject.get("temp_min").toString();
                                                    String MaxTemp = MainObject.get("temp_max").toString();
                                                    finalRv1.setTextViewText(R.id.TvTempMaxMin, "H:" + MaxTemp.substring(0, MaxTemp.lastIndexOf(".")) + tempExt + " L:" + MinTemp.substring(0, MinTemp.lastIndexOf(".")) + tempExt);

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
                                    } catch (Exception e) {
                                    }
                                } else {

                                }
                            }
                            AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                            appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                            PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                        }
                    }
                } else if (widgetData.get(i).getType() == 1) {
                    //todo calender 4 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar4_medium);
                    rv.setCharSequence(R.id.TClockDay, "setFormat12Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockDay, "setFormat24Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMMM");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMMM");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");

                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetData.get(i).getNumber());
                    intent.putExtra("TypeId", widgetData.get(i).getPosition());

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView, intent);
                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);

                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                    Calendar NewCalendar = Calendar.getInstance();
                    currentDay = NewCalendar.get(Calendar.DAY_OF_MONTH);
                    currentMonth = NewCalendar.get(Calendar.MONTH);
                    currentYear = NewCalendar.get(Calendar.YEAR);
                    if (!new MyAppPref(context).getString(MyAppPref.IS_DATE_4, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView);
                        new MyAppPref(context).putString(MyAppPref.IS_DATE_4, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                }
            } else if (widgetData.get(i).getPosition() == 2) {
                if (widgetData.get(i).getType() == 1) {

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
                        RemoteViews finalRv = rv;
                        int finalI = i;
                        manager.registerTorchCallback(new CameraManager.TorchCallback() {
                            @Override
                            public void onTorchModeUnavailable(@NonNull String cameraId) {
                                super.onTorchModeUnavailable(cameraId);
                            }

                            @Override
                            public void onTorchModeChanged(@NonNull String cameraId, boolean enabled) {
                                super.onTorchModeChanged(cameraId, enabled);
                                IsTorchOn = enabled;
                                if (widgetData.get(finalI).getType() == 2) {
                                    if (IsTorchOn) {
                                        finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1_selected);
                                    } else {
                                        finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1);
                                    }
                                } else {
                                    if (IsTorchOn) {
                                        finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_xpanel_medium_2_flashlight_selected);
                                    } else {
                                        finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_xpanel_medium_2_flashlight);
                                    }
                                }

                                AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                                appWidgetManager.updateAppWidget(widgetData.get(finalI).getNumber(), finalRv);
                            }

                            @Override
                            public void onTorchStrengthLevelChanged(@NonNull String cameraId, int newStrengthLevel) {
                                super.onTorchStrengthLevelChanged(cameraId, newStrengthLevel);
                            }
                        }, null);
                    }
                    if (widgetData.get(i).getType() == 2) {
                        if (MyAppConstants.IsWIfiConnected(context)) {
                            rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi1_selected);
                        } else {
                            rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi1);
                        }
                        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (bluetoothAdapter != null) {
                            if (bluetoothAdapter.isEnabled()) {
                                rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluethooth1_selected);
                            } else if (!bluetoothAdapter.isEnabled()) {
                                rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluethooth1);
                            }
                        }
                    } else {
                        if (MyAppConstants.IsWIfiConnected(context)) {
                            rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_xpanel_medium_2_wifi_selected);
                        } else {
                            rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_xpanel_medium_2_wifi);
                        }
                        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (bluetoothAdapter != null) {
                            if (bluetoothAdapter.isEnabled()) {
                                rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_xpanel_medium_2_bluetooth_selected);
                            } else if (!bluetoothAdapter.isEnabled()) {
                                rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_xpanel_medium_2_bluetooth);
                            }
                        }
                    }
                    final float totalSpace = MyAppConstants.DeviceMemory.getInternalStorageSpace();
                    final float occupiedSpace = MyAppConstants.DeviceMemory.getInternalUsedSpace();
                    final float freeSpace = MyAppConstants.DeviceMemory.getInternalFreeSpace();

                    rv.setProgressBar(R.id.progressBarCharge, 100, property, false);
                    rv.setProgressBar(R.id.progressBarStorage, (int) totalSpace, (int) occupiedSpace, false);

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

                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                } else if (widgetData.get(i).getType() == 2) {

                    //todo weather 2 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_weather2_large);

                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                }
            } else if (widgetData.get(i).getPosition() == 4) {
                if (widgetData.get(i).getType() == 1) {

                    //todo calender 1 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar1_medium);
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMM yyyy");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMM yyyy");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");

                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetData.get(i).getNumber());
                    intent.putExtra("TypeId", widgetData.get(i).getPosition());
                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView, intent);

                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);

                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                    Calendar NewCalendar = Calendar.getInstance();
                    currentDay = NewCalendar.get(Calendar.DAY_OF_MONTH);
                    currentMonth = NewCalendar.get(Calendar.MONTH);
                    currentYear = NewCalendar.get(Calendar.YEAR);
                    if (!new MyAppPref(context).getString(MyAppPref.IS_DATE_1, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView);
                        new MyAppPref(context).putString(MyAppPref.IS_DATE_1, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                } else if (widgetData.get(i).getType() == 2) {

                    //todo calender 1 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar1_large);
                    rv.setImageViewBitmap(R.id.IvBackground, MyAppConstants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_widget_calendar1_small_bg), 30));
                    rv.setCharSequence(R.id.TClockYear, "setFormat12Hour", "yyyy");
                    rv.setCharSequence(R.id.TClockYear, "setFormat24Hour", "yyyy");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMMM");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMMM");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                    intent = new Intent(context, LargeWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetData.get(i).getNumber());
                    intent.putExtra("TypeId", widgetData.get(i).getPosition());

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(widgetData.get(i).getNumber(), R.id.GridCalendarLargeView, intent);

                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlLargeCal, configPendingIntent);

                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                    Calendar NewCalendar = Calendar.getInstance();
                    currentDay = NewCalendar.get(Calendar.DAY_OF_MONTH);
                    currentMonth = NewCalendar.get(Calendar.MONTH);
                    currentYear = NewCalendar.get(Calendar.YEAR);
                    if (!new MyAppPref(context).getString(MyAppPref.IS_DATE_LARGE_1, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarLargeView);
                        new MyAppPref(context).putString(MyAppPref.IS_DATE_LARGE_1, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                }
            } else if (widgetData.get(i).getPosition() == 5) {
                if (widgetData.get(i).getType() == 1) {

                    //todo calender 2 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar3_medium);
                    rv.setImageViewBitmap(R.id.IvBackground, MyAppConstants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_widget_calendar2_medium_bg), 30));
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "EEE, yyyy");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "EEE, yyyy");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");

                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetData.get(i).getNumber());
                    intent.putExtra("TypeId", widgetData.get(i).getPosition());
                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView, intent);

                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);

                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                    Calendar NewCalendar = Calendar.getInstance();
                    currentDay = NewCalendar.get(Calendar.DAY_OF_MONTH);
                    currentMonth = NewCalendar.get(Calendar.MONTH);
                    currentYear = NewCalendar.get(Calendar.YEAR);
                    if (!new MyAppPref(context).getString(MyAppPref.IS_DATE_3, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView);
                        new MyAppPref(context).putString(MyAppPref.IS_DATE_3, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                }
            } else if (widgetData.get(i).getPosition() == 6) {
                if (widgetData.get(i).getType() == 1) {

                    //todo calender 3 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar2_medium);
                    rv.setCharSequence(R.id.TClockDay, "setFormat12Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockDay, "setFormat24Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMMM yyyy");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMMM yyyy");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetData.get(i).getNumber());
                    intent.putExtra("TypeId", widgetData.get(i).getPosition());
                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView, intent);

                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);

                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                    Calendar NewCalendar = Calendar.getInstance();
                    currentDay = NewCalendar.get(Calendar.DAY_OF_MONTH);
                    currentMonth = NewCalendar.get(Calendar.MONTH);
                    currentYear = NewCalendar.get(Calendar.YEAR);
                    if (!new MyAppPref(context).getString(MyAppPref.IS_DATE_2, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView);
                        new MyAppPref(context).putString(MyAppPref.IS_DATE_2, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                } else if (widgetData.get(i).getType() == 2) {

                    //todo calender 3 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar2_large);

                    intent = new Intent(context, LargeWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetData.get(i).getNumber());
                    intent.putExtra("TypeId", widgetData.get(i).getPosition());

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(widgetData.get(i).getNumber(), R.id.GridCalendarLargeView, intent);

                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlLargeCal, configPendingIntent);

                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                    Calendar NewCalendar = Calendar.getInstance();
                    currentDay = NewCalendar.get(Calendar.DAY_OF_MONTH);
                    currentMonth = NewCalendar.get(Calendar.MONTH);
                    currentYear = NewCalendar.get(Calendar.YEAR);
                    if (!new MyAppPref(context).getString(MyAppPref.IS_DATE_LARGE_3, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarLargeView);
                        new MyAppPref(context).putString(MyAppPref.IS_DATE_LARGE_3, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                }
            } else if (widgetData.get(i).getPosition() == 7) {
                if (widgetData.get(i).getType() == 1) {

                    //todo calender 4 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar4_medium);
                    rv.setCharSequence(R.id.TClockDay, "setFormat12Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockDay, "setFormat24Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMMM");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMMM");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");

                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetData.get(i).getNumber());
                    intent.putExtra("TypeId", widgetData.get(i).getPosition());
                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView, intent);
                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);

                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                    Calendar NewCalendar = Calendar.getInstance();
                    currentDay = NewCalendar.get(Calendar.DAY_OF_MONTH);
                    currentMonth = NewCalendar.get(Calendar.MONTH);
                    currentYear = NewCalendar.get(Calendar.YEAR);
                    if (!new MyAppPref(context).getString(MyAppPref.IS_DATE_4, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView);
                        new MyAppPref(context).putString(MyAppPref.IS_DATE_4, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                } else if (widgetData.get(i).getType() == 2) {

                    //todo calender 4 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar4_large);
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMMM, yyyy");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMMM, yyyy");
                    rv.setCharSequence(R.id.TClockDay, "setFormat12Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockDay, "setFormat24Hour", "EEEE");

                    intent = new Intent(context, LargeWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetData.get(i).getNumber());
                    intent.putExtra("TypeId", widgetData.get(i).getPosition());

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(widgetData.get(i).getNumber(), R.id.GridCalendarLargeView, intent);
                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlLargeCal, configPendingIntent);

                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                    Calendar NewCalendar = Calendar.getInstance();
                    currentDay = NewCalendar.get(Calendar.DAY_OF_MONTH);
                    currentMonth = NewCalendar.get(Calendar.MONTH);
                    currentYear = NewCalendar.get(Calendar.YEAR);
                    if (!new MyAppPref(context).getString(MyAppPref.IS_DATE_LARGE_4, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarLargeView);
                        new MyAppPref(context).putString(MyAppPref.IS_DATE_LARGE_4, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                }
            } else if (widgetData.get(i).getPosition() == 8) {
                String city = widgetData.get(i).getCity();
                if (widgetData.get(i).getType() == 0) {

                    //todo weather 1 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_weather1_small);
                    RequestQueue queue = Volley.newRequestQueue(context);
                    String url, tempExt;
                    if (widgetData.get(i).getTemp() == 0) {
                        url = MyAppConstants.BASE_URL_WEATHER + city + "&units=metric&APPID=" + context.getString(R.string.str_weather_key);
                        tempExt = "°C";
                    } else {
                        url = MyAppConstants.BASE_URL_WEATHER + city + "&units=imperial&APPID=" + context.getString(R.string.str_weather_key);
                        tempExt = "°F";
                    }
                    RemoteViews finalRv1 = rv;
                    int finalI1 = i;
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
                                    finalRv1.setTextViewText(R.id.TvDesc, WeatherObject.get("description").toString());
                                    finalRv1.setImageViewResource(R.id.IvWeatherIcon, MyAppConstants.getWeatherIcons(WeatherObject.getString("icon")));
                                }
                                JSONObject MainObject = obj.getJSONObject("main");

                                JSONObject SysObject = obj.getJSONObject("sys");
                                finalRv1.setTextViewText(R.id.TvCity, obj.get("name") + "," + SysObject.get("country"));
                                String Temp = MainObject.get("temp").toString();
                                finalRv1.setTextViewText(R.id.TvTemp, Temp.substring(0, Temp.lastIndexOf(".")) + tempExt);
                                String MinTemp = MainObject.get("temp_min").toString();
                                String MaxTemp = MainObject.get("temp_max").toString();
                                finalRv1.setTextViewText(R.id.TvTempMaxMin, "H:" + MaxTemp.substring(0, MaxTemp.lastIndexOf(".")) + tempExt + " / L:" + MinTemp.substring(0, MinTemp.lastIndexOf(".")) + tempExt);

                                AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                                appWidgetManager.updateAppWidget(widgetData.get(finalI1).getNumber(), finalRv1);
                                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                                PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
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
                } else if (widgetData.get(i).getType() == 1) {

                    //todo weather 1 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_weather1_medium);

                    RemoteViews finalRv4 = rv;
                    RequestQueue queue = Volley.newRequestQueue(context);
                    String url, tempExt;
                    if (widgetData.get(i).getTemp() == 0) {
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
                                    finalRv4.setTextViewText(R.id.TvDesc, WeatherObject.get("description").toString());
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
                            //displaying the error in toast if occur
                        }
                    });
                    queue.add(stringReq);

                    RequestQueue queue1 = Volley.newRequestQueue(context);
                    String url1, tempExt1;
                    if (widgetData.get(i).getTemp() == 0) {
                        url1 = MyAppConstants.BASE_URL_FORECAST + widgetData.get(i).getCity() + "&cnt=6&units=metric&APPID=" + context.getString(R.string.str_weather_key);
                        tempExt1 = "°C";
                    } else {
                        url1 = MyAppConstants.BASE_URL_FORECAST + widgetData.get(i).getCity() + "&cnt=6&units=imperial&APPID=" + context.getString(R.string.str_weather_key);
                        tempExt1 = "°F";
                    }
                    int finalI2 = i;
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
                                            finalRv4.setTextViewText(R.id.TvTempFirst, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconFirst, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                            break;
                                        case 1:
                                            finalRv4.setTextViewText(R.id.TvTimeSecond, res.toString());
                                            MainObject = WeatherObject.getJSONObject("main");
                                            Temp = MainObject.getString("temp").toString();
                                            finalRv4.setTextViewText(R.id.TvTempSecond, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconSecond, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                            break;
                                        case 2:
                                            finalRv4.setTextViewText(R.id.TvTimeThird, res.toString());
                                            MainObject = WeatherObject.getJSONObject("main");
                                            Temp = MainObject.getString("temp").toString();
                                            finalRv4.setTextViewText(R.id.TvTempThird, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconThird, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                            break;
                                        case 3:
                                            finalRv4.setTextViewText(R.id.TvTimeForth, res.toString());
                                            MainObject = WeatherObject.getJSONObject("main");
                                            Temp = MainObject.getString("temp").toString();
                                            finalRv4.setTextViewText(R.id.TvTempForth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconForth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                            break;
                                        case 4:
                                            finalRv4.setTextViewText(R.id.TvTimeFifth, res.toString());
                                            MainObject = WeatherObject.getJSONObject("main");
                                            Temp = MainObject.getString("temp").toString();
                                            finalRv4.setTextViewText(R.id.TvTempFifth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconFifth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                            break;
                                        case 5:
                                            finalRv4.setTextViewText(R.id.TvTimeSixth, res.toString());
                                            MainObject = WeatherObject.getJSONObject("main");
                                            Temp = MainObject.getString("temp").toString();
                                            finalRv4.setTextViewText(R.id.TvTempSixth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconSixth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                            break;
                                    }
                                }
                                AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                                appWidgetManager.updateAppWidget(widgetData.get(finalI2).getNumber(), finalRv4);
                                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                                PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);

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
                } else if (widgetData.get(i).getType() == 2) {

                    //todo weather 1 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_weather1_large);
                    try {

                        RemoteViews finalRv4 = rv;
                        RequestQueue queue = Volley.newRequestQueue(context);
                        String url, tempExt;
                        if (widgetData.get(i).getTemp() == 0) {
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
                                        finalRv4.setTextViewText(R.id.TvDesc, WeatherObject.get("description").toString());
                                        finalRv4.setImageViewResource(R.id.IvWeatherIcon, MyAppConstants.getWeatherIcons(WeatherObject.getString("icon")));
                                    }
                                    JSONObject MainObject = obj.getJSONObject("main");

                                    JSONObject SysObject = obj.getJSONObject("sys");
                                    long millisecond = Long.parseLong(SysObject.getString("sunrise"));
                                    String dateString = android.text.format.DateFormat.format("HH:mm:ss a", new Date(millisecond)).toString();
                                    finalRv4.setTextViewText(R.id.TvSunRise, dateString);
                                    long millisecondset = Long.parseLong(SysObject.getString("sunset"));
                                    String dateStringset = DateFormat.format("HH:mm:ss a", new Date(millisecondset)).toString();
                                    finalRv4.setTextViewText(R.id.TvSunSet, dateStringset);
                                    JSONObject WindObject = obj.getJSONObject("wind");
                                    double millisecondWind = Double.parseDouble(WindObject.getString("speed"));
                                    finalRv4.setTextViewText(R.id.TvWind, mps_to_kmph(millisecondWind) + "km/h");
                                    finalRv4.setTextViewText(R.id.TvCity, obj.get("name") + "," + SysObject.get("country"));
                                    String Temp = MainObject.get("temp").toString();
                                    finalRv4.setTextViewText(R.id.TvTemp, Temp.substring(0, Temp.lastIndexOf(".")) + tempExt);
                                    String MinTemp = MainObject.get("temp_min").toString();
                                    String MaxTemp = MainObject.get("temp_max").toString();
                                    finalRv4.setTextViewText(R.id.TvTempMaxMin, "H:" + MaxTemp.substring(0, MaxTemp.lastIndexOf(".")) + tempExt + " / L:" + MinTemp.substring(0, MinTemp.lastIndexOf(".")) + tempExt);
                                    finalRv4.setTextViewText(R.id.TvHumidity, MainObject.get("humidity").toString() + "%");
                                    finalRv4.setTextViewText(R.id.TvPressure, MainObject.get("pressure").toString() + "%");

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
                        if (widgetData.get(i).getTemp() == 0) {
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
                                                finalRv4.setTextViewText(R.id.TvTempFirst, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                IconObject = WeatherObject.getJSONArray("weather");
                                                finalRv4.setImageViewResource(R.id.IvWeatherIconFirst, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                break;
                                            case 1:
                                                finalRv4.setTextViewText(R.id.TvTimeSecond, res.toString());
                                                MainObject = WeatherObject.getJSONObject("main");
                                                Temp = MainObject.getString("temp").toString();
                                                finalRv4.setTextViewText(R.id.TvTempSecond, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                IconObject = WeatherObject.getJSONArray("weather");
                                                finalRv4.setImageViewResource(R.id.IvWeatherIconSecond, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                break;
                                            case 2:
                                                finalRv4.setTextViewText(R.id.TvTimeThird, res.toString());
                                                MainObject = WeatherObject.getJSONObject("main");
                                                Temp = MainObject.getString("temp").toString();
                                                finalRv4.setTextViewText(R.id.TvTempThird, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                IconObject = WeatherObject.getJSONArray("weather");
                                                finalRv4.setImageViewResource(R.id.IvWeatherIconThird, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                break;
                                            case 3:
                                                finalRv4.setTextViewText(R.id.TvTimeForth, res.toString());
                                                MainObject = WeatherObject.getJSONObject("main");
                                                Temp = MainObject.getString("temp").toString();
                                                finalRv4.setTextViewText(R.id.TvTempForth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                IconObject = WeatherObject.getJSONArray("weather");
                                                finalRv4.setImageViewResource(R.id.IvWeatherIconForth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                break;
                                            case 4:
                                                finalRv4.setTextViewText(R.id.TvTimeFifth, res.toString());
                                                MainObject = WeatherObject.getJSONObject("main");
                                                Temp = MainObject.getString("temp").toString();
                                                finalRv4.setTextViewText(R.id.TvTempFifth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                IconObject = WeatherObject.getJSONArray("weather");
                                                finalRv4.setImageViewResource(R.id.IvWeatherIconFifth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                break;
                                            case 5:
                                                finalRv4.setTextViewText(R.id.TvTimeSixth, res.toString());
                                                MainObject = WeatherObject.getJSONObject("main");
                                                Temp = MainObject.getString("temp").toString();
                                                finalRv4.setTextViewText(R.id.TvTempSixth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                IconObject = WeatherObject.getJSONArray("weather");
                                                finalRv4.setImageViewResource(R.id.IvWeatherIconSixth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                break;
                                        }
                                    }

//                                                    if (!new Pref(context).getBoolean(Pref.IS_WEATHER_1 _ALARM, false)) {
                                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                                    appWidgetManager.updateAppWidget(widgetData.get(finalI4).getNumber(), finalRv4);
                                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);

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
                }

            } else if (widgetData.get(i).getPosition() == 9) {
                String city = widgetData.get(i).getCity();
                if (widgetData.get(i).getType() == 0) {
                    //todo weather 2 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_weather2_small);
                    RequestQueue queue = Volley.newRequestQueue(context);
                    String url, tempExt;
                    if (widgetData.get(i).getTemp() == 0) {
                        url = MyAppConstants.BASE_URL_WEATHER + city + "&units=metric&APPID=" + context.getString(R.string.str_weather_key);
                        tempExt = "°C";
                    } else {
                        url = MyAppConstants.BASE_URL_WEATHER + city + "&units=imperial&APPID=" + context.getString(R.string.str_weather_key);
                        tempExt = "°F";
                    }
                    RemoteViews finalRv1 = rv;
                    int finalI1 = i;
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
                                    finalRv1.setTextViewText(R.id.TvDesc, WeatherObject.get("main").toString());
                                    finalRv1.setImageViewResource(R.id.IvWeatherIcon, MyAppConstants.getWeatherIcons(WeatherObject.getString("icon")));
                                }
                                JSONObject MainObject = obj.getJSONObject("main");

                                JSONObject SysObject = obj.getJSONObject("sys");
                                finalRv1.setTextViewText(R.id.TvCity, obj.get("name") + "," + SysObject.get("country"));
                                String Temp = MainObject.get("temp").toString();
                                finalRv1.setTextViewText(R.id.TvTemp, Temp.substring(0, Temp.lastIndexOf(".")) + tempExt);
                                String MinTemp = MainObject.get("temp_min").toString();
                                String MaxTemp = MainObject.get("temp_max").toString();
                                finalRv1.setTextViewText(R.id.TvTempMaxMin, "H:" + MaxTemp.substring(0, MaxTemp.lastIndexOf(".")) + tempExt + " L:" + MinTemp.substring(0, MinTemp.lastIndexOf(".")) + tempExt);

                                AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                                appWidgetManager.updateAppWidget(widgetData.get(finalI1).getNumber(), finalRv1);
                                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                                PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
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
                } else if (widgetData.get(i).getType() == 1) {

                    //todo weather 2 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_weather2_medium);
                    RemoteViews finalRv4 = rv;
                    RequestQueue queue = Volley.newRequestQueue(context);
                    String url, tempExt;
                    if (widgetData.get(i).getTemp() == 0) {
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
                                    finalRv4.setTextViewText(R.id.TvDesc, WeatherObject.get("description").toString());
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
                    if (widgetData.get(i).getTemp() == 0) {
                        url1 = MyAppConstants.BASE_URL_FORECAST + widgetData.get(i).getCity() + "&cnt=6&units=metric&APPID=" + context.getString(R.string.str_weather_key);
                        tempExt1 = "°C";
                    } else {
                        url1 = MyAppConstants.BASE_URL_FORECAST + widgetData.get(i).getCity() + "&cnt=6&units=imperial&APPID=" + context.getString(R.string.str_weather_key);
                        tempExt1 = "°F";
                    }
                    int finalI2 = i;
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
                                            finalRv4.setTextViewText(R.id.TvTempFirst, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconFirst, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                            break;
                                        case 1:
                                            finalRv4.setTextViewText(R.id.TvTimeSecond, res.toString());
                                            MainObject = WeatherObject.getJSONObject("main");
                                            Temp = MainObject.getString("temp").toString();
                                            finalRv4.setTextViewText(R.id.TvTempSecond, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconSecond, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                            break;
                                        case 2:
                                            finalRv4.setTextViewText(R.id.TvTimeThird, res.toString());
                                            MainObject = WeatherObject.getJSONObject("main");
                                            Temp = MainObject.getString("temp").toString();
                                            finalRv4.setTextViewText(R.id.TvTempThird, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconThird, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                            break;
                                        case 3:
                                            finalRv4.setTextViewText(R.id.TvTimeForth, res.toString());
                                            MainObject = WeatherObject.getJSONObject("main");
                                            Temp = MainObject.getString("temp").toString();
                                            finalRv4.setTextViewText(R.id.TvTempForth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconForth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                            break;
                                        case 4:
                                            finalRv4.setTextViewText(R.id.TvTimeFifth, res.toString());
                                            MainObject = WeatherObject.getJSONObject("main");
                                            Temp = MainObject.getString("temp").toString();
                                            finalRv4.setTextViewText(R.id.TvTempFifth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconFifth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                            break;
                                        case 5:
                                            finalRv4.setTextViewText(R.id.TvTimeSixth, res.toString());
                                            MainObject = WeatherObject.getJSONObject("main");
                                            Temp = MainObject.getString("temp").toString();
                                            finalRv4.setTextViewText(R.id.TvTempSixth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconSixth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                            break;
                                    }
                                }
                                AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                                appWidgetManager.updateAppWidget(widgetData.get(finalI2).getNumber(), finalRv4);
                                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                                PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);

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
                } else if (widgetData.get(i).getType() == 2) {

                    //todo weather 2 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_weather2_large);
                    try {

                        RemoteViews finalRv4 = rv;
                        RequestQueue queue = Volley.newRequestQueue(context);
                        String url, tempExt;
                        if (widgetData.get(i).getTemp() == 0) {
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
                                        finalRv4.setTextViewText(R.id.TvDesc, WeatherObject.get("description").toString());
                                        finalRv4.setImageViewResource(R.id.IvWeatherIcon, MyAppConstants.getWeatherIcons(WeatherObject.getString("icon")));
                                    }
                                    JSONObject MainObject = obj.getJSONObject("main");

                                    JSONObject SysObject = obj.getJSONObject("sys");
                                    long millisecond = Long.parseLong(SysObject.getString("sunrise"));
                                    String dateString = android.text.format.DateFormat.format("HH:mm:ss a", new Date(millisecond)).toString();
                                    finalRv4.setTextViewText(R.id.TvSunRise, dateString);
                                    long millisecondset = Long.parseLong(SysObject.getString("sunset"));
                                    String dateStringset = DateFormat.format("HH:mm:ss a", new Date(millisecondset)).toString();
                                    finalRv4.setTextViewText(R.id.TvSunSet, dateStringset);
                                    JSONObject WindObject = obj.getJSONObject("wind");
                                    double millisecondWind = Double.parseDouble(WindObject.getString("speed"));
                                    finalRv4.setTextViewText(R.id.TvWind, mps_to_kmph(millisecondWind) + "km/h");
                                    finalRv4.setTextViewText(R.id.TvCity, obj.get("name") + "," + SysObject.get("country"));
                                    String Temp = MainObject.get("temp").toString();
                                    finalRv4.setTextViewText(R.id.TvTemp, Temp.substring(0, Temp.lastIndexOf(".")) + tempExt);
                                    String MinTemp = MainObject.get("temp_min").toString();
                                    String MaxTemp = MainObject.get("temp_max").toString();
                                    finalRv4.setTextViewText(R.id.TvTempMaxMin, "H:" + MaxTemp.substring(0, MaxTemp.lastIndexOf(".")) + tempExt + "\nL:" + MinTemp.substring(0, MinTemp.lastIndexOf(".")) + tempExt);
                                    finalRv4.setTextViewText(R.id.TvHumidity, MainObject.get("humidity").toString() + "%");
                                    finalRv4.setTextViewText(R.id.TvPressure, MainObject.get("pressure").toString() + "%");

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
                        if (widgetData.get(i).getTemp() == 0) {
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
                                                finalRv4.setTextViewText(R.id.TvTempFirst, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                IconObject = WeatherObject.getJSONArray("weather");
                                                finalRv4.setImageViewResource(R.id.IvWeatherIconFirst, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                break;
                                            case 1:
                                                finalRv4.setTextViewText(R.id.TvTimeSecond, res.toString());
                                                MainObject = WeatherObject.getJSONObject("main");
                                                Temp = MainObject.getString("temp").toString();
                                                finalRv4.setTextViewText(R.id.TvTempSecond, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                IconObject = WeatherObject.getJSONArray("weather");
                                                finalRv4.setImageViewResource(R.id.IvWeatherIconSecond, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                break;
                                            case 2:
                                                finalRv4.setTextViewText(R.id.TvTimeThird, res.toString());
                                                MainObject = WeatherObject.getJSONObject("main");
                                                Temp = MainObject.getString("temp").toString();
                                                finalRv4.setTextViewText(R.id.TvTempThird, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                IconObject = WeatherObject.getJSONArray("weather");
                                                finalRv4.setImageViewResource(R.id.IvWeatherIconThird, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                break;
                                            case 3:
                                                finalRv4.setTextViewText(R.id.TvTimeForth, res.toString());
                                                MainObject = WeatherObject.getJSONObject("main");
                                                Temp = MainObject.getString("temp").toString();
                                                finalRv4.setTextViewText(R.id.TvTempForth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                IconObject = WeatherObject.getJSONArray("weather");
                                                finalRv4.setImageViewResource(R.id.IvWeatherIconForth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                break;
                                            case 4:
                                                finalRv4.setTextViewText(R.id.TvTimeFifth, res.toString());
                                                MainObject = WeatherObject.getJSONObject("main");
                                                Temp = MainObject.getString("temp").toString();
                                                finalRv4.setTextViewText(R.id.TvTempFifth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                IconObject = WeatherObject.getJSONArray("weather");
                                                finalRv4.setImageViewResource(R.id.IvWeatherIconFifth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                break;
                                            case 5:
                                                finalRv4.setTextViewText(R.id.TvTimeSixth, res.toString());
                                                MainObject = WeatherObject.getJSONObject("main");
                                                Temp = MainObject.getString("temp").toString();
                                                finalRv4.setTextViewText(R.id.TvTempSixth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                IconObject = WeatherObject.getJSONArray("weather");
                                                finalRv4.setImageViewResource(R.id.IvWeatherIconSixth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                break;
                                        }
                                    }

                                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                                    appWidgetManager.updateAppWidget(widgetData.get(finalI4).getNumber(), finalRv4);
                                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);

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
                }
                AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);

            } else if (widgetData.get(i).getPosition() == 10) {
                String city = widgetData.get(i).getCity();
                if (widgetData.get(i).getType() == 0) {
                    //todo weather 3 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_weather3_small);
                    RequestQueue queue = Volley.newRequestQueue(context);
                    String url, tempExt;
                    if (widgetData.get(i).getTemp() == 0) {
                        url = MyAppConstants.BASE_URL_WEATHER + city + "&units=metric&APPID=" + context.getString(R.string.str_weather_key);
                        tempExt = "°C";
                    } else {
                        url = MyAppConstants.BASE_URL_WEATHER + city + "&units=imperial&APPID=" + context.getString(R.string.str_weather_key);
                        tempExt = "°F";
                    }
                    RemoteViews finalRv1 = rv;
                    int finalI1 = i;
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
                                    finalRv1.setTextViewText(R.id.TvDesc, WeatherObject.get("main").toString());
                                    finalRv1.setImageViewResource(R.id.IvWeatherIcon, MyAppConstants.getWeatherIcons(WeatherObject.getString("icon")));
                                }
                                JSONObject MainObject = obj.getJSONObject("main");

                                JSONObject SysObject = obj.getJSONObject("sys");
                                finalRv1.setTextViewText(R.id.TvCity, obj.get("name") + "," + SysObject.get("country"));
                                String Temp = MainObject.get("temp").toString();
                                finalRv1.setTextViewText(R.id.TvTemp, Temp.substring(0, Temp.lastIndexOf(".")) + tempExt);
                                String MinTemp = MainObject.get("temp_min").toString();
                                String MaxTemp = MainObject.get("temp_max").toString();
                                finalRv1.setTextViewText(R.id.TvTempMaxMin, "H:" + MaxTemp.substring(0, MaxTemp.lastIndexOf(".")) + tempExt + " ~ L:" + MinTemp.substring(0, MinTemp.lastIndexOf(".")) + tempExt);

                                AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                                appWidgetManager.updateAppWidget(widgetData.get(finalI1).getNumber(), finalRv1);
                                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                                PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
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
                } else if (widgetData.get(i).getType() == 1) {

                    //todo weather 3 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_weather3_medium);
                    RemoteViews finalRv4 = rv;
                    RequestQueue queue = Volley.newRequestQueue(context);
                    String url, tempExt;
                    if (widgetData.get(i).getTemp() == 0) {
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
                    if (widgetData.get(i).getTemp() == 0) {
                        url1 = MyAppConstants.BASE_URL_FORECAST + widgetData.get(i).getCity() + "&cnt=6&units=metric&APPID=" + context.getString(R.string.str_weather_key);
                        tempExt1 = "°C";
                    } else {
                        url1 = MyAppConstants.BASE_URL_FORECAST + widgetData.get(i).getCity() + "&cnt=6&units=imperial&APPID=" + context.getString(R.string.str_weather_key);
                        tempExt1 = "°F";
                    }
                    int finalI2 = i;
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
                                            finalRv4.setTextViewText(R.id.TvTempFirst, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconFirst, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                            break;
                                        case 1:
                                            finalRv4.setTextViewText(R.id.TvTimeSecond, res.toString());
                                            MainObject = WeatherObject.getJSONObject("main");
                                            Temp = MainObject.getString("temp").toString();
                                            finalRv4.setTextViewText(R.id.TvTempSecond, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconSecond, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                            break;
                                        case 2:
                                            finalRv4.setTextViewText(R.id.TvTimeThird, res.toString());
                                            MainObject = WeatherObject.getJSONObject("main");
                                            Temp = MainObject.getString("temp").toString();
                                            finalRv4.setTextViewText(R.id.TvTempThird, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconThird, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                            break;
                                        case 3:
                                            finalRv4.setTextViewText(R.id.TvTimeForth, res.toString());
                                            MainObject = WeatherObject.getJSONObject("main");
                                            Temp = MainObject.getString("temp").toString();
                                            finalRv4.setTextViewText(R.id.TvTempForth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconForth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                            break;
                                        case 4:
                                            finalRv4.setTextViewText(R.id.TvTimeFifth, res.toString());
                                            MainObject = WeatherObject.getJSONObject("main");
                                            Temp = MainObject.getString("temp").toString();
                                            finalRv4.setTextViewText(R.id.TvTempFifth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconFifth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                            break;
                                        case 5:
                                            finalRv4.setTextViewText(R.id.TvTimeSixth, res.toString());
                                            MainObject = WeatherObject.getJSONObject("main");
                                            Temp = MainObject.getString("temp").toString();
                                            finalRv4.setTextViewText(R.id.TvTempSixth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconSixth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                            break;
                                    }
                                }
                                AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                                appWidgetManager.updateAppWidget(widgetData.get(finalI2).getNumber(), finalRv4);
                                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                                PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);

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
                } else if (widgetData.get(i).getType() == 2) {

                    //todo weather 3 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_weather3_large);
                    try {

                        RemoteViews finalRv4 = rv;
                        RequestQueue queue = Volley.newRequestQueue(context);
                        String url, tempExt;
                        if (widgetData.get(i).getTemp() == 0) {
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
                                    long millisecond = Long.parseLong(SysObject.getString("sunrise"));
                                    String dateString = android.text.format.DateFormat.format("HH:mm:ss a", new Date(millisecond)).toString();
                                    finalRv4.setTextViewText(R.id.TvSunRise, dateString);
                                    long millisecondset = Long.parseLong(SysObject.getString("sunset"));
                                    String dateStringset = DateFormat.format("HH:mm:ss a", new Date(millisecondset)).toString();
                                    finalRv4.setTextViewText(R.id.TvSunSet, dateStringset);
                                    JSONObject WindObject = obj.getJSONObject("wind");
                                    double millisecondWind = Double.parseDouble(WindObject.getString("speed"));
                                    finalRv4.setTextViewText(R.id.TvWind, mps_to_kmph(millisecondWind) + "km/h");
                                    finalRv4.setTextViewText(R.id.TvCity, obj.get("name") + "," + SysObject.get("country"));
                                    String Temp = MainObject.get("temp").toString();
                                    finalRv4.setTextViewText(R.id.TvTemp, Temp.substring(0, Temp.lastIndexOf(".")) + tempExt);
                                    String MinTemp = MainObject.get("temp_min").toString();
                                    String MaxTemp = MainObject.get("temp_max").toString();
                                    finalRv4.setTextViewText(R.id.TvTempMaxMin, "H:" + MaxTemp.substring(0, MaxTemp.lastIndexOf(".")) + tempExt + " ~ L:" + MinTemp.substring(0, MinTemp.lastIndexOf(".")) + tempExt);
                                    finalRv4.setTextViewText(R.id.TvHumidity, MainObject.get("humidity").toString() + "%");
                                    finalRv4.setTextViewText(R.id.TvPressure, MainObject.get("pressure").toString() + "%");

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
                        if (widgetData.get(i).getTemp() == 0) {
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
                                                finalRv4.setTextViewText(R.id.TvTempFirst, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                IconObject = WeatherObject.getJSONArray("weather");
                                                finalRv4.setImageViewResource(R.id.IvWeatherIconFirst, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                break;
                                            case 1:
                                                finalRv4.setTextViewText(R.id.TvTimeSecond, res.toString());
                                                MainObject = WeatherObject.getJSONObject("main");
                                                Temp = MainObject.getString("temp").toString();
                                                finalRv4.setTextViewText(R.id.TvTempSecond, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                IconObject = WeatherObject.getJSONArray("weather");
                                                finalRv4.setImageViewResource(R.id.IvWeatherIconSecond, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                break;
                                            case 2:
                                                finalRv4.setTextViewText(R.id.TvTimeThird, res.toString());
                                                MainObject = WeatherObject.getJSONObject("main");
                                                Temp = MainObject.getString("temp").toString();
                                                finalRv4.setTextViewText(R.id.TvTempThird, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                IconObject = WeatherObject.getJSONArray("weather");
                                                finalRv4.setImageViewResource(R.id.IvWeatherIconThird, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                break;
                                            case 3:
                                                finalRv4.setTextViewText(R.id.TvTimeForth, res.toString());
                                                MainObject = WeatherObject.getJSONObject("main");
                                                Temp = MainObject.getString("temp").toString();
                                                finalRv4.setTextViewText(R.id.TvTempForth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                IconObject = WeatherObject.getJSONArray("weather");
                                                finalRv4.setImageViewResource(R.id.IvWeatherIconForth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                break;
                                            case 4:
                                                finalRv4.setTextViewText(R.id.TvTimeFifth, res.toString());
                                                MainObject = WeatherObject.getJSONObject("main");
                                                Temp = MainObject.getString("temp").toString();
                                                finalRv4.setTextViewText(R.id.TvTempFifth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                IconObject = WeatherObject.getJSONArray("weather");
                                                finalRv4.setImageViewResource(R.id.IvWeatherIconFifth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                break;
                                            case 5:
                                                finalRv4.setTextViewText(R.id.TvTimeSixth, res.toString());
                                                MainObject = WeatherObject.getJSONObject("main");
                                                Temp = MainObject.getString("temp").toString();
                                                finalRv4.setTextViewText(R.id.TvTempSixth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                IconObject = WeatherObject.getJSONArray("weather");
                                                finalRv4.setImageViewResource(R.id.IvWeatherIconSixth, MyAppConstants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                break;
                                        }
                                    }

//                                                    if (!new Pref(context).getBoolean(Pref.IS_WEATHER_1 _ALARM, false)) {
                                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                                    appWidgetManager.updateAppWidget(widgetData.get(finalI4).getNumber(), finalRv4);
                                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);

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
                }
                AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);

            } else if (widgetData.get(i).getPosition() == 20) {
                if (widgetData.get(i).getType() == 0) {
                    //todo x-panel 1 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel1_small);
                } else if (widgetData.get(i).getType() == 1) {

                    //todo x-panel 1 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel1_medium);
                } else if (widgetData.get(i).getType() == 2) {

                    //todo x-panel 1 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel1_large);
                }
                if (MyAppConstants.IsWIfiConnected(context)) {
                    rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi1_selected);
                } else {
                    rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi1);
                }

                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter != null) {
                    if (mBluetoothAdapter.isEnabled()) {
                        rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluethooth1_selected);
                    } else if (!mBluetoothAdapter.isEnabled()) {
                        rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluethooth1);
                    }
                }
                System.out.println("------------ rrrr :: " + MyAppConstants.hasSIMCard(context));
                if (MyAppConstants.isNetworkAvailable(context)) {
                    rv.setImageViewResource(R.id.IvCellular, R.drawable.ic_celluer1_selected);
                } else {
                    rv.setImageViewResource(R.id.IvCellular, R.drawable.ic_celluer1);
                }
                CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    RemoteViews finalRv = rv;
                    int finalI = i;
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
                                finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1_selected);
                            } else {
                                finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1);
                            }

                            AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                            appWidgetManager.updateAppWidget(widgetData.get(finalI).getNumber(), finalRv);
                        }

                        @Override
                        public void onTorchStrengthLevelChanged(@NonNull String cameraId, int newStrengthLevel) {
                            super.onTorchStrengthLevelChanged(cameraId, newStrengthLevel);
                        }
                    }, null);
                }

                Intent intentWifi = new Intent(Settings.ACTION_WIFI_SETTINGS);
                configPendingIntent = PendingIntent.getActivity(context, 0, intentWifi, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                rv.setOnClickPendingIntent(R.id.IvWifi, configPendingIntent);

                Intent intentBluetooth = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                configPendingIntent = PendingIntent.getActivity(context, 0, intentBluetooth, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                rv.setOnClickPendingIntent(R.id.IvBluetooth, configPendingIntent);

                Intent intentCellular = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                if (intentCellular.resolveActivity(context.getPackageManager()) != null) {
                    configPendingIntent = PendingIntent.getActivity(context, 0, intentCellular, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvCellular, configPendingIntent);
                }

                Intent intent2 = new Intent(context, XPanelFlashlightWidgetReceiver.class);
                intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetData.get(i).getNumber());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                rv.setOnClickPendingIntent(R.id.IvTorch, pendingIntent);

                AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
            } else if (widgetData.get(i).getPosition() == 18) {
                if (widgetData.get(i).getType() == 0) {

                    //todo x-panel 4 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel4_small);
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

                } else if (widgetData.get(i).getType() == 1) {

                    //todo x-panel 4 medium
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
                        RemoteViews finalRv2 = rv;
                        int finalI3 = i;
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
                                    finalRv2.setImageViewResource(R.id.IvTorch, R.drawable.ic_torch4_selected);
                                } else {
                                    finalRv2.setImageViewResource(R.id.IvTorch, R.drawable.ic_torch4);
                                }
                                AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                                appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(finalI3).getNumber(), R.id.IvTorch);
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


                    if (MyAppConstants.isNetworkAvailable(context)) {
                        rv.setImageViewResource(R.id.IvCellular, R.drawable.ic_cellular4_selected);
                    } else {
                        rv.setImageViewResource(R.id.IvCellular, R.drawable.ic_cellular4);
                    }


                    Intent intentWifi = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intentWifi, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvWifi, configPendingIntent);

                    Intent intentBluetooth = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intentBluetooth, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvBluetooth, configPendingIntent);

                    Intent intent2 = new Intent(context, XPanelFlashlight4WidgetReceiver.class);
                    intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetData.get(i).getNumber());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvTorch, pendingIntent);

                    Intent intentCellular3 = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                    if (intentCellular3.resolveActivity(context.getPackageManager()) != null) {
                        configPendingIntent = PendingIntent.getActivity(context, 0, intentCellular3, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.IvCellular, configPendingIntent);
                    }
                } else if (widgetData.get(i).getType() == 2) {

                    //todo x-panel 4 large
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

                }

                AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
            } else if (widgetData.get(i).getPosition() == 21) {
                if (widgetData.get(i).getType() == 0) {

                    //todo x-panel 2 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel2_small);
                } else if (widgetData.get(i).getType() == 1) {

                    //todo x-panel 2 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel2_medium);
                } else if (widgetData.get(i).getType() == 2) {

                    //todo x-panel 2 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel2_large);
                }

                BatteryManager bm = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
                int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                new MyAppPref(context).putInt(MyAppPref.IS_BATTERY, batLevel);

                rv.setTextViewText(R.id.TvProgressText, batLevel + "%");
                rv.setProgressBar(R.id.ProgressBarWidget2, 100, batLevel, false);
                if (new MyAppPref(context).getInt(MyAppPref.IS_BATTERY, -1) != batLevel) {
                    BatteryManager systemService = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
                    int batlevel = systemService.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                    rv.setTextViewText(R.id.TvProgressText, batLevel + "%");
                    rv.setProgressBar(R.id.ProgressBarWidget2, 100, batLevel, false);
                    new MyAppPref(context).putInt(MyAppPref.IS_BATTERY, batlevel);

                }

                Intent intentBattery = new Intent(Settings.EXTRA_BATTERY_SAVER_MODE_ENABLED);
                configPendingIntent = PendingIntent.getActivity(context, 0, intentBattery, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                rv.setOnClickPendingIntent(R.id.RlBattery, configPendingIntent);
                AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
            } else if (widgetData.get(i).getPosition() == 22) {
                if (widgetData.get(i).getType() == 0) {

                    //todo x-panel 3 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel3_small);
                } else if (widgetData.get(i).getType() == 1) {

                    //todo x-panel 3 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel3_medium);
                } else if (widgetData.get(i).getType() == 2) {

                    //todo x-panel 3 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel3_large);
                }

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
                    RemoteViews finalRv = rv;
                    int finalI = i;
                    manager.registerTorchCallback(new CameraManager.TorchCallback() {
                        @Override
                        public void onTorchModeUnavailable(@NonNull String cameraId) {
                            super.onTorchModeUnavailable(cameraId);
                        }

                        @Override
                        public void onTorchModeChanged(@NonNull String cameraId, boolean enabled) {
                            super.onTorchModeChanged(cameraId, enabled);
                            IsTorchOn = enabled;
                            if (widgetData.get(finalI).getType() == 2) {
                                if (IsTorchOn) {
                                    finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1_selected);
                                } else {
                                    finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1);
                                }
                            } else {
                                if (IsTorchOn) {
                                    finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_xpanel_medium_2_flashlight_selected);
                                } else {
                                    finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_xpanel_medium_2_flashlight);
                                }
                            }

                            AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                            appWidgetManager.updateAppWidget(widgetData.get(finalI).getNumber(), finalRv);
                        }

                        @Override
                        public void onTorchStrengthLevelChanged(@NonNull String cameraId, int newStrengthLevel) {
                            super.onTorchStrengthLevelChanged(cameraId, newStrengthLevel);
                        }
                    }, null);
                }
                if (widgetData.get(i).getType() == 2) {
                    if (MyAppConstants.IsWIfiConnected(context)) {
                        rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi1_selected);
                    } else {
                        rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi1);
                    }
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (bluetoothAdapter != null) {
                        if (bluetoothAdapter.isEnabled()) {
                            rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluethooth1_selected);
                        } else if (!bluetoothAdapter.isEnabled()) {
                            rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluethooth1);
                        }
                    }

                    if (MyAppConstants.isNetworkAvailable(context)) {
                        rv.setImageViewResource(R.id.IvCellular, R.drawable.ic_celluer1_selected);
                    } else {
                        rv.setImageViewResource(R.id.IvCellular, R.drawable.ic_celluer1);
                    }
                } else {
                    if (MyAppConstants.IsWIfiConnected(context)) {
                        rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_xpanel_medium_2_wifi_selected);
                    } else {
                        rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_xpanel_medium_2_wifi);
                    }
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (bluetoothAdapter != null) {
                        if (bluetoothAdapter.isEnabled()) {
                            rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_xpanel_medium_2_bluetooth_selected);
                        } else if (!bluetoothAdapter.isEnabled()) {
                            rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_xpanel_medium_2_bluetooth);
                        }
                    }


                    if (MyAppConstants.isNetworkAvailable(context)) {
                        rv.setImageViewResource(R.id.IvCellular, R.drawable.ic_xpanel_medium_2_mobiledata_selected);
                    } else {
                        rv.setImageViewResource(R.id.IvCellular, R.drawable.ic_xpanel_medium_2_mobiledata);
                    }
                }
                final float totalSpace = MyAppConstants.DeviceMemory.getInternalStorageSpace();
                final float occupiedSpace = MyAppConstants.DeviceMemory.getInternalUsedSpace();
                final float freeSpace = MyAppConstants.DeviceMemory.getInternalFreeSpace();

                rv.setProgressBar(R.id.progressBarCharge, 100, property, false);
                rv.setProgressBar(R.id.progressBarStorage, (int) totalSpace, (int) occupiedSpace, false);

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

                AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
            }
        }
    }

    public int mps_to_kmph(double mps) {
        return (int) (3.6 * mps);
    }
}

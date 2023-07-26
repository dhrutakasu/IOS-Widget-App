package com.ios.widget.provider;

import static com.ios.widget.utils.Constants.Widget_Id;
import static com.ios.widget.utils.Constants.Widget_Type_Id;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ios.widget.Model.WidgetData;
import com.ios.widget.R;
import com.ios.widget.helper.DatabaseHelper;
import com.ios.widget.utils.Constants;
import com.ios.widget.utils.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.DateFormat;
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
        System.out.println("********** : come come");
        DatabaseHelper helper = new DatabaseHelper(context);
        ArrayList<WidgetData> widgetData = helper.getWidgets();
        RemoteViews rv = null;
        PendingIntent configPendingIntent = null;

        long startMillis = 0;
        Uri.Builder builder = null;
        Intent intent = null;
        Intent intent1 = null;
        Calendar calendar;
        int currentDay;
        int currentMonth;
        int currentYear;
        for (int i = 0; i < widgetData.size(); i++) {
            System.out.println("********** : come come : " + widgetData.get(i).getPosition());
            if (widgetData.get(i).getPosition() == 0) {
                if (widgetData.get(i).getType() == 1) {
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
                    if (!new Pref(context).getString(Pref.IS_TIME_3, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView);
                        new Pref(context).putString(Pref.IS_TIME_3, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                } else if (widgetData.get(i).getType() == 2) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel1_large);

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
                                System.out.println("********* IsTorchOn ON / OFF : " + IsTorchOn);
                                System.out.println("********* ON / OFF : " + IsTorchOn);
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

                    Intent intentCellular = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intentCellular, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvCellular, configPendingIntent);

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

                            rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_weather1_small);
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

                                        RequestQueue queue = Volley.newRequestQueue(context);
                                        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&APPID=abb0e61bdf12b12ca71bcd2ee74d5d9f";
                                        RemoteViews finalRv1 = rv;
                                        StringRequest stringReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                System.out.println("------- catch Out response: " + response.toString());
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
                                                    finalRv1.setTextViewText(R.id.TvTemp, MainObject.get("temp") + "°C");
                                                    finalRv1.setTextViewText(R.id.TvTempMaxMin, MainObject.get("temp_min").toString().indexOf(2) + "°C" + MainObject.get("temp_max  ").toString().indexOf(2) + "°C");

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
                            AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                            appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                            PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                        }
                    }
                } else if (widgetData.get(i).getType() == 1) {
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
                    if (!new Pref(context).getString(Pref.IS_DATE_4, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView);
                        new Pref(context).putString(Pref.IS_DATE_4, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                }
            } else if (widgetData.get(i).getPosition() == 2) {
                if (widgetData.get(i).getType() == 1) {
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
                    } else {
                        if (Constants.IsWIfiConnected(context)) {
                            System.out.println("************ WIFI RECEIVE  ON ");
                            rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_xpanel_medium_2_wifi_selected);
                        } else {
                            System.out.println("************ WIFI RECEIVE  Off ");
                            rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_xpanel_medium_2_wifi);
                        }
                        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (bluetoothAdapter != null) {
                            if (bluetoothAdapter.isEnabled()) {
                                System.out.println("************  Bluetooth RECEIVE  ON ");
                                rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_xpanel_medium_2_bluetooth_selected);
                            } else if (!bluetoothAdapter.isEnabled()) {
                                System.out.println("************  Bluetooth RECEIVE  else ");
                                rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_xpanel_medium_2_bluetooth);
                            }
                        }
                    }
                    final float totalSpace = Constants.DeviceMemory.getInternalStorageSpace();
                    final float occupiedSpace = Constants.DeviceMemory.getInternalUsedSpace();
                    final float freeSpace = Constants.DeviceMemory.getInternalFreeSpace();

                    rv.setProgressBar(R.id.progressBarCharge, 100, property, false);
                    rv.setProgressBar(R.id.progressBarStorage, (int) totalSpace, (int) occupiedSpace, false);

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

                    System.out.println("************ WIFI RECEIVE COME BROAD W_NUMBER " + widgetData.get(i).getNumber());
                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                }
            } else if (widgetData.get(i).getPosition() == 4) {
                if (widgetData.get(i).getType() == 1) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar1_medium);
//                    rv.setImageViewResource(R.id.iv_background, R.drawable.img_calendar1_medium_bg);
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
                    if (!new Pref(context).getString(Pref.IS_DATE_1, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView);
                        new Pref(context).putString(Pref.IS_DATE_1, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                } else if (widgetData.get(i).getType() == 2) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar1_large);
                    rv.setImageViewBitmap(R.id.iv_background, Constants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.img_calendar1_small_bg), 30));
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
                    if (!new Pref(context).getString(Pref.IS_DATE_LARGE_1, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarLargeView);
                        new Pref(context).putString(Pref.IS_DATE_LARGE_1, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                }
            } else if (widgetData.get(i).getPosition() == 5) {
                if (widgetData.get(i).getType() == 1) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar3_medium);
                    rv.setImageViewBitmap(R.id.iv_background, Constants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.img_calendar2_medium_bg), 30));
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
                    if (!new Pref(context).getString(Pref.IS_DATE_3, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView);
                        new Pref(context).putString(Pref.IS_DATE_3, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                }
            } else if (widgetData.get(i).getPosition() == 6) {
                if (widgetData.get(i).getType() == 1) {
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
                    if (!new Pref(context).getString(Pref.IS_DATE_2, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView);
                        new Pref(context).putString(Pref.IS_DATE_2, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                } else if (widgetData.get(i).getType() == 2) {
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
                    if (!new Pref(context).getString(Pref.IS_DATE_LARGE_3, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarLargeView);
                        new Pref(context).putString(Pref.IS_DATE_LARGE_3, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                }
            } else if (widgetData.get(i).getPosition() == 7) {
                if (widgetData.get(i).getType() == 1) {
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
                    if (!new Pref(context).getString(Pref.IS_DATE_4, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView);
                        new Pref(context).putString(Pref.IS_DATE_4, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                } else if (widgetData.get(i).getType() == 2) {
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
                    if (!new Pref(context).getString(Pref.IS_DATE_LARGE_4, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarLargeView);
                        new Pref(context).putString(Pref.IS_DATE_LARGE_4, currentDay + "/" + currentMonth + "/" + currentYear);
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
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_weather1_small);
                    RequestQueue queue = Volley.newRequestQueue(context);
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&APPID=b7fc383a06f2e5b385f2f811e18192f6";
                    RemoteViews finalRv1 = rv;
                    int finalI1 = i;
                    StringRequest stringReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("------- Receiver catch Out response: " + response.toString());
                            try {
                                JSONObject obj = new JSONObject(response);

                                JSONArray WeatherArray = obj.getJSONArray("weather");
                                for (int j = 0; j < WeatherArray.length(); j++) {
                                    JSONObject WeatherObject = WeatherArray.getJSONObject(j);
                                    System.out.println("------- Receiver catch Out WeatherObject: " + WeatherObject.getString("description"));

                                    WeatherObject.get("main");
                                    WeatherObject.get("icon");
                                    finalRv1.setTextViewText(R.id.TvDesc, WeatherObject.get("description").toString());
                                    finalRv1.setImageViewResource(R.id.IvWeatherIcon, Constants.getWeatherIcons(WeatherObject.getString("icon")));
                                }
                                JSONObject MainObject = obj.getJSONObject("main");

                                JSONObject SysObject = obj.getJSONObject("sys");
                                finalRv1.setTextViewText(R.id.TvCity, obj.get("name") + "," + SysObject.get("country"));
                                String Temp = MainObject.get("temp").toString();
                                System.out.println("------- Receiver catch Out response Temp: " + Temp.substring(0, Temp.lastIndexOf(".")));
                                finalRv1.setTextViewText(R.id.TvTemp, Temp.substring(0, Temp.lastIndexOf(".")) + "°C");
                                String MinTemp = MainObject.get("temp_min").toString();
                                String MaxTemp = MainObject.get("temp_max").toString();
                                System.out.println("------- Receiver catch Out response TempMin: " + MinTemp.substring(0, MinTemp.lastIndexOf(".")) + " -- " + MaxTemp.substring(0, MaxTemp.lastIndexOf(".")));
                                finalRv1.setTextViewText(R.id.TvTempMaxMin, "H:" + MaxTemp.substring(0, MaxTemp.lastIndexOf(".")) + "°C L:" + MinTemp.substring(0, MinTemp.lastIndexOf(".")) + "°C");

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
                            System.out.println("-------Receiver catch Out errrr: " + error.getMessage());
                        }
                    });
                    queue.add(stringReq);
                } else if (widgetData.get(i).getType() == 1) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_weather1_medium);

                    RemoteViews finalRv4 = rv;
                    RequestQueue queue = Volley.newRequestQueue(context);
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + widgetData.get(i).getCity() + "&units=metric&APPID=b7fc383a06f2e5b385f2f811e18192f6";
                    StringRequest stringReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("-------rere catch Out response: " + response.toString());
                            try {
                                JSONObject obj = new JSONObject(response);

                                JSONArray WeatherArray = obj.getJSONArray("weather");
                                for (int j = 0; j < WeatherArray.length(); j++) {
                                    JSONObject WeatherObject = WeatherArray.getJSONObject(j);

                                    WeatherObject.get("main");
                                    WeatherObject.get("icon");
                                    System.out.println("------- rer catch Out WeatherObject: " + WeatherObject.getString("description"));
                                    finalRv4.setTextViewText(R.id.TvDesc, WeatherObject.get("description").toString());
                                    finalRv4.setImageViewResource(R.id.IvWeatherIcon, Constants.getWeatherIcons(WeatherObject.getString("icon")));
                                }
                                JSONObject MainObject = obj.getJSONObject("main");

                                JSONObject SysObject = obj.getJSONObject("sys");
                                finalRv4.setTextViewText(R.id.TvCity, obj.get("name") + "," + SysObject.get("country"));
                                String Temp = MainObject.get("temp").toString();
                                System.out.println("-------rere catch Out response Temp: " + Temp.substring(0, Temp.lastIndexOf(".")));
                                finalRv4.setTextViewText(R.id.TvTemp, Temp.substring(0, Temp.lastIndexOf(".")) + "°C");
                                String MinTemp = MainObject.get("temp_min").toString();
                                String MaxTemp = MainObject.get("temp_max").toString();
                                System.out.println("-------rere catch Out response TempMin: " + MinTemp.substring(0, MinTemp.lastIndexOf(".")) + " -- " + MaxTemp.substring(0, MaxTemp.lastIndexOf(".")));
                                finalRv4.setTextViewText(R.id.TvTempMaxMin, "H:" + MaxTemp.substring(0, MaxTemp.lastIndexOf(".")) + "°C L:" + MinTemp.substring(0, MinTemp.lastIndexOf(".")) + "°C");

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

                    RequestQueue queue1 = Volley.newRequestQueue(context);
                    String url1 = "https://api.openweathermap.org/data/2.5/forecast?q=" + widgetData.get(i).getCity() + "&cnt=6&units=metric&APPID=b7fc383a06f2e5b385f2f811e18192f6";
                    int finalI2 = i;
                    StringRequest stringReq1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("-------rerer catch Out response: " + response.toString());
                            try {
                                JSONObject MainObject = null;
                                JSONArray IconObject = null;
                                String Temp = null;
                                String res=null ;

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
                                        System.out.println(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    System.out.println("------- rerer catch Out WeatherObject111: " + res);
                                    switch (j) {
                                        case 0:
                                            finalRv4.setTextViewText(R.id.TvTimeFirst, res.toString());
                                            MainObject = WeatherObject.getJSONObject("main");
                                            Temp = MainObject.getString("temp").toString();
                                            finalRv4.setTextViewText(R.id.TvTempFirst, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconFirst, Constants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                            break;
                                        case 1:
                                            finalRv4.setTextViewText(R.id.TvTimeSecond, res.toString());
                                            MainObject = WeatherObject.getJSONObject("main");
                                            Temp = MainObject.getString("temp").toString();
                                            finalRv4.setTextViewText(R.id.TvTempSecond, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconSecond, Constants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                            break;
                                        case 2:
                                            finalRv4.setTextViewText(R.id.TvTimeThird, res.toString());
                                            MainObject = WeatherObject.getJSONObject("main");
                                            Temp = MainObject.getString("temp").toString();
                                            finalRv4.setTextViewText(R.id.TvTempThird, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconThird, Constants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                            break;
                                        case 3:
                                            finalRv4.setTextViewText(R.id.TvTimeForth, res.toString());
                                            MainObject = WeatherObject.getJSONObject("main");
                                            Temp = MainObject.getString("temp").toString();
                                            finalRv4.setTextViewText(R.id.TvTempForth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconForth, Constants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                            break;
                                        case 4:
                                            finalRv4.setTextViewText(R.id.TvTimeFifth, res.toString());
                                            MainObject = WeatherObject.getJSONObject("main");
                                            Temp = MainObject.getString("temp").toString();
                                            finalRv4.setTextViewText(R.id.TvTempFifth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconFifth, Constants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                            break;
                                        case 5:
                                            finalRv4.setTextViewText(R.id.TvTimeSixth, res.toString());
                                            MainObject = WeatherObject.getJSONObject("main");
                                            Temp = MainObject.getString("temp").toString();
                                            finalRv4.setTextViewText(R.id.TvTempSixth, Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                            IconObject = WeatherObject.getJSONArray("weather");
                                            finalRv4.setImageViewResource(R.id.IvWeatherIconSixth, Constants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
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
                            System.out.println("------- catch Out errrr: " + error.getMessage());
                        }
                    });
                    queue1.add(stringReq1);
                } else if (widgetData.get(i).getType() == 2) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel1_large);
                }

            } else if (widgetData.get(i).getPosition() == 20) {
                if (widgetData.get(i).getType() == 0) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel1_small);
                } else if (widgetData.get(i).getType() == 1) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel1_medium);
                } else if (widgetData.get(i).getType() == 2) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel1_large);
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
                            System.out.println("********* IsTorchOn ON / OFF : " + IsTorchOn);
                            System.out.println("********* ON / OFF : " + IsTorchOn);
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

                Intent intentCellular = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                configPendingIntent = PendingIntent.getActivity(context, 0, intentCellular, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                rv.setOnClickPendingIntent(R.id.IvCellular, configPendingIntent);

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
                    System.out.println("-----------reee store TTT : " + Constants.bytes2String(total) + "/" + Constants.bytes2String(free) + "/" + Constants.bytes2String(used));
                    rv.setTextViewText(R.id.progress_text, managerIntProperty + "%");
                    rv.setTextViewText(R.id.storage_text, Constants.bytes2String(used) + "/" + Constants.bytes2String(total));

                } else if (widgetData.get(i).getType() == 1) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel1_medium);
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
                    System.out.println("-----------store TTT : " + Constants.bytes2String(total) + "/" + Constants.bytes2String(free) + "/" + Constants.bytes2String(used));
                    rv.setTextViewText(R.id.progress_text, managerIntProperty + "%");
                    rv.setTextViewText(R.id.storage_text, Constants.bytes2String(used) + "/" + Constants.bytes2String(total));

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
                                System.out.println("------********* ON / OFF : " + IsTorchOn);
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
                    if (Constants.IsWIfiConnected(context)) {
                        System.out.println("************ WIFI RECEIVE  ON ");
                        rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi4_selected);
                    } else {
                        System.out.println("************ WIFI RECEIVE  Off ");
                        rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi4);
                    }
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter != null) {
                        if (mBluetoothAdapter.isEnabled()) {
                            System.out.println("************  Bluetooth RECEIVE  ON ");
                            rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluetooth4_selected);
                        } else if (!mBluetoothAdapter.isEnabled()) {
                            System.out.println("************  Bluetooth RECEIVE  else ");
                            rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluetooth4);
                        }
                    }


                    Intent intentWifi = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intentWifi, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvWifi, configPendingIntent);

                    Intent intentBluetooth = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intentBluetooth, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvBluetooth, configPendingIntent);
                    System.out.println("*********** WIDGET _IDD Number : " + widgetData.get(i).getNumber());

                    Intent intent2 = new Intent(context, XPanelFlashlight4WidgetReceiver.class);
                    intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetData.get(i).getNumber());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvTorch, pendingIntent);
                } else if (widgetData.get(i).getType() == 2) {
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
                    System.out.println("-----------ree store TTT : " + Constants.bytes2String(total) + "/" + Constants.bytes2String(free) + "/" + Constants.bytes2String(used));
                    rv.setTextViewText(R.id.progress_text, managerIntProperty + "%");
                    rv.setTextViewText(R.id.storage_text, Constants.bytes2String(used) + "/" + Constants.bytes2String(total));

                }

                AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
            } else if (widgetData.get(i).getPosition() == 21) {
                if (widgetData.get(i).getType() == 0) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel2_small);
                } else if (widgetData.get(i).getType() == 1) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel2_medium);
                } else if (widgetData.get(i).getType() == 2) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel2_large);
                }

                BatteryManager bm = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
                int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                System.out.println("************ WIFI RECEIVE COME BROAD 100/" + batLevel + " -- : 100/" + new Pref(context).getInt(Pref.IS_BATTERY, -1));
                new Pref(context).putInt(Pref.IS_BATTERY, batLevel);

                rv.setTextViewText(R.id.progress_text, batLevel + "%");
                rv.setProgressBar(R.id.progress_bar, 100, batLevel, false);
                if (new Pref(context).getInt(Pref.IS_BATTERY, -1) != batLevel) {
                    System.out.println("************ WIFI RECEIVE BROAD 100/" + batLevel + " -- : 100/" + new Pref(context).getInt(Pref.IS_BATTERY, -1));
                    BatteryManager systemService = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
                    int batlevel = systemService.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                    rv.setTextViewText(R.id.progress_text, batLevel + "%");
                    rv.setProgressBar(R.id.progress_bar, 100, batLevel, false);
                    new Pref(context).putInt(Pref.IS_BATTERY, batlevel);

                }


                Intent intentBattery = new Intent(Settings.EXTRA_BATTERY_SAVER_MODE_ENABLED);
                configPendingIntent = PendingIntent.getActivity(context, 0, intentBattery, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                rv.setOnClickPendingIntent(R.id.RlBattery, configPendingIntent);
                System.out.println("************ WIFI RECEIVE COME BROAD W_NUMBER " + widgetData.get(i).getNumber());
                AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
            } else if (widgetData.get(i).getPosition() == 22) {
                if (widgetData.get(i).getType() == 0) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel3_small);
                } else if (widgetData.get(i).getType() == 1) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel3_medium);
                } else if (widgetData.get(i).getType() == 2) {
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
                } else {
                    if (Constants.IsWIfiConnected(context)) {
                        System.out.println("************ WIFI RECEIVE  ON ");
                        rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_xpanel_medium_2_wifi_selected);
                    } else {
                        System.out.println("************ WIFI RECEIVE  Off ");
                        rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_xpanel_medium_2_wifi);
                    }
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (bluetoothAdapter != null) {
                        if (bluetoothAdapter.isEnabled()) {
                            System.out.println("************  Bluetooth RECEIVE  ON ");
                            rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_xpanel_medium_2_bluetooth_selected);
                        } else if (!bluetoothAdapter.isEnabled()) {
                            System.out.println("************  Bluetooth RECEIVE  else ");
                            rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_xpanel_medium_2_bluetooth);
                        }
                    }
                }
                final float totalSpace = Constants.DeviceMemory.getInternalStorageSpace();
                final float occupiedSpace = Constants.DeviceMemory.getInternalUsedSpace();
                final float freeSpace = Constants.DeviceMemory.getInternalFreeSpace();

                rv.setProgressBar(R.id.progressBarCharge, 100, property, false);
                rv.setProgressBar(R.id.progressBarStorage, (int) totalSpace, (int) occupiedSpace, false);

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

                System.out.println("************ WIFI RECEIVE COME BROAD W_NUMBER " + widgetData.get(i).getNumber());
                AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
            }
        }
    }
}

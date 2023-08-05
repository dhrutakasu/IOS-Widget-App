package com.ios.widget.provider;

import static com.ios.widget.utils.MyAppConstants.CreateWidget;
import static com.ios.widget.utils.MyAppConstants.Widget_Id;

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
import android.os.StatFs;
import android.provider.CalendarContract;
import android.provider.Settings;
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
import com.ios.widget.utils.MyAppConstants;
import com.ios.widget.utils.MyAppPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SmallWidgetProvider extends AppWidgetProvider {
    private boolean IsTorchOn;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        DatabaseHelper helper = new DatabaseHelper(context);
        System.out.println("******** SmallWidgetProvider::"+ MyAppConstants.Temp_Id);
        int insert=-1;
        WidgetData widgetData=null;
        if (CreateWidget) {
            widgetData = new WidgetData(0, MyAppConstants.Widget_Type_Id, -1, "", MyAppConstants.Temp_Id);
            insert = helper.InsertWidget(widgetData);
            for (int id : appWidgetIds) {
                Widget_Id = id;
            }
            if (helper.getWidgetCount() != 0) {
                WidgetData widgetsId = helper.getWidgetsId(insert);
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
            switch (helper.getWidgets().get(i).getPosition()) {
                case 0:
                case 7:
                    //todo calender 4 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar4_small);
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMMM yyyy");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMMM yyyy");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                    rv.setCharSequence(R.id.TClockDay, "setFormat12Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockDay, "setFormat24Hour", "EEEE");
                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW).setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlSmallCal, configPendingIntent);
                    break;
                case 1:
                case 8:
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

                                        WidgetData widgetsId = helper.getWidgetsId(insert);
                                        widgetsId.setCity(city);
                                        helper.updateWidget(widgetsId);

                                        RequestQueue queue = Volley.newRequestQueue(context);
                                        String url,tempExt;
                                        if (widgetData.getTemp()==0) {
                                            url = MyAppConstants.BASE_URL_WEATHER + city + "&units=metric&APPID=" + context.getString(R.string.str_weather_key);
                                            tempExt="°C";
                                        }else {
                                            url = MyAppConstants.BASE_URL_WEATHER + city + "&units=imperial&APPID=" + context.getString(R.string.str_weather_key);
                                            tempExt="°F";
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
                                                    finalRv1.setTextViewText(R.id.TvTempMaxMin, "H:" + MaxTemp.substring(0, MaxTemp.lastIndexOf(".")) + tempExt+" / L:" + MinTemp.substring(0, MinTemp.lastIndexOf(".")) + tempExt);

                                                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                                                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                                    new MyAppPref(context).putBoolean(MyAppPref.IS_WEATHER_1_ALARM, true);
                                                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, broadcast);
                                                    appWidgetManager.updateAppWidget(Widget_Id, finalRv1);
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
                                    } catch (Exception e) {
                                    }
                                } else {

                                }
                            }

                        }
                    }
                    break;
                case 2:
                case 19:
                    //todo clock 9 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_text3_small);

                    rv.setCharSequence(R.id.TClockHour, "setFormat12Hour", "HH");
                    rv.setCharSequence(R.id.TClockHour, "setFormat24Hour", "HH");
                    rv.setCharSequence(R.id.TClockMinutes, "setFormat12Hour", "mm");
                    rv.setCharSequence(R.id.TClockMinutes, "setFormat24Hour", "mm");
                    rv.setCharSequence(R.id.TClockDayMonthDate, "setFormat12Hour", "d EEEE");
                    rv.setCharSequence(R.id.TClockDayMonthDate, "setFormat24Hour", "d EEEE");

                    intent1 = new Intent(Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlSmallClock, configPendingIntent);
                    break;
                case 5:
                    //todo calender 2 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar3_small);
//                    rv.setImageViewBitmap(R.id.IvBackground2, MyAppConstants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_widget_calendar2_small_bg), 30));
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour",                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              "EEE");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "EEE");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW).setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlSmallCal, configPendingIntent);

                    break;
                case 6:
                    //todo calender 3 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar2_small);
                    rv.setCharSequence(R.id.TClockDay, "setFormat12Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockDay, "setFormat24Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMMM yyyy");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMMM yyyy");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");

                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW).setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlSmallCal, configPendingIntent);
                    break;
                case 9:
                    //todo weather 2 small
                    LocationManager manager1 = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    if (manager1.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        if (ActivityCompat.checkSelfPermission(
                                context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        } else {
                            String city = "";

                            rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_weather2_small);
                            List<String> providers = manager1.getProviders(true);
                            for (String provider : providers) {
                                Location locationGPS = manager1.getLastKnownLocation(provider);
                                if (locationGPS != null) {
                                    try {
                                        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                                        List<Address> addresses = geocoder.getFromLocation(locationGPS.getLatitude(), locationGPS.getLongitude(), 1);
                                        city = addresses.get(0).getLocality();

                                        WidgetData widgetsId = helper.getWidgetsId(insert);
                                        widgetsId.setCity(city);
                                        helper.updateWidget(widgetsId);

                                        RequestQueue queue = Volley.newRequestQueue(context);
                                        String url,tempExt;
                                        if (widgetData.getTemp()==0) {
                                            url = MyAppConstants.BASE_URL_WEATHER + city + "&units=metric&APPID=" + context.getString(R.string.str_weather_key);
                                            tempExt="°C";
                                        }else {
                                            url = MyAppConstants.BASE_URL_WEATHER + city + "&units=imperial&APPID=" + context.getString(R.string.str_weather_key);
                                            tempExt="°F";
                                        }
                                        RemoteViews finalRv1 = rv;
                                        StringRequest stringReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject obj = new JSONObject(response);
                                                    System.out.println("******** weather res:: "+obj.toString());
                                                    JSONArray WeatherArray = obj.getJSONArray("weather");
                                                    for (int j = 0; j < WeatherArray.length(); j++) {
                                                        JSONObject WeatherObject = WeatherArray.getJSONObject(j);

                                                        finalRv1.setTextViewText(R.id.TvDesc, WeatherObject.get("main").toString());
                                                        finalRv1.setImageViewResource(R.id.IvWeatherIcon, MyAppConstants.getWeatherIcons(WeatherObject.getString("icon")));
                                                    }
                                                    JSONObject MainObject = obj.getJSONObject("main");

                                                    JSONObject SysObject = obj.getJSONObject("sys");
                                                    finalRv1.setTextViewText(R.id.TvCity, obj.get("name") + "," + SysObject.get("country"));
                                                    String Temp = MainObject.get("temp").toString();
                                                    finalRv1.setTextViewText(R.id.TvTemp, Temp.substring(0, Temp.lastIndexOf(".")) + tempExt);

                                                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                                                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                                    new MyAppPref(context).putBoolean(MyAppPref.IS_WEATHER_1_ALARM, true);
                                                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, broadcast);
                                                    appWidgetManager.updateAppWidget(Widget_Id, finalRv1);
                                                } catch (JSONException e) {
                                                    System.out.println("******** weather:ex: "+e.getMessage());
                                                    throw new RuntimeException(e);
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                System.out.println("******** Wather :err : "+error.getLocalizedMessage());
                                                //displaying the error in toast if occur
                                             }
                                        });
                                        queue.add(stringReq);
                                    } catch (Exception e) {
                                        System.out.println("******** weather :eee : "+e.getMessage());
                                    }
                                } else {

                                }
                            }

                        }
                    }
                    break;
                case 10:
                    //todo weather 3 small
                    LocationManager service = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    if (service.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        if (ActivityCompat.checkSelfPermission(
                                context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        } else {
                            String city = "";

                            rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_weather3_small);
                            List<String> providers = service.getProviders(true);
                            for (String provider : providers) {
                                Location locationGPS = service.getLastKnownLocation(provider);
                                if (locationGPS != null) {
                                    try {
                                        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                                        List<Address> addresses = geocoder.getFromLocation(locationGPS.getLatitude(), locationGPS.getLongitude(), 1);
                                        city = addresses.get(0).getLocality();

                                        WidgetData widgetsId = helper.getWidgetsId(insert);
                                        widgetsId.setCity(city);
                                        helper.updateWidget(widgetsId);

                                        RequestQueue queue = Volley.newRequestQueue(context);
                                        String url,tempExt;
                                        if (widgetData.getTemp()==0) {
                                            url = MyAppConstants.BASE_URL_WEATHER + city + "&units=metric&APPID=" + context.getString(R.string.str_weather_key);
                                            tempExt="°C";
                                        }else {
                                            url = MyAppConstants.BASE_URL_WEATHER + city + "&units=imperial&APPID=" + context.getString(R.string.str_weather_key);
                                            tempExt="°F";
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
                                                    finalRv1.setTextViewText(R.id.TvTempMaxMin, "H:" + MaxTemp.substring(0, MaxTemp.lastIndexOf(".")) + tempExt+" ~ L:" + MinTemp.substring(0, MinTemp.lastIndexOf(".")) + tempExt);

                                                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                                                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                                    new MyAppPref(context).putBoolean(MyAppPref.IS_WEATHER_1_ALARM, true);
                                                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, broadcast);
                                                    appWidgetManager.updateAppWidget(Widget_Id, finalRv1);
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
                                    } catch (Exception e) {
                                    }
                                } else {

                                }
                            }

                        }
                    }
                    break;
                case 11:
                    //todo clock 1 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple1_small);
                    intent1 = new Intent(Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.AnalogClock, configPendingIntent);
                    break;
                case 12:
                    //todo clock 2 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple2_small);

                    intent1 = new Intent(Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.AnalogClock, configPendingIntent);
                    break;
                case 13:
                    //todo clock 3 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple3_small);

                    intent1 = new Intent(Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.AnalogClock, configPendingIntent);
                    break;
                case 14:
                    //todo clock 4 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_text1_small);

                    intent1 = new Intent(Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.LlSmallClock, configPendingIntent);
                    break;
                case 20:
                    //todo x-panel 1 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel1_small);

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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                        System.out.println("------------ ppppp :: "+ MyAppConstants.hasSIMCard(context));
                    }
                    if (MyAppConstants.isNetworkAvailable(context)) {
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
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvTorch, pendingIntent);
                case 21:
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
                    rv.setTextViewText(R.id.TvProgressText, managerIntProperty + "%");
                    rv.setTextViewText(R.id.storage_text, MyAppConstants.bytes2String(used) + "/" + MyAppConstants.bytes2String(total));

                    intent = new Intent(Settings.ACTION_INTERNAL_STORAGE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.LlStorage, configPendingIntent);

                    intent = new Intent(Settings.EXTRA_BATTERY_SAVER_MODE_ENABLED);
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
                    break;
                case 22:
                    //todo x-panel 3 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel3_small);

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

                    intent = new Intent(Settings.EXTRA_BATTERY_SAVER_MODE_ENABLED);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.progressBarCharge, configPendingIntent);

                    intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.LlTime, configPendingIntent);

                    break;
            }
            appWidgetManager.updateAppWidget(Widget_Id, rv);
            MyAppConstants.CreateWidget=false;

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int id : appWidgetIds) {
            System.out.println("_____ delete : "+id);
            DatabaseHelper helper = new DatabaseHelper(context);

            helper.getDeleteWidgets(id);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null && intent.getAction() != null && intent.getAction().equals("APPWIDGET_DELETED")) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                // Show a confirmation dialog to the user for widget removal
                onDeleted(context, new int[] { appWidgetId });
            }
        }
        super.onReceive(context, intent);
    }
}


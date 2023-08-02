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
import android.text.format.DateFormat;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class LargeWidgetProvider extends AppWidgetProvider {
    private boolean IsTorchOn;


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        DatabaseHelper helper = new DatabaseHelper(context);
        WidgetData widgetData = new WidgetData(2, Constants.Widget_Type_Id, -1,"",Constants.Temp_Id);
        int insert = helper.InsertWidget(widgetData);
        for (int id : appWidgetIds) {
            Widget_Id = id;
        }
        if (helper.getWidgetCount() != 0) {
            WidgetData widgetsId = helper.getWidgetsId(insert);
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
            switch (helper.getWidgets().get(i).getPosition()) {
                case 0:
                case 20:
                    //todo x-panel 1 large
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel1_large);

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

                    if (Constants.isNetworkAvailable(context)) {
                        rv.setImageViewResource(R.id.IvCellular, R.drawable.ic_celluer1_selected);
                    } else {
                        rv.setImageViewResource(R.id.IvCellular, R.drawable.ic_celluer1);
                    }
                    if (IsTorchOn) {
                        rv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1_selected);
                    } else {
                        rv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1);
                    }

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

                    Intent intentCellular = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                    if (intentCellular.resolveActivity(context.getPackageManager()) != null) {
                        configPendingIntent = PendingIntent.getActivity(context, 0, intentCellular, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.IvCellular, configPendingIntent);
                    }
                    break;
                case 2:
                case 9:
                    //todo weather 2 large

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
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        if (ActivityCompat.checkSelfPermission(
                                context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        } else {
                            String city = "";

                            rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_weather1_large);
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
                                        widgetsId.setCity(city);
                                        helper.updateWidget(widgetsId);

                                        RemoteViews finalRv4 = rv;
                                        RequestQueue queue = Volley.newRequestQueue(context);
                                        String url,tempExt;
                                        if (widgetData.getTemp()==0) {
                                            url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&APPID=" + context.getString(R.string.weather_key);
                                            tempExt="°C";
                                        }else {
                                            url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=imperial&APPID=" + context.getString(R.string.weather_key);
                                            tempExt="°F";
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
                                                        finalRv4.setImageViewResource(R.id.IvWeatherIcon, Constants.getWeatherIcons(WeatherObject.getString("icon")));
                                                    }
                                                    JSONObject MainObject = obj.getJSONObject("main");

                                                    JSONObject SysObject = obj.getJSONObject("sys");
                                                    long millisecond = Long.parseLong(SysObject.getString("sunrise"));
                                                    String dateString = DateFormat.format("HH:mm:ss a", new Date(millisecond)).toString();
                                                    finalRv4.setTextViewText(R.id.TvSunRise, dateString);
                                                    long millisecondset = Long.parseLong(SysObject.getString("sunset"));
                                                    String dateStringset = DateFormat.format("HH:mm:ss a", new Date(millisecondset)).toString();
                                                    finalRv4.setTextViewText(R.id.TvSunSet, dateStringset);
                                                    JSONObject WindObject = obj.getJSONObject("wind");
                                                    double millisecondWind = Double.parseDouble(WindObject.getString("speed"));
                                                    finalRv4.setTextViewText(R.id.TvWind, mps_to_kmph(millisecondWind)+"km/h");
                                                    finalRv4.setTextViewText(R.id.TvCity, obj.get("name") + "," + SysObject.get("country"));
                                                    String Temp = MainObject.get("temp").toString();
                                                    finalRv4.setTextViewText(R.id.TvTemp, Temp.substring(0, Temp.lastIndexOf(".")) +tempExt);
                                                    String MinTemp = MainObject.get("temp_min").toString();
                                                    String MaxTemp = MainObject.get("temp_max").toString();
                                                    finalRv4.setTextViewText(R.id.TvTempMaxMin, "H:" + MaxTemp.substring(0, MaxTemp.lastIndexOf(".")) +tempExt+ " L:" + MinTemp.substring(0, MinTemp.lastIndexOf(".")) + tempExt);
                                                    finalRv4.setTextViewText(R.id.TvHumidity, MainObject.get("humidity").toString()+ "%" );
                                                    finalRv4.setTextViewText(R.id.TvPressure, MainObject.get("pressure").toString()+ "%" );

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

                                        String url1;
                                        if (widgetData.getTemp()==0) {
                                            url1 = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&cnt=6&units=metric&APPID=" + context.getString(R.string.weather_key);
                                        }else {
                                            url1 = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&cnt=6&units=imperial&APPID=" + context.getString(R.string.weather_key);
                                        }
                                        StringRequest stringReq1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject MainObject=null;
                                                    JSONArray IconObject=null;
                                                    String Temp=null;
                                                    String res=null;

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
                                                                finalRv4.setTextViewText(R.id.TvTimeFirst,res.toString());
                                                                MainObject = WeatherObject.getJSONObject("main");
                                                                Temp = MainObject.getString("temp").toString();
                                                                finalRv4.setTextViewText(R.id.TvTempFirst,Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                IconObject = WeatherObject.getJSONArray("weather");
                                                                finalRv4.setImageViewResource(R.id.IvWeatherIconFirst, Constants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                break;
                                                            case 1:
                                                                finalRv4.setTextViewText(R.id.TvTimeSecond,res.toString());
                                                                MainObject = WeatherObject.getJSONObject("main");
                                                                Temp = MainObject.getString("temp").toString();
                                                                finalRv4.setTextViewText(R.id.TvTempSecond,Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                IconObject = WeatherObject.getJSONArray("weather");
                                                                finalRv4.setImageViewResource(R.id.IvWeatherIconSecond, Constants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                break;
                                                            case 2:
                                                                finalRv4.setTextViewText(R.id.TvTimeThird,res.toString());
                                                                MainObject = WeatherObject.getJSONObject("main");
                                                                Temp = MainObject.getString("temp").toString();
                                                                finalRv4.setTextViewText(R.id.TvTempThird,Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                IconObject = WeatherObject.getJSONArray("weather");
                                                                finalRv4.setImageViewResource(R.id.IvWeatherIconThird, Constants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                break;
                                                            case 3:
                                                                finalRv4.setTextViewText(R.id.TvTimeForth,res.toString());
                                                                MainObject = WeatherObject.getJSONObject("main");
                                                                Temp = MainObject.getString("temp").toString();
                                                                finalRv4.setTextViewText(R.id.TvTempForth,Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                IconObject = WeatherObject.getJSONArray("weather");
                                                                finalRv4.setImageViewResource(R.id.IvWeatherIconForth, Constants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                break;
                                                            case 4:
                                                                finalRv4.setTextViewText(R.id.TvTimeFifth,res.toString());
                                                                MainObject = WeatherObject.getJSONObject("main");
                                                                Temp = MainObject.getString("temp").toString();
                                                                finalRv4.setTextViewText(R.id.TvTempFifth,Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                IconObject = WeatherObject.getJSONArray("weather");
                                                                finalRv4.setImageViewResource(R.id.IvWeatherIconFifth, Constants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                break;
                                                            case 5:
                                                                finalRv4.setTextViewText(R.id.TvTimeSixth,res.toString());
                                                                MainObject = WeatherObject.getJSONObject("main");
                                                                Temp = MainObject.getString("temp").toString();
                                                                finalRv4.setTextViewText(R.id.TvTempSixth,Temp.substring(0, Temp.lastIndexOf(".")) + "°");
                                                                IconObject = WeatherObject.getJSONArray("weather");
                                                                finalRv4.setImageViewResource(R.id.IvWeatherIconSixth, Constants.getWeatherIcons(IconObject.getJSONObject(0).getString("icon")));
                                                                break;
                                                        }
                                                    }

                                                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                                                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                                    new Pref(context).putBoolean(Pref.IS_WEATHER_1_ALARM, true);
                                                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, broadcast);
                                                    appWidgetManager.updateAppWidget(Widget_Id, finalRv4);
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
                                } else {

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


                    if (Constants.isNetworkAvailable(context)) {
                        rv.setImageViewResource(R.id.IvCellular, R.drawable.ic_xpanel_medium_2_mobiledata_selected);
                    } else {
                        rv.setImageViewResource(R.id.IvCellular, R.drawable.ic_xpanel_medium_2_mobiledata);
                    }
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

                    Intent intentCellular3 = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                    if (intentCellular3.resolveActivity(context.getPackageManager()) != null) {
                        configPendingIntent = PendingIntent.getActivity(context, 0, intentCellular3, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        rv.setOnClickPendingIntent(R.id.IvCellular, configPendingIntent);
                    }

                    Intent intentTorch3 = new Intent(context, XPanelFlashlight3WidgetReceiver.class);
                    configPendingIntent = PendingIntent.getBroadcast(context, 0, intentTorch3, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvTorch, configPendingIntent);
                    break;
            }

            appWidgetManager.updateAppWidget(Widget_Id, rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
    public int mps_to_kmph(double mps)
    {
        return(int) (3.6 * mps);
    }
    @Override
    public void onDeleted(Context context, int[] iArr) {
        for (int id : iArr) {
            DatabaseHelper helper = new DatabaseHelper(context);
            helper.getDeleteWidgets(id);
        }
    }
}

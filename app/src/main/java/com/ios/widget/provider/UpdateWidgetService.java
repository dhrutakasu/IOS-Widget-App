package com.ios.widget.provider;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CalendarContract;
import android.widget.RemoteViews;

import com.ios.widget.Model.WidgetData;
import com.ios.widget.R;
import com.ios.widget.helper.DatabaseHelper;
import com.ios.widget.utils.Constants;

import java.util.Calendar;
import java.util.Random;

public class UpdateWidgetService extends Service {

    Intent i;
    boolean started = false;
    final Handler h = new Handler();
    Runnable r;
    private Context context;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (started) {
            h.removeCallbacks(r);
        }
        context = this;
        started = true;
        i = intent;
        r = new Runnable() {
            @Override
            public void run() {
                action();
                h.postDelayed(this, 5000);
            }
        };
        h.post(r);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        h.removeCallbacks(r);
    }

    private void action() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());

        int[] allWidgetIds = i.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        for (int i = 0; i < allWidgetIds.length; ++i) {
            RemoteViews rv = null;
            long startMillis = 0;
            Uri.Builder builder = null;
            Intent intent = null;
            Intent intent1 = null;
            PendingIntent configPendingIntent = null;
            switch (Constants.Widget_Type_Id) {
                case 0:
                case 7:
                    //todo calender 4 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar4_small);
//                    rv.setImageViewBitmap(R.id.iv_background, Constants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.shape_app_widget_1c1c1e_r25_bg), 30));
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
//                    LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                        OnGPS(context);
//                    } else {
//                        getLocation(context, locationManager);
//                    }
//                    try {
//                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context
//                                , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    ActivityCompat#requestPermissions
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for ActivityCompat#requestPermissions for more details.
//                            return;
//                        }
//                        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                        URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + locationGPS + "&lang=72.5714&APPID=b317aca2e83ad16e219ff2283ca837d5");
//                        System.out.println("------- catch : " + url.getPath());
//                    } catch (MalformedURLException e) {
//                        System.out.println("------- catch : " + e.getMessage());
//                        throw new RuntimeException(e);
//                    }
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

                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlSmallClock, configPendingIntent);
                    break;
                case 3:
                case 23:
                    //todo photos small

                    break;
                case 4:
                    //todo calender 1 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar1_small);
                    rv.setImageViewBitmap(R.id.iv_background, Constants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.img_calendar1_small_bg), 30));
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMM");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMM");
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
                case 5:
                    //todo calender 2 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar3_small);
                    rv.setImageViewBitmap(R.id.iv_background2, Constants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.img_calendar2_small_bg), 30));
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "EEE");
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

                    Intent intents = new Intent(context, MyWidgetReceiver.class);
                    intents.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    context.sendBroadcast(intents);
                    break;
                case 6:
                    //todo calender 3 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar2_small);
//                    rv.setImageViewBitmap(R.id.iv_background, Constants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.shape_app_widget_ffffff_r25_bg), 30));
                    rv.setCharSequence(R.id.TClockDay, "setFormat12Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockDay, "setFormat24Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMMM yyyy");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMMM yyyy");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                    intents = new Intent(context, MediumWidgetService.class);
                    intents.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, allWidgetIds[i]);

                    intents.setData(Uri.parse(intents.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(allWidgetIds[i], R.id.GridCalendarMediumView, intents);

                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlSmallCal, configPendingIntent);
                    break;
                case 9:
                    //todo weather 2 small
                    break;
                case 10:
                    //todo weather 3 small
                    break;
                case 11:
                    //todo clock 1 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple1_small);
                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.analog_clock, configPendingIntent);
                    break;
                case 12:
                    //todo clock 2 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple2_small);

                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.analog_clock, configPendingIntent);
                    break;
                case 13:
                    //todo clock 3 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple3_small);

                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.analog_clock, configPendingIntent);
                    break;
                case 14:
                    //todo clock 4 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_realism1_small);

                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.analog_clock, configPendingIntent);
                    break;
                case 15:
                    //todo clock 5 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_realism2_small);

                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.analog_clock, configPendingIntent);
                    break;
                case 16:
                    //todo clock 6 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_realism3_small);

                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.analog_clock, configPendingIntent);
                    break;
                case 17:
                    //todo clock 7 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_text1_small);

                    rv.setImageViewBitmap(R.id.iv_background, Constants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.img_clock_text1_bg_small), 30));
                    rv.setImageViewResource(R.id.IvTextLeft, R.drawable.img_clock_text1_line_hour_large);
                    rv.setImageViewResource(R.id.IvTextRight, R.drawable.img_clock_text1_line_minute_large);
                    rv.setCharSequence(R.id.TClockHour, "setFormat12Hour", "HH");
                    rv.setCharSequence(R.id.TClockHour, "setFormat24Hour", "HH");
                    rv.setCharSequence(R.id.TClockMinutes, "setFormat12Hour", "mm");
                    rv.setCharSequence(R.id.TClockMinutes, "setFormat24Hour", "mm");
                    rv.setCharSequence(R.id.TClockDayMonthDate, "setFormat12Hour", "EEEE, MMM d");
                    rv.setCharSequence(R.id.TClockDayMonthDate, "setFormat24Hour", "EEEE, MMM d");

                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlSmallClock, configPendingIntent);
                    break;
                case 18:
                    //todo clock 8 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_text2_small);

                    rv.setImageViewBitmap(R.id.iv_background, Constants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.img_clock_text2_bg_small), 30));
                    rv.setImageViewResource(R.id.IvTextLeft, R.drawable.img_clock_text2_line_large);
                    rv.setImageViewResource(R.id.IvTextRight, R.drawable.img_clock_text2_line_large);
                    rv.setImageViewResource(R.id.IvTextLeftHook, R.drawable.img_clock_text2_decor);
                    rv.setImageViewResource(R.id.IvTextRightHook, R.drawable.img_clock_text2_decor);
                    rv.setImageViewResource(R.id.IvTextLeftHookCenter, R.drawable.img_clock_text2_decor);
                    rv.setImageViewResource(R.id.IvTextRightHookCenter, R.drawable.img_clock_text2_decor);
                    rv.setCharSequence(R.id.TClockHour, "setFormat12Hour", "HH");
                    rv.setCharSequence(R.id.TClockHour, "setFormat24Hour", "HH");
                    rv.setCharSequence(R.id.TClockMinutes, "setFormat12Hour", "mm");
                    rv.setCharSequence(R.id.TClockMinutes, "setFormat24Hour", "mm");
                    rv.setCharSequence(R.id.TClockDayMonthDate, "setFormat12Hour", "EEEE, MMM d");
                    rv.setCharSequence(R.id.TClockDayMonthDate, "setFormat24Hour", "EEEE, MMM d");

                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlSmallClock, configPendingIntent);
                    break;
                case 20:
                    //todo x-panel 1 small
//                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel1_small);
//
////                    rv.setImageViewBitmap(R.id.iv_background, Constants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.shape_app_widget_121212_r25_bg), 30));
//
//                    rv.setCharSequence(R.id.TClockAMPM, "setFormat12Hour", "a");
//                    rv.setCharSequence(R.id.TClockAMPM, "setFormat24Hour", "a");
//                    rv.setCharSequence(R.id.TClockHrMin, "setFormat12Hour", "h:mm");
//                    rv.setCharSequence(R.id.TClockHrMin, "setFormat24Hour", "h:mm");
//                    rv.setCharSequence(R.id.TClockDay, "setFormat12Hour", "EEE");
//                    rv.setCharSequence(R.id.TClockDay, "setFormat24Hour", "EEE");
//                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
//                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
//                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMMM");
//                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMMM");
//
//                    rv.setProgressBar(R.id.progressBar, 100, Constants.getBatteryLevel(context), false);
//                    rv.setTextViewText(R.id.LlSmallBatteryPerccentage, String.valueOf(Constants.getBatteryLevel(context)) + "%");
//
//                    IntentFilter intentFilter = new IntentFilter(ACTION_TOGGLE_WIFI);
//                    context.registerReceiver(receiver, intentFilter);
//
//
//                    IntentFilter intentFilterBattery = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//                    context.registerReceiver(receiverBattery, intentFilterBattery);
//
//                    if (Constants.IsWIfiConnected(context)) {
//                        rv.setImageViewResource(R.id.LlSmallWifi, R.drawable.ic_wifi);
//                        rv.setTextViewText(R.id.TvSmallWifi, "On");
//                    } else {
//                        rv.setImageViewResource(R.id.LlSmallWifi, R.drawable.ic_wifi_disabled);
//                        rv.setTextViewText(R.id.TvSmallWifi, "Off");
//                    }
//
//                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
//                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
//
//                    rv.setOnClickPendingIntent(R.id.LlSmallClock, configPendingIntent);
//                    rv.setOnClickPendingIntent(R.id.LlSmallDate, configPendingIntent);
//
//                    intents = new Intent(Settings.EXTRA_BATTERY_SAVER_MODE_ENABLED);
//                    configPendingIntent = PendingIntent.getActivity(context, 0, intents, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
//                    rv.setOnClickPendingIntent(R.id.LlSmallBattery, configPendingIntent);
//
//                    intents = new Intent(context, SmallCalenderWidgetProvider.class);
//                    intents.setAction("TOGGLE_WIFI");
//                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intents, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
//
//                    rv.setOnClickPendingIntent(R.id.LlSmallWifi, pendingIntent);
                    break;
                case 21:
                    //todo x-panel 2 small
                    break;
                case 22:
                    //todo x-panel 3 small
                    break;

            }
            DatabaseHelper helper = new DatabaseHelper(context);
            WidgetData widgetData = new WidgetData("S", String.valueOf(Constants.Widget_Type_Id), String.valueOf(allWidgetIds[i]));
            helper.InsertWidget(widgetData);
            appWidgetManager.updateAppWidget(allWidgetIds[i], rv);
            /*switch (Constants.Widget_Type_Id) {
                case 1:
                    //todo Dial clock 1 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple1_small);
                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.analog_clock, configPendingIntent);
                    break;
                case 2:
                    //todo Dial clock 2 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple2_small);

                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.analog_clock, configPendingIntent);
                    break;
                case 3:
                    //todo Dial clock 3 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple3_small);

                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.analog_clock, configPendingIntent);
                    break;
                case 4:
                    //todo Dial clock 4 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_realism1_small);

                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.analog_clock, configPendingIntent);
                    break;
                case 5:
                    //todo Dial clock 5 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_realism2_small);

                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.analog_clock, configPendingIntent);
                    break;
                case 6:
                    //todo Dial clock 6 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_realism3_small);

                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.analog_clock, configPendingIntent);
                    break;
                case 7:
                    //todo Text clock 1 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_text1_small);

                    rv.setImageViewBitmap(R.id.iv_background, Constants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.img_clock_text1_bg_small),30));
                    rv.setImageViewResource(R.id.IvTextLeft, R.drawable.img_clock_text1_line_hour_large);
                    rv.setImageViewResource(R.id.IvTextRight, R.drawable.img_clock_text1_line_minute_large);
                    rv.setCharSequence(R.id.TClockHour, "setFormat12Hour", "HH");
                    rv.setCharSequence(R.id.TClockHour, "setFormat24Hour", "HH");
                    rv.setCharSequence(R.id.TClockMinutes, "setFormat12Hour", "mm");
                    rv.setCharSequence(R.id.TClockMinutes, "setFormat24Hour", "mm");
                    rv.setCharSequence(R.id.TClockDayMonthDate, "setFormat12Hour", "EEEE, MMM d");
                    rv.setCharSequence(R.id.TClockDayMonthDate, "setFormat24Hour", "EEEE, MMM d");

                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlSmallClock, configPendingIntent);
                    break;
                case 13:
                    //todo calender 1 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar1_small);
                    rv.setImageViewBitmap(R.id.iv_background, Constants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.img_calendar1_small_bg),30));
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMM");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMM");
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
                case 14:
                    //todo calender 3 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar3_small);
                    rv.setImageViewResource(R.id.iv_background2, R.drawable.img_calendar2_small_bg);
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "EEE");
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
                case 15:
                    //todo calender 2 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar2_small);
                    rv.setImageViewResource(R.id.iv_background, R.drawable.shape_app_widget_ffffff_r25_bg);
                    rv.setCharSequence(R.id.TClockMonthYear, "setFormat12Hour", "MMMM yyyy");
                    rv.setCharSequence(R.id.TClockMonthYear, "setFormat24Hour", "MMMM yyyy");
                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW).setData(builder.build());

                    Intent intent = new Intent(context, SmallWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(appWidgetIds[i], R.id.GridCalendarSmallView, intent);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlSmallCal, configPendingIntent);
                    break;
                case 16:
                    //todo calender 4 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar4_small);
                    rv.setImageViewResource(R.id.iv_background, R.drawable.shape_app_widget_1c1c1e_r25_bg);
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
            }*/

            //todo calender 3 medium
            /*  RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar3_medium);
            rv.setImageViewResource(R.id.iv_background, R.drawable.img_calendar2_medium_bg);
            rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMM, yyyy");
            rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMM, yyyy");
            rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
            rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
            Intent intent = new Intent(context, MediumWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            rv.setRemoteAdapter(appWidgetIds[i], R.id.GridCalendarMediumView, intent);

            Intent openintent = new Intent() ;
            openintent.putExtra("beginTime", Calendar.getInstance().getTimeInMillis());
            openintent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            openintent.setClassName("com.android.calendar","com.android.calendar.AgendaActivity");
//            long startMillis = Calendar.getInstance().getTimeInMillis();
//            Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
//            builder.appendPath("time");
//            ContentUris.appendId(builder, startMillis);
//            Intent openintent = new Intent(Intent.ACTION_VIEW)
//                    .setData(builder.build());
            PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, openintent, PendingIntent.FLAG_MUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);

            rv.setOnClickPendingIntent(R.id.RlClockMedium, configPendingIntent);
            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);*/

            /*
            //todo calender 3 large
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar3_large);
            rv.setImageViewResource(R.id.iv_background, R.drawable.img_calendar2_large_bg);
            rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMM");
            rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMM");
            rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
            rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
            Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            rv.setRemoteAdapter(appWidgetIds[i], R.id.GridCalendarLargeView, intent);
            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
            */

        }
      /*  for (int widgetId : allWidgetIds) {
            // create some random data
            int number = new Random().nextInt(100);

            RemoteViews remoteViews = new RemoteViews(this
                    .getApplicationContext().getPackageName(),
                    R.layout.first_widget);
            remoteViews.setTextViewText(R.id.TvWidgetHeading, String.valueOf(number));

            // register an onClickListener
            Intent clickIntent = new Intent(this.getApplicationContext(), FirstWidget.class);

            clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.TvWidgetHeading, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }*/
        //stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

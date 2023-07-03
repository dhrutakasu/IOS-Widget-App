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
import java.util.Locale;

public class MediumUpdateWidgetService extends Service {

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

        int[] appWidgetIds = i.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);


        for (int i = 0; i < appWidgetIds.length; ++i) {
            RemoteViews rv = null;
            long startMillis = 0;
            Uri.Builder builder = null;
            Intent intent = null;
            Intent intent1 = null;
            PendingIntent configPendingIntent = null;
            System.out.println("****** Constant : " + Constants.Widget_Type_Id);
            System.out.println("****** Constant :Calendar " + Calendar.getInstance(Locale.getDefault()).getTime());

            switch (Constants.Widget_Type_Id) {
                case 0:
                case 13:
                    //todo clock 3 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple3_medium);
                    break;
                case 1:
                case 7:
                    //todo calender 4 medium

                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar4_medium);
//                    rv.setImageViewResource(R.id.iv_background, R.drawable.shape_app_widget_1c1c1e_r25_bg);
                    rv.setCharSequence(R.id.TClockDay, "setFormat12Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockDay, "setFormat24Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMMM");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMMM");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(appWidgetIds[i], R.id.GridCalendarMediumView, intent);

                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);
                    break;
                case 2:
                case 22:
                    //todo x-panel 3 medium

                    break;
                case 3:
                case 23:
                    //todo photos medium

                    break;
                case 4:
                    //todo calender 1 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar1_medium);
                    rv.setImageViewResource(R.id.iv_background, R.drawable.img_calendar1_medium_bg);
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMM, yyyy");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMM, yyyy");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(appWidgetIds[i], R.id.GridCalendarMediumView, intent);

                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);
                    break;
                case 5:
                    System.out.println("******** Constant 5555 ");
                    //todo calender 2 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar3_medium);
                    rv.setImageViewBitmap(R.id.iv_background, Constants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.img_calendar2_medium_bg), 30));
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "EEE, yyyy");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "EEE, yyyy");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                    RemoteViews finalRv = rv;
                    int finalI = i;
//                    r = new Runnable() {
//                        @Override
//                        public void run() {
                            System.out.println("******** Constant 5555 UPDATE ");
                            intent = new Intent(context, MediumWidgetService.class);
                            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[finalI]);

                            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                            finalRv.setRemoteAdapter(appWidgetIds[finalI], R.id.GridCalendarMediumView, intent);

//                            h.postDelayed(this, 5000);
//                        }
//                    };
//                    h.post(r);
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.GridCalendarMediumView);


                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);

                    break;
                case 6:
                    //todo calender 3 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar2_medium);
//                    rv.setImageViewBitmap(R.id.iv_background,Constants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.shape_app_widget_ffffff_r25_bg),30));
                    rv.setCharSequence(R.id.TClockDay, "setFormat12Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockDay, "setFormat24Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMMM yyyy");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMMM yyyy");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(appWidgetIds[i], R.id.GridCalendarMediumView, intent);

                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);
                    break;
                case 8:
                    //todo weather 2 medium
                    break;
                case 9:
                    //todo weather 2 medium
                    break;
                case 10:
                    //todo weather 3 medium
                    break;
                case 11:
                    //todo clock 1 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple1_medium);
                    break;
                case 12:
                    //todo clock 2 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple2_medium);
                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(appWidgetIds[i], R.id.GridCalendarMediumView, intent);
                    break;
                case 14:
                    //todo clock 4 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_realism1_medium);
                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(appWidgetIds[i], R.id.GridCalendarMediumView, intent);
                    break;
                case 15:
                    //todo clock 5 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_realism2_medium);
                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(appWidgetIds[i], R.id.GridCalendarMediumView, intent);
                    break;
                case 16:
                    //todo clock 6 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_realism3_medium);
                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(appWidgetIds[i], R.id.GridCalendarMediumView, intent);
                    break;
                case 17:
                    //todo clock 7 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_text1_medium);

                    rv.setImageViewBitmap(R.id.iv_background, Constants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.img_clock_text1_bg_medium), 30));
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

                    rv.setOnClickPendingIntent(R.id.RlMediumClock, configPendingIntent);
                    break;
                case 18:
                    //todo clock 8 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_text2_medium);

                    rv.setImageViewBitmap(R.id.iv_background, Constants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.img_clock_text2_bg_medium), 30));
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

                    rv.setOnClickPendingIntent(R.id.RlLargeClock, configPendingIntent);
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
                    break;
                case 20:
                    //todo x-panel 1 medium

//                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel1_medium);
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
//                    intent1 = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
//                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
//
//                    rv.setOnClickPendingIntent(R.id.LlMediumClock, configPendingIntent);
//                    rv.setOnClickPendingIntent(R.id.LlMediumDate, configPendingIntent);
//
//                    intent = new Intent(Settings.EXTRA_BATTERY_SAVER_MODE_ENABLED);
//                    configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
//
//                    rv.setOnClickPendingIntent(R.id.LlMediumBattery, configPendingIntent);
//
//                    intent = new Intent(Settings.ACTION_INTERNAL_STORAGE_SETTINGS);
//                    configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
//
//                    rv.setOnClickPendingIntent(R.id.LlMediumStorage, configPendingIntent);
                    break;
                case 21:
                    //todo x-panel 2 medium
                    break;

            }
            switch (Constants.Widget_Type_Id) {
                case 1: //todo Dial clock 1 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple1_medium);
                    break;
                case 2: //todo Dial clock 2 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple2_medium);
                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(appWidgetIds[i], R.id.GridCalendarMediumView, intent);
                    break;
                case 3: //todo Dial clock 3 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple3_medium);
                    break;
                case 4:
                    //todo Dial clock 4 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_realism1_medium);
                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(appWidgetIds[i], R.id.GridCalendarMediumView, intent);
                    break;
                case 5:
                    //todo Dial clock 5 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_realism2_medium);
                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(appWidgetIds[i], R.id.GridCalendarMediumView, intent);
                    break;
                case 6:
                    //todo Dial clock 6 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_realism3_medium);
                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(appWidgetIds[i], R.id.GridCalendarMediumView, intent);
                    break;
                case 7:
                    //todo Text clock 1 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_text1_medium);

                    rv.setImageViewBitmap(R.id.iv_background, Constants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.img_clock_text1_bg_medium), 30));
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

                    rv.setOnClickPendingIntent(R.id.RlMediumClock, configPendingIntent);
                    break;
                case 13:
                    //todo calender 1 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar1_medium);
                    rv.setImageViewResource(R.id.iv_background, R.drawable.img_calendar1_medium_bg);
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMM, yyyy");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMM, yyyy");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(appWidgetIds[i], R.id.GridCalendarMediumView, intent);

                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);
                    break;
                case 14:
                    //todo calender 2 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar3_medium);
                    rv.setImageViewResource(R.id.iv_background, R.drawable.img_calendar2_medium_bg);
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "EEE, yyyy");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "EEE, yyyy");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(appWidgetIds[i], R.id.GridCalendarMediumView, intent);

                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);
                    break;
                case 15:
                    //todo calender 3 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar2_medium);
                    rv.setImageViewResource(R.id.iv_background, R.drawable.shape_app_widget_ffffff_r25_bg);
                    rv.setCharSequence(R.id.TClockDay, "setFormat12Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockDay, "setFormat24Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMM, yyyy");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMM, yyyy");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(appWidgetIds[i], R.id.GridCalendarMediumView, intent);

                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);
                    break;
                case 16:
                    //todo calender 4 medium
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar4_medium);
                    rv.setImageViewResource(R.id.iv_background, R.drawable.shape_app_widget_1c1c1e_r25_bg);
                    rv.setCharSequence(R.id.TClockDay, "setFormat12Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockDay, "setFormat24Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMMM");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMMM");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(appWidgetIds[i], R.id.GridCalendarMediumView, intent);

                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);
                    break;
            }

            DatabaseHelper helper = new DatabaseHelper(context);
            WidgetData widgetData = new WidgetData("M", String.valueOf(Constants.Widget_Type_Id), String.valueOf(appWidgetIds[i]));
            helper.InsertWidget(widgetData);
            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);

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



            //todo calender 3 small
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar3_small);
            rv.setImageViewResource(R.id.iv_background2, R.drawable.img_calendar2_small_bg);
            rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMM");
            rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMM");
            rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
            rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
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

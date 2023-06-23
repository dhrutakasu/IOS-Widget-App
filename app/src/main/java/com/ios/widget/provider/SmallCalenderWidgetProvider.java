package com.ios.widget.provider;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.CalendarContract;
import android.widget.RemoteViews;

import com.ios.widget.R;
import com.ios.widget.ui.Activity.MainActivity;
import com.ios.widget.ui.Activity.WidgetItemActivity;
import com.ios.widget.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SmallCalenderWidgetProvider extends AppWidgetProvider {
//    private NotesDatabaseHelper helper;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; ++i) {
            RemoteViews rv = null;
            long startMillis = 0;
            Uri.Builder builder = null;
            Intent intent1 = null;
            PendingIntent configPendingIntent = null;
            switch (Constants.Widget_Type_Id) {
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
            }
            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);

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
        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }
    @Override
    public void onDeleted(Context context, int[] iArr) {
//        helper = new NotesDatabaseHelper(context);
//        for (int valueof : iArr) {
//            final Note note = (Note) helper.getWidgetId((int) valueof);
//            if (note != null) {
//                note.setCreateWidgetId(-1);
//            }
//            helper.updateNotes(note);
//        }
    }
}

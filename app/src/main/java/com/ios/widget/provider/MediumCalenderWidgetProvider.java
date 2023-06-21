package com.ios.widget.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.widget.RemoteViews;

import com.ios.widget.R;

import java.util.Calendar;

public class MediumCalenderWidgetProvider extends AppWidgetProvider {
//    private NotesDatabaseHelper helper;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; ++i) {
            //todo calender 3 medium
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar3_medium);
            rv.setImageViewResource(R.id.iv_background, R.drawable.img_calendar2_medium_bg);
            rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMM, yyyy");
            rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMM, yyyy");
            rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
            rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
            Intent intent = new Intent(context, MediumWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            rv.setRemoteAdapter(appWidgetIds[i], R.id.GridCalendarMediumView, intent);

            long startMillis = Calendar.getInstance().getTimeInMillis();
            Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
            builder.appendPath("time");
            ContentUris.appendId(builder, startMillis);
            Intent intent1 = new Intent(Intent.ACTION_VIEW)
                    .setData(builder.build());
            PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

            rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);
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
            */

            /*
            //todo calender 3 small
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar3_small);
            rv.setImageViewResource(R.id.iv_background2, R.drawable.img_calendar2_small_bg);
            rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMM");
            rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMM");
            rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
            rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);*/
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

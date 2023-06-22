package com.ios.widget.provider;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.ios.widget.R;

public class LargeClockWidgetProvider extends AppWidgetProvider {
//    private NotesDatabaseHelper helper;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; ++i) {

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

            //todo calender 3 small
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_realism1_large);
//            rv.setImageViewResource(R.id.iv_background2, R.drawable.);
//            rv.setCharSequence(R.id.analog_clock, "setDial ", R.drawable.img_clock_realism1_dial);
//            rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMM");
//            rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
//            rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
//            long startMillis=Calendar.getInstance().getTimeInMillis();
//            Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
//            builder.appendPath("time");
//            ContentUris.appendId(builder, startMillis);
//            Intent intent1   = new Intent(Intent.ACTION_VIEW).setData(builder.build());
//            PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);
//
//            rv.setOnClickPendingIntent(R.id.RlSmallCal, configPendingIntent);
            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
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

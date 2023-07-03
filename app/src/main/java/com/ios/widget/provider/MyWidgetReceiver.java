package com.ios.widget.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.widget.RemoteViews;

import com.ios.widget.R;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MyWidgetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), SmallCalenderWidgetProvider.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

            for (int appWidgetId : appWidgetIds) {
                System.out.println("******** appWidgetId " + appWidgetIds[appWidgetId]);
                RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar3_medium);
                rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "EEE, yyyy");
                rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "EEE, yyyy");
                rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                intent = new Intent(context, MediumWidgetService.class);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[appWidgetId]);

                intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                rv.setRemoteAdapter(appWidgetIds[appWidgetId], R.id.GridCalendarMediumView, intent);
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                       Intent intent = new Intent(context, MediumWidgetService.class);
                        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[appWidgetId]);

                        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                        rv.setRemoteAdapter(appWidgetIds[appWidgetId], R.id.GridCalendarMediumView, intent);
                    }
                }, 0, 1000);
                long startMillis = Calendar.getInstance().getTimeInMillis();
                Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                builder.appendPath("time");
                ContentUris.appendId(builder, startMillis);
                Intent intent1 = new Intent(Intent.ACTION_VIEW)
                        .setData(builder.build());
                PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);

                appWidgetManager.updateAppWidget(appWidgetId, rv);
            }
        }
    }

//    private void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        for (int appWidgetId : appWidgetIds) {
//            // Update widget UI here
//
//            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar3_medium);
//
//            // Perform daily update operations, e.g., fetch data, update text/images, etc.
//            String updatedText = getDailyUpdateText(); // Replace this with your own logic
//            views.setTextViewText(R.id.textView, updatedText); // Assuming you have a TextView with id "textView" in your widget layout
//
//            // Update the widget
//            appWidgetManager.updateAppWidget(appWidgetId, views);
//        }
//    }

    private String getDailyUpdateText() {
        // TODO: Implement your logic to fetch/update the daily text here
        return "Today's update"; // Replace this with your own logic
    }
}

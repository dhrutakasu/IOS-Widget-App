package com.ios.widget.provider;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;

import com.ios.widget.Model.WidgetData;
import com.ios.widget.R;
import com.ios.widget.helper.DatabaseHelper;
import com.ios.widget.utils.Pref;

import java.util.ArrayList;

public class BetteryBroadcastReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseHelper helper = new DatabaseHelper(context);
        ArrayList<WidgetData> widgetData = helper.getWidgets();
        for (int i = 0; i < widgetData.size(); i++) {
            if (widgetData.get(i).getPosition() == 21) {
                RemoteViews rv = null;
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
                RemoteViews finalrv = rv;

                finalrv.setTextViewText(R.id.progress_text, batLevel + "%");
                finalrv.setProgressBar(R.id.progress_bar, 100, batLevel, false);
                if (new Pref(context).getInt(Pref.IS_BATTERY, -1) != batLevel) {
                    System.out.println("************ WIFI RECEIVE BROAD 100/" + batLevel + " -- : 100/" + new Pref(context).getInt(Pref.IS_BATTERY, -1));
                    BatteryManager systemService = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
                    int batlevel = systemService.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                    finalrv.setTextViewText(R.id.progress_text, batLevel + "%");
                    finalrv.setProgressBar(R.id.progress_bar, 100, batLevel, false);
                    new Pref(context).putInt(Pref.IS_BATTERY, batlevel);

                }
                System.out.println("************ WIFI RECEIVE COME BROAD W_NUMBER " + widgetData.get(i).getNumber());
                AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), finalrv);
            }
        }

    }
}

package com.ios.widget.provider;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.ios.widget.Model.WidgetData;
import com.ios.widget.R;
import com.ios.widget.helper.DatabaseHelper;
import com.ios.widget.utils.Constants;
import com.ios.widget.utils.Pref;

import java.util.ArrayList;

public class BetteryBroadcastReceiver extends BroadcastReceiver {
    private boolean IsTorchOn;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseHelper helper = new DatabaseHelper(context);
        ArrayList<WidgetData> widgetData = helper.getWidgets();
        RemoteViews rv = null;
        PendingIntent configPendingIntent = null;

        for (int i = 0; i < widgetData.size(); i++) {
            if (widgetData.get(i).getPosition() == 20) {
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

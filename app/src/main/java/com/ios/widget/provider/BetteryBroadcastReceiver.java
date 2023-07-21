package com.ios.widget.provider;

import static com.ios.widget.utils.Constants.Widget_Type_Id;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.CalendarContract;
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
import java.util.Calendar;

public class BetteryBroadcastReceiver extends BroadcastReceiver {
    private boolean IsTorchOn;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intentReceiver) {
        System.out.println("********** : come come");
        DatabaseHelper helper = new DatabaseHelper(context);
        ArrayList<WidgetData> widgetData = helper.getWidgets();
        RemoteViews rv = null;
        PendingIntent configPendingIntent = null;

        long startMillis = 0;
        Uri.Builder builder = null;
        Intent intent = null;
        Intent intent1 = null;
        Calendar calendar;
        int currentDay;
        int currentMonth;
        int currentYear;
        for (int i = 0; i < widgetData.size(); i++) {
            System.out.println("********** : come come : " + widgetData.get(i).getPosition());
            if (widgetData.get(i).getPosition() == 0) {
                if (widgetData.get(i).getType() == 1) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple3_medium);

                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetData.get(i).getNumber());
                    intent.putExtra("TypeId", widgetData.get(i).getPosition());

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView, intent);
                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);

                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                    Calendar NewCalendar = Calendar.getInstance();
                    currentDay = NewCalendar.get(Calendar.DAY_OF_MONTH);
                    currentMonth = NewCalendar.get(Calendar.MONTH);
                    currentYear = NewCalendar.get(Calendar.YEAR);
                    if (!new Pref(context).getString(Pref.IS_TIME_3, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView);
                        new Pref(context).putString(Pref.IS_TIME_3, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                } else if (widgetData.get(i).getType() == 2) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel1_large);

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
                }
            }
            else if (widgetData.get(i).getPosition() == 1) {
                if (widgetData.get(i).getType() == 1) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar4_medium);
                    rv.setCharSequence(R.id.TClockDay, "setFormat12Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockDay, "setFormat24Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMMM");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMMM");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");

                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetData.get(i).getNumber());
                    intent.putExtra("TypeId", widgetData.get(i).getPosition());

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView, intent);
                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);

                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                    Calendar NewCalendar = Calendar.getInstance();
                    currentDay = NewCalendar.get(Calendar.DAY_OF_MONTH);
                    currentMonth = NewCalendar.get(Calendar.MONTH);
                    currentYear = NewCalendar.get(Calendar.YEAR);
                    if (!new Pref(context).getString(Pref.IS_DATE_4, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView);
                        new Pref(context).putString(Pref.IS_DATE_4, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                }
            }
            else if (widgetData.get(i).getPosition() == 2) {
                if (widgetData.get(i).getType() == 1) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel3_medium);
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
                        RemoteViews finalRv = rv;
                        int finalI = i;
                        manager.registerTorchCallback(new CameraManager.TorchCallback() {
                            @Override
                            public void onTorchModeUnavailable(@NonNull String cameraId) {
                                super.onTorchModeUnavailable(cameraId);
                            }

                            @Override
                            public void onTorchModeChanged(@NonNull String cameraId, boolean enabled) {
                                super.onTorchModeChanged(cameraId, enabled);
                                IsTorchOn = enabled;
                                if (widgetData.get(finalI).getType() == 2) {
                                    if (IsTorchOn) {
                                        finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1_selected);
                                    } else {
                                        finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1);
                                    }
                                } else {
                                    if (IsTorchOn) {
                                        finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_xpanel_medium_2_flashlight_selected);
                                    } else {
                                        finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_xpanel_medium_2_flashlight);
                                    }
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
                    if (widgetData.get(i).getType() == 2) {
                        if (Constants.IsWIfiConnected(context)) {
                            System.out.println("************ WIFI RECEIVE  ON ");
                            rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi1_selected);
                        } else {
                            System.out.println("************ WIFI RECEIVE  Off ");
                            rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi1);
                        }
                        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (bluetoothAdapter != null) {
                            if (bluetoothAdapter.isEnabled()) {
                                System.out.println("************  Bluetooth RECEIVE  ON ");
                                rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluethooth1_selected);
                            } else if (!bluetoothAdapter.isEnabled()) {
                                System.out.println("************  Bluetooth RECEIVE  else ");
                                rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluethooth1);
                            }
                        }
                    } else {
                        if (Constants.IsWIfiConnected(context)) {
                            System.out.println("************ WIFI RECEIVE  ON ");
                            rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_xpanel_medium_2_wifi_selected);
                        } else {
                            System.out.println("************ WIFI RECEIVE  Off ");
                            rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_xpanel_medium_2_wifi);
                        }
                        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (bluetoothAdapter != null) {
                            if (bluetoothAdapter.isEnabled()) {
                                System.out.println("************  Bluetooth RECEIVE  ON ");
                                rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_xpanel_medium_2_bluetooth_selected);
                            } else if (!bluetoothAdapter.isEnabled()) {
                                System.out.println("************  Bluetooth RECEIVE  else ");
                                rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_xpanel_medium_2_bluetooth);
                            }
                        }
                    }
                    final float totalSpace = Constants.DeviceMemory.getInternalStorageSpace();
                    final float occupiedSpace = Constants.DeviceMemory.getInternalUsedSpace();
                    final float freeSpace = Constants.DeviceMemory.getInternalFreeSpace();

                    rv.setProgressBar(R.id.progressBarCharge, 100, property, false);
                    rv.setProgressBar(R.id.progressBarStorage, (int) totalSpace, (int) occupiedSpace, false);

                    Intent intentWifi3 = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intentWifi3, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvWifi, configPendingIntent);

                    Intent intentBluetooth3 = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intentBluetooth3, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvBluetooth, configPendingIntent);

                    Intent intentCellular3 = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
//                    intentCellular.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
//                    intentCellular.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intentCellular3, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvCellular, configPendingIntent);

                    Intent intentTorch3 = new Intent(context, XPanelFlashlight3WidgetReceiver.class);
                    configPendingIntent = PendingIntent.getBroadcast(context, 0, intentTorch3, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvTorch, configPendingIntent);

                    System.out.println("************ WIFI RECEIVE COME BROAD W_NUMBER " + widgetData.get(i).getNumber());
                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                }
            } else if (widgetData.get(i).getPosition() == 4) {
                if (widgetData.get(i).getType() == 1) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar1_medium);
//                    rv.setImageViewResource(R.id.iv_background, R.drawable.img_calendar1_medium_bg);
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMM yyyy");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMM yyyy");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");

                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetData.get(i).getNumber());
                    intent.putExtra("TypeId", widgetData.get(i).getPosition());
                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView, intent);

                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);

                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                    Calendar NewCalendar = Calendar.getInstance();
                    currentDay = NewCalendar.get(Calendar.DAY_OF_MONTH);
                    currentMonth = NewCalendar.get(Calendar.MONTH);
                    currentYear = NewCalendar.get(Calendar.YEAR);
                    if (!new Pref(context).getString(Pref.IS_DATE_1, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView);
                        new Pref(context).putString(Pref.IS_DATE_1, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                } else if (widgetData.get(i).getType() == 2) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar1_large);
                    rv.setImageViewBitmap(R.id.iv_background, Constants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.img_calendar1_small_bg), 30));
                    rv.setCharSequence(R.id.TClockYear, "setFormat12Hour", "yyyy");
                    rv.setCharSequence(R.id.TClockYear, "setFormat24Hour", "yyyy");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMMM");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMMM");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                    intent = new Intent(context, LargeWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetData.get(i).getNumber());
                    intent.putExtra("TypeId", widgetData.get(i).getPosition());

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(widgetData.get(i).getNumber(), R.id.GridCalendarLargeView, intent);

                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlLargeCal, configPendingIntent);

                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                    Calendar NewCalendar = Calendar.getInstance();
                    currentDay = NewCalendar.get(Calendar.DAY_OF_MONTH);
                    currentMonth = NewCalendar.get(Calendar.MONTH);
                    currentYear = NewCalendar.get(Calendar.YEAR);
                    if (!new Pref(context).getString(Pref.IS_DATE_LARGE_1, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarLargeView);
                        new Pref(context).putString(Pref.IS_DATE_LARGE_1, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                }
            } else if (widgetData.get(i).getPosition() == 5) {
                if (widgetData.get(i).getType() == 1) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar3_medium);
                    rv.setImageViewBitmap(R.id.iv_background, Constants.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.img_calendar2_medium_bg), 30));
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "EEE, yyyy");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "EEE, yyyy");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");

                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetData.get(i).getNumber());
                    intent.putExtra("TypeId", widgetData.get(i).getPosition());
                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView, intent);

                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);

                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                    Calendar NewCalendar = Calendar.getInstance();
                    currentDay = NewCalendar.get(Calendar.DAY_OF_MONTH);
                    currentMonth = NewCalendar.get(Calendar.MONTH);
                    currentYear = NewCalendar.get(Calendar.YEAR);
                    if (!new Pref(context).getString(Pref.IS_DATE_3, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView);
                        new Pref(context).putString(Pref.IS_DATE_3, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                }
            } else if (widgetData.get(i).getPosition() == 6) {
                if (widgetData.get(i).getType() == 1) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar2_medium);
                    rv.setCharSequence(R.id.TClockDay, "setFormat12Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockDay, "setFormat24Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMMM yyyy");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMMM yyyy");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetData.get(i).getNumber());
                    intent.putExtra("TypeId", widgetData.get(i).getPosition());
                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView, intent);

                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);

                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                    Calendar NewCalendar = Calendar.getInstance();
                    currentDay = NewCalendar.get(Calendar.DAY_OF_MONTH);
                    currentMonth = NewCalendar.get(Calendar.MONTH);
                    currentYear = NewCalendar.get(Calendar.YEAR);
                    if (!new Pref(context).getString(Pref.IS_DATE_2, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView);
                        new Pref(context).putString(Pref.IS_DATE_2, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                } else if (widgetData.get(i).getType() == 2) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar2_large);

                    intent = new Intent(context, LargeWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetData.get(i).getNumber());
                    intent.putExtra("TypeId", widgetData.get(i).getPosition());

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(widgetData.get(i).getNumber(), R.id.GridCalendarLargeView, intent);

                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlLargeCal, configPendingIntent);

                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                    Calendar NewCalendar = Calendar.getInstance();
                    currentDay = NewCalendar.get(Calendar.DAY_OF_MONTH);
                    currentMonth = NewCalendar.get(Calendar.MONTH);
                    currentYear = NewCalendar.get(Calendar.YEAR);
                    if (!new Pref(context).getString(Pref.IS_DATE_LARGE_3, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarLargeView);
                        new Pref(context).putString(Pref.IS_DATE_LARGE_3, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                }
            } else if (widgetData.get(i).getPosition() == 7) {
                if (widgetData.get(i).getType() == 1) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar4_medium);
                    rv.setCharSequence(R.id.TClockDay, "setFormat12Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockDay, "setFormat24Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMMM");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMMM");
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");

                    intent = new Intent(context, MediumWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetData.get(i).getNumber());
                    intent.putExtra("TypeId", widgetData.get(i).getPosition());
                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView, intent);
                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlMediumCal, configPendingIntent);

                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                    Calendar NewCalendar = Calendar.getInstance();
                    currentDay = NewCalendar.get(Calendar.DAY_OF_MONTH);
                    currentMonth = NewCalendar.get(Calendar.MONTH);
                    currentYear = NewCalendar.get(Calendar.YEAR);
                    if (!new Pref(context).getString(Pref.IS_DATE_4, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarMediumView);
                        new Pref(context).putString(Pref.IS_DATE_4, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                } else if (widgetData.get(i).getType() == 2) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_calendar4_large);
                    rv.setCharSequence(R.id.TClockDate, "setFormat12Hour", "d");
                    rv.setCharSequence(R.id.TClockDate, "setFormat24Hour", "d");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat12Hour", "MMMM, yyyy");
                    rv.setCharSequence(R.id.TClockMonth, "setFormat24Hour", "MMMM, yyyy");
                    rv.setCharSequence(R.id.TClockDay, "setFormat12Hour", "EEEE");
                    rv.setCharSequence(R.id.TClockDay, "setFormat24Hour", "EEEE");

                    intent = new Intent(context, LargeWidgetService.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetData.get(i).getNumber());
                    intent.putExtra("TypeId", widgetData.get(i).getPosition());

                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                    rv.setRemoteAdapter(widgetData.get(i).getNumber(), R.id.GridCalendarLargeView, intent);
                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlLargeCal, configPendingIntent);

                    AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                    Calendar NewCalendar = Calendar.getInstance();
                    currentDay = NewCalendar.get(Calendar.DAY_OF_MONTH);
                    currentMonth = NewCalendar.get(Calendar.MONTH);
                    currentYear = NewCalendar.get(Calendar.YEAR);
                    if (!new Pref(context).getString(Pref.IS_DATE_LARGE_4, "").equalsIgnoreCase(currentDay + "/" + currentMonth + "/" + currentYear)) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetData.get(i).getNumber(), R.id.GridCalendarLargeView);
                        new Pref(context).putString(Pref.IS_DATE_LARGE_4, currentDay + "/" + currentMonth + "/" + currentYear);
                    }
                    appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                }
            } else if (widgetData.get(i).getPosition() == 20) {
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


                Intent intentBattery = new Intent(Settings.EXTRA_BATTERY_SAVER_MODE_ENABLED);
                configPendingIntent = PendingIntent.getActivity(context, 0, intentBattery, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                rv.setOnClickPendingIntent(R.id.RlBattery, configPendingIntent);
                System.out.println("************ WIFI RECEIVE COME BROAD W_NUMBER " + widgetData.get(i).getNumber());
                AppWidgetManager appWidgetManager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
                appWidgetManager.updateAppWidget(widgetData.get(i).getNumber(), rv);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
            } else if (widgetData.get(i).getPosition() == 22) {
                if (widgetData.get(i).getType() == 0) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel3_small);
                } else if (widgetData.get(i).getType() == 1) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel3_medium);
                } else if (widgetData.get(i).getType() == 2) {
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel3_large);
                }

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
                    RemoteViews finalRv = rv;
                    int finalI = i;
                    manager.registerTorchCallback(new CameraManager.TorchCallback() {
                        @Override
                        public void onTorchModeUnavailable(@NonNull String cameraId) {
                            super.onTorchModeUnavailable(cameraId);
                        }

                        @Override
                        public void onTorchModeChanged(@NonNull String cameraId, boolean enabled) {
                            super.onTorchModeChanged(cameraId, enabled);
                            IsTorchOn = enabled;
                            if (widgetData.get(finalI).getType() == 2) {
                                if (IsTorchOn) {
                                    finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1_selected);
                                } else {
                                    finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1);
                                }
                            } else {
                                if (IsTorchOn) {
                                    finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_xpanel_medium_2_flashlight_selected);
                                } else {
                                    finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_xpanel_medium_2_flashlight);
                                }
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
                if (widgetData.get(i).getType() == 2) {
                    if (Constants.IsWIfiConnected(context)) {
                        System.out.println("************ WIFI RECEIVE  ON ");
                        rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi1_selected);
                    } else {
                        System.out.println("************ WIFI RECEIVE  Off ");
                        rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi1);
                    }
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (bluetoothAdapter != null) {
                        if (bluetoothAdapter.isEnabled()) {
                            System.out.println("************  Bluetooth RECEIVE  ON ");
                            rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluethooth1_selected);
                        } else if (!bluetoothAdapter.isEnabled()) {
                            System.out.println("************  Bluetooth RECEIVE  else ");
                            rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluethooth1);
                        }
                    }
                } else {
                    if (Constants.IsWIfiConnected(context)) {
                        System.out.println("************ WIFI RECEIVE  ON ");
                        rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_xpanel_medium_2_wifi_selected);
                    } else {
                        System.out.println("************ WIFI RECEIVE  Off ");
                        rv.setImageViewResource(R.id.IvWifi, R.drawable.ic_xpanel_medium_2_wifi);
                    }
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (bluetoothAdapter != null) {
                        if (bluetoothAdapter.isEnabled()) {
                            System.out.println("************  Bluetooth RECEIVE  ON ");
                            rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_xpanel_medium_2_bluetooth_selected);
                        } else if (!bluetoothAdapter.isEnabled()) {
                            System.out.println("************  Bluetooth RECEIVE  else ");
                            rv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_xpanel_medium_2_bluetooth);
                        }
                    }
                }
                final float totalSpace = Constants.DeviceMemory.getInternalStorageSpace();
                final float occupiedSpace = Constants.DeviceMemory.getInternalUsedSpace();
                final float freeSpace = Constants.DeviceMemory.getInternalFreeSpace();

                rv.setProgressBar(R.id.progressBarCharge, 100, property, false);
                rv.setProgressBar(R.id.progressBarStorage, (int) totalSpace, (int) occupiedSpace, false);

                Intent intentWifi3 = new Intent(Settings.ACTION_WIFI_SETTINGS);
                configPendingIntent = PendingIntent.getActivity(context, 0, intentWifi3, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                rv.setOnClickPendingIntent(R.id.IvWifi, configPendingIntent);

                Intent intentBluetooth3 = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                configPendingIntent = PendingIntent.getActivity(context, 0, intentBluetooth3, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                rv.setOnClickPendingIntent(R.id.IvBluetooth, configPendingIntent);

                Intent intentCellular3 = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
//                    intentCellular.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
//                    intentCellular.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                configPendingIntent = PendingIntent.getActivity(context, 0, intentCellular3, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                rv.setOnClickPendingIntent(R.id.IvCellular, configPendingIntent);

                Intent intentTorch3 = new Intent(context, XPanelFlashlight3WidgetReceiver.class);
                configPendingIntent = PendingIntent.getBroadcast(context, 0, intentTorch3, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                rv.setOnClickPendingIntent(R.id.IvTorch, configPendingIntent);

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

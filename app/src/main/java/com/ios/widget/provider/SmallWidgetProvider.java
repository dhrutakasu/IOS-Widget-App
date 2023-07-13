package com.ios.widget.provider;

import static com.ios.widget.utils.Constants.Widget_Id;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import com.ios.widget.Model.WidgetData;
import com.ios.widget.R;
import com.ios.widget.helper.DatabaseHelper;
import com.ios.widget.utils.Constants;

import java.util.Calendar;

public class SmallWidgetProvider extends AppWidgetProvider {
    private Handler handler;
    private Runnable runnable;
    private boolean IsTorchOn;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        handler = new Handler();
        DatabaseHelper helper = new DatabaseHelper(context);
//        helper.getDeleteWidgets();
        System.out.println("_*_*_*_*_*_*_ 11 :: " + helper.getWidgetCount());
        WidgetData widgetData = new WidgetData(0, Constants.Widget_Type_Id, -1);
        int insert = helper.InsertWidget(widgetData);
        System.out.println("_*_*_*_*_*_*_ insert : " + insert);
        for (int id : appWidgetIds) {
            Widget_Id = id;
            System.out.println("_*_*_*_*_*_*_ uuid " + id);
        }
        if (helper.getWidgetCount() != 0) {
            WidgetData widgetsId = helper.getWidgetsId(insert);
            System.out.println("_*_*_*_*_*_*_ 33 :: " + widgetsId);
            widgetsId.setNumber(Widget_Id);
            helper.updateWidget(widgetsId);
        }
        System.out.println("_*_*_*_*_*_*_ getWidgets " + helper.getWidgets().size());
        for (int i = 0; i < helper.getWidgets().size(); ++i) {
            RemoteViews rv = null;
            long startMillis = 0;
            Uri.Builder builder = null;
            Intent intent = null;
            Intent intent1 = null;
            PendingIntent configPendingIntent = null;
            System.out.println("_*_*_*_*_*_*_ Widget_Type_Id : " + helper.getWidgets().get(i).getPosition());
            switch (helper.getWidgets().get(i).getPosition()) {
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

                    intent1 = new Intent(Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlSmallClock, configPendingIntent);
                    break;
                case 3:
                case 23:
                    //todo photos small

                    break;
                case 4:
                    //todo calender 1 small
                    System.out.println("_*_*_*_*_*_*_ Widget_Type_Id 444444 : " + helper.getWidgets().get(i).getPosition());
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

                    startMillis = Calendar.getInstance().getTimeInMillis();
                    builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent1 = new Intent(Intent.ACTION_VIEW).setData(builder.build());
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
                    intent1 = new Intent(Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.analog_clock, configPendingIntent);
                    break;
                case 12:
                    //todo clock 2 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple2_small);

                    intent1 = new Intent(Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.analog_clock, configPendingIntent);
                    break;
                case 13:
                    //todo clock 3 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_simple3_small);

                    intent1 = new Intent(Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.analog_clock, configPendingIntent);
                    break;
                case 14:
                    //todo clock 4 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_realism1_small);

                    intent1 = new Intent(Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.analog_clock, configPendingIntent);
                    break;
                case 15:
                    //todo clock 5 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_realism2_small);

                    intent1 = new Intent(Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.analog_clock, configPendingIntent);
                    break;
                case 16:
                    //todo clock 6 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_clock_realism3_small);

                    intent1 = new Intent(Settings.ACTION_DATE_SETTINGS);
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

                    intent1 = new Intent(Settings.ACTION_DATE_SETTINGS);
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

                    intent1 = new Intent(Settings.ACTION_DATE_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.RlSmallClock, configPendingIntent);
                    break;
                case 20:
                    //todo x-panel 1 small
                    rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel1_small);
                    RemoteViews finalRv = rv;
                    int finalI = i;

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
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[finalI], R.id.IvWifi);
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[finalI], R.id.IvTorch);

                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("************ WIFI RECEIVE  ");
                            if (Constants.IsWIfiConnected(context)) {
                                System.out.println("************ WIFI RECEIVE  ON ");
                                finalRv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi1_selected);
                            } else {
                                System.out.println("************ WIFI RECEIVE  Off ");
                                finalRv.setImageViewResource(R.id.IvWifi, R.drawable.ic_wifi1);
                            }

                            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                            if (mBluetoothAdapter != null) {
                                if (mBluetoothAdapter.isEnabled()) {
                                    System.out.println("************  Bluetooth RECEIVE  ON ");
                                    finalRv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluethooth1_selected);
                                } else if (!mBluetoothAdapter.isEnabled()) {
                                    System.out.println("************  Bluetooth RECEIVE  else ");
                                    finalRv.setImageViewResource(R.id.IvBluetooth, R.drawable.ic_bluethooth1);
                                }
                            }

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

                            System.out.println("********* ON / OFF : " + IsTorchOn);
                            if (IsTorchOn) {
                                finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1_selected);
                            } else {
                                finalRv.setImageViewResource(R.id.IvTorch, R.drawable.ic_tourch1);
                            }
                            appWidgetManager.updateAppWidget(appWidgetIds[finalI], finalRv);

                            handler.postDelayed(this, 2000);
                        }
                    };
                    handler.postDelayed(runnable, 0);

                    Intent intentWifi = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intentWifi, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvWifi, configPendingIntent);

                    Intent intentBluetooth = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intentBluetooth, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvBluetooth, configPendingIntent);

                    Intent intentCellular = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
//                    intentCellular.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
//                    intentCellular.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    configPendingIntent = PendingIntent.getActivity(context, 0, intentCellular, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.IvCellular, configPendingIntent);

                    Intent intent2 = new Intent(context, XPanelFlashlightWidgetReceiver.class);
                    intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds[i]);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    finalRv.setOnClickPendingIntent(R.id.IvTorch, pendingIntent);
                case 21:
                    //todo x-panel 2 small
                    break;
                case 22:
                    //todo x-panel 3 small
                    break;

            }
            System.out.println("_*_*_*_*_*_*_ Widget_Type_Id :getNumber " + helper.getWidgets().get(i).getNumber());
            appWidgetManager.updateAppWidget(Widget_Id, rv);

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        DatabaseHelper helper = new DatabaseHelper(context);
        System.out.println("_*_*_*_*_*_*_ 22 :: " + helper.getWidgetCount());
       /* int count;
        if (helper.getWidgetCount() == 0) {
            count = 1;
        } else {
            count = helper.getWidgetCount();
        }
        if (helper.getWidgetCount() != 0) {
            WidgetData widgetData = helper.getWidgetsId(count);
            System.out.println("_*_*_*_*_*_*_ 33 :: " + widgetData);
            widgetData.setNumber(Widget_Id);
            helper.updateWidget(widgetData);
        }*/
    }
}


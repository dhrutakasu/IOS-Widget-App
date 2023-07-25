package com.ios.widget.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.RemoteViews;

import com.ios.widget.Model.WidgetData;
import com.ios.widget.Model.WidgetImages;
import com.ios.widget.Model.WidgetMaster;
import com.ios.widget.R;
import com.ios.widget.helper.DatabaseHelper;
import com.ios.widget.ui.Activity.PhotoWidgetActivity;
import com.ios.widget.utils.Constants;
import com.ios.widget.utils.Pref;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;

public class SmallPhotoWidgetProvider extends AppWidgetProvider {
    private DatabaseHelper database;
    private static final String LeftClick = "left";
    private static final String RightClick = "right";
    private static final String WidgetSettingAction = "WidgetSetting";
    private int Widget_Id;

    public static int getCellsForSize(int i) {
        return (int) (Math.ceil(((double) i) + 30.0d) / 70.0d);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] iArr) {
        DatabaseHelper helper = new DatabaseHelper(context);
        System.out.println("********** insert : " + Constants.Widget_Type_Id);
        WidgetData widgetData = new WidgetData(0, Constants.Widget_Type_Id, -1,"");
        int insert = helper.InsertWidget(widgetData);
        System.out.println("_*_*_*_*_*_*_ insert : " + insert);
        for (int id : iArr) {
            Widget_Id = id;
            System.out.println("_*_*_*_*_*_*_ uuid " + id);
        }
        if (helper.getWidgetCount() != 0) {
            WidgetData widgetsId = helper.getWidgetsId(insert);
            System.out.println("_*_*_*_*_*_*_ 33 :: " + widgetsId);
            widgetsId.setNumber(Widget_Id);
            helper.updateWidget(widgetsId);
        }
        for (int id : iArr) {
            Widget_Id = id;
            System.out.println("*********** Widget_Id = " + Widget_Id);
//            if (Widget_Id != -1 && intent.getBooleanExtra("IS_FIRST_ADDED", false)) {
//                System.out.println("@@@@@@@  IS_FIRST_ADDED : " + intent.getBooleanExtra("IS_FIRST_ADDED", false));
//                if (intent.getBooleanExtra("IS_FIRST_ADDED", false)) {
            List widgetLists = new Pref(context).getWidgetLists(context);
            if (widgetLists == null) {
                widgetLists = new ArrayList();
            }
            widgetLists.add(String.valueOf(Widget_Id));
            new Pref(context).setWidgetLists(context, widgetLists);
            database = new DatabaseHelper(context);
            System.out.println("********* CheckIsAlreadyMasterOrNot : " + Constants.mSelectedList.size());
            System.out.println("********* CheckIsAlreadyMasterOrNot : " + Constants.mSelectedList.size());
            for (int i = 0; i < Constants.mSelectedList.size(); i++) {
                WidgetImages widgetImages = new WidgetImages("0", Constants.mSelectedList.get(i).getPath(), Widget_Id);
                if (database.CheckIsAlreadyDBorNot(Constants.mSelectedList.get(i).getPath().toString(), String.valueOf(Widget_Id))) {
                    WidgetImages widgetImages1 = new WidgetImages(database.getImageListData(Widget_Id).getImageId(), String.valueOf(Constants.mSelectedList.get(i).getPath()), Widget_Id);
                    database.updateWidgetImages(widgetImages1);
                } else {
                    database.InsertWidgetImage(widgetImages);
                }
            }
            if (database.CheckIsAlreadyMasterOrNot(String.valueOf(Widget_Id))) {
                WidgetMaster widgetMaster = database.getWidgetMaster(Widget_Id);
                WidgetMaster widgetMaster1 = new WidgetMaster();
                widgetMaster1.setWidgetId(Widget_Id);
                widgetMaster1.setInterval( new Pref(context).getWidgetInterval(context));
                widgetMaster1.setId(widgetMaster.getId());
                widgetMaster1.setSpaceBorder(widgetMaster.getSpaceBorder());
                widgetMaster1.setSize(widgetMaster.getSize());
                widgetMaster1.setShape(widgetMaster.getShape());
                widgetMaster1.setRow(widgetMaster.getRow());
                widgetMaster1.setRotationType(widgetMaster.getRotationType());
                widgetMaster1.setOpacity(widgetMaster.getOpacity());
                widgetMaster1.setFlipControl(true);
                widgetMaster1.setCustomMode(false);
                widgetMaster1.setCropType(widgetMaster.getCropType());
                widgetMaster1.setCornerBorder(widgetMaster.getCornerBorder());
                widgetMaster1.setColumn(widgetMaster.getColumn());
                widgetMaster1.setColorCode(widgetMaster.getColorCode());

                database.updateWidgetMaster(widgetMaster1);
            } else {
                WidgetMaster widgetMaster = new WidgetMaster();
                widgetMaster.setWidgetId(Widget_Id);
                widgetMaster.setInterval( new Pref(context).getWidgetInterval(context));
                database.InsertWidget(widgetMaster);
            }
            Class<SmallPhotoWidgetProvider> cls = SmallPhotoWidgetProvider.class;
            updateWidgetView(Widget_Id, context, new Intent(context, cls));
//                }
//            }
        }
//        List<String> widgetLists = AppPref.getWidgetLists(context);
//        if (widgetLists != null && widgetLists.size() > 0) {
//            for (int i = 0; i < widgetLists.size(); i++) {
//                int parseInt = Integer.parseInt(AppPref.getWidgetLists(context).get(i));
//                Intent intent = new Intent(context, getClass());
//                intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
//                intent.setData(Uri.parse(AppConstants.getUniqueId()));
//                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, parseInt);
//                updateWidgetView(parseInt, context, intent);
//            }
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                AppWidgetManager manager = (AppWidgetManager) context.getSystemService(AppWidgetManager.class);
//                ComponentName componentName = new ComponentName(context, PhotoWidgetProvider.class);
//                int[] ints = manager.getAppWidgetIds(componentName);
//                for (int id : ints) {
//                    Widget_Id = id;
//                    System.out.println("@@@@@@@  : " + Widget_Id);
//                    System.out.println("@@@@@@@  getIntExtra: " + intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0));
//                    System.out.println("@@@@@@@  IS_FIRST_ADDED: " + intent.getBooleanExtra("IS_FIRST_ADDED", false));
//                    if (Widget_Id != -1 && intent.getBooleanExtra("IS_FIRST_ADDED", false)) {
//                        System.out.println("@@@@@@@  IS_FIRST_ADDED : " + intent.getBooleanExtra("IS_FIRST_ADDED", false));
//                        if (intent.getBooleanExtra("IS_FIRST_ADDED", false)) {
//                            List widgetLists = AppPref.getWidgetLists(context);
//                            if (widgetLists == null) {
//                                widgetLists = new ArrayList();
//                            }
//                            widgetLists.add(String.valueOf(Widget_Id));
//                            AppPref.setWidgetLists(context, widgetLists);
//                            database = new DatabaseHelper(context);
//                            System.out.println("********* CheckIsAlreadyMasterOrNot : " + AppConstants.mSelectedList.size());
//                            for (int i = 0; i < AppConstants.mSelectedList.size(); i++) {
//                                WidgetImages widgetImages = new WidgetImages("0", AppConstants.mSelectedList.get(i).getPath(), Widget_Id);
//                                if (database.CheckIsAlreadyDBorNot(AppConstants.mSelectedList.get(i).getPath().toString(), String.valueOf(Widget_Id))) {
//                                    WidgetImages widgetImages1 = new WidgetImages(database.getImageListData(Widget_Id).getImageId(), String.valueOf(AppConstants.mSelectedList.get(i).getPath()), Widget_Id);
//                                    database.updateWidgetImages(widgetImages1);
//                                } else {
//                                    database.InsertWidgetImage(widgetImages);
//                                }
//                            }
//                            if (database.CheckIsAlreadyMasterOrNot(String.valueOf(Widget_Id))) {
//                                WidgetMaster widgetMaster = database.getWidgetMaster(Widget_Id);
//                                WidgetMaster widgetMaster1 = new WidgetMaster();
//                                widgetMaster1.setWidgetId(Widget_Id);
//                                widgetMaster1.setInterval(AppPref.getWidgetInterval(context));
//                                widgetMaster1.setId(widgetMaster.getId());
//                                widgetMaster1.setSpaceBorder(widgetMaster.getSpaceBorder());
//                                widgetMaster1.setSize(widgetMaster.getSize());
//                                widgetMaster1.setShape(widgetMaster.getShape());
//                                widgetMaster1.setRow(widgetMaster.getRow());
//                                widgetMaster1.setRotationType(widgetMaster.getRotationType());
//                                widgetMaster1.setOpacity(widgetMaster.getOpacity());
//                                widgetMaster1.setFlipControl(true);
//                                widgetMaster1.setCustomMode(false);
//                                widgetMaster1.setCropType(widgetMaster.getCropType());
//                                widgetMaster1.setCornerBorder(widgetMaster.getCornerBorder());
//                                widgetMaster1.setColumn(widgetMaster.getColumn());
//                                widgetMaster1.setColorCode(widgetMaster.getColorCode());
//
//                                database.updateWidget(widgetMaster1);
//                            } else {
//                                WidgetMaster widgetMaster = new WidgetMaster();
//                                widgetMaster.setWidgetId(Widget_Id);
//                                widgetMaster.setInterval(AppPref.getWidgetInterval(context));
//                                database.InsertWidget(widgetMaster);
//                            }
//                            updateWidgetView(Widget_Id, context, intent);
//                        }
//                    }
//                }
//            }
//        }, 1000);

//        int intExtra = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
//        intExtra = Widget_Id;

//        }
        super.onReceive(context, intent);
    }

    public static void updateWidgetView(int i, Context context, Intent intent) {
        AppWidgetManager appWidgetManager;
        int i2;
        int i3 = i;
        Context context2 = context;
        Class<SmallPhotoWidgetProvider> cls = SmallPhotoWidgetProvider.class;
        DatabaseHelper instance = new DatabaseHelper(context);
        boolean booleanExtra = intent.getBooleanExtra("flip", false);
        WidgetMaster mainWidget = instance.getWidgetMaster(i3);
        if (i3 != -1) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            if (mainWidget != null) {
                switch (mainWidget.getShape()) {
                    case 0:
                        switch (mainWidget.getInterval()) {
                            case 0:
                                remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_no_flip);
                                break;
                            case 1:
                                remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_flip_three);
                                break;
                            case 2:
                                remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_flip_five);
                                break;
                            case 3:
                                remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_flip_ten);
                                break;
                            case 4:
                                remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_flip_fifteen);
                                break;
                        }
                }
            }
            AppWidgetManager instance2 = AppWidgetManager.getInstance(context);
            appWidgetManager = instance2;
//            if (instance.getImageListCount(i3) <= 0) {
//                appWidgetManager = instance2;
//            } else
            System.out.println("******** Action : " + intent.getAction());
            if (LeftClick.equals(intent.getAction()) || RightClick.equals(intent.getAction())) {
                appWidgetManager = instance2;
                if (LeftClick.equals(intent.getAction())) {
                    remoteViews.showPrevious(R.id.flipView);
                } else if (RightClick.equals(intent.getAction())) {
                    remoteViews.showNext(R.id.flipView);
                }
            } else {
                remoteViews.setViewVisibility(R.id.rl1, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.fr, View.VISIBLE);
                System.out.println("******** isFlipControl : " + mainWidget.isFlipControl());
                if (mainWidget == null || !mainWidget.isFlipControl()) {
                    appWidgetManager = instance2;
                    Intent intent2 = new Intent(context2, PhotoWidgetActivity.class);
                    intent2.putExtra("widgetId", i3);
                    intent2.putExtra("flag", true);
                    intent2.setAction(WidgetSettingAction);
                    intent2.setData(Uri.parse(Constants.getUniqueId()));
                    PendingIntent activity = PendingIntent.getActivity(context2, 0, intent2, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    remoteViews.setViewVisibility(R.id.linFlipperPaging, View.GONE);
                    remoteViews.setOnClickPendingIntent(R.id.rl1, activity);
                } else {
                    System.out.println("******** context2 : " + mainWidget.isFlipControl());
                    Intent intent3 = new Intent(context2, PhotoWidgetActivity.class);
                    intent3.putExtra("widgetId", i3);
                    intent3.putExtra("flag", true);
                    intent3.setAction(WidgetSettingAction);
                    intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent3.setData(Uri.parse(Constants.getUniqueId()));
                    PendingIntent activity2 = PendingIntent.getActivity(context2, 0, intent3, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    Intent intent4 = new Intent(context2, cls);
                    intent4.setAction(LeftClick);
                    intent4.putExtra("flip", true);
                    intent4.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, i3);
                    appWidgetManager = instance2;
                    intent4.setData(Uri.parse(Constants.getUniqueId()));
                    PendingIntent broadcast = PendingIntent.getBroadcast(context2, 0, intent4, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    Intent intent5 = new Intent(context2, cls);
                    intent5.setAction(RightClick);
                    intent5.putExtra("flip", true);
                    intent5.putExtra("appWidgetId", i3);
                    intent5.setData(Uri.parse(Constants.getUniqueId()));
                    PendingIntent broadcast2 = PendingIntent.getBroadcast(context2, 0, intent5, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    remoteViews.setViewVisibility(R.id.linFlipperPaging, View.VISIBLE);
                    remoteViews.setOnClickPendingIntent(R.id.settingView, activity2);
                    remoteViews.setOnClickPendingIntent(R.id.preView, broadcast);
                    remoteViews.setOnClickPendingIntent(R.id.nextView, broadcast2);
                }
            }
            Intent intent8 = new Intent(context2, SmallPhotoWidgetService.class);
            intent8.putExtra("WidgetID", i3);
            intent8.setData(Uri.parse(Constants.getUniqueId()));
            if (mainWidget != null) {
//                if (!mainWidget.getWidgetName().trim().isEmpty()) {
//                    remoteViews.setViewVisibility(R.id.name_widget, View.VISIBLE);
//                    remoteViews.setTextViewText(R.id.name_widget, mainWidget.getWidgetName());
//                    remoteViews.setTextColor(R.id.name_widget, Color.parseColor(AppConstants.getcolorList().get(mainWidget.getColorCode()).getColorcode()));
//                    remoteViews.setTextViewTextSize(R.id.name_widget, 1, (float) mainWidget.getSize());
//                    i2 = 8;
//                } else {
//                    i2 = 8;
//                    remoteViews.setViewVisibility(R.id.name_widget, View.GONE);
//                }

                switch (mainWidget.getShape()) {
                    case 0:

                        remoteViews.setViewVisibility(R.id.rl1, View.VISIBLE);
                        remoteViews.setViewVisibility(R.id.fr, View.VISIBLE);
                        remoteViews.setViewVisibility(R.id.flipperMainView, View.VISIBLE);
                        remoteViews.setViewVisibility(R.id.flipView, View.VISIBLE);
                        if (!booleanExtra) {
                            remoteViews.setRemoteAdapter(R.id.flipView, intent8);
                            break;
                        }
                        break;
                }
            }
            appWidgetManager.notifyAppWidgetViewDataChanged(i3, R.id.flipView);

            appWidgetManager.updateAppWidget(i3, remoteViews);
        }
    }
}

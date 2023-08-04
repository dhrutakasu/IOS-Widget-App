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
import com.ios.widget.utils.MyAppConstants;
import com.ios.widget.utils.MyAppPref;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;

public class LargePhotoWidgetProvider extends AppWidgetProvider {
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
        System.out.println("******** LargePhotoWidgetProvider::");
        WidgetData widgetData = new WidgetData(2, MyAppConstants.Widget_Type_Id, -1,"", MyAppConstants.Temp_Id);
        int insert = helper.InsertWidget(widgetData);
        for (int id : iArr) {
            Widget_Id = id;
        }
        if (helper.getWidgetCount() != 0) {
            WidgetData widgetsId = helper.getWidgetsId(insert);
            widgetsId.setNumber(Widget_Id);
            helper.updateWidget(widgetsId);
        }
        for (int id : iArr) {
            Widget_Id = id;
            List widgetLists = new MyAppPref(context).getWidgetLists(context);
            if (widgetLists == null) {
                widgetLists = new ArrayList();
            }
            widgetLists.add(String.valueOf(Widget_Id));
            new MyAppPref(context).setWidgetLists(context, widgetLists);
            database = new DatabaseHelper(context);
            for (int i = 0; i < MyAppConstants.mSelectedList.size(); i++) {
                WidgetImages widgetImages = new WidgetImages("0", MyAppConstants.mSelectedList.get(i).getPath(), Widget_Id);
                if (database.CheckIsAlreadyDBorNot(MyAppConstants.mSelectedList.get(i).getPath().toString(), String.valueOf(Widget_Id))) {
                    WidgetImages widgetImages1 = new WidgetImages(database.getImageListData(Widget_Id).getImageId(), String.valueOf(MyAppConstants.mSelectedList.get(i).getPath()), Widget_Id);
                    database.updateWidgetImages(widgetImages1);
                } else {
                    database.InsertWidgetImage(widgetImages);
                }
            }
            if (database.CheckIsAlreadyMasterOrNot(String.valueOf(Widget_Id))) {
                WidgetMaster widgetMaster = database.getWidgetMaster(Widget_Id);
                WidgetMaster widgetMaster1 = new WidgetMaster();
                widgetMaster1.setWidgetId(Widget_Id);
                widgetMaster1.setInterval( new MyAppPref(context).getWidgetInterval(context));
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
                widgetMaster.setInterval( new MyAppPref(context).getWidgetInterval(context));
                database.InsertWidget(widgetMaster);
            }
            Class<LargePhotoWidgetProvider> cls = LargePhotoWidgetProvider.class;
            updateWidgetView(Widget_Id, context, new Intent(context, cls));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    public static void updateWidgetView(int i, Context context, Intent intent) {
        AppWidgetManager appWidgetManager;
        int i2;
        int i3 = i;
        Context context2 = context;
        Class<LargePhotoWidgetProvider> cls = LargePhotoWidgetProvider.class;
        DatabaseHelper instance = new DatabaseHelper(context);
        boolean booleanExtra = intent.getBooleanExtra("flip", false);
        WidgetMaster mainWidget = instance.getWidgetMaster(i3);
        if (i3 != -1) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget_photo_widget);
            if (mainWidget != null) {
                switch (mainWidget.getShape()) {
                    case 0:
                        switch (mainWidget.getInterval()) {
                            case 0:
                                remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget_no_flip);
                                break;
                            case 1:
                                remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget_flip_three);
                                break;
                            case 2:
                                remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget_flip_five);
                                break;
                            case 3:
                                remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget_flip_ten);
                                break;
                            case 4:
                                remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget_flip_fifteen);
                                break;
                        }
                }
            }
            AppWidgetManager instance2 = AppWidgetManager.getInstance(context);
            appWidgetManager = instance2;
            if (LeftClick.equals(intent.getAction()) || RightClick.equals(intent.getAction())) {
                appWidgetManager = instance2;
                if (LeftClick.equals(intent.getAction())) {
                    remoteViews.showPrevious(R.id.FlipView);
                } else if (RightClick.equals(intent.getAction())) {
                    remoteViews.showNext(R.id.FlipView);
                }
            } else {
                remoteViews.setViewVisibility(R.id.RlPhoto1, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.FlPhoto, View.VISIBLE);
                if (mainWidget == null || !mainWidget.isFlipControl()) {
                    appWidgetManager = instance2;
                    Intent intent2 = new Intent(context2, PhotoWidgetActivity.class);
                    intent2.putExtra("widgetId", i3);
                    intent2.putExtra("flag", true);
                    intent2.setAction(WidgetSettingAction);
                    intent2.setData(Uri.parse(MyAppConstants.getUniqueId()));
                    PendingIntent activity = PendingIntent.getActivity(context2, 0, intent2, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    remoteViews.setViewVisibility(R.id.LlFlipperPaging, View.GONE);
                    remoteViews.setOnClickPendingIntent(R.id.RlPhoto1, activity);
                } else {
                    Intent intent3 = new Intent(context2, PhotoWidgetActivity.class);
                    intent3.putExtra("widgetId", i3);
                    intent3.putExtra("flag", true);
                    intent3.setAction(WidgetSettingAction);
                    intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent3.setData(Uri.parse(MyAppConstants.getUniqueId()));
                    PendingIntent activity2 = PendingIntent.getActivity(context2, 0, intent3, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    Intent intent4 = new Intent(context2, cls);
                    intent4.setAction(LeftClick);
                    intent4.putExtra("flip", true);
                    intent4.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, i3);
                    appWidgetManager = instance2;
                    intent4.setData(Uri.parse(MyAppConstants.getUniqueId()));
                    PendingIntent broadcast = PendingIntent.getBroadcast(context2, 0, intent4, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    Intent intent5 = new Intent(context2, cls);
                    intent5.setAction(RightClick);
                    intent5.putExtra("flip", true);
                    intent5.putExtra("appWidgetId", i3);
                    intent5.setData(Uri.parse(MyAppConstants.getUniqueId()));
                    PendingIntent broadcast2 = PendingIntent.getBroadcast(context2, 0, intent5, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    remoteViews.setViewVisibility(R.id.LlFlipperPaging, View.VISIBLE);
                    remoteViews.setOnClickPendingIntent(R.id.FlSettingView, activity2);
                    remoteViews.setOnClickPendingIntent(R.id.FlPreView, broadcast);
                    remoteViews.setOnClickPendingIntent(R.id.FlNextView, broadcast2);
                }
            }
            Intent intent8 = new Intent(context2, LargePhotoWidgetService.class);
            intent8.putExtra("WidgetID", i3);
            intent8.setData(Uri.parse(MyAppConstants.getUniqueId()));
            if (mainWidget != null) {
                switch (mainWidget.getShape()) {
                    case 0:

                        remoteViews.setViewVisibility(R.id.RlPhoto1, View.VISIBLE);
                        remoteViews.setViewVisibility(R.id.FlPhoto, View.VISIBLE);
                        remoteViews.setViewVisibility(R.id.FlipperMainView, View.VISIBLE);
                        remoteViews.setViewVisibility(R.id.FlipView, View.VISIBLE);
                        if (!booleanExtra) {
                            remoteViews.setRemoteAdapter(R.id.FlipView, intent8);
                            break;
                        }
                        break;
                }
            }
            appWidgetManager.notifyAppWidgetViewDataChanged(i3, R.id.FlipView);

            appWidgetManager.updateAppWidget(i3, remoteViews);
        }
    }
}

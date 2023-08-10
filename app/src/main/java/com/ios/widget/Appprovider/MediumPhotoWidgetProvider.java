package com.ios.widget.Appprovider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.RemoteViews;

import com.ios.widget.ImageModel.AppWidgetData;
import com.ios.widget.ImageModel.AppWidgetImages;
import com.ios.widget.ImageModel.AppWidgetMaster;
import com.ios.widget.R;
import com.ios.widget.Apphelper.AppDatabaseHelper;
import com.ios.widget.Appui.Activity.PhotoWidgetActivity;
import com.ios.widget.AppUtils.MyAppConstants;
import com.ios.widget.AppUtils.MyAppPref;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.ios.widget.AppUtils.MyAppConstants.FlipWidget;

public class MediumPhotoWidgetProvider extends AppWidgetProvider {
    private AppDatabaseHelper database;
    private static final String LeftClick = "left";
    private static final String RightClick = "right";
    private static final String WidgetSettingAction = "WidgetSetting";
    private int Widget_Id;

    public static int getCellsForSize(int i) {
        return (int) (Math.ceil(((double) i) + 30.0d) / 100.0d);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] iArr) {
        AppDatabaseHelper helper = new AppDatabaseHelper(context);
        AppWidgetData appWidgetData = new AppWidgetData(1, MyAppConstants.Widget_Type_Id, -1,"", MyAppConstants.Temp_Id, MyAppConstants.IS_SIM_CARD);
        int insert = helper.InsertWidget(appWidgetData);
        for (int id : iArr) {
            Widget_Id = id;
        }
        if (helper.getWidgetCount() != 0) {
            AppWidgetData widgetsId = helper.getWidgetsId(insert);
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
            database = new AppDatabaseHelper(context);

            MyAppConstants.mSelectedList=new ArrayList<>();
            MyAppConstants.mSelectedList.addAll(MyAppConstants.mCropSelectedList);
            for (int i = 0; i < MyAppConstants.mSelectedList.size(); i++) {
                AppWidgetImages appWidgetImages = new AppWidgetImages("0", MyAppConstants.mSelectedList.get(i).getPath(), Widget_Id);
                if (database.CheckIsAlreadyDBorNot(MyAppConstants.mSelectedList.get(i).getPath().toString(), String.valueOf(Widget_Id))) {
                    AppWidgetImages appWidgetImages1 = new AppWidgetImages(database.getImageListData(Widget_Id).getImageId(), String.valueOf(MyAppConstants.mSelectedList.get(i).getPath()), Widget_Id);
                    database.updateWidgetImages(appWidgetImages1);
                } else {
                    database.InsertWidgetImage(appWidgetImages);
                }
            }
            if (database.CheckIsAlreadyMasterOrNot(String.valueOf(Widget_Id))) {
                AppWidgetMaster appWidgetMaster = database.getWidgetMaster(Widget_Id);
                AppWidgetMaster appWidgetMaster1 = new AppWidgetMaster();
                appWidgetMaster1.setWidgetId(Widget_Id);
                appWidgetMaster1.setInterval( new MyAppPref(context).getWidgetInterval(context));
                appWidgetMaster1.setId(appWidgetMaster.getId());
                appWidgetMaster1.setSpaceBorder(appWidgetMaster.getSpaceBorder());
                appWidgetMaster1.setSize(appWidgetMaster.getSize());
                appWidgetMaster1.setShape(appWidgetMaster.getShape());
                appWidgetMaster1.setRow(appWidgetMaster.getRow());
                appWidgetMaster1.setRotationType(appWidgetMaster.getRotationType());
                appWidgetMaster1.setOpacity(appWidgetMaster.getOpacity());
                appWidgetMaster1.setFlipControl(true);
                appWidgetMaster1.setCustomMode(false);
                appWidgetMaster1.setCropType(appWidgetMaster.getCropType());
                appWidgetMaster1.setCornerBorder(appWidgetMaster.getCornerBorder());
                appWidgetMaster1.setColumn(appWidgetMaster.getColumn());
                appWidgetMaster1.setColorCode(appWidgetMaster.getColorCode());

                database.updateWidgetMaster(appWidgetMaster1);
            } else {
                AppWidgetMaster appWidgetMaster = new AppWidgetMaster();
                appWidgetMaster.setWidgetId(Widget_Id);
                appWidgetMaster.setInterval( new MyAppPref(context).getWidgetInterval(context));
                database.InsertWidget(appWidgetMaster);
            }
            Class<MediumPhotoWidgetProvider> cls = MediumPhotoWidgetProvider.class;
            updateWidgetView(Widget_Id, context, new Intent(context, cls));

            Intent widget_create = new Intent("Widget_create");
            LocalBroadcastManager.getInstance(context).sendBroadcast(widget_create);
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
        Class<MediumPhotoWidgetProvider> cls = MediumPhotoWidgetProvider.class;
        AppDatabaseHelper instance = new AppDatabaseHelper(context);
        boolean booleanExtra = intent.getBooleanExtra(FlipWidget, false);
        AppWidgetMaster mainWidget = instance.getWidgetMaster(i3);
        if (i3 != -1) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget_photo_widget);
            if (mainWidget != null) {
                switch (mainWidget.getShape()) {
                    case 0:
                        switch (mainWidget.getInterval()) {
                            case 0:
                                remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget_flip_ten);
                                break;
                            case 1:
                                remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget_flip_twenty);
                                break;
                            case 2:
                                remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget_flip_thirty);
                                break;
                            case 3:
                                remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget_flip_one_min);
                                break;
                            case 4:
                                remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget_flip_five_min);
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
                    intent2.putExtra(MyAppConstants.WidgetId, i3);
                    intent2.putExtra(MyAppConstants.WidgetFlag, true);
                    intent2.setAction(WidgetSettingAction);
                    intent2.setData(Uri.parse(MyAppConstants.getUniqueId()));
                    PendingIntent activity = PendingIntent.getActivity(context2, 0, intent2, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    remoteViews.setViewVisibility(R.id.LlFlipperPaging, View.GONE);
                    remoteViews.setOnClickPendingIntent(R.id.RlPhoto1, activity);
                } else {
                    Intent intent3 = new Intent(context2, PhotoWidgetActivity.class);
                    intent3.putExtra(MyAppConstants.WidgetId, i3);
                    intent3.putExtra(MyAppConstants.WidgetFlag, true);
                    intent3.setAction(WidgetSettingAction);
                    intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent3.setData(Uri.parse(MyAppConstants.getUniqueId()));
                    PendingIntent activity2 = PendingIntent.getActivity(context2, 0, intent3, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    Intent intent4 = new Intent(context2, cls);
                    intent4.setAction(LeftClick);
                    intent4.putExtra(FlipWidget, true);
                    intent4.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, i3);
                    appWidgetManager = instance2;
                    intent4.setData(Uri.parse(MyAppConstants.getUniqueId()));
                    PendingIntent broadcast = PendingIntent.getBroadcast(context2, 0, intent4, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    Intent intent5 = new Intent(context2, cls);
                    intent5.setAction(RightClick);
                    intent5.putExtra(FlipWidget, true);
                    intent5.putExtra("appWidgetId", i3);
                    intent5.setData(Uri.parse(MyAppConstants.getUniqueId()));
                    PendingIntent broadcast2 = PendingIntent.getBroadcast(context2, 0, intent5, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    remoteViews.setViewVisibility(R.id.LlFlipperPaging, View.VISIBLE);
                    remoteViews.setOnClickPendingIntent(R.id.FlSettingView, activity2);
                    remoteViews.setOnClickPendingIntent(R.id.FlPreView, broadcast);
                    remoteViews.setOnClickPendingIntent(R.id.FlNextView, broadcast2);
                }
            }
            Intent intent8 = new Intent(context2, MediumPhotoWidgetService.class);
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

    @Override
    public void onDeleted(Context context, int[] iArr) {
        for (int id : iArr) {
            AppDatabaseHelper helper = new AppDatabaseHelper(context);
            helper.getDeleteWidgets(id);
        }
    }
}

package com.ios.widget.ui.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ios.widget.Model.WidgetModel;
import com.ios.widget.R;
import com.ios.widget.helper.DatabaseHelper;
import com.ios.widget.provider.LargeWidgetProvider;
import com.ios.widget.provider.LargeWidgetService;
import com.ios.widget.provider.MediumWidgetProvider;
import com.ios.widget.provider.SmallWidgetProvider;
import com.ios.widget.ui.Adapter.WidgetPagerAdapter;
import com.ios.widget.utils.Constants;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WidgetItemActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ImageView IvBack;
    private TextView TvTitle;
    private ViewPager PagerWidget;
    private ImageView IvAddWidget;

    private int pos;
    private TabLayout TabWidget, TabSizeLayout;
    private int TabPos;
    private ArrayList<WidgetModel> modelArrayList = new ArrayList<>();
    private WidgetPagerAdapter adapter;
    private AppWidgetManager manager;
    private ComponentName name;
    private DatabaseHelper helper;

    public static void UpdateWidget(int i, String packageName, Context context, int intExtra) {
        @SuppressLint("RemoteViewLayout") RemoteViews WidgetViews = new RemoteViews(packageName, R.layout.layout_widget_calendar3_large);

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        Intent svcIntent = new Intent(context, LargeWidgetService.class);

        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, intExtra);
        WidgetViews.setRemoteAdapter(R.id.GridCalendarLargeView, svcIntent);
//        long eventID = 208;
//        Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
//        Intent clickIntent = new Intent(Intent.ACTION_VIEW).setData(uri);
//        PendingIntent clickPI = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
//
//        WidgetViews.setOnClickPendingIntent(R.id.GridCalendarLargeView, clickPI);

        AppWidgetManager.getInstance(context).updateAppWidget(intExtra, WidgetViews);
//        gridCalendarAdapter = new GridCalendarAdapter(context, month, year, 0);
//        gridCalendarAdapter.notifyDataSetChanged();
//        GridCalendarLargeView.setAdapter(gridCalendarAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_item);
        initViews();
        initIntents();
        iniListeners();
        initActions();
    }

    private void initViews() {
        context = this;
        IvBack = (ImageView) findViewById(R.id.IvBack);
        TvTitle = (TextView) findViewById(R.id.TvTitle);
        PagerWidget = (ViewPager) findViewById(R.id.PagerWidget);
        IvAddWidget = (ImageView) findViewById(R.id.IvAddWidget);
        TabWidget = (TabLayout) findViewById(R.id.TabWidget);
        TabSizeLayout = (TabLayout) findViewById(R.id.TabSizeLayout);
    }

    private void initIntents() {
        pos = getIntent().getIntExtra(Constants.ITEM_POSITION, 0);
        TabPos = getIntent().getIntExtra(Constants.TabPos, 0);
        if (TabPos == 0) {
            TvTitle.setText("Trendy");
            modelArrayList = Constants.getTrendyWidgetLists();
        } else if (TabPos == 1) {
            TvTitle.setText("Calendar");
            modelArrayList = Constants.getCalendarWidgetLists();
        } else if (TabPos == 2) {
            TvTitle.setText("Weather");
            modelArrayList = Constants.getWeatherWidgetLists();
        } else if (TabPos == 3) {
            TvTitle.setText("Clock");
            modelArrayList = Constants.getClockWidgetLists();
        } else if (TabPos == 4) {
            TvTitle.setText("X-Panel");
            modelArrayList = Constants.getXPanelWidgetLists();
        } else if (TabPos == 5) {
            TvTitle.setText("Photo");
            modelArrayList = Constants.getPhotoWidgetLists();
        }
    }

    private void iniListeners() {
        IvBack.setOnClickListener(this);
        IvAddWidget.setOnClickListener(this);
    }

    private void initActions() {
        helper = new DatabaseHelper(context);
        if (modelArrayList.size() > 1) {
            TabWidget.setVisibility(View.VISIBLE);
        } else {
            TabWidget.setVisibility(View.GONE);
        }

        TabSizeLayout.addTab(TabSizeLayout.newTab().setText("Small"));
        TabSizeLayout.addTab(TabSizeLayout.newTab().setText("Medium"));
        TabSizeLayout.addTab(TabSizeLayout.newTab().setText("Large"));

        adapter = new WidgetPagerAdapter(this, modelArrayList, 0);
        PagerWidget.setAdapter(adapter);
        TabWidget.setupWithViewPager(PagerWidget, true);
        TabSizeLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                adapter.setchange(tab.getPosition());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.IvBack:
                onBackPressed();
                break;
            case R.id.IvAddWidget:
                GotoAddWidget();
                break;
        }
    }

    private void GotoAddWidget() {
        System.out.println("---------- Catch : " + modelArrayList.get(PagerWidget.getCurrentItem()).getPosition() + " - " + TabSizeLayout.getSelectedTabPosition());
        if ((modelArrayList.get(PagerWidget.getCurrentItem()).getPosition() == 0 && TabSizeLayout.getSelectedTabPosition() == 2) || (modelArrayList.get(PagerWidget.getCurrentItem()).getPosition() == 2 && TabSizeLayout.getSelectedTabPosition() == 1) || modelArrayList.get(PagerWidget.getCurrentItem()).getPosition() == 20 || modelArrayList.get(PagerWidget.getCurrentItem()).getPosition() == 22) {
            String s1 = Manifest.permission.CAMERA;
            Dexter.withActivity(this)
                    .withPermissions(s1)
                    .withListener(new MultiplePermissionsListener() {
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                setProviderWidgets();

                            }

                            if (report.isAnyPermissionPermanentlyDenied()) {
                                Constants.showSettingsDialog(WidgetItemActivity.this);
                            }
                        }

                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                       PermissionToken permissionToken) {
                            Constants.showPermissionDialog(WidgetItemActivity.this, permissionToken);
                        }
                    })
                    .onSameThread()
                    .check();
        } else if ((modelArrayList.get(PagerWidget.getCurrentItem()).getPosition() == 1 && TabSizeLayout.getSelectedTabPosition() == 0) || (modelArrayList.get(PagerWidget.getCurrentItem()).getPosition() == 2 && TabSizeLayout.getSelectedTabPosition() == 2) || modelArrayList.get(PagerWidget.getCurrentItem()).getPosition() == 8) {
            System.out.println("---------- Catch Enter: " + modelArrayList.get(PagerWidget.getCurrentItem()).getPosition() + " - " + TabSizeLayout.getSelectedTabPosition());

            String s = Manifest.permission.ACCESS_FINE_LOCATION;
            String s1 = Manifest.permission.ACCESS_COARSE_LOCATION;
            Dexter.withActivity(this)
                    .withPermissions(s, s1)
                    .withListener(new MultiplePermissionsListener() {
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                setProviderWidgets();
                            }

                            if (report.isAnyPermissionPermanentlyDenied()) {
                                Constants.showSettingsDialog(WidgetItemActivity.this);
                            }
                        }

                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken permissionToken) {
                            Constants.showPermissionDialog(WidgetItemActivity.this, permissionToken);
                        }
                    })
                    .onSameThread()
                    .check();

        } else {
            setProviderWidgets();
        }
    }

    private void setProviderWidgets() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Constants.Item_Id = PagerWidget.getCurrentItem();
            Constants.Widget_Type_Id = modelArrayList.get(PagerWidget.getCurrentItem()).getPosition();
            manager = (AppWidgetManager) getSystemService(AppWidgetManager.class);
            switch (TabSizeLayout.getSelectedTabPosition()) {
                case 0:
//                    name = new ComponentName(context, FirstWidget.class);
                    name = new ComponentName(context, SmallWidgetProvider.class);
                    break;
                case 1:
//                    name = new ComponentName(context, FirstWidget.class);
                    name = new ComponentName(context, MediumWidgetProvider.class);
                    break;
                case 2:
                    name = new ComponentName(context, LargeWidgetProvider.class);
                    break;
            }
            if (manager != null && manager.isRequestPinAppWidgetSupported()) {
                new Handler().postDelayed(() -> {
                    Intent intent = null;
                    switch (TabSizeLayout.getSelectedTabPosition()) {
                        case 0:
                            intent = new Intent(context, SmallWidgetProvider.class);
                            break;
                        case 1:
                            intent = new Intent(context, MediumWidgetProvider.class);
                            break;
                        case 2:
                            intent = new Intent(context, LargeWidgetProvider.class);
                            break;
                    }
                    intent.putExtra(Constants.TAG_WIDGET_NOTE_ID, pos);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                            PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    manager.requestPinAppWidget(name, (Bundle) null, pendingIntent);
                }, 100);
            }
        }
    }
}
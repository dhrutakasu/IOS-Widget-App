package com.ios.widget.ui.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private TabLayout TabWidget, TabTempLayout, TabSizeLayout;
    private int TabPos;
    private ArrayList<WidgetModel> modelArrayList = new ArrayList<>();
    private WidgetPagerAdapter adapter;
    private AppWidgetManager manager;
    private ComponentName name;
    private DatabaseHelper helper;
    private LinearLayout LlTemp;

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
        TabTempLayout = (TabLayout) findViewById(R.id.TabTempLayout);
        TabSizeLayout = (TabLayout) findViewById(R.id.TabSizeLayout);
        LlTemp = (LinearLayout) findViewById(R.id.LlTemp);
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
            LlTemp.setVisibility(View.VISIBLE);
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

        TabTempLayout.addTab(TabTempLayout.newTab().setText("°C"));
        TabTempLayout.addTab(TabTempLayout.newTab().setText("°F"));

        TabSizeLayout.addTab(TabSizeLayout.newTab().setText("Small"));
        TabSizeLayout.addTab(TabSizeLayout.newTab().setText("Medium"));
        TabSizeLayout.addTab(TabSizeLayout.newTab().setText("Large"));

        adapter = new WidgetPagerAdapter(this, modelArrayList, 0);
        PagerWidget.setAdapter(adapter);
        TabWidget.setupWithViewPager(PagerWidget, true);
        TabTempLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Constants.Temp_Id = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        TabSizeLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                adapter.setchange(tab.getPosition());
                adapter.notifyDataSetChanged();
                System.out.println("___________ Tab 1: " + TabPos + " - " + tab.getPosition() + " - " + TabWidget.getSelectedTabPosition());
                if (TabPos == 0) {
                    if ((TabWidget.getSelectedTabPosition() == 1 && tab.getPosition() == 0)) {
                        LlTemp.setVisibility(View.VISIBLE);
                    } else if ((TabWidget.getSelectedTabPosition() == 2 && tab.getPosition() == 2)) {
                        LlTemp.setVisibility(View.VISIBLE);
                    } else {
                        LlTemp.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        TabWidget.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println("___________ Tab 2: " + TabPos + " - " + tab.getPosition() + " - " + TabSizeLayout.getSelectedTabPosition());
                if (TabPos == 0) {
                    if ((TabSizeLayout.getSelectedTabPosition() == 0 && tab.getPosition() == 1)) {
                        LlTemp.setVisibility(View.VISIBLE);
                    } else if ((TabSizeLayout.getSelectedTabPosition() == 2 && tab.getPosition() == 2)) {
                        LlTemp.setVisibility(View.VISIBLE);
                    } else {
                        LlTemp.setVisibility(View.GONE);
                    }
                }
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
            String s = Manifest.permission.ACCESS_COARSE_LOCATION;
            String s1 = Manifest.permission.ACCESS_FINE_LOCATION;
            Dexter.withActivity(this)
                    .withPermissions(s, s1)
                    .withListener(new MultiplePermissionsListener() {
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                    setProviderWidgets();
                                } else {
                                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(myIntent);
                                }
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
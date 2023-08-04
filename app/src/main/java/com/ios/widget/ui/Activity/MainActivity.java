package com.ios.widget.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdSize;
import com.ios.widget.Ads.MyAppAd_Banner;
import com.ios.widget.Ads.MyAppAd_Interstitial;
import com.ios.widget.Model.WidgetData;
import com.ios.widget.R;
import com.ios.widget.helper.DatabaseHelper;
import com.ios.widget.provider.BetteryBroadcastReceiver;
import com.ios.widget.ui.Adapter.TypeImageAdapter;
import com.ios.widget.utils.MyAppConstants;
import com.ios.widget.utils.MyAppExitDialog;
import com.ios.widget.utils.MyAppPref;
import com.ios.widget.utils.NotificationHelper;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ImageView IvSettings, IvMyWidget;
    private RecyclerView RvTypeList;
    private int countExtra;
    private int itemClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        iniListeners();
        initActions();
    }

    private void initViews() {
        context = this;
        DatabaseHelper helper = new DatabaseHelper(context);
        helper.getWritableDatabase();
        ArrayList<WidgetData> widgetData = helper.getWidgets();
        System.out.println("____+++_ Table : " + widgetData.size());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            String s = Manifest.permission.POST_NOTIFICATIONS;
            Dexter.withActivity(this)
                    .withPermissions(s)
                    .withListener(new MultiplePermissionsListener() {
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (widgetData.size() > 0) {
                                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                                PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
                            } else {
                                NotificationHelper.showNonCancelableNotification(context, context.getString(R.string.app_name) + "Now", context.getString(R.string.app_name));
                            }

                            if (report.isAnyPermissionPermanentlyDenied()) {
                                MyAppConstants.showSettingsDialog(MainActivity.this);
                            }
                        }

                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken permissionToken) {
                        }
                    })
                    .onSameThread()
                    .check();
        } else {
            if (widgetData.size() > 0) {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
                PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
            } else {
                NotificationHelper.showNonCancelableNotification(context, context.getString(R.string.app_name) + "Now", context.getString(R.string.app_name));
            }
        }

        IvSettings = (ImageView) findViewById(R.id.IvSettings);
        IvMyWidget = (ImageView) findViewById(R.id.IvMyWidget);
        RvTypeList = (RecyclerView) findViewById(R.id.RvTypeList);
    }

    private void iniListeners() {
        IvSettings.setOnClickListener(this);
        IvMyWidget.setOnClickListener(this);
    }

    private void initActions() {
        MyAppAd_Banner.getInstance().showBanner(this, AdSize.LARGE_BANNER, (RelativeLayout) findViewById(R.id.RlBannerAdView), (RelativeLayout) findViewById(R.id.RlBannerAd));

        ArrayList<Integer> TypesArrayList = new ArrayList<>();
        TypesArrayList.add(R.drawable.btn_trendy);
        TypesArrayList.add(R.drawable.btn_calendar);
        TypesArrayList.add(R.drawable.btn_weather);
        TypesArrayList.add(R.drawable.btn_alarm);
        TypesArrayList.add(R.drawable.btn_x_panel);
        TypesArrayList.add(R.drawable.btn_photo);

        RvTypeList.setLayoutManager(new GridLayoutManager(context, 2));
        RvTypeList.setAdapter(new TypeImageAdapter(context, TypesArrayList, new TypeImageAdapter.setClickListener() {
            @Override
            public void ClickListener(int position) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    String s = Manifest.permission.POST_NOTIFICATIONS;
                    Dexter.withActivity(MainActivity.this)
                            .withPermissions(s)
                            .withListener(new MultiplePermissionsListener() {
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    if (report.areAllPermissionsGranted()) {
                                        countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
                                        itemClick = SplashActivity.click++;
                                        if (itemClick % countExtra == 0) {
                                            MyAppAd_Interstitial.getInstance().showInter(MainActivity.this, new MyAppAd_Interstitial.MyCallback() {
                                                @Override
                                                public void callbackCall() {
                                                    if (position == 5) {
                                                        MyAppConstants.clearAllSelection();
                                                        startActivity(new Intent(context, PhotoWidgetActivity.class));
                                                    } else {
                                                        Intent intent = new Intent(context, ShowItemActivity.class);
                                                        intent.putExtra(MyAppConstants.TabPos, position);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                        } else {
                                            if (position == 5) {
                                                MyAppConstants.clearAllSelection();
                                                startActivity(new Intent(context, PhotoWidgetActivity.class));
                                            } else {
                                                Intent intent = new Intent(context, ShowItemActivity.class);
                                                intent.putExtra(MyAppConstants.TabPos, position);
                                                startActivity(intent);
                                            }
                                        }
                                    }

                                    if (report.isAnyPermissionPermanentlyDenied()) {
                                        MyAppConstants.showSettingsDialog(MainActivity.this);
                                    }
                                }

                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken permissionToken) {
                                    MyAppConstants.showPermissionDialog(MainActivity.this, permissionToken);
                                }
                            })
                            .onSameThread()
                            .check();
                } else {
                    countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
                    itemClick = SplashActivity.click++;
                    if (itemClick % countExtra == 0) {
                        MyAppAd_Interstitial.getInstance().showInter(MainActivity.this, new MyAppAd_Interstitial.MyCallback() {
                            @Override
                            public void callbackCall() {
                                if (position == 5) {
                                    MyAppConstants.clearAllSelection();
                                    startActivity(new Intent(context, PhotoWidgetActivity.class));
                                } else {
                                    Intent intent = new Intent(context, ShowItemActivity.class);
                                    intent.putExtra(MyAppConstants.TabPos, position);
                                    startActivity(intent);
                                }
                            }
                        });
                    } else {
                        if (position == 5) {
                            MyAppConstants.clearAllSelection();
                            startActivity(new Intent(context, PhotoWidgetActivity.class));
                        } else {
                            Intent intent = new Intent(context, ShowItemActivity.class);
                            intent.putExtra(MyAppConstants.TabPos, position);
                            startActivity(intent);
                        }
                    }
                }
            }
        }));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.IvSettings:
                countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
                itemClick = SplashActivity.click++;
                if (itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(MainActivity.this, new MyAppAd_Interstitial.MyCallback() {
                        @Override
                        public void callbackCall() {
                            startActivity(new Intent(context, SettingActivity.class));
                        }
                    });
                } else {
                    startActivity(new Intent(context, SettingActivity.class));
                }
                break;
            case R.id.IvMyWidget:
                countExtra = new MyAppPref(context).getInt( MyAppPref.AD_COUNTER, 0);
                itemClick = SplashActivity.click++;
                if (itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(MainActivity.this, new MyAppAd_Interstitial.MyCallback() {
                        @Override
                        public void callbackCall() {
                            startActivity(new Intent(context, MyWidgetsActivity.class));
                        }
                    });
                } else {
                    startActivity(new Intent(context, MyWidgetsActivity.class));
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        int countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
        int itemClick = SplashActivity.click++;
        if (itemClick % countExtra == 0) {
            MyAppAd_Interstitial.getInstance().showInter(MainActivity.this, new MyAppAd_Interstitial.MyCallback() {
                @Override
                public void callbackCall() {
                    MyAppExitDialog exitDialog = new MyAppExitDialog(MainActivity.this,context, () -> finishAffinity());
                    exitDialog.show();
                    WindowManager.LayoutParams lp = exitDialog.getWindow().getAttributes();
                    Window window = exitDialog.getWindow();
                    lp.copyFrom(window.getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.gravity = Gravity.BOTTOM;
                    window.setAttributes(lp);
                }
            });
        } else {
            MyAppExitDialog exitDialog = new MyAppExitDialog(MainActivity.this,context, () -> finishAffinity());
            exitDialog.show();
            WindowManager.LayoutParams lp = exitDialog.getWindow().getAttributes();
            Window window = exitDialog.getWindow();
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            window.setAttributes(lp);
        }
    }
}
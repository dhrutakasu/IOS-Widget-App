package com.ios.widget.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdSize;
import com.ios.widget.Ads.MyAppAd_Banner;
import com.ios.widget.Ads.MyAppAd_Interstitial;
import com.ios.widget.Model.WidgetData;
import com.ios.widget.R;
import com.ios.widget.helper.DatabaseHelper;
import com.ios.widget.ui.Adapter.MyWidgetAdapter;
import com.ios.widget.utils.MyAppConstants;
import com.ios.widget.utils.MyAppPref;

import java.util.ArrayList;

public class MyWidgetsActivity extends AppCompatActivity implements View.OnClickListener, MyWidgetAdapter.deleteWidget {

    private Context context;
    private ImageView IvBack, IvNotFoundRecord, iv_done, IvMyWidgetTrendy, IvMyWidgetCalendar, IvMyWidgetWeather, IvMyWidgetAlarm, IvMyWidgetXPanel, IvMyWidgetPhoto;
    private TextView TvTitle;
    private DatabaseHelper helper;
    private RecyclerView RvMyWidget;
    private ArrayList<WidgetData> myWidgetLists = new ArrayList<WidgetData>();
    private int countExtra,itemClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_widgets_acivity);

        initViews();
        iniListeners();
        initActions();
    }

    private void initViews() {
        context = this;
        IvBack = (ImageView) findViewById(R.id.IvBack);
        iv_done = (ImageView) findViewById(R.id.IvDone);
        IvNotFoundRecord = (ImageView) findViewById(R.id.IvNotFoundRecord);
        IvMyWidgetTrendy = (ImageView) findViewById(R.id.IvMyWidgetTrendy);
        IvMyWidgetCalendar = (ImageView) findViewById(R.id.IvMyWidgetCalendar);
        IvMyWidgetWeather = (ImageView) findViewById(R.id.IvMyWidgetWeather);
        IvMyWidgetAlarm = (ImageView) findViewById(R.id.IvMyWidgetAlarm);
        IvMyWidgetXPanel = (ImageView) findViewById(R.id.IvMyWidgetXPanel);
        IvMyWidgetPhoto = (ImageView) findViewById(R.id.IvMyWidgetPhoto);
        TvTitle = (TextView) findViewById(R.id.TvTitle);
        RvMyWidget = (RecyclerView) findViewById(R.id.RvMyWidget);
    }

    private void iniListeners() {
        IvBack.setOnClickListener(this);
        iv_done.setOnClickListener(this);
        IvMyWidgetTrendy.setOnClickListener(this);
        IvMyWidgetCalendar.setOnClickListener(this);
        IvMyWidgetWeather.setOnClickListener(this);
        IvMyWidgetAlarm.setOnClickListener(this);
        IvMyWidgetXPanel.setOnClickListener(this);
        IvMyWidgetPhoto.setOnClickListener(this);
    }

    private void initActions() {
        MyAppAd_Banner.getInstance().showBanner(this, AdSize.LARGE_BANNER, (RelativeLayout) findViewById(R.id.RlBannerAdView), (RelativeLayout) findViewById(R.id.RlBannerAd));

        iv_done.setImageResource(R.drawable.ic_delete);
        helper = new DatabaseHelper(context);
        TvTitle.setText(getResources().getString(R.string.str_my_widget));
        myWidgetLists = new ArrayList<>();
        for (int j = 0; j < MyAppConstants.getTrendyWidgetLists().size(); j++) {
            System.out.println("_______%%%% PPPP: " + helper.getWidgetsType(MyAppConstants.getTrendyWidgetLists().get(j).getPosition()).toString());
            myWidgetLists.addAll(helper.getWidgetsType(MyAppConstants.getTrendyWidgetLists().get(j).getPosition()));
        }
        RvMyWidget.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        RvMyWidget.setAdapter(new MyWidgetAdapter(context, myWidgetLists, this));
        getIsNotFound();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.IvBack:
                onBackPressed();
                break;
            case R.id.IvDone:
                countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
                itemClick = SplashActivity.click++;
                if (itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(MyWidgetsActivity.this, new MyAppAd_Interstitial.MyCallback() {
                        @Override
                        public void callbackCall() {
                            System.out.println("_____  _ _ : " + MyAppConstants.WidgetRemove);
                            if (MyAppConstants.WidgetRemove) {
                                MyAppConstants.WidgetRemove = false;
                            } else {
                                MyAppConstants.WidgetRemove = true;
                            }
                            RvMyWidget.getAdapter().notifyDataSetChanged();
                        }
                    });
                } else {
                    System.out.println("_____  _ _ : " + MyAppConstants.WidgetRemove);
                    if (MyAppConstants.WidgetRemove) {
                        MyAppConstants.WidgetRemove = false;
                    } else {
                        MyAppConstants.WidgetRemove = true;
                    }
                    RvMyWidget.getAdapter().notifyDataSetChanged();
                }
                break;
            case R.id.IvMyWidgetTrendy:
                System.out.println("_____  _ _ : " + MyAppConstants.WidgetRemove);
                countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
                itemClick = SplashActivity.click++;
                if (itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(MyWidgetsActivity.this, new MyAppAd_Interstitial.MyCallback() {
                        @Override
                        public void callbackCall() {
                            MyAppConstants.WidgetRemove = false;
                            myWidgetLists = new ArrayList<>();
                            for (int j = 0; j < MyAppConstants.getTrendyWidgetLists().size(); j++) {
                                myWidgetLists.addAll(helper.getWidgetsType(MyAppConstants.getTrendyWidgetLists().get(j).getPosition()));
                            }
                            RvMyWidget.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                            RvMyWidget.setAdapter(new MyWidgetAdapter(context, myWidgetLists, MyWidgetsActivity.this));
                            getIsNotFound();
                        }
                    });
                } else {
                    MyAppConstants.WidgetRemove = false;
                    myWidgetLists = new ArrayList<>();
                    for (int j = 0; j < MyAppConstants.getTrendyWidgetLists().size(); j++) {
                        myWidgetLists.addAll(helper.getWidgetsType(MyAppConstants.getTrendyWidgetLists().get(j).getPosition()));
                    }
                    RvMyWidget.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                    RvMyWidget.setAdapter(new MyWidgetAdapter(context, myWidgetLists, MyWidgetsActivity.this));
                    getIsNotFound();
                }
                break;
            case R.id.IvMyWidgetCalendar:
                System.out.println("_____  _ _ : " + MyAppConstants.WidgetRemove);
                countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
                itemClick = SplashActivity.click++;
                if (itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(MyWidgetsActivity.this, new MyAppAd_Interstitial.MyCallback() {
                        @Override
                        public void callbackCall() {
                            MyAppConstants.WidgetRemove = false;
                            myWidgetLists = new ArrayList<>();
                            for (int j = 0; j < MyAppConstants.getCalendarWidgetLists().size(); j++) {
                                myWidgetLists.addAll(helper.getWidgetsType(MyAppConstants.getCalendarWidgetLists().get(j).getPosition()));
                            }
                            RvMyWidget.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                            RvMyWidget.setAdapter(new MyWidgetAdapter(context, myWidgetLists, MyWidgetsActivity.this));
                            getIsNotFound();
                        }
                    });
                } else {
                    MyAppConstants.WidgetRemove = false;
                    myWidgetLists = new ArrayList<>();
                    for (int j = 0; j < MyAppConstants.getCalendarWidgetLists().size(); j++) {
                        myWidgetLists.addAll(helper.getWidgetsType(MyAppConstants.getCalendarWidgetLists().get(j).getPosition()));
                    }
                    RvMyWidget.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                    RvMyWidget.setAdapter(new MyWidgetAdapter(context, myWidgetLists, MyWidgetsActivity.this));
                    getIsNotFound();
                }
                break;
            case R.id.IvMyWidgetWeather:
                System.out.println("_____  _ _ : " + MyAppConstants.WidgetRemove);
                countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
                itemClick = SplashActivity.click++;
                if (itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(MyWidgetsActivity.this, new MyAppAd_Interstitial.MyCallback() {
                        @Override
                        public void callbackCall() {
                            MyAppConstants.WidgetRemove = false;
                            myWidgetLists = new ArrayList<>();
                            for (int j = 0; j < MyAppConstants.getWeatherWidgetLists().size(); j++) {
                                myWidgetLists.addAll(helper.getWidgetsType(MyAppConstants.getWeatherWidgetLists().get(j).getPosition()));
                            }
                            RvMyWidget.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                            RvMyWidget.setAdapter(new MyWidgetAdapter(context, myWidgetLists, MyWidgetsActivity.this));
                            getIsNotFound();
                        }
                    });
                } else {
                    MyAppConstants.WidgetRemove = false;
                    myWidgetLists = new ArrayList<>();
                    for (int j = 0; j < MyAppConstants.getWeatherWidgetLists().size(); j++) {
                        myWidgetLists.addAll(helper.getWidgetsType(MyAppConstants.getWeatherWidgetLists().get(j).getPosition()));
                    }
                    RvMyWidget.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                    RvMyWidget.setAdapter(new MyWidgetAdapter(context, myWidgetLists, MyWidgetsActivity.this));
                    getIsNotFound();
                }
                break;
            case R.id.IvMyWidgetAlarm:
                System.out.println("_____  _ _ : " + MyAppConstants.WidgetRemove);
                countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
                itemClick = SplashActivity.click++;
                if (itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(MyWidgetsActivity.this, new MyAppAd_Interstitial.MyCallback() {
                        @Override
                        public void callbackCall() {
                            MyAppConstants.WidgetRemove = false;
                            myWidgetLists = new ArrayList<>();
                            for (int j = 0; j < MyAppConstants.getClockWidgetLists().size(); j++) {
                                myWidgetLists.addAll(helper.getWidgetsType(MyAppConstants.getClockWidgetLists().get(j).getPosition()));
                            }
                            RvMyWidget.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                            RvMyWidget.setAdapter(new MyWidgetAdapter(context, myWidgetLists, MyWidgetsActivity.this));
                            getIsNotFound();
                        }
                    });
                } else {
                    MyAppConstants.WidgetRemove = false;
                    myWidgetLists = new ArrayList<>();
                    for (int j = 0; j < MyAppConstants.getClockWidgetLists().size(); j++) {
                        myWidgetLists.addAll(helper.getWidgetsType(MyAppConstants.getClockWidgetLists().get(j).getPosition()));
                    }
                    RvMyWidget.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                    RvMyWidget.setAdapter(new MyWidgetAdapter(context, myWidgetLists, MyWidgetsActivity.this));
                    getIsNotFound();
                }
                break;
            case R.id.IvMyWidgetXPanel:
                System.out.println("_____  _ _ : " + MyAppConstants.WidgetRemove);
                countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
                itemClick = SplashActivity.click++;
                if (itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(MyWidgetsActivity.this, new MyAppAd_Interstitial.MyCallback() {
                        @Override
                        public void callbackCall() {
                            MyAppConstants.WidgetRemove = false;
                            myWidgetLists = new ArrayList<>();
                            for (int j = 0; j < MyAppConstants.getXPanelWidgetLists().size(); j++) {
                                myWidgetLists.addAll(helper.getWidgetsType(MyAppConstants.getXPanelWidgetLists().get(j).getPosition()));
                            }
                            RvMyWidget.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                            RvMyWidget.setAdapter(new MyWidgetAdapter(context, myWidgetLists, MyWidgetsActivity.this));
                            getIsNotFound();
                        }
                    });
                } else {
                    MyAppConstants.WidgetRemove = false;
                    myWidgetLists = new ArrayList<>();
                    for (int j = 0; j < MyAppConstants.getXPanelWidgetLists().size(); j++) {
                        myWidgetLists.addAll(helper.getWidgetsType(MyAppConstants.getXPanelWidgetLists().get(j).getPosition()));
                    }
                    RvMyWidget.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                    RvMyWidget.setAdapter(new MyWidgetAdapter(context, myWidgetLists, MyWidgetsActivity.this));
                    getIsNotFound();
                }
                break;
            case R.id.IvMyWidgetPhoto:
                countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
                itemClick = SplashActivity.click++;
                if (itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(MyWidgetsActivity.this, new MyAppAd_Interstitial.MyCallback() {
                        @Override
                        public void callbackCall() {
                            MyAppConstants.WidgetRemove = false;
                            myWidgetLists = new ArrayList<>();
                            for (int j = 0; j < MyAppConstants.getPhotoWidgetLists().size(); j++) {
                                System.out.println("_____  _ _ : " + MyAppConstants.getPhotoWidgetLists().get(j).getPosition());
                                myWidgetLists.addAll(helper.getWidgetsType(MyAppConstants.getPhotoWidgetLists().get(j).getPosition()));
                            }
                            System.out.println("_____  _ _ : " + myWidgetLists.size());
                            RvMyWidget.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                            RvMyWidget.setAdapter(new MyWidgetAdapter(context, myWidgetLists, MyWidgetsActivity.this));
                            getIsNotFound();
                        }
                    });
                } else {
                    MyAppConstants.WidgetRemove = false;
                    myWidgetLists = new ArrayList<>();
                    for (int j = 0; j < MyAppConstants.getPhotoWidgetLists().size(); j++) {
                        System.out.println("_____  _ _ : " + MyAppConstants.getPhotoWidgetLists().get(j).getPosition());
                        myWidgetLists.addAll(helper.getWidgetsType(MyAppConstants.getPhotoWidgetLists().get(j).getPosition()));
                    }
                    System.out.println("_____  _ _ : " + myWidgetLists.size());
                    RvMyWidget.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                    RvMyWidget.setAdapter(new MyWidgetAdapter(context, myWidgetLists, MyWidgetsActivity.this));
                    getIsNotFound();
                }

                break;
        }
    }

    private void getIsNotFound() {
        if (myWidgetLists.size() > 0) {
            IvNotFoundRecord.setVisibility(View.GONE);
            RvMyWidget.setVisibility(View.VISIBLE);
            iv_done.setVisibility(View.VISIBLE);
        } else {
            IvNotFoundRecord.setVisibility(View.VISIBLE);
            RvMyWidget.setVisibility(View.GONE);
            iv_done.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void DeleteWidget(int position) {
        myWidgetLists.remove(position);
        if (myWidgetLists.size() > 0) {
            IvNotFoundRecord.setVisibility(View.GONE);
            RvMyWidget.setVisibility(View.VISIBLE);
            iv_done.setVisibility(View.VISIBLE);
        } else {
            IvNotFoundRecord.setVisibility(View.VISIBLE);
            RvMyWidget.setVisibility(View.GONE);
            iv_done.setVisibility(View.GONE);
        }
        helper.getDeleteWidgets(myWidgetLists.get(position).getNumber());
        RvMyWidget.getAdapter().notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        int countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
        int itemClick = SplashActivity.click++;
        if (itemClick % countExtra == 0) {
            MyAppAd_Interstitial.getInstance().showInter(MyWidgetsActivity.this, new MyAppAd_Interstitial.MyCallback() {
                @Override
                public void callbackCall() {
                    finish();
                }
            });
        } else {
            finish();
        }
    }
}
package com.ios.widget.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
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
import com.ios.widget.crop.utils.MyAppConstants;
import com.ios.widget.crop.utils.MyAppPref;

import java.util.ArrayList;

public class MyWidgetsActivity extends AppCompatActivity implements View.OnClickListener, MyWidgetAdapter.deleteWidget {

    private Context context;
    private ImageView IvBack, IvNotFoundRecord, iv_done, IvMyWidgetTrendy, IvMyWidgetCalendar, IvMyWidgetWeather, IvMyWidgetAlarm, IvMyWidgetXPanel, IvMyWidgetPhoto;
    private TextView TvTitle;
    private DatabaseHelper helper;
    private RecyclerView RvMyWidget;
    private ArrayList<WidgetData> myWidgetLists = new ArrayList<WidgetData>();
    private int countExtra, itemClick;

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
        if (MyAppConstants.isConnectingToInternet(context)){
            MyAppAd_Banner.getInstance().showBanner(this, AdSize.LARGE_BANNER, (RelativeLayout) findViewById(R.id.RlBannerAdView), (RelativeLayout) findViewById(R.id.RlBannerAd));
        }
        IvMyWidgetTrendy.setVisibility(View.GONE);
        IvMyWidgetCalendar.setVisibility(View.GONE);
        IvMyWidgetWeather.setVisibility(View.GONE);
        IvMyWidgetAlarm.setVisibility(View.GONE);
        IvMyWidgetXPanel.setVisibility(View.GONE);
        IvMyWidgetPhoto.setVisibility(View.GONE);

        IvNotFoundRecord.setVisibility(View.GONE);
        RvMyWidget.setVisibility(View.VISIBLE);
        iv_done.setVisibility(View.VISIBLE);

        iv_done.setImageResource(R.drawable.ic_delete);
        helper = new DatabaseHelper(context);
        TvTitle.setText(getResources().getString(R.string.str_my_widget));
        myWidgetLists = new ArrayList<>();

        for (int i = 0; i < helper.getWidgetsTypeOrderBy().size(); i++) {
            if (helper.getWidgetsTypeOrderBy().get(i).getPosition() >= 0 && helper.getWidgetsTypeOrderBy().get(i).getPosition() <= 2) {
                IvMyWidgetTrendy.setVisibility(View.VISIBLE);

                if (myWidgetLists.size() == 0) {
                    for (int j = 0; j < MyAppConstants.getTrendyWidgetLists().size(); j++) {
                        myWidgetLists.addAll(helper.getWidgetsType(MyAppConstants.getTrendyWidgetLists().get(j).getPosition()));
                    }
                    IvMyWidgetTrendy.setImageResource(R.drawable.bttn_trendy_selected);
                }
                IvNotFoundRecord.setVisibility(View.GONE);
                RvMyWidget.setVisibility(View.VISIBLE);
                iv_done.setVisibility(View.VISIBLE);
            } else if (helper.getWidgetsTypeOrderBy().get(i).getPosition() >= 5 && helper.getWidgetsTypeOrderBy().get(i).getPosition() <= 7) {
                IvMyWidgetCalendar.setVisibility(View.VISIBLE);
                if (myWidgetLists.size() == 0) {
                    for (int j = 0; j < MyAppConstants.getCalendarWidgetLists().size(); j++) {
                        myWidgetLists.addAll(helper.getWidgetsType(MyAppConstants.getCalendarWidgetLists().get(j).getPosition()));
                    }
                    IvMyWidgetCalendar.setImageResource(R.drawable.bttn_calendar_selected);
                }
                IvNotFoundRecord.setVisibility(View.GONE);
                RvMyWidget.setVisibility(View.VISIBLE);
                iv_done.setVisibility(View.VISIBLE);
            } else if (helper.getWidgetsTypeOrderBy().get(i).getPosition() >= 8 && helper.getWidgetsTypeOrderBy().get(i).getPosition() <= 10) {
                IvMyWidgetWeather.setVisibility(View.VISIBLE);
                if (myWidgetLists.size() == 0) {
                    for (int j = 0; j < MyAppConstants.getWidgetLists().size(); j++) {
                        myWidgetLists.addAll(helper.getWidgetsType(MyAppConstants.getWidgetLists().get(j).getPosition()));
                    }
                    IvMyWidgetWeather.setImageResource(R.drawable.bttn_weather_selected);
                }
                IvNotFoundRecord.setVisibility(View.GONE);
                RvMyWidget.setVisibility(View.VISIBLE);
                iv_done.setVisibility(View.VISIBLE);
            } else if (helper.getWidgetsTypeOrderBy().get(i).getPosition() >= 11 && helper.getWidgetsTypeOrderBy().get(i).getPosition() <= 19) {
                IvMyWidgetAlarm.setVisibility(View.VISIBLE);
                if (myWidgetLists.size() == 0) {
                    for (int j = 0; j < MyAppConstants.getClockWidgetLists().size(); j++) {
                        myWidgetLists.addAll(helper.getWidgetsType(MyAppConstants.getClockWidgetLists().get(j).getPosition()));
                    }
                    IvMyWidgetAlarm.setImageResource(R.drawable.bttn_alarm_selected);
                }
                IvNotFoundRecord.setVisibility(View.GONE);
                RvMyWidget.setVisibility(View.VISIBLE);
                iv_done.setVisibility(View.VISIBLE);
            } else if (helper.getWidgetsTypeOrderBy().get(i).getPosition() >= 20 && helper.getWidgetsTypeOrderBy().get(i).getPosition() <= 22) {
                IvMyWidgetXPanel.setVisibility(View.VISIBLE);
                if (myWidgetLists.size() == 0) {
                    for (int j = 0; j < MyAppConstants.getXPanelWidgetLists().size(); j++) {
                        myWidgetLists.addAll(helper.getWidgetsType(MyAppConstants.getXPanelWidgetLists().get(j).getPosition()));
                    }
                    IvMyWidgetXPanel.setImageResource(R.drawable.bttn_x_panel_selected);
                }
                IvNotFoundRecord.setVisibility(View.GONE);
                RvMyWidget.setVisibility(View.VISIBLE);
                iv_done.setVisibility(View.VISIBLE);
            } else if (helper.getWidgetsTypeOrderBy().get(i).getPosition() >= 23) {
                IvMyWidgetPhoto.setVisibility(View.VISIBLE);
                if (myWidgetLists.size() == 0) {
                    for (int j = 0; j < MyAppConstants.getPhotoWidgetLists().size(); j++) {
                        myWidgetLists.addAll(helper.getWidgetsType(MyAppConstants.getPhotoWidgetLists().get(j).getPosition()));
                    }
                    IvMyWidgetPhoto.setImageResource(R.drawable.bttn_photo_selected);
                }
                IvNotFoundRecord.setVisibility(View.GONE);
                RvMyWidget.setVisibility(View.VISIBLE);
                iv_done.setVisibility(View.VISIBLE);
            } else {
                IvNotFoundRecord.setVisibility(View.VISIBLE);
                RvMyWidget.setVisibility(View.GONE);
                iv_done.setVisibility(View.GONE);
            }
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
                countExtra = new MyAppPref(context).getInt(MyAppPref.APP_AD_COUNTER, 0);
                itemClick = SplashActivity.click++;
                if (MyAppConstants.isConnectingToInternet(context)&&itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(MyWidgetsActivity.this, new MyAppAd_Interstitial.MyAppCallback() {
                        @Override
                        public void AppCallback() {
                            if (MyAppConstants.WidgetRemove) {
                                MyAppConstants.WidgetRemove = false;
                            } else {
                                MyAppConstants.WidgetRemove = true;
                            }
                            RvMyWidget.getAdapter().notifyDataSetChanged();
                        }
                    });
                } else {
                    if (MyAppConstants.WidgetRemove) {
                        MyAppConstants.WidgetRemove = false;
                    } else {
                        MyAppConstants.WidgetRemove = true;
                    }
                    RvMyWidget.getAdapter().notifyDataSetChanged();
                }
                break;
            case R.id.IvMyWidgetTrendy:
                countExtra = new MyAppPref(context).getInt(MyAppPref.APP_AD_COUNTER, 0);
                itemClick = SplashActivity.click++;
                if (MyAppConstants.isConnectingToInternet(context)&&itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(MyWidgetsActivity.this, new MyAppAd_Interstitial.MyAppCallback() {
                        @Override
                        public void AppCallback() {
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
                IvMyWidgetTrendy.setImageResource(R.drawable.bttn_trendy_selected);
                IvMyWidgetCalendar.setImageResource(R.drawable.bttn_calendar);
                IvMyWidgetWeather.setImageResource(R.drawable.bttn_weather);
                IvMyWidgetAlarm.setImageResource(R.drawable.bttn_alarm);
                IvMyWidgetXPanel.setImageResource(R.drawable.bttn_x_panel);
                IvMyWidgetPhoto.setImageResource(R.drawable.bttn_photo);
                break;
            case R.id.IvMyWidgetCalendar:
                countExtra = new MyAppPref(context).getInt(MyAppPref.APP_AD_COUNTER, 0);
                itemClick = SplashActivity.click++;
                if (MyAppConstants.isConnectingToInternet(context)&&itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(MyWidgetsActivity.this, new MyAppAd_Interstitial.MyAppCallback() {
                        @Override
                        public void AppCallback() {
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

                IvMyWidgetTrendy.setImageResource(R.drawable.bttn_trendy);
                IvMyWidgetCalendar.setImageResource(R.drawable.bttn_calendar_selected);
                IvMyWidgetWeather.setImageResource(R.drawable.bttn_weather);
                IvMyWidgetAlarm.setImageResource(R.drawable.bttn_alarm);
                IvMyWidgetXPanel.setImageResource(R.drawable.bttn_x_panel);
                IvMyWidgetPhoto.setImageResource(R.drawable.bttn_photo);
                break;
            case R.id.IvMyWidgetWeather:
                countExtra = new MyAppPref(context).getInt(MyAppPref.APP_AD_COUNTER, 0);
                itemClick = SplashActivity.click++;
                if (MyAppConstants.isConnectingToInternet(context)&&itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(MyWidgetsActivity.this, new MyAppAd_Interstitial.MyAppCallback() {
                        @Override
                        public void AppCallback() {
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
                IvMyWidgetTrendy.setImageResource(R.drawable.bttn_trendy);
                IvMyWidgetCalendar.setImageResource(R.drawable.bttn_calendar);
                IvMyWidgetWeather.setImageResource(R.drawable.bttn_weather_selected);
                IvMyWidgetAlarm.setImageResource(R.drawable.bttn_alarm);
                IvMyWidgetXPanel.setImageResource(R.drawable.bttn_x_panel);
                IvMyWidgetPhoto.setImageResource(R.drawable.bttn_photo);
                break;
            case R.id.IvMyWidgetAlarm:
                countExtra = new MyAppPref(context).getInt(MyAppPref.APP_AD_COUNTER, 0);
                itemClick = SplashActivity.click++;
                if (MyAppConstants.isConnectingToInternet(context)&&itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(MyWidgetsActivity.this, new MyAppAd_Interstitial.MyAppCallback() {
                        @Override
                        public void AppCallback() {
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

                IvMyWidgetTrendy.setImageResource(R.drawable.bttn_trendy);
                IvMyWidgetCalendar.setImageResource(R.drawable.bttn_calendar);
                IvMyWidgetWeather.setImageResource(R.drawable.bttn_weather);
                IvMyWidgetAlarm.setImageResource(R.drawable.bttn_alarm_selected);
                IvMyWidgetXPanel.setImageResource(R.drawable.bttn_x_panel);
                IvMyWidgetPhoto.setImageResource(R.drawable.bttn_photo);
                break;
            case R.id.IvMyWidgetXPanel:
                countExtra = new MyAppPref(context).getInt(MyAppPref.APP_AD_COUNTER, 0);
                itemClick = SplashActivity.click++;
                if (MyAppConstants.isConnectingToInternet(context)&&itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(MyWidgetsActivity.this, new MyAppAd_Interstitial.MyAppCallback() {
                        @Override
                        public void AppCallback() {
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

                IvMyWidgetTrendy.setImageResource(R.drawable.bttn_trendy);
                IvMyWidgetCalendar.setImageResource(R.drawable.bttn_calendar);
                IvMyWidgetWeather.setImageResource(R.drawable.bttn_weather);
                IvMyWidgetAlarm.setImageResource(R.drawable.bttn_alarm);
                IvMyWidgetXPanel.setImageResource(R.drawable.bttn_x_panel_selected);
                IvMyWidgetPhoto.setImageResource(R.drawable.bttn_photo);
                break;
            case R.id.IvMyWidgetPhoto:
                countExtra = new MyAppPref(context).getInt(MyAppPref.APP_AD_COUNTER, 0);
                itemClick = SplashActivity.click++;
                if (MyAppConstants.isConnectingToInternet(context)&&itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(MyWidgetsActivity.this, new MyAppAd_Interstitial.MyAppCallback() {
                        @Override
                        public void AppCallback() {
                            MyAppConstants.WidgetRemove = false;
                            myWidgetLists = new ArrayList<>();
                            for (int j = 0; j < MyAppConstants.getPhotoWidgetLists().size(); j++) {
                                myWidgetLists.addAll(helper.getWidgetsType(MyAppConstants.getPhotoWidgetLists().get(j).getPosition()));
                            }
                            RvMyWidget.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                            RvMyWidget.setAdapter(new MyWidgetAdapter(context, myWidgetLists, MyWidgetsActivity.this));
                            getIsNotFound();
                        }
                    });
                } else {
                    MyAppConstants.WidgetRemove = false;
                    myWidgetLists = new ArrayList<>();
                    for (int j = 0; j < MyAppConstants.getPhotoWidgetLists().size(); j++) {
                        myWidgetLists.addAll(helper.getWidgetsType(MyAppConstants.getPhotoWidgetLists().get(j).getPosition()));
                    }
                    RvMyWidget.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                    RvMyWidget.setAdapter(new MyWidgetAdapter(context, myWidgetLists, MyWidgetsActivity.this));
                    getIsNotFound();
                }

                IvMyWidgetTrendy.setImageResource(R.drawable.bttn_trendy);
                IvMyWidgetCalendar.setImageResource(R.drawable.bttn_calendar);
                IvMyWidgetWeather.setImageResource(R.drawable.bttn_weather);
                IvMyWidgetAlarm.setImageResource(R.drawable.bttn_alarm);
                IvMyWidgetXPanel.setImageResource(R.drawable.bttn_x_panel);
                IvMyWidgetPhoto.setImageResource(R.drawable.bttn_photo_selected);
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
    public void DeleteWidget(ArrayList<WidgetData> myWidgetLists, int position, int number) {
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
        helper.getDeleteWidgets(number);
        RvMyWidget.getAdapter().notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        int countExtra = new MyAppPref(context).getInt(MyAppPref.APP_AD_COUNTER, 0);
        int itemClick = SplashActivity.click++;
        if (MyAppConstants.isConnectingToInternet(context)&&itemClick % countExtra == 0) {
            MyAppAd_Interstitial.getInstance().showInter(MyWidgetsActivity.this, new MyAppAd_Interstitial.MyAppCallback() {
                @Override
                public void AppCallback() {
                    finish();
                }
            });
        } else {
            finish();
        }
    }
}
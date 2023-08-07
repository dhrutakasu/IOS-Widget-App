package com.ios.widget.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdSize;
import com.ios.widget.Ads.MyAppAd_Banner;
import com.ios.widget.Ads.MyAppAd_Interstitial;
import com.ios.widget.Model.WidgetModel;
import com.ios.widget.R;
import com.ios.widget.ui.Adapter.ItemAdapter;
import com.ios.widget.crop.utils.MyAppConstants;
import com.ios.widget.crop.utils.MyAppPref;

import java.util.ArrayList;

public class ShowItemActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ImageView IvBack;
    private TextView TvTitle;
    private RecyclerView RvItemList;
    private int TabPos;
    private ArrayList<WidgetModel> modelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);
        initViews();
        initIntents();
        iniListeners();
        initActions();
    }

    private void initViews() {
        context = this;
        IvBack = (ImageView) findViewById(R.id.IvBack);
        TvTitle = (TextView) findViewById(R.id.TvTitle);
        RvItemList = (RecyclerView) findViewById(R.id.RvItemList);
    }

    private void initIntents() {
        TabPos = getIntent().getIntExtra(MyAppConstants.TabPos, 0);
        if (TabPos == 0) {
            TvTitle.setText("Trendy");
            modelArrayList = MyAppConstants.getTrendyWidgetLists();
        } else if (TabPos == 1) {
            TvTitle.setText("Calendar");
            modelArrayList = MyAppConstants.getCalendarWidgetLists();
        } else if (TabPos == 2) {
            TvTitle.setText("Weather");
            modelArrayList = MyAppConstants.getWeatherWidgetLists();
        } else if (TabPos == 3) {
            TvTitle.setText("Clock");
            modelArrayList = MyAppConstants.getClockWidgetLists();
        } else if (TabPos == 4) {
            TvTitle.setText("X-Panel");
            modelArrayList = MyAppConstants.getXPanelWidgetLists();
        } else if (TabPos == 5) {
            TvTitle.setText("Photo");
            modelArrayList = MyAppConstants.getPhotoWidgetLists();
        }
    }

    private void iniListeners() {
        IvBack.setOnClickListener(this);
    }

    private void initActions() {
        if (MyAppConstants.isConnectingToInternet(context)) {
            MyAppAd_Banner.getInstance().showBanner(this, AdSize.LARGE_BANNER, (RelativeLayout) findViewById(R.id.RlBannerAdView), (RelativeLayout) findViewById(R.id.RlBannerAd));
        }
        RvItemList.setLayoutManager(new LinearLayoutManager(context));
        RvItemList.setAdapter(new ItemAdapter(context, modelArrayList,TabPos,ShowItemActivity.this));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.IvBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        int countExtra = new MyAppPref(context).getInt(MyAppPref.APP_AD_COUNTER, 0);
        int itemClick = SplashActivity.click++;
        if (MyAppConstants.isConnectingToInternet(context)&&itemClick % countExtra == 0) {
            MyAppAd_Interstitial.getInstance().showInter(ShowItemActivity.this, new MyAppAd_Interstitial.MyAppCallback() {
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
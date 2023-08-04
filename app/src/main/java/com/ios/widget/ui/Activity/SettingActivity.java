package com.ios.widget.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ios.widget.Ads.MyAppAd_Interstitial;
import com.ios.widget.Ads.MyAppAd_Native;
import com.ios.widget.R;
import com.ios.widget.utils.MyAppPref;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ImageView IvBack;
    private TextView TvTitle;
    private ConstraintLayout ConstRate,ConstShare,ConstPolicy,ConstAbout;
    private int countExtra,itemClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initViews();
        iniListeners();
        initActions();
    }

    private void initViews() {
        context=this;
        IvBack= (ImageView) findViewById(R.id.IvBack);
        TvTitle= (TextView) findViewById(R.id.TvTitle);
        ConstRate=(ConstraintLayout)findViewById(R.id.ConstRate);
        ConstShare=(ConstraintLayout)findViewById(R.id.ConstShare);
        ConstPolicy=(ConstraintLayout)findViewById(R.id.ConstPolicy);
        ConstAbout=(ConstraintLayout)findViewById(R.id.ConstAbout);
    }

    private void iniListeners() {
        IvBack.setOnClickListener(this);
        ConstRate.setOnClickListener(this);
        ConstShare.setOnClickListener(this);
        ConstPolicy.setOnClickListener(this);
        ConstAbout.setOnClickListener(this);
    }

    private void initActions() {
        MyAppAd_Native.getInstance().showNative250(this, findViewById(R.id.FlNativeAds));

        TvTitle.setText(getResources().getString(R.string.str_setting));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.IvBack:
                onBackPressed();
                break;
            case R.id.ConstRate:
                countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
                itemClick = SplashActivity.click++;
                if (itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(SettingActivity.this, new MyAppAd_Interstitial.MyCallback() {
                        @Override
                        public void callbackCall() {
                        }
                    });
                } else {
                }
                break;
            case R.id.ConstShare:
                countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
                itemClick = SplashActivity.click++;
                if (itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(SettingActivity.this, new MyAppAd_Interstitial.MyCallback() {
                        @Override
                        public void callbackCall() {
                        }
                    });
                } else {
                }
                break;
            case R.id.ConstPolicy:
                countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
                itemClick = SplashActivity.click++;
                if (itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(SettingActivity.this, new MyAppAd_Interstitial.MyCallback() {
                        @Override
                        public void callbackCall() {
                        }
                    });
                } else {
                }
                break;
            case R.id.ConstAbout:
                countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
                itemClick = SplashActivity.click++;
                if (itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(SettingActivity.this, new MyAppAd_Interstitial.MyCallback() {
                        @Override
                        public void callbackCall() {
                        }
                    });
                } else {
                }
                break;
        }
    }


    @Override
    public void onBackPressed() {
        int countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
        int itemClick = SplashActivity.click++;
        if (itemClick % countExtra == 0) {
            MyAppAd_Interstitial.getInstance().showInter(SettingActivity.this, new MyAppAd_Interstitial.MyCallback() {
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
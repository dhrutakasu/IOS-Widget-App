package com.ios.widget.ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdSize;
import com.google.android.material.tabs.TabLayout;
import com.ios.widget.Ads.MyAppAd_Banner;
import com.ios.widget.Ads.MyAppAd_Interstitial;
import com.ios.widget.R;
import com.ios.widget.ui.Adapter.WalkAdapter;
import com.ios.widget.utils.MyAppConstants;
import com.ios.widget.utils.MyAppPref;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class WalkthroughActivity extends AppCompatActivity {

    private ViewPager PagerWalk;
    private TextView TvWalkTitle, TvWalkSubTitle, TvWalkContinue;
    private Context context;

    private Integer[] images = {R.drawable.ic_bg_1, R.drawable.ic_bg_2, R.drawable.ic_bg_3};
    private TabLayout TabWalk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.GRAY);
        }
        setContentView(R.layout.activity_walkthrough);

        initViews();
        initListeners();
        initActions();
    }

    private void initViews() {
        context = this;
        PagerWalk = (ViewPager) findViewById(R.id.PagerWalk);
        TvWalkTitle = (TextView) findViewById(R.id.TvWalkTitle);
        TvWalkSubTitle = (TextView) findViewById(R.id.TvWalkSubTitle);
        TvWalkContinue = (TextView) findViewById(R.id.TvWalkContinue);
        TabWalk = (TabLayout) findViewById(R.id.TabWalk);
    }

    private void initListeners() {
        TvWalkContinue.setOnClickListener(view -> {
            int countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
            int itemClick = SplashActivity.click++;
            if (itemClick % countExtra == 0) {
                MyAppAd_Interstitial.getInstance().showInter(WalkthroughActivity.this, new MyAppAd_Interstitial.MyCallback() {
                    @Override
                    public void callbackCall() {
                        if (PagerWalk.getCurrentItem() == 0) {
                            PagerWalk.setCurrentItem(1);
                        } else if (PagerWalk.getCurrentItem() == 1) {
                            PagerWalk.setCurrentItem(2);
                        } else {
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            } else {
                if (PagerWalk.getCurrentItem() == 0) {
                    PagerWalk.setCurrentItem(1);
                } else if (PagerWalk.getCurrentItem() == 1) {
                    PagerWalk.setCurrentItem(2);
                } else {
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        });
    }

    private void initActions() {
        AudienceNetworkAds.initialize(context);
        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = MyAppConstants.md5(android_id).toUpperCase();
        AdSettings.addTestDevice(deviceId);
        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(context, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID", com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        ((RelativeLayout) findViewById(R.id.RlBannerAd)).addView(adView);
        com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                System.out.println("********* Error : ");
            }

            @Override
            public void onAdLoaded(Ad ad) {
                System.out.println("********* Loaded : ");
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };
        com.facebook.ads.AdView.AdViewLoadConfig loadAdConfig = adView.buildLoadAdConfig()
                .withAdListener(adListener)
                .build();
        adView.loadAd(loadAdConfig);
//        adView.loadAd();
//        MyAppAd_Banner.getInstance().showBanner(this, AdSize.LARGE_BANNER, (RelativeLayout) findViewById(R.id.RlBannerAdView), (RelativeLayout) findViewById(R.id.RlBannerAd));

        WalkAdapter adapter = new WalkAdapter(this, images);
        PagerWalk.setAdapter(adapter);
        TvWalkTitle.setText(getResources().getString(R.string.str_walk_title_1));
        TvWalkSubTitle.setText(getResources().getString(R.string.str_walk_sub_title_1));
        TabWalk.setupWithViewPager(PagerWalk, true);

        PagerWalk.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                changeText(i);
            }

            @Override
            public void onPageSelected(int i) {
                changeText(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void changeText(int i) {
        if (i == 0) {
            TvWalkTitle.setText(getResources().getString(R.string.str_walk_title_1));
            TvWalkSubTitle.setText(getResources().getString(R.string.str_walk_sub_title_1));
            TvWalkContinue.setText("Next");
        } else if (i == 1) {
            TvWalkTitle.setText(getResources().getString(R.string.str_walk_title_2));
            TvWalkSubTitle.setText(getResources().getString(R.string.str_walk_sub_title_2));
            TvWalkContinue.setText("Next");
        } else if (i == 2) {
            TvWalkTitle.setText(getResources().getString(R.string.str_walk_title_3));
            TvWalkSubTitle.setText(getResources().getString(R.string.str_walk_sub_title_3));
            TvWalkContinue.setText("Start Training");
        }
    }


    @Override
    public void onBackPressed() {
        int countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
        int itemClick = SplashActivity.click++;
        if (itemClick % countExtra == 0) {
            MyAppAd_Interstitial.getInstance().showInter(WalkthroughActivity.this, new MyAppAd_Interstitial.MyCallback() {
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
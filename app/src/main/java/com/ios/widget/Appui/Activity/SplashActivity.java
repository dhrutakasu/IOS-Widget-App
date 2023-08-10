package com.ios.widget.Appui.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.ios.widget.AppAdv.MyAppAd_Interstitial;
import com.ios.widget.AppAdv.MyAppAd_Native;
import com.ios.widget.AppAdv.MyAppSingleJsonPass;
import com.ios.widget.R;
import com.ios.widget.AppUtils.MyAppConstants;
import com.ios.widget.AppUtils.MyAppPref;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    public static int click = 1;
    private Context context;
    private String url2 = "https://7starinnovation.com/api/demoapi.json";
    String AdShow, appid, app_open, banner_ad_unit_id, interstitial_full_screen = "", native_id;
    int AdCount;
    private static final String LOG_TAG = "AppOpenAdManager";
    private long secondsRemaining;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private AppOpenAd appOpenAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        setContentView(R.layout.activity_splash);
        context = this;
        createHandler(3);
    }

    public void fetchAd() {
        loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                appOpenAd = null;
                startMainActivity();
            }

            @Override
            public void onAdLoaded(@NonNull AppOpenAd appOpenAds) {
                appOpenAd = appOpenAds;
                if (appOpenAd != null) {
                    appOpenAd.show(SplashActivity.this);
                    appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            startMainActivity();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            appOpenAd = null;
                            startMainActivity();
                        }

                        @Override
                        public void onAdImpression() {
                            super.onAdImpression();
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent();
                        }
                    });
                }
            }
        };
        AdRequest request = new AdRequest.Builder().build();
        AppOpenAd.load(this, new MyAppPref(context).getString(MyAppPref.APP_AD_OPEN, ""), request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }

    private void createHandler(long seconds) {
        if (MyAppConstants.isConnectingToInternet(context)) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url2,
                    response2 -> {
                        try {
                            JSONObject response = new JSONObject(response2);

                            app_open = response.getString("AppOpenAd");
                            banner_ad_unit_id = response.getString("BannerAd");
                            interstitial_full_screen = response.getString("InterstitialAd");
                            native_id = response.getString("NativaAds");
                            AdShow = response.getString("AdShow");
                            AdCount = response.getInt("Counter");

                            new MyAppPref(SplashActivity.this).putString(MyAppPref.APP_SHOW, AdShow);
                            new MyAppPref(SplashActivity.this).putString(MyAppPref.APP_AD_BANNER, banner_ad_unit_id);
                            new MyAppPref(SplashActivity.this).putString(MyAppPref.APP_AD_INTER, interstitial_full_screen);
                            new MyAppPref(SplashActivity.this).putString(MyAppPref.APP_AD_NATIVE, native_id);
                            new MyAppPref(SplashActivity.this).putString(MyAppPref.APP_AD_OPEN, app_open);
                            new MyAppPref(SplashActivity.this).putInt(MyAppPref.APP_AD_COUNTER, AdCount);
                            String show = new MyAppPref(context).getString(MyAppPref.APP_SHOW, "no");


//                            MyAppOpenAdManager appOpenAdManager = new MyAppOpenAdManager(getApplication());
//                            MyAppOpenAdManager appOpenAdManager = new MyAppOpenAdManager(SplashActivity.this);
//                            appOpenAdManager.loadAd(SplashActivity.this);
                            MyAppAd_Native.getInstance().LoadNative(SplashActivity.this);
                            MyAppAd_Interstitial.getInstance().loadInterOne(SplashActivity.this);

                            if (show.equalsIgnoreCase("yes")) {
                                fetchAd();
                            } else {
                                CountDownTimer countDownTimer = new CountDownTimer(seconds * 1000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        secondsRemaining = ((millisUntilFinished / 1000) + 1);
                                    }

                                    @Override
                                    public void onFinish() {
                                        secondsRemaining = 0;
//                                            Ad_Interstitial.getInstance().showInter(SplashActivity.this, new Ad_Interstitial.MyCallback() {
//                                        @Override
//                                        public void callbackCall() {
//                                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                                            finish();
//                                        }
//                                    });

                                        startMainActivity();
                                    }
                                };
                                countDownTimer.start();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        },
                    error -> {
                    }) {
            };
            MyAppSingleJsonPass.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        } else {
            Toast.makeText(context, "Please turn on your internet connection...", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void startMainActivity() {
        if (!new MyAppPref(SplashActivity.this).getBoolean(MyAppPref.APP_WALKTHROUGH, false)) {
            startActivity(new Intent(this, WalkthroughActivity.class));
            new MyAppPref(SplashActivity.this).putBoolean(MyAppPref.APP_WALKTHROUGH, true);
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }
}
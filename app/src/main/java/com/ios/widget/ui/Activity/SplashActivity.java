package com.ios.widget.ui.Activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.ios.widget.Ads.MyAppAd_Interstitial;
import com.ios.widget.Ads.MyAppAd_Native;
import com.ios.widget.Ads.MyAppOpenAdManager;
import com.ios.widget.Ads.MyAppSingleJsonPass;
import com.ios.widget.Application.MyApp;
import com.ios.widget.R;
import com.ios.widget.utils.MyAppConstants;
import com.ios.widget.utils.MyAppPref;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    public static int click=1;
    private Context context;
    private String url2 = "https://7starinnovation.com/api/demoapi.json";
    String AdShow, appid, app_open, banner_ad_unit_id, interstitial_full_screen = "", native_id;
    int AdCount;
    private static final String LOG_TAG = "AppOpenAdManager";
    private long secondsRemaining;

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

                            Log.d("Manish", "onResponse is: " + banner_ad_unit_id);


                            new MyAppPref(SplashActivity.this).putString(MyAppPref.SHOW, AdShow);
                            new MyAppPref(SplashActivity.this).putString(MyAppPref.AD_BANNER, banner_ad_unit_id);
                            new MyAppPref(SplashActivity.this).putString(MyAppPref.AD_INTER, interstitial_full_screen);
                            new MyAppPref(SplashActivity.this).putString(MyAppPref.AD_NATIVE, native_id);
                            new MyAppPref(SplashActivity.this).putString(MyAppPref.AD_OPEN, app_open);
                            new MyAppPref(SplashActivity.this).putInt(MyAppPref.AD_COUNTER, AdCount);

                            MyAppOpenAdManager appOpenAdManager = new MyAppOpenAdManager(SplashActivity.this);
//                            appOpenAdManager.loadAd(SplashActivity.this);
                            MyAppAd_Native.getInstance().LoadNative(SplashActivity.this);
                            MyAppAd_Interstitial.getInstance().loadInterOne(SplashActivity.this);
                            try {
                                ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                                Bundle bundle = ai.metaData;
                                String myApiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID");
                                ai.metaData.putString("com.google.android.gms.ads.APPLICATION_ID", app_open);//you can replace your key APPLICATION_ID here
                                String ApiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID");
                            } catch (PackageManager.NameNotFoundException e) {
                            } catch (NullPointerException e) {
                            }
                            CountDownTimer countDownTimer = new CountDownTimer(seconds * 1000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    secondsRemaining = ((millisUntilFinished / 1000) + 1);
                                }

                                @Override
                                public void onFinish() {
                                    secondsRemaining = 0;
                                    Application application = getApplication();

//                                            if (!(application instanceof App)) {
//                                                Log.e(LOG_TAG, "Failed to cast application to MyApplication.");
//                                                startMainActivity();
//                                                return;
//                                            }
//                                            Ad_Interstitial.getInstance().showInter(SplashActivity.this, new Ad_Interstitial.MyCallback() {
//                                        @Override
//                                        public void callbackCall() {
//                                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                                            finish();
//                                        }
//                                    });
                                    ((MyApp) application)
                                            .showAdIfAvailable(
                                                    SplashActivity.this,
                                                    new MyApp.OnShowAdCompleteListener() {
                                                        @Override
                                                        public void onShowAdComplete() {
                                                            Log.d(LOG_TAG, "onShowAdComplete.");
                                                            startMainActivity();
                                                        }
                                                    });
                                }
                            };
                            countDownTimer.start();

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
        }
    }

    public void startMainActivity() {
        startActivity(new Intent(this, WalkthroughActivity.class));
        finish();
    }
}
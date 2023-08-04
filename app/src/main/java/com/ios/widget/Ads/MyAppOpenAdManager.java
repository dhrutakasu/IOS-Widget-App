package com.ios.widget.Ads;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.ios.widget.Application.MyApp;
import com.ios.widget.utils.MyAppPref;

import java.util.Date;

import androidx.lifecycle.LifecycleObserver;

public class MyAppOpenAdManager implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    private static final String LOG_TAG = "AppOpenAdManager";
    private final String AD_UNIT_ID;
    private AppOpenAd appOpenAd = null;
    private boolean isLoadingAd = false;
    public boolean isShowingAd = false;
    private long loadTime = 0;
    MyApp myApplication;

    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }

    public MyAppOpenAdManager(Context context) {
        String string = new MyAppPref(context).getString(MyAppPref.AD_OPEN, "");
        this.AD_UNIT_ID = string;
        MyAppPref.openads = string;
        loadAd(context);
    }

    public MyAppOpenAdManager(MyApp myApplicationMyAppOpen, Context context) {
        this.AD_UNIT_ID = new MyAppPref(context).getString(MyAppPref.AD_OPEN, "");
        this.myApplication = myApplicationMyAppOpen;
        myApplicationMyAppOpen.registerActivityLifecycleCallbacks(this);
        loadAd(context);
    }

    public void loadAd(Context context) {
        if (!this.isLoadingAd && !isAdAvailable()) {
            this.isLoadingAd = true;
            AppOpenAd.load(context, this.AD_UNIT_ID, new AdRequest.Builder().build(), 1, new AppOpenAd.AppOpenAdLoadCallback() {

                public void onAdLoaded(AppOpenAd appOpenAd) {
                    MyAppOpenAdManager.this.appOpenAd = appOpenAd;
                    MyAppOpenAdManager.this.isLoadingAd = false;
                    MyAppOpenAdManager.this.loadTime = new Date().getTime();
                }

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    MyAppOpenAdManager.this.isLoadingAd = false;
                }
            });
        }
    }


    private boolean wasLoadTimeLessThanNHoursAgo(long j) {
        return new Date().getTime() - this.loadTime < j * 3600000;
    }


    private boolean isAdAvailable() {
        return this.appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }


}

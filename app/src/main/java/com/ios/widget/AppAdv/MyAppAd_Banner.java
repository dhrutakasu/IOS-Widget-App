package com.ios.widget.AppAdv;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.ios.widget.AppUtils.MyAppPref;

public class MyAppAd_Banner {

    private static MyAppAd_Banner mInstance;

    public static MyAppAd_Banner getInstance() {
        if (mInstance == null) {
            mInstance = new MyAppAd_Banner();
        }
        return mInstance;
    }

    public void showBanner(Activity activity, AdSize adSize, RelativeLayout RlBannerView, RelativeLayout RlBannerParent) {
        String Bannerstring = new MyAppPref(activity).getString(MyAppPref.APP_AD_BANNER, "");
        AdView view = new AdView(activity);
        view.setAdSize(adSize);
        view.setAdUnitId(Bannerstring);
        view.loadAd(new AdRequest.Builder().build());
        view.setAdListener(new AdListener() {

            @Override
            public void onAdClicked() {
            }

            @Override
            public void onAdClosed() {
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                RlBannerView.setVisibility(View.INVISIBLE);
            }

            @Override

            public void onAdLoaded() {
            }

            @Override
            public void onAdOpened() {
            }
        });
        RlBannerView.addView(view);
        String show = new MyAppPref(activity).getString(MyAppPref.APP_SHOW, "no");

        if (show.equalsIgnoreCase("yes")) {
            RlBannerParent.setVisibility(View.VISIBLE);
        } else {
            RlBannerParent.setVisibility(View.INVISIBLE);
        }
    }

    private AdSize getAdSize(Activity activity) {
        Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, (int) (((float) displayMetrics.widthPixels) / displayMetrics.density));
    }
}

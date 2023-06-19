package com.ios.widget.Ads;

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
import com.ios.widget.utils.Pref;

public class Ad_Banner {

    private static Ad_Banner mInstance;

    public static Ad_Banner getInstance() {
        if (mInstance == null) {
            mInstance = new Ad_Banner();
        }
        return mInstance;
    }

    public void showBanner(Activity activity, AdSize adSize, RelativeLayout relativeLayout, RelativeLayout bannerlay) {
        String string = new Pref(activity).getString(Pref.AD_BANNER, "");
        AdView adView = new AdView(activity);
        adView.setAdSize(adSize);
        adView.setAdUnitId(string);
        adView.loadAd(new AdRequest.Builder().build());
        adView.setAdListener(new AdListener() {

            @Override
            public void onAdClicked() {
            }

            @Override
            public void onAdClosed() {
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
            }

            @Override

            public void onAdLoaded() {
            }

            @Override
            public void onAdOpened() {
            }
        });
        relativeLayout.addView(adView);
        String string2 = new Pref(activity).getString(Pref.SHOW, "no");

        if (string2.equalsIgnoreCase("yes")) {
            bannerlay.setVisibility(View.VISIBLE);
        } else {
            bannerlay.setVisibility(View.GONE);
        }
    }

    private AdSize getAdSize(Activity activity) {
        Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, (int) (((float) displayMetrics.widthPixels) / displayMetrics.density));
    }
}

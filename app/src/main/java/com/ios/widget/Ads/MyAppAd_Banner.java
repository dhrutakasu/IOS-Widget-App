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
import com.ios.widget.utils.MyAppPref;

public class MyAppAd_Banner {

    private static MyAppAd_Banner mInstance;

    public static MyAppAd_Banner getInstance() {
        if (mInstance == null) {
            mInstance = new MyAppAd_Banner();
        }
        return mInstance;
    }

    public void showBanner(Activity activity, AdSize adSize, RelativeLayout relativeLayout, RelativeLayout bannerlay) {
        String string = new MyAppPref(activity).getString(MyAppPref.AD_BANNER, "");
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
                System.out.println("********* LoadAdError : ");


                relativeLayout.setVisibility(View.GONE);
//                AudienceNetworkAds.initialize(activity);
//                com.facebook.ads.AdView adView = new com.facebook.ads.AdView(activity, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID", com.facebook.ads.AdSize.BANNER_HEIGHT_50);
//                bannerlay.addView(adView);
//                com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
//                    @Override
//                    public void onError(Ad ad, AdError adError) {
//                        System.out.println("********* Error : ");
//                    }
//
//                    @Override
//                    public void onAdLoaded(Ad ad) {
//                        System.out.println("********* Loaded : ");
//                    }
//
//                    @Override
//                    public void onAdClicked(Ad ad) {
//                    }
//
//                    @Override
//                    public void onLoggingImpression(Ad ad) {
//                    }
//                };
//                com.facebook.ads.AdView.AdViewLoadConfig loadAdConfig = adView.buildLoadAdConfig()
//                        .withAdListener(adListener)
//                        .build();
//                adView.loadAd(loadAdConfig);
            }

            @Override

            public void onAdLoaded() {
                System.out.println("********* onAdLoaded : ");
            }

            @Override
            public void onAdOpened() {
            }
        });
        relativeLayout.addView(adView);
        String string2 = new MyAppPref(activity).getString(MyAppPref.SHOW, "no");

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

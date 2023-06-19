package com.ios.widget.Ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.ios.widget.R;
import com.ios.widget.utils.Pref;

public class Ad_Native {
    private static Ad_Native mInstance;
    private NativeAd nativeAd;

    public static Ad_Native getInstance() {
        if (mInstance == null) {
            mInstance = new Ad_Native();
        }
        return mInstance;
    }

    public void LoadNative(final Activity activity) {

        AdLoader.Builder builder = new AdLoader.Builder(activity, new Pref(activity).getString(Pref.AD_NATIVE, ""));
        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {

            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
//                if ((Build.VERSION.SDK_INT >= 17 ? activity.isDestroyed() : false) || activity.isFinishing() || activity.isChangingConfigurations()) {
//                    nativeAd.destroy();
//                    return;
//                }
//                if (Ad_Native.this.nativeAd != null) {
//                    Ad_Native.this.nativeAd.destroy();
//                }
                Ad_Native.this.nativeAd = nativeAd;
            }
        });
        builder.withNativeAdOptions(new NativeAdOptions.Builder().setVideoOptions(new VideoOptions.Builder().setStartMuted(true).build()).build());
        builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
            }
        }).build().loadAd(new AdRequest.Builder().build());
    }

    public void showNativeMedium(Activity activity, FrameLayout frameLayout) {
        if (this.nativeAd != null) {
            NativeAdView nativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.item_ad_unified, (ViewGroup) null);
            populateUnifiedNativeAdView(this.nativeAd, nativeAdView);
            frameLayout.removeAllViews();
            frameLayout.addView(nativeAdView);
        }
        LoadNative(activity);
    }

    public void showNativeBig(Activity activity, FrameLayout frameLayout) {
        if (this.nativeAd != null) {
            NativeAdView nativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.item_ad_unified, (ViewGroup) null);
            populateUnifiedNativeAdView(this.nativeAd, nativeAdView);
            frameLayout.removeAllViews();
            frameLayout.addView(nativeAdView);

        }
        LoadNative(activity);
    }
    public void showNative250(Activity activity, FrameLayout frameLayout) {
        if (this.nativeAd != null) {
            NativeAdView nativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.item_big_native_layout, (ViewGroup) null);
            populateUnifiedNativeAdView(this.nativeAd, nativeAdView);
            frameLayout.removeAllViews();
            String string2 = new Pref(activity).getString(Pref.SHOW, "no");

            if (string2.equalsIgnoreCase("yes") ) {
                frameLayout.setVisibility(View.VISIBLE);
                frameLayout.addView(nativeAdView);
            } else {
                frameLayout.setVisibility(View.GONE);
            }

        }
        LoadNative(activity);
    }

    public void showNative55(Activity activity, FrameLayout frameLayout) {
        if (this.nativeAd != null) {
            NativeAdView nativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.item_ad_unified, (ViewGroup) null);
            populateUnifiedNativeAdView100(this.nativeAd, nativeAdView, activity);
            frameLayout.removeAllViews();
            frameLayout.addView(nativeAdView);
        }
        LoadNative(activity);
    }

    @SuppressLint("WrongConstant")
    private void populateUnifiedNativeAdView(NativeAd nativeAd2, NativeAdView nativeAdView) {
        nativeAdView.setMediaView((MediaView) nativeAdView.findViewById(R.id.ad_media));
        nativeAdView.setHeadlineView(nativeAdView.findViewById(R.id.ad_headline));
        nativeAdView.setBodyView(nativeAdView.findViewById(R.id.ad_body));
        Button button = nativeAdView.findViewById(R.id.ad_call_to_action);
        nativeAdView.setCallToActionView(button);
        nativeAdView.setIconView(nativeAdView.findViewById(R.id.ad_icon));
        nativeAdView.setPriceView(nativeAdView.findViewById(R.id.ad_price));
        nativeAdView.setStarRatingView(nativeAdView.findViewById(R.id.ad_stars));
        nativeAdView.setStoreView(nativeAdView.findViewById(R.id.ad_store));
        nativeAdView.setAdvertiserView(nativeAdView.findViewById(R.id.ad_advertiser));
        ((TextView) nativeAdView.getHeadlineView()).setText(nativeAd2.getHeadline());
        if (nativeAd2.getBody() == null) {
            nativeAdView.getBodyView().setVisibility(4);
        } else {
            nativeAdView.getBodyView().setVisibility(0);
            ((TextView) nativeAdView.getBodyView()).setText(nativeAd2.getBody());
        }
        if (nativeAd2.getCallToAction() == null) {
            nativeAdView.getCallToActionView().setVisibility(4);
        } else {
            nativeAdView.getCallToActionView().setVisibility(0);
            ((Button) nativeAdView.getCallToActionView()).setText(nativeAd2.getCallToAction());
        }
        if (nativeAd2.getIcon() == null) {
            nativeAdView.getIconView().setVisibility(8);
        } else {
            ((ImageView) nativeAdView.getIconView()).setImageDrawable(nativeAd2.getIcon().getDrawable());
            nativeAdView.getIconView().setVisibility(0);
        }
        if (nativeAd2.getPrice() == null) {
            nativeAdView.getPriceView().setVisibility(4);
        } else {
            nativeAdView.getPriceView().setVisibility(0);
            ((TextView) nativeAdView.getPriceView()).setText(nativeAd2.getPrice());
        }
        if (nativeAd2.getStore() == null) {
            nativeAdView.getStoreView().setVisibility(4);
        } else {
            nativeAdView.getStoreView().setVisibility(0);
            ((TextView) nativeAdView.getStoreView()).setText(nativeAd2.getStore());
        }
        if (nativeAd2.getStarRating() == null) {
            nativeAdView.getStarRatingView().setVisibility(4);
        } else {
            ((RatingBar) nativeAdView.getStarRatingView()).setRating(nativeAd2.getStarRating().floatValue());
            nativeAdView.getStarRatingView().setVisibility(0);
        }
        if (nativeAd2.getAdvertiser() == null) {
            nativeAdView.getAdvertiserView().setVisibility(4);
        } else {
            ((TextView) nativeAdView.getAdvertiserView()).setText(nativeAd2.getAdvertiser());
            nativeAdView.getAdvertiserView().setVisibility(0);
        }
        nativeAdView.setNativeAd(nativeAd2);
    }

    @SuppressLint("WrongConstant")
    private void populateUnifiedNativeAdView100(NativeAd nativeAd2, NativeAdView nativeAdView, Activity activity) {
        nativeAdView.setHeadlineView(nativeAdView.findViewById(R.id.ad_headline));
        nativeAdView.setBodyView(nativeAdView.findViewById(R.id.ad_body));
        Button button = nativeAdView.findViewById(R.id.ad_call_to_action);
        nativeAdView.setCallToActionView(button);
        nativeAdView.setIconView(nativeAdView.findViewById(R.id.ad_icon));
        nativeAdView.setStarRatingView(nativeAdView.findViewById(R.id.ad_stars));
        ((TextView) nativeAdView.getHeadlineView()).setText(nativeAd2.getHeadline());
        if (nativeAd2.getBody() == null) {
            nativeAdView.getBodyView().setVisibility(4);
        } else {
            nativeAdView.getBodyView().setVisibility(0);
            ((TextView) nativeAdView.getBodyView()).setText(nativeAd2.getBody());
        }
        if (nativeAd2.getCallToAction() == null) {
            nativeAdView.getCallToActionView().setVisibility(4);
        } else {
            nativeAdView.getCallToActionView().setVisibility(0);
            ((Button) nativeAdView.getCallToActionView()).setText(nativeAd2.getCallToAction());
        }
        if (nativeAd2.getIcon() == null) {
            nativeAdView.getIconView().setVisibility(8);
        } else {
            ((ImageView) nativeAdView.getIconView()).setImageDrawable(nativeAd2.getIcon().getDrawable());
            nativeAdView.getIconView().setVisibility(0);
        }
        if (nativeAd2.getStarRating() == null) {
            nativeAdView.getStarRatingView().setVisibility(4);
        } else {
            ((RatingBar) nativeAdView.getStarRatingView()).setRating(nativeAd2.getStarRating().floatValue());
            nativeAdView.getStarRatingView().setVisibility(0);
        }
        nativeAdView.setNativeAd(nativeAd2);
    }
}

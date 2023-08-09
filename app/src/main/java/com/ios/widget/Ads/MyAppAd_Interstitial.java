package com.ios.widget.Ads;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.ios.widget.utils.MyAppPref;

public class MyAppAd_Interstitial {
    static int OnClick = 1;
    private static MyAppAd_Interstitial mInstance;
    public InterstitialAd interstitial;
    MyAppCallback myAppCallback;

    public interface MyAppCallback {
        void AppCallback();
    }

    public static MyAppAd_Interstitial getInstance() {
        if (mInstance == null) {
            mInstance = new MyAppAd_Interstitial();
        }
        return mInstance;
    }

    public void loadInterOne(final Activity activity) {
        MobileAds.initialize(activity, new OnInitializationCompleteListener() {

            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        InterstitialAd.load(activity, new MyAppPref(activity).getString(MyAppPref.APP_AD_INTER, ""), new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {

            public void onAdLoaded(InterstitialAd interstitialAd) {
                MyAppAd_Interstitial.this.interstitial = interstitialAd;
                MyAppAd_Interstitial.this.interstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        MyAppAd_Interstitial.this.interstitial = null;
                        MyAppAd_Interstitial.this.loadInterOne(activity);
                        if (MyAppAd_Interstitial.this.myAppCallback != null) {
                            MyAppAd_Interstitial.this.myAppCallback.AppCallback();
                            MyAppAd_Interstitial.this.myAppCallback = null;
                        }
                    }
                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {

                        MyAppAd_Interstitial.this.interstitial = null;
                        MyAppAd_Interstitial.this.loadInterOne(activity);
                    }
                });
            }
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                MyAppAd_Interstitial.this.interstitial = null;
            }
        });
    }



    public void showInter(Activity activity, MyAppCallback myAppCallback2) {
        this.myAppCallback = myAppCallback2;
        int integer =new MyAppPref(activity).getInt(MyAppPref.APP_CLICK, 1);
        int i = OnClick;

        String string2 = new MyAppPref(activity).getString(MyAppPref.APP_SHOW, "no");

        if (string2.equalsIgnoreCase("yes")) {
            if (i == integer) {
                OnClick = 1;
                InterstitialAd interstitialAd = this.interstitial;
                if (interstitialAd != null) {
                    interstitialAd.show(activity);
                    return;
                }

                MyAppCallback myAppCallback3 = this.myAppCallback;
                if (myAppCallback3 != null) {
                    myAppCallback3.AppCallback();
                    this.myAppCallback = null;
                    return;
                }
                return;
            }
            OnClick = i + 1;
        } else {
            MyAppCallback myAppCallback4 = this.myAppCallback;
            if (myAppCallback4 != null) {
                myAppCallback4.AppCallback();
                this.myAppCallback = null;
            }
            return;
        }
    }

    public void showInterBack(Activity activity, MyAppCallback myAppCallback2) {
        this.myAppCallback = myAppCallback2;
        int integer = new MyAppPref(activity).getInt(MyAppPref.APP_AD_BACK, 0);
        int integer2 = new MyAppPref(activity).getInt(MyAppPref.APP_CLICK, 1);
        if (integer == 0) {
            int i = OnClick;
            if (i == integer2) {
                OnClick = 1;
                InterstitialAd interstitialAd = this.interstitial;
                if (interstitialAd != null) {
                    interstitialAd.show(activity);
                    return;
                }
                MyAppCallback myAppCallback3 = this.myAppCallback;
                if (myAppCallback3 != null) {
                    myAppCallback3.AppCallback();
                    this.myAppCallback = null;
                    return;
                }
                return;
            }
            OnClick = i + 1;
            MyAppCallback myAppCallback4 = this.myAppCallback;
            if (myAppCallback4 != null) {
                myAppCallback4.AppCallback();
                this.myAppCallback = null;
                return;
            }
            return;
        }
        MyAppCallback myAppCallback5 = this.myAppCallback;
        if (myAppCallback5 != null) {
            myAppCallback5.AppCallback();
            this.myAppCallback = null;
        }
    }

    public boolean isInternetOn(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING || connectivityManager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING || connectivityManager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        if (connectivityManager.getNetworkInfo(0).getState() != NetworkInfo.State.DISCONNECTED) {
            connectivityManager.getNetworkInfo(1).getState();
            NetworkInfo.State state = NetworkInfo.State.DISCONNECTED;
        }
        return false;
    }

    public static void alert(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Internet Alert");
        builder.setMessage("You need to internet connection");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }
}

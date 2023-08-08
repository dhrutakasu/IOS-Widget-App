package com.ios.widget.ui.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdSize;
import com.ios.widget.Ads.MyAppAd_Banner;
import com.ios.widget.Ads.MyAppAd_Interstitial;
import com.ios.widget.R;
import com.ios.widget.crop.utils.MyAppConstants;
import com.ios.widget.crop.utils.MyAppPref;
import com.ios.widget.cropiwa.AspectRatio;
import com.ios.widget.cropiwa.CropIwaView;
import com.ios.widget.cropiwa.config.CropIwaSaveConfig;
import com.ios.widget.cropiwa.image.CropIwaResultListener;
import com.ios.widget.cropiwa.image.CropIwaResultReceiver;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class CropActivityCopy extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private TextView TvTitle;
    private ImageView IvBack, IvDone;
    private Uri imageUri;
    private CropIwaSaveConfig.Builder saveConfig16_9, saveConfig1_1;

    private CropIwaView CropView1_1, CropView16_9;
    private int imagePos;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        initViews();
        intents();
        initListeners();
        initActions();

    }

    private void initViews() {
        context = this;
        TvTitle = (TextView) findViewById(R.id.TvTitle);
        IvBack = (ImageView) findViewById(R.id.IvBack);
        IvDone = (ImageView) findViewById(R.id.IvDone);
        CropView1_1 = (CropIwaView) findViewById(R.id.CropView1_1);
        CropView16_9 = (CropIwaView) findViewById(R.id.CropView16_9);
    }

    private void intents() {
        imageUri = getIntent().getParcelableExtra(MyAppConstants.CROP_URI);
        imagePos = getIntent().getIntExtra(MyAppConstants.CROP_POS, -1);
    }

    private void initListeners() {
        IvBack.setOnClickListener(this);
        IvDone.setOnClickListener(this);
    }

    private void initActions() {
        if (MyAppConstants.isConnectingToInternet(context)) {
            MyAppAd_Banner.getInstance().showBanner(this, AdSize.LARGE_BANNER, (RelativeLayout) findViewById(R.id.RlBannerAdView), (RelativeLayout) findViewById(R.id.RlBannerAd));
        }
        IvDone.setVisibility(View.VISIBLE);
        TvTitle.setText("Crop Image");

        CropView1_1.configureOverlay()
                .setAspectRatio(new AspectRatio(1, 1))
                .apply();

        CropView16_9.configureOverlay()
                .setAspectRatio(new AspectRatio(16, 9))
                .apply();

        CropView16_9.setImageUri(imageUri);
        CropView1_1.setImageUri(imageUri);
        System.out.println("------ - - crop size : " + MyAppConstants.CROP_SIZE);
        if (MyAppConstants.CROP_SIZE == 0 || MyAppConstants.CROP_SIZE == 2) {
            CropView1_1.setVisibility(View.VISIBLE);
            CropView16_9.setVisibility(View.GONE);
        } else {
            CropView16_9.setVisibility(View.VISIBLE);
            CropView1_1.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.IvBack:
                GotoBack();
                break;
            case R.id.IvDone:
                GotoDone();
                break;
        }
    }

    private void GotoBack() {
        onBackPressed();
    }

    private void GotoDone() {
    /*    saveConfig1_1 = new CropIwaSaveConfig.Builder(MyAppConstants.createNewEmptyFile());
        CropView1_1.crop(saveConfig1_1.setCompressFormat(Bitmap.CompressFormat.PNG).setQuality(100).build(), CropActivity.this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int countExtra = new MyAppPref(context).getInt(MyAppPref.APP_AD_COUNTER, 0);
                int itemClick = SplashActivity.click++;
                if (MyAppConstants.isConnectingToInternet(context) && itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(CropActivity.this, new MyAppAd_Interstitial.MyAppCallback() {
                        @Override
                        public void AppCallback() {
                            Intent intent = new Intent();
                            if (MyAppConstants.CROP_SIZE == 0 || MyAppConstants.CROP_SIZE == 2) {
                                intent.setData(saveConfig1_1.build().getDstUri());
                                MyAppConstants.CROP_DATA = saveConfig16_9.build().getDstUri();
                            } else {
                                intent.setData(saveConfig16_9.build().getDstUri());
                                MyAppConstants.CROP_DATA = saveConfig1_1.build().getDstUri();
                            }
                            intent.putExtra(MyAppConstants.CROP_POS, imagePos);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });
                } else {
                    Intent intent = new Intent();
                    if (MyAppConstants.CROP_SIZE == 0 || MyAppConstants.CROP_SIZE == 2) {
                        intent.setData(saveConfig1_1.build().getDstUri());
//                        MyAppConstants.CROP_DATA = saveConfig16_9.build().getDstUri();
                    } else {
                        intent.setData(saveConfig16_9.build().getDstUri());
//                        MyAppConstants.CROP_DATA = saveConfig1_1.build().getDstUri();
                    }
                    intent.putExtra(MyAppConstants.CROP_POS, imagePos);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                findViewById(R.id.ProgressBarCrop).setVisibility(View.GONE);
            }
        }, 500);
*/

      /*  findViewById(R.id.ProgressBarCrop).setVisibility(View.VISIBLE);

        saveConfig16_9 = new CropIwaSaveConfig.Builder(MyAppConstants.createNewEmptyFile());
        saveConfig1_1 = new CropIwaSaveConfig.Builder(MyAppConstants.createNewEmptyFile());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("----1-1-1--1");
                CropView16_9.crop(saveConfig16_9.setCompressFormat(Bitmap.CompressFormat.PNG).setQuality(100).build());
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("----2-2--2-2");
                CropView1_1.crop(saveConfig1_1.setCompressFormat(Bitmap.CompressFormat.PNG).setQuality(100).build());
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int countExtra = new MyAppPref(context).getInt(MyAppPref.APP_AD_COUNTER, 0);
                int itemClick = SplashActivity.click++;
                if (MyAppConstants.isConnectingToInternet(context) && itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(CropActivity.this, new MyAppAd_Interstitial.MyAppCallback() {
                        @Override
                        public void AppCallback() {
                            Intent intent = new Intent();
                            if (MyAppConstants.CROP_SIZE == 0 || MyAppConstants.CROP_SIZE == 2) {
                                intent.setData(saveConfig1_1.build().getDstUri());
                                MyAppConstants.CROP_DATA = saveConfig16_9.build().getDstUri();
                            } else {
                                intent.setData(saveConfig16_9.build().getDstUri());
                                MyAppConstants.CROP_DATA = saveConfig1_1.build().getDstUri();
                            }
                            intent.putExtra(MyAppConstants.CROP_POS, imagePos);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });
                } else {
                    Intent intent = new Intent();
                    if (MyAppConstants.CROP_SIZE == 0 || MyAppConstants.CROP_SIZE == 2) {
                        intent.setData(saveConfig1_1.build().getDstUri());
                        MyAppConstants.CROP_DATA = saveConfig16_9.build().getDstUri();
                    } else {
                        intent.setData(saveConfig16_9.build().getDstUri());
                        MyAppConstants.CROP_DATA = saveConfig1_1.build().getDstUri();
                    }
                    intent.putExtra(MyAppConstants.CROP_POS, imagePos);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                findViewById(R.id.ProgressBarCrop).setVisibility(View.GONE);
            }
        },500);*/
        LocalBroadcastManager.getInstance(this).registerReceiver(CropBroadcastReceiver, new IntentFilter(CropIwaResultReceiver.ACTION_CROP_COMPLETED));


        findViewById(R.id.ProgressBarCrop).setVisibility(View.VISIBLE);

        saveConfig1_1 = new CropIwaSaveConfig.Builder(MyAppConstants.createNewEmptyFile());
//        CropView1_1.crop(saveConfig1_1.setCompressFormat(Bitmap.CompressFormat.PNG).setQuality(100).build(), CropActivityCopy.this, new CropIwaResultListener() {
//            @Override
//            public void onCropSuccess(Uri croppedUri) {
//                System.out.println("---- - -Succe 111 ");

//                saveConfig16_9 = new CropIwaSaveConfig.Builder(MyAppConstants.createNewEmptyFile());
//
//                CropView16_9.crop(saveConfig16_9.setCompressFormat(Bitmap.CompressFormat.PNG).setQuality(100).build(), CropActivity.this, new CropIwaResultListener() {
//                    @Override
//                    public void onCropSuccess(Uri croppedUri) {
//                        System.out.println("---- - -Succe 222 ");
//                        int countExtra = new MyAppPref(context).getInt(MyAppPref.APP_AD_COUNTER, 0);
//                        int itemClick = SplashActivity.click++;
//                        if (MyAppConstants.isConnectingToInternet(context) && itemClick % countExtra == 0) {
//                            MyAppAd_Interstitial.getInstance().showInter(CropActivity.this, new MyAppAd_Interstitial.MyAppCallback() {
//                                @Override
//                                public void AppCallback() {
//                                    Intent intent = new Intent();
//                                    if (MyAppConstants.CROP_SIZE == 0 || MyAppConstants.CROP_SIZE == 2) {
//                                        intent.setData(saveConfig1_1.build().getDstUri());
//                                        MyAppConstants.CROP_DATA = saveConfig16_9.build().getDstUri();
//                                    } else {
//                                        intent.setData(saveConfig16_9.build().getDstUri());
//                                        MyAppConstants.CROP_DATA = saveConfig1_1.build().getDstUri();
//                                    }
//                                    intent.putExtra(MyAppConstants.CROP_POS, imagePos);
//                                    setResult(RESULT_OK, intent);
//                                    finish();
//                                }
//                            });
//                        } else {
//                            Intent intent = new Intent();
//                            if (MyAppConstants.CROP_SIZE == 0 || MyAppConstants.CROP_SIZE == 2) {
//                                intent.setData(saveConfig1_1.build().getDstUri());
//                                MyAppConstants.CROP_DATA = saveConfig16_9.build().getDstUri();
//                            } else {
//                                intent.setData(saveConfig16_9.build().getDstUri());
//                                MyAppConstants.CROP_DATA = saveConfig1_1.build().getDstUri();
//                            }
//                            intent.putExtra(MyAppConstants.CROP_POS, imagePos);
//                            setResult(RESULT_OK, intent);
//                            finish();
//                        }
//                        findViewById(R.id.ProgressBarCrop).setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onCropFailed(Throwable e) {
//                        System.out.println("---- - -Fail 222 ");
//                    }
//                });
//            }
//
//            @Override
//            public void onCropFailed(Throwable e) {
//                System.out.println("---- - -Fail 111 ");
//            }
//        });
      /*  new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                findViewById(R.id.ProgressBarCrop).setVisibility(View.VISIBLE);

                saveConfig16_9 = new CropIwaSaveConfig.Builder(MyAppConstants.createNewEmptyFile());
                saveConfig1_1 = new CropIwaSaveConfig.Builder(MyAppConstants.createNewEmptyFile());
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                System.out.println("---- - -InBack ");
                CropView1_1.crop(saveConfig1_1.setCompressFormat(Bitmap.CompressFormat.PNG).setQuality(100).build(), CropActivity.this, resultListener);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                System.out.println("---- - -postt ");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                System.out.println("---- - -InBack 22 ");
                                CropView16_9.crop(saveConfig16_9.setCompressFormat(Bitmap.CompressFormat.PNG).setQuality(100).build(), CropActivity.this, resultListener);

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void unused) {
                                super.onPostExecute(unused);
                                System.out.println("---- - -postt 22");
                                int countExtra = new MyAppPref(context).getInt(MyAppPref.APP_AD_COUNTER, 0);
                                int itemClick = SplashActivity.click++;
                                if (MyAppConstants.isConnectingToInternet(context) && itemClick % countExtra == 0) {
                                    MyAppAd_Interstitial.getInstance().showInter(CropActivity.this, new MyAppAd_Interstitial.MyAppCallback() {
                                        @Override
                                        public void AppCallback() {
                                            Intent intent = new Intent();
                                            if (MyAppConstants.CROP_SIZE == 0 || MyAppConstants.CROP_SIZE == 2) {
                                                intent.setData(saveConfig1_1.build().getDstUri());
                                                MyAppConstants.CROP_DATA = saveConfig16_9.build().getDstUri();
                                            } else {
                                                intent.setData(saveConfig16_9.build().getDstUri());
                                                MyAppConstants.CROP_DATA = saveConfig1_1.build().getDstUri();
                                            }
                                            intent.putExtra(MyAppConstants.CROP_POS, imagePos);
                                            setResult(RESULT_OK, intent);
                                            finish();
                                        }
                                    });
                                } else {
                                    Intent intent = new Intent();
                                    if (MyAppConstants.CROP_SIZE == 0 || MyAppConstants.CROP_SIZE == 2) {
                                        intent.setData(saveConfig1_1.build().getDstUri());
                                        MyAppConstants.CROP_DATA = saveConfig16_9.build().getDstUri();
                                    } else {
                                        intent.setData(saveConfig16_9.build().getDstUri());
                                        MyAppConstants.CROP_DATA = saveConfig1_1.build().getDstUri();
                                    }
                                    intent.putExtra(MyAppConstants.CROP_POS, imagePos);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                                findViewById(R.id.ProgressBarCrop).setVisibility(View.GONE);
                            }
                        }.execute();
                    }
                }, 500);
                super.onPostExecute(unused);
            }
        }.execute();*/
    }

    @Override
    public void onBackPressed() {
        int countExtra = new MyAppPref(context).getInt(MyAppPref.APP_AD_COUNTER, 0);
        int itemClick = SplashActivity.click++;
        if (MyAppConstants.isConnectingToInternet(context) && itemClick % countExtra == 0) {
            MyAppAd_Interstitial.getInstance().showInter(CropActivityCopy.this, new MyAppAd_Interstitial.MyAppCallback() {
                @Override
                public void AppCallback() {
                    finish();
                }
            });
        } else {
            finish();
        }
    }
    private BroadcastReceiver CropBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("--- - - -:Come ");

        }
    };
}

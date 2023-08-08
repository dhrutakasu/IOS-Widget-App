package com.ios.widget.ui.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdSize;
import com.ios.widget.Ads.MyAppAd_Banner;
import com.ios.widget.Ads.MyAppAd_Interstitial;
import com.ios.widget.Files.ImageFile;
import com.ios.widget.R;
import com.ios.widget.crop.utils.MyAppConstants;
import com.ios.widget.crop.utils.MyAppPref;
import com.oginotihiro.cropview.CropUtil;
import com.oginotihiro.cropview.CropView;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CropActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private TextView TvTitle;
    private ImageView IvBack, IvDone;
    private Uri imageUri;

    private CropView CropView1_1, CropView16_9;
    private int imagePos;
    private Bitmap Crop1_1, Crop16_9;

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
        CropView1_1 = (CropView) findViewById(R.id.CropView1_1);
        CropView16_9 = (CropView) findViewById(R.id.CropView16_9);
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

        CropView1_1.withAspect(1, 1).of(imageUri)
                .initialize(context);
        CropView16_9.withAspect(16, 9).of(imageUri)
                .initialize(context);

        System.out.println("------ - - crop size : " + MyAppConstants.CROP_SIZE);
        if (MyAppConstants.CROP_SIZE == 0 || MyAppConstants.CROP_SIZE == 2) {
            CropView1_1.setVisibility(View.VISIBLE);
            CropView16_9.setVisibility(View.INVISIBLE);
        } else {
            CropView16_9.setVisibility(View.VISIBLE);
            CropView1_1.setVisibility(View.INVISIBLE);
        }
        MyAppConstants.mSelectedList1_1 = new ArrayList<>();
        MyAppConstants.mSelectedList1_1.addAll(MyAppConstants.mSelectedList);
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
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                System.out.println("-----1111 :");
                Crop1_1 = CropView1_1.getOutput();
                return Crop1_1;
            }

            @Override
            protected void onPostExecute(Bitmap unused) {
                super.onPostExecute(unused);
                System.out.println("-----1221 :");
                Uri uri1_1 = MyAppConstants.createNewEmptyFile();
                CropUtil.saveOutput(CropActivity.this, uri1_1, Crop1_1, 90);

                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        findViewById(R.id.ProgressBarCrop).setVisibility(View.VISIBLE);

                    }

                    @Override
                    protected Bitmap doInBackground(Void... voids) {
                        System.out.println("-----1211 :");

                        CropView16_9.withAspect(16, 9).of(imageUri)
                                .initialize(context);
                        Crop16_9 = CropView16_9.getOutput();

                        return Crop16_9;
                    }

                    @Override
                    protected void onPostExecute(Bitmap unused) {
                        super.onPostExecute(unused);
                        System.out.println("-----2221 :");
                        Uri uri16_9 = MyAppConstants.createNewEmptyFile();
                        CropUtil.saveOutput(CropActivity.this, uri16_9, Crop16_9, 90);
                        int countExtra = new MyAppPref(context).getInt(MyAppPref.APP_AD_COUNTER, 0);
                        int itemClick = SplashActivity.click++;
                        if (MyAppConstants.isConnectingToInternet(context) && itemClick % countExtra == 0) {
                            MyAppAd_Interstitial.getInstance().showInter(CropActivity.this, new MyAppAd_Interstitial.MyAppCallback() {
                                @Override
                                public void AppCallback() {
                                    for (int i = 0; i < MyAppConstants.mSelectedList.size(); i++) {
                                        if (imagePos == i) {
                                            ImageFile imageFile = new ImageFile();
                                            imageFile.setPath(uri1_1.getPath());
                                            MyAppConstants.mSelectedList.set(imagePos,imageFile);
                                        }
                                    }
                                    for (int i = 0; i < MyAppConstants.mSelectedList1_1.size(); i++) {
                                        if (imagePos == i) {
                                            ImageFile imageFile = new ImageFile();
                                            imageFile.setPath(uri16_9.getPath());
                                            MyAppConstants.mSelectedList1_1.set(imagePos,imageFile);
                                        }
                                    }
                                    Intent intent = new Intent();
                                    if (MyAppConstants.CROP_SIZE == 0 || MyAppConstants.CROP_SIZE == 2) {
                                        intent.setData(uri1_1);
                                        MyAppConstants.CROP_DATA = uri16_9;
                                    } else {
                                        intent.setData(uri16_9);
                                        MyAppConstants.CROP_DATA = uri1_1;
                                    }
                                    intent.putExtra(MyAppConstants.CROP_POS, imagePos);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            });
                        } else {
                            for (int i = 0; i < MyAppConstants.mSelectedList.size(); i++) {
                                if (imagePos == i) {
                                    ImageFile imageFile = new ImageFile();
                                    imageFile.setPath(uri1_1.getPath());
                                    MyAppConstants.mSelectedList.set(imagePos,imageFile);
                                }
                            }
                            for (int i = 0; i < MyAppConstants.mSelectedList1_1.size(); i++) {
                                if (imagePos == i) {
                                    ImageFile imageFile = new ImageFile();
                                    imageFile.setPath(uri16_9.getPath());
                                    MyAppConstants.mSelectedList1_1.set(imagePos,imageFile);
                                }
                            }
                            Intent intent = new Intent();
                            if (MyAppConstants.CROP_SIZE == 0 || MyAppConstants.CROP_SIZE == 2) {
                                intent.setData(uri1_1);
                                MyAppConstants.CROP_DATA = uri16_9;

                            } else {
                                intent.setData(uri16_9);
                                MyAppConstants.CROP_DATA = uri1_1;
                            }
                            intent.putExtra(MyAppConstants.CROP_POS, imagePos);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                        findViewById(R.id.ProgressBarCrop).setVisibility(View.GONE);
                    }
                }.execute();
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        int countExtra = new MyAppPref(context).getInt(MyAppPref.APP_AD_COUNTER, 0);
        int itemClick = SplashActivity.click++;
        if (MyAppConstants.isConnectingToInternet(context) && itemClick % countExtra == 0) {
            MyAppAd_Interstitial.getInstance().showInter(CropActivity.this, new MyAppAd_Interstitial.MyAppCallback() {
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

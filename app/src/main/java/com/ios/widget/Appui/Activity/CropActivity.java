package com.ios.widget.Appui.Activity;

import static com.ios.widget.AppUtils.MyAppConstants.mSelectedList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdSize;
import com.google.android.material.snackbar.Snackbar;
import com.ios.widget.AppAdv.MyAppAd_Banner;
import com.ios.widget.AppAdv.MyAppAd_Interstitial;
import com.ios.widget.AppFiles.ImageFile;
import com.ios.widget.R;
import com.ios.widget.AppUtils.MyAppConstants;
import com.ios.widget.AppUtils.MyAppPref;
import com.ios.widget.ImageCropview.CropUtil;
import com.ios.widget.ImageCropview.ImageCropView;
import com.ios.widget.Appui.Adapter.CropAdapter;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CropActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private TextView TvTitle;
    private ImageView IvBack, IvDone;
    private Uri imageUri;

    private ImageCropView imageCropView1_1, imageCropView16_9;
    private int imagePos;
    private Bitmap Crop1_1, Crop16_9;
    private RecyclerView RvCropList;
    private CropAdapter cropAdapter;
    private RelativeLayout RlCropView;
    private int CropInt;

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
        imageCropView1_1 = (ImageCropView) findViewById(R.id.CropView1_1);
        imageCropView16_9 = (ImageCropView) findViewById(R.id.CropView16_9);
        RvCropList = (RecyclerView) findViewById(R.id.RvCropList);
        RlCropView = (RelativeLayout) findViewById(R.id.RlCropView);
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

        CropInt = 0;
        imageCropView1_1.withAspect(1, 1).of(FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(mSelectedList.get(0).getPath().toString())))
                .initialize(context);
        imageCropView16_9.withAspect(16, 9).of(FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(mSelectedList.get(0).getPath().toString())))
                .initialize(context);

        if (MyAppConstants.CROP_SIZE == 0 || MyAppConstants.CROP_SIZE == 2) {
            imageCropView1_1.setVisibility(View.VISIBLE);
            imageCropView16_9.setVisibility(View.INVISIBLE);
        } else {
            imageCropView16_9.setVisibility(View.VISIBLE);
            imageCropView1_1.setVisibility(View.INVISIBLE);
        }
        MyAppConstants.mCropSelectedList_1_1 = new ArrayList<>();
        MyAppConstants.mCropSelectedList = new ArrayList<>();
        MyAppConstants.mCropSelectedList.addAll(mSelectedList);
        MyAppConstants.mCropSelectedList_1_1.addAll(mSelectedList);

        RvCropList.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        cropAdapter = new CropAdapter(context, MyAppConstants.mCropSelectedList, new CropAdapter.SetWidgetRemoveCrop() {
            @Override
            public void RemoveCropImage(int position) {
                if (MyAppConstants.getSelectedImages().size() <= 2) {
                    Snackbar snackbar = Snackbar
                            .make(RlCropView, "Two photos compulsory for widget create....", Snackbar.LENGTH_LONG);

                    snackbar.show();
                } else {
                    MyAppConstants.mCropSelectedList_1_1.remove(position);
                    MyAppConstants.mCropSelectedList.remove(position);
                    ImageFile file = MyAppConstants.getSelectedImages().get(position);
                    MyAppConstants.removeSelectedImages(file);
                    cropAdapter.notifyDataSetChanged();
                }
            }
        });
        RvCropList.setAdapter(cropAdapter);
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
        if (CropInt == (mSelectedList.size() - 1)) {
            if (MyAppConstants.CROP_SIZE == 0 || MyAppConstants.CROP_SIZE == 2) {
                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(Void... voids) {
                        Crop1_1 = imageCropView1_1.getOutput();
                        return Crop1_1;
                    }

                    @Override
                    protected void onPostExecute(Bitmap unused) {
                        super.onPostExecute(unused);
                        Uri uri1_1 = MyAppConstants.createNewEmptyFile("Crop1_1");
                        CropUtil.saveOutput(CropActivity.this, uri1_1, Crop1_1, 90);

                        new AsyncTask<Void, Void, Bitmap>() {
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                findViewById(R.id.ProgressBarCrop).setVisibility(View.VISIBLE);

                            }

                            @Override
                            protected Bitmap doInBackground(Void... voids) {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        imageCropView16_9.withAspect(16, 9).of(FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(mSelectedList.get(CropInt).getPath().toString())))
                                                .initialize(context);
                                        Crop16_9 = imageCropView16_9.getOutput();
                                    }
                                });
                                return Crop16_9;
                            }

                            @Override
                            protected void onPostExecute(Bitmap unused) {
                                super.onPostExecute(unused);
                                Uri uri16_9 = MyAppConstants.createNewEmptyFile("Crop16_9");
                                CropUtil.saveOutput(CropActivity.this, uri16_9, Crop16_9, 90);

                                ImageFile file = new ImageFile();
                                file.setPath(uri1_1.getPath());
                                MyAppConstants.mCropSelectedList_1_1.set(CropInt, file);

                                ImageFile file16_9 = new ImageFile();
                                file16_9.setPath(uri16_9.getPath());
                                MyAppConstants.mCropSelectedList.set(CropInt, file16_9);

                                int countExtra = new MyAppPref(context).getInt(MyAppPref.APP_AD_COUNTER, 0);
                                int itemClick = SplashActivity.click++;
                                if (MyAppConstants.isConnectingToInternet(context) && itemClick % countExtra == 0) {
                                    MyAppAd_Interstitial.getInstance().showInter(CropActivity.this, new MyAppAd_Interstitial.MyAppCallback() {
                                        @Override
                                        public void AppCallback() {
                                            Intent intent = new Intent();
                                            setResult(RESULT_OK, intent);
                                            finish();
                                        }
                                    });
                                } else {

                                    ImageFile fileelse = new ImageFile();
                                    fileelse.setPath(uri1_1.getPath());
                                    MyAppConstants.mCropSelectedList_1_1.set(CropInt, fileelse);

                                    ImageFile fileelse16_9 = new ImageFile();
                                    fileelse16_9.setPath(uri16_9.getPath());
                                    MyAppConstants.mCropSelectedList.set(CropInt, fileelse16_9);
                                    Intent intent = new Intent();
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.ProgressBarCrop).setVisibility(View.GONE);
                                    }
                                }, 800);
                            }
                        }.execute();
                    }
                }.execute();
            } else {
                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(Void... voids) {
                        Crop16_9 = imageCropView16_9.getOutput();
                        return Crop16_9;
                    }

                    @Override
                    protected void onPostExecute(Bitmap unused) {
                        super.onPostExecute(unused);
                        Uri uri16_9 = MyAppConstants.createNewEmptyFile("Crop16_9");
                        CropUtil.saveOutput(CropActivity.this, uri16_9, Crop16_9, 90);

                        new AsyncTask<Void, Void, Bitmap>() {
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                findViewById(R.id.ProgressBarCrop).setVisibility(View.VISIBLE);

                            }

                            @Override
                            protected Bitmap doInBackground(Void... voids) {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        imageCropView1_1.withAspect(1, 1).of(FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(mSelectedList.get(CropInt).getPath().toString())))
                                                .initialize(context);
                                        Crop1_1 = imageCropView1_1.getOutput();
                                    }
                                });
                                return Crop1_1;
                            }

                            @Override
                            protected void onPostExecute(Bitmap unused) {
                                super.onPostExecute(unused);
                                Uri uri1_1 = MyAppConstants.createNewEmptyFile("Crop1_1");
                                CropUtil.saveOutput(CropActivity.this, uri1_1, Crop1_1, 90);

                                ImageFile file = new ImageFile();
                                file.setPath(uri1_1.getPath());
                                MyAppConstants.mCropSelectedList_1_1.set(CropInt, file);

                                ImageFile file16_9 = new ImageFile();
                                file16_9.setPath(uri16_9.getPath());
                                MyAppConstants.mCropSelectedList.set(CropInt, file16_9);

                                int countExtra = new MyAppPref(context).getInt(MyAppPref.APP_AD_COUNTER, 0);
                                int itemClick = SplashActivity.click++;
                                if (MyAppConstants.isConnectingToInternet(context) && itemClick % countExtra == 0) {
                                    MyAppAd_Interstitial.getInstance().showInter(CropActivity.this, new MyAppAd_Interstitial.MyAppCallback() {
                                        @Override
                                        public void AppCallback() {
                                            Intent intent = new Intent();
                                            setResult(RESULT_OK, intent);
                                            finish();
                                        }
                                    });
                                } else {

                                    ImageFile fileelse = new ImageFile();
                                    fileelse.setPath(uri1_1.getPath());
                                    MyAppConstants.mCropSelectedList_1_1.set(CropInt, fileelse);

                                    ImageFile fileelse16_9 = new ImageFile();
                                    fileelse16_9.setPath(uri16_9.getPath());
                                    MyAppConstants.mCropSelectedList.set(CropInt, fileelse16_9);
                                    Intent intent = new Intent();
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.ProgressBarCrop).setVisibility(View.GONE);
                                    }
                                }, 800);
                            }
                        }.execute();
                    }
                }.execute();
            }
        } else {
            if (MyAppConstants.CROP_SIZE == 0 || MyAppConstants.CROP_SIZE == 2) {
                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        findViewById(R.id.ProgressBarCrop).setVisibility(View.VISIBLE);
                    }

                    @Override
                    protected Bitmap doInBackground(Void... voids) {

                        Crop1_1 = imageCropView1_1.getOutput();

                        return Crop1_1;
                    }

                    @Override
                    protected void onPostExecute(Bitmap unused) {
                        super.onPostExecute(unused);
                        Uri uri1_1 = MyAppConstants.createNewEmptyFile("Crop1_1");
                        CropUtil.saveOutput(CropActivity.this, uri1_1, Crop1_1, 90);

                        new AsyncTask<Void, Void, Bitmap>() {


                            @Override
                            protected Bitmap doInBackground(Void... voids) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        imageCropView16_9.withAspect(16, 9).of(FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(mSelectedList.get(CropInt).getPath().toString())))
                                                .initialize(context);
                                        Crop16_9 = imageCropView16_9.getOutput();
                                    }
                                });
                                return Crop16_9;
                            }

                            @Override
                            protected void onPostExecute(Bitmap unused) {
                                super.onPostExecute(unused);
                                Uri uri16_9 = MyAppConstants.createNewEmptyFile("Crop16_9");
                                CropUtil.saveOutput(CropActivity.this, uri16_9, Crop16_9, 90);

                                ImageFile file = new ImageFile();
                                file.setPath(uri1_1.getPath());
                                MyAppConstants.mCropSelectedList_1_1.set(CropInt, file);

                                ImageFile file16_9 = new ImageFile();
                                file16_9.setPath(uri16_9.getPath());
                                MyAppConstants.mCropSelectedList.set(CropInt, file16_9);
                                cropAdapter.notifyItemChanged(CropInt);
                                CropInt++;
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.ProgressBarCrop).setVisibility(View.GONE);

                                        if (!(CropInt >= mSelectedList.size())) {
                                            imageCropView1_1.withAspect(1, 1).of(FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(mSelectedList.get(CropInt).getPath().toString())))
                                                    .initialize(context);
                                        }
                                    }
                                }, 800);
                            }
                        }.execute();
                    }
                }.execute();
            } else {

                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        findViewById(R.id.ProgressBarCrop).setVisibility(View.VISIBLE);
                    }

                    @Override
                    protected Bitmap doInBackground(Void... voids) {

                        Crop16_9 = imageCropView16_9.getOutput();

                        return Crop16_9;
                    }

                    @Override
                    protected void onPostExecute(Bitmap unused) {
                        super.onPostExecute(unused);
                        Uri uri16_9 = MyAppConstants.createNewEmptyFile("Crop16_9");
                        CropUtil.saveOutput(CropActivity.this, uri16_9, Crop16_9, 90);

                        new AsyncTask<Void, Void, Bitmap>() {


                            @Override
                            protected Bitmap doInBackground(Void... voids) {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        imageCropView1_1.withAspect(16, 9).of(FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(mSelectedList.get(CropInt).getPath().toString())))
                                                .initialize(context);
                                        Crop1_1 = imageCropView1_1.getOutput();
                                    }
                                });
                                return Crop1_1;
                            }

                            @Override
                            protected void onPostExecute(Bitmap unused) {
                                super.onPostExecute(unused);
                                Uri uri1_1 = MyAppConstants.createNewEmptyFile("Crop1_1");
                                CropUtil.saveOutput(CropActivity.this, uri1_1, Crop1_1, 90);

                                ImageFile file = new ImageFile();
                                file.setPath(uri1_1.getPath());
                                MyAppConstants.mCropSelectedList_1_1.set(CropInt, file);

                                ImageFile file16_9 = new ImageFile();
                                file16_9.setPath(uri16_9.getPath());
                                MyAppConstants.mCropSelectedList.set(CropInt, file16_9);
                                cropAdapter.notifyItemChanged(CropInt);
                                CropInt++;

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.ProgressBarCrop).setVisibility(View.GONE);
                                        if (!(CropInt >= mSelectedList.size())) {
                                            imageCropView16_9.withAspect(16, 9).of(FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(mSelectedList.get(CropInt).getPath().toString())))
                                                    .initialize(context);
                                        }
                                    }
                                }, 800);
                            }
                        }.execute();
                    }
                }.execute();
            }
        }

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
}

package com.ios.widget.ui.Activity;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.ios.widget.ui.Adapter.ImageCropAdapter;
import com.ios.widget.ui.Adapter.ImagePickAdapter;

import java.io.File;

public class ImageCropListActivity extends AppCompatActivity implements View.OnClickListener {
    private final int CROP_RESULT = 21;
    private Context context;
    private TextView TvTitle;
    private ImageView IvBack, IvDone;
    private RecyclerView RvImageList;
    private ImageCropAdapter CropAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop_list);

        initViews();
        intents();
        initListners();
        initActions();
    }

    private void initViews() {
        context = this;
        TvTitle = (TextView) findViewById(R.id.TvTitle);
        IvBack = (ImageView) findViewById(R.id.IvBack);
        IvDone = (ImageView) findViewById(R.id.IvDone);
        RvImageList = (RecyclerView) findViewById(R.id.RvImageList);
    }

    private void intents() {

    }

    private void initListners() {
        IvBack.setOnClickListener(this);
        IvDone.setOnClickListener(this);
    }

    private void initActions() {
        if (MyAppConstants.isConnectingToInternet(context)) {
            MyAppAd_Banner.getInstance().showBanner(this, AdSize.LARGE_BANNER, (RelativeLayout) findViewById(R.id.RlBannerAdView), (RelativeLayout) findViewById(R.id.RlBannerAd));
        }
        IvDone.setVisibility(View.VISIBLE);
        TvTitle.setText("Images");
        GridLayoutManager layoutManager = new GridLayoutManager(context, 4);
        RvImageList.setLayoutManager(layoutManager);

        CropAdapter = new ImageCropAdapter(this, MyAppConstants.mSelectedList, new ImageCropAdapter.CropImage() {
            @Override
            public void setCropImage(@NonNull ImageFile imageFile, int position) {
                startActivityForResult(new Intent(context, CropActivity.class).putExtra(MyAppConstants.CROP_URI, FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(imageFile.getPath().toString()))).putExtra(MyAppConstants.CROP_POS, position), CROP_RESULT);
            }
        });
        RvImageList.setAdapter(CropAdapter);
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
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        int countExtra = new MyAppPref(context).getInt(MyAppPref.APP_AD_COUNTER, 0);
        int itemClick = SplashActivity.click++;
        if (MyAppConstants.isConnectingToInternet(context) && itemClick % countExtra == 0) {
            MyAppAd_Interstitial.getInstance().showInter(ImageCropListActivity.this, () -> finish());
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CROP_RESULT:
                switch (resultCode) {
                    case RESULT_OK:
                        if (data.getData() != null) {
//                            System.out.println("---- - - path : " + data.getData().getPath());
                            int pos = data.getIntExtra(MyAppConstants.CROP_POS, -1);
                            if (MyAppConstants.CROP_SIZE == 0 || MyAppConstants.CROP_SIZE == 2) {
                                ImageFile file = MyAppConstants.mSelectedList.get(pos);
                                file.setPath(data.getData().getPath());
                                MyAppConstants.mSelectedList.set(pos, file);
                            }else {
                                ImageFile file = MyAppConstants.mSelectedList1_1.get(pos);
                                file.setPath(data.getData().getPath());
                                MyAppConstants.mSelectedList1_1.set(pos, file);
                            }
                            CropAdapter.notifyItemChanged(pos);
                        }
                        break;
                }
                break;
        }
    }
}
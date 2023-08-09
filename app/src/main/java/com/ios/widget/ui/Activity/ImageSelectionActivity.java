package com.ios.widget.ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdSize;
import com.google.android.material.snackbar.Snackbar;
import com.ios.widget.Ads.MyAppAd_Banner;
import com.ios.widget.Ads.MyAppAd_Interstitial;
import com.ios.widget.Callback.OnSelectStateListener;
import com.ios.widget.Files.Directory;
import com.ios.widget.Files.ImageFile;
import com.ios.widget.R;
import com.ios.widget.ui.Adapter.FolderListAdapter;
import com.ios.widget.ui.Adapter.ImagePickAdapter;
import com.ios.widget.utils.MyAppConstants;
import com.ios.widget.utils.MyAppPref;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.ios.widget.utils.MyAppConstants.getmSelectedList;
import static com.ios.widget.utils.MyAppConstants.list;
import static com.ios.widget.utils.MyAppConstants.mAll;
import static com.ios.widget.utils.MyAppConstants.mSelectedList;

public class ImageSelectionActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CROP_LIST = 20;
    public static String EXTRA_FROM_PREVIEW = MyAppConstants.EDIT_PERVIEW;
    private Context context;
    private TextView TvTitle;
    private ImageView IvBack, IvDone;
    private RecyclerView RvFolderList, RvImageAlbumlist;
    private FolderListAdapter folderListAdapter;
    private ImagePickAdapter PickAdapter;
    private int MaxNumber;
    private boolean isPreview = false;
    private RelativeLayout RlMainImgSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_image_selection);

        initViews();
        intents();
        initlistners();
        initActions();
    }

    private void initViews() {
        context = this;
        TvTitle = (TextView) findViewById(R.id.TvTitle);
        IvBack = (ImageView) findViewById(R.id.IvBack);
        IvDone = (ImageView) findViewById(R.id.IvDone);
        RvFolderList = (RecyclerView) findViewById(R.id.RvFolderList);
        RvImageAlbumlist = (RecyclerView) findViewById(R.id.RvImageAlbumlist);
        RlMainImgSelection = (RelativeLayout) findViewById(R.id.RlMainImgSelection);

    }

    private void intents() {
        MaxNumber = getIntent().getIntExtra(MyAppConstants.MAX_NUMBER, 2);
        isPreview = getIntent().hasExtra(EXTRA_FROM_PREVIEW);

    }

    private void initlistners() {
        IvBack.setOnClickListener(this);
        IvDone.setOnClickListener(this);


    }

    private void initActions() {
        if (MyAppConstants.isConnectingToInternet(context)) {
            MyAppAd_Banner.getInstance().showBanner(this, AdSize.LARGE_BANNER, (RelativeLayout) findViewById(R.id.RlBannerAdView), (RelativeLayout) findViewById(R.id.RlBannerAd));
        }

        TvTitle.setText(getResources().getString(R.string.str_folder_all));

        if (MaxNumber == 10) {
            IvDone.setVisibility(View.VISIBLE);
        }
        if (isPreview) {
            mSelectedList.clear();
            mSelectedList.addAll(getmSelectedList);
        }
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        RvImageAlbumlist.setLayoutManager(layoutManager);

        PickAdapter = new ImagePickAdapter(this);
        RvImageAlbumlist.setAdapter(PickAdapter);

        PickAdapter.setOnSelectStateListener(new OnSelectStateListener<ImageFile>() {
            @Override
            public void OnSelectStateChanged(boolean state, ImageFile file) {
                if (state) {
                    if (mSelectedList.size() == 10) {
                        Snackbar snackbar = Snackbar.make(RlMainImgSelection, "Select up to 10 images", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else {
                        if (file.isSelected()) {
                            MyAppConstants.addSelectedImage(file);
                        } else {
                            MyAppConstants.removeSelectedImages(file);
                        }
                    }
                }
            }

            @Override
            public void OnRemoveStateChanged(boolean state, int file) {

            }
        });

        RvFolderList.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        folderListAdapter = new FolderListAdapter(context, list);
        RvFolderList.setAdapter(folderListAdapter);

        folderListAdapter.setListener(new FolderListAdapter.FolderListListener() {
            @Override
            public void onFolderListClick(Directory directory) {
                TvTitle.setText(directory.getName());
                if (TextUtils.isEmpty(directory.getPath())) { //All
                    refreshData(mAll);
                } else {
                    for (Directory<ImageFile> dir : mAll) {
                        if (dir.getPath().equals(directory.getPath())) {
                            List<Directory<ImageFile>> list = new ArrayList<>();
                            list.add(dir);
                            refreshData(list);
                            break;
                        }
                    }
                }
            }
        });
        refreshData(mAll);
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
        ImageSelectionActivity.this.onBackPressed();
    }

    private void GotoDone() {
        if (MyAppConstants.getSelectedImages().size() <= 1) {
            Snackbar snackbar = Snackbar
                    .make(RlMainImgSelection, "Please Select more then 10 Images for Create widget....", Snackbar.LENGTH_LONG);

            snackbar.show();
        } else {
            startActivityForResult(new Intent(context, CropActivity.class), CROP_LIST);
        }
    }

    private void GotoEditActivity() {
        int countExtra = new MyAppPref(context).getInt(MyAppPref.APP_AD_COUNTER, 0);
        int itemClick = SplashActivity.click++;
        if (MyAppConstants.isConnectingToInternet(context) && itemClick % countExtra == 0) {
            MyAppAd_Interstitial.getInstance().showInter(ImageSelectionActivity.this, new MyAppAd_Interstitial.MyAppCallback() {
                @Override
                public void AppCallback() {
                    setResult(RESULT_OK);
                    finish();
                }
            });
        } else {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CROP_LIST:
                switch (resultCode){
                    case RESULT_OK:
                        GotoEditActivity();
                        break;
                }
                break;
        }
    }

    private void refreshData(List<Directory<ImageFile>> directories) {
        List<ImageFile> list = new ArrayList<>();
        for (Directory<ImageFile> directory : directories) {
            list.addAll(directory.getFiles());
        }

        for (ImageFile file : mSelectedList) {
            int index = list.indexOf(file);
            if (index != -1) {
                list.get(index).setSelected(true);
            }
        }
        PickAdapter.refresh(list);
    }

    @Override
    public void onBackPressed() {
        int countExtra = new MyAppPref(context).getInt(MyAppPref.APP_AD_COUNTER, 0);
        int itemClick = SplashActivity.click++;
        if (MyAppConstants.isConnectingToInternet(context) && itemClick % countExtra == 0) {
            MyAppAd_Interstitial.getInstance().showInter(ImageSelectionActivity.this, new MyAppAd_Interstitial.MyAppCallback() {
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
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

import com.google.android.material.snackbar.Snackbar;
import com.ios.widget.Callback.OnSelectStateListener;
import com.ios.widget.Files.Directory;
import com.ios.widget.Files.ImageFile;
import com.ios.widget.R;
import com.ios.widget.ui.Adapter.FolderListAdapter;
import com.ios.widget.ui.Adapter.ImagePickAdapter;
import com.ios.widget.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.ios.widget.utils.Constants.getmSelectedList;
import static com.ios.widget.utils.Constants.list;
import static com.ios.widget.utils.Constants.mAll;
import static com.ios.widget.utils.Constants.mSelectedList;

public class ImageSelection_Activity extends AppCompatActivity implements View.OnClickListener {

    private static final int ARRANGE_IMAGE = 111;
    public static String EXTRA_FROM_PREVIEW = Constants.EDIT_PERVIEW;
    private Context context;
    private TextView tv_title;
    private ImageView iv_Back, iv_done;
    private RecyclerView rv_folder_list, rv_Image_Albumlist;
    private FolderListAdapter mAdapterList;
    private ImagePickAdapter pickAdapter;
    private int MaxNumber;
    private boolean isPreview = false;
    private RelativeLayout rl_main_img_selection;

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
        tv_title = (TextView) findViewById(R.id.TvTitle);
        iv_Back = (ImageView) findViewById(R.id.IvBack);
        iv_done = (ImageView) findViewById(R.id.iv_done);
        rv_folder_list = (RecyclerView) findViewById(R.id.rv_folder_list);
        rv_Image_Albumlist = (RecyclerView) findViewById(R.id.rv_Image_Albumlist);
        rl_main_img_selection = (RelativeLayout) findViewById(R.id.rl_main_img_selection);

    }

    private void intents() {
        MaxNumber = getIntent().getIntExtra(Constants.MAX_NUMBER, 2);
        isPreview = getIntent().hasExtra(EXTRA_FROM_PREVIEW);

    }

    private void initlistners() {
        iv_Back.setOnClickListener(this);
        iv_done.setOnClickListener(this);


    }

    private void initActions() {
        tv_title.setText(getResources().getString(R.string.folder_all));

        if (MaxNumber == 10) {
            iv_done.setVisibility(View.VISIBLE);
        }
        if (isPreview) {
            mSelectedList.clear();
            mSelectedList.addAll(getmSelectedList);
        }
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rv_Image_Albumlist.setLayoutManager(layoutManager);

        pickAdapter = new ImagePickAdapter(this);
        rv_Image_Albumlist.setAdapter(pickAdapter);

        pickAdapter.setOnSelectStateListener(new OnSelectStateListener<ImageFile>() {
            @Override
            public void OnSelectStateChanged(boolean state, ImageFile file) {
                if (state) {
                    if (mSelectedList.size() == 10) {
                        Snackbar snackbar = Snackbar.make(rl_main_img_selection, "Select up to 10 images", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else {
                        if (file.isSelected()) {
                            Constants.addSelectedImage(file);
                        } else {
                            Constants.removeSelectedImages(file);
                        }
                    }
                }
            }

            @Override
            public void OnRemoveStateChanged(boolean state, int file) {

            }
        });

        rv_folder_list.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        mAdapterList = new FolderListAdapter(context, list);
        rv_folder_list.setAdapter(mAdapterList);

        mAdapterList.setListener(new FolderListAdapter.FolderListListener() {
            @Override
            public void onFolderListClick(Directory directory) {
                tv_title.setText(directory.getName());
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
        GridLayoutManager manager = new GridLayoutManager(context, 4);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.IvBack:
                GotoBack();
                break;
            case R.id.iv_done:
                GotoDone();
                break;
        }
    }

    private void GotoBack() {
        ImageSelection_Activity.this.onBackPressed();
    }

    private void GotoDone() {
        if (Constants.getSelectedImages().size() <= 1) {
            Snackbar snackbar = Snackbar
                    .make(rl_main_img_selection, "Please Select more then 10 Images for Create widget....", Snackbar.LENGTH_LONG);

            snackbar.show();
        } else {
            GotoEditActivity();
        }
    }

    private void GotoEditActivity() {
        for (int i = 0; i < mSelectedList.size(); i++) {
        }
        setResult(RESULT_OK);
        finish();
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
        pickAdapter.refresh(list);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ARRANGE_IMAGE:
                switch (resultCode) {
                    case RESULT_OK:
                        setResult(RESULT_OK);
                        finish();
                        break;
                }
                break;
        }
    }
}
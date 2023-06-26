package com.ios.widget.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ios.widget.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ImageView IvBack;
    private TextView TvTitle;
    private ConstraintLayout ConstRate,ConstShare,ConstPolicy,ConstAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initViews();
        iniListeners();
        initActions();
    }

    private void initViews() {
        context=this;
        IvBack= (ImageView) findViewById(R.id.IvBack);
        TvTitle= (TextView) findViewById(R.id.TvTitle);
        ConstRate=(ConstraintLayout)findViewById(R.id.ConstRate);
        ConstShare=(ConstraintLayout)findViewById(R.id.ConstShare);
        ConstPolicy=(ConstraintLayout)findViewById(R.id.ConstPolicy);
        ConstAbout=(ConstraintLayout)findViewById(R.id.ConstAbout);
    }

    private void iniListeners() {
        IvBack.setOnClickListener(this);
        ConstRate.setOnClickListener(this);
        ConstShare.setOnClickListener(this);
        ConstPolicy.setOnClickListener(this);
        ConstAbout.setOnClickListener(this);
    }

    private void initActions() {
        TvTitle.setText(getResources().getString(R.string.setting));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.IvBack:
                onBackPressed();
                break;
            case R.id.ConstRate:
                break;
            case R.id.ConstShare:
                break;
            case R.id.ConstPolicy:
                break;
            case R.id.ConstAbout:
                break;
        }
    }
}
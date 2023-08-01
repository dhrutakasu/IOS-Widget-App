package com.ios.widget.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ios.widget.Model.WidgetModel;
import com.ios.widget.R;
import com.ios.widget.ui.Adapter.ItemAdapter;
import com.ios.widget.ui.Adapter.MyWidgetAdapter;
import com.ios.widget.utils.Constants;

import java.util.ArrayList;

public class ShowItemActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ImageView IvBack;
    private TextView TvTitle;
    private RecyclerView RvItemList;
    private int TabPos;
    private ArrayList<WidgetModel> modelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);
        initViews();
        initIntents();
        iniListeners();
        initActions();
    }

    private void initViews() {
        context = this;
        IvBack = (ImageView) findViewById(R.id.IvBack);
        TvTitle = (TextView) findViewById(R.id.TvTitle);
        RvItemList = (RecyclerView) findViewById(R.id.RvItemList);
    }

    private void initIntents() {
        TabPos = getIntent().getIntExtra(Constants.TabPos, 0);
        if (TabPos == 0) {
            TvTitle.setText("Trendy");
            modelArrayList = Constants.getTrendyWidgetLists();
        } else if (TabPos == 1) {
            TvTitle.setText("Calendar");
            modelArrayList = Constants.getCalendarWidgetLists();
        } else if (TabPos == 2) {
            TvTitle.setText("Weather");
            modelArrayList = Constants.getWeatherWidgetLists();
        } else if (TabPos == 3) {
            TvTitle.setText("Clock");
            modelArrayList = Constants.getClockWidgetLists();
        } else if (TabPos == 4) {
            TvTitle.setText("X-Panel");
            modelArrayList = Constants.getXPanelWidgetLists();
        } else if (TabPos == 5) {
            TvTitle.setText("Photo");
            modelArrayList = Constants.getPhotoWidgetLists();
        }
    }

    private void iniListeners() {
        IvBack.setOnClickListener(this);
    }

    private void initActions() {
        RvItemList.setLayoutManager(new LinearLayoutManager(context));
        RvItemList.setAdapter(new ItemAdapter(context, modelArrayList,TabPos));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.IvBack:
                onBackPressed();
                break;
        }
    }
}
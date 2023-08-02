package com.ios.widget.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ios.widget.Model.WidgetData;
import com.ios.widget.Model.WidgetModel;
import com.ios.widget.R;
import com.ios.widget.helper.DatabaseHelper;
import com.ios.widget.provider.LargeWidgetProvider;
import com.ios.widget.provider.MediumWidgetProvider;
import com.ios.widget.provider.SmallWidgetProvider;
import com.ios.widget.ui.Adapter.MyWidgetAdapter;
import com.ios.widget.utils.Constants;

import java.util.ArrayList;
import java.util.HashSet;

public class MyWidgetsActivity extends AppCompatActivity implements View.OnClickListener, MyWidgetAdapter.deleteWidget {

    private Context context;
    private ImageView IvBack, IvNotFoundRecord, iv_done, IvMyWidgetTrendy, IvMyWidgetCalendar, IvMyWidgetWeather, IvMyWidgetAlarm, IvMyWidgetXPanel, IvMyWidgetPhoto;
    private TextView TvTitle;
    private DatabaseHelper helper;
    private RecyclerView RvMyWidget;
    private ArrayList<WidgetData> myWidgetLists = new ArrayList<WidgetData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_widgets_acivity);

        initViews();
        iniListeners();
        initActions();
    }

    private void initViews() {
        context = this;
        IvBack = (ImageView) findViewById(R.id.IvBack);
        iv_done = (ImageView) findViewById(R.id.iv_done);
        IvNotFoundRecord = (ImageView) findViewById(R.id.IvNotFoundRecord);
        IvMyWidgetTrendy = (ImageView) findViewById(R.id.IvMyWidgetTrendy);
        IvMyWidgetCalendar = (ImageView) findViewById(R.id.IvMyWidgetCalendar);
        IvMyWidgetWeather = (ImageView) findViewById(R.id.IvMyWidgetWeather);
        IvMyWidgetAlarm = (ImageView) findViewById(R.id.IvMyWidgetAlarm);
        IvMyWidgetXPanel = (ImageView) findViewById(R.id.IvMyWidgetXPanel);
        IvMyWidgetPhoto = (ImageView) findViewById(R.id.IvMyWidgetPhoto);
        TvTitle = (TextView) findViewById(R.id.TvTitle);
        RvMyWidget = (RecyclerView) findViewById(R.id.RvMyWidget);
    }

    private void iniListeners() {
        IvBack.setOnClickListener(this);
        iv_done.setOnClickListener(this);
        IvMyWidgetTrendy.setOnClickListener(this);
        IvMyWidgetCalendar.setOnClickListener(this);
        IvMyWidgetWeather.setOnClickListener(this);
        IvMyWidgetAlarm.setOnClickListener(this);
        IvMyWidgetXPanel.setOnClickListener(this);
        IvMyWidgetPhoto.setOnClickListener(this);
    }

    private void initActions() {
        iv_done.setImageResource(R.drawable.ic_delete);
        helper = new DatabaseHelper(context);
//        myWidgetLists = helper.getWidgets();
        TvTitle.setText(getResources().getString(R.string.my_widget));
        myWidgetLists = new ArrayList<>();
        for (int j = 0; j < Constants.getTrendyWidgetLists().size(); j++) {
            System.out.println("_______%%%% PPPP: " + helper.getWidgetsType(Constants.getTrendyWidgetLists().get(j).getPosition()).toString());
            myWidgetLists.addAll(helper.getWidgetsType(Constants.getTrendyWidgetLists().get(j).getPosition()));
        }
        RvMyWidget.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        RvMyWidget.setAdapter(new MyWidgetAdapter(context, myWidgetLists, this));
        getIsNotFound();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.IvBack:
                onBackPressed();
                break;
            case R.id.iv_done:
                System.out.println("_____  _ _ : " + Constants.WidgetRemove);
                if (Constants.WidgetRemove) {
                    Constants.WidgetRemove = false;
                } else {
                    Constants.WidgetRemove = true;
                }
                RvMyWidget.getAdapter().notifyDataSetChanged();
                break;
            case R.id.IvMyWidgetTrendy:
                System.out.println("_____  _ _ : " + Constants.WidgetRemove);

                Constants.WidgetRemove = false;
                myWidgetLists = new ArrayList<>();
                for (int j = 0; j < Constants.getTrendyWidgetLists().size(); j++) {
                    myWidgetLists.addAll(helper.getWidgetsType(Constants.getTrendyWidgetLists().get(j).getPosition()));
                }
                RvMyWidget.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                RvMyWidget.setAdapter(new MyWidgetAdapter(context, myWidgetLists, this));
                getIsNotFound();
                break;
            case R.id.IvMyWidgetCalendar:
                System.out.println("_____  _ _ : " + Constants.WidgetRemove);

                Constants.WidgetRemove = false;
                myWidgetLists = new ArrayList<>();
                for (int j = 0; j < Constants.getCalendarWidgetLists().size(); j++) {
                    myWidgetLists.addAll(helper.getWidgetsType(Constants.getCalendarWidgetLists().get(j).getPosition()));
                }
                RvMyWidget.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                RvMyWidget.setAdapter(new MyWidgetAdapter(context, myWidgetLists, this));
                getIsNotFound();
                break;
            case R.id.IvMyWidgetWeather:
                System.out.println("_____  _ _ : " + Constants.WidgetRemove);

                Constants.WidgetRemove = false;
                myWidgetLists = new ArrayList<>();
                for (int j = 0; j < Constants.getWeatherWidgetLists().size(); j++) {
                    myWidgetLists.addAll(helper.getWidgetsType(Constants.getWeatherWidgetLists().get(j).getPosition()));
                }
                RvMyWidget.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                RvMyWidget.setAdapter(new MyWidgetAdapter(context, myWidgetLists, this));
                getIsNotFound();
                break;
            case R.id.IvMyWidgetAlarm:
                System.out.println("_____  _ _ : " + Constants.WidgetRemove);

                Constants.WidgetRemove = false;
                myWidgetLists = new ArrayList<>();
                for (int j = 0; j < Constants.getClockWidgetLists().size(); j++) {
                    myWidgetLists.addAll(helper.getWidgetsType(Constants.getClockWidgetLists().get(j).getPosition()));
                }
                RvMyWidget.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                RvMyWidget.setAdapter(new MyWidgetAdapter(context, myWidgetLists, this));
                getIsNotFound();
                break;
            case R.id.IvMyWidgetXPanel:
                System.out.println("_____  _ _ : " + Constants.WidgetRemove);

                Constants.WidgetRemove = false;
                myWidgetLists = new ArrayList<>();
                for (int j = 0; j < Constants.getXPanelWidgetLists().size(); j++) {
                    myWidgetLists.addAll(helper.getWidgetsType(Constants.getXPanelWidgetLists().get(j).getPosition()));
                }
                RvMyWidget.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                RvMyWidget.setAdapter(new MyWidgetAdapter(context, myWidgetLists, this));
                getIsNotFound();
                break;
            case R.id.IvMyWidgetPhoto:

                Constants.WidgetRemove = false;
                myWidgetLists = new ArrayList<>();
                for (int j = 0; j < Constants.getPhotoWidgetLists().size(); j++) {
                    System.out.println("_____  _ _ : " + Constants.getPhotoWidgetLists().get(j).getPosition());
                    myWidgetLists.addAll(helper.getWidgetsType(Constants.getPhotoWidgetLists().get(j).getPosition()));
                }
                System.out.println("_____  _ _ : " + myWidgetLists.size());
                RvMyWidget.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                RvMyWidget.setAdapter(new MyWidgetAdapter(context, myWidgetLists, this));
                getIsNotFound();
                break;
        }
    }

    private void getIsNotFound() {
        if (myWidgetLists.size() > 0) {
            IvNotFoundRecord.setVisibility(View.GONE);
            RvMyWidget.setVisibility(View.VISIBLE);
            iv_done.setVisibility(View.VISIBLE);
        } else {
            IvNotFoundRecord.setVisibility(View.VISIBLE);
            RvMyWidget.setVisibility(View.GONE);
            iv_done.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void DeleteWidget(int position) {
        myWidgetLists.remove(position);
        if (myWidgetLists.size() > 0) {
            IvNotFoundRecord.setVisibility(View.GONE);
            RvMyWidget.setVisibility(View.VISIBLE);
            iv_done.setVisibility(View.VISIBLE);
        } else {
            IvNotFoundRecord.setVisibility(View.VISIBLE);
            RvMyWidget.setVisibility(View.GONE);
            iv_done.setVisibility(View.GONE);
        }
        helper.getDeleteWidgets(myWidgetLists.get(position).getNumber());
        RvMyWidget.getAdapter().notifyDataSetChanged();
    }
}
package com.ios.widget.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ios.widget.Model.WidgetData;
import com.ios.widget.Model.WidgetModel;
import com.ios.widget.R;
import com.ios.widget.helper.DatabaseHelper;
import com.ios.widget.provider.BetteryBroadcastReceiver;
import com.ios.widget.ui.Adapter.MainWidgetListAdapter;
import com.ios.widget.ui.Adapter.TypeImageAdapter;
import com.ios.widget.utils.Constants;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ImageView IvSettings,IvMyWidget;
    private RecyclerView RvTypeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        iniListeners();
        initActions();
    }

    private void initViews() {
        context = this;
        DatabaseHelper helper = new DatabaseHelper(context);
        ArrayList<WidgetData> widgetData = helper.getWidgets();
        System.out.println("---- -- - - zzzzz : "+widgetData.size());
        if (widgetData.size()>0){
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(context, BetteryBroadcastReceiver.class);
            PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, broadcast);
        }
        IvSettings = (ImageView) findViewById(R.id.IvSettings);
        IvMyWidget = (ImageView) findViewById(R.id.IvMyWidget);
        RvTypeList = (RecyclerView) findViewById(R.id.RvTypeList);
    }

    private void iniListeners() {
        IvSettings.setOnClickListener(this);
        IvMyWidget.setOnClickListener(this);
    }

    private void initActions() {
        ArrayList<Integer> TypesArrayList=new ArrayList<>();
        TypesArrayList.add(R.drawable.btn_trendy);
        TypesArrayList.add(R.drawable.btn_calendar);
        TypesArrayList.add(R.drawable.btn_weather);
        TypesArrayList.add(R.drawable.btn_alarm);
        TypesArrayList.add(R.drawable.btn_x_panel);
        TypesArrayList.add(R.drawable.btn_photo);

        RvTypeList.setLayoutManager(new GridLayoutManager(context,2));
        RvTypeList.setAdapter(new TypeImageAdapter(context,TypesArrayList));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.IvSettings:
                startActivity(new Intent(context, SettingActivity.class));
                break;
            case R.id.IvMyWidget:
                startActivity(new Intent(context, MyWidgetsActivity.class));
                break;
        }
    }
}
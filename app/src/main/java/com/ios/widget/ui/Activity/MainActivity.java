package com.ios.widget.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.ios.widget.Model.WidgetModel;
import com.ios.widget.R;
import com.ios.widget.ui.Adapter.MainWidgetListAdapter;
import com.ios.widget.ui.Adapter.TypeImageAdapter;
import com.ios.widget.utils.Constants;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Context context;
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
        RvTypeList = (RecyclerView) findViewById(R.id.RvTypeList);
    }

    private void iniListeners() {

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
}
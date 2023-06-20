package com.ios.widget.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.ios.widget.Model.WidgetModel;
import com.ios.widget.R;
import com.ios.widget.ui.Adapter.MainWidgetListAdapter;
import com.ios.widget.utils.Constants;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView RvWidgetList;
    private ArrayList<WidgetModel> modelArrayList;

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
        RvWidgetList = (RecyclerView) findViewById(R.id.RvWidgetList);
    }

    private void iniListeners() {

    }

    private void initActions() {
        modelArrayList = new ArrayList<>();
        modelArrayList.addAll(Constants.getWidgetLists());

        RvWidgetList.setLayoutManager(new LinearLayoutManager(context));
        RvWidgetList.setAdapter(new MainWidgetListAdapter(context, modelArrayList));
    }
}
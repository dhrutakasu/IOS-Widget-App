package com.ios.widget.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ios.widget.Model.WidgetData;
import com.ios.widget.Model.WidgetModel;
import com.ios.widget.R;
import com.ios.widget.helper.DatabaseHelper;
import com.ios.widget.ui.Adapter.MyWidgetAdapter;
import com.ios.widget.utils.Constants;

import java.util.ArrayList;
import java.util.HashSet;

public class MyWidgetsActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ImageView IvBack;
    private TextView TvTitle;
    private DatabaseHelper helper;
    private RecyclerView RvMyWidget;
    private ArrayList<WidgetData> myWidgetLists = new ArrayList<WidgetData>();
    private ArrayList<WidgetModel> ListOfWidget=new ArrayList<>();

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
        TvTitle = (TextView) findViewById(R.id.TvTitle);
        RvMyWidget = (RecyclerView) findViewById(R.id.RvMyWidget);
    }

    private void iniListeners() {
        IvBack.setOnClickListener(this);
    }

    private void initActions() {
        helper = new DatabaseHelper(context);
        myWidgetLists = helper.getWidgets();
        TvTitle.setText(getResources().getString(R.string.my_widget));
        for (int i = 0; i < myWidgetLists.size(); i++) {
            for (int j = 0; j < Constants.getWidgetLists().size(); j++) {
                if (myWidgetLists.get(i).getPosition()==Constants.getWidgetLists().get(j).getPosition()) {
                    ListOfWidget.add(Constants.getWidgetLists().get(j));
                }
            }
        }
        RvMyWidget.setLayoutManager(new GridLayoutManager(context, 2));
        RvMyWidget.setAdapter(new MyWidgetAdapter(context, myWidgetLists));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.IvBack:
                onBackPressed();
                break;
        }
    }
}
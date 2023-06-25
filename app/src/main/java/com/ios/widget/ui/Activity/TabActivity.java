package com.ios.widget.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.ios.widget.R;
import com.ios.widget.ui.Adapter.CategoryAdapter;
import com.ios.widget.ui.Adapter.DataAdapter;
import com.ios.widget.ui.Adapter.TabAdapter;
import com.ios.widget.utils.Constants;

import java.util.ArrayList;

public class TabActivity extends AppCompatActivity {

    RecyclerView myRecyclerView;
    TabLayout myTabLayout;
    GridLayoutManager linearLayoutManager;
    ArrayList<String> arrayList = new ArrayList<>();
    DataAdapter adapter;
    private boolean isUserScrolling = false;
    private boolean isListGoingUp = true;
    private Context context;
    private RecyclerView RvTabCat,RvCategory;
    private int TabPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        initViews();
        iniIntents();
        iniListeners();
        initActions();
        myTabLayout = findViewById(R.id.myTabLayout);

        myRecyclerView = findViewById(R.id.myRecyclerView);
        linearLayoutManager = new GridLayoutManager(this, 2);
        myRecyclerView.setLayoutManager(linearLayoutManager);
        myRecyclerView.setHasFixedSize(true);

        for (int i = 0; i < 120; i++) {
            arrayList.add("Item " + i);
        }

        for (int i = 0; i < 4; i++) {
            TabLayout.Tab tab1 = myTabLayout.newTab();
            tab1.setText("Tab " + i);
            myTabLayout.addTab(tab1);
        }

        adapter = new DataAdapter(this, arrayList);
        myRecyclerView.setAdapter(adapter);

        myTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isUserScrolling = false;
                int position = tab.getPosition();
                if (position == 0) {
                    myRecyclerView.smoothScrollToPosition(0);
                } else if (position == 1) {
                    myRecyclerView.smoothScrollToPosition(30);
                } else if (position == 2) {
                    myRecyclerView.smoothScrollToPosition(60);
                } else if (position == 3) {
                    myRecyclerView.smoothScrollToPosition(90);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    isUserScrolling = true;
                    if (isListGoingUp) {
                        //my recycler view is actually inverted so I have to write this condition instead
                        if (linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1 == arrayList.size()) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (isListGoingUp) {
                                        if (linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1 == arrayList.size()) {
                                            Toast.makeText(TabActivity.this, "exeute something", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }, 50);
                            //waiting for 50ms because when scrolling down from top, the variable isListGoingUp is still true until the onScrolled method is executed
                        }
                    }
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int itemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                if (isUserScrolling) {
                    if (itemPosition == 0) { //  item position of uses
                        TabLayout.Tab tab = myTabLayout.getTabAt(0);
                        tab.select();
                    } else if (itemPosition == 30) {//  item position of side effects
                        TabLayout.Tab tab = myTabLayout.getTabAt(1);
                        tab.select();
                    } else if (itemPosition == 60) {//  item position of how it works
                        TabLayout.Tab tab = myTabLayout.getTabAt(2);
                        tab.select();
                    } else if (itemPosition == 90) {//  item position of precaution
                        TabLayout.Tab tab = myTabLayout.getTabAt(3);
                        tab.select();
                    }
                }
            }
        });
    }

    private void initViews() {
        context = this;
        RvTabCat = (RecyclerView) findViewById(R.id.RvTabCat);
        RvCategory = (RecyclerView) findViewById(R.id.RvCategory);
    }

    private void iniIntents() {
        TabPos = getIntent().getIntExtra(Constants.TabPos, -1);
    }

    private void iniListeners() {

    }

    private void initActions() {
        ArrayList<Integer> TypesArrayList = Constants.getWidgetCategoryLists();
        RvTabCat.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        RvTabCat.setHasFixedSize(true);
        RvTabCat.setAdapter(new TabAdapter(context, TypesArrayList, new TabAdapter.setClickCategory() {
            @Override
            public void onClickCategory(int pos) {

            }
        }));

        RvCategory.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        RvCategory.setHasFixedSize(true);
//        RvCategory.setAdapter(new CategoryAdapter(context, Constants.getWidgetLists(), new CategoryAdapter.setClickCategory() {
//            @Override
//            public void onClickCategory(int pos) {
//
//            }
//        }));
    }
}
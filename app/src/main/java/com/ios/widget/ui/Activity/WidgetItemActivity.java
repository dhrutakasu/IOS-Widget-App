package com.ios.widget.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.ios.widget.Model.WidgetModel;
import com.ios.widget.R;
import com.ios.widget.provider.NoteCreateWidgetProvider;
import com.ios.widget.provider.NoteWidgetCreatedReceiver;
import com.ios.widget.ui.Adapter.WidgetPagerAdapter;
import com.ios.widget.utils.Constants;

public class WidgetItemActivity extends AppCompatActivity {

    private Context context;
    private int pos;
    private ViewPager PagerWidget;
    private TextView TvAddWidget;

    private Integer[] images;
    private TabLayout TabWidget;
    private WidgetModel widgetModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_item);
        initViews();
        initIntents();
        iniListeners();
        initActions();
    }

    private void initViews() {
        context = this;
        PagerWidget = (ViewPager) findViewById(R.id.PagerWidget);
        TvAddWidget = (TextView) findViewById(R.id.TvAddWidget);
        TabWidget = (TabLayout) findViewById(R.id.TabWidget);
    }

    private void initIntents() {
        pos = getIntent().getIntExtra(Constants.ITEM_POSITION, 0);
    }

    private void iniListeners() {
        TvAddWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    AppWidgetManager manager = (AppWidgetManager) getSystemService(AppWidgetManager.class);
                    ComponentName name = new ComponentName(context, NoteCreateWidgetProvider.class);
                    if (manager != null && manager.isRequestPinAppWidgetSupported()) {
                        new Handler().postDelayed(() -> {
                            int ids;
//                            String title = EdtCreateNoteTitle.getText().toString();
//                            String content = EdtCreateNote.getText().toString();
//                            if (noteActionCode == 0) {
//                                int notesCount = (int) noteHelper.getNotesCount();
//                                if (notesCount != 0) {
//                                    if (!noteHelper.checkRecordExist(title, content)) {
//                                        NoteSave();
//                                        isInserted = true;
//                                    }
//                                } else {
//                                    NoteSave();
//                                    isInserted = true;
//                                }
//                                ids = (int) count;
//                            } else {
//                                ids = (int) NoteId;
//                            }
                            Intent intent = new Intent(context, NoteWidgetCreatedReceiver.class);
//                            intent.putExtra(Constants.TAG_WIDGET_NOTE_TITLE, title);
//                            intent.putExtra(Constants.TAG_WIDGET_NOTE_CONTENT, content);
//                            intent.putExtra(Constants.TAG_WIDGET_NOTE_ID, ids);
                            PendingIntent pendingIntent;
                            pendingIntent = PendingIntent.getBroadcast(
                                    context,
                                    0, intent,
                                    PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                            manager.requestPinAppWidget(name, (Bundle) null, pendingIntent);
                        }, 100);
                    }
                }
            }
        });
    }

    private void initActions() {
        widgetModel = Constants.getWidgetLists().get(pos);
        images = new Integer[]{widgetModel.getSmall(), widgetModel.getMedium(), widgetModel.getLarge()};

        WidgetPagerAdapter adapter = new WidgetPagerAdapter(this, images);
        PagerWidget.setAdapter(adapter);
        TabWidget.setupWithViewPager(PagerWidget, true);
    }
}
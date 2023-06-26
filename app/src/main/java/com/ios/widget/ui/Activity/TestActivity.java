package com.ios.widget.ui.Activity;

import android.os.Bundle;
import android.widget.GridView;

import com.ios.widget.R;
import com.ios.widget.ui.Adapter.WidgtetCalendarAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;
//todo last remove
public class TestActivity extends AppCompatActivity {

    private GridView GridCalendarLargeView,GridCalendarMediumView;
    private WidgtetCalendarAdapter widgtetCalendarAdapter;
    private Calendar calendar;
    private int month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        GridCalendarMediumView = (GridView) this.findViewById(R.id.GridCalendarMediumView);
        GridCalendarLargeView = (GridView) this.findViewById(R.id.GridCalendarLargeView);

        calendar = Calendar.getInstance(Locale.getDefault());
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);

        widgtetCalendarAdapter = new WidgtetCalendarAdapter(getApplicationContext(), month, year,0);
        widgtetCalendarAdapter.notifyDataSetChanged();
        GridCalendarLargeView.setAdapter(widgtetCalendarAdapter);

        widgtetCalendarAdapter = new WidgtetCalendarAdapter(getApplicationContext(), month, year,1);
        widgtetCalendarAdapter.notifyDataSetChanged();
        GridCalendarMediumView.setAdapter(widgtetCalendarAdapter);
    }
}
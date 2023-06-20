package com.ios.widget.ui.Activity;

import android.os.Bundle;
import android.widget.GridView;

import com.ios.widget.R;
import com.ios.widget.ui.Adapter.GridCalendarAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;
public class TestActivity extends AppCompatActivity {

    private GridView GridCalendarLargeView,GridCalendarMediumView;
    private GridCalendarAdapter gridCalendarAdapter;
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

        gridCalendarAdapter = new GridCalendarAdapter(getApplicationContext(), month, year,0);
        gridCalendarAdapter.notifyDataSetChanged();
        GridCalendarLargeView.setAdapter(gridCalendarAdapter);

        gridCalendarAdapter = new GridCalendarAdapter(getApplicationContext(), month, year,1);
        gridCalendarAdapter.notifyDataSetChanged();
        GridCalendarMediumView.setAdapter(gridCalendarAdapter);
    }
}
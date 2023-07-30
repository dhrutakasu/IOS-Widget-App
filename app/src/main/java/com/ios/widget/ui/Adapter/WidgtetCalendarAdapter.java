package com.ios.widget.ui.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ios.widget.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class WidgtetCalendarAdapter extends BaseAdapter {
    private final Context context;
    private final List<String> strings;
    private static final int DAY_OFFSET = 1;
    private final String[] monthsList = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private final int IsMedium;
    private int daysInMonth;
    private int currentDayOfMonth;
    private int currentWeekDay;
    private TextView TvDates;
    private RelativeLayout RlMainCalendar;

    public WidgtetCalendarAdapter(Context context, int month, int year, int i) {
        super();
        this.context = context;
        this.IsMedium = i;
        this.strings = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        GotoCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
        GotoCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

        GotoMonthDetail(month, year);
    }

    private String getMonthAsString(int i) {
        return monthsList[i];
    }

    private int getNumberOfDaysOfMonth(int i) {
        return daysOfMonth[i];
    }

    public String getItem(int position) {
        return strings.get(position);
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    private void GotoMonthDetail(int mm, int yy) {
        int trailingSpaces = 0;
        int daysInPrevMonth = 0;
        int prevMonth = 0;
        int prevYear = 0;
        int nextMonth = 0;
        int nextYear = 0;

        int currentMonth = mm - 1;
        daysInMonth = getNumberOfDaysOfMonth(currentMonth);

        GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);

        if (currentMonth == 11) {
            prevMonth = currentMonth - 1;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 0;
            prevYear = yy;
            nextYear = yy + 1;
        } else if (currentMonth == 0) {
            prevMonth = 11;
            prevYear = yy - 1;
            nextYear = yy;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 1;
        } else {
            prevMonth = currentMonth - 1;
            nextMonth = currentMonth + 1;
            nextYear = yy;
            prevYear = yy;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
        }

        int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
        trailingSpaces = currentWeekDay;

        if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1) {
            ++daysInMonth;
        }

        for (int i = 0; i < trailingSpaces; i++) {
            strings.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
        }

        for (int i = 1; i <= daysInMonth; i++) {
            if (i == getCurrentDayOfMonth())
                strings.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
            else
                strings.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
        }

        for (int i = 0; i < strings.size() % 7; i++) {
            strings.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_calendar_day, parent, false);
        }

        TvDates = (TextView) view.findViewById(R.id.TvCalendarDates);

        String[] DayOfString = strings.get(position).split("-");
        String Day = DayOfString[0];
        String Month = DayOfString[2];
        String Year = DayOfString[3];

        if (!DayOfString[1].equals("GREY")) {
            TvDates.setText(Day);
        }
        TvDates.setTag(Day + "-" + Month + "-" + Year);
        if (IsMedium == 0) {
            TvDates.setTextSize(11f);
        } else {
            TvDates.setTextSize(10f);
        }
        if (DayOfString[1].equals("WHITE")) {
            TvDates.setTextColor(context.getResources().getColor(R.color.light_brown));
        }
        if (DayOfString[1].equals("BLUE")) {
            TvDates.setPadding(5, 5, 5, 5);
            TvDates.setTextColor(context.getResources().getColor(R.color.white));
        }
        return view;
    }

    public int getCurrentDayOfMonth() {
        return currentDayOfMonth;
    }

    private void GotoCurrentDayOfMonth(int currentDayOfMonth) {
        this.currentDayOfMonth = currentDayOfMonth;
    }

    public void GotoCurrentWeekDay(int currentWeekDay) {
        this.currentWeekDay = currentWeekDay;
    }

    public int getCurrentWeekDay() {
        return currentWeekDay;
    }
}
package com.ios.widget.Appprovider;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ios.widget.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class LargeRemoteView implements RemoteViewsService.RemoteViewsFactory {
    private Calendar calendar;
    private Context context;
    private int NoteId;

    private List<String> strings = new ArrayList<String>();
    private static final int DAY_OFFSET = 1;
    private final String[] monthsList = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private int daysInMonth;
    private int currentDayOfMonth;
    private int currentWeekDay;
    private int month, year;

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public long getItemId(int i) {
        return (long) 0;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
        strings.clear();
        calendar = Calendar.getInstance(Locale.getDefault());
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);

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

    public int getCurrentDayOfMonth() {
        return currentDayOfMonth;
    }

    private void GotoCurrentDayOfMonth(int currentDayOfMonth) {
        this.currentDayOfMonth = currentDayOfMonth;
    }

    public void GotoCurrentWeekDay(int currentWeekDay) {
        this.currentWeekDay = currentWeekDay;
    }

    @Override
    public void onDataSetChanged() {
        strings.clear();

        calendar = Calendar.getInstance(Locale.getDefault());
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        Calendar calendar = Calendar.getInstance();
        GotoCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
        GotoCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

        GotoMonthDetail(month, year);
    }

    public LargeRemoteView(Context con, Intent intent) {
        NoteId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        NoteId = intent.getIntExtra("TypeId", -1);
        context = con;
    }

    @Override
    public void onDestroy() {
        if (context != null) {
            context = null;
        }
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.item_calendar_day_large);

        String[] DayOfString = strings.get(i).split("-");
        String Day = DayOfString[0];
        String Month = DayOfString[2];
        String Year = DayOfString[3];
        if (!DayOfString[1].equals("GREY")) {
            views.setTextViewText(R.id.TvCalendarDates, Day);
        } else {
            views.setTextViewText(R.id.TvCalendarDates, "");
        }
        switch (NoteId) {
            case 5:
            case 7:
                views.setTextViewTextSize(R.id.TvCalendarDates, TypedValue.COMPLEX_UNIT_SP, 11f);
                break;
            case 6:
                views.setTextViewTextSize(R.id.TvCalendarDates, TypedValue.COMPLEX_UNIT_SP, 12f);
                break;
        }
        if (DayOfString[1].equals("WHITE")) {
            switch (NoteId) {
                case 4:
                    views.setViewPadding(R.id.TvCalendarDates, 1, 1, 1, 1);
                    views.setTextViewTextSize(R.id.TvCalendarDates, TypedValue.COMPLEX_UNIT_SP, 8f);
                    views.setTextColor(R.id.TvCalendarDates, context.getResources().getColor(R.color.widget_white));
                    break;
               case 6:
                    views.setViewPadding(R.id.TvCalendarDates, 1, 1, 1, 1);
                    views.setTextViewTextSize(R.id.TvCalendarDates, TypedValue.COMPLEX_UNIT_SP, 18f);
                    views.setTextColor(R.id.TvCalendarDates, context.getResources().getColor(R.color.widget_white));
                    break;
                case 7:
                    views.setViewPadding(R.id.TvCalendarDates, 1, 1, 1, 1);
                    views.setTextViewTextSize(R.id.TvCalendarDates, TypedValue.COMPLEX_UNIT_SP, 16f);
                    views.setTextColor(R.id.TvCalendarDates, context.getResources().getColor(R.color.widget_white));
                    break;
            }
            views.setInt(R.id.TvCalendarDates, "setBackgroundResource", 0);
        }
        if (DayOfString[1].equals("BLUE")) {
            switch (NoteId) {
                case 4:
                    views.setInt(R.id.TvCalendarDates, "setBackgroundResource", R.drawable.ic_calendar_ring_white);
                    views.setTextColor(R.id.TvCalendarDates, context.getResources().getColor(R.color.widget_color_0263FF));
                    break;
                case 6:
                    views.setInt(R.id.TvCalendarDates, "setBackgroundResource", R.drawable.ic_calendar_purple_large);
                    views.setTextColor(R.id.TvCalendarDates, context.getResources().getColor(R.color.widget_white));
                    break;
                case 7:
                    views.setInt(R.id.TvCalendarDates, "setBackgroundResource", R.drawable.ic_calendar_square_large);
                    views.setTextColor(R.id.TvCalendarDates, context.getResources().getColor(R.color.widget_white));
                    break;
            }
        } else {
            views.setViewVisibility(R.id.IvMainCalendar, View.GONE);
        }
        return views;
    }
}

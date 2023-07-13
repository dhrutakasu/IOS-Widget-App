package com.ios.widget.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ios.widget.Model.WidgetData;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Define your database name and version
    private static final String DATABASE_NAME = "WidgetDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "Widget";
    private static final String WIDGET_ID = "WId";
    private static final String WIDGET_SMALL = "WSmall";
    private static final String WIDGET_MEDIUM = "WMedium";
    private static final String WIDGET_LARGE = "WLarge";
    private static final String WIDGET_TYPE = "WType";
    private static final String WIDGET_POSITION = "WPosition";
    private static final String WIDGET_NUMBER = "WNumber";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                WIDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WIDGET_SMALL + " TEXT," +
                WIDGET_MEDIUM + " TEXT," +
                WIDGET_LARGE + " TEXT," +
                WIDGET_TYPE + " TEXT," +
                WIDGET_POSITION + " TEXT," +
                WIDGET_NUMBER + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }

    //todo Widgets insert
    public int InsertWidget(WidgetData widgetData) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WIDGET_SMALL, widgetData.getSmall());
        values.put(WIDGET_MEDIUM, widgetData.getMedium());
        values.put(WIDGET_LARGE, widgetData.getLarge());
        values.put(WIDGET_TYPE, widgetData.getType());
        values.put(WIDGET_POSITION, widgetData.getPosition());
        values.put(WIDGET_NUMBER, widgetData.getNumber());

       return (int) db.insert(TABLE_NAME, null, values);
    }

    //todo Widgets update
    public boolean updateWidget(WidgetData widgetData) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WIDGET_ID, widgetData.getId());
        values.put(WIDGET_TYPE, widgetData.getType());
        values.put(WIDGET_POSITION, widgetData.getPosition());
        values.put(WIDGET_NUMBER, widgetData.getNumber());

        db.update(TABLE_NAME, values, WIDGET_ID + " = ?", new String[]{String.valueOf(widgetData.getId())});
        return true;
    }

    //todo get Widget data
    public ArrayList<WidgetData> getWidgets() {
        ArrayList<WidgetData> widgetDataArrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String table_name = "SELECT *FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(table_name, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") WidgetData widgetData = new WidgetData(cursor.getInt(cursor.getColumnIndex(WIDGET_TYPE))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_POSITION))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_NUMBER)));

//                @SuppressLint("Range") WidgetData widgetData = new WidgetData(cursor.getString(cursor.getColumnIndex(WIDGET_ID))
//                        , cursor.getString(cursor.getColumnIndex(WIDGET_TYPE))
//                        , cursor.getInt(cursor.getColumnIndex(WIDGET_SMALL))
//                        , cursor.getInt(cursor.getColumnIndex(WIDGET_MEDIUM))
//                        , cursor.getInt(cursor.getColumnIndex(WIDGET_LARGE))
//                        , cursor.getString(cursor.getColumnIndex(WIDGET_POSITION))
//                        , cursor.getString(cursor.getColumnIndex(WIDGET_NUMBER)));
                widgetDataArrayList.add(widgetData);
            } while (cursor.moveToNext());
        }
        return widgetDataArrayList;
    }

    //todo get Specific Widget data
    @SuppressLint("Range")
    public WidgetData getWidgetsId(int index) {
        WidgetData widgetDataArrayList = null;
        SQLiteDatabase db = getReadableDatabase();
        String table_name = "SELECT *FROM " + TABLE_NAME  + " WHERE " + WIDGET_ID + "=? ";
        Cursor cursor = db.rawQuery(table_name, new String[]{String.valueOf(index)});
        if (cursor.moveToFirst()) {
            do {
//                @SuppressLint("Range") WidgetData widgetData = new WidgetData(cursor.getString(cursor.getColumnIndex(WIDGET_TYPE))
//                        , cursor.getString(cursor.getColumnIndex(WIDGET_POSITION))
//                        , cursor.getString(cursor.getColumnIndex(WIDGET_NUMBER)));

                widgetDataArrayList = new WidgetData(cursor.getInt(cursor.getColumnIndex(WIDGET_ID))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_TYPE))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_SMALL))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_MEDIUM))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_LARGE))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_POSITION))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_NUMBER)));
//                widgetDataArrayList.add(widgetData);
//                widgetDataArrayList = widgetData;
            } while (cursor.moveToNext());
        }
        return widgetDataArrayList;
    }

    //todo get count widgets record
    public int getWidgetCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    //todo get count widgets record
    public void getDeleteWidgets() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        cursor.moveToFirst();
    }


}
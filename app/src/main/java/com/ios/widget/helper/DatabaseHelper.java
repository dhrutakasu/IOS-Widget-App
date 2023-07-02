package com.ios.widget.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
    public void InsertWidget(WidgetData widgetData) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WIDGET_TYPE, widgetData.getType());
        values.put(WIDGET_POSITION, widgetData.getPosition());
        values.put(WIDGET_NUMBER, widgetData.getNumber());

        db.insert(TABLE_NAME, null, values);
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
                @SuppressLint("Range") WidgetData widgetData = new WidgetData(cursor.getString(cursor.getColumnIndex(WIDGET_ID))
                        , cursor.getString(cursor.getColumnIndex(WIDGET_TYPE))
                        , cursor.getString(cursor.getColumnIndex(WIDGET_POSITION))
                        , cursor.getString(cursor.getColumnIndex(WIDGET_NUMBER)));
                widgetDataArrayList.add(widgetData);
            } while (cursor.moveToNext());
        }
        return widgetDataArrayList;
    }
}
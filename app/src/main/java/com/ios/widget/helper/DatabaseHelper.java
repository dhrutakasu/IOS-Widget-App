package com.ios.widget.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ios.widget.Model.WidgetData;
import com.ios.widget.Model.WidgetImages;
import com.ios.widget.Model.WidgetMaster;

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



    private static final String WIDGET_MASTER_TABLE_NAME = "WidgetMaster";
    private static final String WIDGET_MASTER_Loading_Indicator = "WMasterLoadingIndicator";
    private static final String WIDGET_MASTER_colorCode = "WMasterColorCode";
    private static final String WIDGET_MASTER_column = "WMatserColumn";
    private static final String WIDGET_MASTER_cornerBorder = "WMasterCornerBorder";
    private static final String WIDGET_MASTER_cropType = "WMasterCropType";
    private static final String WIDGET_MASTER_customMode = "WMasterCustomMode";
    private static final String WIDGET_MASTER_flipControl = "WMasterFlipControl";
    private static final String WIDGET_MASTER_interval = "WMasterInterval";
    private static final String WIDGET_MASTER_opacity = "WMasterOpacity";
    private static final String WIDGET_MASTER_rotationType = "WMasterRotationType";
    private static final String WIDGET_MASTER_row = "WMasterRow";
    private static final String WIDGET_MASTER_shape = "WMasterShape";
    private static final String WIDGET_MASTER_size = "WMasterSize";
    private static final String WIDGET_MASTER_spaceBorder = "WMasterSpaceBorder";
    private static final String WIDGET_MASTER_widgetId = "WMasterWidgetId";
    private static final String WIDGET_MASTER_Id = "WMasterId";


    private static final String WIDGET_IMAGES_TABLE_NAME = "WidgetImages";
    private static final String WIDGET_IMAGES_Id = "WImagesId";
    private static final String WIDGET_IMAGES_Uri = "WImageUri";
    private static final String WIDGET_IMAGES_Widget_ID = "WImageWidgetId";

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

        String WidgetTableQuery = "CREATE TABLE " + WIDGET_MASTER_TABLE_NAME + " (" +
                WIDGET_MASTER_Id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WIDGET_MASTER_widgetId + " TEXT," +
                WIDGET_MASTER_spaceBorder + " TEXT," +
                WIDGET_MASTER_size + " TEXT," +
                WIDGET_MASTER_shape + " TEXT," +
                WIDGET_MASTER_row + " TEXT," +
                WIDGET_MASTER_rotationType + " TEXT," +
                WIDGET_MASTER_opacity + " TEXT," +
                WIDGET_MASTER_interval + " TEXT," +
                WIDGET_MASTER_flipControl + " TEXT," +
                WIDGET_MASTER_customMode + " TEXT," +
                WIDGET_MASTER_cropType + " TEXT," +
                WIDGET_MASTER_cornerBorder + " TEXT," +
                WIDGET_MASTER_column + " TEXT," +
                WIDGET_MASTER_colorCode + " TEXT," +
                WIDGET_MASTER_Loading_Indicator + " TEXT)";
        db.execSQL(WidgetTableQuery);

        String WidgetImageTableQuery = "CREATE TABLE " + WIDGET_IMAGES_TABLE_NAME + " (" +
                WIDGET_IMAGES_Id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WIDGET_IMAGES_Uri + " TEXT," +
                WIDGET_IMAGES_Widget_ID + " TEXT)";
        db.execSQL(WidgetImageTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropTableQuery);
        String dropWidgetTableQuery = "DROP TABLE IF EXISTS " + WIDGET_MASTER_TABLE_NAME;
        db.execSQL(dropWidgetTableQuery);
        String dropWidgetImageTableQuery = "DROP TABLE IF EXISTS " + WIDGET_IMAGES_TABLE_NAME;
        db.execSQL(dropWidgetImageTableQuery);
        onCreate(db);
    }

    //todo Widgets insert
    public int InsertWidget(WidgetData widgetData) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WIDGET_TYPE, widgetData.getType());
        values.put(WIDGET_POSITION, widgetData.getPosition());
        values.put(WIDGET_NUMBER, widgetData.getNumber());

        return (int) db.insert(TABLE_NAME, null, values);
    }

    //todo WidgetMaster insert
    public void InsertWidget(WidgetMaster widgetMaster) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WIDGET_MASTER_widgetId, widgetMaster.getWidgetId());
        values.put(WIDGET_MASTER_spaceBorder, widgetMaster.getSpaceBorder());
        values.put(WIDGET_MASTER_size, widgetMaster.getSize());
        values.put(WIDGET_MASTER_shape, widgetMaster.getShape());
        values.put(WIDGET_MASTER_row, widgetMaster.getRow());
        values.put(WIDGET_MASTER_rotationType, widgetMaster.getRotationType());
        values.put(WIDGET_MASTER_opacity, widgetMaster.getOpacity());
        values.put(WIDGET_MASTER_interval, widgetMaster.getInterval());
        values.put(WIDGET_MASTER_flipControl, widgetMaster.isFlipControl() == true ? 1 : 0);
        values.put(WIDGET_MASTER_customMode, widgetMaster.isCustomMode() == true ? 1 : 0);
        values.put(WIDGET_MASTER_cropType, widgetMaster.getCropType());
        values.put(WIDGET_MASTER_cornerBorder, widgetMaster.getCornerBorder());
        values.put(WIDGET_MASTER_column, widgetMaster.getColumn());
        values.put(WIDGET_MASTER_colorCode, widgetMaster.getColorCode());
        values.put(WIDGET_MASTER_Loading_Indicator, widgetMaster.isLoadingIndicator() == true ? 1 : 0);

        db.insert(WIDGET_MASTER_TABLE_NAME, null, values);
    }

    //todo WidgetImages insert
    public void InsertWidgetImage(WidgetImages widgetImages) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WIDGET_IMAGES_Uri, widgetImages.getUri());
        values.put(WIDGET_IMAGES_Widget_ID, widgetImages.getWidgetId());

        db.insert(WIDGET_IMAGES_TABLE_NAME, null, values);
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

    //todo WidgetMaster update
    public boolean updateWidgetMaster(WidgetMaster widgetMaster) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WIDGET_MASTER_Id, widgetMaster.getId());
        values.put(WIDGET_MASTER_widgetId, widgetMaster.getWidgetId());
        values.put(WIDGET_MASTER_spaceBorder, widgetMaster.getSpaceBorder());
        values.put(WIDGET_MASTER_size, widgetMaster.getSize());
        values.put(WIDGET_MASTER_shape, widgetMaster.getShape());
        values.put(WIDGET_MASTER_row, widgetMaster.getRow());
        values.put(WIDGET_MASTER_rotationType, widgetMaster.getRotationType());
        values.put(WIDGET_MASTER_opacity, widgetMaster.getOpacity());
        values.put(WIDGET_MASTER_interval, widgetMaster.getInterval());
        values.put(WIDGET_MASTER_flipControl, widgetMaster.isFlipControl());
        values.put(WIDGET_MASTER_customMode, widgetMaster.isCustomMode());
        values.put(WIDGET_MASTER_cropType, widgetMaster.getCropType());
        values.put(WIDGET_MASTER_cornerBorder, widgetMaster.getCornerBorder());
        values.put(WIDGET_MASTER_column, widgetMaster.getColumn());
        values.put(WIDGET_MASTER_colorCode, widgetMaster.getColorCode());
        values.put(WIDGET_MASTER_Loading_Indicator, widgetMaster.isLoadingIndicator());

        db.update(WIDGET_MASTER_TABLE_NAME, values, WIDGET_MASTER_widgetId + " = ?", new String[]{String.valueOf(widgetMaster.getWidgetId())});
        return true;
    }

    //todo WidgetsImages update
    public boolean updateWidgetImages(WidgetImages widgetImages) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WIDGET_IMAGES_Id, widgetImages.getImageId());
        values.put(WIDGET_IMAGES_Uri, widgetImages.getUri());
        values.put(WIDGET_IMAGES_Widget_ID, widgetImages.getWidgetId());

        db.update(WIDGET_IMAGES_TABLE_NAME, values, WIDGET_IMAGES_Id + " = ?", new String[]{String.valueOf(widgetImages.getImageId())});
        return true;
    }

    //todo get Specific Widget data
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

    //todo get Widget data
    @SuppressLint("Range")
    public WidgetData getWidgetsId(int index) {
        WidgetData widgetDataArrayList = null;
        SQLiteDatabase db = getReadableDatabase();
        String table_name = "SELECT *FROM " + TABLE_NAME + " WHERE " + WIDGET_ID + "=? ";
        Cursor cursor = db.rawQuery(table_name, new String[]{String.valueOf(index)});
        if (cursor.moveToFirst()) {
            do {
//                @SuppressLint("Range") WidgetData widgetData = new WidgetData(cursor.getString(cursor.getColumnIndex(WIDGET_TYPE))
//                        , cursor.getString(cursor.getColumnIndex(WIDGET_POSITION))
//                        , cursor.getString(cursor.getColumnIndex(WIDGET_NUMBER)));

                widgetDataArrayList = new WidgetData(cursor.getInt(cursor.getColumnIndex(WIDGET_ID))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_TYPE))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_POSITION))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_NUMBER)));
//                widgetDataArrayList.add(widgetData);
//                widgetDataArrayList = widgetData;
            } while (cursor.moveToNext());
        }
        return widgetDataArrayList;
    }

    //todo get Widget Id data
    @SuppressLint("Range")
    public WidgetData getWidgetsNumber(int index) {
        WidgetData widgetDataArrayList = null;
        SQLiteDatabase db = getReadableDatabase();
        String table_name = "SELECT *FROM " + TABLE_NAME + " WHERE " + WIDGET_NUMBER + "=? ";
        Cursor cursor = db.rawQuery(table_name, new String[]{String.valueOf(index)});
        if (cursor.moveToFirst()) {
            do {
//                @SuppressLint("Range") WidgetData widgetData = new WidgetData(cursor.getString(cursor.getColumnIndex(WIDGET_TYPE))
//                        , cursor.getString(cursor.getColumnIndex(WIDGET_POSITION))
//                        , cursor.getString(cursor.getColumnIndex(WIDGET_NUMBER)));

                widgetDataArrayList = new WidgetData(cursor.getInt(cursor.getColumnIndex(WIDGET_ID))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_TYPE))
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

    //todo delete Widget data
    public void getDeleteWidgets(int index) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME+ " WHERE " + WIDGET_NUMBER + "=? ", new String[]{String.valueOf(index)});
        cursor.moveToFirst();
    }

    //todo get WidgetMaster data
    @SuppressLint("Range")
    public WidgetMaster getWidgetMaster(int i3) {
        WidgetMaster widgetDataArrayList = null;
        SQLiteDatabase db = getReadableDatabase();
        String table_name = "SELECT * FROM " + WIDGET_MASTER_TABLE_NAME + " where " + WIDGET_MASTER_widgetId + "=?";
        Cursor cursor = db.rawQuery(table_name, new String[]{String.valueOf(i3)});
        if (cursor.moveToFirst()) {
            do {
                widgetDataArrayList = new WidgetMaster();
                widgetDataArrayList.setId(cursor.getInt(cursor.getColumnIndex(WIDGET_MASTER_Id)));
                widgetDataArrayList.setWidgetId(cursor.getInt(cursor.getColumnIndex(WIDGET_MASTER_widgetId)));
                widgetDataArrayList.setSpaceBorder(cursor.getInt(cursor.getColumnIndex(WIDGET_MASTER_spaceBorder)));
                widgetDataArrayList.setSize(cursor.getInt(cursor.getColumnIndex(WIDGET_MASTER_size)));
                widgetDataArrayList.setShape(cursor.getInt(cursor.getColumnIndex(WIDGET_MASTER_shape)));
                widgetDataArrayList.setRow(cursor.getInt(cursor.getColumnIndex(WIDGET_MASTER_row)));
                widgetDataArrayList.setRotationType(cursor.getInt(cursor.getColumnIndex(WIDGET_MASTER_rotationType)));
                widgetDataArrayList.setOpacity(cursor.getInt(cursor.getColumnIndex(WIDGET_MASTER_opacity)));
                widgetDataArrayList.setInterval(cursor.getInt(cursor.getColumnIndex(WIDGET_MASTER_interval)));
                widgetDataArrayList.setFlipControl(cursor.getInt(cursor.getColumnIndex(WIDGET_MASTER_flipControl)) == 1 ? true : false);
                widgetDataArrayList.setCustomMode(cursor.getInt(cursor.getColumnIndex(WIDGET_MASTER_customMode)) == 1 ? true : false);
                widgetDataArrayList.setCropType(cursor.getInt(cursor.getColumnIndex(WIDGET_MASTER_cropType)));
                widgetDataArrayList.setCornerBorder(cursor.getInt(cursor.getColumnIndex(WIDGET_MASTER_cornerBorder)));
                widgetDataArrayList.setColumn(cursor.getInt(cursor.getColumnIndex(WIDGET_MASTER_column)));
                widgetDataArrayList.setColorCode(cursor.getInt(cursor.getColumnIndex(WIDGET_MASTER_colorCode)));

                widgetDataArrayList.setLoadingIndicator(cursor.getInt(cursor.getColumnIndex(WIDGET_MASTER_Loading_Indicator)) == 1 ? true : false);
            } while (cursor.moveToNext());
        }
        return widgetDataArrayList;
    }

    //todo get WidgetImages data
    @SuppressLint("Range")
    public ArrayList<WidgetImages> getImageList(int i3) {
        ArrayList<WidgetImages> widgetDataArrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String table_name = "SELECT * FROM " + WIDGET_IMAGES_TABLE_NAME + " where " + WIDGET_IMAGES_Widget_ID + "=?";
        Cursor cursor = db.rawQuery(table_name, new String[]{String.valueOf(i3)});
        if (cursor.moveToFirst()) {
            do {
                WidgetImages widgetDataArray = new WidgetImages(
                        cursor.getString(cursor.getColumnIndex(WIDGET_IMAGES_Id)),
                        cursor.getString(cursor.getColumnIndex(WIDGET_IMAGES_Uri)),
                        cursor.getInt(cursor.getColumnIndex(WIDGET_IMAGES_Widget_ID)));

                widgetDataArrayList.add(widgetDataArray);
            } while (cursor.moveToNext());
        }
        return widgetDataArrayList;
    }

    //todo get WidgetImages data
    @SuppressLint("Range")
    public WidgetImages getImageListData(int i3) {
        WidgetImages widgetImages = null;
        SQLiteDatabase db = getReadableDatabase();
        String table_name = "SELECT *FROM " + WIDGET_IMAGES_TABLE_NAME + " where " + WIDGET_IMAGES_Widget_ID + "=?";
        Cursor cursor = db.rawQuery(table_name, new String[]{String.valueOf(i3)});
        if (cursor.moveToFirst()) {
            do {
                widgetImages = new WidgetImages(
                        cursor.getString(cursor.getColumnIndex(WIDGET_IMAGES_Id)),
                        cursor.getString(cursor.getColumnIndex(WIDGET_IMAGES_Uri)),
                        cursor.getInt(cursor.getColumnIndex(WIDGET_IMAGES_Widget_ID)));

            } while (cursor.moveToNext());
        }
        return widgetImages;
    }

    //todo exist or not
    public boolean CheckIsAlreadyDBorNot(String fieldValue, String fieldValue1) {
        SQLiteDatabase sqldb = getReadableDatabase();
        String Query = "Select * from " + WIDGET_IMAGES_TABLE_NAME + " where " + WIDGET_IMAGES_Uri + " = '" + fieldValue + "' And " + WIDGET_IMAGES_Widget_ID + " = " + fieldValue1;
        Cursor cursor = sqldb.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    //todo exist or not
    public boolean CheckIsAlreadyMasterOrNot(String fieldValue) {
        SQLiteDatabase sqldb = getReadableDatabase();
        String Query = "Select * from " + WIDGET_MASTER_TABLE_NAME + " where " + WIDGET_MASTER_widgetId + " = " + fieldValue;
        Cursor cursor = sqldb.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
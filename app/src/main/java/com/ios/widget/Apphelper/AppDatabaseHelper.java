package com.ios.widget.Apphelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ios.widget.ImageModel.AppWidgetData;
import com.ios.widget.ImageModel.AppWidgetImages;
import com.ios.widget.ImageModel.AppWidgetMaster;

import java.util.ArrayList;

public class AppDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "WidgetDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "Widget";
    private static final String WIDGET_ID = "WId";
    private static final String WIDGET_TYPE = "WType";
    private static final String WIDGET_POSITION = "WPosition";
    private static final String WIDGET_NUMBER = "WNumber";
    private static final String WIDGET_CITY = "WCity";
    private static final String WIDGET_TEMP = "WTemp";
    private static final String WIDGET_SIM = "WSim";


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

    public AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                WIDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WIDGET_TYPE + " TEXT," +
                WIDGET_POSITION + " INTEGER," +
                WIDGET_CITY + " TEXT," +
                WIDGET_TEMP + " INTEGER," +
                WIDGET_SIM + " INTEGER," +
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
    public int InsertWidget(AppWidgetData appWidgetData) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WIDGET_TYPE, appWidgetData.getType());
        values.put(WIDGET_POSITION, appWidgetData.getPosition());
        values.put(WIDGET_CITY, appWidgetData.getCity());
        int sim;
        if (appWidgetData.getSim()) {
            sim = 0;
        } else {
            sim = 1;
        }
        values.put(WIDGET_SIM, sim);
        values.put(WIDGET_NUMBER, appWidgetData.getNumber());

        return (int) db.insert(TABLE_NAME, null, values);
    }

    //todo WidgetMaster insert
    public void InsertWidget(AppWidgetMaster appWidgetMaster) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WIDGET_MASTER_widgetId, appWidgetMaster.getWidgetId());
        values.put(WIDGET_MASTER_spaceBorder, appWidgetMaster.getSpaceBorder());
        values.put(WIDGET_MASTER_size, appWidgetMaster.getSize());
        values.put(WIDGET_MASTER_shape, appWidgetMaster.getShape());
        values.put(WIDGET_MASTER_row, appWidgetMaster.getRow());
        values.put(WIDGET_MASTER_rotationType, appWidgetMaster.getRotationType());
        values.put(WIDGET_MASTER_opacity, appWidgetMaster.getOpacity());
        values.put(WIDGET_MASTER_interval, appWidgetMaster.getInterval());
        values.put(WIDGET_MASTER_flipControl, appWidgetMaster.isFlipControl() == true ? 1 : 0);
        values.put(WIDGET_MASTER_customMode, appWidgetMaster.isCustomMode() == true ? 1 : 0);
        values.put(WIDGET_MASTER_cropType, appWidgetMaster.getCropType());
        values.put(WIDGET_MASTER_cornerBorder, appWidgetMaster.getCornerBorder());
        values.put(WIDGET_MASTER_column, appWidgetMaster.getColumn());
        values.put(WIDGET_MASTER_colorCode, appWidgetMaster.getColorCode());
        values.put(WIDGET_MASTER_Loading_Indicator, appWidgetMaster.isLoadingIndicator() == true ? 1 : 0);

        db.insert(WIDGET_MASTER_TABLE_NAME, null, values);
    }

    //todo WidgetImages insert
    public void InsertWidgetImage(AppWidgetImages appWidgetImages) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WIDGET_IMAGES_Uri, appWidgetImages.getUri());
        values.put(WIDGET_IMAGES_Widget_ID, appWidgetImages.getWidgetId());

        db.insert(WIDGET_IMAGES_TABLE_NAME, null, values);
    }

    //todo Widgets update
    public boolean updateWidget(AppWidgetData appWidgetData) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WIDGET_ID, appWidgetData.getId());
        values.put(WIDGET_TYPE, appWidgetData.getType());
        values.put(WIDGET_POSITION, appWidgetData.getPosition());
        values.put(WIDGET_CITY, appWidgetData.getCity());
        values.put(WIDGET_TEMP, appWidgetData.getTemp());
        int sim;
        if (appWidgetData.getSim()) {
            sim = 0;
        } else {
            sim = 1;
        }
        values.put(WIDGET_SIM, sim);
        values.put(WIDGET_NUMBER, appWidgetData.getNumber());

        db.update(TABLE_NAME, values, WIDGET_ID + " = ?", new String[]{String.valueOf(appWidgetData.getId())});
        return true;
    }

    //todo WidgetMaster update
    public boolean updateWidgetMaster(AppWidgetMaster appWidgetMaster) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WIDGET_MASTER_Id, appWidgetMaster.getId());
        values.put(WIDGET_MASTER_widgetId, appWidgetMaster.getWidgetId());
        values.put(WIDGET_MASTER_spaceBorder, appWidgetMaster.getSpaceBorder());
        values.put(WIDGET_MASTER_size, appWidgetMaster.getSize());
        values.put(WIDGET_MASTER_shape, appWidgetMaster.getShape());
        values.put(WIDGET_MASTER_row, appWidgetMaster.getRow());
        values.put(WIDGET_MASTER_rotationType, appWidgetMaster.getRotationType());
        values.put(WIDGET_MASTER_opacity, appWidgetMaster.getOpacity());
        values.put(WIDGET_MASTER_interval, appWidgetMaster.getInterval());
        values.put(WIDGET_MASTER_flipControl, appWidgetMaster.isFlipControl());
        values.put(WIDGET_MASTER_customMode, appWidgetMaster.isCustomMode());
        values.put(WIDGET_MASTER_cropType, appWidgetMaster.getCropType());
        values.put(WIDGET_MASTER_cornerBorder, appWidgetMaster.getCornerBorder());
        values.put(WIDGET_MASTER_column, appWidgetMaster.getColumn());
        values.put(WIDGET_MASTER_colorCode, appWidgetMaster.getColorCode());
        values.put(WIDGET_MASTER_Loading_Indicator, appWidgetMaster.isLoadingIndicator());

        db.update(WIDGET_MASTER_TABLE_NAME, values, WIDGET_MASTER_widgetId + " = ?", new String[]{String.valueOf(appWidgetMaster.getWidgetId())});
        return true;
    }

    //todo WidgetsImages update
    public boolean updateWidgetImages(AppWidgetImages appWidgetImages) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WIDGET_IMAGES_Id, appWidgetImages.getImageId());
        values.put(WIDGET_IMAGES_Uri, appWidgetImages.getUri());
        values.put(WIDGET_IMAGES_Widget_ID, appWidgetImages.getWidgetId());

        db.update(WIDGET_IMAGES_TABLE_NAME, values, WIDGET_IMAGES_Id + " = ?", new String[]{String.valueOf(appWidgetImages.getImageId())});
        return true;
    }

    //todo get Specific Widget data
    @SuppressLint("Range")
    public ArrayList<AppWidgetData> getWidgets() {
        ArrayList<AppWidgetData> appWidgetDataArrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String table_name = "SELECT *FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(table_name, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    boolean sim;
                    if (cursor.getInt(cursor.getColumnIndex(WIDGET_SIM)) == 0) {
                        sim = true;
                    } else {
                        sim = false;
                    }
                    @SuppressLint("Range") AppWidgetData appWidgetData = new AppWidgetData(cursor.getInt(cursor.getColumnIndex(WIDGET_TYPE))
                            , cursor.getInt(cursor.getColumnIndex(WIDGET_POSITION))
                            , cursor.getInt(cursor.getColumnIndex(WIDGET_NUMBER))
                            , cursor.getString(cursor.getColumnIndex(WIDGET_CITY))
                            , cursor.getInt(cursor.getColumnIndex(WIDGET_TEMP))
                            , sim);

                    appWidgetDataArrayList.add(appWidgetData);
                } while (cursor.moveToNext());
            }
        }
        return appWidgetDataArrayList;
    }

    //todo get Widget Type
    @SuppressLint("Range")
    public ArrayList<AppWidgetData> getWidgetsType(int index, int type) {
        ArrayList<AppWidgetData> appWidgetDataArrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String table_name = "SELECT *FROM " + TABLE_NAME + " WHERE " + WIDGET_POSITION + "=? AND "+WIDGET_TYPE+ "=? ";
        Cursor cursor = db.rawQuery(table_name, new String[]{String.valueOf(index), String.valueOf(type)});
        if (cursor.moveToFirst()) {
            do {
                boolean sim;
                if (cursor.getInt(cursor.getColumnIndex(WIDGET_SIM)) == 0) {
                    sim = true;
                } else {
                    sim = false;
                }
                appWidgetDataArrayList.add(new AppWidgetData(cursor.getInt(cursor.getColumnIndex(WIDGET_ID))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_TYPE))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_POSITION))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_NUMBER))
                        , cursor.getString(cursor.getColumnIndex(WIDGET_CITY))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_TEMP))
                        , sim));
            } while (cursor.moveToNext());
        }
        return appWidgetDataArrayList;
    }


    //todo get Widget Type Order By
    @SuppressLint("Range")
    public ArrayList<AppWidgetData> getWidgetsTypeOrderBy() {
        ArrayList<AppWidgetData> appWidgetDataArrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String table_name = "SELECT *FROM " + TABLE_NAME + " ORDER BY " + WIDGET_POSITION + " ASC";
        Cursor cursor = db.rawQuery(table_name, null);
        if (cursor.moveToNext()) {
            do {
                boolean sim;
                if (cursor.getInt(cursor.getColumnIndex(WIDGET_SIM)) == 0) {
                    sim = true;
                } else {
                    sim = false;
                }
                appWidgetDataArrayList.add(new AppWidgetData(cursor.getInt(cursor.getColumnIndex(WIDGET_ID))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_TYPE))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_POSITION))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_NUMBER))
                        , cursor.getString(cursor.getColumnIndex(WIDGET_CITY))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_TEMP))
                        , sim));
            } while (cursor.moveToNext());
        }
        return appWidgetDataArrayList;
    }

    //todo get Widget data
    @SuppressLint("Range")
    public AppWidgetData getWidgetsId(int index) {
        AppWidgetData appWidgetDataArrayList = null;
        SQLiteDatabase db = getReadableDatabase();
        String table_name = "SELECT *FROM " + TABLE_NAME + " WHERE " + WIDGET_ID + "=? ";
        Cursor cursor = db.rawQuery(table_name, new String[]{String.valueOf(index)});
        if (cursor.moveToFirst()) {
            do {
                boolean sim;
                if (cursor.getInt(cursor.getColumnIndex(WIDGET_SIM)) == 0) {
                    sim = true;
                } else {
                    sim = false;
                }
                appWidgetDataArrayList = new AppWidgetData(cursor.getInt(cursor.getColumnIndex(WIDGET_ID))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_TYPE))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_POSITION))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_NUMBER))
                        , cursor.getString(cursor.getColumnIndex(WIDGET_CITY))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_TEMP))
                        , sim);
            } while (cursor.moveToNext());
        }
        return appWidgetDataArrayList;
    }

    //todo get Widget Id data
    @SuppressLint("Range")
    public AppWidgetData getWidgetsNumber(int index) {
        AppWidgetData appWidgetDataArrayList = null;
        SQLiteDatabase db = getReadableDatabase();
        String table_name = "SELECT *FROM " + TABLE_NAME + " WHERE " + WIDGET_NUMBER + "=? ";
        Cursor cursor = db.rawQuery(table_name, new String[]{String.valueOf(index)});
        if (cursor.moveToFirst()) {
            do {
                boolean sim;
                if (cursor.getInt(cursor.getColumnIndex(WIDGET_SIM)) == 0) {
                    sim = true;
                } else {
                    sim = false;
                }
                appWidgetDataArrayList = new AppWidgetData(cursor.getInt(cursor.getColumnIndex(WIDGET_ID))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_TYPE))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_POSITION))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_NUMBER))
                        , cursor.getString(cursor.getColumnIndex(WIDGET_CITY))
                        , cursor.getInt(cursor.getColumnIndex(WIDGET_TEMP))
                        , sim);
            } while (cursor.moveToNext());
        }
        return appWidgetDataArrayList;
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
        SQLiteDatabase database = this.getReadableDatabase();

        database.delete(TABLE_NAME, WIDGET_NUMBER + "=? ", new String[]{String.valueOf(index)});
    }

    //todo get WidgetMaster data
    @SuppressLint("Range")
    public AppWidgetMaster getWidgetMaster(int i3) {
        AppWidgetMaster widgetDataArrayList = null;
        SQLiteDatabase db = getReadableDatabase();
        String table_name = "SELECT * FROM " + WIDGET_MASTER_TABLE_NAME + " where " + WIDGET_MASTER_widgetId + "=?";
        Cursor cursor = db.rawQuery(table_name, new String[]{String.valueOf(i3)});
        if (cursor.moveToFirst()) {
            do {
                widgetDataArrayList = new AppWidgetMaster();
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
    public ArrayList<AppWidgetImages> getImageList(int i3) {
        ArrayList<AppWidgetImages> widgetDataArrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String table_name = "SELECT * FROM " + WIDGET_IMAGES_TABLE_NAME + " where " + WIDGET_IMAGES_Widget_ID + "=?";
        Cursor cursor = db.rawQuery(table_name, new String[]{String.valueOf(i3)});
        if (cursor.moveToFirst()) {
            do {
                AppWidgetImages widgetDataArray = new AppWidgetImages(
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
    public AppWidgetImages getImageListData(int i3) {
        AppWidgetImages appWidgetImages = null;
        SQLiteDatabase db = getReadableDatabase();
        String table_name = "SELECT *FROM " + WIDGET_IMAGES_TABLE_NAME + " where " + WIDGET_IMAGES_Widget_ID + "=?";
        Cursor cursor = db.rawQuery(table_name, new String[]{String.valueOf(i3)});
        if (cursor.moveToFirst()) {
            do {
                appWidgetImages = new AppWidgetImages(
                        cursor.getString(cursor.getColumnIndex(WIDGET_IMAGES_Id)),
                        cursor.getString(cursor.getColumnIndex(WIDGET_IMAGES_Uri)),
                        cursor.getInt(cursor.getColumnIndex(WIDGET_IMAGES_Widget_ID)));

            } while (cursor.moveToNext());
        }
        return appWidgetImages;
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

    public boolean isTableExists() {
        SQLiteDatabase mDatabase = getReadableDatabase();
        if (mDatabase == null || !mDatabase.isOpen()) {
            mDatabase = getReadableDatabase();
        }

        if (!mDatabase.isReadOnly()) {
            mDatabase.close();
            mDatabase = getReadableDatabase();
        }

        String query = "select DISTINCT " + TABLE_NAME + " from sqlite_master ";
        try (Cursor cursor = mDatabase.rawQuery(query, null)) {
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    return true;
                }
            }
            return false;
        }
    }
}
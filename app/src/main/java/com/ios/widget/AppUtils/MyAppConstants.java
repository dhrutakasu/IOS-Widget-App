package com.ios.widget.AppUtils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;

import com.ios.widget.AppClass.MyApp;
import com.ios.widget.AppCallback.FilterResultCallback;
import com.ios.widget.AppFiles.Directory;
import com.ios.widget.AppFiles.FileFilter;
import com.ios.widget.AppFiles.ImageFile;
import com.ios.widget.ImageModel.AppWidgetModel;
import com.ios.widget.R;
import com.karumi.dexter.PermissionToken;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

public class MyAppConstants {

    public static final String ITEM_POSITION = "ITEM_POSITION";
    public static final String WIDGET_ITEM_POSITION = "WIDGET_ITEM_POSITION";
    public static final String CROP_URI = "CROP_URI";
    public static int CROP_SIZE = 0;
    public static Uri CROP_DATA;
    public static String BASE_URL_WEATHER = "http://api.openweathermap.org/data/2.5/weather?q=";
    public static String BASE_URL_FORECAST = "http://api.openweathermap.org/data/2.5/forecast?q=";
    public static final String TAG_WIDGET_NOTE_ID = "WIDGET_NOTE_ID";
    public static int Item_Id;
    public static int Widget_Type_Id;
    public static String TabPos = "TabPos";

    public static int Widget_Id = -1;

    public static ArrayList<Directory> list = new ArrayList<>();
    public static List<Directory<ImageFile>> mAll = new ArrayList<>();
    public static String MAX_NUMBER = "Max Number";
    public static ArrayList<ImageFile> mCropSelectedList_1_1 = new ArrayList<>();
    public static ArrayList<ImageFile> mCropSelectedList = new ArrayList<>();

    public static ArrayList<ImageFile> mSelectedList = new ArrayList<>();
    public static ArrayList<ImageFile> mSelectedList1_1 = new ArrayList<>();
    public static ArrayList<ImageFile> getmSelectedList = new ArrayList<>();
    public static final String EDIT_PERVIEW = "Edit_preview";
    public static int Temp_Id = -1;
    public static boolean WidgetRemove = false;
    public static boolean CreateWidget = false;
    public static boolean IS_SIM_CARD = false;
    public static String CROP_POS = "CROP_POS";
    public static String WidgetId = "WidgetId";
    public static String WidgetFlag = "WidgetFlag";
    public static String FlipWidget = "FlipWidget";

    public static boolean isConnectingToInternet(Context con) {
        ConnectivityManager connectivity = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo state : info) {
                    if (state.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static ArrayList<AppWidgetModel> getWidgetLists() {
        ArrayList<AppWidgetModel> modelArrayList = new ArrayList<>();
        AppWidgetModel appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_calendar_small_4, R.drawable.ic_widget_clock_medium_3, R.drawable.ic_widget_xpanel_large_1, "Trendy", 0);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_weather_small_1, R.drawable.ic_widget_calendar_medium_4, R.drawable.ic_widget_clock_large_1, "Trendy", 1);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_clock_small_9, R.drawable.ic_widget_xpanel_medium_3, R.drawable.ic_widget_weather_large_2, "Trendy", 2);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_calendar_small_2, R.drawable.ic_widget_calendar_medium_2, R.drawable.ic_widget_calendar_large_2, "Calendar", 5);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_calendar_small_3, R.drawable.ic_widget_calendar_medium_3, R.drawable.ic_widget_calendar_large_3, "Calendar", 6);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_calendar_small_4, R.drawable.ic_widget_calendar_medium_4, R.drawable.ic_widget_calendar_large_4, "Calendar", 7);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_weather_small_1, R.drawable.ic_widget_weather_medium_1, R.drawable.ic_widget_weather_large_1, "Weather", 8);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_weather_small_2, R.drawable.ic_widget_weather_medium_2, R.drawable.ic_widget_weather_large_2, "Weather", 9);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_weather_small_3, R.drawable.ic_widget_weather_medium_3, R.drawable.ic_widget_weather_large_3, "Weather", 10);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_clock_small_1, R.drawable.ic_widget_clock_medium_1, R.drawable.ic_widget_clock_large_1, "Clock", 11);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_clock_small_2, R.drawable.ic_widget_clock_medium_2, R.drawable.ic_widget_clock_large_2, "Clock", 12);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_clock_small_3, R.drawable.ic_widget_clock_medium_3, R.drawable.ic_widget_clock_large_3, "Clock", 13);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_clock_small_4, R.drawable.ic_widget_clock_medium_4, R.drawable.ic_widget_clock_large_4, "Clock", 14);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_clock_small_9, R.drawable.ic_widget_clock_medium_9, R.drawable.ic_widget_clock_large_9, "Clock", 19);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_xpanel_small_1, R.drawable.ic_widget_xpanel_medium_1, R.drawable.ic_widget_xpanel_large_1, "X-Panel", 20);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_xpanel_small_4, R.drawable.ic_widget_xpanel_medium_4, R.drawable.ic_widget_xpanel_large_4, "X-Panel", 21);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_xpanel_small_3, R.drawable.ic_widget_xpanel_medium_3, R.drawable.ic_widget_xpanel_large_3, "X-Panel", 22);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_photo_small, R.drawable.ic_widget_photo_medium, R.drawable.ic_widget_photo_large, "Trendy", 23);
        modelArrayList.add(appWidgetModel);
        return modelArrayList;
    }

    public static ArrayList<AppWidgetModel> getTrendyWidgetLists() {
        ArrayList<AppWidgetModel> modelArrayList = new ArrayList<>();
        AppWidgetModel appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_calendar_small_4, R.drawable.ic_widget_clock_medium_3, R.drawable.ic_widget_xpanel_large_1, "Trendy", 0);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_weather_small_1, R.drawable.ic_widget_calendar_medium_4, R.drawable.ic_widget_clock_large_1, "Trendy", 1);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_clock_small_9, R.drawable.ic_widget_xpanel_medium_3, R.drawable.ic_widget_weather_large_2, "Trendy", 2);
        modelArrayList.add(appWidgetModel);
        return modelArrayList;
    }

    public static ArrayList<AppWidgetModel> getCalendarWidgetLists() {
        ArrayList<AppWidgetModel> modelArrayList = new ArrayList<>();
        AppWidgetModel appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_calendar_small_2, R.drawable.ic_widget_calendar_medium_2, R.drawable.ic_widget_calendar_large_2, "Calendar", 5);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_calendar_small_3, R.drawable.ic_widget_calendar_medium_3, R.drawable.ic_widget_calendar_large_3, "Calendar", 6);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_calendar_small_4, R.drawable.ic_widget_calendar_medium_4, R.drawable.ic_widget_calendar_large_4, "Calendar", 7);
        modelArrayList.add(appWidgetModel);

        return modelArrayList;
    }

    public static ArrayList<AppWidgetModel> getWeatherWidgetLists() {
        ArrayList<AppWidgetModel> modelArrayList = new ArrayList<>();
        AppWidgetModel appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_weather_small_1, R.drawable.ic_widget_weather_medium_1, R.drawable.ic_widget_weather_large_1, "Weather", 8);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_weather_small_2, R.drawable.ic_widget_weather_medium_2, R.drawable.ic_widget_weather_large_2, "Weather", 9);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_weather_small_3, R.drawable.ic_widget_weather_medium_3, R.drawable.ic_widget_weather_large_3, "Weather", 10);
        modelArrayList.add(appWidgetModel);

        return modelArrayList;
    }

    public static ArrayList<AppWidgetModel> getClockWidgetLists() {
        ArrayList<AppWidgetModel> modelArrayList = new ArrayList<>();
        AppWidgetModel appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_clock_small_1, R.drawable.ic_widget_clock_medium_1, R.drawable.ic_widget_clock_large_1, "Clock", 11);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_clock_small_2, R.drawable.ic_widget_clock_medium_2, R.drawable.ic_widget_clock_large_2, "Clock", 12);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_clock_small_3, R.drawable.ic_widget_clock_medium_3, R.drawable.ic_widget_clock_large_3, "Clock", 13);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_clock_small_4, R.drawable.ic_widget_clock_medium_4, R.drawable.ic_widget_clock_large_4, "Clock", 14);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_clock_small_9, R.drawable.ic_widget_clock_medium_9, R.drawable.ic_widget_clock_large_9, "Clock", 19);
        modelArrayList.add(appWidgetModel);
        return modelArrayList;
    }

    public static ArrayList<AppWidgetModel> getXPanelWidgetLists() {
        ArrayList<AppWidgetModel> modelArrayList = new ArrayList<>();
        AppWidgetModel appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_xpanel_small_1, R.drawable.ic_widget_xpanel_medium_1, R.drawable.ic_widget_xpanel_large_1, "X-Panel", 20);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_xpanel_small_4, R.drawable.ic_widget_xpanel_medium_4, R.drawable.ic_widget_xpanel_large_4, "X-Panel", 21);
        modelArrayList.add(appWidgetModel);
        appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_xpanel_small_3, R.drawable.ic_widget_xpanel_medium_3, R.drawable.ic_widget_xpanel_large_3, "X-Panel", 22);
        modelArrayList.add(appWidgetModel);

        return modelArrayList;
    }

    public static ArrayList<AppWidgetModel> getPhotoWidgetLists() {
        ArrayList<AppWidgetModel> modelArrayList = new ArrayList<>();
        AppWidgetModel appWidgetModel = new AppWidgetModel(R.drawable.ic_widget_photo_small, R.drawable.ic_widget_photo_medium, R.drawable.ic_widget_photo_large, "Trendy", 23);
        modelArrayList.add(appWidgetModel);

        return modelArrayList;
    }

    private static double SPACE_KB = 1024;
    private static double SPACE_MB = 1024 * SPACE_KB;
    private static double SPACE_GB = 1024 * SPACE_MB;
    private static double SPACE_TB = 1024 * SPACE_GB;

    public static String bytes2String(long sizeInBytes) {

        NumberFormat nf = new DecimalFormat();
        nf.setMaximumFractionDigits(2);

        try {
            if (sizeInBytes < SPACE_KB) {
                return nf.format(sizeInBytes) + " Byte(s)";
            } else if (sizeInBytes < SPACE_MB) {
                return nf.format(sizeInBytes / SPACE_KB) + " KB";
            } else if (sizeInBytes < SPACE_GB) {
                return nf.format(sizeInBytes / SPACE_MB) + " MB";
            } else if (sizeInBytes < SPACE_TB) {
                return nf.format(sizeInBytes / SPACE_GB) + " GB";
            } else {
                return nf.format(sizeInBytes / SPACE_TB) + " TB";
            }
        } catch (Exception e) {
            return sizeInBytes + " Byte(s)";
        }
    }

    public static int getWeatherIcons(String icon) {
        switch (icon) {
            case "01d":
                return R.drawable.ic_per_sunny;
            case "02d":
            case "02n":
                return R.drawable.ic_per_cloudy;
            case "03d":
                return R.drawable.ic_per_mostcloudy;
            case "04d":
            case "04n":
                return R.drawable.ic_per_broken_clouds;
            case "09d":
            case "09n":
                return R.drawable.ic_per_shower_rain;
            case "10d":
                return R.drawable.ic_per_rain;
            case "11d":
            case "11n":
                return R.drawable.ic_per_rainstorm;
            case "13d":
            case "13n":
                return R.drawable.ic_per_snow;
            case "50d":
            case "50n":
                return R.drawable.ic_per_fog;
            case "01n":
                return R.drawable.ic_per_night_clear;
            case "03n":
                return R.drawable.ic_per_night_cloudy;
            case "10n":
                return R.drawable.ic_per_night_rain;
        }
        return 0;
    }

    public static Uri createNewEmptyFile(String cropName) {
        return Uri.fromFile(new File(
                MyApp.getInstance().getFilesDir(),
                cropName + "_" + System.currentTimeMillis() + ".png"));
    }

    public static class DeviceMemory {
        public static float getInternalStorageSpace() {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            float total = ((float) statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
            return total;
        }

        public static float getInternalFreeSpace() {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            float free = ((float) statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576;
            return free;
        }

        public static float getInternalUsedSpace() {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            float total = ((float) statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
            float free = ((float) statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576;
            float busy = total - free;
            return busy;
        }
    }

    public static boolean IsWIfiConnected(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        return wifi.isWifiEnabled();
    }

    public static void showSettingsDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogColor);
        builder.setTitle((CharSequence) "Need Permissions");
        builder.setMessage((CharSequence) "This app needs permissions to use this feature. You can grant them in app settings.");
        builder.setPositiveButton((CharSequence) "GOTO SETTINGS", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                MyAppConstants.openSettings(activity);
            }
        });
        builder.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                activity.onBackPressed();
            }
        });
        builder.show();
    }

    public static void showPermissionDialog(final Activity activity, final PermissionToken permissionToken) {
        new AlertDialog.Builder(activity, R.style.DialogColor
        ).setMessage((int) R.string.MSG_ASK_PERMISSION).setNegativeButton("Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                permissionToken.cancelPermissionRequest();
                activity.onBackPressed();
            }
        }).setPositiveButton("Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                permissionToken.continuePermissionRequest();
            }
        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                permissionToken.cancelPermissionRequest();
            }
        }).show();
    }

    private static void openSettings(Activity activity) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", activity.getPackageName(), (String) null));
        activity.startActivityForResult(intent, 101);
    }

    public static String extractPathWithoutSeparator(String url) {
        return url.substring(0, url.lastIndexOf("/"));
    }

    public static String extractFileSuffix(String url) {
        if (url.contains(".")) {
            return url.substring(url.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    public static String extractFileNameWithSuffix(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public static void getFolders(Activity activity) {
        FileFilter.getImages((FragmentActivity) activity, new FilterResultCallback<ImageFile>() {
            @Override
            public void onResult(List<Directory<ImageFile>> directories) {
                list = new ArrayList<>();
                Directory all = new Directory();
                all.setName(activity.getResources().getString(R.string.str_folder_all));
                list.add(all);
                list.addAll(directories);
                mAll = directories;
            }
        });
    }

    public static ArrayList<ImageFile> getSelectedImages() {
        return mSelectedList;
    }

    public static void addSelectedImage(ImageFile imageFile) {
        mSelectedList.add(imageFile);
        getmSelectedList.add(imageFile);
        imageFile.imageCount++;
    }

    public static void removeSelectedImages(ImageFile imageFile) {
        for (int i = 0; i < mSelectedList.size(); i++) {
            if (mSelectedList.get(i).equals(imageFile)) {
                mSelectedList.remove(imageFile);
                imageFile.imageCount--;
            }
        }
    }

    public static void clearAllSelection() {
        getSelectedImages().clear();
        mSelectedList.clear();
        getmSelectedList.clear();
        mSelectedList = new ArrayList<>();
        getmSelectedList = new ArrayList<>();
        System.gc();
    }

    public static String getUniqueId() {
        return UUID.randomUUID().toString();
    }

    public static int dpToPx(Context context, int i) {
        return Math.round(((float) i) * context.getResources().getDisplayMetrics().density);
    }
}


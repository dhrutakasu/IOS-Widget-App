package com.ios.widget.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import com.ios.widget.Callback.FilterResultCallback;
import com.ios.widget.Files.Directory;
import com.ios.widget.Files.FileFilter;
import com.ios.widget.Files.ImageFile;
import com.ios.widget.Model.WidgetModel;
import com.ios.widget.R;
import com.karumi.dexter.PermissionToken;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.content.Context.BATTERY_SERVICE;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

public class Constants {

    public static final String ITEM_POSITION = "ITEM_POSITION";
    public static final String WIDGET_ITEM_POSITION = "WIDGET_ITEM_POSITION";
    public static final String ACTION_REMOVE_WIDGET = "APPWIDGET_DELETED";
    public static String ItemName = "ItemName";
    public static String ItemIcon = "ItemIcon";
    public static String NOTIFICATION_HOUR = "NotificationHour";
    public static String NOTIFICATION_MINUTES = "NotificationMinutes";
    public static String ExerciseSetTime;
    public static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    public static String BASE_URL_EXTENSION = ".mp3";
    public static final String TAG_WIDGET_NOTE_ID = "WIDGET_NOTE_ID";
    public static final String TAG_WIDGET_TYPE = "WIDGET_TYPE";
    public static int Item_Id;
    public static int Widget_Type_Id;
    public static String WidgetClick = "WidgetClick";
    public static String TabPos = "TabPos";

    public static int Widget_Id = -1;

    public static ArrayList<Directory> list = new ArrayList<>();
    public static List<Directory<ImageFile>> mAll = new ArrayList<>();
    public static String MAX_NUMBER = "Max Number";
    public static ArrayList<ImageFile> mSelectedList = new ArrayList<>();
    public static ArrayList<ImageFile> getmSelectedList = new ArrayList<>();
    public static final String EDIT_PERVIEW = "Edit_preview";
    public static int Temp_Id = -1;
    public static boolean WidgetRemove = false;

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

    public static ArrayList<WidgetModel> getWidgetLists() {
        ArrayList<WidgetModel> modelArrayList = new ArrayList<>();
        WidgetModel widgetModel = new WidgetModel(R.drawable.img_calendar_small_4_xxhdpi, R.drawable.img_clock_medium_3_xxhdpi, R.drawable.img_xpanel_large_1_xxhdpi, "Trendy", 0);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_weather_small_1_xxhdpi, R.drawable.img_calendar_medium_4_xxhdpi, R.drawable.img_clock_large_1_xxhdpi, "Trendy", 1);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_9_xxhdpi, R.drawable.img_xpanel_medium_3_xxhdpi, R.drawable.img_weather_large_2_xxhdpi, "Trendy", 2);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_calendar_small_2_xxhdpi, R.drawable.img_calendar_medium_2_xxhdpi, R.drawable.img_calendar_large_2_xxhdpi, "Calendar", 5);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_calendar_small_3_xxhdpi, R.drawable.img_calendar_medium_3_xxhdpi, R.drawable.img_calendar_large_3_xxhdpi, "Calendar", 6);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_calendar_small_4_xxhdpi, R.drawable.img_calendar_medium_4_xxhdpi, R.drawable.img_calendar_large_4_xxhdpi, "Calendar", 7);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_weather_small_1_xxhdpi, R.drawable.img_weather_medium_1_xxhdpi, R.drawable.img_weather_large_1_xxhdpi, "Weather", 8);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_1_xxhdpi, R.drawable.img_clock_medium_1_xxhdpi, R.drawable.img_clock_large_1_xxhdpi, "Clock", 11);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_2_xxhdpi, R.drawable.img_clock_medium_2_xxhdpi, R.drawable.img_clock_large_2_xxhdpi, "Clock", 12);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_3_xxhdpi, R.drawable.img_clock_medium_3_xxhdpi, R.drawable.img_clock_large_3_xxhdpi, "Clock", 13);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_4_xxhdpi, R.drawable.img_clock_medium_4_xxhdpi, R.drawable.img_clock_large_4_xxhdpi, "Clock", 14);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_9_xxhdpi, R.drawable.img_clock_medium_9_xxhdpi, R.drawable.img_clock_large_9_xxhdpi, "Clock", 19);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_xpanel_small_1_xxhdpi, R.drawable.img_xpanel_medium_1_xxhdpi, R.drawable.img_xpanel_large_1_xxhdpi, "X-Panel", 20);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_xpanel_small_4_xxhdpi, R.drawable.img_xpanel_medium_4_xxhdpi, R.drawable.img_xpanel_large_4_xxhdpi, "X-Panel", 21);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_xpanel_small_3_xxhdpi, R.drawable.img_xpanel_medium_3_xxhdpi, R.drawable.img_xpanel_large_3_xxhdpi, "X-Panel", 22);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_photo_s_xxhdpi, R.drawable.img_photo_m_xxhdpi, R.drawable.img_photo_l_xxhdpi, "Trendy", 23);
        modelArrayList.add(widgetModel);
        return modelArrayList;
    }

    public static ArrayList<WidgetModel> getTrendyWidgetLists() {
        ArrayList<WidgetModel> modelArrayList = new ArrayList<>();
        WidgetModel widgetModel = new WidgetModel(R.drawable.img_calendar_small_4_xxhdpi, R.drawable.img_clock_medium_3_xxhdpi, R.drawable.img_xpanel_large_1_xxhdpi, "Trendy", 0);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_weather_small_1_xxhdpi, R.drawable.img_calendar_medium_4_xxhdpi, R.drawable.img_clock_large_1_xxhdpi, "Trendy", 1);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_9_xxhdpi, R.drawable.img_xpanel_medium_3_xxhdpi, R.drawable.img_weather_large_2_xxhdpi, "Trendy", 2);
        modelArrayList.add(widgetModel);
        return modelArrayList;
    }

    public static ArrayList<WidgetModel> getCalendarWidgetLists() {
        ArrayList<WidgetModel> modelArrayList = new ArrayList<>();
        WidgetModel widgetModel = new WidgetModel(R.drawable.img_calendar_small_2_xxhdpi, R.drawable.img_calendar_medium_2_xxhdpi, R.drawable.img_calendar_large_2_xxhdpi, "Calendar", 5);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_calendar_small_3_xxhdpi, R.drawable.img_calendar_medium_3_xxhdpi, R.drawable.img_calendar_large_3_xxhdpi, "Calendar", 6);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_calendar_small_4_xxhdpi, R.drawable.img_calendar_medium_4_xxhdpi, R.drawable.img_calendar_large_4_xxhdpi, "Calendar", 7);
        modelArrayList.add(widgetModel);

        return modelArrayList;
    }

    public static ArrayList<WidgetModel> getWeatherWidgetLists() {
        ArrayList<WidgetModel> modelArrayList = new ArrayList<>();
        WidgetModel widgetModel = new WidgetModel(R.drawable.img_weather_small_1_xxhdpi, R.drawable.img_weather_medium_1_xxhdpi, R.drawable.img_weather_large_1_xxhdpi, "Weather", 8);
        modelArrayList.add(widgetModel);
//        widgetModel = new WidgetModel(R.drawable.img_weather_small_2_xxhdpi, R.drawable.img_weather_medium_2_xxhdpi, R.drawable.img_weather_large_2_xxhdpi, "Weather", 9);
//        modelArrayList.add(widgetModel);
//        widgetModel = new WidgetModel(R.drawable.img_weather_small_3_xxhdpi, R.drawable.img_weather_medium_3_xxhdpi, R.drawable.img_weather_large_3_xxhdpi, "Weather", 10);
//        modelArrayList.add(widgetModel);

        return modelArrayList;
    }

    public static ArrayList<WidgetModel> getClockWidgetLists() {
        ArrayList<WidgetModel> modelArrayList = new ArrayList<>();
        WidgetModel widgetModel = new WidgetModel(R.drawable.img_clock_small_1_xxhdpi, R.drawable.img_clock_medium_1_xxhdpi, R.drawable.img_clock_large_1_xxhdpi, "Clock", 11);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_2_xxhdpi, R.drawable.img_clock_medium_2_xxhdpi, R.drawable.img_clock_large_2_xxhdpi, "Clock", 12);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_3_xxhdpi, R.drawable.img_clock_medium_3_xxhdpi, R.drawable.img_clock_large_3_xxhdpi, "Clock", 13);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_4_xxhdpi, R.drawable.img_clock_medium_4_xxhdpi, R.drawable.img_clock_large_4_xxhdpi, "Clock", 14);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_clock_small_9_xxhdpi, R.drawable.img_clock_medium_9_xxhdpi, R.drawable.img_clock_large_9_xxhdpi, "Clock", 19);
        modelArrayList.add(widgetModel);
        return modelArrayList;
    }

    public static ArrayList<WidgetModel> getXPanelWidgetLists() {
        ArrayList<WidgetModel> modelArrayList = new ArrayList<>();
        WidgetModel widgetModel = new WidgetModel(R.drawable.img_xpanel_small_1_xxhdpi, R.drawable.img_xpanel_medium_1_xxhdpi, R.drawable.img_xpanel_large_1_xxhdpi, "X-Panel", 20);
        modelArrayList.add(widgetModel);
//        widgetModel = new WidgetModel(R.drawable.img_xpanel_small_2_xxhdpi, R.drawable.img_xpanel_medium_2_xxhdpi, R.drawable.img_xpanel_large_2_xxhdpi, "X-Panel", 21);
//        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_xpanel_small_4_xxhdpi, R.drawable.img_xpanel_medium_4_xxhdpi, R.drawable.img_xpanel_large_4_xxhdpi, "X-Panel", 21);
        modelArrayList.add(widgetModel);
        widgetModel = new WidgetModel(R.drawable.img_xpanel_small_3_xxhdpi, R.drawable.img_xpanel_medium_3_xxhdpi, R.drawable.img_xpanel_large_3_xxhdpi, "X-Panel", 22);
        modelArrayList.add(widgetModel);

        return modelArrayList;
    }

    public static ArrayList<WidgetModel> getPhotoWidgetLists() {
        ArrayList<WidgetModel> modelArrayList = new ArrayList<>();
        WidgetModel widgetModel = new WidgetModel(R.drawable.img_photo_s_xxhdpi, R.drawable.img_photo_m_xxhdpi, R.drawable.img_photo_l_xxhdpi, "Trendy", 23);
        modelArrayList.add(widgetModel);

        return modelArrayList;
    }

    public static ArrayList<Integer> getWidgetCategoryLists() {
        ArrayList<Integer> TypesArrayList = new ArrayList<>();
        TypesArrayList.add(R.drawable.btn_trendy);
        TypesArrayList.add(R.drawable.btn_calendar);
        TypesArrayList.add(R.drawable.btn_weather);
        TypesArrayList.add(R.drawable.btn_alarm);
        TypesArrayList.add(R.drawable.btn_x_panel);
        TypesArrayList.add(R.drawable.btn_photo);

        return TypesArrayList;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle((CharSequence) "Need Permissions");
        builder.setMessage((CharSequence) "This app needs permissions to use this feature. You can grant them in app settings.");
        builder.setPositiveButton((CharSequence) "GOTO SETTINGS", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                Constants.openSettings(activity);
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
        new AlertDialog.Builder(activity).setMessage((int) R.string.MSG_ASK_PERMISSION).setNegativeButton("Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
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

    public static boolean detectIntent(Context ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static void getFolders(Activity activity) {
        FileFilter.getImages((FragmentActivity) activity, new FilterResultCallback<ImageFile>() {
            @Override
            public void onResult(List<Directory<ImageFile>> directories) {
                list = new ArrayList<>();
                Directory all = new Directory();
                all.setName(activity.getResources().getString(R.string.folder_all));
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

    public static void removeSelectedImage(int imageFile) {
//        if (imageFile <= mSelectedList.size()) {
        ImageFile imageFile1 = (ImageFile) mSelectedList.remove(imageFile);
        ImageFile file = (ImageFile) getmSelectedList.remove(imageFile);
        imageFile1.imageCount--;
//        }
    }

    public static void removeSelectedImages(ImageFile imageFile) {
        for (int i = 0; i < mSelectedList.size(); i++) {
            if (mSelectedList.get(i).equals(imageFile)) {
                mSelectedList.remove(imageFile);
                imageFile.imageCount--;
            }
        }
//        if (imageFile <= mSelectedList.size()) {
//            ImageFile imageFile1 = (ImageFile) mSelectedList.remove(imageFile);
//            ImageFile file = (ImageFile) getmSelectedList.remove(imageFile);
//            imageFile1.imageCount--;
//        }
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static boolean hasSIMCard(Context context) {
        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        // Check if the device supports telephony (has a SIM card slot)
        if (telephonyManager != null && telephonyManager.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE) {
            SubscriptionManager subscriptionManager =
                    null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                subscriptionManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            }

            // Get a list of active SIM cards (subscriptions)
            if (subscriptionManager != null) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return false;
                }
                final int activeSubscriptionInfoCount = subscriptionManager.getActiveSubscriptionInfoCount();
                return activeSubscriptionInfoCount > 0;
            }
        }

        // Device does not have telephony capabilities or unable to access SubscriptionManager
        return false;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network network = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                network = connectivityManager.getActiveNetwork();
            }
            if (network != null) {
                NetworkCapabilities networkCapabilities =
                        connectivityManager.getNetworkCapabilities(network);
                return networkCapabilities != null
                        && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
            }
        }
        return false;
    }

    public static boolean checkDataRoamingEnabled(Context context) {
        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null && telephonyManager.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return false;
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                return telephonyManager.isDataRoamingEnabled();
            }
            // Use isDataRoamingEnabled as needed
        } else {
            // TelephonyManager not available or device does not support telephony
            // Handle the case gracefully
        }
        return false;
    }
}


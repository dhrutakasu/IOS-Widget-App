package com.ios.widget.ui.Activity;

import android.Manifest;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.ios.widget.Files.ImageFile;
import com.ios.widget.Model.WidgetData;
import com.ios.widget.Model.WidgetImages;
import com.ios.widget.Model.WidgetMaster;
import com.ios.widget.Model.WidgetModel;
import com.ios.widget.R;
import com.ios.widget.helper.DatabaseHelper;
import com.ios.widget.provider.LargePhotoWidgetProvider;
import com.ios.widget.provider.MediumPhotoWidgetProvider;
import com.ios.widget.provider.SmallPhotoWidgetProvider;
import com.ios.widget.ui.Adapter.PhotoAdapter;
import com.ios.widget.ui.Adapter.WidgetPagerAdapter;
import com.ios.widget.utils.Constants;
import com.ios.widget.utils.Pref;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import static com.ios.widget.utils.Constants.mSelectedList;

public class PhotoWidgetActivity extends AppCompatActivity {
    private final int GET_PHOTO = 102;
    private ArrayList<String> PhotoLists;
    private ImageView IvBack;
    private TextView TvTitle;
    private RecyclerView RvPhotos;
    public TextView TvUploadPhotoSize;
    private ArrayList<Bitmap> bitmapList = new ArrayList<>();
    private Context context;
    public List<WidgetImages> imageUriList = new ArrayList();
    private WidgetImages model;
    private PhotoAdapter photoAdapter;
    private ViewPager PagerWidget;
    private TabLayout TabSizeLayout;
    private WidgetPagerAdapter adapter;
    private ArrayList<WidgetModel> modelArrayList;
    private Spinner SpinnerWidgetInterval;
    private String[] intervals = {"1 Min", "5 Min", "30 Min", "1 h", "6 h"};
    private ImageView IvAddWidget;
    private DatabaseHelper database;
    private int widgetId;
    private WidgetMaster widgetMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_photo_widget);
        initViews();
        initListener();
        initActions();
    }

    private void initViews() {
        IvBack = (ImageView) findViewById(R.id.IvBack);
        TvTitle = (TextView) findViewById(R.id.TvTitle);
        RvPhotos = (RecyclerView) findViewById(R.id.RvPhotos);
        TvUploadPhotoSize = (TextView) findViewById(R.id.TvUploadPhotoSize);
        TabSizeLayout = (TabLayout) findViewById(R.id.TabSizeLayout);
        PagerWidget = (ViewPager) findViewById(R.id.PagerWidget);
        SpinnerWidgetInterval = (Spinner) findViewById(R.id.SpinnerWidgetInterval);
        IvAddWidget = (ImageView) findViewById(R.id.IvAddWidget);

        modelArrayList = new ArrayList<>();
        WidgetModel widgetModel = new WidgetModel(R.drawable.img_photo_s_xxhdpi, R.drawable.img_photo_m_xxhdpi, R.drawable.img_photo_l_xxhdpi, "Trendy", 23);
        modelArrayList.add(widgetModel);

        TabSizeLayout.addTab(TabSizeLayout.newTab().setText("Small"));
        TabSizeLayout.addTab(TabSizeLayout.newTab().setText("Medium"));
        TabSizeLayout.addTab(TabSizeLayout.newTab().setText("Large"));

        adapter = new WidgetPagerAdapter(this, modelArrayList, 0);
        PagerWidget.setAdapter(adapter);

        database = new DatabaseHelper(this.context);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int intExtra = intent.getIntExtra("widgetId", 0);
        System.out.println("*********** widgetId = " + intExtra);
        this.widgetId = intExtra;
        if (intExtra != 0) {
            WidgetMaster mainWidget = this.database.getWidgetMaster(this.widgetId);
            this.widgetMaster = mainWidget;
            if (mainWidget != null) {
                imageUriList = new ArrayList<>();
                this.imageUriList = this.database.getImageList(this.widgetId);
                Constants.clearAllSelection();
                for (int i = 0; i < imageUriList.size(); i++) {
                    ImageFile imageFile = new ImageFile();
                    imageFile.setPath(imageUriList.get(i).getUri());
                    mSelectedList.add(imageFile);
                }
                System.out.println("************ Path Constants----: "+Constants.mSelectedList.size());
                imageUriList.add(0, new WidgetImages("", String.valueOf(R.drawable.ic_upload), -1));
                System.out.println("*********** UriList = " + imageUriList.size());
                SpinnerWidgetInterval.setSelection(mainWidget.getInterval());

                WidgetData widgetData = database.getWidgetsNumber(widgetId);

                System.out.println("*********** widgetData.getType() = " + widgetData.getType());
                TabLayout.Tab tab = TabSizeLayout.getTabAt(widgetData.getType());
                tab.select();
                adapter.setchange(tab.getPosition());
                adapter.notifyDataSetChanged();

                System.out.println("*********** mSelectedList = " + mSelectedList.size());
            } else {
                this.widgetMaster = new WidgetMaster();
            }
        } else if (extras != null) {
            this.widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
            this.widgetMaster = new WidgetMaster();
            widgetMaster.setWidgetId(widgetId);
        } else {
            imageUriList = new ArrayList<>();
            imageUriList.add(new WidgetImages("", String.valueOf(R.drawable.ic_upload), -1));
            this.widgetMaster = new WidgetMaster();
            widgetMaster.setWidgetId(0);
//            Toast.makeText(this, "Widget Is Invalid..", Toast.LENGTH_SHORT).show();
//            finish();
        }
    }

    private void initListener() {
        IvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        SpinnerWidgetInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                System.out.println("@@@@@@@@@ pos : " + position);
                new Pref(context).setWidgetInterval(context, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        IvAddWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.Widget_Type_Id = 23;
                System.out.println("******* imageUriList : " + TabSizeLayout.getSelectedTabPosition());
                if (mSelectedList.size() < 2) {
                    Toast.makeText(context, "Select at least two photo", Toast.LENGTH_SHORT).show();
                } else {
//                    if (widgetMaster.getWidgetId() != 0) {
//                        widgetMaster.setInterval(SpinnerWidgetInterval.getSelectedItemPosition());
//                        database.updateWidget(widgetMaster);
//                    } else {
                    if (widgetMaster.getWidgetId() == 0) {
                        if (TabSizeLayout.getSelectedTabPosition() == 0) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                AppWidgetManager manager = (AppWidgetManager) getSystemService(AppWidgetManager.class);
                                ComponentName componentName = new ComponentName(context, SmallPhotoWidgetProvider.class);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    if (manager != null && manager.isRequestPinAppWidgetSupported()) {
                                        new Handler().postDelayed(() -> {
                                            Intent pinnedWidgetCallbackIntent = new Intent(context, SmallPhotoWidgetProvider.class);
                                            pinnedWidgetCallbackIntent.putExtra("IS_FIRST_ADDED", true);
                                            PendingIntent pinAppWidget = PendingIntent.getBroadcast(context, 0, pinnedWidgetCallbackIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                            manager.requestPinAppWidget(componentName, null, pinAppWidget);
                                        }, 100);
                                    }
                                }
                            }
                        } else if (TabSizeLayout.getSelectedTabPosition() == 1) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                AppWidgetManager manager = (AppWidgetManager) getSystemService(AppWidgetManager.class);
                                ComponentName componentName = new ComponentName(context, MediumPhotoWidgetProvider.class);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    if (manager != null && manager.isRequestPinAppWidgetSupported()) {
                                        new Handler().postDelayed(() -> {
                                            Intent pinnedWidgetCallbackIntent = new Intent(context, MediumPhotoWidgetProvider.class);
                                            pinnedWidgetCallbackIntent.putExtra("IS_FIRST_ADDED", true);
                                            PendingIntent pinAppWidget = PendingIntent.getBroadcast(context, 0, pinnedWidgetCallbackIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                            manager.requestPinAppWidget(componentName, null, pinAppWidget);
                                        }, 100);
                                    }
                                }
                            }
                        } else if (TabSizeLayout.getSelectedTabPosition() == 2) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                AppWidgetManager manager = (AppWidgetManager) getSystemService(AppWidgetManager.class);
                                ComponentName componentName = new ComponentName(context, LargePhotoWidgetProvider.class);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    if (manager != null && manager.isRequestPinAppWidgetSupported()) {
                                        new Handler().postDelayed(() -> {
                                            Intent pinnedWidgetCallbackIntent = new Intent(context, LargePhotoWidgetProvider.class);
                                            pinnedWidgetCallbackIntent.putExtra("IS_FIRST_ADDED", true);
                                            PendingIntent pinAppWidget = PendingIntent.getBroadcast(context, 0, pinnedWidgetCallbackIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                                            manager.requestPinAppWidget(componentName, null, pinAppWidget);
                                        }, 100);
                                    }
                                }
                            }
                        }
                    } else {
                        System.out.println("************ Path Constants: "+Constants.mSelectedList.size());

                        for (int i = 0; i < Constants.mSelectedList.size(); i++) {
                            System.out.println("************ Path : "+database.CheckIsAlreadyDBorNot(Constants.mSelectedList.get(i).getPath().toString(), String.valueOf(widgetId)));
                            if (database.CheckIsAlreadyDBorNot(Constants.mSelectedList.get(i).getPath().toString(), String.valueOf(widgetId))) {
                                System.out.println("************ Path Constants--iiiii: "+i);
                                WidgetImages widgetImages1 = new WidgetImages(database.getImageListData(widgetId).getImageId(), String.valueOf(Constants.mSelectedList.get(i).getPath()), widgetId);
                                database.updateWidgetImages(widgetImages1);
                            } else {
                                System.out.println("************ Path Constants--iieee: "+i);
                                WidgetImages widgetImages = new WidgetImages("0", Constants.mSelectedList.get(i).getPath(), widgetId);
                                database.InsertWidgetImage(widgetImages);
                            }
                        }
                        if (database.CheckIsAlreadyMasterOrNot(String.valueOf(widgetId))) {
                            WidgetMaster widgetMaster = database.getWidgetMaster(widgetId);
                            WidgetMaster widgetMaster1 = new WidgetMaster();
                            widgetMaster1.setWidgetId(widgetId);
                            widgetMaster1.setInterval(new Pref(context).getWidgetInterval(context));
                            widgetMaster1.setId(widgetMaster.getId());
                            widgetMaster1.setSpaceBorder(widgetMaster.getSpaceBorder());
                            widgetMaster1.setSize(widgetMaster.getSize());
                            widgetMaster1.setShape(widgetMaster.getShape());
                            widgetMaster1.setRow(widgetMaster.getRow());
                            widgetMaster1.setRotationType(widgetMaster.getRotationType());
                            widgetMaster1.setOpacity(widgetMaster.getOpacity());
                            widgetMaster1.setFlipControl(true);
                            widgetMaster1.setCustomMode(false);
                            widgetMaster1.setCropType(widgetMaster.getCropType());
                            widgetMaster1.setCornerBorder(widgetMaster.getCornerBorder());
                            widgetMaster1.setColumn(widgetMaster.getColumn());
                            widgetMaster1.setColorCode(widgetMaster.getColorCode());

                            database.updateWidgetMaster(widgetMaster1);
                        }
                        Intent intent = new Intent(context, getClass());
                        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                        intent.setData(Uri.parse(Constants.getUniqueId()));
                        intent.putExtra("appWidgetId", widgetId);

                        if (TabSizeLayout.getSelectedTabPosition() == 0) {
                            SmallPhotoWidgetProvider.updateWidgetView(widgetId, context, intent);
                        } else if (TabSizeLayout.getSelectedTabPosition() == 1) {
                            MediumPhotoWidgetProvider.updateWidgetView(widgetId, context, intent);
                        } else if (TabSizeLayout.getSelectedTabPosition() == 2) {
                            LargePhotoWidgetProvider.updateWidgetView(widgetId, context, intent);
                        }

                    }
                   /* AppWidgetManager appWidgetManager = (AppWidgetManager) getSystemService(AppWidgetManager.class);
                    ComponentName componentName = new ComponentName(context, PhotoWidgetProvider.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("ggg", "ggg");
                    if (appWidgetManager.isRequestPinAppWidgetSupported()) {
                        Intent intent = new Intent(context, PhotoWidgetProvider.class);
                        intent.putExtra("IS_FIRST_ADDED", true);
                        appWidgetManager.requestPinAppWidget(componentName, bundle, PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT));
                    }*/
                }
            }
        });
    }

    private void initActions() {
        TvTitle.setText("Photo Widget");
        RvPhotos.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        photoAdapter = new PhotoAdapter(context, imageUriList, new PhotoAdapter.setPhotoWidget() {
            public void setPhotoWidget(int pos) {
                if (pos == 0) {
                    GotoPermission();
                } else {

                }
                TvUploadPhotoSize.setText((imageUriList.size() - 1) + "/10");
            }

            @Override
            public void RemovePhotoWidget(int pos) {
                imageUriList.remove(pos);
                mSelectedList.remove(pos);
                TvUploadPhotoSize.setText((imageUriList.size() - 1) + "/10");
                photoAdapter.notifyDataSetChanged();
            }
        });
        TvUploadPhotoSize.setText((imageUriList.size() - 1) + "/10");
        RvPhotos.setAdapter(photoAdapter);

        TabSizeLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                adapter.setchange(tab.getPosition());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, intervals);
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(SpinnerWidgetInterval);
            int altoSpinner = (int) getResources().getDimension(com.intuit.sdp.R.dimen._50sdp);
            popupWindow.setHeight(altoSpinner);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException |
                IllegalAccessException e) {
        }

        SpinnerWidgetInterval.setAdapter(arrayAdapter);
    }

    private void GotoPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            String s1 = Manifest.permission.READ_MEDIA_IMAGES;
            Dexter.withActivity(this)
                    .withPermissions(s1)
                    .withListener(new MultiplePermissionsListener() {
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                onPermissionGranted();
                            }

                            if (report.isAnyPermissionPermanentlyDenied()) {
                                Constants.showSettingsDialog(PhotoWidgetActivity.this);
                            }
                        }

                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                       PermissionToken permissionToken) {
                            Constants.showPermissionDialog(PhotoWidgetActivity.this, permissionToken);
                        }
                    })
                    .onSameThread()
                    .check();
        } else {
            String s = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            String s1 = Manifest.permission.READ_EXTERNAL_STORAGE;
            Dexter.withActivity(this)
                    .withPermissions(s, s1)
                    .withListener(new MultiplePermissionsListener() {
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                onPermissionGranted();
                            }

                            if (report.isAnyPermissionPermanentlyDenied()) {
                                Constants.showSettingsDialog(PhotoWidgetActivity.this);
                            }
                        }

                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                       PermissionToken permissionToken) {
                            Constants.showPermissionDialog(PhotoWidgetActivity.this, permissionToken);
                        }
                    })
                    .onSameThread()
                    .check();
        }
    }

    private void onPermissionGranted() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                Constants.getFolders(PhotoWidgetActivity.this);
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {

                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("******* mSele : " + mSelectedList.size());
                                startActivityForResult(new Intent(PhotoWidgetActivity.this, ImageSelection_Activity.class)
                                        .putExtra(Constants.MAX_NUMBER, 10), GET_PHOTO);
                            }
                        }, 200);
                    }
                });
            }
        }.execute();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GET_PHOTO:
                switch (resultCode) {
                    case RESULT_OK:
                        imageUriList.clear();
                        imageUriList.add(0, new WidgetImages("", String.valueOf(R.drawable.ic_upload), -1));
                        System.out.println("---------IMAGe imageUriList :: " + imageUriList.size());
                        for (int i = 0; i < mSelectedList.size(); i++) {
                            System.out.println("---------IMAGe :: " + mSelectedList.get(i).getPath());
                            String uri = mSelectedList.get(i).getPath().toString();
                            Bitmap bitmap2 = null;
                            try {
                                System.out.println("**************bitmap uri: ");
                                bitmap2 = getThumbnail(Uri.fromFile(new File(uri)), context, true);
                            } catch (IOException e2) {
                                System.out.println("**************bitmap EXCEPTION: " + e2.getMessage());
                                e2.printStackTrace();
                            }
                            bitmapList.add(bitmap2);

                            WidgetImages widgetImages2 = new WidgetImages(Constants.getUniqueId(), storeImage(context, bitmap2).toString(), 0);
                            model = widgetImages2;
//                            if (widgetMaster.getWidgetId() != 0) {
//                                for (int k = 0; k < imageUriList.size(); k++) {
//                                        if (!mSelectedList.get(i).getPath().equalsIgnoreCase(imageUriList.get(k).getUri())){
//                                            System.out.println("******* mSele : " + mSelectedList.get(i).getPath());
//                                            System.out.println("******* imageUriList : " + imageUriList.get(k).getUri());
////                                            imageUriList.add(widgetImages2);
//                                        }
//                                }
//                            } else {
                            imageUriList.add(widgetImages2);
//                            }
                            System.out.println("---------IMAGe imageUriList 222:: " + imageUriList.size());
                            TvUploadPhotoSize.setText((imageUriList.size() - 1) + "/10");
                            photoAdapter.notifyDataSetChanged();
                        }
                        break;
                    case RESULT_CANCELED:
                        break;
                }
                break;
        }
    }

    public static Uri storeImage(Context context2, Bitmap bitmap) {
        File file = new File(context2.getFilesDir(), System.currentTimeMillis() + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return Uri.parse(file.toString());
    }

    public static Bitmap getThumbnail(Uri uri, Context context2, boolean z) throws IOException {
        Uri uri2;
        if (uri.toString().contains(context2.getApplicationContext().getPackageName())) {
            uri2 = FileProvider.getUriForFile(context2.getApplicationContext(), context2.getApplicationContext().getPackageName() + ".fileprovider", new File(uri.toString()));
        } else {
            uri2 = uri;
        }
        InputStream openInputStream = context2.getContentResolver().openInputStream(uri2);
        int photoOrientation = getPhotoOrientation(uri2, context2);
        Matrix matrix = new Matrix();
        matrix.postRotate((float) photoOrientation);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inDither = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Rect rect = null;
        BitmapFactory.decodeStream(openInputStream, (Rect) null, options);
        openInputStream.close();
        if (options.outWidth == -1 || options.outHeight == -1) {
            return null;
        }
        int i = options.outHeight > options.outWidth ? options.outHeight : options.outWidth;
        double d = i > 1024 ? (double) (i / 1024) : 1.0d;
        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inSampleSize = getPowerOfTwoForSampleRatio(d);
        options2.inDither = true;
        options2.inPreferredConfig = Bitmap.Config.ARGB_8888;
        InputStream openInputStream2 = context2.getContentResolver().openInputStream(uri2);
        Bitmap decodeStream = BitmapFactory.decodeStream(openInputStream2, (Rect) null, options2);
        openInputStream2.close();
        InputStream inputStream = openInputStream2;
        BitmapFactory.Options options3 = options2;
        return Bitmap.createBitmap(decodeStream, 0, 0, decodeStream.getWidth(), decodeStream.getHeight(), matrix, true);
    }

    private static int getPowerOfTwoForSampleRatio(double d) {
        int highestOneBit = Integer.highestOneBit((int) Math.floor(d));
        if (highestOneBit == 0) {
            return 1;
        }
        return highestOneBit;
    }

    private static int getPhotoOrientation(Uri uri, Context context2) {
        ExifInterface exifInterface;
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                InputStream inputStream = null;
                try {
                    inputStream = context2.getContentResolver().openInputStream(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                exifInterface = new ExifInterface(inputStream);
                inputStream.close();
            } else {
                ExifInterface exifInterface2 = new ExifInterface(uri.getPath());
                Log.e("ExifInterface", uri.getPath());
                exifInterface = exifInterface2;
            }
            int attributeInt = exifInterface.getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, 1);
            if (attributeInt == 3) {
                return 180;
            }
            if (attributeInt == 6) {
                return 90;
            }
            if (attributeInt != 8) {
                return 0;
            }
            return 270;
        } catch (Exception e2) {
            e2.printStackTrace();
            return 0;
        }
    }
}
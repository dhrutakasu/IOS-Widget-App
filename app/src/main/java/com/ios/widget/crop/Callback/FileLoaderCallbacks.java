package com.ios.widget.crop.Callback;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;

import com.ios.widget.Files.Directory;
import com.ios.widget.Files.ImageFile;
import com.ios.widget.Loaders.ImageLoader;
import com.ios.widget.crop.utils.MyAppConstants;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;
import static android.provider.MediaStore.MediaColumns.SIZE;
import static android.provider.MediaStore.MediaColumns.TITLE;

public class FileLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int TYPE_IMAGE = 0;

    private WeakReference<Context> context;
    private FilterResultCallback resultCallback;

    private int mType = TYPE_IMAGE;
    String[] mSuffixArgs;
    private CursorLoader mLoader;
    private String mSuffixRegex;

    public FileLoaderCallbacks(Context context, FilterResultCallback resultCallback, int type) {
        this(context, resultCallback, type, null);
    }

    public FileLoaderCallbacks(Context context, FilterResultCallback filterResultCallback, int type, String[] suffixArgs) {
        this.context = new WeakReference<>(context);
        resultCallback = filterResultCallback;
        mType = type;
        mSuffixArgs = suffixArgs;
        if (suffixArgs != null && suffixArgs.length > 0) {
            mSuffixRegex = obtainSuffixRegex(suffixArgs);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (mType) {
            case TYPE_IMAGE:
                mLoader = new ImageLoader(context.get());
                break;
        }

        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) return;
        switch (mType) {
            case TYPE_IMAGE:
                onImageResult(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @SuppressWarnings("unchecked")
    private void onImageResult(Cursor data) {
        List<Directory<ImageFile>> directories = new ArrayList<>();

        if (data.getPosition() != -1) {
            data.moveToPosition(-1);
        }

        while (data.moveToNext()) {
            ImageFile img = new ImageFile();
            img.setId(data.getLong(data.getColumnIndexOrThrow(_ID)));
            img.setName(data.getString(data.getColumnIndexOrThrow(TITLE)));
            img.setPath(data.getString(data.getColumnIndexOrThrow(DATA)));
            img.setSize(data.getLong(data.getColumnIndexOrThrow(SIZE)));
            img.setBucketId(data.getString(data.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_ID)));
            img.setBucketName(data.getString(data.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME)));
            img.setDate(data.getLong(data.getColumnIndexOrThrow(DATE_ADDED)));

            img.setOrientation(data.getInt(data.getColumnIndexOrThrow(MediaStore.MediaColumns.ORIENTATION)));
            Directory<ImageFile> directory = new Directory<>();
            directory.setId(img.getBucketId());
            directory.setName(img.getBucketName());
            directory.setPath(MyAppConstants.extractPathWithoutSeparator(img.getPath()));
            if (!directories.contains(directory)) {
                directory.addFile(img);
                directories.add(directory);
            } else {
                directories.get(directories.indexOf(directory)).addFile(img);
            }
        }

        if (resultCallback != null) {
            resultCallback.onResult(directories);
        }
    }

    private boolean contains(String path) {
        String name = MyAppConstants.extractFileNameWithSuffix(path);
        Pattern pattern = Pattern.compile(mSuffixRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    private String obtainSuffixRegex(String[] suffixes) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < suffixes.length; i++) {
            if (i == 0) {
                builder.append(suffixes[i].replace(".", ""));
            } else {
                builder.append("|\\.");
                builder.append(suffixes[i].replace(".", ""));
            }
        }
        return ".+(\\." + builder.toString() + ")$";
    }
}

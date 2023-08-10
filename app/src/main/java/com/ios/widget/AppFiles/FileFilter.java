package com.ios.widget.AppFiles;


import androidx.fragment.app.FragmentActivity;

import com.ios.widget.AppCallback.FileLoaderCallbacks;
import com.ios.widget.AppCallback.FilterResultCallback;

public class FileFilter {
    public static void getImages(FragmentActivity activity, FilterResultCallback<ImageFile> callback) {
        activity.getSupportLoaderManager().initLoader(0, null,
                new FileLoaderCallbacks(activity, callback, FileLoaderCallbacks.TYPE_IMAGE));
    }
}

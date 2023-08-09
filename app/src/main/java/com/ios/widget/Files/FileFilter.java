package com.ios.widget.Files;


import androidx.fragment.app.FragmentActivity;

import com.ios.widget.Callback.FileLoaderCallbacks;
import com.ios.widget.Callback.FilterResultCallback;

public class FileFilter {
    public static void getImages(FragmentActivity activity, FilterResultCallback<ImageFile> callback) {
        activity.getSupportLoaderManager().initLoader(0, null,
                new FileLoaderCallbacks(activity, callback, FileLoaderCallbacks.TYPE_IMAGE));
    }
}

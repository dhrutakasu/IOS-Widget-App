package com.ios.widget.AppCallback;

import com.ios.widget.AppFiles.BaseFile;
import com.ios.widget.AppFiles.Directory;

import java.util.List;

public interface FilterResultCallback<T extends BaseFile> {
    void onResult(List<Directory<T>> directories);
}

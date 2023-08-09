package com.ios.widget.Callback;

import com.ios.widget.Files.BaseFile;
import com.ios.widget.Files.Directory;

import java.util.List;

public interface FilterResultCallback<T extends BaseFile> {
    void onResult(List<Directory<T>> directories);
}

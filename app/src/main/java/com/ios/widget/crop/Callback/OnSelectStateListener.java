package com.ios.widget.crop.Callback;

public interface OnSelectStateListener<T> {
    void OnSelectStateChanged(boolean state, T file);
    void OnRemoveStateChanged(boolean state, int file);
}

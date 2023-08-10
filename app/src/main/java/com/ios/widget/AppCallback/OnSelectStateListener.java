package com.ios.widget.AppCallback;

public interface OnSelectStateListener<T> {
    void OnSelectStateChanged(boolean state, T file);
    void OnRemoveStateChanged(boolean state, int file);
}

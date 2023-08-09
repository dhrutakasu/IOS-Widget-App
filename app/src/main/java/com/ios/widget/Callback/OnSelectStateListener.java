package com.ios.widget.Callback;

public interface OnSelectStateListener<T> {
    void OnSelectStateChanged(boolean state, T file);
    void OnRemoveStateChanged(boolean state, int file);
}

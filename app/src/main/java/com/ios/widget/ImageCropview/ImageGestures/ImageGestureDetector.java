package com.ios.widget.ImageCropview.ImageGestures;

import android.view.MotionEvent;

public interface ImageGestureDetector {
	public boolean onTouchEvent(MotionEvent ev);

	public boolean isDragging();

	public boolean isScaling();

	public void setOnGestureListener(OnImageGestureListener listener);
}
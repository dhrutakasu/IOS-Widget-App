package com.ios.widget.ImageCropview.ImageGestures;

import android.content.Context;
import android.os.Build;

public final class ImageVersionedGestureDetector {
	public static ImageGestureDetector newInstance(Context context, OnImageGestureListener listener) {
		final int sdkVersion = Build.VERSION.SDK_INT;
		ImageGestureDetector detector;

		if (sdkVersion < Build.VERSION_CODES.ECLAIR) {
			detector = new ImageCupcakeImageGestureDetector(context);
		} else if (sdkVersion < Build.VERSION_CODES.FROYO) {
			detector = new ImageEclairImageGestureDetector(context);
		} else {
			detector = new ImageFroyoImageGestureDetector(context);
		}

		detector.setOnGestureListener(listener);

		return detector;
	}
}
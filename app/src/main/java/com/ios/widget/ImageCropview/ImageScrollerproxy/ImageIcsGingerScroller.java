package com.ios.widget.ImageCropview.ImageScrollerproxy;

import android.content.Context;

public class ImageIcsGingerScroller extends ImageGingerScroller {
	public ImageIcsGingerScroller(Context context) {
		super(context);
	}

	@Override
	public boolean computeScrollOffset() {
		return mScroller.computeScrollOffset();
	}
}
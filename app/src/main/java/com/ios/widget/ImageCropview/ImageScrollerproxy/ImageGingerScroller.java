package com.ios.widget.ImageCropview.ImageScrollerproxy;

import android.content.Context;
import android.widget.OverScroller;

public class ImageGingerScroller extends ImageScrollerProxy {
	protected final OverScroller mScroller;
	private boolean mFirstScroll = false;

	public ImageGingerScroller(Context context) {
		mScroller = new OverScroller(context);
	}

	@Override
	public void fling(int startX, int startY, int velocityX, int velocityY,
					  int minX, int maxX, int minY, int maxY, int overX, int overY) {
		mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, overX, overY);
	}

	@Override
	public boolean computeScrollOffset() {
		if (mFirstScroll) {
			mScroller.computeScrollOffset();
			mFirstScroll = false;
		}
		return mScroller.computeScrollOffset();
	}

	@Override
	public boolean isFinished() {
		return mScroller.isFinished();
	}

	@Override
	public void forceFinished(boolean finished) {
		mScroller.forceFinished(finished);
	}

	@Override
	public int getCurrX() {
		return mScroller.getCurrX();
	}

	@Override
	public int getCurrY() {
		return mScroller.getCurrY();
	}
}
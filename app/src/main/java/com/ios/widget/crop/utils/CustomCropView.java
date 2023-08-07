package com.ios.widget.crop.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class CustomCropView extends View {

    private Paint rectPaint;
    private Rect croppingRect;

    public CustomCropView(Context context) {
        super(context);
        init();
    }

    private void init() {
        rectPaint = new Paint();
        rectPaint.setColor(Color.WHITE);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(4);

        croppingRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(croppingRect, rectPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                croppingRect.left = (int) event.getX();
                croppingRect.top = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                croppingRect.right = (int) event.getX();
                croppingRect.bottom = (int) event.getY();
                invalidate();
                break;
        }
        return true;
    }
}
package com.ios.widget.provider;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ios.widget.Model.WidgetImages;
import com.ios.widget.Model.WidgetMaster;
import com.ios.widget.R;
import com.ios.widget.helper.DatabaseHelper;
import com.ios.widget.crop.utils.MyAppConstants;

import java.util.List;

public class LargePhotoWidgetService extends RemoteViewsService {
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this, intent);
    }

    private class GridRemoteViewsFactory implements RemoteViewsFactory {
        Bitmap[] bitmapList;
        int columns;
        int custCols = 2;
        int custPadding = 0;
        int custRows = 3;
        DatabaseHelper database;
        int height;
        List<WidgetImages> imageUriList;
        private int mAppWidgetId;
        private Context mContext;
        WidgetMaster widgetMaster;
        int width;

        public long getItemId(int i) {
            return (long) i;
        }

        public int getViewTypeCount() {
            return 1;
        }

        public boolean hasStableIds() {
            return true;
        }

        public void onCreate() {
        }

        public GridRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra("WidgetID", 0);
        }

        public void onDataSetChanged() {
            DatabaseHelper instance = new DatabaseHelper(mContext);
            database = instance;
            imageUriList = instance.getImageList(mAppWidgetId);
            widgetMaster = database.getWidgetMaster(mAppWidgetId);
            Bundle appWidgetOptions = AppWidgetManager.getInstance(mContext).getAppWidgetOptions(mAppWidgetId);
            width = 320;
            height = 320;
            columns = LargePhotoWidgetProvider.getCellsForSize(width);
            custPadding = widgetMaster.getSpaceBorder();
            if (widgetMaster.getShape() == 3 || widgetMaster.getShape() == 4) {
                if (!widgetMaster.isCustomMode()) {
                    int maxNumberOfCell = maxNumberOfCell(height);
                    int maxNumberOfCell2 = maxNumberOfCell(width);
                    int i = maxNumberOfCell * maxNumberOfCell2;
                    if (imageUriList.size() >= 2) {
                        if (imageUriList.size() <= i) {
                            int i2 = width;
                            int i3 = height;
                            if (i2 > i3) {
                                int shouldNumberOfCell = shouldNumberOfCell(i2);
                                if (shouldNumberOfCell(width) <= 0) {
                                    shouldNumberOfCell = 1;
                                }
                                if (imageUriList.size() / shouldNumberOfCell <= 1) {
                                    custRows = 2;
                                    custCols = (int) Math.ceil((double) ((((float) imageUriList.size()) * 1.0f) / ((float) custRows)));
                                } else {
                                    custRows = (int) Math.ceil((double) ((((float) imageUriList.size()) * 1.0f) / ((float) shouldNumberOfCell)));
                                    custCols = (int) Math.ceil((double) ((((float) imageUriList.size()) * 1.0f) / ((float) custRows)));
                                }
                            } else {
                                int shouldNumberOfCell2 = shouldNumberOfCell(i3);
                                if (shouldNumberOfCell(height) <= 0) {
                                    shouldNumberOfCell2 = 1;
                                }
                                if (imageUriList.size() / shouldNumberOfCell2 <= 1) {
                                    custCols = 2;
                                    custRows = (int) Math.ceil((double) ((((float) imageUriList.size()) * 1.0f) / ((float) custCols)));
                                } else {
                                    custCols = (int) Math.ceil((double) ((((float) imageUriList.size()) * 1.0f) / ((float) shouldNumberOfCell2)));
                                    custRows = (int) Math.ceil((double) ((((float) imageUriList.size()) * 1.0f) / ((float) custCols)));
                                }
                            }
                        } else {
                            custCols = maxNumberOfCell2;
                            custRows = maxNumberOfCell;
                        }
                        if (custRows == 0) {
                            custRows = 1;
                        }
                        if (custCols == 0) {
                            custCols = 1;
                        }
                    } else {
                        custCols = 1;
                        custRows = 1;
                    }
                } else {
                    custRows = widgetMaster.getRow() == 0 ? 1 : widgetMaster.getRow();
                    custCols = widgetMaster.getColumn() == 0 ? 1 : widgetMaster.getColumn();
                }
            }
            Bitmap[] bitmapArr = bitmapList;
            if (bitmapArr != null) {
                for (int length = bitmapArr.length - 1; length >= 0; length--) {
                    Bitmap[] bitmapArr2 = bitmapList;
                    if (bitmapArr2[length] != null) {
                        bitmapArr2[length].recycle();
                        bitmapList[length] = null;
                    }
                }
                bitmapList = null;
            }
            bitmapList = new Bitmap[imageUriList.size()];
        }

        public void onDestroy() {
        }

        public int maxNumberOfCell(int i) {
            return i / 90;
        }

        public int shouldNumberOfCell(int i) {
            return i / 100;
        }

        public int getCount() {
            WidgetMaster widgetMaster2 = widgetMaster;
            if (widgetMaster2 == null || (widgetMaster2.getShape() != 3 && widgetMaster.getShape() != 4)) {
                return imageUriList.size();
            }
            return (int) Math.ceil((double) ((((float) imageUriList.size()) * 1.0f) / ((float) (custCols * custRows))));
        }

        public RemoteViews getViewAt(int i) {
            int i2;
            int i3;
            int i4;
            int i5;
            int i6;
            int i7;
            int i8;
            int i9;
            int i10;
            int i11;
            int i28 = i;
            int dpToPx = MyAppConstants.dpToPx(mContext, width);
            int dpToPx2 = MyAppConstants.dpToPx(mContext, height);
            RemoteViews remoteViews = new RemoteViews(LargePhotoWidgetService.this.getPackageName(), R.layout.layout_image);
            remoteViews.setInt(R.id.IvItemFull, "setAlpha", widgetMaster.getOpacity());
            float f = 1.0f;
            int i29 = 1;
            switch (widgetMaster.getShape()) {
                case 0:
                    Bitmap[] bitmapArr = bitmapList;
                    if (bitmapArr[i28] != null) {
                        remoteViews.setImageViewBitmap(R.id.IvItemFull, bitmapArr[i28]);
                        break;
                    } else {
                        try {
                            Bitmap decodeFile = BitmapFactory.decodeFile(imageUriList.get(i28).getUri());
                            Matrix matrix = new Matrix();
                            matrix.postRotate((float) widgetMaster.getRotationType());
                            Bitmap createBitmap = Bitmap.createBitmap(decodeFile, 0, 0, decodeFile.getWidth(), decodeFile.getHeight(), matrix, true);
                            double width2 = (double) ((((float) createBitmap.getWidth()) * 1.0f) / ((float) createBitmap.getHeight()));
                            if (widgetMaster.getShape() == 3) {
                                i3 = dpToPx / (imageUriList.size() >= 3 ? Math.min(columns, 3) : 1);
                                i2 = i3;
                            } else {
                                i3 = dpToPx;
                                i2 = dpToPx2;
                            }
                            if (widgetMaster.getCropType() != 1) {
                                if (widgetMaster.getCropType() == 2) {
                                    if (i2 > i3) {
                                        i5 = (int) (((double) i2) * width2);
                                        i4 = i2;
                                    } else {
                                        i4 = (int) (((double) i3) / width2);
                                        i5 = i3;
                                    }
                                    if (i5 > i3) {
                                        i4 = (int) (((double) i3) / width2);
                                        i5 = i3;
                                    }
                                    if (i4 > i2) {
                                        i7 = (int) (((double) i2) * width2);
                                        i6 = i2;
                                    } else {
                                        i7 = i5;
                                        i6 = i4;
                                    }
                                    remoteViews.setImageViewBitmap(R.id.IvItemFull, getCenterFitRectangle(createBitmap, i7, i6, i3, i2, widgetMaster.getCornerBorder()));
                                    break;
                                }
                            } else {
                                if (createBitmap.getHeight() < createBitmap.getWidth()) {
                                    i9 = (int) (((double) i2) * width2);
                                    i8 = i2;
                                } else {
                                    i8 = (int) (((double) i3) / width2);
                                    i9 = i3;
                                }
                                if (i9 < i3) {
                                    i8 = (int) (((double) i3) / width2);
                                    i9 = i3;
                                }
                                if (i8 < i2) {
                                    i11 = (int) (((double) i2) * width2);
                                    i10 = i2;
                                } else {
                                    i11 = i9;
                                    i10 = i8;
                                }
                                remoteViews.setImageViewBitmap(R.id.IvItemFull, getCenterCropRectangle(createBitmap, i3, i2, i11, i10, widgetMaster.getCornerBorder()));
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                    break;
            }
            return remoteViews;
        }

        public Bitmap getCenterCropRectangle(Bitmap bitmap, int i, int i2, int i3, int i4, int i5) {
            int i6 = i;
            int i7 = i2;
            int i8 = i3;
            int i9 = i4;
            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, i8, i9, true);
            Bitmap createBitmap = Bitmap.createBitmap(i6, i7, createScaledBitmap.getConfig());
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            int i10 = (int) ((((double) i8) / 1.2d) - (((double) i6) / 1.2d));
            int i11 = (int) ((((double) i9) / 1.2d) - (((double) i7) / 1.2d));
            paint.setShader(new BitmapShader(createScaledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            float f = ((float) (i6 * i5)) / 100.0f;
            if (i7 < i6) {
                f = ((float) (i7 * i5)) / 100.0f;
            }
            canvas.drawRoundRect(new RectF(0.0f, 0.0f, (float) i6, (float) i7), f, f, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(createScaledBitmap, new Rect(i10, i11, i6 + i10, i7 + i11), new Rect(0, 0, i6, i7), paint);
            return createBitmap;
        }

        public Bitmap getCenterFitRectangle(Bitmap bitmap, int i, int i2, int i3, int i4, int i5) {
            int i6 = i;
            int i7 = i2;
            int i8 = i3;
            int i9 = i4;
            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, i6, i7, true);
            Bitmap createBitmap = Bitmap.createBitmap(i8, i9, Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(createScaledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            float width2 = ((float) (createScaledBitmap.getWidth() * i5)) / 100.0f;
            if (createScaledBitmap.getHeight() < createScaledBitmap.getWidth()) {
                width2 = ((float) (createScaledBitmap.getHeight() * i5)) / 100.0f;
            }
            int i10 = (int) ((((double) i8) / 2.0d) - (((double) i6) / 2.0d));
            int i11 = (int) ((((double) i9) / 2.0d) - (((double) i7) / 2.0d));
            float f = (float) i10;
            float f2 = (float) i11;
            canvas.drawRoundRect(new RectF(f, f2, (float) (createScaledBitmap.getWidth() + i10), (float) (createScaledBitmap.getHeight() + i11)), width2, width2, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(createScaledBitmap, f, f2, paint);
            return createBitmap;
        }

        public RemoteViews getLoadingView() {
            return widgetMaster.isLoadingIndicator() ? new RemoteViews(LargePhotoWidgetService.this.getPackageName(), R.layout.layout_widget_progress) : new RemoteViews(LargePhotoWidgetService.this.getPackageName(), R.layout.layout_empty);
        }
    }
}

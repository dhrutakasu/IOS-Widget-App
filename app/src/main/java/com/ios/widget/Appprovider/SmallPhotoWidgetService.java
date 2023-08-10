package com.ios.widget.Appprovider;

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

import com.ios.widget.ImageModel.AppWidgetImages;
import com.ios.widget.ImageModel.AppWidgetMaster;
import com.ios.widget.R;
import com.ios.widget.Apphelper.AppDatabaseHelper;
import com.ios.widget.AppUtils.MyAppConstants;

import java.util.List;

public class SmallPhotoWidgetService extends RemoteViewsService {
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this, intent);
    }

    private class GridRemoteViewsFactory implements RemoteViewsFactory {
        Bitmap[] bitmapList;
        int columns;
        int custCols = 2;
        int custPadding = 0;
        int custRows = 3;
        AppDatabaseHelper database;
        int height;
        List<AppWidgetImages> imageUriList;
        private int mAppWidgetId;
        private Context mContext;
        AppWidgetMaster appWidgetMaster;
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
            this.mContext = context;
            this.mAppWidgetId = intent.getIntExtra("WidgetID", 0);
        }

        public void onDataSetChanged() {
            AppDatabaseHelper instance = new AppDatabaseHelper(this.mContext);
            this.database = instance;
            this.imageUriList = instance.getImageList(this.mAppWidgetId);
            this.appWidgetMaster = this.database.getWidgetMaster(this.mAppWidgetId);
            Bundle appWidgetOptions = AppWidgetManager.getInstance(this.mContext).getAppWidgetOptions(this.mAppWidgetId);
            this.width = 180;
            this.height = 180;
            this.columns = SmallPhotoWidgetProvider.getCellsForSize(this.width);
            this.custPadding = this.appWidgetMaster.getSpaceBorder();
            if (this.appWidgetMaster.getShape() == 3 || this.appWidgetMaster.getShape() == 4) {
                if (!this.appWidgetMaster.isCustomMode()) {
                    int maxNumberOfCell = maxNumberOfCell(this.height);
                    int maxNumberOfCell2 = maxNumberOfCell(this.width);
                    int i = maxNumberOfCell * maxNumberOfCell2;
                    if (this.imageUriList.size() >= 2) {
                        if (this.imageUriList.size() <= i) {
                            int i2 = this.width;
                            int i3 = this.height;
                            if (i2 > i3) {
                                int shouldNumberOfCell = shouldNumberOfCell(i2);
                                if (shouldNumberOfCell(this.width) <= 0) {
                                    shouldNumberOfCell = 1;
                                }
                                if (this.imageUriList.size() / shouldNumberOfCell <= 1) {
                                    this.custRows = 2;
                                    this.custCols = (int) Math.ceil((double) ((((float) this.imageUriList.size()) * 1.0f) / ((float) this.custRows)));
                                } else {
                                    this.custRows = (int) Math.ceil((double) ((((float) this.imageUriList.size()) * 1.0f) / ((float) shouldNumberOfCell)));
                                    this.custCols = (int) Math.ceil((double) ((((float) this.imageUriList.size()) * 1.0f) / ((float) this.custRows)));
                                }
                            } else {
                                int shouldNumberOfCell2 = shouldNumberOfCell(i3);
                                if (shouldNumberOfCell(this.height) <= 0) {
                                    shouldNumberOfCell2 = 1;
                                }
                                if (this.imageUriList.size() / shouldNumberOfCell2 <= 1) {
                                    this.custCols = 2;
                                    this.custRows = (int) Math.ceil((double) ((((float) this.imageUriList.size()) * 1.0f) / ((float) this.custCols)));
                                } else {
                                    this.custCols = (int) Math.ceil((double) ((((float) this.imageUriList.size()) * 1.0f) / ((float) shouldNumberOfCell2)));
                                    this.custRows = (int) Math.ceil((double) ((((float) this.imageUriList.size()) * 1.0f) / ((float) this.custCols)));
                                }
                            }
                        } else {
                            this.custCols = maxNumberOfCell2;
                            this.custRows = maxNumberOfCell;
                        }
                        if (this.custRows == 0) {
                            this.custRows = 1;
                        }
                        if (this.custCols == 0) {
                            this.custCols = 1;
                        }
                    } else {
                        this.custCols = 1;
                        this.custRows = 1;
                    }
                } else {
                    this.custRows = this.appWidgetMaster.getRow() == 0 ? 1 : this.appWidgetMaster.getRow();
                    this.custCols = this.appWidgetMaster.getColumn() == 0 ? 1 : this.appWidgetMaster.getColumn();
                }
            }
            Bitmap[] bitmapArr = this.bitmapList;
            if (bitmapArr != null) {
                for (int length = bitmapArr.length - 1; length >= 0; length--) {
                    Bitmap[] bitmapArr2 = this.bitmapList;
                    if (bitmapArr2[length] != null) {
                        bitmapArr2[length].recycle();
                        this.bitmapList[length] = null;
                    }
                }
                this.bitmapList = null;
            }
            this.bitmapList = new Bitmap[this.imageUriList.size()];
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
            AppWidgetMaster appWidgetMaster2 = this.appWidgetMaster;
            if (appWidgetMaster2 == null || (appWidgetMaster2.getShape() != 3 && this.appWidgetMaster.getShape() != 4)) {
                return this.imageUriList.size();
            }
            return (int) Math.ceil((double) ((((float) this.imageUriList.size()) * 1.0f) / ((float) (this.custCols * this.custRows))));
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
            int i12;
            int i13;
            boolean z;
            int i14;
            int i15;
            int i16;
            int i17;
            Canvas canvas;
            Paint paint;
            int i18;
            int i19;
            int i20;
            int i21;
            int i22;
            int i23;
            int i24;
            int i25;
            Bitmap bitmap;
            int i26;
            int i27;
            int i28 = i;
            int dpToPx = MyAppConstants.dpToPx(this.mContext, this.width);
            int dpToPx2 = MyAppConstants.dpToPx(this.mContext, this.height);
            RemoteViews remoteViews = new RemoteViews(SmallPhotoWidgetService.this.getPackageName(), R.layout.layout_image);
            remoteViews.setInt(R.id.IvItemFull, "setAlpha", this.appWidgetMaster.getOpacity());
            float f = 1.0f;
            int i29 = 1;
            switch (this.appWidgetMaster.getShape()) {
                case 0:
                    Bitmap[] bitmapArr = this.bitmapList;
                    if (bitmapArr[i28] != null) {
                        remoteViews.setImageViewBitmap(R.id.IvItemFull, bitmapArr[i28]);
                        break;
                    } else {
                        try {
                            Bitmap decodeFile = BitmapFactory.decodeFile(this.imageUriList.get(i28).getUri());
                            Matrix matrix = new Matrix();
                            matrix.postRotate((float) this.appWidgetMaster.getRotationType());
                            Bitmap createBitmap = Bitmap.createBitmap(decodeFile, 0, 0, decodeFile.getWidth(), decodeFile.getHeight(), matrix, true);
                            double width2 = (double) ((((float) createBitmap.getWidth()) * 1.0f) / ((float) createBitmap.getHeight()));
                            if (this.appWidgetMaster.getShape() == 3) {
                                i3 = dpToPx / (this.imageUriList.size() >= 3 ? Math.min(this.columns, 3) : 1);
                                i2 = i3;
                            } else {
                                i3 = dpToPx;
                                i2 = dpToPx2;
                            }
                            if (this.appWidgetMaster.getCropType() != 1) {
                                if (this.appWidgetMaster.getCropType() == 2) {
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
                                    remoteViews.setImageViewBitmap(R.id.IvItemFull, getCenterFitRectangle(createBitmap, i7, i6, i3, i2, this.appWidgetMaster.getCornerBorder()));
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
                                remoteViews.setImageViewBitmap(R.id.IvItemFull, getCenterCropRectangle(createBitmap, i3, i2, i11, i10, this.appWidgetMaster.getCornerBorder()));
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

        public Bitmap  getCenterCropRectangle(Bitmap bitmap, int i, int i2, int i3, int i4, int i5) {
            int i6 = i;
            int i7 = i2;
            int i8 = i3;
            int i9 = i4;
            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, i8, i9, true);
            Bitmap createBitmap = Bitmap.createBitmap(i6, i7, createScaledBitmap.getConfig());
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            int i10 = (int) ((((double) i8) / 2.0d) - (((double) i6) / 2.0d));
            int i11 = (int) ((((double) i9) / 2.0d) - (((double) i7) / 2.0d));
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
            return this.appWidgetMaster.isLoadingIndicator() ? new RemoteViews(SmallPhotoWidgetService.this.getPackageName(), R.layout.layout_widget_progress) : new RemoteViews(SmallPhotoWidgetService.this.getPackageName(), R.layout.layout_empty);
        }
    }
}

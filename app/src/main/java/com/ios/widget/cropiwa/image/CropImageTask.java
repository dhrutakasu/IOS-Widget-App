package com.ios.widget.cropiwa.image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

import com.ios.widget.cropiwa.config.CropIwaSaveConfig;
import com.ios.widget.cropiwa.shape.CropIwaShapeMask;
import com.ios.widget.cropiwa.util.CropIwaUtils;
import com.ios.widget.ui.Activity.CropActivity;

import java.io.IOException;
import java.io.OutputStream;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.ios.widget.crop.utils.MyAppConstants.LOCAL_BROAD_CROP;

/**
 * Created by Yaroslav Polyakov on 22.03.2017.
 * https://github.com/polyak01
 */

class CropImageTask extends AsyncTask<Void, Void, String> {

    private final CropActivity cropActivity;
    private final CropIwaResultListener resultListener;
    private Context context;
    private CropArea cropArea;
    private CropIwaShapeMask mask;
    private Uri srcUri;
    private CropIwaSaveConfig saveConfig;

    public  CropImageTask( Context context, CropArea cropArea, CropIwaShapeMask mask,
            Uri srcUri, CropIwaSaveConfig saveConfig, CropActivity cropActivity, CropIwaResultListener resultListener) {
        this.context = context;
        this.cropArea = cropArea;
        this.mask = mask;
        this.srcUri = srcUri;
        this.saveConfig = saveConfig;
        this.cropActivity = cropActivity;
        this.resultListener = resultListener;
    }

    @Override
    protected String doInBackground(Void... params) {
        cropActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = CropIwaBitmapManager.get().loadToMemory(
                            context, srcUri, saveConfig.getWidth(),
                            saveConfig.getHeight());
                    System.out.println("--- - - - bitmapwww: " + bitmap.getWidth());


                    Bitmap cropped = cropArea.applyCropTo(bitmap);

                    cropped = mask.applyMaskTo(cropped);

                    Uri dst = saveConfig.getDstUri();
                    OutputStream os = context.getContentResolver().openOutputStream(dst);
                    cropped.compress(saveConfig.getCompressFormat(), saveConfig.getQuality(), os);
                    CropIwaUtils.closeSilently(os);

                    bitmap.recycle();
                    cropped.recycle();
                } catch (IOException e) {
                }
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(String throwable) {
//        if (throwable == null) {
            CropIwaResultReceiver.onCropCompleted(context, saveConfig.getDstUri());
            Intent intent=new Intent(LOCAL_BROAD_CROP);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            resultListener.onCropSuccess(saveConfig.getDstUri());

//        } else {
//            try {
//
//            } catch (Exception e) {
//                CropIwaResultReceiver.onCropFailed(context, e);
//            }
//        }
    }
}
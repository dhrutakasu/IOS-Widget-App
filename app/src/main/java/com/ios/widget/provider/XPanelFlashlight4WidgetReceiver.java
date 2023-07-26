package com.ios.widget.provider;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.ios.widget.R;

public class XPanelFlashlight4WidgetReceiver extends BroadcastReceiver {
    private static boolean isLightOn = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_widget_xpanel4_medium);
        if (isLightOn) {

            try {
                context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                Log.e("#isLightOn",isLightOn+"");

                CameraManager camManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                String cameraId = camManager.getCameraIdList()[0]; // Usually front camera is at 0 position.
                camManager.setTorchMode(cameraId, false);
                isLightOn = false;
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else {
            try {
                context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

                Log.e("#isLightOn",isLightOn+"");

                CameraManager camManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                String cameraId = camManager.getCameraIdList()[0]; // Usually front camera is at 0 position.
                camManager.setTorchMode(cameraId, true);
                isLightOn = true;

            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        if (isLightOn) {
            views.setImageViewResource(R.id.IvTorch, R.drawable.ic_torch4_selected);
        } else {
            views.setImageViewResource(R.id.IvTorch, R.drawable.ic_torch4);
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        System.out.println("*********** WIDGET _IDD : " + intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, -1));

        appWidgetManager.updateAppWidget(intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, -1),
                views);
    }
}
package com.ios.widget.crop.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.ios.widget.R;

public class NotificationHelper {

    private static final String CHANNEL_ID = "widget_channel_id";
    private static final int NOTIFICATION_ID = 123;

    public static void showNonCancelableNotification(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Your Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setSilent(true)
                .setAutoCancel(false) // Make the notification non-cancelable
                .setOngoing(true); // Show the notification as an ongoing one (non-dismissable)

        Notification notification = builder.build();

        // Show the notification
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
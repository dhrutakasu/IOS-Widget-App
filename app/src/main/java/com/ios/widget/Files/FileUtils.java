package com.ios.widget.Files;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;

import com.ios.widget.R;

import java.io.File;

public class FileUtils {

    public static long mDeleteFileCount = 0;
    public static File mDownloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
    public static File mSdCard = new File(MyApplication.getInstance().getFilesDir().getAbsolutePath());
    public static File APP_DIRECTORY = new File(mSdCard, "Pic2Video");
    public static final File TEMP_DIRECTORY = new File(APP_DIRECTORY, "temp");
    public static final File TEMP_VID_DIRECTORY = new File(TEMP_DIRECTORY, "temp_vid");
    public static final File TEMP_DIRECTORY_AUDIO = new File(APP_DIRECTORY, "temp_audio");
    public static final File frameFile = new File(APP_DIRECTORY, "frame.png");
    public static final File startFile = new File(APP_DIRECTORY, "start.png");
    public static final File endFile = new File(APP_DIRECTORY, "end.png");
    public static final File BitmapFile = new File(APP_DIRECTORY, "bitmap.png");
    public static final File gifFile = new File(APP_DIRECTORY, "gifSample.gif");
    public static File APP_DIRECTORY2 = new File(mDownloadDir, MyApplication.getInstance().getResources().getString(R.string.app_name));

    static {
        if (!TEMP_DIRECTORY.exists()) {
            TEMP_DIRECTORY.mkdirs();
        }
        if (!TEMP_VID_DIRECTORY.exists()) {
            TEMP_VID_DIRECTORY.mkdirs();
        }
    }

    public FileUtils() {
        mDeleteFileCount = 0;
    }

    public static void deleteTempDir() {
        for (final File child : TEMP_DIRECTORY.listFiles()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    FileUtils.deleteFile(child);
                }
            }).start();
        }
    }

    public static boolean deleteFile(File mFile) {
        boolean idDelete = false;
        if (mFile == null) {
            return false;
        }
        if (mFile.exists()) {
            if (mFile.isDirectory()) {
                File[] children = mFile.listFiles();
                if (children != null && children.length > 0) {
                    for (File child : children) {
                        mDeleteFileCount += child.length();
                        idDelete = deleteFile(child);
                    }
                }
                mDeleteFileCount += mFile.length();
                idDelete = mFile.delete();
            } else {
                mDeleteFileCount += mFile.length();
                idDelete = mFile.delete();
            }
        }
        return idDelete;
    }

    public static boolean deleteFile(String s) {
        return deleteFile(new File(s));
    }

    public static long getDuration(Context context, Uri videoPath) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(context, videoPath);
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long timeInMillisec = Long.parseLong(time);
            retriever.release();
            return timeInMillisec / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String formatSeconds(long timeInSeconds) {
        long hours = timeInSeconds / 3600;
        long secondsLeft = timeInSeconds - hours * 3600;
        long minutes = secondsLeft / 60;
        long seconds = secondsLeft - minutes * 60;

        String formattedTime = "";
        if (hours < 10 && hours != 0) {
            formattedTime += "0";
            formattedTime += hours + ":";
        }

        if (minutes < 10)
            formattedTime += "0";
        formattedTime += minutes + ":";

        if (seconds < 10)
            formattedTime += "0";
        formattedTime += seconds;

        return formattedTime;
    }
}

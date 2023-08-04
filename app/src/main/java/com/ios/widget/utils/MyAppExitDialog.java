package com.ios.widget.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.ads.nativead.NativeAd;
import com.ios.widget.Ads.MyAppAd_Native;
import com.ios.widget.R;
import com.ios.widget.ui.Activity.MainActivity;

public class MyAppExitDialog extends Dialog {
    private final MainActivity activity;
    public ExitListener exitListener;
    private NativeAd nativeAd;

    public interface ExitListener {

        void onExit();
    }

    public MyAppExitDialog(MainActivity activity, Context context, ExitListener exitListener) {
        super(context);
        this.activity = activity;
        this.exitListener = exitListener;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_exit);
        TextView TvDialogExit = (TextView) findViewById(R.id.TvDialogExit);
        MyAppAd_Native.getInstance().showNative250(activity, findViewById(R.id.FlNativeExit));
        TvDialogExit.setOnClickListener(view -> {
            exitListener.onExit();
        });
    }
}

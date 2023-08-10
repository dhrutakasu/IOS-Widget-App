package com.ios.widget.AppClass;

import android.app.Application;
import android.os.StrictMode;

public class MyApp extends Application{

    public static MyApp MyApp = null;

    public static MyApp getInstance() {
        return MyApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        MyApp = this;
    }
}

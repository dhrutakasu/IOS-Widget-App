package com.ios.widget.Ads;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyAppSingleJsonPass extends Application {
    private static MyAppSingleJsonPass appSingleJsonPass;
    private RequestQueue queue;
    private static Context mCtx;

    private MyAppSingleJsonPass(Context context) {
        mCtx = context;
        queue = getRequestQueue();
    }

    public static synchronized MyAppSingleJsonPass getInstance(Context context) {
        if (appSingleJsonPass == null) {
            appSingleJsonPass = new MyAppSingleJsonPass(context);
        }
        return appSingleJsonPass;
    }

    public RequestQueue getRequestQueue() {
        if (queue == null) {
            queue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return queue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}

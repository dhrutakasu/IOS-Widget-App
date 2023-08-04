package com.ios.widget.Ads;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyAppSingleJsonPass extends Application {
    private static MyAppSingleJsonPass mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private MyAppSingleJsonPass(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized MyAppSingleJsonPass getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyAppSingleJsonPass(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}

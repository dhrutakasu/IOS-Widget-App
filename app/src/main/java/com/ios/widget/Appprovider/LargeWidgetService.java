package com.ios.widget.Appprovider;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class LargeWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new LargeRemoteView(getApplicationContext(), intent);
    }
}

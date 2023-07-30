package com.ios.widget.provider;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class SmallWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new SmallRemoteView(getApplicationContext(), intent);
    }
}

package com.ios.widget.Appprovider;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class MediumWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MediumRemoteView(this.getApplicationContext(), intent);
    }
}


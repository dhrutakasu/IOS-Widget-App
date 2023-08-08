package com.ios.widget.cropiwa.image;

import android.net.Uri;

public interface CropIwaResultListener {

    void onCropSuccess(Uri croppedUri);

    void onCropFailed(Throwable e);
}

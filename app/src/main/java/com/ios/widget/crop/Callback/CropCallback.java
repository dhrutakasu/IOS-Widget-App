package com.ios.widget.crop.Callback;

import android.graphics.Bitmap;

public interface CropCallback extends Callback {
  void onSuccess(Bitmap cropped);
}

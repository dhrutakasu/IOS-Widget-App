package com.ios.widget.animation;

public interface SimpleValueAnimatorListener {
  void onAnimationStarted();

  void onAnimationUpdated(float scale);

  void onAnimationFinished();
}

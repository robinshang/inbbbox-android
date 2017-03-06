package co.netguru.android.inbbbox.feature.shared.view;

import android.view.animation.Animation;

public final class OnAnimationEndListener implements Animation.AnimationListener {

    private final Runnable runnable;

    public OnAnimationEndListener(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        // no-op
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        runnable.run();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // no-op
    }
}

package co.netguru.android.inbbbox.feature.shared.view;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

public abstract class AnimationDrawableCallback implements Callback {

    private WeakReference<Drawable> lastFrameWeakReference;
    private WeakReference<Callback> callbackWeakReference;
    private boolean shouldFinishAnimation = false;

    public AnimationDrawableCallback(AnimationDrawable animationDrawable, Callback callback) {
        lastFrameWeakReference = new WeakReference<>(
                animationDrawable.getFrame(animationDrawable.getNumberOfFrames() - 1));
        callbackWeakReference = new WeakReference<>(callback);
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable who) {
        final Callback callback = callbackWeakReference.get();
        if (callback != null) {
            callback.invalidateDrawable(who);
        }
        checkCanFinishAnimation(who);
    }

    @Override
    public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
        final Callback callback = callbackWeakReference.get();
        if (callback != null) {
            callback.scheduleDrawable(who, what, when);
        }
    }

    @Override
    public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
        final Callback callback = callbackWeakReference.get();
        if (callback != null) {
            callback.unscheduleDrawable(who, what);
        }
    }

    public void setShouldFinishAnimation(boolean shouldFinishAnimation) {
        this.shouldFinishAnimation = shouldFinishAnimation;
    }

    public abstract void onAnimationComplete();

    private void checkCanFinishAnimation(@NonNull Drawable who) {
        final Drawable lastFrame = lastFrameWeakReference.get();

        if (shouldFinishAnimation && lastFrame != null && lastFrame.equals(who.getCurrent())) {
            shouldFinishAnimation = false;
            onAnimationComplete();
        }
    }
}
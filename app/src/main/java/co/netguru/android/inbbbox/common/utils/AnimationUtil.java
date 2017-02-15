package co.netguru.android.inbbbox.common.utils;

import android.content.res.Resources;
import android.view.View;

import timber.log.Timber;

public class AnimationUtil {

    private static final float SLID_IN_DESIGNATED_TRANSLATION = 0;
    private static final float TRANSLATION_INITIAL_OFFSET = 550f;
    public static final float ALPHA_MIN = 0f;
    public static final float ALPHA_MAX = 1f;

    private AnimationUtil() {
        throw new AssertionError();
    }

    public static void startSlideInFromBottomShowAnimation(View view, int duration) {
        view.setTranslationY(TRANSLATION_INITIAL_OFFSET);
        view.animate()
                .setDuration(duration)
                .translationY(SLID_IN_DESIGNATED_TRANSLATION)
                .withStartAction(() -> view.setVisibility(View.VISIBLE))
                .start();
    }

    public static void scaleView(View v, float percent) {
        scaleView(v, percent, 0, v.getHeight() / 2f);
    }

    public static void scaleView(View v, float percent, float pivotX, float pivotY) {
        if (percent > 0 && percent < 1) {
            v.setPivotX(pivotX);
            v.setPivotY(pivotY);
            v.setScaleX(percent);
            v.setScaleY(percent);
        } else {
            v.setScaleX(1);
            v.setScaleY(1);
        }
    }

    public static void translateView(View v, int progress, float percent) {
        if (percent >= 0 && percent <= 1) {
            int horizontalTranslation = progress - v.getLeft() - ((int) (v.getWidth() * percent));
            v.setTranslationX(horizontalTranslation);
        } else {
            v.setTranslationX(0);
        }
    }

    public static void animateAlpha(View view, float fromProgress, float toProgress,
                              float currentProgress) {
        animateAlpha(view, fromProgress, toProgress, currentProgress, false);
    }

    public static void animateAlpha(View view, float fromProgress, float toProgress,
                              float currentProgress, boolean reverse) {
        if (currentProgress <= fromProgress) {
            view.setAlpha(reverse ? ALPHA_MAX : ALPHA_MIN);
        } else if (currentProgress >= toProgress) {
            view.setAlpha(reverse ? ALPHA_MIN : ALPHA_MAX);
        } else if (currentProgress > fromProgress && currentProgress < toProgress) {
            float alpha = (currentProgress - fromProgress) / (toProgress - fromProgress);
            view.setAlpha(reverse ? 1 - alpha : alpha);
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}

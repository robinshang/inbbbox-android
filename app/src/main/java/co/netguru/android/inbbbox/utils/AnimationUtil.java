package co.netguru.android.inbbbox.utils;

import android.view.View;

public class AnimationUtil {

    private static final long SLIDE_IN_DURATION = 700;
    private static final float SLID_IN_DESIGNATED_TRANSLATION = 0;

    private AnimationUtil() {
        throw new AssertionError();
    }

    public static void startSlideInFromBottomShowAnimation(View view) {
        view.setTranslationY(view.getHeight());
        view.animate()
                .setDuration(SLIDE_IN_DURATION)
                .translationY(SLID_IN_DESIGNATED_TRANSLATION)
                .withStartAction(() -> view.setVisibility(View.VISIBLE))
                .start();
    }
}

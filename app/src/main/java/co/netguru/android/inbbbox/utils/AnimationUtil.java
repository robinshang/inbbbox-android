package co.netguru.android.inbbbox.utils;

import android.view.View;

public class AnimationUtil {

    private static final float SLID_IN_DESIGNATED_TRANSLATION = 0;
    private static final float TRANSLATION_INITIAL_OFFSET = 550f;

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
}

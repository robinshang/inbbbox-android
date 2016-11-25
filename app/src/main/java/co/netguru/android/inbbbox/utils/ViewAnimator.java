package co.netguru.android.inbbbox.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import co.netguru.android.inbbbox.R;

public class ViewAnimator {

    private ViewAnimator() {
        throw new AssertionError();
    }

    public static void startSlideInFromBottomShowAnimation(View view) {
        Animation anim = AnimationUtils
                .loadAnimation(view.getContext(), R.anim.slide_in_animation);
        view.setAnimation(anim);
        view.animate()
                .withStartAction(() -> view.setVisibility(View.VISIBLE))
                .start();
    }

    public static void startSlideInFromRightShowAnimation(View view) {
        Animation anim = AnimationUtils
                .loadAnimation(view.getContext(), R.anim.slide_in_right_animation);
        view.setAnimation(anim);
        view.animate()
                .withStartAction(() -> view.setVisibility(View.VISIBLE))
                .start();
    }

    public static void startSlideInFromRightHideAnimation(View view) {
        Animation anim = AnimationUtils
                .loadAnimation(view.getContext(), R.anim.slide_out_left_animation);
        view.setAnimation(anim);
        view.animate()
                .withEndAction(() -> view.setVisibility(View.GONE))
                .start();
    }

    public static void startSlideOutHideAnimation(View view) {
        Animation anim = AnimationUtils
                .loadAnimation(view.getContext(), R.anim.slide_out_animation);
        view.setAnimation(anim);
        view.animate()
                .withEndAction(() -> view.setVisibility(View.GONE))
                .start();
    }
}

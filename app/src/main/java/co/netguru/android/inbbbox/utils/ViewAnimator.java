package co.netguru.android.inbbbox.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import co.netguru.android.inbbbox.R;

public class ViewAnimator {

    private ViewAnimator() {
        throw new AssertionError();
    }

    public static void startSlideInAnimation(View view) {
        Animation anim = AnimationUtils
                .loadAnimation(view.getContext(), R.anim.slide_in_animation);
        view.setAnimation(anim);
        view.animate()
                .withStartAction(() -> view.setVisibility(View.VISIBLE))
                .start();
    }

    public static void startSlideOutAnimation(View view) {
        Animation anim = AnimationUtils
                .loadAnimation(view.getContext(), R.anim.slide_out_animation);
        view.setAnimation(anim);
        view.animate()
                .withEndAction(() -> view.setVisibility(View.GONE))
                .start();
    }
}

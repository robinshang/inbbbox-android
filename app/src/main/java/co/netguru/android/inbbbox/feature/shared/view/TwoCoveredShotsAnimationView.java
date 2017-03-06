package co.netguru.android.inbbbox.feature.shared.view;

import android.content.Context;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

public class TwoCoveredShotsAnimationView extends FrameLayout {

    @BindView(R.id.two_covered_shots_first_view)
    RoundedCornersShotImageView firstShotImageView;
    @BindView(R.id.two_covered_shots_second_view)
    RoundedCornersShotImageView secondShotImageView;

    public TwoCoveredShotsAnimationView(Context context) {
        super(context);
        init();
    }

    public TwoCoveredShotsAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TwoCoveredShotsAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void loadShotsAndStartAnimation(@NonNull Shot firstShot, @NonNull Shot secondShot,
                                           @NonNull OnAnimationEndCallback onAnimationEndCallback) {
        loadShotWithAnimation(firstShotImageView, firstShot, () -> {
            firstShotImageView.setVisibility(VISIBLE);
            loadSecondShotWithAnimation(secondShot, onAnimationEndCallback);
        });
    }

    private void loadSecondShotWithAnimation(@NonNull Shot secondShot,
                                             @NonNull OnAnimationEndCallback onAnimationEndCallback) {
        loadShotWithAnimation(secondShotImageView, secondShot, () -> {
            secondShotImageView.setVisibility(VISIBLE);
            startSecondShotDropDownAnimation(onAnimationEndCallback);
        });
    }

    private void loadShotWithAnimation(@NonNull RoundedCornersShotImageView view, @NonNull Shot shot,
                                       Runnable runnable) {
        view.setVisibility(INVISIBLE);
        view.loadBlurredShotWithListener(shot, createRequestListener(view,
                R.anim.two_shots_load_shot_animation, runnable));
    }

    private void startSecondShotDropDownAnimation(OnAnimationEndCallback onAnimationEndCallback) {
        startAnimation(secondShotImageView, R.anim.two_shots_second_shot_drop_down_animation, () -> {
            secondShotImageView.setVisibility(GONE);
            startFirstShotDropDownAnimation(onAnimationEndCallback);
        });
    }

    private void startFirstShotDropDownAnimation(@NonNull OnAnimationEndCallback onAnimationEndCallback) {
        startAnimation(firstShotImageView, R.anim.two_shots_first_shot_drop_down_animation, () -> {
            firstShotImageView.setVisibility(GONE);
            onAnimationEndCallback.onAnimationEnd();
        });
    }

    private RequestListener<String, GlideDrawable> createRequestListener(RoundedCornersShotImageView view,
                                                                         @AnimRes int animationRes,
                                                                         Runnable runnable) {
        return new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                                       boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                           boolean isFromMemoryCache, boolean isFirstResource) {
                startAnimation(view, animationRes, runnable);
                return true;
            }
        };
    }

    private void startAnimation(RoundedCornersShotImageView view, @AnimRes int animRes,
                                Runnable runnable) {
        final Animation animation = AnimationUtils.loadAnimation(getContext(), animRes);
        animation.setAnimationListener(new OnAnimationEndListener(runnable));
        view.startAnimation(animation);
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.two_covered_shots_animation_view, this);
        ButterKnife.bind(this);
    }

    @FunctionalInterface
    public interface OnAnimationEndCallback {

        void onAnimationEnd();
    }
}
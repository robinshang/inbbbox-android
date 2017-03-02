package co.netguru.android.inbbbox.feature.shared.view;

import android.content.Context;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

public class TwoCoveredShotsAnimationView extends FrameLayout {

    @BindView(R.id.two_covered_shots_first_view)
    RoundedCornersShotImageView firstShotImageView;
    @BindView(R.id.two_covered_shots_second_view)
    RoundedCornersShotImageView secondShotImageView;

    @NonNull
    private OnAnimationEndListener onAnimationEndListener = OnAnimationEndListener.NULL;

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

    public void loadShots(@NonNull Shot firstShot, @NonNull Shot secondShot) {
        firstShotImageView.loadBlurredShot(firstShot);
        secondShotImageView.loadBlurredShot(secondShot);
        makeViewsVisible();
    }

    public void startAnimation(@NonNull OnAnimationEndListener onAnimationEndListener) {
        this.onAnimationEndListener = onAnimationEndListener;
        startViewAnimation(firstShotImageView, R.anim.three_shots_first_shot_drop_down_animation, true);
        startViewAnimation(secondShotImageView, R.anim.three_shots_second_shot_drop_down_animation, false);
    }

    private void startViewAnimation(RoundedCornersShotImageView view, @AnimRes int animationRes,
                                         boolean shouldHideParentView) {
        final Animation animation = AnimationUtils.loadAnimation(getContext(), animationRes);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //no-op
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(GONE);
                if (shouldHideParentView) {
                    setVisibility(GONE);
                    onAnimationEndListener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //no-op
            }
        });
        view.startAnimation(animation);
    }

    private void makeViewsVisible() {
        setVisibility(VISIBLE);
        firstShotImageView.setVisibility(VISIBLE);
        secondShotImageView.setVisibility(VISIBLE);
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.two_covered_shots_animation_view, this);
        ButterKnife.bind(this);
    }

    public interface OnAnimationEndListener {

        OnAnimationEndListener NULL = () -> { /* no-op */ };

        void onAnimationEnd();
    }
}

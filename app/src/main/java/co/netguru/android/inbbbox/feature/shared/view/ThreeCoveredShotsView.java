package co.netguru.android.inbbbox.feature.shared.view;

import android.content.Context;
import android.support.annotation.AnimRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;

public class ThreeCoveredShotsView extends FrameLayout {

    @BindView(R.id.three_covered_shots_first_view)
    RoundedCornersShotImageView firstShotImageView;
    @BindView(R.id.three_covered_shots_second_view)
    RoundedCornersShotImageView secondShotImageView;
    @BindView(R.id.three_covered_shots_third_view)
    RoundedCornersShotImageView thirdShotImageView;

    public ThreeCoveredShotsView(Context context) {
        super(context);
        init();
    }

    public ThreeCoveredShotsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ThreeCoveredShotsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void startAnimation() {
        startViewAnimation(thirdShotImageView, R.anim.three_shots_third_shot_drop_down_animation, false);
        startViewAnimation(secondShotImageView, R.anim.three_shots_second_shot_drop_down_animation, true);
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
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //no-op
            }
        });
        view.startAnimation(animation);
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.triple_shots_view, this);
        ButterKnife.bind(this);
    }
}

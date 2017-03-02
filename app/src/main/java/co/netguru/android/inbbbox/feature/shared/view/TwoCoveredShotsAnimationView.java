package co.netguru.android.inbbbox.feature.shared.view;

import android.content.Context;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
                                           @NonNull OnAnimationEndListener onAnimationEndListener) {
        setVisibility(VISIBLE);
        loadShotWithAnimation(firstShotImageView, firstShot,
                R.anim.two_shots_first_shot_drop_down_animation, onAnimationEndListener);
        loadShotWithAnimation(secondShotImageView, secondShot,
                R.anim.two_shots_second_shot_drop_down_animation, null);
    }

    private void loadShotWithAnimation(RoundedCornersShotImageView view, Shot shot, @AnimRes int animationRes,
                                       @Nullable OnAnimationEndListener onAnimationEndListener) {
        view.loadBlurredShotWithListener(shot, new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                                       boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                           boolean isFromMemoryCache, boolean isFirstResource) {
                startDropDownAnimation(view, animationRes, onAnimationEndListener);
                return true;
            }
        });
    }

    private void startDropDownAnimation(RoundedCornersShotImageView view, @AnimRes int animationRes,
                                           @Nullable OnAnimationEndListener onAnimationEndListener) {
        final Animation animation = AnimationUtils.loadAnimation(getContext(), animationRes);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // no-op
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(GONE);
                if (onAnimationEndListener != null) {
                    onAnimationEndListener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // no-op
            }
        });
        view.startAnimation(animation);

    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.two_covered_shots_animation_view, this);
        ButterKnife.bind(this);
    }

    public interface OnAnimationEndListener {

        void onAnimationEnd();
    }
}

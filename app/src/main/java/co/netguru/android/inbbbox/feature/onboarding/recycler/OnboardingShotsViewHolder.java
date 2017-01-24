package co.netguru.android.inbbbox.feature.onboarding.recycler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.onboarding.OnboardingShot;
import co.netguru.android.inbbbox.feature.onboarding.OnboardingShotSwipeListener;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersShotImageView;
import co.netguru.android.inbbbox.feature.shared.view.swipingpanel.ItemSwipeListener;
import co.netguru.android.inbbbox.feature.shared.view.swipingpanel.LongSwipeLayout;

class OnboardingShotsViewHolder extends BaseViewHolder<OnboardingShot>
        implements ItemSwipeListener {

    public static final int ALPHA_MAX = 255;
    public static final int ALPHA_MIN = 0;
    public static final int LOCATION_ON_SCREEN_COORDINATES_NUMBER = 2;
    public static final float COMMENT_TRANSLATION_FACTOR = 1 / 3f;
    private final OnboardingShotSwipeListener shotSwipeListener;
    @BindView(R.id.long_swipe_layout)
    @Nullable
    LongSwipeLayout longSwipeLayout;
    @BindView(R.id.iv_shot_image)
    RoundedCornersShotImageView shotImageView;
    @BindView(R.id.iv_like_action)
    @Nullable
    ImageView likeIconImageView;
    @BindView(R.id.iv_plus_image)
    @Nullable
    ImageView plusIconImageView;
    @BindView(R.id.iv_bucket_action)
    @Nullable
    ImageView bucketImageView;
    @BindView(R.id.iv_comment)
    @Nullable
    ImageView commentImageView;
    @BindView(R.id.iv_follow)
    @Nullable
    ImageView followImageView;
    @BindView(R.id._left_wrapper)
    @Nullable
    LinearLayout leftWrapper;
    private OnboardingShot onboardingShot;

    OnboardingShotsViewHolder(View itemView, @NonNull OnboardingShotSwipeListener shotSwipeListener) {
        super(itemView);
        this.shotSwipeListener = shotSwipeListener;

        if (longSwipeLayout != null) {
            longSwipeLayout.setItemSwipeListener(this);
        }
    }

    @OnClick(R.id.iv_shot_image)
    void onShotClick() {
        shotSwipeListener.onShotSelected(onboardingShot);
    }

    @Override
    public void bind(OnboardingShot shot) {
        this.onboardingShot = shot;
        setupImage(shot);

        // this is needed because comment image view would be deactivated on last pixel of swipe
        if (shot.getStep() == OnboardingShot.STEP_COMMENT) {
            longSwipeLayout.setSwipeLimitShift(-1);
        }
    }

    private void setupImage(OnboardingShot shot) {
        Glide.clear(shotImageView);
        Glide.with(shotImageView.getContext())
                .load(shot.getDrawableResourceId())
                .fitCenter()
                .into(shotImageView.getImageView());
    }

    @Override
    public void onLeftSwipe() {
        shotSwipeListener.onShotLikeSwipe(onboardingShot);
    }

    @Override
    public void onLeftLongSwipe() {
        shotSwipeListener.onAddShotToBucketSwipe(onboardingShot);
    }

    @Override
    public void onRightSwipe() {
        shotSwipeListener.onCommentShotSwipe(onboardingShot);
    }

    @Override
    public void onRightLongSwipe() {
        shotSwipeListener.onFollowUserSwipe(onboardingShot);
    }

    @Override
    public void onLeftSwipeActivate(boolean isActive) {
        if (likeIconImageView != null) {
            likeIconImageView.setActivated(isActive);
        }
    }

    @Override
    public void onLeftLongSwipeActivate(boolean isActive) {
        if (bucketImageView != null) {
            bucketImageView.setActivated(isActive);
        }
    }

    @Override
    public void onRightSwipeActivate(boolean isActive) {
        if (commentImageView != null) {
            commentImageView.setActivated(isActive);
        }
    }

    @Override
    public void onRightLongSwipeActivate(boolean isActive) {
        if (followImageView != null) {
            followImageView.setVisibility(isActive ? View.VISIBLE : View.INVISIBLE);
            commentImageView.setImageAlpha(isActive ? ALPHA_MIN : ALPHA_MAX);
        }
    }

    @Override
    public void onSwipeProgress(int positionX, int swipeLimit) {
        if (positionX > 0) {
            handleLeftSwipe();
        } else if (onboardingShot.getStep() >= OnboardingShot.STEP_COMMENT) {
            handleRightSwipe(positionX, swipeLimit);
        }
    }

    private void handleLeftSwipe() {
        int[] shotAbsoluteCoordinates = new int[LOCATION_ON_SCREEN_COORDINATES_NUMBER];
        shotImageView.getLocationOnScreen(shotAbsoluteCoordinates);
        int[] leftWrapperAbsoluteCoordinates = new int[LOCATION_ON_SCREEN_COORDINATES_NUMBER];
        leftWrapper.getLocationOnScreen(leftWrapperAbsoluteCoordinates);
        int diff = leftWrapperAbsoluteCoordinates[0] - leftWrapper.getLeft();
        int progress = shotAbsoluteCoordinates[0] - diff;
        int likeIconImageViewLeft = likeIconImageView.getLeft();

        float percent = getPercent(progress, likeIconImageViewLeft, likeIconImageView.getWidth());
        float plusPercent = 0;
        float bucketPercent = 0;
        if (onboardingShot.getStep() >= OnboardingShot.STEP_BUCKET) {
            plusPercent = getPercent(progress, plusIconImageView.getLeft(), plusIconImageView.getWidth());
            bucketPercent = getPercent(progress, bucketImageView.getLeft(), bucketImageView.getWidth());
        }

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setFillAfter(true);

        if (percent <= 1) {
            int horizontalTranslation = progress - likeIconImageViewLeft - ((int) (likeIconImageView.getWidth() * percent));
            animationSet.addAnimation(new ScaleAnimation(percent, percent, percent, percent, 0, likeIconImageView.getHeight() / 2));
            animationSet.addAnimation(new TranslateAnimation(horizontalTranslation, horizontalTranslation, 0, 0));
            likeIconImageView.startAnimation(animationSet);
        } else if (plusPercent <= 1 && onboardingShot.getStep() >= OnboardingShot.STEP_BUCKET) {
            //in case of fast swipe
            likeIconImageView.clearAnimation();

            animationSet.addAnimation(new ScaleAnimation(plusPercent, plusPercent, plusPercent, plusPercent, 0, plusIconImageView.getHeight() / 2));
            plusIconImageView.startAnimation(animationSet);
        } else if (bucketPercent <= 1 && onboardingShot.getStep() >= OnboardingShot.STEP_COMMENT) {
            //in case of fast swipe
            plusIconImageView.clearAnimation();

            animationSet.addAnimation(new ScaleAnimation(bucketPercent, bucketPercent, bucketPercent, bucketPercent, 0, bucketImageView.getHeight() / 2));
            bucketImageView.startAnimation(animationSet);
        } else if (onboardingShot.getStep() >= OnboardingShot.STEP_COMMENT) {
            //in case of fast swipe
            bucketImageView.clearAnimation();
        }
    }

    private void handleRightSwipe(int positionX, int swipeLimit) {
        float progress = (float) -positionX / (float) swipeLimit;

        float horizontalTranslation = positionX * COMMENT_TRANSLATION_FACTOR;
        commentImageView.setTranslationX(horizontalTranslation);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setFillAfter(true);
        animationSet.addAnimation(new ScaleAnimation(progress, progress, progress, progress,
                commentImageView.getWidth(), commentImageView.getHeight() / 2));
        commentImageView.startAnimation(animationSet);
    }

    private float getPercent(int progress, int viewLeft, int viewWidth) {
        int percent = 0;
        if (viewLeft != 0 || viewWidth != 0) {
            percent = 100 * progress / (viewLeft + viewWidth);
        }
        return percent / 100f;
    }
}
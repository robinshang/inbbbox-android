package co.netguru.android.inbbbox.feature.onboarding.recycler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.onboarding.OnboardingShotSwipeListener;
import co.netguru.android.inbbbox.feature.onboarding.OnboardingStep;
import co.netguru.android.inbbbox.feature.onboarding.OnboardingStepData;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersShotImageView;
import co.netguru.android.inbbbox.feature.shared.view.swipingpanel.ItemSwipeListener;
import co.netguru.android.inbbbox.feature.shared.view.swipingpanel.LongSwipeLayout;

class OnboardingShotsViewHolder extends BaseViewHolder<OnboardingStep>
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
    ImageView likeIcon;
    @BindView(R.id.iv_plus_image)
    @Nullable
    ImageView plusIcon;
    @BindView(R.id.iv_bucket_action)
    @Nullable
    ImageView bucketIcon;
    @BindView(R.id.iv_comment)
    @Nullable
    ImageView commentIcon;
    @BindView(R.id.iv_follow)
    @Nullable
    ImageView followIcon;
    @BindView(R.id._left_wrapper)
    @Nullable
    LinearLayout leftWrapper;
    private OnboardingStep onboardingShot;

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

    @OnClick(R.id.skip_follow)
    @Optional
    void onSkipClick() {
        shotSwipeListener.onSkip(onboardingShot);
    }

    @Override
    public void bind(OnboardingStep shot) {
        this.onboardingShot = shot;
        setupImage(shot);
        setupSwipeLayout(shot);
    }

    private void setupImage(OnboardingStep shot) {
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
        if (likeIcon != null) {
            likeIcon.setActivated(isActive);
        }
    }

    @Override
    public void onLeftLongSwipeActivate(boolean isActive) {
        if (bucketIcon != null) {
            bucketIcon.setActivated(isActive);
        }
    }

    @Override
    public void onRightSwipeActivate(boolean isActive) {
        if (commentIcon != null) {
            commentIcon.setActivated(isActive);
        }
    }

    @Override
    public void onRightLongSwipeActivate(boolean isActive) {
        if (followIcon != null) {
            followIcon.setVisibility(isActive ? View.VISIBLE : View.INVISIBLE);
            commentIcon.setImageAlpha(isActive ? ALPHA_MIN : ALPHA_MAX);
        }
    }

    @Override
    public void onSwipeProgress(int positionX, int swipeLimit) {
        if (positionX > 0) {
            handleLeftSwipe();
        } else if (onboardingShot.getStep() >= OnboardingStepData.STEP_COMMENT.getStep()) {
            handleRightSwipe(positionX, swipeLimit);
        }
    }

    private void handleLeftSwipe() {
        int progress = getSwipeProgress();
        handleLikeAnimation(progress);
        handlePlusAndBucketAnimation(progress);
    }

    private void handleLikeAnimation(int progress) {
        int likeIconLeft = likeIcon.getLeft();
        float likePercent = getPercent(progress, likeIconLeft, likeIcon.getWidth());

        translateView(likeIcon, progress, likePercent);
        scaleView(likeIcon, likePercent);
        handlePlusAndBucketAnimation(progress);
    }

    private void handlePlusAndBucketAnimation(int progress) {
        if (onboardingShot.getStep() >= OnboardingStepData.STEP_BUCKET.getStep()) {
            float plusPercent = getPercent(progress, plusIcon.getLeft(), plusIcon.getWidth());
            float bucketPercent = getPercent(progress, bucketIcon.getLeft(), bucketIcon.getWidth());

            scaleView(plusIcon, plusPercent);
            scaleView(bucketIcon, bucketPercent);
        }
    }

    private void handleRightSwipe(int positionX, int swipeLimit) {
        float progress = (float) -positionX / (float) swipeLimit;
        float horizontalTranslation = positionX * COMMENT_TRANSLATION_FACTOR;

        commentIcon.setTranslationX(horizontalTranslation);
        scaleView(commentIcon, progress, commentIcon.getWidth(), commentIcon.getHeight() / 2f);
    }

    private void scaleView(View v, float percent) {
        scaleView(v, percent, 0, v.getHeight() / 2f);
    }

    private void scaleView(View v, float percent, float pivotX, float pivotY) {
        if (percent > 0 && percent < 1) {
            v.setPivotX(pivotX);
            v.setPivotY(pivotY);
            v.setScaleX(percent);
            v.setScaleY(percent);
        }
    }

    private void translateView(View v, int progress, float percent) {
        if (percent > 0 && percent < 1) {
            int horizontalTranslation = progress - v.getLeft() - ((int) (v.getWidth() * percent));
            v.setTranslationX(horizontalTranslation);
        }
    }

    private int getSwipeProgress() {
        int[] shotAbsoluteCoordinates = new int[LOCATION_ON_SCREEN_COORDINATES_NUMBER];
        shotImageView.getLocationOnScreen(shotAbsoluteCoordinates);

        int[] leftWrapperAbsoluteCoordinates = new int[LOCATION_ON_SCREEN_COORDINATES_NUMBER];
        leftWrapper.getLocationOnScreen(leftWrapperAbsoluteCoordinates);

        int diff = leftWrapperAbsoluteCoordinates[0] - leftWrapper.getLeft();
        return shotAbsoluteCoordinates[0] - diff;
    }

    private float getPercent(int progress, int viewLeft, int viewWidth) {
        if (viewLeft != 0 || viewWidth != 0) {
            return (float) progress / (viewLeft + viewWidth);
        } else {
            return 0;
        }
    }

    private void setupSwipeLayout(OnboardingStep shot) {
        // this is needed because comment image view would be deactivated on last pixel of swipe
        if (shot.getStep() == OnboardingStepData.STEP_COMMENT.getStep()) {
            longSwipeLayout.setSwipeLimitShift(Constants.UNDEFINED);
        }
    }
}
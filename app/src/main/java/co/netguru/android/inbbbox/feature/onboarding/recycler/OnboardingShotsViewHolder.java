package co.netguru.android.inbbbox.feature.onboarding.recycler;

import android.support.annotation.NonNull;
import android.view.View;

import com.bumptech.glide.Glide;

import butterknife.OnClick;
import butterknife.Optional;
import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.onboarding.OnboardingShotSwipeListener;
import co.netguru.android.inbbbox.feature.onboarding.OnboardingStep;
import co.netguru.android.inbbbox.feature.onboarding.OnboardingStepData;
import co.netguru.android.inbbbox.feature.shot.recycler.BaseShotsViewHolder;

import static co.netguru.android.inbbbox.common.utils.UnitsUtils.dpToPx;

public class OnboardingShotsViewHolder extends BaseShotsViewHolder<OnboardingStep> {

    public static final int ONBOARDING_SWIPE_LIMIT_SHIFT_DP = -24;

    private final OnboardingShotSwipeListener shotSwipeListener;
    private OnboardingStep onboardingShot;

    public OnboardingShotsViewHolder(View view, @NonNull
            OnboardingShotSwipeListener shotSwipeListener) {
        super(view);
        this.shotSwipeListener = shotSwipeListener;
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
    public void bind(OnboardingStep item) {
        this.onboardingShot = item;
        setupImage(item);
        setupSwipeLayout(item);
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
    public void onStartSwipe() {
        // no-op
    }

    @Override
    public void onEndSwipe() {
        // no-op
    }

    @Override
    protected void handlePlusAndBucketAnimation(int swipePosition) {
        if (onboardingShot.getStep() >= OnboardingStepData.STEP_BUCKET.getStep()) {
            super.handlePlusAndBucketAnimation(swipePosition);
        }
    }

    @Override
    protected void handleRightSwipe(float progress, int swipePosition) {
        if (onboardingShot.getStep() >= OnboardingStepData.STEP_COMMENT.getStep()) {
            super.handleRightSwipe(progress, swipePosition);
        }
    }

    private void setupImage(OnboardingStep shot) {
        Glide.clear(shotImageView);
        Glide.with(shotImageView.getContext())
                .load(shot.getDrawableResourceId())
                .fitCenter()
                .into(shotImageView.getImageView());
    }

    private void setupSwipeLayout(OnboardingStep shot) {
        if (shot.getStep() == OnboardingStepData.STEP_COMMENT.getStep()) {
            longSwipeLayout.setSwipeLimitShift(dpToPx(ONBOARDING_SWIPE_LIMIT_SHIFT_DP));
        }
    }
}

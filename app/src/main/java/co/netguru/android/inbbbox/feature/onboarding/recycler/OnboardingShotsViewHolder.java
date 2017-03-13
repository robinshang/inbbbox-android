package co.netguru.android.inbbbox.feature.onboarding.recycler;

import android.support.annotation.NonNull;
import android.view.View;

import com.bumptech.glide.Glide;

import butterknife.OnClick;
import butterknife.Optional;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.onboarding.OnboardingShotSwipeListener;
import co.netguru.android.inbbbox.feature.onboarding.OnboardingStep;
import co.netguru.android.inbbbox.feature.onboarding.OnboardingStepData;
import co.netguru.android.inbbbox.feature.shot.recycler.BaseShotsViewHolder;

import static co.netguru.android.inbbbox.common.utils.UnitsUtils.dpToPx;

public class OnboardingShotsViewHolder extends BaseShotsViewHolder<OnboardingStep> {

    private static final int ONBOARDING_SWIPE_LIMIT_SHIFT_DP = -24;

    private final OnboardingShotSwipeListener shotSwipeListener;
    private OnboardingStep onboardingShot;

    public OnboardingShotsViewHolder(View view, @NonNull
            OnboardingShotSwipeListener shotSwipeListener) {
        super(view);
        this.shotSwipeListener = shotSwipeListener;
    }

    @Override
    public void bind(OnboardingStep item) {
        this.onboardingShot = item;
        setupImage(item);
        setupSwipeLayout(item);
    }

    @Override
    public void onLeftSwipe() {
        if (onboardingShot.getStep() == OnboardingStepData.STEP_LIKE.getStep()) {
            animateShotView(false);
            animateAndFinishLike(() -> shotSwipeListener.onShotLikeSwipe(onboardingShot));
        } else {
            longSwipeLayout.close();
        }
    }

    @Override
    public void onLeftLongSwipe() {
        if (onboardingShot.getStep() == OnboardingStepData.STEP_BUCKET.getStep()) {
            animateShotView(false);
            animateAndFinishAddToBucket(() -> shotSwipeListener.onAddShotToBucketSwipe(onboardingShot));
        } else if (onboardingShot.getStep() == OnboardingStepData.STEP_LIKE.getStep()) {
            animateShotView(false);
            animateAndFinishLike(() -> shotSwipeListener.onShotLikeSwipe(onboardingShot));
        } else {
            longSwipeLayout.close();
        }
    }

    @Override
    public void onRightSwipe() {
        if (onboardingShot.getStep() == OnboardingStepData.STEP_COMMENT.getStep()) {
            animateShotView(true);
            animateAndFinishComment(() -> shotSwipeListener.onCommentShotSwipe(onboardingShot));
        } else
            longSwipeLayout.close();
    }

    @Override
    public void onRightLongSwipe() {
        if (onboardingShot.getStep() == OnboardingStepData.STEP_FOLLOW.getStep()) {
            animateShotView(true);
            animateAndFinishFollowUser(() -> shotSwipeListener.onFollowUserSwipe(onboardingShot));
        } else if (onboardingShot.getStep() == OnboardingStepData.STEP_COMMENT.getStep()) {
            animateShotView(true);
            animateAndFinishComment(() -> shotSwipeListener.onCommentShotSwipe(onboardingShot));
        } else
            longSwipeLayout.close();
    }

    @Override
    public void onStartSwipe() {
        // no-op
    }

    @Override
    public void onEndSwipe() {
        // no-op
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

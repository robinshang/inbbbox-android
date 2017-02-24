package co.netguru.android.inbbbox.feature.onboarding;

public interface OnboardingShotSwipeListener {
    void onShotLikeSwipe(OnboardingStep step);

    void onAddShotToBucketSwipe(OnboardingStep step);

    void onCommentShotSwipe(OnboardingStep step);

    void onFollowUserSwipe(OnboardingStep step);

    void onSkip(OnboardingStep step);

    void onShotSelected(OnboardingStep step);
}

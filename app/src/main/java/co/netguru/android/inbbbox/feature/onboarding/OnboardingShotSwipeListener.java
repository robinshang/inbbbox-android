package co.netguru.android.inbbbox.feature.onboarding;

public interface OnboardingShotSwipeListener {
    void onShotLikeSwipe(OnboardingStep shot);

    void onAddShotToBucketSwipe(OnboardingStep shot);

    void onCommentShotSwipe(OnboardingStep shot);

    void onFollowUserSwipe(OnboardingStep shot);

    void onShotSelected(OnboardingStep shot);
}

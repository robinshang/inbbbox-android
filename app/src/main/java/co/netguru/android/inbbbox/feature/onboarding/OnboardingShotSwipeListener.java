package co.netguru.android.inbbbox.feature.onboarding;

public interface OnboardingShotSwipeListener {
    void onShotLikeSwipe(OnboardingShot shot);

    void onAddShotToBucketSwipe(OnboardingShot shot);

    void onCommentShotSwipe(OnboardingShot shot);

    void onFollowUserSwipe(OnboardingShot shot);

    void onShotSelected(OnboardingShot shot);
}

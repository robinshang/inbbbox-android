package co.netguru.android.inbbbox.feature.onboarding;

import co.netguru.android.inbbbox.R;

public enum OnboardingStepData {

    STEP_LIKE(0, R.drawable.onboarding_step_1, R.layout.item_shot_onboarding_step_1),
    STEP_BUCKET(1, R.drawable.onboarding_step_2, R.layout.item_shot_onboarding_step_2),
    STEP_COMMENT(2, R.drawable.onboarding_step_3, R.layout.item_shot_onboarding_step_3),
    STEP_FOLLOW(3, R.drawable.onboarding_step_4, R.layout.item_shot_onboarding_step_4),
    STEP_CLICK(4, R.drawable.onboarding_step_5, R.layout.item_shot_onboarding_step_5);

    private final int step;
    private final int drawableResourceId;
    private final int layoutResourceId;

    OnboardingStepData(int step, int drawableResourceId, int layoutResourceId) {
        this.step = step;
        this.drawableResourceId = drawableResourceId;
        this.layoutResourceId = layoutResourceId;
    }

    public int getStep() {
        return step;
    }

    public int getDrawableResourceId() {
        return drawableResourceId;
    }

    public int getLayoutResourceId() {
        return layoutResourceId;
    }
}
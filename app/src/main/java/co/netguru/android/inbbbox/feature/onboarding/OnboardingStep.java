package co.netguru.android.inbbbox.feature.onboarding;

public class OnboardingStep {

    private int step;
    private int drawableResourceId;
    private int layoutResourceId;

    public OnboardingStep(OnboardingStepData data) {
        this.step = data.getStep();
        this.drawableResourceId = data.getDrawableResourceId();
        this.layoutResourceId = data.getLayoutResourceId();
    }

    public int getDrawableResourceId() {
        return drawableResourceId;
    }

    public int getStep() {
        return step;
    }

    public int getLayoutResourceId() {
        return layoutResourceId;
    }

}

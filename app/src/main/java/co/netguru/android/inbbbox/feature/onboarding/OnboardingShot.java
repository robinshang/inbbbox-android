package co.netguru.android.inbbbox.feature.onboarding;

public class OnboardingShot {

    public static final int STEP_LIKE = 0;
    public static final int STEP_BUCKET = 1;
    public static final int STEP_COMMENT = 2;
    public static final int STEP_FOLLOW = 3;
    public static final int STEP_CLICK = 4;

    private int step;
    private int drawableResourceId;
    private int layoutResourceId;

    public OnboardingShot(int step, int drawableResourceId, int layoutResourceId) {
        this.step = step;
        this.drawableResourceId = drawableResourceId;
        this.layoutResourceId = layoutResourceId;
    }

    public int getDrawableResourceId() {
        return drawableResourceId;
    }

    public void setDrawableResourceId(int drawableResourceId) {
        this.drawableResourceId = drawableResourceId;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getLayoutResourceId() {
        return layoutResourceId;
    }

    public void setLayoutResourceId(int layoutResourceId) {
        this.layoutResourceId = layoutResourceId;
    }
}

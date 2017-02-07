package co.netguru.android.inbbbox.feature.onboarding;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.onboarding.OnboardingController;

@FragmentScope
public class OnboardingPresenter extends MvpNullObjectBasePresenter<OnboardingContract.View>
        implements OnboardingContract.Presenter {

    private final OnboardingController onboardingController;

    public static final int STEPS_AMOUNT = 5;

    public static final int[] STEPS_DRAWABLES = {
            R.drawable.onboarding_step_1,
            R.drawable.onboarding_step_2,
            R.drawable.onboarding_step_3,
            R.drawable.onboarding_step_4,
            R.drawable.onboarding_step_5};

    public static final int[] STEPS_LAYOUTS = {
            R.layout.item_shot_onboarding_step_1,
            R.layout.item_shot_onboarding_step_2,
            R.layout.item_shot_onboarding_step_3,
            R.layout.item_shot_onboarding_step_4,
            R.layout.item_shot_onboarding_step_5};

    @Inject
    OnboardingPresenter(OnboardingController onboardingController) {
        this.onboardingController = onboardingController;
    }

    @Override
    public void getShots() {
        getView().setData(provideOnboardingShots());
    }

    @Override
    public void handleLikeShot(OnboardingStep step) {
        if (step.getStep() == OnboardingStep.STEP_LIKE) {
            scrollToNextStep(step);
        }
    }

    @Override
    public void handleAddShotToBucket(OnboardingStep step) {
        if (step.getStep() == OnboardingStep.STEP_BUCKET) {
            scrollToNextStep(step);
        }
    }

    @Override
    public void handleCommentShot(OnboardingStep step) {
        if (step.getStep() == OnboardingStep.STEP_COMMENT) {
            scrollToNextStep(step);
        }
    }

    @Override
    public void handleFollowShotAuthor(OnboardingStep step) {
        if (step.getStep() == OnboardingStep.STEP_FOLLOW) {
            scrollToNextStep(step);
        }
    }

    @Override
    public void handleShowShotDetails(OnboardingStep step) {
        if (step.getStep() == OnboardingStep.STEP_CLICK) {
            onboardingController.setOnboardingPassed(true);
            getView().closeOnboarding();
        }
    }

    private void scrollToNextStep(OnboardingStep currentStep) {
        getView().scrollToStep(currentStep.getStep() + 1);
    }

    private List<OnboardingStep> provideOnboardingShots() {
        List<OnboardingStep> shots = new ArrayList<>();

        for(int i = 0; i < STEPS_AMOUNT; i++) {
            shots.add(new OnboardingStep(i, STEPS_DRAWABLES[i], STEPS_LAYOUTS[i]));
        }

        return shots;
    }
}

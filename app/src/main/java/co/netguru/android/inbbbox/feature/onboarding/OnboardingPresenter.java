package co.netguru.android.inbbbox.feature.onboarding;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.onboarding.OnboardingController;

@FragmentScope
public class OnboardingPresenter extends MvpNullObjectBasePresenter<OnboardingContract.View>
        implements OnboardingContract.Presenter {

    private final OnboardingController onboardingController;

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
        if (step.getStep() == OnboardingStepData.STEP_LIKE.getStep()) {
            scrollToNextStep(step);
        }
    }

    @Override
    public void handleAddShotToBucket(OnboardingStep step) {
        if (step.getStep() == OnboardingStepData.STEP_BUCKET.getStep()) {
            scrollToNextStep(step);
        }
    }

    @Override
    public void handleCommentShot(OnboardingStep step) {
        if (step.getStep() == OnboardingStepData.STEP_COMMENT.getStep()) {
            scrollToNextStep(step);
        }
    }

    @Override
    public void handleFollowShotAuthor(OnboardingStep step) {
        if (step.getStep() == OnboardingStepData.STEP_FOLLOW.getStep()) {
            scrollToNextStep(step);
        }
    }

    @Override
    public void handleShowShotDetails(OnboardingStep step) {
        if (step.getStep() == OnboardingStepData.STEP_CLICK.getStep()) {
            onboardingController.setOnboardingPassed(true);
            getView().closeOnboarding();
        }
    }

    private void scrollToNextStep(OnboardingStep currentStep) {
        getView().scrollToStep(currentStep.getStep() + 1);
    }

    private List<OnboardingStep> provideOnboardingShots() {
        return Arrays.asList(
                new OnboardingStep(OnboardingStepData.STEP_LIKE),
                new OnboardingStep(OnboardingStepData.STEP_BUCKET),
                new OnboardingStep(OnboardingStepData.STEP_COMMENT),
                new OnboardingStep(OnboardingStepData.STEP_FOLLOW),
                new OnboardingStep(OnboardingStepData.STEP_CLICK));
    }
}

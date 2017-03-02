package co.netguru.android.inbbbox.feature.onboarding;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.dribbbleuser.Links;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.controllers.FollowersController;
import co.netguru.android.inbbbox.data.onboarding.OnboardingController;
import timber.log.Timber;

import static co.netguru.android.inbbbox.common.utils.RxTransformerUtil.applyCompletableIoSchedulers;

@FragmentScope
public class OnboardingPresenter extends MvpNullObjectBasePresenter<OnboardingContract.View>
        implements OnboardingContract.Presenter {

    private static final int NETGURU_ID = 653174;
    private static final User NETGURU_USER = User

            .builder()
            .id(NETGURU_ID)
            .name("")
            .avatarUrl("")
            .username("")
            .shotsCount(0)
            .type("team")
            .followersCount(0)
            .followingsCount(0)
            .bio("")
            .location("")
            .links(Links.create("", ""))
            .build();

    private final OnboardingController onboardingController;
    private final FollowersController followersController;

    @Inject
    OnboardingPresenter(OnboardingController onboardingController,
                        FollowersController followersController) {
        this.onboardingController = onboardingController;
        this.followersController = followersController;
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
            followersController.followUser(NETGURU_USER)
                    .compose(applyCompletableIoSchedulers())
                    .subscribe(() -> {},
                            throwable -> Timber.d("Error occurred when following netguru"));
        }
    }

    @Override
    public void handleSkipFollow(OnboardingStep step) {
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

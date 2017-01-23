package co.netguru.android.inbbbox.feature.onboarding;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.R;

@FragmentScope
public class OnboardingPresenter extends MvpNullObjectBasePresenter<OnboardingContract.View>
        implements OnboardingContract.Presenter {

    private int currentStep = 0;

    @Inject
    OnboardingPresenter() {
    }

    @Override
    public void getShots() {
        OnboardingShot shot1 = new OnboardingShot(0, R.drawable.onboarding_step_1, R.layout.item_shot_onboarding_step_1);
        OnboardingShot shot2 = new OnboardingShot(1, R.drawable.onboarding_step_2, R.layout.item_shot_onboarding_step_2);
        OnboardingShot shot3 = new OnboardingShot(2, R.drawable.onboarding_step_3, R.layout.item_shot_layout);
        OnboardingShot shot4 = new OnboardingShot(3, R.drawable.onboarding_step_4, R.layout.item_shot_layout);
        OnboardingShot shot5 = new OnboardingShot(4, R.drawable.onboarding_step_5, R.layout.item_shot_layout);
        List<OnboardingShot> shots = Arrays.asList(shot1, shot2, shot3, shot4, shot5);
        getView().setData(shots);
    }

    @Override
    public void handleLikeShot(OnboardingShot shot) {
        if (currentStep == 0) {
            currentStep++;
            getView().scrollToStep(currentStep);
        }
    }

    @Override
    public void handleAddShotToBucket(OnboardingShot shot) {
        if (currentStep == 1) {
            currentStep++;
            getView().scrollToStep(currentStep);
        }
    }

    @Override
    public void handleCommentShot(OnboardingShot shot) {
        if (currentStep == 2) {
            currentStep++;
            getView().scrollToStep(currentStep);
        }
    }

    @Override
    public void handleFollowShotAuthor(OnboardingShot shot) {
        if (currentStep == 3) {
            currentStep++;
            getView().scrollToStep(currentStep);
        }
    }

    @Override
    public void handleShowShotDetails(OnboardingShot shot) {
        if (currentStep == 4) {
            currentStep++;
            // finish onboarding
        }
    }
}

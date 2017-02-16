package co.netguru.android.inbbbox.feature.onboarding;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

interface OnboardingContract {

    interface View extends MvpView {
        void scrollToStep(int step);

        void setData(List<OnboardingStep> data);

        void closeOnboarding();
    }

    interface Presenter extends MvpPresenter<View> {

        void getShots();

        void handleLikeShot(OnboardingStep step);

        void handleAddShotToBucket(OnboardingStep step);

        void handleShowShotDetails(OnboardingStep step);

        void handleCommentShot(OnboardingStep step);

        void handleFollowShotAuthor(OnboardingStep step);

        void handleSkipFollow(OnboardingStep step);
    }
}

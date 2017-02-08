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

        void handleLikeShot(OnboardingStep shot);

        void handleAddShotToBucket(OnboardingStep shot);

        void handleShowShotDetails(OnboardingStep shot);

        void handleCommentShot(OnboardingStep shot);

        void handleFollowShotAuthor(OnboardingStep shot);
    }
}

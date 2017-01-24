package co.netguru.android.inbbbox.feature.onboarding;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

interface OnboardingContract {

    interface View extends MvpView {
        void scrollToStep(int step);

        void setData(List<OnboardingShot> data);

        void closeOnboarding();
    }

    interface Presenter extends MvpPresenter<View> {

        void getShots();

        void handleLikeShot(OnboardingShot shot);

        void handleAddShotToBucket(OnboardingShot shot);

        void handleShowShotDetails(OnboardingShot shot);

        void handleCommentShot(OnboardingShot shot);

        void handleFollowShotAuthor(OnboardingShot shot);
    }
}

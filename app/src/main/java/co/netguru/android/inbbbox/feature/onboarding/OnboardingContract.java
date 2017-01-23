package co.netguru.android.inbbbox.feature.onboarding;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.HttpErrorView;

interface OnboardingContract {

    interface View extends MvpView {
        void scrollToStep(int step);
        void setData(List<OnboardingShot> data);
    }

    interface Presenter extends MvpPresenter<View> {

        void getShots();

        void handleLikeShot(OnboardingShot shot);

        void handleAddShotToBucket(OnboardingShot shot);

        void handleShowShotDetails(OnboardingShot shot);

        void handleCommentShot(OnboardingShot selectedShot);

        void handleFollowShotAuthor(OnboardingShot shot);
    }
}

package co.netguru.android.inbbbox.feature.shared.peekandpop;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

public interface ShotPeekAndPopContract {

    interface View extends MvpView {
        void showMessageOnServerError(String errorMessage);

        void showMessageShotLiked();

        void showMessageShotUnliked();

        void showBucketAddSuccess();

        void showBucketChooserView(Shot shot);
    }

    interface Presenter extends MvpPresenter<View> {
        void addShotToBucket(Shot shot, Bucket bucket);

        void toggleLikeShot();

        void detach();

        void onBucketShot();

        void vibrate();
    }
}

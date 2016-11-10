package co.netguru.android.inbbbox.feature.likes;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import co.netguru.android.inbbbox.model.ui.LikedShot;

interface LikesViewContract {

    interface View extends MvpView {

        void showLikes(List<LikedShot> likedShotList);

        void showMoreLikes(List<LikedShot> likedShotList);

        void hideEmptyLikesInfo();

        void showEmptyLikesInfo();
    }

    interface Presenter extends MvpPresenter<View> {
        void getLikesFromServer();

        void getMoreLikesFromServer();
    }
}

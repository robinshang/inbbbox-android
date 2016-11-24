package co.netguru.android.inbbbox.feature.likes;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import co.netguru.android.inbbbox.model.ui.Shot;

interface LikesViewContract {

    interface View extends MvpView {

        void showLikes(List<Shot> likedShotList);

        void showMoreLikes(List<Shot> likedShotList);

        void hideEmptyLikesInfo();

        void showEmptyLikesInfo();

        void openShowDetailsScreen(Shot shot);
    }

    interface Presenter extends MvpPresenter<View> {
        void getLikesFromServer();

        void getMoreLikesFromServer();

        void showShotDetails(Shot shot);
    }
}

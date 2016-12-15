package co.netguru.android.inbbbox.feature.likes;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.feature.common.ErrorPresenter;
import co.netguru.android.inbbbox.feature.common.HttpErrorView;
import co.netguru.android.inbbbox.model.ui.Shot;

interface LikesViewContract {

    interface View extends MvpView, HttpErrorView, MvpLceView<List<Shot>> {

        void showMoreLikes(List<Shot> likedShotList);

        void showLoadingMoreLikesView();

        void hideLoadingMoreBucketsView();

        void hideEmptyLikesInfo();

        void hideProgressBar();

        void showEmptyLikesInfo();

        void openShowDetailsScreen(Shot shot);
    }

    interface Presenter extends MvpPresenter<View>, ErrorPresenter {
        void getLikesFromServer();

        void getMoreLikesFromServer();

        void showShotDetails(Shot shot);

        void checkDataEmpty(boolean isEmpty);
    }
}

package co.netguru.android.inbbbox.feature.likes;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.feature.common.BaseMvpRestPresenter;
import co.netguru.android.inbbbox.feature.common.BaseMvpRestView;
import co.netguru.android.inbbbox.model.ui.Shot;

interface LikesViewContract {

    interface View extends BaseMvpRestView, MvpLceView<List<Shot>> {

        void showMoreLikes(List<Shot> likedShotList);

        void showLoadingMoreLikesView();

        void hideLoadingMoreBucketsView();

        void hideEmptyLikesInfo();

        void hideProgressBar();

        void showEmptyLikesInfo();

        void openShowDetailsScreen(Shot shot);
    }

    interface Presenter extends BaseMvpRestPresenter<View> {
        void getLikesFromServer();

        void getMoreLikesFromServer();

        void showShotDetails(Shot shot);

        void checkDataEmpty(boolean isEmpty);
    }
}

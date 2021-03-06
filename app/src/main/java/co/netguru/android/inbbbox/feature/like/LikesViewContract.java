package co.netguru.android.inbbbox.feature.like;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.ErrorPresenter;
import co.netguru.android.inbbbox.feature.shared.base.HttpErrorView;

interface LikesViewContract {

    interface View extends HttpErrorView, MvpLceView<List<Shot>> {

        void showMoreLikes(List<Shot> likedShotList);

        void showLikeAtTop(Shot likedShot);

        void removeShotFromLikes(Shot unlikedShot);

        void showLoadingMoreLikesView();

        void hideLoadingMoreLikesView();

        void hideEmptyLikesInfo();

        void hideProgressBar();

        void showEmptyLikesInfo();

        void openShowDetailsScreen(Shot shot, List<Shot> shotList);
    }

    interface Presenter extends MvpPresenter<View>, ErrorPresenter {
        void getLikesFromServer();

        void getMoreLikesFromServer();

        void showShotDetails(Shot shot, List<Shot> allShots);

        void checkDataEmpty(boolean isEmpty);
    }
}

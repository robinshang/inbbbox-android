package co.netguru.android.inbbbox.feature.followers;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.feature.common.BaseMvpRestPresenter;
import co.netguru.android.inbbbox.feature.common.BaseMvpRestView;
import co.netguru.android.inbbbox.model.ui.Follower;

interface FollowersContract {

    interface View extends BaseMvpRestView, MvpLceView<List<Follower>> {

        void showMoreFollowedUsers(List<Follower> followerList);

        void hideLoadingMoreBucketsView();

        void hideEmptyLikesInfo();

        void showEmptyLikesInfo();

        void showLoadingMoreFollowersView();

        void hideProgressBars();
    }

    interface Presenter extends BaseMvpRestPresenter<View> {

        void getFollowedUsersFromServer();

        void getMoreFollowedUsersFromServer();

        void checkDataEmpty(List<Follower> data);
    }
}

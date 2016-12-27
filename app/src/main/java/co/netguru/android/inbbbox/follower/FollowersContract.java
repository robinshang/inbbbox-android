package co.netguru.android.inbbbox.follower;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.common.base.ErrorPresenter;
import co.netguru.android.inbbbox.common.base.HttpErrorView;
import co.netguru.android.inbbbox.data.follower.model.ui.Follower;

interface FollowersContract {

    interface View extends MvpView, HttpErrorView, MvpLceView<List<Follower>> {

        void showMoreFollowedUsers(List<Follower> followerList);

        void hideLoadingMoreBucketsView();

        void hideEmptyLikesInfo();

        void showEmptyLikesInfo();

        void showLoadingMoreFollowersView();

        void hideProgressBars();
    }

    interface Presenter extends MvpPresenter<View>, ErrorPresenter {

        void getFollowedUsersFromServer();

        void getMoreFollowedUsersFromServer();

        void checkDataEmpty(List<Follower> data);
    }
}

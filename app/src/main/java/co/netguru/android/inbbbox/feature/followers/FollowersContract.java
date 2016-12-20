package co.netguru.android.inbbbox.feature.followers;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.feature.common.ErrorPresenter;
import co.netguru.android.inbbbox.feature.common.HttpErrorView;
import co.netguru.android.inbbbox.model.ui.Follower;

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

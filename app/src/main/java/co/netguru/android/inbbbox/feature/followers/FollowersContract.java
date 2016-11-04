package co.netguru.android.inbbbox.feature.followers;

import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import co.netguru.android.inbbbox.data.ui.Follower;

interface FollowersContract {

    interface View extends MvpView {

        void showFollowedUsers(List<Follower> followerList);

        void setEmptyViewText(SpannableStringBuilder spannableStringBuilder);
    }

    interface Presenter extends MvpPresenter<View> {

        void getFollowedUsersFromServer();

        void addIconToText(String text, Drawable icon);
    }
}

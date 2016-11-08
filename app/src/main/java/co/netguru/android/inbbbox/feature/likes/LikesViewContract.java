package co.netguru.android.inbbbox.feature.likes;

import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import co.netguru.android.inbbbox.model.ui.LikedShot;

interface LikesViewContract {

    interface View extends MvpView {

        void showLikes(List<LikedShot> likedShotList);

        void hideEmptyLikesInfo();

        void showEmptyLikesInfo();

        void setEmptyViewText(SpannableStringBuilder spannableStringBuilder);
    }

    interface Presenter extends MvpPresenter<View> {
        void getLikesFromServer();

        void addIconToText(String text, Drawable icon);
    }
}

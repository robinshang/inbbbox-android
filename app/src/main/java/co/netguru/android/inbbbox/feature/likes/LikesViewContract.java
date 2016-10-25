package co.netguru.android.inbbbox.feature.likes;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

interface LikesViewContract {

    interface View extends MvpView {
    }

    interface Presenter extends MvpPresenter<View> {

    }
}

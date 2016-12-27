package co.netguru.android.inbbbox.bucket.createbucket;


import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import co.netguru.android.inbbbox.common.base.ErrorPresenter;
import co.netguru.android.inbbbox.common.base.HttpErrorView;

public interface CreateBucketContract {
    interface View extends MvpView, HttpErrorView {

        void close();

        void hideErrorMessages();

        void showErrorEmptyBucket();

        void showProgressView();

        void hideProgressView();
    }

    interface Presenter extends MvpPresenter<View>, ErrorPresenter {

        void handleCreateBucket(String name, String description);

    }
}

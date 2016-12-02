package co.netguru.android.inbbbox.feature.buckets.createbucket;


import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

public interface CreateBucketContract {
    interface View extends MvpView {

        void close();

        void hideErrorMessages();

        void showErrorEmptyBucket();

        void showProgressView();

        void hideProgressView();
    }

    interface Presenter extends MvpPresenter<View> {

        void handleCreateBucket(String name, String description);

    }
}

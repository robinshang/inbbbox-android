package co.netguru.android.inbbbox.feature.buckets.createbucket;


import co.netguru.android.inbbbox.feature.common.BaseMvpRestPresenter;
import co.netguru.android.inbbbox.feature.common.BaseMvpRestView;

public interface CreateBucketContract {
    interface View extends BaseMvpRestView {

        void close();

        void hideErrorMessages();

        void showErrorEmptyBucket();

        void showProgressView();

        void hideProgressView();
    }

    interface Presenter extends BaseMvpRestPresenter<View> {

        void handleCreateBucket(String name, String description);

    }
}

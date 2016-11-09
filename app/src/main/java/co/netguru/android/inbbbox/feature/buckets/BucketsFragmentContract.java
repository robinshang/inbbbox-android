package co.netguru.android.inbbbox.feature.buckets;


import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

public interface BucketsFragmentContract {

    interface View extends MvpView {

    }

    interface Presenter extends MvpPresenter<BucketsFragmentContract.View> {

    }
}

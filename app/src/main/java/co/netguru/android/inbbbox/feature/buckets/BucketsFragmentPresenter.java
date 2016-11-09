package co.netguru.android.inbbbox.feature.buckets;


import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.controler.BucketsController;

public class BucketsFragmentPresenter extends MvpNullObjectBasePresenter<BucketsFragmentContract.View>
        implements BucketsFragmentContract.Presenter {

    private final BucketsController bucketsController;

    @Inject
    BucketsFragmentPresenter(BucketsController bucketsController) {
        this.bucketsController = bucketsController;
    }
}

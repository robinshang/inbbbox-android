package co.netguru.android.inbbbox.feature.buckets.details;


import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;

@FragmentScope
public class BucketDetailsPresenter extends MvpNullObjectBasePresenter<BucketDetailsContract.View>
        implements BucketDetailsContract.Presenter {

    @Inject
    public BucketDetailsPresenter() {
        //di
    }
}

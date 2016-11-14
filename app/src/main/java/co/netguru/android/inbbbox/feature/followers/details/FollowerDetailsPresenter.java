package co.netguru.android.inbbbox.feature.followers.details;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;

@FragmentScope
public class FollowerDetailsPresenter extends MvpNullObjectBasePresenter<FollowerDetailsContract.View>
        implements FollowerDetailsContract.Presenter {

    @Inject
    public FollowerDetailsPresenter() {

    }
}

package co.netguru.android.inbbbox.feature.user.info;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;

@FragmentScope
public class UserInfoPresenter extends MvpNullObjectBasePresenter<UserInfoContract.View>
        implements UserInfoContract.Presenter {

    @Inject
    public UserInfoPresenter() {

    }
}

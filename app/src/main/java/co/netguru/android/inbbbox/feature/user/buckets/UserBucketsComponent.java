package co.netguru.android.inbbbox.feature.user.buckets;

import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@Subcomponent(modules = {UserBucketsModule.class})
@FragmentScope
public interface UserBucketsComponent {

    UserBucketsPresenter getPresenter();
}

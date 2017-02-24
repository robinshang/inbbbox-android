package co.netguru.android.inbbbox.feature.user;

import co.netguru.android.commons.di.ActivityScope;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent
public interface UserActivityComponent {

    UserActivityPresenter getPresenter();
}

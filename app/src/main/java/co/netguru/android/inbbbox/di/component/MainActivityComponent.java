package co.netguru.android.inbbbox.di.component;

import co.netguru.android.inbbbox.di.scope.ActivityScope;
import co.netguru.android.inbbbox.feature.main.MainActivityPresenter;
import dagger.Component;

@ActivityScope
@Component(
        dependencies = ApplicationComponent.class
)
public interface MainActivityComponent {

    MainActivityPresenter getMainActivityPresenter();
}

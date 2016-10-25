package co.netguru.android.inbbbox.di.component;

import co.netguru.android.inbbbox.di.module.MainActivityModule;
import co.netguru.android.inbbbox.di.scope.ActivityScope;
import co.netguru.android.inbbbox.feature.main.MainActivity;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {MainActivityModule.class})
public interface MainActivityComponent {

    void inject(MainActivity activity);

}

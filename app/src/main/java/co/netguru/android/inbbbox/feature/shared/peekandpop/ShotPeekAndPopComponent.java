package co.netguru.android.inbbbox.feature.shared.peekandpop;

import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {ShotPeekAndPopModule.class})
public interface ShotPeekAndPopComponent {

    ShotPeekAndPopPresenter getPresenter();
}

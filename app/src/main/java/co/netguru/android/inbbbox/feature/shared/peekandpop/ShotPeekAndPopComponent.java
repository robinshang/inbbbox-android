package co.netguru.android.inbbbox.feature.shared.peekandpop;

import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface ShotPeekAndPopComponent {

    ShotPeekAndPopPresenter getPresenter();
}

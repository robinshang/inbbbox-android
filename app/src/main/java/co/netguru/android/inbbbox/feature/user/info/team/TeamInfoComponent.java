package co.netguru.android.inbbbox.feature.user.info.team;

import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = TeamInfoModule.class)
public interface TeamInfoComponent {

    void inject(TeamInfoFragment fragment);

    TeamInfoPresenter getPresenter();
}

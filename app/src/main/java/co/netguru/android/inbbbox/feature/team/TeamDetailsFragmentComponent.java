package co.netguru.android.inbbbox.feature.team;

import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface TeamDetailsFragmentComponent {

    void inject(TeamDetailsFragment fragment);

    TeamDetailsPresenter getPresenter();
}

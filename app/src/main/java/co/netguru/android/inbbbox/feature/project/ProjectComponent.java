package co.netguru.android.inbbbox.feature.project;

import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {ProjectModule.class})
public interface ProjectComponent {

    void inject(ProjectFragment fragment);

    ProjectPresenter getPresenter();
}

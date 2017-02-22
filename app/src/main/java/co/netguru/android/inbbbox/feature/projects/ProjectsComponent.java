package co.netguru.android.inbbbox.feature.projects;

import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface ProjectsComponent {
    void inject(ProjectsFragment fragment);

    ProjectsPresenter getPresenter();
}

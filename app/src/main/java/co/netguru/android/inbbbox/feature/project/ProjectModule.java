package co.netguru.android.inbbbox.feature.project;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.user.projects.model.ui.ProjectWithShots;
import dagger.Module;
import dagger.Provides;

@Module
public class ProjectModule {

    private final ProjectWithShots projectWithShots;

    public ProjectModule(ProjectWithShots projectWithShots) {
        this.projectWithShots = projectWithShots;
    }

    @Provides
    @FragmentScope
    public ProjectWithShots provideProjectWithShots() {
        return projectWithShots;
    }
}

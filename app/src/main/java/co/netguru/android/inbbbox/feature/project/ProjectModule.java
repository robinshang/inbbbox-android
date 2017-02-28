package co.netguru.android.inbbbox.feature.project;

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
    public ProjectWithShots provideProjectWithShots() {
        return projectWithShots;
    }
}

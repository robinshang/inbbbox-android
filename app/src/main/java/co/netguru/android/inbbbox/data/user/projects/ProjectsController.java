package co.netguru.android.inbbbox.data.user.projects;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.user.projects.model.ui.Project;
import rx.Observable;

@Singleton
public class ProjectsController {

    private final ProjectsApi projectsApi;

    @Inject
    ProjectsController(ProjectsApi projectsApi) {
        this.projectsApi = projectsApi;
    }

    public Observable<List<Project>> getUserProjects(long userId) {
        return projectsApi.getUserProjects(userId)
                .flatMap(Observable::from)
                .map(Project::create)
                .toList();
    }
}

package co.netguru.android.inbbbox.data.user.projects;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.data.user.projects.model.api.ProjectEntity;
import co.netguru.android.inbbbox.data.user.projects.model.ui.ProjectWithShots;
import rx.Observable;
import rx.schedulers.Schedulers;

@Singleton
public class ProjectsController {

    private final ProjectsApi projectsApi;

    @Inject
    ProjectsController(ProjectsApi projectsApi) {
        this.projectsApi = projectsApi;
    }

    public Observable<List<ProjectWithShots>> getUserProjects(long userId) {
        return projectsApi.getUserProjects(userId)
                .flatMap(Observable::from)
                .flatMap(this::getProjectWithShots)
                .toList();
    }

    private Observable<ProjectWithShots> getProjectWithShots(ProjectEntity projectEntity) {
        return projectsApi.getShotsFromProject(projectEntity.id())
                .flatMap(Observable::from)
                .map(Shot::create)
                .toList()
                .map(shotList -> ProjectWithShots.create(projectEntity, shotList))
                .subscribeOn(Schedulers.io());
    }
}

package co.netguru.android.inbbbox.data.user.projects;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.data.user.projects.model.api.ProjectEntity;
import co.netguru.android.inbbbox.data.user.projects.model.ui.ProjectWithShots;
import co.netguru.android.inbbbox.feature.user.projects.ProjectsPresenter;
import rx.Observable;
import rx.schedulers.Schedulers;

@Singleton
public class ProjectsController {

    private final ProjectsApi projectsApi;

    @Inject
    ProjectsController(ProjectsApi projectsApi) {
        this.projectsApi = projectsApi;
    }

    public Observable<List<ProjectWithShots>> getUserProjectsWithShots(long userId, int shotsPageNumber,
                                                                       int shotsPageCount) {
        return projectsApi.getUserProjects(userId)
                .flatMap(Observable::from)
                .flatMap(projectEntity -> getProjectWithShots(projectEntity, shotsPageNumber, shotsPageCount))
                .toList();
    }

    public Observable<List<Shot>> getShotsFromProject(long projectId, int shotsPageNumber,
                                                      int shotsPageCount) {
        return projectsApi.getShotsFromProject(projectId, shotsPageNumber, shotsPageCount)
                .flatMap(Observable::from)
                .map(Shot::create)
                .toList();
    }

    private Observable<ProjectWithShots> getProjectWithShots(ProjectEntity projectEntity,
                                                            int shotsPageNumber, int shotsPageCount) {
        return projectsApi.getShotsFromProject(projectEntity.id(), shotsPageNumber, shotsPageCount)
                .flatMap(Observable::from)
                .map(Shot::create)
                .toList()
                .map(shotList -> ProjectWithShots.create(projectEntity, shotList,
                        shotList.size() >= ProjectsPresenter.PROJECT_SHOTS_PAGE_COUNT))
                .subscribeOn(Schedulers.io());
    }
}

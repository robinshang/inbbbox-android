package co.netguru.android.inbbbox.feature.user.projects;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.common.utils.RxTransformerUtil;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.data.user.projects.ProjectsController;
import co.netguru.android.inbbbox.data.user.projects.model.ui.ProjectWithShots;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@FragmentScope
public class ProjectsPresenter extends MvpNullObjectBasePresenter<ProjectsContract.View>
        implements ProjectsContract.Presenter {

    public static final int PROJECT_SHOTS_PAGE_COUNT = 15;
    private static final int PROJECTS_PAGE_COUNT = 15;
    private static final int PROJECT_SHOTS_FIRST_PAGE = 1;
    private static final int SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE = 1;

    private final ProjectsController projectsController;
    private final ErrorController errorController;

    private User user;

    private boolean hasMoreProjects = true;
    private int projectsPage = 1;

    @NonNull
    private Subscription refreshSubscription;

    @NonNull
    private Subscription loadNextProjectsSubscription;

    @NonNull
    private Subscription loadNextProjectShotsSubscription;

    @Inject
    ProjectsPresenter(ProjectsController projectsController, ErrorController errorController) {
        this.projectsController = projectsController;
        this.errorController = errorController;
        refreshSubscription = Subscriptions.unsubscribed();
        loadNextProjectsSubscription = Subscriptions.unsubscribed();
        loadNextProjectShotsSubscription = Subscriptions.unsubscribed();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            refreshSubscription.unsubscribe();
            loadNextProjectsSubscription.unsubscribe();
            loadNextProjectShotsSubscription.unsubscribe();
        }
    }

    @Override
    public void userDataReceived(User user) {
        this.user = user;
        getUserProjects();
    }

    @Override
    public void getUserProjects() {
        if (refreshSubscription.isUnsubscribed()) {
            loadNextProjectShotsSubscription.unsubscribe();
            loadNextProjectsSubscription.unsubscribe();
            projectsPage = 1;

            refreshSubscription = projectsController.getUserProjectsWithShots(user.id(),
                    projectsPage, PROJECTS_PAGE_COUNT, PROJECT_SHOTS_FIRST_PAGE, PROJECT_SHOTS_PAGE_COUNT)
                    .compose(androidIO())
                    .doAfterTerminate(getView()::hideProgressBar)
                    .subscribe(this::onGetUserProjectsNext,
                            throwable -> handleError(throwable, "Error while getting user projects from server"));
        }
    }

    @Override
    public void getMoreUserProjects() {
        if (hasMoreProjects && refreshSubscription.isUnsubscribed() && loadNextProjectsSubscription.isUnsubscribed()) {
            projectsPage++;
            loadNextProjectsSubscription = projectsController.getUserProjectsWithShots(user.id(),
                    projectsPage, PROJECTS_PAGE_COUNT, PROJECT_SHOTS_FIRST_PAGE, PROJECT_SHOTS_PAGE_COUNT)
                    .compose(RxTransformerUtil.executeRunnableIfObservableDidntEmitUntilGivenTime(
                            SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE, TimeUnit.SECONDS,
                            getView()::showLoadingMoreProjectsView))
                    .compose(androidIO())
                    .doAfterTerminate(getView()::hideProgressBar)
                    .subscribe(this::onGetMoreUserProjectsNext,
                            throwable -> handleError(throwable, "Error while getting more user projects from server"));
        }
    }

    @Override
    public void getMoreShotsFromProject(ProjectWithShots project) {
        if (project.hasMoreShots() && refreshSubscription.isUnsubscribed()
                && loadNextProjectShotsSubscription.isUnsubscribed()) {
            loadNextProjectShotsSubscription = projectsController.getShotsFromProject(project.id(),
                    project.nextShotPage(), PROJECT_SHOTS_PAGE_COUNT)
                    .compose(RxTransformerUtil.executeRunnableIfObservableDidntEmitUntilGivenTime(
                            SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE, TimeUnit.SECONDS,
                            getView()::showLoadingMoreShotsFromProjectView))
                    .compose(androidIO())
                    .doAfterTerminate(getView()::hideProgressBar)
                    .subscribe(shotList -> onGetUserProjectShotsNext(project.id(), shotList),
                            throwable -> handleError(throwable, "Error while getting more project shots from server"));
        }
    }

    @Override
    public void onProjectClick(ProjectWithShots projectWithShots) {
        getView().showProjectDetails(projectWithShots);
    }

    @Override
    public void showContentForData(List<ProjectWithShots> projectWithShotsList) {
        if (projectWithShotsList.isEmpty()) {
            getView().showEmptyView();
        } else {
            getView().hideEmptyView();
        }
    }

    @Override
    public void onShotClick(Shot shot, ProjectWithShots inProject) {
        getView().showShotDetails(shot, inProject.shotList());
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }

    private void onGetUserProjectsNext(List<ProjectWithShots> projects) {
        hasMoreProjects = projects.size() >= PROJECTS_PAGE_COUNT;
        getView().setData(projects);
        getView().showContent();
    }

    private void onGetMoreUserProjectsNext(List<ProjectWithShots> projects) {
        hasMoreProjects = projects.size() >= PROJECTS_PAGE_COUNT;
        getView().addMoreProjects(projects);
    }

    private void onGetUserProjectShotsNext(long projectId, List<Shot> shotList) {
        getView().addMoreProjectShots(projectId, shotList);
    }
}

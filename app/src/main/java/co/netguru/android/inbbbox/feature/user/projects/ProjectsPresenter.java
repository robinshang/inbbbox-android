package co.netguru.android.inbbbox.feature.user.projects;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
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
    private static final int PROJECT_SHOTS_FIRST_PAGE = 1;
    private static final int SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE = 1;

    private final ProjectsController projectsController;
    private final ErrorController errorController;

    private UserWithShots user;

    @NonNull
    private Subscription refreshSubscription;

    @NonNull
    private Subscription loadNextProjectShotsSubscription;

    @Inject
    ProjectsPresenter(ProjectsController projectsController, ErrorController errorController) {
        this.projectsController = projectsController;
        this.errorController = errorController;
        refreshSubscription = Subscriptions.unsubscribed();
        loadNextProjectShotsSubscription = Subscriptions.unsubscribed();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            refreshSubscription.unsubscribe();
            loadNextProjectShotsSubscription.unsubscribe();
        }
    }

    @Override
    public void userDataReceived(UserWithShots user) {
        this.user = user;
        getUserProjects();
    }

    @Override
    public void getUserProjects() {
        if (refreshSubscription.isUnsubscribed()) {
            loadNextProjectShotsSubscription.unsubscribe();
            refreshSubscription = projectsController.getUserProjectsWithShots(user.user().id(),
                    PROJECT_SHOTS_FIRST_PAGE, PROJECT_SHOTS_PAGE_COUNT)
                    .compose(androidIO())
                    .doAfterTerminate(getView()::hideProgressBar)
                    .subscribe(this::onGetUserProjectsNext,
                            throwable -> handleError(throwable, "Error while getting user projects from server"));
        }
    }

    @Override
    public void getMoreShotsFromProject(ProjectWithShots project) {
        if (project.hasMoreShots() && refreshSubscription.isUnsubscribed()
                && loadNextProjectShotsSubscription.isUnsubscribed()) {
            loadNextProjectShotsSubscription = projectsController.getShotsFromProject(project.id(),
                    project.nextShotPage(), PROJECT_SHOTS_PAGE_COUNT)
                    .compose(androidIO())
                    .doAfterTerminate(getView()::hideProgressBar)
                    .subscribe(shotList -> onGetUserProjectShotsNext(project.id(), shotList),
                            throwable -> handleError(throwable, "Error while getting project shots from server"));
        }
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }

    private void onGetUserProjectsNext(List<ProjectWithShots> projects) {
        getView().setData(projects);
        getView().showContent();
    }

    private void onGetUserProjectShotsNext(long projectId, List<Shot> shotList) {
        getView().addMoreProjectShots(projectId, shotList);
    }
}

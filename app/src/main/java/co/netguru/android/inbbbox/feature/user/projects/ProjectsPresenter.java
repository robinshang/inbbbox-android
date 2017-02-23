package co.netguru.android.inbbbox.feature.user.projects;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.user.projects.ProjectsController;
import co.netguru.android.inbbbox.data.user.projects.model.ui.ProjectWithShots;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@FragmentScope
public class ProjectsPresenter extends MvpNullObjectBasePresenter<ProjectsContract.View>
        implements ProjectsContract.Presenter {

    private final ProjectsController projectsController;
    private final ErrorController errorController;

    private UserWithShots user;

    @NonNull
    private final CompositeSubscription compositeSubscription;

    @Inject
    ProjectsPresenter(ProjectsController projectsController, ErrorController errorController) {
        this.projectsController = projectsController;
        this.errorController = errorController;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void userDataReceived(UserWithShots user) {
        this.user = user;
        getUserProjects();
    }

    @Override
    public void getUserProjects() {
        compositeSubscription.add(projectsController.getUserProjects(user.user().id())
                .compose(androidIO())
                .doAfterTerminate(getView()::hideProgressBar)
                .subscribe(this::onGetUserProjectsNext,
                        throwable -> handleError(throwable, "Error while getting user projects from server")));
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
}

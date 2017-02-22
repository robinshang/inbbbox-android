package co.netguru.android.inbbbox.feature.user.projects;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.user.projects.ProjectsController;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@FragmentScope
public class ProjectsPresenter extends MvpNullObjectBasePresenter<ProjectsContract.View>
        implements ProjectsContract.Presenter {

    private final ProjectsController projectsController;

    private UserWithShots user;

    @NonNull
    private final CompositeSubscription compositeSubscription;

    @Inject
    ProjectsPresenter(ProjectsController projectsController) {
        this.projectsController = projectsController;
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
                .subscribe(projects -> {
                            getView().setData(projects);
                            getView().showContent();
                        },
                        throwable -> Timber.e(throwable, "Error while getting user projects from server")));
    }
}

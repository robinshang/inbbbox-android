package co.netguru.android.inbbbox.feature.projects;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.projects.ProjectsController;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@FragmentScope
public class ProjectsPresenter extends MvpNullObjectBasePresenter<ProjectsContract.View>
        implements ProjectsContract.Presenter {

    private final ProjectsController projectsController;

    @NonNull
    private final CompositeSubscription compositeSubscription;

    @Inject
    ProjectsPresenter(ProjectsController projectsController) {
        this.projectsController = projectsController;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getUserProjects(long userId) {
        compositeSubscription.add(projectsController.getUserProjects(userId)
                .compose(androidIO())
                .subscribe(projects -> {
                            getView().setData(projects);
                            getView().showContent();
                        },
                        throwable -> Timber.e(throwable, "Error while getting user projects from server")));
    }
}

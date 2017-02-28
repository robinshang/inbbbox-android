package co.netguru.android.inbbbox.feature.project;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.common.utils.RxTransformerUtil;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.data.user.projects.ProjectsController;
import co.netguru.android.inbbbox.data.user.projects.model.ui.ProjectWithShots;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@FragmentScope
public class ProjectPresenter extends MvpNullObjectBasePresenter<ProjectContract.View>
        implements ProjectContract.Presenter {

    private static final int SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE = 1;
    private static final int SHOTS_PER_PAGE = 16;

    private final ProjectsController projectsController;
    private final ErrorController errorController;
    private final ProjectWithShots projectWithShots;
    @NonNull
    Subscription refreshSubscription;
    @NonNull
    Subscription loadMoreSubscription;
    private int pageNumber = 1;
    private boolean hasMore = true;

    @Inject
    public ProjectPresenter(ProjectWithShots projectWithShots,
                            ProjectsController projectsController,
                            ErrorController errorController) {
        this.projectWithShots = projectWithShots;
        this.projectsController = projectsController;
        this.errorController = errorController;

        refreshSubscription = Subscriptions.unsubscribed();
        loadMoreSubscription = Subscriptions.unsubscribed();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        refreshSubscription.unsubscribe();
        loadMoreSubscription.unsubscribe();
    }

    @Override
    public void refreshShots() {
        if (refreshSubscription.isUnsubscribed()) {
            loadMoreSubscription.unsubscribe();
            pageNumber = 1;

            refreshSubscription = projectsController.getShotsFromProject
                    (projectWithShots.id(), pageNumber, SHOTS_PER_PAGE)
                    .compose(androidIO())
                    .doAfterTerminate(getView()::hideProgressBar)
                    .subscribe(this::handleShots, this::handleError);
        }
    }

    @Override
    public void loadMoreShots() {
        if (hasMore && refreshSubscription.isUnsubscribed()
                && loadMoreSubscription.isUnsubscribed()) {
            pageNumber++;
            loadMoreSubscription = projectsController.getShotsFromProject
                    (projectWithShots.id(), pageNumber, SHOTS_PER_PAGE)
                    .compose(RxTransformerUtil
                            .executeRunnableIfObservableDidntEmitUntilGivenTime(
                                    SECONDS_TIMEOUT_BEFORE_SHOWING_LOADING_MORE,
                                    TimeUnit.SECONDS,
                                    getView()::showLoadingMoreShotsView))
                    .compose(androidIO())
                    .subscribe(this::handleNewShots, this::handleError);
        }
    }

    @Override
    public void onShotClick(Shot shot) {
        getView().showShotDetails(shot);
    }

    private void handleShots(List<Shot> shots) {
        hasMore = shots.size() == SHOTS_PER_PAGE;
        getView().setData(shots);
        getView().showContent();
    }

    private void handleError(Throwable throwable) {
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }

    private void handleNewShots(List<Shot> shots) {
        hasMore = shots.size() == SHOTS_PER_PAGE;
        getView().addShots(shots);
    }
}
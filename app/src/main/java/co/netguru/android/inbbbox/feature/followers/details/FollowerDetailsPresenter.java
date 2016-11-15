package co.netguru.android.inbbbox.feature.followers.details;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.controler.UserShotsController;
import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.model.ui.Shot;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@FragmentScope
public class FollowerDetailsPresenter extends MvpNullObjectBasePresenter<FollowerDetailsContract.View>
        implements FollowerDetailsContract.Presenter {

    private static final int SHOT_PAGE_COUNT = 30;

    private final UserShotsController userShotsController;
    private final CompositeSubscription subscriptions;

    private Follower follower;
    private boolean hasMore = true;
    private int pageNumber = 1;

    @Inject
    FollowerDetailsPresenter(UserShotsController userShotsController) {
        this.userShotsController = userShotsController;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        subscriptions.clear();
    }

    @Override
    public void followerDataReceived(Follower follower) {
        Timber.d("Received follower : %s", follower);
        if (follower != null) {
            this.follower = follower;
            getView().showFollowerData(follower);
        }
    }

    @Override
    public void getMoreUserShotsFromServer() {
        if (hasMore) {
            pageNumber++;
            final Subscription subscription = userShotsController.getUserShotsList(follower.id(),
                    pageNumber, SHOT_PAGE_COUNT)
                    .compose(androidIO())
                    .subscribe(this::onGetUserShotsNext,
                            throwable -> Timber.e(throwable, "Error while getting more user shots"));
            subscriptions.add(subscription);
        }
    }

    private void onGetUserShotsNext(List<Shot> shotList) {
        hasMore = shotList.size() == SHOT_PAGE_COUNT;
        getView().showMoreUserShots(shotList);
    }
}

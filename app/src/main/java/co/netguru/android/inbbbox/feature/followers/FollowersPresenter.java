package co.netguru.android.inbbbox.feature.followers;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.controler.FollowersController;
import co.netguru.android.inbbbox.controler.FollowersShotController;
import co.netguru.android.inbbbox.model.ui.Follower;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@FragmentScope
public class FollowersPresenter extends MvpNullObjectBasePresenter<FollowersContract.View>
        implements FollowersContract.Presenter {

    private static final int FOLLOWERS_PAGE_COUNT = 15;
    private static final int FOLLOWERS_SHOT_PAGE_COUNT = 30;
    private static final int FOLLOWERS_SHOT_PAGE_NUMBER = 1;

    private final FollowersController followersController;
    private final FollowersShotController followersShotController;
    private final CompositeSubscription subscriptions;

    private boolean hasMore = true;
    private int pageNumber = 1;

    @Inject
    FollowersPresenter(FollowersController followersController, FollowersShotController followersShotController) {
        this.followersController = followersController;
        this.followersShotController = followersShotController;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        subscriptions.clear();
    }

    @Override
    public void getFollowedUsersFromServer() {
        final Subscription subscription = followersController.getFollowedUsers(pageNumber, FOLLOWERS_PAGE_COUNT)
                .flatMap(follower -> followersShotController.getFollowedUserWithShots(follower,
                        FOLLOWERS_SHOT_PAGE_NUMBER, FOLLOWERS_SHOT_PAGE_COUNT))
                .toList()
                .compose(androidIO())
                .subscribe(this::onGetFollowersNext,
                        throwable -> Timber.e(throwable, "Error while getting followed users form server"));
        subscriptions.add(subscription);
    }

    @Override
    public void getMoreFollowedUsersFromServer() {
        if (hasMore) {
            pageNumber++;
            final Subscription subscription = followersController.getFollowedUsers(pageNumber, FOLLOWERS_PAGE_COUNT)
                    .flatMap(follower -> followersShotController.getFollowedUserWithShots(follower,
                            FOLLOWERS_SHOT_PAGE_NUMBER, FOLLOWERS_SHOT_PAGE_COUNT))
                    .toList()
                    .compose(androidIO())
                    .subscribe(this::onGetMoreFollowersNext,
                            throwable -> Timber.e(throwable, "Error while getting followed users form server"));
            subscriptions.add(subscription);
        }
    }

    private void onGetFollowersNext(List<Follower> followersList) {
        hasMore = followersList.size() == FOLLOWERS_PAGE_COUNT;
        if (followersList.isEmpty()) {
            getView().showEmptyLikesInfo();
        } else {
            getView().hideEmptyLikesInfo();
            getView().showFollowedUsers(followersList);
        }
    }

    private void onGetMoreFollowersNext(List<Follower> followersList) {
        hasMore = followersList.size() == FOLLOWERS_PAGE_COUNT;
        getView().showMoreFollowedUsers(followersList);
    }
}

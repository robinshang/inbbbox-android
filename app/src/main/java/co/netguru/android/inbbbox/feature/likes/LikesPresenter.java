package co.netguru.android.inbbbox.feature.likes;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.controler.LikedShotsController;
import co.netguru.android.inbbbox.model.ui.Shot;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@FragmentScope
public final class LikesPresenter extends MvpNullObjectBasePresenter<LikesViewContract.View>
        implements LikesViewContract.Presenter {

    private static final int PAGE_COUNT = 30;

    private final LikedShotsController likedShotsController;
    private final CompositeSubscription subscriptions;

    private boolean hasMore = true;
    private int pageNumber = 1;

    @Inject
    LikesPresenter(LikedShotsController likedShotsController) {
        this.likedShotsController = likedShotsController;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        subscriptions.clear();
    }

    @Override
    public void getLikesFromServer() {
        final Subscription subscription = likedShotsController.getLikedShots(pageNumber, PAGE_COUNT)
                .toList()
                .compose(androidIO())
                .subscribe(this::onGetLikeShotListNext,
                        throwable -> Timber.e(throwable, "Error while getting likes from server"));
        subscriptions.add(subscription);
    }

    @Override
    public void getMoreLikesFromServer() {
        if (hasMore) {
            pageNumber++;
            final Subscription subscription = likedShotsController.getLikedShots(pageNumber, PAGE_COUNT)
                    .toList()
                    .compose(androidIO())
                    .subscribe(this::onGetMoreLikeShotListNext,
                            throwable -> Timber.e(throwable, "Error while getting more likes from server"));
            subscriptions.add(subscription);
        }
    }

    private void onGetLikeShotListNext(List<Shot> likedShotList) {
        hasMore = likedShotList.size() == PAGE_COUNT;
        if (likedShotList.isEmpty()) {
            getView().showEmptyLikesInfo();
        } else {
            getView().hideEmptyLikesInfo();
            getView().showLikes(likedShotList);
        }
    }

    private void onGetMoreLikeShotListNext(List<Shot> likedShotList) {
        hasMore = likedShotList.size() == PAGE_COUNT;
        getView().showMoreLikes(likedShotList);
    }
}
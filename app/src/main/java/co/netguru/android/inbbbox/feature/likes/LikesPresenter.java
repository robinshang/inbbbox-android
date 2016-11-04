package co.netguru.android.inbbbox.feature.likes;

import android.graphics.drawable.Drawable;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.ui.LikedShot;
import co.netguru.android.inbbbox.utils.TextFormatter;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@FragmentScope
public final class LikesPresenter extends MvpNullObjectBasePresenter<LikesViewContract.View>
        implements LikesViewContract.Presenter {

    private final LikedShotsProvider likedShotsProvider;
    private final TextFormatter textFormatter;
    private final CompositeSubscription subscriptions;

    @Inject
    LikesPresenter(LikedShotsProvider likedShotsProvider, TextFormatter textFormatter) {
        this.likedShotsProvider = likedShotsProvider;
        this.textFormatter = textFormatter;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        subscriptions.clear();
    }

    @Override
    public void getLikesFromServer() {
        final Subscription subscription = likedShotsProvider.getLikedShots()
                .toList()
                .compose(androidIO())
                .subscribe(this::onGetLikeShotListNext,
                        throwable -> Timber.e(throwable, "Error while getting likes from server"));
        subscriptions.add(subscription);
    }

    @Override
    public void addIconToText(String text, Drawable icon) {
        getView().setEmptyViewText(textFormatter.addDrawableToTextAtFirstSpace(text, icon));
    }

    private void onGetLikeShotListNext(List<LikedShot> likedShotList) {
        if (likedShotList.isEmpty()) {
            getView().showEmptyLikesInfo();
            return;
        }
        getView().hideEmptyLikesInfo();
        getView().showLikes(likedShotList);
    }
}

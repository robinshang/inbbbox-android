package co.netguru.android.inbbbox.feature.followers;

import android.graphics.drawable.Drawable;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.controler.FollowersController;
import co.netguru.android.inbbbox.controler.FollowersShotController;
import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.utils.TextFormatter;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@FragmentScope
public class FollowersPresenter extends MvpNullObjectBasePresenter<FollowersContract.View>
        implements FollowersContract.Presenter {

    private final FollowersController followersController;
    private final FollowersShotController followersShotController;
    private final TextFormatter textFormatter;
    private final CompositeSubscription subscriptions;

    @Inject
    FollowersPresenter(FollowersController followersController, FollowersShotController followersShotController,
                       TextFormatter textFormatter) {
        this.followersController = followersController;
        this.followersShotController = followersShotController;
        this.textFormatter = textFormatter;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        subscriptions.clear();
    }

    @Override
    public void getFollowedUsersFromServer() {
        final Subscription subscription = followersController.getFollowedUsers()
                .flatMap(followersShotController::getFollowedUserWithShots)
                .toList()
                .compose(androidIO())
                .subscribe(this::onGetShotsNext,
                        throwable -> Timber.e(throwable, "Error while getting followed users form server"));
        subscriptions.add(subscription);
    }

    @Override
    public void addIconToText(String text, Drawable icon) {
        getView().setEmptyViewText(textFormatter.addDrawableToTextAtFirstSpace(text, icon));
    }

    private void onGetShotsNext(List<Follower> followersList) {
        if (followersList.isEmpty()) {
            getView().showEmptyLikesInfo();
            return;
        }
        getView().hideEmptyLikesInfo();
        getView().showFollowedUsers(followersList);
    }
}

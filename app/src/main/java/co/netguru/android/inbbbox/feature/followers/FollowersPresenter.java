package co.netguru.android.inbbbox.feature.followers;

import android.graphics.drawable.Drawable;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.utils.TextFormatter;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@FragmentScope
public class FollowersPresenter extends MvpNullObjectBasePresenter<FollowersContract.View>
        implements FollowersContract.Presenter {

    private final FollowersProvider followersProvider;
    private final TextFormatter textFormatter;
    private final CompositeSubscription subscriptions;

    @Inject
    FollowersPresenter(FollowersProvider followersProvider, TextFormatter textFormatter) {
        this.followersProvider = followersProvider;
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
        final Subscription subscription = followersProvider.getFollowedUsers()
                .toList()
                .compose(androidIO())
                .subscribe(followers -> getView().showFollowedUsers(followers),
                        throwable -> Timber.e(throwable, "Error while getting followed users form server"));
        subscriptions.add(subscription);
    }

    @Override
    public void addIconToText(String text, Drawable icon) {
        getView().setEmptyViewText(textFormatter.addDrawableToTextAtFirstSpace(text, icon));
    }
}

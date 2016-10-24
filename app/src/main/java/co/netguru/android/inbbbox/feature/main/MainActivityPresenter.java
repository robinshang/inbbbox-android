package co.netguru.android.inbbbox.feature.main;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.models.User;
import co.netguru.android.inbbbox.db.datasource.DataSource;
import co.netguru.android.inbbbox.di.scope.ActivityScope;
import co.netguru.android.inbbbox.feature.main.MainViewContract.Presenter;
import co.netguru.android.inbbbox.utils.LocalTimeFormatter;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@ActivityScope
public final class MainActivityPresenter extends MvpNullObjectBasePresenter<MainViewContract.View>
        implements Presenter {

    private final LocalTimeFormatter localTimeFormatter;
    private final DataSource<User> userDataSource;
    private final CompositeSubscription subscriptions;

    private User user;

    @Inject
    MainActivityPresenter(LocalTimeFormatter localTimeFormatter, DataSource<User> userDataSource) {
        this.localTimeFormatter = localTimeFormatter;
        this.userDataSource = userDataSource;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        subscriptions.clear();
    }

    @Override
    public void toggleButtonClicked(boolean isChecked) {
        if (isChecked) {
            getView().showLogoutMenu();
            getView().showUserName(user.getName());
        } else {
            getView().showMainMenu();
            getView().showUserName(user.getUsername());
        }
    }

    @Override
    public void performLogout() {
        // TODO: 18.10.2016 Clear user shared preferences
        getView().showLoginActivity();
    }

    @Override
    public void prepareUserData() {
        final Subscription subscription = userDataSource.get()
                .compose(androidIO())
                .subscribe(user -> this.user = user,
                        throwable -> Timber.e(throwable, "Error while getting user"),
                        this::showUserData);
        subscriptions.add(subscription);
    }

    @Override
    public void timeViewClicked() {
        getView().showTimePickDialog(localTimeFormatter.getCurrentHour(),
                localTimeFormatter.getCurrentMinute(),
                (view, hourOfDay, minute) ->
                        getView().showChangedTime(localTimeFormatter.getFormattedTime(hourOfDay, minute))
                );
    }

    private void showUserData() {
        getView().showUserName(user.getUsername());
        getView().showUserPhoto(user.getAvatarUrl());
        getView().showChangedTime(localTimeFormatter.getFormattedCurrentTime());
    }
}

package co.netguru.android.inbbbox.feature.main;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.models.User;
import co.netguru.android.inbbbox.db.datasource.DataSource;
import co.netguru.android.inbbbox.data.models.NotificationSettings;
import co.netguru.android.inbbbox.data.models.Settings;
import co.netguru.android.inbbbox.di.scope.ActivityScope;
import co.netguru.android.inbbbox.feature.main.MainViewContract.Presenter;
import co.netguru.android.inbbbox.feature.notification.NotificationScheduler;
import co.netguru.android.inbbbox.feature.settings.SettingsManager;
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
    private final NotificationScheduler notificationScheduler;
    private final SettingsManager settingsManager;

    private User user;

    @Inject
    MainActivityPresenter(LocalTimeFormatter localTimeFormatter, DataSource<User> userDataSource,
                          NotificationScheduler notificationScheduler, SettingsManager settingsManager) {
        this.localTimeFormatter = localTimeFormatter;
        this.userDataSource = userDataSource;
        this.notificationScheduler = notificationScheduler;
        this.settingsManager = settingsManager;
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
        prepareUserSettings();
    }


    @Override
    public void timeViewClicked() {
        settingsManager.getSettings()
                .map(Settings::getNotificationSettings)
                .compose(androidIO())
                .subscribe(this::showTimePickDialog,
                        throwable -> Timber.e(throwable, "Error while getting settings"));
    }

    @Override
    public void notificationStatusChanged(boolean status) {
        saveNotificationStatus(status);
        if (!status) {
            notificationScheduler.cancelNotification();
            return;
        }
        settingsManager.getSettings()
                .map(Settings::getNotificationSettings)
                .doOnNext(settings -> notificationScheduler.scheduleRepeatingNotification(settings.getHour(), settings.getMinute()))
                .compose(androidIO())
                .subscribe(time -> {}, throwable -> Timber.e(throwable, "Error while scheduling notification"));
    }

    private void saveNotificationStatus(boolean status) {
        settingsManager.changeNotificationStatus(status)
                .compose(androidIO())
                .subscribe(status1 -> Timber.d("Saving new notification status : %s", status),
                        throwable -> Timber.e(throwable, "Error while saving notification status"));
    }

    private void showTimePickDialog(NotificationSettings notificationSettings) {
        getView().showTimePickDialog(notificationSettings.getHour(), notificationSettings.getMinute(),
                (view, hour, minute) -> onTimePicked(hour, minute));
    }

    private void onTimePicked(int hour, int minute) {
        settingsManager.changeNotificationTime(hour, minute)
                .doOnNext(status -> notificationScheduler.scheduleRepeatingNotification(hour, minute))
                .compose(androidIO())
                .subscribe(status -> Timber.d("Notification time changed : %s", status),
                        throwable -> Timber.e(throwable, "Error while changing notification time"),
                        () -> getView().showNotificationTime(localTimeFormatter.getFormattedTime(hour, minute)));
    }

    private void prepareUserSettings() {
        settingsManager.getSettings()
                .map(Settings::getNotificationSettings)
                .doOnNext(this::checkNotificationSchedule)
                .compose(androidIO())
                .subscribe(this::setNotificationSettings,
                        throwable -> Timber.e(throwable, "Error while getting settings"));
    }

    private void checkNotificationSchedule(NotificationSettings settings) {
        if (settings.isEnabled()) {
            notificationScheduler.scheduleRepeatingNotification(settings.getHour(), settings.getMinute());
        }
    }

    private void setNotificationSettings(NotificationSettings notificationSettings) {
        getView().changeNotificationStatus(notificationSettings.isEnabled());
        getView().showNotificationTime(localTimeFormatter.getFormattedTime(notificationSettings.getHour(), notificationSettings.getMinute()));
    }

    private void showUserData() {
        getView().showUserName(user.getUsername());
        getView().showUserPhoto(user.getAvatarUrl());
    }
}

package co.netguru.android.inbbbox.feature.main;

import android.support.annotation.Nullable;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.models.Settings;
import co.netguru.android.inbbbox.data.models.StreamSourceSettings;
import co.netguru.android.inbbbox.data.models.User;
import co.netguru.android.inbbbox.db.datasource.DataSource;

import co.netguru.android.inbbbox.R;

import co.netguru.android.inbbbox.data.models.NotificationSettings;
import co.netguru.android.inbbbox.di.scope.ActivityScope;
import co.netguru.android.inbbbox.feature.main.MainViewContract.Presenter;
import co.netguru.android.inbbbox.feature.notification.NotificationController;
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
    private final NotificationScheduler notificationScheduler;
    private final NotificationController notificationController;
    private final SettingsManager settingsManager;
    private final CompositeSubscription subscriptions;

    private User user;

    @Inject
    MainActivityPresenter(LocalTimeFormatter localTimeFormatter, DataSource<User> userDataSource,
                          NotificationScheduler notificationScheduler, NotificationController notificationController,
                          SettingsManager settingsManager) {
        this.localTimeFormatter = localTimeFormatter;
        this.userDataSource = userDataSource;
        this.notificationScheduler = notificationScheduler;
        this.notificationController = notificationController;
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
        notificationScheduler.cancelNotification();
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
        final Subscription subscription = settingsManager.getNotificationSettings()
                .compose(androidIO())
                .subscribe(this::showTimePickDialog,
                        throwable -> {
                            Timber.e(throwable, "Error while getting settings");
                            getView().showMessage(R.string.error_database);
                        });
        subscriptions.add(subscription);
    }

    @Override
    public void notificationStatusChanged(boolean status) {
        saveNotificationStatus(status);
        if (!status) {
            notificationScheduler.cancelNotification();
            return;
        }
        final Subscription subscription = notificationController.scheduleNotification()
                .compose(androidIO())
                .subscribe(this::onScheduleNotificationNext, this::onScheduleNotificationError);

        subscriptions.add(subscription);
    }

    @Override
    public void streamSourceStatusChanged(@Nullable Boolean isFollowing, @Nullable Boolean isNew,
                                          @Nullable Boolean isPopular, @Nullable Boolean isDebuts) {
        final Subscription subscription = settingsManager.changeStreamSourceSettings(isFollowing, isNew, isPopular, isDebuts)
                .compose(androidIO())
                .subscribe(status -> Timber.d("Stream source settings changed : %s", status),
                        throwable -> Timber.e(throwable, "Error while changing stream source settings"));
        subscriptions.add(subscription);
    }

    @Override
    public void customizationStatusChanged(boolean isDetails) {
        final Subscription subscription = settingsManager.changeShotsDetailsStatus(isDetails)
                .compose(androidIO())
                .subscribe(status -> Timber.d("Customization settings changed : %s", status),
                        throwable -> Timber.e(throwable, "Error while changing customization settings"));
        subscriptions.add(subscription);
    }

    private void saveNotificationStatus(boolean status) {
        final Subscription subscription = settingsManager.changeNotificationStatus(status)
                .compose(androidIO())
                .subscribe(status1 -> Timber.d("Saving new notification status : %s", status1),
                        throwable -> Timber.e(throwable, "Error while saving notification status"));
        subscriptions.add(subscription);
    }

    private void showTimePickDialog(NotificationSettings notificationSettings) {
        getView().showTimePickDialog(notificationSettings.getHour(), notificationSettings.getMinute(),
                (view, hour, minute) -> onTimePicked(hour, minute));
    }

    private void onTimePicked(int hour, int minute) {
        final Subscription subscription = settingsManager.changeNotificationTime(hour, minute)
                .concatMap(status -> notificationController.scheduleNotification())
                .compose(androidIO())
                .subscribe(this::onScheduleNotificationNext, this::onScheduleNotificationError,
                        () -> getView().showNotificationTime(localTimeFormatter.getFormattedTime(hour, minute)));
        subscriptions.add(subscription);
    }

    private void prepareUserSettings() {
        final Subscription subscription = settingsManager.getSettings()
                .doOnNext(settings -> checkNotificationSchedule(settings.getNotificationSettings()))
                .compose(androidIO())
                .subscribe(this::setSettings,
                        throwable -> Timber.e(throwable, "Error while getting settings"));
        subscriptions.add(subscription);
    }

    private void checkNotificationSchedule(NotificationSettings settings) {
        if (settings.isEnabled()) {
            notificationController.scheduleNotification()
                    .compose(androidIO())
                    .subscribe(this::onScheduleNotificationNext, this::onScheduleNotificationError);
        }
    }

    private void setSettings(Settings settings) {
        setNotificationSettings(settings.getNotificationSettings());
        setStreamSourceSettings(settings.getStreamSourceSettings());
        getView().changeCustomizationStatus(settings.getCustomizationSettings().isShowDetails());
    }

    private void setNotificationSettings(NotificationSettings notificationSettings) {
        getView().changeNotificationStatus(notificationSettings.isEnabled());
        getView().showNotificationTime(localTimeFormatter.getFormattedTime(notificationSettings.getHour(),
                notificationSettings.getMinute()));
    }

    private void setStreamSourceSettings(StreamSourceSettings streamSourceSettings) {
        getView().changeStreamSourceStatus(streamSourceSettings.isFollowing(), streamSourceSettings.isNewToday(),
                streamSourceSettings.isPopularToday(), streamSourceSettings.isDebut());
    }

    private void onScheduleNotificationNext(NotificationSettings settings) {
        Timber.d("Notification scheduled : %s", settings);
    }
    private void onScheduleNotificationError(Throwable throwable) {
        Timber.e(throwable, "Error while scheduling notification");
    }



    private void showUserData() {
        getView().showUserName(user.getUsername());
        getView().showUserPhoto(user.getAvatarUrl());
    }
}

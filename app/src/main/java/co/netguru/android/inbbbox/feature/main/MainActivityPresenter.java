package co.netguru.android.inbbbox.feature.main;

import android.support.annotation.Nullable;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.local.UserPrefsController;
import co.netguru.android.inbbbox.feature.main.MainViewContract.Presenter;
import co.netguru.android.inbbbox.feature.notification.NotificationController;
import co.netguru.android.inbbbox.feature.notification.NotificationScheduler;
import co.netguru.android.inbbbox.feature.settings.SettingsManager;
import co.netguru.android.inbbbox.models.NotificationSettings;
import co.netguru.android.inbbbox.models.Settings;
import co.netguru.android.inbbbox.models.StreamSourceSettings;
import co.netguru.android.inbbbox.models.User;
import co.netguru.android.inbbbox.utils.LocalTimeFormatter;
import co.netguru.android.inbbbox.utils.RxTransformers;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@ActivityScope
public final class MainActivityPresenter extends MvpNullObjectBasePresenter<MainViewContract.View>
        implements Presenter {

    private final LocalTimeFormatter localTimeFormatter;
    private final UserPrefsController userPrefsController;
    private final NotificationScheduler notificationScheduler;
    private final NotificationController notificationController;
    private final SettingsManager settingsManager;
    private final CompositeSubscription subscriptions;

    @Inject
    MainActivityPresenter(LocalTimeFormatter localTimeFormatter, UserPrefsController userPrefsController,
                          NotificationScheduler notificationScheduler, NotificationController notificationController,
                          SettingsManager settingsManager) {
        this.localTimeFormatter = localTimeFormatter;
        this.userPrefsController = userPrefsController;
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
        } else {
            getView().showMainMenu();
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
        final Subscription subscription = userPrefsController.getUser()
                .compose(RxTransformers.applySingleIoSchedulers())
                .subscribe(this::showUserData,
                        throwable -> Timber.e(throwable, "Error while getting user"));
        subscriptions.add(subscription);
        prepareUserSettings();
    }


    @Override
    public void timeViewClicked() {
        final Subscription subscription = settingsManager.getNotificationSettings()
                .compose(RxTransformers.applySingleIoSchedulers())
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
                .compose(RxTransformers.applySingleIoSchedulers())
                .subscribe(this::onScheduleNotificationNext, this::onScheduleNotificationError);

        subscriptions.add(subscription);
    }

    @Override
    public void followingStatusChanged(boolean status) {
        changeStreamSourceStatus(status, null, null, null);
    }

    @Override
    public void newStatusChanged(boolean status) {
        changeStreamSourceStatus(null, status, null, null);
    }

    @Override
    public void popularStatusChanged(boolean status) {
        changeStreamSourceStatus(null, null, status, null);
    }

    @Override
    public void debutsStatusChanged(boolean status) {
        changeStreamSourceStatus(null, null, null, status);
    }

    @Override
    public void customizationStatusChanged(boolean isDetails) {
        final Subscription subscription = settingsManager.changeShotsDetailsStatus(isDetails)
                .compose(RxTransformers.applyCompletableIoSchedulers())
                .subscribe(() -> Timber.d("Customization settings changed"),
                        throwable -> Timber.e(throwable, "Error while changing customization settings"));
        subscriptions.add(subscription);
    }

    private void changeStreamSourceStatus(@Nullable Boolean isFollowing, @Nullable Boolean isNew,
                                          @Nullable Boolean isPopular, @Nullable Boolean isDebuts) {
        final Subscription subscription = settingsManager.changeStreamSourceSettings(isFollowing, isNew, isPopular, isDebuts)
                .compose(RxTransformers.applyCompletableIoSchedulers())
                .subscribe(() -> Timber.d("Stream source settings changed"),
                        throwable -> Timber.e(throwable, "Error while changing stream source settings"));
        subscriptions.add(subscription);
    }

    private void saveNotificationStatus(boolean status) {
        final Subscription subscription = settingsManager.changeNotificationStatus(status)
                .compose(RxTransformers.applyCompletableIoSchedulers())
                .subscribe(() -> Timber.d("New notification status saved"),
                        throwable -> Timber.e(throwable, "Error while saving notification status"));
        subscriptions.add(subscription);
    }

    private void showTimePickDialog(NotificationSettings notificationSettings) {
        getView().showTimePickDialog(notificationSettings.getHour(), notificationSettings.getMinute(),
                (view, hour, minute) -> onTimePicked(hour, minute));
    }

    private void onTimePicked(int hour, int minute) {
        final Subscription subscription = settingsManager.changeNotificationTime(hour, minute)
                .andThen(notificationController.scheduleNotification())
                .compose(RxTransformers.applySingleIoSchedulers())
                .subscribe(notificationSettings ->
                                getView().showNotificationTime(localTimeFormatter.getFormattedTime(hour, minute)),
                        this::onScheduleNotificationError);

        subscriptions.add(subscription);
    }

    private void prepareUserSettings() {
        final Subscription subscription = settingsManager.getSettings()
                .doOnSuccess(settings -> checkNotificationSchedule(settings.getNotificationSettings()))
                .compose(RxTransformers.applySingleIoSchedulers())
                .subscribe(this::setSettings,
                        throwable -> Timber.e(throwable, "Error while getting settings"));
        subscriptions.add(subscription);
    }

    private void checkNotificationSchedule(NotificationSettings settings) {
        if (settings.isEnabled()) {
            notificationController.scheduleNotification()
                    .compose(RxTransformers.applySingleIoSchedulers())
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
        getView().changeFollowingStatus(streamSourceSettings.isFollowing());
        getView().changeNewStatus(streamSourceSettings.isNewToday());
        getView().changePopularStatus(streamSourceSettings.isPopularToday());
        getView().changeDebutsStatus(streamSourceSettings.isDebut());
    }

    private void onScheduleNotificationNext(NotificationSettings settings) {
        Timber.d("Notification scheduled : %s", settings);
    }

    private void onScheduleNotificationError(Throwable throwable) {
        Timber.e(throwable, "Error while scheduling notification");
    }

    private void showUserData(User user) {
        getView().showUserName(user.username());
        getView().showUserPhoto(user.avatarUrl());
    }
}

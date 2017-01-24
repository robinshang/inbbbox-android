package co.netguru.android.inbbbox.feature.main;

import android.support.annotation.Nullable;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.common.utils.DateTimeFormatUtil;
import co.netguru.android.inbbbox.common.utils.RxTransformerUtil;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserController;
import co.netguru.android.inbbbox.data.onboarding.OnboardingController;
import co.netguru.android.inbbbox.data.session.controllers.LogoutController;
import co.netguru.android.inbbbox.data.session.controllers.TokenParametersController;
import co.netguru.android.inbbbox.data.settings.SettingsController;
import co.netguru.android.inbbbox.data.settings.model.CustomizationSettings;
import co.netguru.android.inbbbox.data.settings.model.NotificationSettings;
import co.netguru.android.inbbbox.data.settings.model.Settings;
import co.netguru.android.inbbbox.data.settings.model.StreamSourceSettings;
import co.netguru.android.inbbbox.feature.main.MainViewContract.Presenter;
import co.netguru.android.inbbbox.feature.remindernotification.NotificationController;
import co.netguru.android.inbbbox.feature.remindernotification.NotificationScheduler;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.inbbbox.common.utils.RxTransformerUtil.applySingleComputationSchedulers;
import static co.netguru.android.inbbbox.common.utils.RxTransformerUtil.applySingleIoSchedulers;

@ActivityScope
public final class MainActivityPresenter extends MvpNullObjectBasePresenter<MainViewContract.View>
        implements Presenter {

    private final UserController userController;
    private final NotificationScheduler notificationScheduler;
    private final NotificationController notificationController;
    private final SettingsController settingsController;
    private final TokenParametersController tokenParametersController;
    private final LogoutController logoutController;
    private final ErrorController errorController;
    private final OnboardingController onboardingController;
    private final CompositeSubscription subscriptions;

    private boolean isFollowing;
    private boolean isNew;
    private boolean isPopular;
    private boolean isDebut;

    @Nullable
    private User user;

    @Inject
    MainActivityPresenter(UserController userController,
                          NotificationScheduler notificationScheduler,
                          NotificationController notificationController,
                          SettingsController settingsController,
                          ErrorController errorController,
                          TokenParametersController tokenParametersController,
                          LogoutController logoutController, OnboardingController onboardingController) {
        this.userController = userController;
        this.notificationScheduler = notificationScheduler;
        this.notificationController = notificationController;
        this.settingsController = settingsController;
        this.tokenParametersController = tokenParametersController;
        this.logoutController = logoutController;
        this.errorController = errorController;
        this.onboardingController = onboardingController;
        this.subscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        subscriptions.clear();
    }

    @Override
    public void toggleButtonChanged(boolean isChecked) {
        if (isChecked) {
            getView().showLogoutMenu();
            if (user != null) {
                getView().showUserName(user.name());
            }
        } else {
            getView().showMainMenu();
            if (user != null) {
                getView().showUserName(user.username());
            }
        }
    }

    @Override
    public void performLogout() {
        notificationScheduler.cancelNotification();
        subscriptions.add(
                logoutController.performLogout()
                        .subscribe(getView()::showLoginActivity,
                                throwable -> handleError(throwable, "critical logout error"))
        );
    }

    @Override
    public void prepareUserData() {
        subscriptions.add(
                userController.isGuestModeEnabled()
                        .compose(applySingleIoSchedulers())
                        .subscribe(this::verifyGuestMode,
                                throwable -> Timber
                                        .e(throwable, "Error while getting guest mode state"))
        );
        prepareUserSettings();
        prepareOnboardingSettings();
    }

    private void verifyGuestMode(Boolean isGuestModeEnabled) {
        if (!isGuestModeEnabled) {
            requestUserData();
        } else {
            getView().showCreateAccountButton();
        }
    }

    @Override
    public void timeViewClicked() {
        final Subscription subscription = settingsController.getNotificationSettings()
                .compose(applySingleIoSchedulers())
                .subscribe(this::showTimePickDialog,
                        throwable -> {
                            handleError(throwable, "Error while getting settings");
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
                .compose(applySingleComputationSchedulers())
                .subscribe(this::onScheduleNotificationNext, this::onScheduleNotificationError);

        subscriptions.add(subscription);
    }

    @Override
    public void followingStatusChanged(boolean status) {
        isFollowing = status;
        changeStreamSourceStatusIfCorrect();
    }

    @Override
    public void newStatusChanged(boolean status) {
        isNew = status;
        changeStreamSourceStatusIfCorrect();
    }

    @Override
    public void popularStatusChanged(boolean status) {
        isPopular = status;
        changeStreamSourceStatusIfCorrect();
    }

    @Override
    public void debutsStatusChanged(boolean status) {
        isDebut = status;
        changeStreamSourceStatusIfCorrect();
    }

    @Override
    public void customizationStatusChanged(boolean isDetails) {
        final Subscription subscription = settingsController.changeShotsDetailsStatus(isDetails)
                .compose(RxTransformerUtil.applyCompletableIoSchedulers())
                .subscribe(() -> Timber.d("Customization settings changed"),
                        throwable -> handleError(throwable, "Error while changing customization settings"));
        subscriptions.add(subscription);
    }

    @Override
    public void nightModeChanged(boolean isNightMode) {
        final Subscription subscription = settingsController.changeNightMode(isNightMode)
                .compose(RxTransformerUtil.applyCompletableIoSchedulers())
                .subscribe(() -> {
                    Timber.d("Customization settings changed");
                    getView().changeNightMode(isNightMode);
                }, throwable -> handleError(throwable, "Error while changing customization settings"));
        subscriptions.add(subscription);
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }

    @Override
    public void onTimePicked(int hour, int minute) {
        subscriptions.add(settingsController.changeNotificationTime(hour, minute)
                .andThen(notificationController.scheduleNotification())
                .compose(applySingleComputationSchedulers())
                .subscribe(notificationSettings ->
                                getView().showNotificationTime(DateTimeFormatUtil.getFormattedTime(hour, minute)),
                        this::onScheduleNotificationError));
    }

    @Override
    public void onCreateAccountClick() {
        subscriptions.add(
                tokenParametersController
                        .getSignUpUrl()
                        .subscribe(getView()::openSignUpPage,
                                throwable -> Timber
                                        .e(throwable, "Error during sign up url retrieving"))
        );
    }

    private void requestUserData() {
        subscriptions.add(
                userController.getUserFromCache()
                        .subscribe(this::handleUserData,
                                throwable -> Timber.e(throwable, "Error while getting user")));
    }

    private void changeStreamSourceStatusIfCorrect() {
        if (isFollowing || isNew || isPopular || isDebut) {
            changeStreamSourceStatus();
        } else {
            canNotChangeStreamSourceStatus();
        }
    }

    private void changeStreamSourceStatus() {
        final Subscription subscription = settingsController.changeStreamSourceSettings(isFollowing, isNew, isPopular, isDebut)
                .compose(RxTransformerUtil.applyCompletableIoSchedulers())
                .subscribe(() -> {
                            Timber.d("Stream source settings changed");
                            getView().refreshShotsView();
                        },
                        throwable -> Timber.e(throwable, "Error while changing stream source settings"));
        subscriptions.add(subscription);
    }

    private void canNotChangeStreamSourceStatus() {
        getView().showMessage(R.string.change_stream_source_error);
        prepareUserSettings();
    }

    private void saveNotificationStatus(boolean status) {
        final Subscription subscription = settingsController.changeNotificationStatus(status)
                .compose(RxTransformerUtil.applyCompletableIoSchedulers())
                .subscribe(() -> Timber.d("New notification status saved"),
                        throwable -> Timber.e(throwable, "Error while saving notification status"));
        subscriptions.add(subscription);
    }

    private void showTimePickDialog(NotificationSettings notificationSettings) {
        getView().showTimePickDialog(notificationSettings.getHour(), notificationSettings.getMinute());
    }

    private void prepareUserSettings() {
        final Subscription subscription = settingsController.getSettings()
                .doOnSuccess(settings -> checkNotificationSchedule(settings.getNotificationSettings()))
                .compose(applySingleIoSchedulers())
                .subscribe(this::setSettings,
                        throwable -> Timber.e(throwable, "Error while getting settings"));
        subscriptions.add(subscription);
    }

    private void checkNotificationSchedule(NotificationSettings settings) {
        if (settings.isEnabled()) {
            notificationController.scheduleNotification()
                    .compose(applySingleComputationSchedulers())
                    .subscribe(this::onScheduleNotificationNext, this::onScheduleNotificationError);
        }
    }

    private void setSettings(Settings settings) {
        setNotificationSettings(settings.getNotificationSettings());
        setStreamSourceSettings(settings.getStreamSourceSettings());
        setCustomizationSettings(settings.getCustomizationSettings());
        getView().setSettingsListeners();
    }

    private void prepareOnboardingSettings() {
        onboardingController.isOnboardingPassed()
                .subscribe(isOnboardingPassed -> {
                    getView().initializePager(isOnboardingPassed);
                });
    }

    private void setNotificationSettings(NotificationSettings notificationSettings) {
        getView().changeNotificationStatus(notificationSettings.isEnabled());
        getView().showNotificationTime(DateTimeFormatUtil.getFormattedTime(notificationSettings.getHour(),
                notificationSettings.getMinute()));
    }

    private void setStreamSourceSettings(StreamSourceSettings streamSourceSettings) {
        isFollowing = streamSourceSettings.isFollowing();
        isNew = streamSourceSettings.isNewToday();
        isPopular = streamSourceSettings.isPopularToday();
        isDebut = streamSourceSettings.isDebut();
        
        setStreamSourcesInView(streamSourceSettings);
    }

    private void setStreamSourcesInView(StreamSourceSettings streamSourceSettings) {
        getView().changeFollowingStatus(streamSourceSettings.isFollowing());
        getView().changeNewStatus(streamSourceSettings.isNewToday());
        getView().changePopularStatus(streamSourceSettings.isPopularToday());
        getView().changeDebutsStatus(streamSourceSettings.isDebut());
    }

    private void setCustomizationSettings(CustomizationSettings settings) {
        getView().changeCustomizationStatus(settings.isShowDetails());
        getView().setNightModeStatus(settings.isNightMode());
    }

    private void onScheduleNotificationNext(NotificationSettings settings) {
        Timber.d("Notification scheduled : %s", settings);
    }

    private void onScheduleNotificationError(Throwable throwable) {
        Timber.e(throwable, "Error while scheduling notification");
    }

    private void handleUserData(User user) {
        this.user = user;
        getView().showUserName(user.username());
        getView().showUserPhoto(user.avatarUrl());
    }
}

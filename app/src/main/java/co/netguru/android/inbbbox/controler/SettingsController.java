package co.netguru.android.inbbbox.controler;

import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.localrepository.SettingsPrefsRepository;
import co.netguru.android.inbbbox.model.localrepository.CustomizationSettings;
import co.netguru.android.inbbbox.model.localrepository.NotificationSettings;
import co.netguru.android.inbbbox.model.localrepository.Settings;
import co.netguru.android.inbbbox.model.localrepository.StreamSourceSettings;
import rx.Completable;
import rx.Single;

@Singleton
public class SettingsController {

    private final SettingsPrefsRepository settingsPrefsRepository;
    private final ShotsPagingController shotsPagingController;

    @Inject
    public SettingsController(SettingsPrefsRepository settingsPrefsRepository, ShotsPagingController shotsPagingController) {
        this.settingsPrefsRepository = settingsPrefsRepository;
        this.shotsPagingController = shotsPagingController;
    }

    public Single<Settings> getSettings() {
        return Single.zip(
                settingsPrefsRepository.getStreamSourceSettings(),
                settingsPrefsRepository.getNotificationsSettings(),
                settingsPrefsRepository.getCustomizationSettings(),
                Settings::new
        );
    }

    public Single<NotificationSettings> getNotificationSettings() {
        return settingsPrefsRepository.getNotificationsSettings();
    }

    public Single<StreamSourceSettings> getStreamSourceSettings() {
        return settingsPrefsRepository.getStreamSourceSettings();
    }

    public Single<CustomizationSettings> getCustomizationSettings() {
        return settingsPrefsRepository.getCustomizationSettings();
    }

    public Completable changeNotificationStatus(boolean isEnabled) {
        return settingsPrefsRepository.getNotificationsSettings()
                .map(notificationSettings -> new NotificationSettings(isEnabled,
                        notificationSettings.getHour(),
                        notificationSettings.getMinute()))
                .flatMapCompletable(settingsPrefsRepository::saveNotificationSettings);
    }

    public Completable changeNotificationTime(int hour, int minute) {
        return settingsPrefsRepository.getNotificationsSettings()
                .map(notificationSettings -> new NotificationSettings(notificationSettings.isEnabled(),
                        hour,
                        minute))
                .flatMapCompletable(settingsPrefsRepository::saveNotificationSettings);
    }

    public Completable changeStreamSourceSettings(@Nullable Boolean isFollowing, @Nullable Boolean isNew,
                                                  @Nullable Boolean isPopular, @Nullable Boolean isDebuts) {
        return settingsPrefsRepository.getStreamSourceSettings()
                .doOnSuccess(settings -> shotsPagingController.setFirstShotsPage())
                .map(streamSourceSettings -> getNewStreamSourceSettings(streamSourceSettings,
                        isFollowing, isNew, isPopular, isDebuts))
                .flatMapCompletable(settingsPrefsRepository::saveStreamSourceSettingsToPrefs);
    }

    public Completable changeShotsDetailsStatus(boolean isEnabled) {
        return settingsPrefsRepository.saveDetailsShowed(isEnabled);
    }

    private StreamSourceSettings getNewStreamSourceSettings(StreamSourceSettings settings, @Nullable Boolean isFollowing,
                                                            @Nullable Boolean isNew, @Nullable Boolean isPopular,
                                                            @Nullable Boolean isDebut) {
        return new StreamSourceSettings(
                isFollowing == null ? settings.isFollowing() : isFollowing,
                isNew == null ? settings.isNewToday() : isNew,
                isPopular == null ? settings.isPopularToday() : isPopular,
                isDebut == null ? settings.isDebut() : isDebut
        );
    }
}

package co.netguru.android.inbbbox.data.settings;

import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.settings.model.CustomizationSettings;
import co.netguru.android.inbbbox.data.settings.model.NotificationSettings;
import co.netguru.android.inbbbox.data.settings.model.Settings;
import co.netguru.android.inbbbox.data.settings.model.StreamSourceSettings;
import rx.Completable;
import rx.Single;

@Singleton
public class SettingsController {

    private final SettingsPrefsRepository settingsPrefsRepository;

    @Inject
    public SettingsController(SettingsPrefsRepository settingsPrefsRepository) {
        this.settingsPrefsRepository = settingsPrefsRepository;
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
                .map(streamSourceSettings -> getNewStreamSourceSettings(streamSourceSettings,
                        isFollowing, isNew, isPopular, isDebuts))
                .flatMapCompletable(settingsPrefsRepository::saveStreamSourceSettingsToPrefs);
    }

    public Completable changeShotsDetailsStatus(boolean isEnabled) {
        return settingsPrefsRepository.saveDetailsShowed(isEnabled);
    }

    public Completable changeNightMode(boolean isEnabled) {
        return settingsPrefsRepository.saveNightMode(isEnabled);
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

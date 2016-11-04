package co.netguru.android.inbbbox.feature.settings;

import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.local.SettingsPrefsController;
import co.netguru.android.inbbbox.models.CustomizationSettings;
import co.netguru.android.inbbbox.models.NotificationSettings;
import co.netguru.android.inbbbox.models.Settings;
import co.netguru.android.inbbbox.models.StreamSourceSettings;
import rx.Completable;
import rx.Observable;
import rx.Single;

@Singleton
public class SettingsManager {

    private final SettingsPrefsController settingsPrefsController;

    @Inject
    public SettingsManager(SettingsPrefsController settingsPrefsController) {
        this.settingsPrefsController = settingsPrefsController;
    }

    public Single<Settings> getSettings() {
        return Single.zip(
                settingsPrefsController.getStreamSourceSettings(),
                settingsPrefsController.getNotificationsSettings(),
                settingsPrefsController.detailsShowed(),
                (streamSourceSettings, notificationSettings, detailsShowed) ->
                        new Settings(streamSourceSettings, notificationSettings, new CustomizationSettings(detailsShowed))
        );
    }

    public Single<NotificationSettings> getNotificationSettings() {
        return settingsPrefsController.getNotificationsSettings();
    }

    public Completable changeNotificationStatus(boolean isEnabled) {
        return settingsPrefsController.getNotificationsSettings()
                .map(notificationSettings -> new NotificationSettings(isEnabled,
                        notificationSettings.getHour(),
                        notificationSettings.getMinute()))
                .doOnSuccess(settingsPrefsController::saveNotificationSettings)
                .toCompletable();
    }

    public Completable changeNotificationTime(int hour, int minute) {
        return settingsPrefsController.getNotificationsSettings()
                .map(notificationSettings -> new NotificationSettings(notificationSettings.isEnabled(),
                        hour,
                        minute))
                .doOnSuccess(settingsPrefsController::saveNotificationSettings)
                .toCompletable();
    }

    public Completable changeStreamSourceSettings(@Nullable Boolean isFollowing, @Nullable Boolean isNew,
                                                  @Nullable Boolean isPopular, @Nullable Boolean isDebuts) {
        return settingsPrefsController.getStreamSourceSettings()
                .map(streamSourceSettings -> getNewStreamSourceSettings(streamSourceSettings,
                        isFollowing, isNew, isPopular, isDebuts))
                .doOnSuccess(settingsPrefsController::saveStreamSourceSettingsToPrefs)
                .toCompletable();
    }

    public Completable changeShotsDetailsStatus(boolean isEnabled) {
        return settingsPrefsController.saveDetailsShowed(isEnabled);
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

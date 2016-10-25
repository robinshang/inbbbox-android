package co.netguru.android.inbbbox.feature.settings;

import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.models.CustomizationSettings;
import co.netguru.android.inbbbox.data.models.NotificationSettings;
import co.netguru.android.inbbbox.data.models.Settings;
import co.netguru.android.inbbbox.data.models.StreamSourceSettings;
import co.netguru.android.inbbbox.db.datasource.DataSource;
import rx.Observable;

@Singleton
public final class SettingsManager {

    @VisibleForTesting
    final Settings defaultSettings;

    private final DataSource<Settings> settingsDataSource;

    @Inject
    public SettingsManager(DataSource<Settings> settingsDataSource) {
        this.settingsDataSource = settingsDataSource;
        defaultSettings = new Settings();
    }

    public Observable<Settings> getSettings() {
        return settingsDataSource.get()
                .onErrorReturn(throwable -> defaultSettings);
    }

    public Observable<NotificationSettings> getNotificationSettings() {
        return getSettings()
                .map(Settings::getNotificationSettings);
    }

    public Observable<Boolean> changeNotificationStatus(boolean isEnabled) {
        return getSettings()
                .map(settings -> new Settings(settings.getStreamSourceSettings(),
                        new NotificationSettings(isEnabled,
                                settings.getNotificationSettings().getHour(),
                                settings.getNotificationSettings().getMinute()),
                        settings.getCustomizationSettings()))
                .flatMap(settingsDataSource::save);
    }

    public Observable<Boolean> changeNotificationTime(int hour, int minute) {
        return getSettings()
                .map(settings -> new Settings(settings.getStreamSourceSettings(),
                        new NotificationSettings(settings.getNotificationSettings().isEnabled(), hour, minute),
                        settings.getCustomizationSettings()))
                .flatMap(settingsDataSource::save);
    }

    public Observable<Boolean> changeStreamSourceSettings(@Nullable Boolean isFollowing, @Nullable Boolean isNew,
                                                          @Nullable Boolean isPopular, @Nullable Boolean isDebuts) {
        return getSettings()
                .map(settings -> new Settings(
                        getNewStreamSourceSettings(settings.getStreamSourceSettings(), isFollowing, isNew, isPopular, isDebuts),
                        settings.getNotificationSettings(),
                        settings.getCustomizationSettings())
                )
                .flatMap(settingsDataSource::save);
    }

    public Observable<Boolean> changeShotsDetailsStatus(boolean isEnabled) {
        return getSettings()
                .map(settings -> new Settings(settings.getStreamSourceSettings(),
                        settings.getNotificationSettings(),
                        new CustomizationSettings(isEnabled)))
                .flatMap(settingsDataSource::save);
    }

    private StreamSourceSettings getNewStreamSourceSettings(StreamSourceSettings settings, @Nullable Boolean isFollowing,
                                                            @Nullable  Boolean isNew, @Nullable Boolean isPopular,
                                                            @Nullable Boolean isDebut) {
        return new StreamSourceSettings(
                isFollowing == null ? settings.isFollowing() : isFollowing,
                isNew == null ? settings.isNewToday() : isNew,
                isPopular == null ? settings.isPopularToday() : isPopular,
                isDebut == null ? settings.isDebut() : isDebut
        );
    }
}

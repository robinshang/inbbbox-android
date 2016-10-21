package co.netguru.android.inbbbox.feature.settings;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.models.NotificationSettings;
import co.netguru.android.inbbbox.data.models.Settings;
import co.netguru.android.inbbbox.db.datasource.DataSource;
import rx.Observable;

@Singleton
public final class SettingsManager {

    private final DataSource<Settings> settingsDataSource;
    private static final Settings DEFAULT_SETTINGS;

    static  {
        DEFAULT_SETTINGS = new Settings();
    }

    @Inject
    public SettingsManager(DataSource<Settings> settingsDataSource) {
        this.settingsDataSource = settingsDataSource;
    }

    public Observable<Settings> getSettings() {
        return settingsDataSource.get()
                .onErrorReturn(throwable -> DEFAULT_SETTINGS);
    }

    public Observable<Boolean> changeNotificationStatus(boolean isEnabled) {
        return getSettings()
                .map(settings -> new Settings(settings.getStreamSourceState(),
                        new NotificationSettings(isEnabled,
                                settings.getNotificationSettings().getHour(),
                                settings.getNotificationSettings().getMinute())))
                .flatMap(settingsDataSource::save);
    }

    public Observable<Boolean> changeNotificationTime(int hour, int minute) {
        return getSettings()
                .map(settings -> new Settings(settings.getStreamSourceState(),
                        new NotificationSettings(settings.getNotificationSettings().isEnabled(), hour, minute)))
                .flatMap(settingsDataSource::save);
    }
}

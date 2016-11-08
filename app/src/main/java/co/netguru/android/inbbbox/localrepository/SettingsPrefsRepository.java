package co.netguru.android.inbbbox.localrepository;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import org.threeten.bp.LocalTime;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.model.localrepository.CustomizationSettings;
import co.netguru.android.inbbbox.model.localrepository.NotificationSettings;
import co.netguru.android.inbbbox.model.localrepository.StreamSourceSettings;
import rx.Completable;
import rx.Single;

@Singleton
public class SettingsPrefsRepository {

    private static final String STREAM_SOURCE_SETTINGS_IS_DEBUT_KEY = "stream_source_settings_is_debut";
    private static final String STREAM_SOURCE_SETTINGS_IS_FOLLOWING_KEY = "stream_source_settings_is_following";
    private static final String STREAM_SOURCE_SETTINGS_IS_NEW_TODAY_KEY = "stream_source_settings_is_new_today";
    private static final String STREAM_SOURCE_SETTINGS_IS_POPULAR_TODAY_KEY = "stream_source_settings_is_popular_today";

    private static final String NOTIFICATION_SETTINGS_HOUR_KEY = "notification_settings_hour";
    private static final String NOTIFICATION_SETTINGS_MINUTE_KEY = "notification_settings_minute";
    private static final String NOTIFICATION_SETTINGS_IS_ENABLED_KEY = "notification_settings_is_enabled";

    private static final String DETAILS_SHOWED_KEY = "details_showed_key";

    private final SharedPreferences sharedPreferences;

    @Inject
    public SettingsPrefsRepository(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public Completable saveStreamSourceSettingsToPrefs(@NonNull StreamSourceSettings streamSourceSettings) {
        return Completable.fromCallable(() -> {
            sharedPreferences.edit()
                    .putBoolean(STREAM_SOURCE_SETTINGS_IS_DEBUT_KEY, streamSourceSettings.isDebut())
                    .putBoolean(STREAM_SOURCE_SETTINGS_IS_FOLLOWING_KEY, streamSourceSettings.isFollowing())
                    .putBoolean(STREAM_SOURCE_SETTINGS_IS_NEW_TODAY_KEY, streamSourceSettings.isNewToday())
                    .putBoolean(STREAM_SOURCE_SETTINGS_IS_POPULAR_TODAY_KEY, streamSourceSettings.isPopularToday())
                    .apply();
            return null;
        });
    }

    public Single<StreamSourceSettings> getStreamSourceSettings() {
        return Single.fromCallable(() ->
                new StreamSourceSettings(
                        sharedPreferences.getBoolean(STREAM_SOURCE_SETTINGS_IS_FOLLOWING_KEY, true),
                        sharedPreferences.getBoolean(STREAM_SOURCE_SETTINGS_IS_NEW_TODAY_KEY, false),
                        sharedPreferences.getBoolean(STREAM_SOURCE_SETTINGS_IS_POPULAR_TODAY_KEY, false),
                        sharedPreferences.getBoolean(STREAM_SOURCE_SETTINGS_IS_DEBUT_KEY, false)
                ));
    }

    public Completable saveNotificationSettings(@NonNull NotificationSettings notificationSettings) {
        return Completable.fromCallable(() -> {
            sharedPreferences.edit()
                    .putBoolean(NOTIFICATION_SETTINGS_IS_ENABLED_KEY, notificationSettings.isEnabled())
                    .putInt(NOTIFICATION_SETTINGS_HOUR_KEY, notificationSettings.getHour())
                    .putInt(NOTIFICATION_SETTINGS_MINUTE_KEY, notificationSettings.getMinute())
                    .apply();
            return null;
        });
    }

    public Single<NotificationSettings> getNotificationsSettings() {
        return Single.fromCallable(() ->
                new NotificationSettings(
                        sharedPreferences.getBoolean(NOTIFICATION_SETTINGS_IS_ENABLED_KEY, false),
                        sharedPreferences.getInt(NOTIFICATION_SETTINGS_HOUR_KEY, LocalTime.now().getHour()),
                        sharedPreferences.getInt(NOTIFICATION_SETTINGS_MINUTE_KEY, LocalTime.now().getMinute())
                ));
    }

    public Completable saveDetailsShowed(boolean showed) {
        return Completable.fromCallable(() -> {
            sharedPreferences.edit()
                    .putBoolean(DETAILS_SHOWED_KEY, showed)
                    .apply();
            return null;
        });
    }

    public Single<CustomizationSettings> getCustomizationSettings() {
        return Single.fromCallable(() ->
                new CustomizationSettings(sharedPreferences.getBoolean(DETAILS_SHOWED_KEY, false)));
    }
}

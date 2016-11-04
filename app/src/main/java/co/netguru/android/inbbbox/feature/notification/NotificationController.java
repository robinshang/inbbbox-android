package co.netguru.android.inbbbox.feature.notification;

import android.os.Build;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.feature.settings.SettingsManager;
import co.netguru.android.inbbbox.models.NotificationSettings;
import rx.Single;

@Singleton
public final class NotificationController {

    private final SettingsManager settingsManager;
    private final NotificationScheduler notificationScheduler;

    @Inject
    public NotificationController(SettingsManager settingsManager, NotificationScheduler notificationScheduler) {
        this.settingsManager = settingsManager;
        this.notificationScheduler = notificationScheduler;
    }

    public Single<NotificationSettings> scheduleNotification() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return settingsManager.getNotificationSettings()
                    .doOnSuccess(settings -> notificationScheduler.scheduleRepeatingNotification(settings.getHour(),
                            settings.getMinute()));
        }

        return settingsManager.getNotificationSettings()
                .doOnSuccess(settings -> notificationScheduler.scheduleNotificationWhileIdle(settings.getHour(),
                        settings.getMinute()));
    }

    public Single<Boolean> rescheduleNotification() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return Single.just(false);
        }
        return settingsManager.getNotificationSettings()
                .doOnSuccess(settings -> notificationScheduler.scheduleNotificationWhileIdle(settings.getHour(),
                        settings.getMinute()))
                .flatMap(notificationSettings -> Single.just(true));
    }
}

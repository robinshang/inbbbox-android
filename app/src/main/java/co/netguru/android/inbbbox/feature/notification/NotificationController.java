package co.netguru.android.inbbbox.feature.notification;

import android.os.Build;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.models.NotificationSettings;
import co.netguru.android.inbbbox.feature.settings.SettingsManager;
import rx.Observable;

@Singleton
public final class NotificationController {

    private final SettingsManager settingsManager;
    private final NotificationScheduler notificationScheduler;

    @Inject
    public NotificationController(SettingsManager settingsManager, NotificationScheduler notificationScheduler) {
        this.settingsManager = settingsManager;
        this.notificationScheduler = notificationScheduler;
    }

    public Observable<NotificationSettings> scheduleNotification() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return settingsManager.getNotificationSettings()
                    .doOnNext(settings -> notificationScheduler.scheduleRepeatingNotification(settings.getHour(),
                            settings.getMinute()));
        }

        return settingsManager.getNotificationSettings()
                .doOnNext(settings -> notificationScheduler.scheduleNotificationWhileIdle(settings.getHour(),
                        settings.getMinute()));
    }
    public Observable<Boolean> rescheduleNotification() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return Observable.just(false);
        }
        return settingsManager.getNotificationSettings()
                .doOnNext(settings -> notificationScheduler.scheduleNotificationWhileIdle(settings.getHour() ,
                        settings.getMinute()))
                .map(settings -> true);
    }
}

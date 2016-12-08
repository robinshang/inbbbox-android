package co.netguru.android.inbbbox.controler.notification;

import android.os.Build;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.controler.SettingsController;
import co.netguru.android.inbbbox.model.localrepository.NotificationSettings;
import rx.Single;

@Singleton
public class NotificationController {

    private final SettingsController settingsController;
    private final NotificationScheduler notificationScheduler;

    @Inject
    public NotificationController(SettingsController settingsController, NotificationScheduler notificationScheduler) {
        this.settingsController = settingsController;
        this.notificationScheduler = notificationScheduler;
    }

    public Single<NotificationSettings> scheduleNotification() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return settingsController.getNotificationSettings()
                    .doOnSuccess(settings -> notificationScheduler.scheduleRepeatingNotification(settings.getHour(),
                            settings.getMinute()));
        }

        return settingsController.getNotificationSettings()
                .doOnSuccess(settings -> notificationScheduler.scheduleNotificationWhileIdle(settings.getHour(),
                        settings.getMinute()));
    }

    public Single<Boolean> rescheduleNotification() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return Single.just(false);
        }
        return settingsController.getNotificationSettings()
                .doOnSuccess(settings -> notificationScheduler.scheduleNotificationWhileIdle(settings.getHour(),
                        settings.getMinute()))
                .flatMap(notificationSettings -> Single.just(true));
    }
}

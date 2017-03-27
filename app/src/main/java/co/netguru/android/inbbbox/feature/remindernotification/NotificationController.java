package co.netguru.android.inbbbox.feature.remindernotification;

import android.os.Build;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.settings.SettingsController;
import co.netguru.android.inbbbox.data.settings.model.NotificationSettings;
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
        return settingsController.getNotificationSettings()
                .doOnSuccess(this::scheduleNotificationForRightBuildVersion);
    }

    private void scheduleNotificationForRightBuildVersion(NotificationSettings settings) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            notificationScheduler.scheduleExactNotification(settings.getHour(),
                    settings.getMinute());
        } else {
            notificationScheduler.scheduleExactNotificationWhileIdle(settings.getHour(),
                    settings.getMinute());
        }
    }

    Single<Boolean> rescheduleNotification() {
        return scheduleNotification()
                .flatMap(notificationSettings -> Single.just(Boolean.TRUE));
    }
}

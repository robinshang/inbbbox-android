package co.netguru.android.inbbbox.feature.notification;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import org.threeten.bp.LocalTime;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.utils.LocalTimeFormatter;
import timber.log.Timber;

@Singleton
public final class NotificationScheduler {

    private static final int BROADCAST_REQUEST_CODE = 0;

    private final AlarmManager alarmManager;
    private final Context appContext;
    private final LocalTimeFormatter localTimeFormatter;

    @Inject
    public NotificationScheduler(AlarmManager alarmManager, Context appContext, LocalTimeFormatter localTimeFormatter) {
        this.alarmManager = alarmManager;
        this.appContext = appContext;
        this.localTimeFormatter = localTimeFormatter;
    }

    public void scheduleRepeatingNotification(int hour, int minute) {
        Timber.d("Scheduling repeating notification at : %s", LocalTime.of(hour, minute));
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, localTimeFormatter.getSecondsFromTime(hour, minute),
                AlarmManager.INTERVAL_DAY,
                createBroadcastPendingIntent());
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void scheduleNotificationWhileIdle(int hour, int minute) {
        Timber.d("Scheduling idle notification at : %s", LocalTime.of(hour, minute));
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, localTimeFormatter.getSecondsFromTime(hour, minute),
                createBroadcastPendingIntent());
    }

    public void cancelNotification() {
        Timber.d("Canceling notification");
        alarmManager.cancel(createBroadcastPendingIntent());
    }

    private PendingIntent createBroadcastPendingIntent() {
        final Intent intent = new Intent(appContext, NotificationAlarmReceiver.class);

        return PendingIntent.getBroadcast(appContext, BROADCAST_REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}

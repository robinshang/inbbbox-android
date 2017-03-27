package co.netguru.android.inbbbox.feature.remindernotification;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import org.threeten.bp.LocalTime;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.common.utils.DateTimeFormatUtil;
import timber.log.Timber;

@Singleton
public class NotificationScheduler {

    private static final int BROADCAST_REQUEST_CODE = 0;

    private final AlarmManager alarmManager;
    private final Context appContext;

    @Inject
    public NotificationScheduler(AlarmManager alarmManager, Context appContext) {
        this.alarmManager = alarmManager;
        this.appContext = appContext;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void scheduleExactNotification(int hour, int minute) {
        Timber.d("Scheduling exact notification at : %s", LocalTime.of(hour, minute));
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                DateTimeFormatUtil.getSecondsFromTime(hour, minute),
                createBroadcastPendingIntent());
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void scheduleExactNotificationWhileIdle(int hour, int minute) {
        Timber.d("Scheduling exact idle notification at : %s", LocalTime.of(hour, minute));
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                DateTimeFormatUtil.getSecondsFromTime(hour, minute),
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

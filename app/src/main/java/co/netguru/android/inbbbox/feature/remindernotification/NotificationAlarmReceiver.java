package co.netguru.android.inbbbox.feature.remindernotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import javax.inject.Inject;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.feature.splash.SplashActivity;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static co.netguru.android.inbbbox.feature.splash.SplashActivity.SPLASH_ACTIVITY_REQUEST_CODE;

public class NotificationAlarmReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 0;

    @Inject
    NotificationManager notificationManager;
    @Inject
    NotificationController notificationController;

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.d("Received new broadcast : %s", intent);
        App.getAppComponent(context).inject(this);

        notificationManager.notify(NOTIFICATION_ID, getNotification(context));
        notificationController.rescheduleNotification()
                .subscribeOn(Schedulers.computation())
                .subscribe(status -> Timber.d("Notification rescheduled : %s", status),
                        throwable -> Timber.e(throwable, "Error while rescheduling notification"));
    }

    public Notification getNotification(Context context) {
        return new NotificationCompat.Builder(context)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle(context.getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(createSplashActivityPendingIntent(context))
                .setContentText(context.getString(R.string.notification_text))
                .build();
    }

    private PendingIntent createSplashActivityPendingIntent(Context context) {
        final Intent intent = new Intent(context, SplashActivity.class);
        return PendingIntent.getActivity(context, SPLASH_ACTIVITY_REQUEST_CODE,
                intent, PendingIntent.FLAG_ONE_SHOT);
    }
}

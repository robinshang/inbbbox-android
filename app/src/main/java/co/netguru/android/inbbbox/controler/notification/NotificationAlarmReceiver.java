package co.netguru.android.inbbbox.controler.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import javax.inject.Inject;

import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.main.MainActivity;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class NotificationAlarmReceiver extends BroadcastReceiver {

    private static final int MAIN_ACTIVITY_REQUEST_CODE = 1;
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
                .subscribeOn(Schedulers.io())
                .subscribe(status -> Timber.d("Notification rescheduled : %s", status),
                        throwable -> Timber.e(throwable, "Error while rescheduling notification"));
    }

    public Notification getNotification(Context context) {
        return new NotificationCompat.Builder(context)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle(context.getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(createMainActivityPendingIntent(context))
                .setContentText(context.getString(R.string.notification_text))
                .build();
    }

    private PendingIntent createMainActivityPendingIntent(Context context) {
        final Intent intent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, MAIN_ACTIVITY_REQUEST_CODE,
                intent, PendingIntent.FLAG_ONE_SHOT);
    }
}

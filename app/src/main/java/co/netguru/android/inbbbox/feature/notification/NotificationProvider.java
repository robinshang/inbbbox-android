package co.netguru.android.inbbbox.feature.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.main.MainActivity;

@Singleton
public final class NotificationProvider {

    private static final int MAIN_ACTIVITY_REQUEST_CODE = 1;

    @Inject
    public NotificationProvider() {

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

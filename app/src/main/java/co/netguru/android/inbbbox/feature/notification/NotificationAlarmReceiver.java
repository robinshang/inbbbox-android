package co.netguru.android.inbbbox.feature.notification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import co.netguru.android.inbbbox.application.App;
import timber.log.Timber;

import static co.netguru.android.inbbbox.utils.Constants.Notification.*;

public class NotificationAlarmReceiver extends BroadcastReceiver {

    @Inject
    NotificationManager notificationManager;
    @Inject
    NotificationProvider notificationProvider;

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.d("Received new broadcast : %s", intent);
        App.getAppComponent(context).inject(this);

        notificationManager.notify(NOTIFICATION_ID, notificationProvider.getNotification(context));
    }
}

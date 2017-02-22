package co.netguru.android.inbbbox.common.analytics.event;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import timber.log.Timber;

public class BaseEvent {

    private final String name;
    private final Bundle params = new Bundle();

    public BaseEvent(String name) {
        this.name = name;
    }

    protected String getName() {
        return name;
    }

    protected Bundle getParams() {
        return params;
    }

    public void logEvent(FirebaseAnalytics analytics) {
        Timber.e("Event-%s", name);
        analytics.logEvent(name, params);
    }
}

package co.netguru.android.inbbbox.common.analytics;


import android.support.v4.widget.DrawerLayout;
import android.view.View;

import javax.inject.Inject;

public class AnalyticsDrawerListener implements DrawerLayout.DrawerListener {

    private final AnalyticsEventLogger analyticsEventLogger;

    @Inject
    public AnalyticsDrawerListener(AnalyticsEventLogger analyticsEventLogger) {
        this.analyticsEventLogger = analyticsEventLogger;
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        // no-op
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        analyticsEventLogger.logEventScreenSettings();
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        // no-op
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        // no-op
    }
}

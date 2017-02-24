package co.netguru.android.inbbbox.common.analytics.event;


import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class LocalSettingEvent extends BaseEvent {

    private static final String EVENT_NAME = "local_setting";
    private static final String PARAM_SETTING_VALUE = "enabled";

    public LocalSettingEvent(String settingName, boolean enabled) {
        super(EVENT_NAME);
        Bundle params = getParams();
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, settingName);
        params.putString(PARAM_SETTING_VALUE, Boolean.toString(enabled));
    }
}

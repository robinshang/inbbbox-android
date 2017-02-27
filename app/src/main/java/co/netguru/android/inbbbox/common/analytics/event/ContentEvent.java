package co.netguru.android.inbbbox.common.analytics.event;


import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class ContentEvent extends BaseEvent {

    public ContentEvent(String contentType, String itemId) {
        super(FirebaseAnalytics.Event.SELECT_CONTENT);
        Bundle params = getParams();
        params.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType);
        params.putString(FirebaseAnalytics.Param.ITEM_ID, itemId);
    }
}

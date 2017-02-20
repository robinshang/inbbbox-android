package co.netguru.android.inbbbox.common.analytics;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseAnalyticsHelper {

    private final FirebaseAnalytics analytics;

    //Screen events
    private final static String EVENT_SCREEN_BUCKETS = "screen:buckets";
    private final static String EVENT_SCREEN_FOLLOWEES = "screen:followees";
    private final static String EVENT_SCREEN_LOGIN = "screen:login";
    private final static String EVENT_SCREEN_SETTINGS = "screen:settings";
    private final static String EVENT_SCREEN_SHOT_BUCKETS = "screen:shot_buckets";
    private final static String EVENT_SCREEN_SHOT_DETAILS = "screen:shot_details";
    private final static String EVENT_SCREEN_SHOTS = "screen:shots";

    //Login events
    private final static String EVENT_LOGIN_SUCCESS = "login:login_success";
    private final static String EVENT_LOGIN_FAILED = "login:login_failed";
    private final static String EVENT_LOGIN_GUEST = "login:login_guest";

    //User action events
    private final static String EVENT_USER_LIKE = "user:like";
    private final static String EVENT_USER_ADD_TO_BUCKET = "user:add_to_bucket";
    private final static String EVENT_USER_COMMENT = "user:comment";
    private final static String EVENT_USER_SWIPE_DOWN = "user:swipe_down";

    //Settings events
    private final static String EVENT_SETTINGS_DAILY_REMINDER = "settings:daily_reminder_enable";
    private final static String EVENT_SETTINGS_SOURCE_FOLLOWING =
            "settings:stream_source_following";
    private final static String EVENT_SETTINGS_SOURCE_NEW_TODAY =
            "settings:stream_source_new_today";
    private final static String EVENT_SETTINGS_SOURCE_POPULAR_TODAY =
            "settings:stream_source_popular_today";
    private final static String EVENT_SETTINGS_SOURCE_DEBUTS = "settings:stream_source_debuts";
    private final static String EVENT_SETTINGS_AUTHOR_ON_HOME = "settings:author_on_home_screen";

    //API events
    private final static String EVENT_API_REQUESTS_REMAINING = "api:requests_remaining";

    public FirebaseAnalyticsHelper(FirebaseAnalytics analytics) {
        this.analytics = analytics;
    }
}

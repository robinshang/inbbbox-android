package co.netguru.android.inbbbox.common.analytics;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

class FirebaseAnalyticsEventLogger implements AnalyticsEventLogger {

    /**
     * Events
     */
    //Screen events
    private static final String EVENT_SCREEN_BUCKETS = "screen_buckets";
    private static final String EVENT_SCREEN_FOLLOWEES = "screen_followees";
    private static final String EVENT_SCREEN_LOGIN = "screen_login";
    private static final String EVENT_SCREEN_SETTINGS = "screen_settings";
    private static final String EVENT_SCREEN_SHOT_BUCKETS = "screen_shot_buckets";
    private static final String EVENT_SCREEN_SHOT_DETAILS = "screen_shot_details";
    private static final String EVENT_SCREEN_SHOTS = "screen_shots";
    private static final String EVENT_SCREEN_LIKES = "screen_likes";

    //Login events
    private static final String EVENT_LOGIN_SUCCESS = "login_success";
    private static final String EVENT_LOGIN_FAILED = "login_failed";
    private static final String EVENT_LOGIN_GUEST = "login_guest";

    //User action events
    private static final String EVENT_USER_LIKE = "user_like";
    private static final String EVENT_USER_ADD_TO_BUCKET = "user_add_to_bucket";
    private static final String EVENT_USER_COMMENT = "user_comment";
    private static final String EVENT_USER_SWIPE_DOWN = "user_swipe_down";
    private static final String EVENT_USER_FOLLOW = "user_follow";

    //Settings events
    private static final String EVENT_SETTINGS_DAILY_REMINDER = "settings_daily_reminder_enable";
    private static final String EVENT_SETTINGS_SOURCE_FOLLOWING =
            "settings_source_following";
    private static final String EVENT_SETTINGS_SOURCE_NEW_TODAY =
            "settings_source_new_today";
    private static final String EVENT_SETTINGS_SOURCE_POPULAR_TODAY =
            "settings_source_popular_today";
    private static final String EVENT_SETTINGS_SOURCE_DEBUTS = "settings_source_debuts";
    private static final String EVENT_SETTINGS_AUTHOR_ON_HOME = "settings_author_on_home_screen";
    private static final String EVENT_SETTINGS_NIGHTMODE = "settings_night_mode";
    private static final String EVENT_SETTINGS_NIGHTMODE_AUTO = "settings_night_mode_auto";

    //API events
    private static final String EVENT_API_REQUESTS_REMAINING = "api_requests_remaining";

    /**
     * Params
     */
    private static final String PARAM_REQUESTS_REMAINING = "param_requests_remaining";

    private final FirebaseAnalytics analytics;

    FirebaseAnalyticsEventLogger(FirebaseAnalytics analytics) {
        this.analytics = analytics;
    }

    @Override
    public void logEventScreenBuckets() {
        analytics.logEvent(EVENT_SCREEN_BUCKETS, new Bundle());
    }

    @Override
    public void logEventScreenFollowees() {
        analytics.logEvent(EVENT_SCREEN_FOLLOWEES, new Bundle());
    }

    @Override
    public void logEventScreenLikes() {
        analytics.logEvent(EVENT_SCREEN_LIKES, new Bundle());
    }

    @Override
    public void logEventScreenLogin() {
        analytics.logEvent(EVENT_SCREEN_LOGIN, new Bundle());
    }

    @Override
    public void logEventScreenSettings() {
        analytics.logEvent(EVENT_SCREEN_SETTINGS, new Bundle());
    }

    @Override
    public void logEventScreenShotBuckets() {
        analytics.logEvent(EVENT_SCREEN_SHOT_BUCKETS, new Bundle());
    }

    @Override
    public void logEventScreenShotDetails() {
        analytics.logEvent(EVENT_SCREEN_SHOT_DETAILS, new Bundle());
    }

    @Override
    public void logEventScreenShots() {
        analytics.logEvent(EVENT_SCREEN_SHOTS, new Bundle());
    }

    @Override
    public void logEventLoginSuccess() {
        analytics.logEvent(EVENT_LOGIN_SUCCESS, new Bundle());
    }

    @Override
    public void logEventLoginFail() {
        analytics.logEvent(EVENT_LOGIN_FAILED, new Bundle());
    }

    @Override
    public void logEventLoginGuest() {
        analytics.logEvent(EVENT_LOGIN_GUEST, new Bundle());
    }

    @Override
    public void logEventUserLike() {
        analytics.logEvent(EVENT_USER_LIKE, new Bundle());
    }

    @Override
    public void logEventUserAddToBucket() {
        analytics.logEvent(EVENT_USER_ADD_TO_BUCKET, new Bundle());
    }

    @Override
    public void logEventUserComment() {
        analytics.logEvent(EVENT_USER_COMMENT, new Bundle());
    }

    @Override
    public void logEventUserSwipeDown() {
        analytics.logEvent(EVENT_USER_SWIPE_DOWN, new Bundle());
    }

    @Override
    public void logEventUserFollow() {
        analytics.logEvent(EVENT_USER_FOLLOW, new Bundle());
    }

    @Override
    public void logEventApiRequestsRemaining(int requestsRemaining) {
        Bundle params = new Bundle();
        params.putInt(PARAM_REQUESTS_REMAINING, requestsRemaining);
        analytics.logEvent(EVENT_API_REQUESTS_REMAINING, params);
    }

    @Override
    public void logEventSettingsAuthorOnHome() {
        analytics.logEvent(EVENT_SETTINGS_AUTHOR_ON_HOME, new Bundle());
    }

    @Override
    public void logEventSettingsReminder() {
        analytics.logEvent(EVENT_SETTINGS_DAILY_REMINDER, new Bundle());
    }

    @Override
    public void logEventSettingsNightmode() {
        analytics.logEvent(EVENT_SETTINGS_NIGHTMODE, new Bundle());
    }

    @Override
    public void logEventSettingsNightmodeAuto() {
        analytics.logEvent(EVENT_SETTINGS_NIGHTMODE_AUTO, new Bundle());
    }

    @Override
    public void logEventSettingsDebuts() {
        analytics.logEvent(EVENT_SETTINGS_SOURCE_DEBUTS, new Bundle());
    }

    @Override
    public void logEventSettingsFollowing() {
        analytics.logEvent(EVENT_SETTINGS_SOURCE_FOLLOWING, new Bundle());
    }

    @Override
    public void logEventSettingsNewToday() {
        analytics.logEvent(EVENT_SETTINGS_SOURCE_NEW_TODAY, new Bundle());
    }

    @Override
    public void logEventSettingsPopularToday() {
        analytics.logEvent(EVENT_SETTINGS_SOURCE_POPULAR_TODAY, new Bundle());
    }
}

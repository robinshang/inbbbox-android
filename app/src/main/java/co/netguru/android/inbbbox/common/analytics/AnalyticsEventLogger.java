package co.netguru.android.inbbbox.common.analytics;

public interface AnalyticsEventLogger {

    void logEventScreenBuckets();

    void logEventScreenFollowees();

    void logEventScreenLikes();

    void logEventScreenLogin();

    void logEventScreenSettings();

    void logEventScreenShotBuckets();

    void logEventScreenShotDetails();

    void logEventScreenShots();

    void logEventLoginSuccess();

    void logEventLoginFail();

    void logEventLoginGuest();

    void logEventUserLike();

    void logEventUserAddToBucket();

    void logEventUserComment();

    void logEventUserSwipeDown();

    void logEventUserFollow();

    void logEventApiRequestsRemaining(int requestsRemaining);

    void logEventSettingsAuthorOnHome();

    void logEventSettingsReminder();

    void logEventSettingsNightmode();

    void logEventSettingsNightmodeAuto();

    void logEventSettingsDebuts();

    void logEventSettingsFollowing();

    void logEventSettingsNewToday();

    void logEventSettingsPopularToday();
}

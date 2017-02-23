package co.netguru.android.inbbbox.common.analytics;

public interface AnalyticsEventLogger {

    /**
     * Screens
     */

    void logEventScreenBuckets();

    void logEventScreenFollowing();

    void logEventScreenLikes();

    void logEventScreenLogin();

    void logEventScreenSettings();

    void logEventScreenBucketDetails();

    void logEventScreenShotDetails();

    void logEventScreenShots();

    void logEventScreenUserDetails();

    /**
     * Dialogs
     */

    void logEventDialogShotToBuckets();

    void logEventDialogCreateBucket();

    /**
     * Login events
     */

    void logEventLoginSuccess();

    void logEventLoginFail();

    void logEventLoginGuest();

    void logEventCreateAccountAsGuest();

    /**
     * User interaction
     */

    void logEventUserSwipeDown();

    void logEventShotsFABComment();

    void logEventShotsFABAddToBucket();

    void logEventShotsFABFollow();

    void logEventShotsFABLike();

    void logEventBucketsFABCreate();

    void logEventAppbarList();

    void logEventAppbarGrid();

    void logEventAppbarFollow(boolean follow);

    void logEventShotSwipeLike();

    void logEventShotSwipeAddToBucket();

    void logEventShotSwipeComment();

    void logEventShotSwipeFollow();

    void logEventShotsListSwipes(int numberOfSwipes);

    void logEventShotDetailsLike();

    void logEventShotDetailsAddToBucket();

    void logEventShotDetailsComment();

    void logEventShotDetailsAuthorDetails();

    void logEventShotDetailsCollapseToolbar();

    void logEventShotDetailsCloseBack();

    void logEventShotDetailsCloseX();

    void logEventShotDetailsCloseSwipe();

    void logEventLikesItemClick();

    void logEventBucketsItemClick();

    void logEventFollowingItemClick();

    /**
     * API
     */

    void logEventApiRequestsRemaining(int requestsRemaining);

    void logEventApiFollow();

    void logEventApiLike();

    void logEventApiAddToBucket();

    void logEventApiComment();

    void logEventApiOther();

    /**
     * Settings
     */

    void logEventSettingsDetailsOnHome(boolean enabled);

    void logEventSettingsReminder(boolean enabled);

    void logEventSettingsNightmode(boolean enabled);

    void logEventSettingsDebuts(boolean enabled);

    void logEventSettingsFollowing(boolean enabled);

    void logEventSettingsNewToday(boolean enabled);

    void logEventSettingsPopularToday(boolean enabled);
}

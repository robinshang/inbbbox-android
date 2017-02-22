package co.netguru.android.inbbbox.common.analytics;

import com.google.firebase.analytics.FirebaseAnalytics;

import co.netguru.android.inbbbox.common.analytics.event.ScreenViewEvent;
import co.netguru.android.inbbbox.common.analytics.event.SourceStreamEvent;
import co.netguru.android.inbbbox.common.analytics.event.SourceStreamLeaveEvent;

class FirebaseAnalyticsEventLogger implements AnalyticsEventLogger {

    private static final String SCREEN_SHOTS = "shots";
    private static final String SCREEN_LIKES = "likes";
    private static final String SCREEN_BUCKETS = "buckets";
    private static final String SCREEN_FOLLOWING = "following";
    private static final String SCREEN_LOGIN = "login";
    private static final String SCREEN_SHOT_DETAILS = "shot details";
    private static final String SCREEN_USER_DETAILS = "user details";
    private static final String SCREEN_TEAM_DETAILS = "team details";
    private static final String SCREEN_BUCKET_DETAILS = "bucket details";
    private static final String SCREEN_SETTINGS_DRAWER = "settings drawer";

    private static final String SOURCE_FOLLOWING = "following";
    private static final String SOURCE_NEW_TODAY = "new today";
    private static final String SOURCE_POPULAR_TODAY = "popular today";
    private static final String SOURCE_DEBUT = "debut";

    private final FirebaseAnalytics analytics;

    FirebaseAnalyticsEventLogger(FirebaseAnalytics analytics) {
        this.analytics = analytics;
    }

    /**
     * Screens
     */

    @Override
    public void logEventScreenBuckets() {
        new ScreenViewEvent(SCREEN_BUCKETS).logEvent(analytics);
    }

    @Override
    public void logEventScreenFollowing() {
        new ScreenViewEvent(SCREEN_FOLLOWING).logEvent(analytics);
    }

    @Override
    public void logEventScreenLikes() {
        new ScreenViewEvent(SCREEN_LIKES).logEvent(analytics);
    }

    @Override
    public void logEventScreenLogin() {
        new ScreenViewEvent(SCREEN_LOGIN).logEvent(analytics);
    }

    @Override
    public void logEventScreenSettings() {
        new ScreenViewEvent(SCREEN_SETTINGS_DRAWER).logEvent(analytics);
    }

    @Override
    public void logEventScreenBucketDetails() {
        new ScreenViewEvent(SCREEN_BUCKET_DETAILS).logEvent(analytics);
    }

    @Override
    public void logEventScreenShotDetails() {
        new ScreenViewEvent(SCREEN_SHOT_DETAILS).logEvent(analytics);
    }

    @Override
    public void logEventScreenShots() {
        new ScreenViewEvent(SCREEN_SHOTS).logEvent(analytics);
    }

    @Override
    public void logEventScreenUserDetails() {
        new ScreenViewEvent(SCREEN_USER_DETAILS).logEvent(analytics);
    }

    /**
     * Dialogs
     */

    @Override
    public void logEventDialogShotToBuckets() {

    }

    @Override
    public void logEventDialogCreateBucket() {

    }

    /**
     * Sign up and login
     */

    @Override
    public void logEventLoginSuccess() {

    }

    @Override
    public void logEventLoginFail() {

    }

    @Override
    public void logEventLoginGuest() {

    }

    @Override
    public void logEventCreateAccountAsGuest() {

    }

    /**
     * User interaction
     */

    @Override
    public void logEventUserSwipeDown() {

    }

    @Override
    public void logEventShotsFABComment() {

    }

    @Override
    public void logEventShotsFABAddToBucket() {

    }

    @Override
    public void logEventShotsFABFollow() {

    }

    @Override
    public void logEventShotsFABLike() {

    }

    @Override
    public void logEventBucketsFABCreate() {

    }

    @Override
    public void logEventAppbarList() {

    }

    @Override
    public void logEventAppbarGrid() {

    }

    @Override
    public void logEventAppbarFollow(boolean follow) {

    }

    @Override
    public void logEventShotSwipeLike() {

    }

    @Override
    public void logEventShotSwipeAddToBucket() {

    }

    @Override
    public void logEventShotSwipeComment() {

    }

    @Override
    public void logEventShotSwipeFollow() {

    }

    @Override
    public void logEventShotsListSwipes(int numberOfSwipes) {

    }

    @Override
    public void logEventShotDetailsLike() {

    }

    @Override
    public void logEventShotDetailsAddToBucket() {

    }

    @Override
    public void logEventShotDetailsComment() {

    }

    @Override
    public void logEventShotDetailsAuthorDetails() {

    }

    @Override
    public void logEventShotDetailsCollapseToolbar() {

    }

    @Override
    public void logEventShotDetailsCloseBack() {

    }

    @Override
    public void logEventShotDetailsCloseX() {

    }

    @Override
    public void logEventShotDetailsCloseSwipe() {

    }

    @Override
    public void logEventLikesItemClick() {

    }

    @Override
    public void logEventBucketsItemClick() {

    }

    @Override
    public void logEventFollowingItemClick() {

    }

    /**
     * API requests
     */

    @Override
    public void logEventApiRequestsRemaining(int requestsRemaining) {

    }

    @Override
    public void logEventApiFollow() {

    }

    @Override
    public void logEventApiLike() {

    }

    @Override
    public void logEventApiAddToBucket() {

    }

    @Override
    public void logEventApiComment() {

    }

    /**
     * Settings
     */

    @Override
    public void logEventSettingsDetailsOnHome(boolean enabled) {

    }

    @Override
    public void logEventSettingsReminder(boolean enabled) {

    }

    @Override
    public void logEventSettingsNightmode(boolean enabled) {

    }

    @Override
    public void logEventSettingsDebuts(boolean enabled) {
        logSourceStreamEvent(enabled, SOURCE_DEBUT);
    }

    @Override
    public void logEventSettingsFollowing(boolean enabled) {
        logSourceStreamEvent(enabled, SOURCE_FOLLOWING);
    }

    @Override
    public void logEventSettingsNewToday(boolean enabled) {
        logSourceStreamEvent(enabled, SOURCE_NEW_TODAY);
    }

    @Override
    public void logEventSettingsPopularToday(boolean enabled) {
        logSourceStreamEvent(enabled, SOURCE_POPULAR_TODAY);
    }

    private void logSourceStreamEvent(boolean enabled, String groupId) {
        (enabled ? new SourceStreamEvent(groupId) :
                new SourceStreamLeaveEvent(groupId))
                .logEvent(analytics);
    }
}

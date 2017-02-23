package co.netguru.android.inbbbox.common.analytics;

import com.google.firebase.analytics.FirebaseAnalytics;

import co.netguru.android.inbbbox.common.analytics.event.BaseEvent;
import co.netguru.android.inbbbox.common.analytics.event.LocalSettingEvent;
import co.netguru.android.inbbbox.common.analytics.event.LoginEvent;
import co.netguru.android.inbbbox.common.analytics.event.RemainingRequestsEvent;
import co.netguru.android.inbbbox.common.analytics.event.RequestEvent;
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
    private static final String SCREEN_DIALOG_SHOT_TO_BUCKETS = "dialog: add shot to buckets";
    private static final String SCREEN_DIALOG_CREATE_BUCKET = "dialog: create bucket";

    private static final String SOURCE_FOLLOWING = "following";
    private static final String SOURCE_NEW_TODAY = "new today";
    private static final String SOURCE_POPULAR_TODAY = "popular today";
    private static final String SOURCE_DEBUT = "debut";

    private static final String SETTING_REMINDER = "reminder";
    private static final String SETTING_NIGHT_MODE = "night mode";
    private static final String SETTING_DETAILS_ON_HOME = "details on home";

    private static final String REQUEST_LIKE = "like";
    private static final String REQUEST_ADD_TO_BUCKET = "add to bucket";
    private static final String REQUEST_COMMENT = "comment";
    private static final String REQUEST_FOLLOW = "follow";

    private static final String LOGIN_SUCCESS = "login success";
    private static final String LOGIN_FAILURE = "login failure";
    private static final String LOGIN_GUEST = "login guest";
    private static final String LOGIN_CREATE_ACCOUNT_AS_GUEST = "create account as guest";

    private final FirebaseAnalytics analytics;

    FirebaseAnalyticsEventLogger(FirebaseAnalytics analytics) {
        this.analytics = analytics;
    }

    /**
     * Screens
     */

    @Override
    public void logEventScreenBuckets() {
        logEvent(new ScreenViewEvent(SCREEN_BUCKETS));
    }

    @Override
    public void logEventScreenFollowing() {
        logEvent(new ScreenViewEvent(SCREEN_FOLLOWING));
    }

    @Override
    public void logEventScreenLikes() {
        logEvent(new ScreenViewEvent(SCREEN_LIKES));
    }

    @Override
    public void logEventScreenLogin() {
        logEvent(new ScreenViewEvent(SCREEN_LOGIN));
    }

    @Override
    public void logEventScreenSettings() {
        logEvent(new ScreenViewEvent(SCREEN_SETTINGS_DRAWER));
    }

    @Override
    public void logEventScreenBucketDetails() {
        logEvent(new ScreenViewEvent(SCREEN_BUCKET_DETAILS));
    }

    @Override
    public void logEventScreenShotDetails() {
        logEvent(new ScreenViewEvent(SCREEN_SHOT_DETAILS));
    }

    @Override
    public void logEventScreenShots() {
        logEvent(new ScreenViewEvent(SCREEN_SHOTS));
    }

    @Override
    public void logEventScreenUserDetails() {
        logEvent(new ScreenViewEvent(SCREEN_USER_DETAILS));
    }

    /**
     * Dialogs
     */

    @Override
    public void logEventDialogShotToBuckets() {
        logEvent(new ScreenViewEvent(SCREEN_DIALOG_SHOT_TO_BUCKETS));
    }

    @Override
    public void logEventDialogCreateBucket() {
        logEvent(new ScreenViewEvent(SCREEN_DIALOG_CREATE_BUCKET));
    }

    /**
     * Sign up and login
     */

    @Override
    public void logEventLoginSuccess() {
        logEvent(new LoginEvent(LOGIN_SUCCESS));
    }

    @Override
    public void logEventLoginFail() {
        logEvent(new LoginEvent(LOGIN_FAILURE));
    }

    @Override
    public void logEventLoginGuest() {
        logEvent(new LoginEvent(LOGIN_GUEST));
    }

    @Override
    public void logEventCreateAccountAsGuest() {
        logEvent(new LoginEvent(LOGIN_CREATE_ACCOUNT_AS_GUEST));
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
        logEvent(new RemainingRequestsEvent(requestsRemaining));
    }

    @Override
    public void logEventApiFollow() {
        logEvent(new RequestEvent(REQUEST_FOLLOW));
    }

    @Override
    public void logEventApiLike() {
        logEvent(new RequestEvent(REQUEST_LIKE));
    }

    @Override
    public void logEventApiAddToBucket() {
        logEvent(new RequestEvent(REQUEST_ADD_TO_BUCKET));
    }

    @Override
    public void logEventApiComment() {
        logEvent(new RequestEvent(REQUEST_COMMENT));
    }

    /**
     * Settings
     */

    @Override
    public void logEventSettingsDetailsOnHome(boolean enabled) {
        logEvent(new LocalSettingEvent(SETTING_DETAILS_ON_HOME, enabled));
    }

    @Override
    public void logEventSettingsReminder(boolean enabled) {
        logEvent(new LocalSettingEvent(SETTING_REMINDER, enabled));
    }

    @Override
    public void logEventSettingsNightmode(boolean enabled) {
        logEvent(new LocalSettingEvent(SETTING_NIGHT_MODE, enabled));
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
        logEvent((enabled ? new SourceStreamEvent(groupId) :
                new SourceStreamLeaveEvent(groupId)));
    }

    private void logEvent(BaseEvent event) {
        event.logEvent(analytics);
    }
}

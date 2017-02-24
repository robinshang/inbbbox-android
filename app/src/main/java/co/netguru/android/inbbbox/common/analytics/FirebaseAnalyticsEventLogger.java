package co.netguru.android.inbbbox.common.analytics;

import com.google.firebase.analytics.FirebaseAnalytics;

import co.netguru.android.inbbbox.common.analytics.event.BaseEvent;
import co.netguru.android.inbbbox.common.analytics.event.LocalSettingEvent;
import co.netguru.android.inbbbox.common.analytics.event.LoginEvent;
import co.netguru.android.inbbbox.common.analytics.event.OnboardingEndEvent;
import co.netguru.android.inbbbox.common.analytics.event.OnboardingStartEvent;
import co.netguru.android.inbbbox.common.analytics.event.RemainingRequestsEvent;
import co.netguru.android.inbbbox.common.analytics.event.RequestEvent;
import co.netguru.android.inbbbox.common.analytics.event.ScreenViewEvent;
import co.netguru.android.inbbbox.common.analytics.event.ShotsSwipeEvent;
import co.netguru.android.inbbbox.common.analytics.event.SourceStreamEvent;
import co.netguru.android.inbbbox.common.analytics.event.UserInteractionEvent;

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
    private static final String SCREEN_SHOT_FULLSCREEN = "shot fullscreen";

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
    private static final String REQUEST_OTHER = "other";

    private static final String LOGIN_SUCCESS = "login success";
    private static final String LOGIN_FAILURE = "login failure";
    private static final String LOGIN_GUEST = "login guest";
    private static final String LOGIN_CREATE_ACCOUNT_AS_GUEST = "create account as guest";

    private static final String BUTTON_SHOTS_FAB_ADD_TO_BUCKETS = "shots fab: add to bucket";
    private static final String BUTTON_SHOTS_FAB_LIKE = "shots fab: like";
    private static final String BUTTON_SHOTS_FAB_COMMENT = "shots fab: add to bucket";
    private static final String BUTTON_SHOTS_FAB_FOLLOW = "shots fab: follow";
    private static final String BUTTON_BUCKETS_FAB_CREATE = "buckets fab: create";
    private static final String BUTTON_APPBAR_GRID = "appbar: grid";
    private static final String BUTTON_APPBAR_LIST = "appbar: list";
    private static final String BUTTON_APPBAR_DELETE_BUCKET = "appbar: delete bucket";
    private static final String BUTTON_CLOSE_SHOT_DETAILS_X = "close shot details x";
    private static final String BUTTON_CLOSE_SHOT_DETAILS_BACK = "close shot details back";
    private static final String BUTTON_SHOT_DETAILS_LIKE = "shot details like";
    private static final String BUTTON_SHOT_DETAILS_COMMENT = "shot details comment";
    private static final String BUTTON_SHOT_DETAILS_BUCKET = "shot details bucket";
    private static final String BUTTON_SHOT_DETAILS_USER = "shot details user";
    private static final String BUTTON_SHOT_DETAILS_TEAM = "shot details team";

    private static final String LIST_ITEM_SHOTS = "list item: shots";
    private static final String LIST_ITEM_LIKES = "list item: likes";
    private static final String LIST_ITEM_BUCKETS = "list item: buckets";
    private static final String LIST_ITEM_FOLLOWING = "list item: following";

    private static final String SWIPE_SHOT_LIKE = "shot swipe: like";
    private static final String SWIPE_SHOT_BUCKET = "shot swipe: bucket";
    private static final String SWIPE_SHOT_COMMENT = "shot swipe: comment";
    private static final String SWIPE_SHOT_FOLLOW = "shot swipe: follow";

    private static final String SWIPE_FULLSCREEN = "fullscreen swipe";
    private static final String SWIPE_SHOT_LIST = "shot list swipe";

    private static final String SHOT_DETAILS_COLLAPSED = "shot details collapsed";

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

    @Override
    public void logEventScreenTeamDetails() {
        logEvent(new ScreenViewEvent(SCREEN_TEAM_DETAILS));
    }

    @Override
    public void logEventScreenShotFullscreen() {
        logEvent(new ScreenViewEvent(SCREEN_SHOT_FULLSCREEN));
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
    public void logEventShotsFABComment() {
        logEvent(new UserInteractionEvent(BUTTON_SHOTS_FAB_COMMENT));
    }

    @Override
    public void logEventShotsFABAddToBucket() {
        logEvent(new UserInteractionEvent(BUTTON_SHOTS_FAB_ADD_TO_BUCKETS));
    }

    @Override
    public void logEventShotsFABFollow() {
        logEvent(new UserInteractionEvent(BUTTON_SHOTS_FAB_FOLLOW));
    }

    @Override
    public void logEventShotsFABLike() {
        logEvent(new UserInteractionEvent(BUTTON_SHOTS_FAB_LIKE));
    }

    @Override
    public void logEventBucketsFABCreate() {
        logEvent(new UserInteractionEvent(BUTTON_BUCKETS_FAB_CREATE));
    }

    @Override
    public void logEventAppbarCollectionLayoutChange(boolean isGrid) {
        if (isGrid)
            logEventAppbarGrid();
        else
            logEventAppbarList();
    }

    @Override
    public void logEventAppbarList() {
        logEvent(new UserInteractionEvent(BUTTON_APPBAR_LIST));
    }

    @Override
    public void logEventAppbarGrid() {
        logEvent(new UserInteractionEvent(BUTTON_APPBAR_GRID));
    }

    @Override
    public void logEventAppbarDeleteBucket() {
        logEvent(new UserInteractionEvent(BUTTON_APPBAR_DELETE_BUCKET));
    }

    @Override
    public void logEventShotSwipeLike() {
        logEvent(new UserInteractionEvent(SWIPE_SHOT_LIKE));
    }

    @Override
    public void logEventShotSwipeAddToBucket() {
        logEvent(new UserInteractionEvent(SWIPE_SHOT_BUCKET));
    }

    @Override
    public void logEventShotSwipeComment() {
        logEvent(new UserInteractionEvent(SWIPE_SHOT_COMMENT));
    }

    @Override
    public void logEventShotSwipeFollow() {
        logEvent(new UserInteractionEvent(SWIPE_SHOT_FOLLOW));
    }

    @Override
    public void logEventShotsListSwipes(int numberOfSwipes) {
        logEvent(new ShotsSwipeEvent(SWIPE_SHOT_LIST, numberOfSwipes));
    }

    @Override
    public void logEventShotDetailsLike() {
        logEvent(new UserInteractionEvent(BUTTON_SHOT_DETAILS_LIKE));
    }

    @Override
    public void logEventShotDetailsAddToBucket() {
        logEvent(new UserInteractionEvent(BUTTON_SHOT_DETAILS_BUCKET));
    }

    @Override
    public void logEventShotDetailsComment() {
        logEvent(new UserInteractionEvent(BUTTON_SHOT_DETAILS_COMMENT));
    }

    @Override
    public void logEventShotDetailsAuthorDetails() {
        logEvent(new UserInteractionEvent(BUTTON_SHOT_DETAILS_USER));
    }

    @Override
    public void logEventShotDetailsTeamDetails() {
        logEvent(new UserInteractionEvent(BUTTON_SHOT_DETAILS_TEAM));
    }

    @Override
    public void logEventShotDetailsCloseBack() {
        logEvent(new UserInteractionEvent(BUTTON_CLOSE_SHOT_DETAILS_BACK));
    }

    @Override
    public void logEventShotDetailsCloseX() {
        logEvent(new UserInteractionEvent(BUTTON_CLOSE_SHOT_DETAILS_X));
    }

    @Override
    public void logEventShotDetailsViewCollapsed() {
        logEvent(new UserInteractionEvent(SHOT_DETAILS_COLLAPSED));
    }

    @Override
    public void logEventShotsItemClick() {
        logEvent(new UserInteractionEvent(LIST_ITEM_SHOTS));
    }

    @Override
    public void logEventLikesItemClick() {
        logEvent(new UserInteractionEvent(LIST_ITEM_LIKES));
    }

    @Override
    public void logEventBucketsItemClick() {
        logEvent(new UserInteractionEvent(LIST_ITEM_BUCKETS));
    }

    @Override
    public void logEventFollowingItemClick() {
        logEvent(new UserInteractionEvent(LIST_ITEM_FOLLOWING));
    }

    @Override
    public void logEventFullscreenSwipe(int numberOfSwipes) {
        logEvent(new ShotsSwipeEvent(SWIPE_FULLSCREEN, numberOfSwipes));
    }

    /**
     * API requests
     */

    @Override
    public void logEventApiRequestsRemaining(int requestsRemaining) {
        logEvent(new RemainingRequestsEvent(requestsRemaining));
    }

    @Override
    public void logEventApiFollow(int requestsRemaining) {
        logEvent(new RequestEvent(REQUEST_FOLLOW, requestsRemaining));
    }

    @Override
    public void logEventApiLike(int requestsRemaining) {
        logEvent(new RequestEvent(REQUEST_LIKE, requestsRemaining));
    }

    @Override
    public void logEventApiAddToBucket(int requestsRemaining) {
        logEvent(new RequestEvent(REQUEST_ADD_TO_BUCKET, requestsRemaining));
    }

    @Override
    public void logEventApiComment(int requestsRemaining) {
        logEvent(new RequestEvent(REQUEST_COMMENT, requestsRemaining));
    }

    @Override
    public void logEventApiOther(int requestsRemaining) {
        logEvent(new RequestEvent(REQUEST_OTHER, requestsRemaining));
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
        logEvent(new SourceStreamEvent(SOURCE_DEBUT, enabled));
    }

    @Override
    public void logEventSettingsFollowing(boolean enabled) {
        logEvent(new SourceStreamEvent(SOURCE_FOLLOWING, enabled));
    }

    @Override
    public void logEventSettingsNewToday(boolean enabled) {
        logEvent(new SourceStreamEvent(SOURCE_NEW_TODAY, enabled));
    }

    @Override
    public void logEventSettingsPopularToday(boolean enabled) {
        logEvent(new SourceStreamEvent(SOURCE_POPULAR_TODAY, enabled));
    }

    /**
     * Onboarding
     */

    @Override
    public void logEventOnboardingStart() {
        logEvent(new OnboardingStartEvent());
    }

    @Override
    public void logEventOnboardingEnd() {
        logEvent(new OnboardingEndEvent());
    }

    private void logEvent(BaseEvent event) {
        event.logEvent(analytics);
    }
}

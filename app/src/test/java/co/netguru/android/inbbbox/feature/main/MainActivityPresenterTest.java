package co.netguru.android.inbbbox.feature.main;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.session.controllers.LogoutController;
import co.netguru.android.inbbbox.data.settings.SettingsController;
import co.netguru.android.inbbbox.data.session.controllers.TokenParametersController;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserController;
import co.netguru.android.inbbbox.notification.NotificationController;
import co.netguru.android.inbbbox.notification.NotificationScheduler;
import co.netguru.android.inbbbox.data.settings.model.NotificationSettings;
import co.netguru.android.inbbbox.data.settings.model.Settings;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;
import rx.Single;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainActivityPresenterTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    public UserController userControllerMock;

    @Mock
    public NotificationScheduler notificationSchedulerMock;

    @Mock
    public NotificationController notificationControllerMock;

    @Mock
    public SettingsController settingsControllerMock;

    @Mock
    public TokenParametersController tokenParametersControllerMock;

    @Mock
    public LogoutController logoutControllerMock;

    @Mock
    public MainViewContract.View viewMock;

    @Mock
    public User userMock;

    @Mock
    public Settings settingsMock;

    @Mock
    public NotificationSettings notificationSettingsMock;

    @InjectMocks
    MainActivityPresenter mainActivityPresenter;

    @Before
    public void setUp() {
        mainActivityPresenter.attachView(viewMock);
        when(userControllerMock.getUserFromCache())
                .thenReturn(Single.just(userMock));
        when(settingsMock.getNotificationSettings())
                .thenReturn(notificationSettingsMock);
        when(notificationSettingsMock.isEnabled()).thenReturn(true);
        when(settingsControllerMock.getSettings())
                .thenReturn(Single.just(settingsMock));
        when(settingsControllerMock.getNotificationSettings())
                .thenReturn(Single.just(notificationSettingsMock));
        when(logoutControllerMock.performLogout()).thenReturn(Completable.complete());

        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(false));
    }

    @Test
    public void whenPrepareUserData_thenGetGuestModeState() {
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(true));

        mainActivityPresenter.prepareUserData();

        verify(userControllerMock, times(1)).isGuestModeEnabled();
    }

    @Test
    public void whenGuestModeEnabled_thenShowCreateAccountButton() {
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(true));

        mainActivityPresenter.prepareUserData();

        verify(viewMock, times(1)).showCreateAccountButton();
    }

    @Test
    public void whenGuestModeEnabled_thenDoNotRequestUserData() {
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(true));

        mainActivityPresenter.prepareUserData();

        verify(userControllerMock, never()).getUserFromCache();
    }

    @Test
    public void whenPrepareUserData_thenRequestSettings() {
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(false));

        mainActivityPresenter.prepareUserData();

        verify(settingsControllerMock, times(1)).getSettings();
    }

    @Test
    public void whenGuestModeDisabled_thenRequestUserData() {
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(false));

        mainActivityPresenter.prepareUserData();

        verify(userControllerMock, times(1)).getUserFromCache();
    }

    @Test
    public void whenCreateAccountClicked_thenGetCreateAccountUrl() {
        String url = "url";
        when(tokenParametersControllerMock.getSignUpUrl()).thenReturn(Single.just(url));

        mainActivityPresenter.onCreateAccountClick();

        verify(tokenParametersControllerMock, times(1)).getSignUpUrl();
    }

    @Test
    public void whenCreateAccountClickAndUrlRetrieved_thenOpenSignUpUrlWebPage() {
        String url = "url";
        when(tokenParametersControllerMock.getSignUpUrl()).thenReturn(Single.just(url));

        mainActivityPresenter.onCreateAccountClick();

        verify(viewMock, times(1)).openSignUpPage(url);
    }

    @Test
    public void whenLogoutClicked_thenPreformLogoutActionOnLogoutController() {

        mainActivityPresenter.performLogout();

        verify(logoutControllerMock, times(1)).performLogout();
    }

    @Test
    public void whenLogoutComplete_thenShowLoginView() {

        mainActivityPresenter.performLogout();

        verify(viewMock, times(1)).showLoginActivity();
    }

    @Test
    public void whenToggleStatusChangedToTrue_thenShowLogoutMenu() {

        mainActivityPresenter.toggleButtonChanged(true);

        verify(viewMock, times(1)).showLogoutMenu();
    }

    @Test
    public void whenToggleStatusChangedToFalse_thenShowMainMenu() {

        mainActivityPresenter.toggleButtonChanged(false);

        verify(viewMock, times(1)).showMainMenu();
    }

    @Test
    public void whenToggleStatusChangedToTrueAndUserIsNotNull_thenShowUserName() {
        mainActivityPresenter.prepareUserData();

        mainActivityPresenter.toggleButtonChanged(true);

        verify(viewMock, atLeastOnce()).showUserName(anyString());
    }

    @Test
    public void whenToggleStatusChangedToFalseAndUserIsNotNull_thenShowUserName() {
        mainActivityPresenter.prepareUserData();

        mainActivityPresenter.toggleButtonChanged(false);

        verify(viewMock, atLeastOnce()).showUserName(anyString());
    }

    @Test
    public void whenNotificationEnabled_thenSaveNotificationSettings() {
        Boolean notificationState = false;
        when(settingsControllerMock.changeNotificationStatus(anyBoolean()))
                .thenReturn(Completable.complete());
        when(notificationControllerMock.scheduleNotification())
                .thenReturn(Single.just(notificationSettingsMock));

        mainActivityPresenter.notificationStatusChanged(notificationState);

        verify(settingsControllerMock, times(1)).changeNotificationStatus(notificationState);
    }

    @Test
    public void whenNotificationDisabled_thenSaveNotificationSettings() {
        Boolean notificationState = false;
        when(settingsControllerMock.changeNotificationStatus(anyBoolean()))
                .thenReturn(Completable.complete());

        mainActivityPresenter.notificationStatusChanged(notificationState);

        verify(settingsControllerMock, times(1)).changeNotificationStatus(notificationState);
    }

    @Test
    public void whenNotificationDisabled_thenCancelNotification() {
        when(settingsControllerMock.changeNotificationStatus(anyBoolean()))
                .thenReturn(Completable.complete());

        mainActivityPresenter.notificationStatusChanged(false);

        verify(notificationSchedulerMock, times(1)).cancelNotification();
    }

    @Test
    public void whenNotificationEnabled_thenScheduleNotification() {
        when(settingsControllerMock.changeNotificationStatus(anyBoolean()))
                .thenReturn(Completable.complete());
        when(notificationControllerMock.scheduleNotification())
                .thenReturn(Single.just(notificationSettingsMock));

        mainActivityPresenter.notificationStatusChanged(true);

        verify(notificationControllerMock, times(1)).scheduleNotification();
    }

    @Test
    public void whenFollowingStatusEnabled_thenEnabledChangeIt() {
        Boolean status = true;
        when(settingsControllerMock
                .changeStreamSourceSettings(anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean()))
                .thenReturn(Completable.complete());

        mainActivityPresenter.followingStatusChanged(status);

        verify(settingsControllerMock, times(1))
                .changeStreamSourceSettings(eq(status), anyBoolean(), anyBoolean(), anyBoolean());
    }

    @Test
    public void whenNewStatusEnabled_thenEnabledChangeIt() {
        Boolean status = true;
        when(settingsControllerMock
                .changeStreamSourceSettings(anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean()))
                .thenReturn(Completable.complete());

        mainActivityPresenter.newStatusChanged(status);

        verify(settingsControllerMock, times(1))
                .changeStreamSourceSettings(anyBoolean(), eq(status), anyBoolean(), anyBoolean());
    }

    @Test
    public void whenPopularStatusEnabled_thenEnabledChangeIt() {
        Boolean status = true;
        when(settingsControllerMock
                .changeStreamSourceSettings(anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean()))
                .thenReturn(Completable.complete());

        mainActivityPresenter.popularStatusChanged(status);

        verify(settingsControllerMock, times(1))
                .changeStreamSourceSettings(anyBoolean(), anyBoolean(), eq(status), anyBoolean());
    }

    @Test
    public void whenDebutsStatusEnabled_thenEnabledChangeIt() {
        Boolean status = true;
        when(settingsControllerMock
                .changeStreamSourceSettings(anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean()))
                .thenReturn(Completable.complete());

        mainActivityPresenter.debutsStatusChanged(status);

        verify(settingsControllerMock, times(1))
                .changeStreamSourceSettings(anyBoolean(), anyBoolean(), anyBoolean(), eq(status));
    }

    @Test
    public void whenAllStreamsSourceStatusAreDisabled_thenShowAtLestOneMustBeEnabledInfo() {
        when(settingsControllerMock
                .changeStreamSourceSettings(anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean()))
                .thenReturn(Completable.complete());

        mainActivityPresenter.followingStatusChanged(false);
        mainActivityPresenter.newStatusChanged(false);
        mainActivityPresenter.popularStatusChanged(false);
        mainActivityPresenter.debutsStatusChanged(false);

        verify(viewMock, atLeastOnce()).showMessage(R.string.change_stream_source_error);
    }

    @After
    public void tearDown() {
        mainActivityPresenter.detachView(false);
    }
}
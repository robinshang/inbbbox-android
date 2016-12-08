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

import co.netguru.android.inbbbox.controler.LogoutController;
import co.netguru.android.inbbbox.controler.SettingsController;
import co.netguru.android.inbbbox.controler.TokenParametersController;
import co.netguru.android.inbbbox.controler.UserController;
import co.netguru.android.inbbbox.controler.notification.NotificationController;
import co.netguru.android.inbbbox.controler.notification.NotificationScheduler;
import co.netguru.android.inbbbox.model.localrepository.NotificationSettings;
import co.netguru.android.inbbbox.model.localrepository.Settings;
import co.netguru.android.inbbbox.model.ui.User;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;
import rx.Single;

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

    @After
    public void tearDown() {
        mainActivityPresenter.detachView(false);
    }
}
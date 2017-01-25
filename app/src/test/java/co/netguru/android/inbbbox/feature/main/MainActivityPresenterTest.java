package co.netguru.android.inbbbox.feature.main;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Random;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserController;
import co.netguru.android.inbbbox.data.session.controllers.LogoutController;
import co.netguru.android.inbbbox.data.session.controllers.TokenParametersController;
import co.netguru.android.inbbbox.data.settings.SettingsController;
import co.netguru.android.inbbbox.data.settings.model.CustomizationSettings;
import co.netguru.android.inbbbox.data.settings.model.NotificationSettings;
import co.netguru.android.inbbbox.data.settings.model.Settings;
import co.netguru.android.inbbbox.data.settings.model.StreamSourceSettings;
import co.netguru.android.inbbbox.feature.remindernotification.NotificationController;
import co.netguru.android.inbbbox.feature.remindernotification.NotificationScheduler;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Completable;
import rx.Single;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
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
    public StreamSourceSettings streamSourceSettings;

    @Mock
    public LogoutController logoutControllerMock;

    @Mock
    public MainViewContract.View viewMock;

    @Mock
    public User userMock;

    @Mock
    public Settings settingsMock;

    @Mock
    public ErrorController errorControllerMock;

    @Mock
    public NotificationSettings notificationSettingsMock;

    @InjectMocks
    MainActivityPresenter mainActivityPresenter;

    private Random random;

    @Before
    public void setUp() {
        random = new Random();
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
        when(errorControllerMock.getThrowableMessage(any(Throwable.class)))
                .thenCallRealMethod();
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

    @Test
    public void whenShowTimePickDialogClicked_thenShowPickerDialog() {

        mainActivityPresenter.timeViewClicked();

        verify(viewMock).showTimePickDialog(anyInt(), anyInt());
    }

    @Test
    public void whenShowTimePickDialogClickedAndGettingTimerSettingsFail_thenShowError() {
        when(settingsControllerMock.getNotificationSettings())
                .thenReturn(Single.error(new Throwable()));

        mainActivityPresenter.timeViewClicked();

        verify(viewMock).showMessage(anyInt());
    }

    @Test
    public void whenCustomizationStatusChanged_thenChangeShotDetailsStatusOnSettingsController() {
        when(settingsControllerMock.changeShotsDetailsStatus(anyBoolean()))
                .thenReturn(Completable.complete());
        boolean state = random.nextBoolean();

        mainActivityPresenter.customizationStatusChanged(state);

        verify(settingsControllerMock).changeShotsDetailsStatus(state);
    }

    @Test
    public void whenCustomizationStatusChangedAndSavingSettingsFail_thenShowError() {
        when(settingsControllerMock.changeShotsDetailsStatus(anyBoolean()))
                .thenReturn(Completable.error(new Throwable()));
        boolean state = random.nextBoolean();

        mainActivityPresenter.customizationStatusChanged(state);

        verify(viewMock).showMessageOnServerError(anyString());
    }

    @Test
    public void whenNightChanged_thenChangeModeInSettings() {
        when(settingsControllerMock.changeNightMode(anyBoolean()))
                .thenReturn(Completable.complete());
        boolean state = random.nextBoolean();

        mainActivityPresenter.nightModeChanged(state);

        verify(settingsControllerMock).changeNightMode(state);
    }

    @Test
    public void whenNightChangedAndSavingItComplete_thenChangeViewNightModeState() {
        when(settingsControllerMock.changeNightMode(anyBoolean()))
                .thenReturn(Completable.complete());
        boolean state = random.nextBoolean();

        mainActivityPresenter.nightModeChanged(state);

        verify(viewMock).changeNightMode(state);
    }

    @Test
    public void whenNightChangedAndSavingItFail_thenShowError() {
        when(settingsControllerMock.changeNightMode(anyBoolean()))
                .thenReturn(Completable.error(new Throwable()));
        boolean state = random.nextBoolean();

        mainActivityPresenter.nightModeChanged(state);

        verify(viewMock).showMessageOnServerError(anyString());
    }

    @Test
    public void whenTimePicked_thenSaveNewTimeInNotificationSettingsAndScheduleNotificaiton() {
        when(settingsControllerMock.changeNotificationTime(anyInt(), anyInt()))
                .thenReturn(Completable.complete());
        when(notificationControllerMock.scheduleNotification())
                .thenReturn(Single.just(notificationSettingsMock));
        int h = random.nextInt(24);
        int min = random.nextInt(59);

        mainActivityPresenter.onTimePicked(h, min);

        verify(settingsControllerMock).changeNotificationTime(h, min);
        verify(notificationControllerMock).scheduleNotification();
    }

    @Test
    public void whenTimePickedAndSavedAndScheduled_thenShowNotificationTimeInView() {
        when(settingsControllerMock.changeNotificationTime(anyInt(), anyInt()))
                .thenReturn(Completable.complete());
        when(notificationControllerMock.scheduleNotification())
                .thenReturn(Single.just(notificationSettingsMock));
        int h = random.nextInt(24);
        int min = random.nextInt(59);

        mainActivityPresenter.onTimePicked(h, min);

        verify(viewMock).showNotificationTime(anyString());
    }

    @Test
    public void whenPrepareUserSettingsCalled_thenSetNotificationViewSettings() {
        NotificationSettings notificationSettingsMock = mock(NotificationSettings.class);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Integer hour = random.nextInt(12);
        Integer min = random.nextInt(59);
        boolean isFollowing = random.nextBoolean();
        boolean isNew = random.nextBoolean();
        boolean isPopular = random.nextBoolean();
        boolean isDebut = random.nextBoolean();
        setupStreamSourceStatesMocks(isFollowing, isNew, isPopular, isDebut);
        when(settingsMock.getNotificationSettings()).thenReturn(notificationSettingsMock);
        when(notificationSettingsMock.isEnabled()).thenReturn(false);
        when(notificationSettingsMock.getHour()).thenReturn(hour);
        when(notificationSettingsMock.getMinute()).thenReturn(min);

        mainActivityPresenter.prepareUserData();

        verify(viewMock).changeNotificationStatus(false);
        verify(viewMock).showNotificationTime(anyString());
    }

    @Test
    public void whenPrepareUserSettingsCalled_thenSetStreamsSettingsInView() {
        boolean isFollowing = random.nextBoolean();
        boolean isNew = random.nextBoolean();
        boolean isPopular = random.nextBoolean();
        boolean isDebut = random.nextBoolean();
        setupStreamSourceStatesMocks(isFollowing, isNew, isPopular, isDebut);
        CustomizationSettings customizationSettings = mock(CustomizationSettings.class);
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(false));
        when(settingsControllerMock.getStreamSourceSettings())
                .thenReturn(Single.just(streamSourceSettings));
        when(settingsControllerMock.getSettings()).thenReturn(Single.just(settingsMock));
        when(settingsMock.getNotificationSettings()).thenReturn(notificationSettingsMock);
        when(notificationSettingsMock.isEnabled()).thenReturn(false);
        when(customizationSettings.isNightMode()).thenReturn(random.nextBoolean());
        when(customizationSettings.isShowDetails()).thenReturn(random.nextBoolean());
        when(settingsMock.getCustomizationSettings()).thenReturn(customizationSettings);

        mainActivityPresenter.prepareUserData();

        verify(viewMock).changeFollowingStatus(isFollowing);
        verify(viewMock).changeNewStatus(isNew);
        verify(viewMock).changePopularStatus(isPopular);
        verify(viewMock).changeDebutsStatus(isDebut);
    }

    @Test
    public void whenPrepareUserSettingsCalled_thenSetupCustomizationSettings() {
        CustomizationSettings customizationSettings = mock(CustomizationSettings.class);
        boolean customizationStatus = random.nextBoolean();
        boolean nightModeStatus = random.nextBoolean();
        boolean exampleState = random.nextBoolean();
        setupStreamSourceStatesMocks(exampleState, exampleState, exampleState, exampleState);
        when(userControllerMock.isGuestModeEnabled()).thenReturn(Single.just(false));
        when(settingsControllerMock.getStreamSourceSettings())
                .thenReturn(Single.just(streamSourceSettings));
        when(settingsControllerMock.getSettings()).thenReturn(Single.just(settingsMock));
        when(settingsMock.getNotificationSettings()).thenReturn(notificationSettingsMock);
        when(notificationSettingsMock.isEnabled()).thenReturn(false);
        when(customizationSettings.isNightMode()).thenReturn(nightModeStatus);
        when(customizationSettings.isShowDetails()).thenReturn(customizationStatus);
        when(settingsMock.getCustomizationSettings()).thenReturn(customizationSettings);

        mainActivityPresenter.prepareUserData();

        verify(viewMock).changeCustomizationStatus(customizationStatus);
        verify(viewMock).setNightModeStatus(nightModeStatus);
    }

    @Test
    public void whenPrepareUserSettingsCalled_thenSetSettingsListener() {
        CustomizationSettings customizationSettings = mock(CustomizationSettings.class);
        boolean customizationStatus = random.nextBoolean();
        boolean nightModeStatus = random.nextBoolean();
        boolean exampleState = random.nextBoolean();
        setupStreamSourceStatesMocks(exampleState, exampleState, exampleState, exampleState);
        when(settingsControllerMock.getStreamSourceSettings())
                .thenReturn(Single.just(streamSourceSettings));
        when(settingsControllerMock.getSettings()).thenReturn(Single.just(settingsMock));
        when(settingsMock.getNotificationSettings()).thenReturn(notificationSettingsMock);
        when(notificationSettingsMock.isEnabled()).thenReturn(false);
        when(customizationSettings.isNightMode()).thenReturn(nightModeStatus);
        when(customizationSettings.isShowDetails()).thenReturn(customizationStatus);
        when(settingsMock.getCustomizationSettings()).thenReturn(customizationSettings);

        mainActivityPresenter.prepareUserData();

        verify(viewMock).setSettingsListeners();
    }

    @After
    public void tearDown() {
        mainActivityPresenter.detachView(false);
    }

    private void setupStreamSourceStatesMocks(boolean following,
                                              boolean newToday,
                                              boolean popularToday,
                                              boolean debut) {

        when(streamSourceSettings.isFollowing()).thenReturn(following);
        when(streamSourceSettings.isNewToday()).thenReturn(newToday);
        when(streamSourceSettings.isPopularToday()).thenReturn(popularToday);
        when(streamSourceSettings.isDebut()).thenReturn(debut);
        when(settingsMock.getStreamSourceSettings()).thenReturn(streamSourceSettings);
    }
}
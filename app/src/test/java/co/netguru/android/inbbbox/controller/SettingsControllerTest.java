package co.netguru.android.inbbbox.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.localrepository.SettingsPrefsRepository;
import co.netguru.android.inbbbox.model.localrepository.CustomizationSettings;
import co.netguru.android.inbbbox.model.localrepository.NotificationSettings;
import co.netguru.android.inbbbox.model.localrepository.Settings;
import co.netguru.android.inbbbox.model.localrepository.StreamSourceSettings;
import rx.Completable;
import rx.Single;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SettingsControllerTest {

    @Mock
    SettingsPrefsRepository settingsPrefsRepository;
    @InjectMocks
    SettingsController settingsController;

    private NotificationSettings notificationSettings = new NotificationSettings();
    private StreamSourceSettings streamSourceSettings = new StreamSourceSettings();
    private CustomizationSettings customizationSettings = new CustomizationSettings();

    @Before
    public void setup() {
        when(settingsPrefsRepository.getNotificationsSettings()).thenReturn(Single.just(notificationSettings));
        when(settingsPrefsRepository.getStreamSourceSettings()).thenReturn(Single.just(streamSourceSettings));
        when(settingsPrefsRepository.getCustomizationSettings()).thenReturn(Single.just(customizationSettings));

        when(settingsPrefsRepository.saveDetailsShowed(anyBoolean())).thenReturn(Completable.complete());
        when(settingsPrefsRepository.saveNotificationSettings(anyObject())).thenReturn(Completable.complete());
        when(settingsPrefsRepository.saveStreamSourceSettingsToPrefs(anyObject())).thenReturn(Completable.complete());

    }

    @Test
    public void shouldGetSettingsFromPrefs() {
        //given
        TestSubscriber<Settings> testSubscriber = new TestSubscriber<>();
        //when
        settingsController.getSettings().subscribe(testSubscriber);
        //then
        verify(settingsPrefsRepository, times(1)).getCustomizationSettings();
        verify(settingsPrefsRepository, times(1)).getStreamSourceSettings();
        verify(settingsPrefsRepository, times(1)).getNotificationsSettings();

        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
    }

    @Test
    public void shouldGetNotificationSettingsFromPrefs() {
        //given
        TestSubscriber<NotificationSettings> testSubscriber = new TestSubscriber<>();
        //when
        settingsController.getNotificationSettings().subscribe(testSubscriber);
        //then
        verify(settingsPrefsRepository, times(1)).getNotificationsSettings();
        testSubscriber.assertValue(notificationSettings);
        testSubscriber.assertNoErrors();
    }

    @Test
    public void shouldGetStreamSourceSettingsFromPrefs() {
        //given
        TestSubscriber<StreamSourceSettings> testSubscriber = new TestSubscriber<>();
        //when
        settingsController.getStreamSourceSettings().subscribe(testSubscriber);
        //then
        verify(settingsPrefsRepository, times(1)).getStreamSourceSettings();
        testSubscriber.assertValue(streamSourceSettings);
        testSubscriber.assertNoErrors();
    }

    @Test
    public void shouldGetCustomizationSettingsFromPrefs() {
        //given
        TestSubscriber<CustomizationSettings> testSubscriber = new TestSubscriber<>();
        //when
        settingsController.getCustomizationSettings().subscribe(testSubscriber);
        //then
        verify(settingsPrefsRepository, times(1)).getCustomizationSettings();
        testSubscriber.assertValue(customizationSettings);
        testSubscriber.assertNoErrors();
    }
}
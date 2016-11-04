package co.netguru.android.inbbbox.feature.settings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.data.local.SettingsPrefsController;
import co.netguru.android.inbbbox.models.CustomizationSettings;
import co.netguru.android.inbbbox.models.NotificationSettings;
import co.netguru.android.inbbbox.models.Settings;
import co.netguru.android.inbbbox.models.StreamSourceSettings;
import rx.Completable;
import rx.Single;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SettingsManagerTest {

    @Mock
    SettingsPrefsController settingsPrefsController;
    @InjectMocks
    SettingsManager settingsManager;

    private NotificationSettings notificationSettings = new NotificationSettings();
    private StreamSourceSettings streamSourceSettings = new StreamSourceSettings();
    private CustomizationSettings customizationSettings = new CustomizationSettings();

    @Before
    public void setup() {
        when(settingsPrefsController.getNotificationsSettings()).thenReturn(Single.just(notificationSettings));
        when(settingsPrefsController.getStreamSourceSettings()).thenReturn(Single.just(streamSourceSettings));
        when(settingsPrefsController.getCustomizationSettings()).thenReturn(Single.just(customizationSettings));

        when(settingsPrefsController.saveDetailsShowed(anyBoolean())).thenReturn(Completable.complete());
        when(settingsPrefsController.saveNotificationSettings(anyObject())).thenReturn(Completable.complete());
        when(settingsPrefsController.saveStreamSourceSettingsToPrefs(anyObject())).thenReturn(Completable.complete());

    }

    @Test
    public void shouldGetSettingsFromPrefs() {
        //given
        TestSubscriber<Settings> testSubscriber = new TestSubscriber<>();
        //when
        settingsManager.getSettings().subscribe(testSubscriber);
        //then
        verify(settingsPrefsController, times(1)).getCustomizationSettings();
        verify(settingsPrefsController, times(1)).getStreamSourceSettings();
        verify(settingsPrefsController, times(1)).getNotificationsSettings();

        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
    }

    @Test
    public void shouldGetNotificationSettingsFromPrefs() {
        //given
        TestSubscriber<NotificationSettings> testSubscriber = new TestSubscriber<>();
        //when
        settingsManager.getNotificationSettings().subscribe(testSubscriber);
        //then
        verify(settingsPrefsController, times(1)).getNotificationsSettings();
        testSubscriber.assertValue(notificationSettings);
        testSubscriber.assertNoErrors();
    }

    @Test
    public void shouldGetStreamSourceSettingsFromPrefs() {
        //given
        TestSubscriber<StreamSourceSettings> testSubscriber = new TestSubscriber<>();
        //when
        settingsManager.getStreamSourceSettings().subscribe(testSubscriber);
        //then
        verify(settingsPrefsController, times(1)).getStreamSourceSettings();
        testSubscriber.assertValue(streamSourceSettings);
        testSubscriber.assertNoErrors();
    }

    @Test
    public void shouldGetCustomizationSettingsFromPrefs() {
        //given
        TestSubscriber<CustomizationSettings> testSubscriber = new TestSubscriber<>();
        //when
        settingsManager.getCustomizationSettings().subscribe(testSubscriber);
        //then
        verify(settingsPrefsController, times(1)).getCustomizationSettings();
        testSubscriber.assertValue(customizationSettings);
        testSubscriber.assertNoErrors();
    }
}
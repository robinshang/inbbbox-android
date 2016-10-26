package co.netguru.android.inbbbox.feature.settings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.data.models.NotificationSettings;
import co.netguru.android.inbbbox.data.models.Settings;
import co.netguru.android.inbbbox.db.datasource.DataSource;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SettingsManagerTest {

    @Mock
    DataSource<Settings> settingsDataSource;
    @InjectMocks
    SettingsManager settingsManager;

    private Settings settings = new Settings();
    private TestSubscriber<Boolean> booleanTestSubscriber;

    @Before
    public void setup() {
        when(settingsDataSource.get()).thenReturn(Observable.just(settings));
        when(settingsDataSource.save(anyObject())).thenReturn(Observable.just(true));
        booleanTestSubscriber = new TestSubscriber<>();
    }

    @Test
    public void shouldGetSettingsFromDatabaseWhenNoErrors() {
        //given
        TestSubscriber<Settings> testSubscriber = new TestSubscriber<>();
        //when
        settingsManager.getSettings().subscribe(testSubscriber);
        //then
        verify(settingsDataSource, times(1)).get();
        testSubscriber.assertValue(settings);
        testSubscriber.assertNoErrors();
    }

    @Test
    public void shouldReturnDefaultSettingsWhenDatabaseError() {
        //given
        when(settingsDataSource.get()).thenReturn(Observable.error(new Throwable()));
        TestSubscriber<Settings> testSubscriber = new TestSubscriber<>();
        //when
        settingsManager.getSettings().subscribe(testSubscriber);
        //then
        testSubscriber.assertValue(settingsManager.defaultSettings);
        testSubscriber.assertNoErrors();
    }

    @Test
    public void shouldGetNotificationSettingsFromDatabaseWhenNoErrors() {
        //given
        TestSubscriber<NotificationSettings> testSubscriber = new TestSubscriber<>();
        //when
        settingsManager.getNotificationSettings().subscribe(testSubscriber);
        //then
        verify(settingsDataSource, times(1)).get();
        testSubscriber.assertValue(settings.getNotificationSettings());
        testSubscriber.assertNoErrors();
    }

    @Test
    public void shouldReturnDefaultNotificationSettingsWhenDatabaseError() {
        //given
        when(settingsDataSource.get()).thenReturn(Observable.error(new Throwable()));
        TestSubscriber<NotificationSettings> testSubscriber = new TestSubscriber<>();
        //when
        settingsManager.getNotificationSettings().subscribe(testSubscriber);
        //then
        testSubscriber.assertValue(settingsManager.defaultSettings.getNotificationSettings());
        testSubscriber.assertNoErrors();
    }

    @Test
    public void shouldReturnTrueWhenSavingNotificationStatusCompleted() {
        //when
        settingsManager.changeNotificationStatus(false).subscribe(booleanTestSubscriber);
        //then
        verify(settingsDataSource, times(1)).save(anyObject());
        booleanTestSubscriber.assertValue(true);
        booleanTestSubscriber.assertNoErrors();
    }

    @Test
    public void shouldReturnFalseWhenSavingNotificationStatusFailed() {
        //given
        when(settingsDataSource.save(anyObject())).thenReturn(Observable.just(false));
        //when
        settingsManager.changeNotificationStatus(true).subscribe(booleanTestSubscriber);
        //then
        verify(settingsDataSource, times(1)).save(anyObject());
        booleanTestSubscriber.assertValue(false);
        booleanTestSubscriber.assertNoErrors();
    }

    @Test
    public void shouldReturnTrueWhenSavingNotificationTimeCompleted() {
        //when
        settingsManager.changeNotificationTime(1, 1).subscribe(booleanTestSubscriber);
        //then
        verify(settingsDataSource, times(1)).save(anyObject());
        booleanTestSubscriber.assertValue(true);
        booleanTestSubscriber.assertNoErrors();
    }

    @Test
    public void shouldReturnFalseWhenSavingNotificationTimeFailed() {
        //given
        when(settingsDataSource.save(anyObject())).thenReturn(Observable.just(false));
        //when
        settingsManager.changeNotificationTime(1, 1).subscribe(booleanTestSubscriber);
        //then
        verify(settingsDataSource, times(1)).save(anyObject());
        booleanTestSubscriber.assertValue(false);
        booleanTestSubscriber.assertNoErrors();
    }

    @Test
    public void shouldReturnTrueWhenSavingStreamSourceCompleted() {
        //when
        settingsManager.changeStreamSourceSettings(false, false, false, false).subscribe(booleanTestSubscriber);
        //then
        verify(settingsDataSource, times(1)).save(anyObject());
        booleanTestSubscriber.assertValue(true);
        booleanTestSubscriber.assertNoErrors();
    }

    @Test
    public void shouldReturnFalseWhenSavingStreamSourceFailed() {
        //given
        when(settingsDataSource.save(anyObject())).thenReturn(Observable.just(false));
        //when
        settingsManager.changeStreamSourceSettings(true, true, true, true).subscribe(booleanTestSubscriber);
        //then
        verify(settingsDataSource, times(1)).save(anyObject());
        booleanTestSubscriber.assertValue(false);
        booleanTestSubscriber.assertNoErrors();
    }

    @Test
    public void shouldReturnTrueWhenSavingShotDetailsCompleted() {
        //when
        settingsManager.changeShotsDetailsStatus(false).subscribe(booleanTestSubscriber);
        //then
        verify(settingsDataSource, times(1)).save(anyObject());
        booleanTestSubscriber.assertValue(true);
        booleanTestSubscriber.assertNoErrors();
    }

    @Test
    public void shouldReturnFalseWhenSavingShotDetailsFailed() {
        //given
        when(settingsDataSource.save(anyObject())).thenReturn(Observable.just(false));
        //when
        settingsManager.changeShotsDetailsStatus(true).subscribe(booleanTestSubscriber);
        //then
        verify(settingsDataSource, times(1)).save(anyObject());
        booleanTestSubscriber.assertValue(false);
        booleanTestSubscriber.assertNoErrors();
    }
}
package co.netguru.android.inbbbox.db.datasource;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.data.models.Settings;
import co.netguru.android.inbbbox.db.Storage;
import co.netguru.android.inbbbox.utils.Constants;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SettingsDataSourceImplTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    public Storage storageMock;

    @InjectMocks
    public SettingsDataSourceImpl settingsDataSource;

    @Test
    public void whenSaveMethodCalled_thenPutSettingsObjectToStorageWithSettingsKey() {
        Settings objectToSave = new Settings();
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        settingsDataSource.save(objectToSave).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        try {
            verify(storageMock, times(1)).put(Constants.Db.SETTINGS_KEY, objectToSave);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void whenObjectSaved_thenReturnTrueAsResult() {
        Settings objectToSave = new Settings();
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        settingsDataSource.save(objectToSave).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        Assert.assertEquals(testSubscriber.getOnNextEvents().get(0), true);
    }

    @Test
    public void whenGetExistingObjectFromDb_thenReturnTheObject() {
        Settings object = new Settings();
        try {
            when(storageMock.get(Constants.Db.SETTINGS_KEY, Settings.class)).thenReturn(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TestSubscriber<Settings> testSubscriber = new TestSubscriber<>();

        settingsDataSource.get().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        Settings result = testSubscriber.getOnNextEvents().get(0);
        Assert.assertEquals(object, result);
    }

    @Test
    public void whenGettingNotExistingObjectFromDb_thenReturnReturnEmptyObservable() {
        try {
            when(storageMock.get(Constants.Db.SETTINGS_KEY, Settings.class))
                    .thenThrow(new Throwable("test"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        TestSubscriber<Settings> testSubscriber = new TestSubscriber<>();

        settingsDataSource.get().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        Settings object = testSubscriber.getOnNextEvents().get(0);
        Assert.assertNull(object);
    }
}
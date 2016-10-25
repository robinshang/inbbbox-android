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

import static org.mockito.Mockito.doThrow;
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
    public void whenSaveMethodCalled_thenPutSettingsObjectToStorageWithSettingsKey() throws Exception {
        Settings objectToSave = new Settings();
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        settingsDataSource.save(objectToSave).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(storageMock, times(1)).put(Constants.Db.SETTINGS_KEY, objectToSave);
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
    public void whenGetExistingObjectFromDb_thenReturnTheObject() throws Exception {
        Settings object = new Settings();
        when(storageMock.get(Constants.Db.SETTINGS_KEY, Settings.class)).thenReturn(object);
        TestSubscriber<Settings> testSubscriber = new TestSubscriber<>();

        settingsDataSource.get().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        Settings result = testSubscriber.getOnNextEvents().get(0);
        Assert.assertEquals(object, result);
    }

    //ERRORS
    @Test
    public void whenSaveMethodFailed_thenReturnFalse() throws Exception {
        doThrow(new Throwable()).doCallRealMethod();
        Settings objectToSave = new Settings();
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        settingsDataSource.save(objectToSave).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        Assert.assertEquals(testSubscriber.getOnNextEvents().get(0), false);
    }

    @Test
    public void whenGettingNotExistingObjectFromDb_thenReturnReturnEmptyObservable() throws Exception {
        doThrow(new Exception()).when(storageMock).get(Constants.Db.SETTINGS_KEY, Settings.class);
        TestSubscriber<Settings> testSubscriber = new TestSubscriber<>();

        settingsDataSource.get().subscribe(testSubscriber);

        Assert.assertEquals(testSubscriber.getValueCount(), 0);
    }
}
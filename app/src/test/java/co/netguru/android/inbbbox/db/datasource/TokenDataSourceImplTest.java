package co.netguru.android.inbbbox.db.datasource;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.data.models.Token;
import co.netguru.android.inbbbox.db.Storage;
import co.netguru.android.inbbbox.utils.Constants;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TokenDataSourceImplTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    public Storage storageMock;

    @InjectMocks
    public TokenDataSourceImpl tokkenDataSource;

    @Test
    public void whenSaveMethodCalled_thenPutSettingsObjectToStorageWithSettingsKey() throws Exception {
        Token objectToSave = new Token();
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        tokkenDataSource.save(objectToSave).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(storageMock, times(1)).put(Constants.Db.TOKEN_KEY, objectToSave);
    }

    @Test
    public void whenObjectSaved_thenReturnTrueAsResult() {
        Token objectToSave = new Token();
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        tokkenDataSource.save(objectToSave).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        Assert.assertEquals(testSubscriber.getOnNextEvents().get(0), true);
    }

    @Test
    public void whenGetExistingObjectFromDb_thenReturnTheObject() throws Exception {
        Token object = new Token();
        when(storageMock.get(Constants.Db.TOKEN_KEY, Token.class)).thenReturn(object);
        TestSubscriber<Token> testSubscriber = new TestSubscriber<>();

        tokkenDataSource.get().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        Token result = testSubscriber.getOnNextEvents().get(0);
        Assert.assertEquals(object, result);
    }

    //ERRORS
    @Test
    public void whenSaveMethodFailed_thenReturnFalse() throws Exception {
        doThrow(new Throwable()).doCallRealMethod();
        Token objectToSave = new Token();
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        tokkenDataSource.save(objectToSave).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        Assert.assertEquals(false, testSubscriber.getOnNextEvents().get(0));
    }

    @Test
    public void whenGettingNotExistingObjectFromDb_thenReturnReturnEmptyObservable() throws Exception {
        doThrow(new Exception()).when(storageMock).get(Constants.Db.TOKEN_KEY, Token.class);
        TestSubscriber<Token> testSubscriber = new TestSubscriber<>();

        tokkenDataSource.get().subscribe(testSubscriber);

        Assert.assertEquals(testSubscriber.getValueCount(), 0);
    }

}
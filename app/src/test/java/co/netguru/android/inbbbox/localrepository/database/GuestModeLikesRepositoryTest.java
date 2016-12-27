package co.netguru.android.inbbbox.localrepository.database;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.rx.RxDao;
import org.greenrobot.greendao.rx.RxQuery;
import org.greenrobot.greendao.rx.RxTransaction;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.model.localrepository.database.DaoSession;
import co.netguru.android.inbbbox.model.localrepository.database.ShotDB;
import co.netguru.android.inbbbox.model.localrepository.database.ShotDBDao;
import co.netguru.android.inbbbox.model.localrepository.database.TeamDB;
import co.netguru.android.inbbbox.model.localrepository.database.TeamDBDao;
import co.netguru.android.inbbbox.model.localrepository.database.UserDB;
import co.netguru.android.inbbbox.model.localrepository.database.UserDBDao;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GuestModeLikesRepositoryTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    DaoSession daoSession;
    @Mock
    ShotDBDao shotDBDao;
    @Mock
    UserDBDao userDBDao;
    @Mock
    TeamDBDao teamDBDao;
    @Mock
    RxDao<ShotDB, Long> shotRxDao;
    @Mock
    QueryBuilder<ShotDB> shotDBQueryBuilder;
    @Mock
    RxQuery<ShotDB> shotDBRxQuery;

    private RxTransaction rxTransaction;

    @InjectMocks
    GuestModeLikesRepository repository;

    @Before
    public void setUp() {
        rxTransaction = new RxTransaction(daoSession);
        when(daoSession.getShotDBDao()).thenReturn(shotDBDao);
        when(daoSession.getUserDBDao()).thenReturn(userDBDao);
        when(daoSession.getTeamDBDao()).thenReturn(teamDBDao);
        when(shotDBDao.rx()).thenReturn(shotRxDao);
        when(daoSession.rxTx()).thenReturn(rxTransaction);
        doAnswer(invocation -> {
            final Runnable test = (Runnable) invocation.getArguments()[0];
            test.run();
            return null;
        }).when(daoSession).runInTx(any());
    }

    @Test
    public void shouldInsertShotIntoDBWhenAddingLike() {
        //given
        final TestSubscriber subscriber = new TestSubscriber();
        //when
        repository.addLikedShot(Statics.NOT_LIKED_SHOT).subscribe(subscriber);
        //then
        verify(shotDBDao).insertOrReplace(any(ShotDB.class));

        subscriber.assertNoErrors();
    }

    @Test
    public void shouldInsertUserIntoDBWhenAddingLike() {
        //given
        final TestSubscriber subscriber = new TestSubscriber();
        //when
        repository.addLikedShot(Statics.NOT_LIKED_SHOT).subscribe(subscriber);
        //then
        verify(userDBDao).insertOrReplace(any(UserDB.class));
        subscriber.assertNoErrors();
    }

    @Test
    public void shouldInsertTeamIntoDBWhenAddingLike() {
        //given
        final TestSubscriber subscriber = new TestSubscriber();
        //when
        repository.addLikedShot(Statics.NOT_LIKED_SHOT).subscribe(subscriber);
        //then
        verify(teamDBDao).insertOrReplace(any(TeamDB.class));
        subscriber.assertNoErrors();
    }

    @Test
    public void shouldQueryDBWhenGettingLikedShots() {
        //given
        final TestSubscriber<List<Shot>> subscriber = new TestSubscriber<>();
        when(shotDBDao.queryBuilder()).thenReturn(shotDBQueryBuilder);
        when(shotDBQueryBuilder.rx()).thenReturn(shotDBRxQuery);
        when(shotDBRxQuery.oneByOne()).thenReturn(Observable.empty());
        //when
        repository.getLikedShots().subscribe(subscriber);
        //then
        subscriber.assertNoErrors();
    }

    @Test
    public void shouldDeleteFromDBWhenRemovingNotBucketedShot() {
        //given
        final TestSubscriber subscriber = new TestSubscriber();
        when(shotRxDao.deleteByKey(any())).thenReturn(Observable.empty());
        //when
        repository.removeLikedShot(Statics.LIKED_SHOT_NOT_BUCKETED).subscribe(subscriber);
        //then
        verify(shotRxDao).deleteByKey(any());
        verify(shotRxDao, never()).insertOrReplace(any());
        subscriber.assertNoErrors();
    }

    @Test
    public void shouldInsertUpdatedShotToDBWhenRemovingBucketedShot() {
        //given
        final TestSubscriber subscriber = new TestSubscriber();
        when(shotRxDao.insertOrReplace(any())).thenReturn(Observable.empty());
        //when
        repository.removeLikedShot(Statics.LIKED_SHOT_BUCKETED).subscribe(subscriber);
        //then
        verify(shotRxDao, never()).deleteByKey(any());
        verify(shotRxDao).insertOrReplace(any());
        subscriber.assertNoErrors();
    }

    @Test
    public void shouldReturnErrorWhenRemovingLikedShotFailed() {
        //given
        final TestSubscriber subscriber = new TestSubscriber();
        final Throwable throwable = new Throwable();
        when(shotRxDao.deleteByKey(any())).thenReturn(Observable.error(throwable));
        //when
        repository.removeLikedShot(Statics.LIKED_SHOT_NOT_BUCKETED).subscribe(subscriber);
        //then
        subscriber.assertError(throwable);
    }

    @Test
    public void shouldBeNoErrorsWhenShotIsLiked() {
        //given
        final TestSubscriber subscriber = new TestSubscriber();
        when(shotDBDao.queryBuilder()).thenReturn(shotDBQueryBuilder);
        when(shotDBQueryBuilder.where(any())).thenReturn(shotDBQueryBuilder);
        when(shotDBQueryBuilder.rx()).thenReturn(shotDBRxQuery);
        when(shotDBRxQuery.unique()).thenReturn(Observable.empty());
        //when
        repository.isShotLiked(Statics.NOT_LIKED_SHOT).subscribe(subscriber);
        //then
        subscriber.assertNoErrors();
    }

    @Test
    public void shouldReturnErrorWhenShotIsNotLiked() {
        //given
        final TestSubscriber subscriber = new TestSubscriber();
        when(shotDBDao.queryBuilder()).thenReturn(shotDBQueryBuilder);
        when(shotDBQueryBuilder.where(any())).thenReturn(shotDBQueryBuilder);
        when(shotDBQueryBuilder.rx()).thenReturn(shotDBRxQuery);
        when(shotDBRxQuery.unique()).thenReturn(Observable.just(null));
        //when
        repository.isShotLiked(Statics.NOT_LIKED_SHOT).subscribe(subscriber);
        //then
        subscriber.assertError(Throwable.class);
    }
}
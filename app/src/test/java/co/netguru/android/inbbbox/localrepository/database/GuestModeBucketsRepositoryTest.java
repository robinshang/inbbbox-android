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

import java.util.LinkedList;
import java.util.List;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.localrepository.database.BucketDB;
import co.netguru.android.inbbbox.model.localrepository.database.BucketDBDao;
import co.netguru.android.inbbbox.model.localrepository.database.DaoSession;
import co.netguru.android.inbbbox.model.localrepository.database.ShotDB;
import co.netguru.android.inbbbox.model.localrepository.database.ShotDBDao;
import co.netguru.android.inbbbox.model.localrepository.database.mapper.ShotDBMapper;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GuestModeBucketsRepositoryTest {

    private static final int BUCKET_ID = 1;
    private static final String BUCKET_NAME = "test";
    private static final String BUCKET_DESCRIPTION = "test";
    private static final List<ShotDB> LIST_WITH_LIKED_SHOT;
    private static final List<ShotDB> LIST_WITH_NOT_LIKED_SHOT;

    static {
        LIST_WITH_LIKED_SHOT = new LinkedList<>();
        LIST_WITH_NOT_LIKED_SHOT = new LinkedList<>();
        LIST_WITH_LIKED_SHOT.add(ShotDBMapper.fromShot(Statics.LIKED_SHOT_NOT_BUCKETED));
        LIST_WITH_NOT_LIKED_SHOT.add(ShotDBMapper.fromShot(Statics.NOT_LIKED_SHOT_NOT_BUCKETED));
    }

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    DaoSession daoSession;
    @Mock
    BucketDBDao bucketDBDao;
    @Mock
    ShotDBDao shotDBDao;
    @Mock
    QueryBuilder<ShotDB> shotDBQueryBuilder;
    @Mock
    QueryBuilder<BucketDB> bucketDBQueryBuilder;
    @Mock
    RxQuery<BucketDB> bucketDBRxQuery;
    @Mock
    RxQuery<ShotDB> shotDBRxQuery;
    @Mock
    RxDao<BucketDB, Long> bucketDBRxDao;
    @Mock
    BucketDB bucketDB;

    @InjectMocks
    GuestModeBucketsRepository repository;

    private RxTransaction rxTransaction;

    @Before
    public void setUp() {
        rxTransaction = new RxTransaction(daoSession);
        when(daoSession.getShotDBDao()).thenReturn(shotDBDao);
        when(daoSession.getBucketDBDao()).thenReturn(bucketDBDao);
        when(daoSession.rxTx()).thenReturn(rxTransaction);
        when(bucketDBDao.rx()).thenReturn(bucketDBRxDao);
        doAnswer(invocation -> {
            final Runnable test = (Runnable) invocation.getArguments()[0];
            test.run();
            return null;
        }).when(daoSession).runInTx(any());
    }

    @Test
    public void shouldQueryDBWhenGettingBuckets() {
        //given
        final TestSubscriber<List<Bucket>> subscriber = new TestSubscriber<>();
        when(bucketDBDao.queryBuilder()).thenReturn(bucketDBQueryBuilder);
        when(bucketDBQueryBuilder.rx()).thenReturn(bucketDBRxQuery);
        when(bucketDBRxQuery.oneByOne()).thenReturn(Observable.empty());
        //when
        repository.getUserBuckets().subscribe(subscriber);
        //then
        subscriber.assertNoErrors();
    }

    @Test
    public void shouldQueryDBWhenAddingShotToBucket() {
        //given
        final TestSubscriber<List<Bucket>> subscriber = new TestSubscriber<>();
        when(bucketDBDao.queryBuilder()).thenReturn(bucketDBQueryBuilder);
        when(bucketDBQueryBuilder.rx()).thenReturn(bucketDBRxQuery);
        when(bucketDBRxQuery.oneByOne()).thenReturn(Observable.empty());
        //when
        repository.addShotToBucket(BUCKET_ID, Statics.NOT_LIKED_SHOT).subscribe(subscriber);
        //then
        subscriber.assertNoErrors();
    }

    @Test
    public void shouldInsertShotAuthorIfExistsWhenAddingShotToBucket() {
        //given
        final TestSubscriber<List<Bucket>> subscriber = new TestSubscriber<>();
        when(bucketDBDao.queryBuilder()).thenReturn(bucketDBQueryBuilder);
        when(bucketDBQueryBuilder.rx()).thenReturn(bucketDBRxQuery);
        when(bucketDBRxQuery.oneByOne()).thenReturn(Observable.just(Statics.BUCKET_DB));
        //when
        repository.addShotToBucket(BUCKET_ID, Statics.NOT_LIKED_SHOT).subscribe(subscriber);
        //then
        verify(daoSession, times(3)).insertOrReplace(any());
    }

    @Test
    public void shouldNotInsertShotAuthorIfNotExistsWhenAddingShotToBucket() {
        //given
        final TestSubscriber<List<Bucket>> subscriber = new TestSubscriber<>();
        when(bucketDBDao.queryBuilder()).thenReturn(bucketDBQueryBuilder);
        when(bucketDBQueryBuilder.rx()).thenReturn(bucketDBRxQuery);
        when(bucketDBRxQuery.oneByOne()).thenReturn(Observable.just(Statics.BUCKET_DB));
        //when
        repository.addShotToBucket(BUCKET_ID, Statics.NOT_LIKED_SHOT_WITHOUT_AUTHOR).subscribe(subscriber);
        //then
        verify(daoSession, times(2)).insertOrReplace(any());
    }

    @Test
    public void shouldInsertBucketToDBWhenCreatingBucket() {
        //given
        final TestSubscriber<Bucket> subscriber = new TestSubscriber<>();
        when(bucketDBRxDao.insertOrReplace(any())).thenReturn(Observable.just(Statics.BUCKET_DB));
        //when
        repository.createBucket(BUCKET_NAME, BUCKET_DESCRIPTION).subscribe(subscriber);
        //then
        verify(bucketDBRxDao, times(1)).insertOrReplace(any());
        subscriber.assertNoErrors();
    }

    @Test
    public void shouldQueryDbWhenGettingBucketWithShots() {
        //given
        final TestSubscriber<List<BucketWithShots>> subscriber = new TestSubscriber<>();
        when(bucketDBDao.queryBuilder()).thenReturn(bucketDBQueryBuilder);
        when(bucketDBQueryBuilder.rx()).thenReturn(bucketDBRxQuery);
        when(bucketDBRxQuery.oneByOne()).thenReturn(Observable.empty());
        //when
        repository.getUserBucketsWithShots().subscribe(subscriber);
        //then
        subscriber.assertNoErrors();
    }

    @Test
    public void shouldRemoveBucketFromDBWhenRemovingBucket() {
        //given
        final TestSubscriber subscriber = new TestSubscriber<>();
        when(daoSession.load(any(), any())).thenReturn(bucketDB);
        when(bucketDB.getShots()).thenReturn(new LinkedList<>());
        //when
        repository.removeBucket(BUCKET_ID).subscribe(subscriber);
        //then
        verify(bucketDBDao).deleteByKey(any());
        subscriber.assertNoErrors();
    }

    @Test
    public void shouldRemoveShotsFromIfIsNotBucketedAndLikedWhenRemovingBucket() {
        //given
        final TestSubscriber subscriber = new TestSubscriber<>();
        when(daoSession.load(any(), any())).thenReturn(bucketDB);
        when(bucketDB.getShots()).thenReturn(LIST_WITH_NOT_LIKED_SHOT);
        //when
        repository.removeBucket(BUCKET_ID).subscribe(subscriber);
        //then
        verify(shotDBDao).deleteByKey(any());
    }

    @Test
    public void shouldNotRemoveShotsFromIfIsLikedWhenRemovingBucket() {
        //given
        final TestSubscriber subscriber = new TestSubscriber<>();
        when(daoSession.load(any(), any())).thenReturn(bucketDB);
        when(bucketDB.getShots()).thenReturn(LIST_WITH_LIKED_SHOT);
        //when
        repository.removeBucket(BUCKET_ID).subscribe(subscriber);
        //then
        verify(shotDBDao, never()).deleteByKey(any());
    }

    @Test
    public void shouldEmitNoValuesWhenBucketHasNoShots() {
        //given
        final TestSubscriber<List<Shot>> subscriber = new TestSubscriber<>();
        when(bucketDBDao.queryBuilder()).thenReturn(bucketDBQueryBuilder);
        when(bucketDBQueryBuilder.where(any())).thenReturn(bucketDBQueryBuilder);
        when(bucketDBQueryBuilder.rx()).thenReturn(bucketDBRxQuery);
        when(bucketDBRxQuery.unique()).thenReturn(Observable.just(null));
        //when
        repository.getShotsListFromBucket(BUCKET_ID).subscribe(subscriber);
        //then
        subscriber.assertNoValues();
    }

    @Test
    public void shouldEmitNoValuesWhenShotIsBucketed() {
        //given
        final TestSubscriber<List<Shot>> subscriber = new TestSubscriber<>();
        when(shotDBDao.queryBuilder()).thenReturn(shotDBQueryBuilder);
        when(shotDBQueryBuilder.where(any())).thenReturn(shotDBQueryBuilder);
        when(shotDBQueryBuilder.rx()).thenReturn(shotDBRxQuery);
        when(shotDBRxQuery.unique()).thenReturn(Observable.just(ShotDBMapper.fromShot(Statics.LIKED_SHOT_BUCKETED)));
        //when
        repository.isShotBucketed(BUCKET_ID).subscribe(subscriber);
        //then
        subscriber.assertNoValues();
        subscriber.assertNoErrors();
    }

    @Test
    public void shouldEmitErrorWhenShotIsNotBucketed() {
        //given
        final TestSubscriber<List<Shot>> subscriber = new TestSubscriber<>();
        when(shotDBDao.queryBuilder()).thenReturn(shotDBQueryBuilder);
        when(shotDBQueryBuilder.where(any())).thenReturn(shotDBQueryBuilder);
        when(shotDBQueryBuilder.rx()).thenReturn(shotDBRxQuery);
        when(shotDBRxQuery.unique()).thenReturn(Observable.just(ShotDBMapper.fromShot(Statics.LIKED_SHOT_NOT_BUCKETED)));
        //when
        repository.isShotBucketed(BUCKET_ID).subscribe(subscriber);
        //then
        subscriber.assertError(Throwable.class);
    }
}
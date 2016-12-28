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
import co.netguru.android.inbbbox.model.localrepository.database.UserDBDao;
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

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    DaoSession daoSession;
    @Mock
    BucketDBDao bucketDBDao;
    @Mock
    ShotDBDao shotDBDao;
    @Mock
    UserDBDao userDBDao;
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
    @Mock
    ShotDB shotDB;

    @InjectMocks
    GuestModeBucketsRepository repository;

    private RxTransaction rxTransaction;

    @Before
    public void setUp() {
        rxTransaction = new RxTransaction(daoSession);
        when(daoSession.getShotDBDao()).thenReturn(shotDBDao);
        when(daoSession.getBucketDBDao()).thenReturn(bucketDBDao);
        when(daoSession.getUserDBDao()).thenReturn(userDBDao);
        when(daoSession.rxTx()).thenReturn(rxTransaction);
        when(bucketDBDao.rx()).thenReturn(bucketDBRxDao);
        when(shotDBDao.queryBuilder()).thenReturn(shotDBQueryBuilder);
        when(shotDBQueryBuilder.where(any())).thenReturn(shotDBQueryBuilder);
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
        when(bucketDBQueryBuilder.where(any())).thenReturn(bucketDBQueryBuilder);
        when(bucketDBQueryBuilder.rx()).thenReturn(bucketDBRxQuery);
        when(bucketDBRxQuery.unique()).thenReturn(Observable.empty());
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
        when(bucketDBQueryBuilder.where(any())).thenReturn(bucketDBQueryBuilder);
        when(bucketDBQueryBuilder.rx()).thenReturn(bucketDBRxQuery);
        when(bucketDBRxQuery.unique()).thenReturn(Observable.just(Statics.BUCKET_DB));
        //when
        repository.addShotToBucket(BUCKET_ID, Statics.NOT_LIKED_SHOT).subscribe(subscriber);
        //then
        verify(daoSession.getUserDBDao()).insertOrReplace(any());
    }

    @Test
    public void shouldNotInsertShotAuthorIfNotExistsWhenAddingShotToBucket() {
        //given
        final TestSubscriber<List<Bucket>> subscriber = new TestSubscriber<>();
        when(bucketDBDao.queryBuilder()).thenReturn(bucketDBQueryBuilder);
        when(bucketDBQueryBuilder.where(any())).thenReturn(bucketDBQueryBuilder);
        when(bucketDBQueryBuilder.rx()).thenReturn(bucketDBRxQuery);
        when(bucketDBRxQuery.unique()).thenReturn(Observable.just(Statics.BUCKET_DB));
        //when
        repository.addShotToBucket(BUCKET_ID, Statics.NOT_LIKED_SHOT_WITHOUT_AUTHOR).subscribe(subscriber);
        //then
        verify(daoSession.getUserDBDao(), never()).insertOrReplace(any());
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
        verify(bucketDB).delete();
        subscriber.assertNoErrors();
    }

    @Test
    public void shouldRemoveShotsFromIfIsNotBucketedAndLikedWhenRemovingBucket() {
        //given
        final TestSubscriber subscriber = new TestSubscriber<>();
        final List<ShotDB> mockedList = getListWithMockedShot(false);
        when(daoSession.load(any(), any())).thenReturn(bucketDB);
        when(bucketDB.getShots()).thenReturn(mockedList);
        //when
        repository.removeBucket(BUCKET_ID).subscribe(subscriber);
        //then
        verify(shotDB).delete();
    }

    @Test
    public void shouldNotRemoveShotsFromIfIsLikedWhenRemovingBucket() {
        //given
        final TestSubscriber subscriber = new TestSubscriber<>();
        final List<ShotDB> mockedList = getListWithMockedShot(true);
        when(daoSession.load(any(), any())).thenReturn(bucketDB);
        when(bucketDB.getShots()).thenReturn(mockedList);
        //when
        repository.removeBucket(BUCKET_ID).subscribe(subscriber);
        //then
        verify(shotDB, never()).delete();
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
    public void shouldEmitTrueWhenShotIsBucketed() {
        //given
        final TestSubscriber<Boolean> subscriber = new TestSubscriber<>();
        when(shotDBDao.queryBuilder()).thenReturn(shotDBQueryBuilder);
        when(shotDBQueryBuilder.where(any())).thenReturn(shotDBQueryBuilder);
        when(shotDBQueryBuilder.rx()).thenReturn(shotDBRxQuery);
        when(shotDBRxQuery.unique()).thenReturn(Observable.just(ShotDBMapper.fromShot(Statics.LIKED_SHOT_BUCKETED)));
        //when
        repository.isShotBucketed(BUCKET_ID).subscribe(subscriber);
        //then
        subscriber.assertValue(Boolean.TRUE);
        subscriber.assertNoErrors();
    }

    @Test
    public void shouldEmitFalseWhenShotIsNotBucketed() {
        //given
        final TestSubscriber<Boolean> subscriber = new TestSubscriber<>();
        when(shotDBDao.queryBuilder()).thenReturn(shotDBQueryBuilder);
        when(shotDBQueryBuilder.where(any())).thenReturn(shotDBQueryBuilder);
        when(shotDBQueryBuilder.rx()).thenReturn(shotDBRxQuery);
        when(shotDBRxQuery.unique()).thenReturn(Observable.just(ShotDBMapper.fromShot(Statics.LIKED_SHOT_NOT_BUCKETED)));
        //when
        repository.isShotBucketed(BUCKET_ID).subscribe(subscriber);
        //then
        subscriber.assertValue(Boolean.FALSE);
        subscriber.assertNoErrors();
    }

    private List<ShotDB> getListWithMockedShot(boolean isLiked) {
        final List<ShotDB> mockedList = new LinkedList<>();
        mockedList.add(shotDB);
        when(shotDB.getIsLiked()).thenReturn(isLiked);
        return mockedList;
    }
}
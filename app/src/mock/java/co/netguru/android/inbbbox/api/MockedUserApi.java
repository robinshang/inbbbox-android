package co.netguru.android.inbbbox.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.api.Links;
import co.netguru.android.inbbbox.model.api.UserEntity;
import retrofit2.http.Query;
import rx.Observable;
import rx.Single;

public class MockedUserApi implements UserApi {

    private static final UserEntity MOCKED_USER;
    private static final List<Bucket> mockedUserBuckets;

    static {
        MOCKED_USER = UserEntity.builder().avatarUrl("").bio("").bucketsCount(1).bucketsUrl("")
                .bio("").canUploadShot(false).commentsReceivedCount(1).createdAt(ZonedDateTime.now()).followersCount(1)
                .followersUrl("").followingsCount(1).followingUrl("").htmlUrl("").id(1).likesCount(1).likesReceivedCount(1)
                .likesUrl("").links(Links.create("", "")).location("").name("").pro(true).projectsCount(1).reboundsReceivedCount(1)
                .shotsCount(1).shotsUrl("").teamsCount(1).teamsUrl("").username("").updatedAt(ZonedDateTime.now()).type("").build();

        mockedUserBuckets = Arrays.asList(
                Bucket.builder().createdAt(ZonedDateTime.now())
                        .description("")
                        .id(1)
                        .name("name1")
                        .shotsCount(10)
                        .build(),
                Bucket.builder().createdAt(ZonedDateTime.now())
                        .description("")
                        .id(2)
                        .name("name2")
                        .shotsCount(10)
                        .build()
        );
    }

    @Override
    public Observable<UserEntity> getAuthenticatedUser() {
        return Observable.just(MOCKED_USER);
    }

    @Override
    public Single<List<Bucket>> getUserBucketsList(@Query("page") int pageNumber, @Query("per_page") int pageCount) {
        return Single.fromCallable(() -> new ArrayList<>(mockedUserBuckets));
    }

    @Override
    public Single<List<Bucket>> getUserBucketsList() {
        return Single.fromCallable(() -> new ArrayList<>(mockedUserBuckets));
    }
}

package co.netguru.android.inbbbox.api;


import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.netguru.android.inbbbox.model.api.BucketShot;
import co.netguru.android.inbbbox.model.api.Image;
import retrofit2.http.Path;
import rx.Single;

public class MockedBucketApi implements BucketApi {

    private static final List<BucketShot> mockedBucketShots;

    static {
        mockedBucketShots = Arrays.asList(
                BucketShot.builder().createdAt(LocalDateTime.now()).description("")
                        .updatedAt(LocalDateTime.now()).id(1).image(Image.builder().build())
                        .updatedAt(LocalDateTime.now()).title("").build(),
                BucketShot.builder().createdAt(LocalDateTime.now()).description("")
                        .updatedAt(LocalDateTime.now()).id(2).image(Image.builder().build())
                        .updatedAt(LocalDateTime.now()).title("").build()
        );
    }

    @Override
    public Single<List<BucketShot>> getBucketShots(@Path("id") long id) {
        return Single.fromCallable(() -> new ArrayList<>(mockedBucketShots));
    }
}

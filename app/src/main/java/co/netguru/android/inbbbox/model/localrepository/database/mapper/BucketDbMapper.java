package co.netguru.android.inbbbox.model.localrepository.database.mapper;

import org.threeten.bp.ZonedDateTime;

import co.netguru.android.inbbbox.model.localrepository.database.BucketDB;

public class BucketDbMapper {

    private BucketDbMapper() {
        throw new AssertionError();
    }

    public static BucketDB createNewBucket(String name, String description) {
        return new BucketDB(null, name, description, 0, ZonedDateTime.now());
    }
}

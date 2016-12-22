package co.netguru.android.inbbbox.model.localrepository.database.mapper;

import org.threeten.bp.LocalDateTime;

import co.netguru.android.inbbbox.model.localrepository.database.BucketDB;

public class BucketDbMapper {

    private BucketDbMapper() {
        throw new AssertionError();
    }

    // TODO: 22.12.2016 Set unique id
    public static BucketDB createNewBucket(String name, String description) {
        return new BucketDB(1, name, description, 0, LocalDateTime.now());
    }
}

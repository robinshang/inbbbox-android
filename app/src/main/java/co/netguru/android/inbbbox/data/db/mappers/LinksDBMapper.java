package co.netguru.android.inbbbox.data.db.mappers;

import co.netguru.android.inbbbox.data.db.LinksDB;
import co.netguru.android.inbbbox.data.dribbbleuser.Links;

public class LinksDBMapper {

    private LinksDBMapper() {
        throw new AssertionError();
    }

    public static LinksDB fromLinks(long userId, Links links) {
        return new LinksDB(userId, links.web(), links.twitter());
    }
}

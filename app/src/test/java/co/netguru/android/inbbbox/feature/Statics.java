package co.netguru.android.inbbbox.feature;

import co.netguru.android.inbbbox.models.Links;
import co.netguru.android.inbbbox.models.User;

public final class Statics {

    public static final User USER = User.builder().avatarUrl("").bio("").bucketsCount(1).bucketsUrl("")
            .bio("").canUploadShot(false).commentsReceivedCount(1).createdAt("").followersCount(1)
            .followersUrl("").followingsCount(1).followingUrl("").htmlUrl("").id(1).likesCount(1).likesReceivedCount(1)
            .likesUrl("").links(new Links()).location("").name("").pro(true).projectsCount(1).reboundsReceivedCount(1)
            .shotsCount(1).shotsUrl("").teamsCount(1).teamsUrl("").username(""  ).updatedAt("").type("").build();
}

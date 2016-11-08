package co.netguru.android.inbbbox;

import org.threeten.bp.LocalDateTime;

import co.netguru.android.inbbbox.model.api.Links;
import co.netguru.android.inbbbox.model.api.User;

public final class Statics {

    public static final User USER = User.builder().avatarUrl("").bio("").bucketsCount(1).bucketsUrl("")
            .bio("").canUploadShot(false).commentsReceivedCount(1).createdAt(LocalDateTime.now()).followersCount(1)
            .followersUrl("").followingsCount(1).followingUrl("").htmlUrl("").id(1).likesCount(1).likesReceivedCount(1)
            .likesUrl("").links(new Links()).location("").name("").pro(true).projectsCount(1).reboundsReceivedCount(1)
            .shotsCount(1).shotsUrl("").teamsCount(1).teamsUrl("").username("").updatedAt(LocalDateTime.now()).type("").build();
}

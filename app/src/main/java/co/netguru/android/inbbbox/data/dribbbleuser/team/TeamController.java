package co.netguru.android.inbbbox.data.dribbbleuser.team;

import java.util.List;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import rx.Single;

public interface TeamController {
    Single<List<User>> getTeamMembers(long teamId, int pageNumber,
                                      int pageCount, int shotsPerUser);
}

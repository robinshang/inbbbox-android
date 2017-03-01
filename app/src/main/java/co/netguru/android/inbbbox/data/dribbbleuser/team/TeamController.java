package co.netguru.android.inbbbox.data.dribbbleuser.team;

import java.util.List;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import rx.Single;

public interface TeamController {
    Single<List<User>> getTeamMembers(long teamId, int pageNumber,
                                      int pageCount);

    Single<List<User>> getUserTeams(long userId, int pageNumber, int pageCount);
}

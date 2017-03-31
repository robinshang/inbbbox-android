package co.netguru.android.inbbbox.data.db.mappers;

import co.netguru.android.inbbbox.data.db.TeamDB;
import co.netguru.android.inbbbox.data.dribbbleuser.team.Team;

public class TeamDBMapper {

    private TeamDBMapper() {
        throw new AssertionError();
    }

    public static TeamDB fromTeam(Team team) {
        return new TeamDB(team.id(), team.name(), team.username(), team.avatarUrl(), team.shotsCount(),
                team.followersCount(), team.followingsCount(), team.bucketsCount(), team.projectsCount(),
                team.bio(), team.location());
    }
}

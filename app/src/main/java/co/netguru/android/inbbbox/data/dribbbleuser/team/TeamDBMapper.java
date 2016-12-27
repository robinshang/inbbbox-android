package co.netguru.android.inbbbox.data.dribbbleuser.team;

public class TeamDBMapper {

    private TeamDBMapper() {
        throw new AssertionError();
    }

    public static TeamDB fromTeam(Team team) {
        return new TeamDB(team.id(), team.name());
    }
}

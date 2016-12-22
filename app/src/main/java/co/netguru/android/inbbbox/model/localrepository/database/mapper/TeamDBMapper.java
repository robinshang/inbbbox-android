package co.netguru.android.inbbbox.model.localrepository.database.mapper;

import co.netguru.android.inbbbox.model.localrepository.database.TeamDB;
import co.netguru.android.inbbbox.model.ui.Team;

public class TeamDBMapper {

    private TeamDBMapper() {
        throw new AssertionError();
    }

    public static TeamDB fromTeam(Team team) {
        return new TeamDB(team.id(), team.name());
    }
}

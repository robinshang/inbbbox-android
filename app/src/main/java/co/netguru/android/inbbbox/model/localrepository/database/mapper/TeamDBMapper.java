package co.netguru.android.inbbbox.model.localrepository.database.mapper;

import javax.inject.Singleton;

import co.netguru.android.inbbbox.model.localrepository.database.TeamDB;
import co.netguru.android.inbbbox.model.ui.Team;

@Singleton
public class TeamDBMapper {

    private TeamDBMapper() {
        throw new AssertionError();
    }

    public static TeamDB fromTeam(Team team) {
        return new TeamDB(team.id(), team.name());
    }
}

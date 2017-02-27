package co.netguru.android.inbbbox.feature.user.info.singleuser;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.user.info.singleuser.teams.TeamClickListener;
import co.netguru.android.inbbbox.feature.user.info.singleuser.teams.TeamsAdapter;

public class UserInfoTeamsViewHolder extends BaseViewHolder<List<User>> {

    @BindView(R.id.user_teams_recyclerView)
    RecyclerView teamsRecyclerView;

    private TeamClickListener teamClickListener;

    public UserInfoTeamsViewHolder(ViewGroup parent, TeamClickListener teamClickListener) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_info_item_teams, parent, false));
        this.teamClickListener = teamClickListener;
    }

    @Override
    public void bind(List<User> teams) {
        initRecycler(teams);
    }

    private void initRecycler(List<User> teams) {
        TeamsAdapter teamsAdapter = new TeamsAdapter(teamClickListener);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(teamsRecyclerView.getContext(), 2);

        teamsRecyclerView.setLayoutManager(gridLayoutManager);
        teamsRecyclerView.setHasFixedSize(true);
        teamsRecyclerView.setAdapter(teamsAdapter);
        teamsRecyclerView.setNestedScrollingEnabled(false);

        teamsAdapter.setTeams(teams);
    }
}

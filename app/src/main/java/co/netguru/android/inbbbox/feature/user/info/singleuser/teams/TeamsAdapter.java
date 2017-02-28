package co.netguru.android.inbbbox.feature.user.info.singleuser.teams;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;

public class TeamsAdapter extends RecyclerView.Adapter<TeamViewHolder> {

    private final List<User> teams = new ArrayList<>();
    private TeamClickListener teamClickListener;

    public TeamsAdapter(TeamClickListener teamClickListener) {
        this.teamClickListener = teamClickListener;
    }

    @Override
    public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TeamViewHolder(parent, teamClickListener);
    }

    @Override
    public void onBindViewHolder(TeamViewHolder holder, int position) {
        holder.bind(teams.get(position));
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public void setTeams(List<User> teams) {
        this.teams.clear();
        this.teams.addAll(teams);
        notifyDataSetChanged();
    }
}

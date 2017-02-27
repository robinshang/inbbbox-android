package co.netguru.android.inbbbox.feature.user.info.singleuser;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.user.info.singleuser.teams.TeamClickListener;
import co.netguru.android.inbbbox.feature.user.info.team.adapter.UserInfoTeamHeaderViewHolder;

class UserInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<User> teams = new ArrayList<>();
    private final List<Shot> shots = new ArrayList<>();
    private User user;

    private ShotClickListener shotClickListener;
    private TeamClickListener teamClickListener;

    UserInfoAdapter(ShotClickListener shotClickListener,
                    TeamClickListener teamClickListener) {
        this.shotClickListener = shotClickListener;
        this.teamClickListener = teamClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new UserInfoTeamHeaderViewHolder(parent);
            case 1:
                return new UserInfoShotsViewHolder(parent, shotClickListener);
            case 2:
                return new UserInfoTeamsViewHolder(parent, teamClickListener);
            default:
                throw new IllegalArgumentException(
                        String.format("Could not find view holder for viewType: %d", viewType));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (position) {
            case 0:
                ((UserInfoTeamHeaderViewHolder) holder).bind(user);
                break;

            case 1:
                ((UserInfoShotsViewHolder) holder).bind(shots);
                break;

            case 2:
                ((UserInfoTeamsViewHolder) holder).bind(teams);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return teams.isEmpty() ? 2 : 3;
    }

    public void setTeams(List<User> teams) {
        this.teams.clear();
        this.teams.addAll(teams);
        notifyDataSetChanged();
    }

    public void setShots(List<Shot> shots) {
        this.shots.clear();
        this.shots.addAll(shots);
        notifyDataSetChanged();
    }

    public void setUser(User user) {
        this.user = user;
        notifyDataSetChanged();
    }
}

package co.netguru.android.inbbbox.feature.user.info.singleuser;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.shared.peekandpop.ShotPeekAndPop;
import co.netguru.android.inbbbox.feature.user.info.LinkClickListener;
import co.netguru.android.inbbbox.feature.user.info.singleuser.teams.TeamClickListener;
import co.netguru.android.inbbbox.feature.user.info.team.adapter.UserInfoTeamHeaderViewHolder;

class UserInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_SHOTS = 1;
    private static final int VIEW_TYPE_TEAMS = 2;

    private static final int ALL_VIEWS_AMOUNT = 4;

    private final List<User> teams = new ArrayList<>();
    private final List<Shot> shots = new ArrayList<>();
    private User user;

    private ShotClickListener shotClickListener;
    private TeamClickListener teamClickListener;
    private ShotPeekAndPop shotPeekAndPop;
    private LinkClickListener linkClickListener;

    UserInfoAdapter(ShotClickListener shotClickListener, TeamClickListener teamClickListener,
                    ShotPeekAndPop shotPeekAndPop, LinkClickListener linkClickListener) {
        this.shotClickListener = shotClickListener;
        this.teamClickListener = teamClickListener;
        this.shotPeekAndPop = shotPeekAndPop;
        this.linkClickListener = linkClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new UserInfoTeamHeaderViewHolder(parent);
            case VIEW_TYPE_SHOTS:
                return new UserInfoShotsViewHolder(parent, shotClickListener, shotPeekAndPop);
            case VIEW_TYPE_TEAMS:
                return new UserInfoTeamsViewHolder(parent, teamClickListener);
            case 3:
                return new UserInfoLinksViewHolder(parent, linkClickListener);
            default:
                throw new IllegalArgumentException(
                        String.format("Could not find view holder for viewType: %d", viewType));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (position) {
            case VIEW_TYPE_HEADER:
                ((UserInfoTeamHeaderViewHolder) holder).bind(user);
                break;
            case VIEW_TYPE_SHOTS:
                ((UserInfoShotsViewHolder) holder).bind(shots);
                break;
            case VIEW_TYPE_TEAMS:
                ((UserInfoTeamsViewHolder) holder).bind(teams);
                break;
            case 3:
                ((UserInfoLinksViewHolder) holder).bind(user.links());
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("Could not bind view holder for position: %d", position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return ALL_VIEWS_AMOUNT;
    }

    public List<Shot> getShots() {
        return shots;
    }

    public void setShots(List<Shot> shots) {
        this.shots.clear();
        this.shots.addAll(shots);
        notifyDataSetChanged();
    }

    public void setTeams(List<User> teams) {
        this.teams.clear();
        this.teams.addAll(teams);
        notifyDataSetChanged();
    }

    public void setUser(User user) {
        this.user = user;
        notifyDataSetChanged();
    }
}

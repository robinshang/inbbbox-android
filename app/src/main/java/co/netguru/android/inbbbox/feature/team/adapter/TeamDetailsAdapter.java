package co.netguru.android.inbbbox.feature.team.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.feature.follower.adapter.BaseFollowersViewHolder;
import co.netguru.android.inbbbox.feature.follower.adapter.FollowersGridViewHolder;
import co.netguru.android.inbbbox.feature.follower.adapter.FollowersListViewHolder;
import co.netguru.android.inbbbox.feature.follower.adapter.OnFollowerClickListener;

public class TeamDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER_POSITION = 0;

    private static final int TYPE_LIST = 0;
    private static final int TYPE_GRID = 1;
    private static final int TYPE_HEADER = 2;

    private final OnFollowerClickListener userClickListener;

    @NonNull
    private List<UserWithShots> teamMembersList;
    private UserWithShots team;
    private boolean isGridMode;

    @Inject
    public TeamDetailsAdapter(OnFollowerClickListener userClickListener) {
        this.userClickListener = userClickListener;
        teamMembersList = Collections.emptyList();
    }

    public UserWithShots getTeam() {
        return team;
    }

    public void setTeam(UserWithShots team) {
        this.team = team;
    }

    public void setTeamMembers(List<UserWithShots> teamMembersList) {
        this.teamMembersList = teamMembersList;
        notifyDataSetChanged();
    }

    public void addMoreMembers(List<UserWithShots> members) {
        final int currentSize = this.teamMembersList.size();
        this.teamMembersList.addAll(members);
        notifyItemRangeChanged(currentSize - 1, members.size());
    }

    public List<UserWithShots> getData() {
        return teamMembersList;
    }

    public void setGridMode(boolean isGridMode) {
        this.isGridMode = isGridMode;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new TeamDetailsHeaderViewHolder(parent);
            case TYPE_LIST:
                return new FollowersListViewHolder(parent, userClickListener);
            case TYPE_GRID:
                return new FollowersGridViewHolder(parent, userClickListener);
            default:
                throw new IllegalArgumentException("Cannot create view holder for type : " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        if (viewType == TYPE_HEADER) {
            ((TeamDetailsHeaderViewHolder) holder).bind(team.user());
        } else if (viewType == TYPE_LIST || viewType == TYPE_GRID) {
            ((BaseFollowersViewHolder) holder).bind(teamMembersList.get(getListPosition(position)));
        } else {
            throw new IllegalArgumentException("There is no item type for " +
                    "holder on this position : " + getListPosition(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == HEADER_POSITION) {
            return TYPE_HEADER;
        }

        return isGridMode ? TYPE_GRID : TYPE_LIST;
    }

    @Override
    public int getItemCount() {
        return team != null ? teamMembersList.size() + 1 : teamMembersList.size();
    }

    private int getListPosition(int position) {
        return team != null ? position - 1 : position;
    }
}

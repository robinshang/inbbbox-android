package co.netguru.android.inbbbox.feature.team.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.feature.follower.adapter.BaseFollowersViewHolder;
import co.netguru.android.inbbbox.feature.follower.adapter.FollowersEmptyShotGridViewHolder;
import co.netguru.android.inbbbox.feature.follower.adapter.FollowersEmptyShotListViewHolder;
import co.netguru.android.inbbbox.feature.follower.adapter.FollowersFourShotGridViewHolder;
import co.netguru.android.inbbbox.feature.follower.adapter.FollowersFourShotListViewHolder;
import co.netguru.android.inbbbox.feature.follower.adapter.FollowersOneShotGridViewHolder;
import co.netguru.android.inbbbox.feature.follower.adapter.FollowersOneShotListViewHolder;
import co.netguru.android.inbbbox.feature.follower.adapter.FollowersThreeShotGridViewHolder;
import co.netguru.android.inbbbox.feature.follower.adapter.FollowersThreeShotListViewHolder;
import co.netguru.android.inbbbox.feature.follower.adapter.FollowersTwoShotGridViewHolder;
import co.netguru.android.inbbbox.feature.follower.adapter.FollowersTwoShotListViewHolder;
import co.netguru.android.inbbbox.feature.follower.adapter.OnFollowerClickListener;

public class TeamDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER_POSITION = 0;

    private static final int TYPE_EMPTY_LIST = 0;
    private static final int TYPE_EMPTY_GRID = 1;
    private static final int TYPE_ONE_SHOT_LIST = 2;
    private static final int TYPE_ONE_SHOT_GRID = 3;
    private static final int TYPE_TWO_SHOT_LIST = 4;
    private static final int TYPE_TWO_SHOT_GRID = 5;
    private static final int TYPE_THREE_SHOT_LIST = 6;
    private static final int TYPE_THREE_SHOT_GRID = 7;
    private static final int TYPE_FOUR_SHOT_LIST = 8;
    private static final int TYPE_FOUR_SHOT_GRID = 9;
    private static final int TYPE_HEADER = 10;

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
            case TYPE_EMPTY_GRID:
                return new FollowersEmptyShotGridViewHolder(parent, userClickListener);
            case TYPE_EMPTY_LIST:
                return new FollowersEmptyShotListViewHolder(parent, userClickListener);
            case TYPE_ONE_SHOT_GRID:
                return new FollowersOneShotGridViewHolder(parent, userClickListener);
            case TYPE_ONE_SHOT_LIST:
                return new FollowersOneShotListViewHolder(parent, userClickListener);
            case TYPE_TWO_SHOT_GRID:
                return new FollowersTwoShotGridViewHolder(parent, userClickListener);
            case TYPE_TWO_SHOT_LIST:
                return new FollowersTwoShotListViewHolder(parent, userClickListener);
            case TYPE_THREE_SHOT_GRID:
                return new FollowersThreeShotGridViewHolder(parent, userClickListener);
            case TYPE_THREE_SHOT_LIST:
                return new FollowersThreeShotListViewHolder(parent, userClickListener);
            case TYPE_FOUR_SHOT_GRID:
                return new FollowersFourShotGridViewHolder(parent, userClickListener);
            case TYPE_FOUR_SHOT_LIST:
                return new FollowersFourShotListViewHolder(parent, userClickListener);
            default:
                throw new IllegalArgumentException("Cannot create view holder for type : " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                ((TeamDetailsHeaderViewHolder) holder).bind(team.user());
                break;
            case TYPE_EMPTY_GRID:
            case TYPE_EMPTY_LIST:
            case TYPE_ONE_SHOT_GRID:
            case TYPE_ONE_SHOT_LIST:
            case TYPE_TWO_SHOT_GRID:
            case TYPE_TWO_SHOT_LIST:
            case TYPE_THREE_SHOT_GRID:
            case TYPE_THREE_SHOT_LIST:
            case TYPE_FOUR_SHOT_GRID:
            case TYPE_FOUR_SHOT_LIST:
                ((BaseFollowersViewHolder) holder).bind(teamMembersList.get(getListPosition(position)));
                break;
            default:
                throw new IllegalArgumentException("There is no item type for " +
                        "holder on this position : " + getListPosition(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == HEADER_POSITION) {
            return TYPE_HEADER;
        }

        if (teamMembersList == null || teamMembersList.isEmpty()) {
            return isGridMode ? TYPE_EMPTY_GRID : TYPE_EMPTY_LIST;
        }

        switch (teamMembersList.get(getListPosition(position)).shotList().size()) {
            case 1:
                return isGridMode ? TYPE_ONE_SHOT_GRID : TYPE_ONE_SHOT_LIST;
            case 2:
                return isGridMode ? TYPE_TWO_SHOT_GRID : TYPE_TWO_SHOT_LIST;
            case 3:
                return isGridMode ? TYPE_THREE_SHOT_GRID : TYPE_THREE_SHOT_LIST;
            default:
                return isGridMode ? TYPE_FOUR_SHOT_GRID : TYPE_FOUR_SHOT_LIST;
        }
    }

    @Override
    public int getItemCount() {
        return team != null ? teamMembersList.size() + 1 : teamMembersList.size();
    }

    private int getListPosition(int position) {
        return team != null ? position - 1 : position;
    }
}

package co.netguru.android.inbbbox.feature.followers.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.model.ui.Shot;

public class FollowersAdapter extends RecyclerView.Adapter<BaseFollowersViewHolder> {

    private static final int TYPE_EMPTY = 0;
    private static final int TYPE_ONE_SHOT = 1;
    private static final int TYPE_TWO_SHOT = 2;
    private static final int TYPE_THREE_SHOT = 3;
    private static final int TYPE_FOUR_SHOT = 4;

    private final List<Follower> followersList;

    private boolean isGridMode;

    @Inject
    public FollowersAdapter() {
        followersList = new ArrayList<>();
    }

    @Override
    public BaseFollowersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_EMPTY:
                return new FollowersEmptyShotListViewHolder(parent);
            case TYPE_ONE_SHOT:
                return isGridMode ? new FollowersOneShotGridViewHolder(parent) : new FollowersOneShotListViewHolder(parent);
            case TYPE_TWO_SHOT:
                return isGridMode ? new FollowersTwoShotGridViewHolder(parent) : new FollowersTwoShotListViewHolder(parent);
            case TYPE_THREE_SHOT:
                return isGridMode ? new FollowersThreeShotGridViewHolder(parent) : new FollowersThreeShotListViewHolder(parent);
            case TYPE_FOUR_SHOT:
                return isGridMode ? new FollowersFourShotGridViewHolder(parent) : new FollowersFourShotListViewHolder(parent);
            default:
                throw new IllegalArgumentException("Cannot create view holder for type : " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(BaseFollowersViewHolder holder, int position) {
        holder.bind(followersList.get(position));
    }

    @Override
    public int getItemCount() {
        return followersList.size();
    }

    public void setFollowersList(List<Follower> followersList) {
        this.followersList.clear();
        this.followersList.addAll(followersList);
        notifyDataSetChanged();
    }

    public void setGridMode(boolean isGridMode) {
        this.isGridMode = isGridMode;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        final List<Shot> shotList = followersList.get(position).shotList();
        if (shotList == null || shotList.isEmpty()) {
            return TYPE_EMPTY;
        }
        switch (shotList.size()) {
            case 1:
                return TYPE_ONE_SHOT;
            case 2:
                return TYPE_TWO_SHOT;
            case 3:
                return TYPE_THREE_SHOT;
            default:
                return TYPE_FOUR_SHOT;
        }
    }
}

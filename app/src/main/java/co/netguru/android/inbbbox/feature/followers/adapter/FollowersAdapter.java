package co.netguru.android.inbbbox.feature.followers.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.model.ui.Shot;

public class FollowersAdapter extends RecyclerView.Adapter<BaseFollowersViewHolder> {

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

    private final BaseFollowersViewHolder.OnFollowerClickListener onFollowerClickListener;
    private final List<Follower> followersList;

    private boolean isGridMode;

    public FollowersAdapter(BaseFollowersViewHolder.OnFollowerClickListener onFollowerClickListener) {
        this.onFollowerClickListener = onFollowerClickListener;
        followersList = new ArrayList<>();
    }

    @Override
    public BaseFollowersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_EMPTY_GRID:
                return new FollowersEmptyShotGridViewHolder(parent, onFollowerClickListener);
            case TYPE_EMPTY_LIST:
                return new FollowersEmptyShotListViewHolder(parent, onFollowerClickListener);
            case TYPE_ONE_SHOT_GRID:
                return new FollowersOneShotGridViewHolder(parent, onFollowerClickListener);
            case TYPE_ONE_SHOT_LIST:
                return new FollowersOneShotListViewHolder(parent, onFollowerClickListener);
            case TYPE_TWO_SHOT_GRID:
                return new FollowersTwoShotGridViewHolder(parent, onFollowerClickListener);
            case TYPE_TWO_SHOT_LIST:
                return new FollowersTwoShotListViewHolder(parent, onFollowerClickListener);
            case TYPE_THREE_SHOT_GRID:
                return new FollowersThreeShotGridViewHolder(parent, onFollowerClickListener);
            case TYPE_THREE_SHOT_LIST:
                return new FollowersThreeShotListViewHolder(parent, onFollowerClickListener);
            case TYPE_FOUR_SHOT_GRID:
                return new FollowersFourShotGridViewHolder(parent, onFollowerClickListener);
            case TYPE_FOUR_SHOT_LIST:
                return new FollowersFourShotListViewHolder(parent, onFollowerClickListener);
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

    public void addMoreFollowers(List<Follower> followersList) {
        final int currentSize = this.followersList.size();
        this.followersList.addAll(followersList);
        notifyItemRangeChanged(currentSize - 1, followersList.size());
    }

    public void setGridMode(boolean isGridMode) {
        this.isGridMode = isGridMode;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        final List<Shot> shotList = followersList.get(position).shotList();
        if (shotList == null || shotList.isEmpty()) {
            return isGridMode ? TYPE_EMPTY_GRID : TYPE_EMPTY_LIST;
        }
        switch (shotList.size()) {
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
}

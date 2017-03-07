package co.netguru.android.inbbbox.feature.follower.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;

public class FollowersAdapter extends RecyclerView.Adapter<BaseFollowersViewHolder> {

    private static final int TYPE_LIST = 0;
    private static final int TYPE_GRID = 1;

    private final OnFollowerClickListener onFollowerClickListener;

    private List<UserWithShots> userWithShotsList;

    private boolean isGridMode;

    public FollowersAdapter(OnFollowerClickListener onFollowerClickListener) {
        this.onFollowerClickListener = onFollowerClickListener;
        userWithShotsList = Collections.emptyList();
    }

    @Override
    public BaseFollowersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LIST:
                return new FollowersListViewHolder(parent, onFollowerClickListener);
            case TYPE_GRID:
                return new FollowersGridViewHolder(parent, onFollowerClickListener);
            default:
                throw new IllegalArgumentException("Cannot create view holder for type : " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(BaseFollowersViewHolder holder, int position) {
        holder.bind(userWithShotsList.get(position));
    }

    @Override
    public int getItemCount() {
        return userWithShotsList.size();
    }

    public void setUserWithShotsList(List<UserWithShots> userWithShotsList) {
        this.userWithShotsList = userWithShotsList;
        notifyDataSetChanged();
    }

    public void addMoreFollowers(List<UserWithShots> userWithShotsList) {
        final int currentSize = this.userWithShotsList.size();
        this.userWithShotsList.addAll(userWithShotsList);
        notifyItemRangeChanged(currentSize - 1, userWithShotsList.size());
    }

    public void setGridMode(boolean isGridMode) {
        this.isGridMode = isGridMode;
        notifyDataSetChanged();
    }

    public List<UserWithShots> getData() {
        return userWithShotsList;
    }

    @Override
    public int getItemViewType(int position) {
        return isGridMode ? TYPE_GRID : TYPE_LIST;
    }
}

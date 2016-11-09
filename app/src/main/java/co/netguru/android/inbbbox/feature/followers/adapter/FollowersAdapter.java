package co.netguru.android.inbbbox.feature.followers.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.model.ui.Follower;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersViewHolder> {

    private final List<Follower> followersList;

    private boolean isGridMode;

    @Inject
    public FollowersAdapter() {
        followersList = new ArrayList<>();
    }

    @Override
    public FollowersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(getViewHolderLayout(parent), parent, false);
        return new FollowersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FollowersViewHolder holder, int position) {
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

    @LayoutRes
    private int getViewHolderLayout(ViewGroup viewGroup) {
        RecyclerView.LayoutManager layoutManager = ((RecyclerView) viewGroup).getLayoutManager();
        if (layoutManager == null) {
            throw new IllegalStateException("Recycler view should have layout manager already!");
        }
        return layoutManager instanceof GridLayoutManager
                ? R.layout.follower_item_grid_view : R.layout.follower_item_list_view;
    }
}

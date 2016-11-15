package co.netguru.android.inbbbox.feature.followers.details.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.model.ui.Shot;

public class FollowerDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_GRID = 1;
    private static final int TYPE_LIST = 2;

    private final List<Shot> shotList;
    private Follower follower;
    private boolean isGridMode;

    @Inject
    FollowerDetailsAdapter() {
        shotList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new FollowerDetailsHeaderViewHolder(parent);
            case TYPE_GRID:
                return new FollowerDetailsGridViewHolder(parent);
            case TYPE_LIST:
                return new FollowerDetailsListViewHolder(parent);
            default:
                throw new IllegalArgumentException("Cannot create view holder for type : " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                ((FollowerDetailsHeaderViewHolder) holder).bind(follower);
                break;
            case TYPE_GRID:
                ((FollowerDetailsGridViewHolder) holder).bind(shotList.get(position - 1));
                break;
            case TYPE_LIST:
                ((FollowerDetailsListViewHolder) holder).bind(shotList.get(position - 1));
            default:
                throw new IllegalArgumentException("There is no item type for holder on this position : " + position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        if (isGridMode) {
            return TYPE_GRID;
        }
        return TYPE_LIST;
    }

    @Override
    public int getItemCount() {
        return follower != null ? shotList.size() + 1 : shotList.size();
    }

    public void setGridMode(boolean isGridMode) {
        this.isGridMode = isGridMode;
        notifyDataSetChanged();
    }

    public void setAdapterData(Follower follower) {
        this.follower = follower;
        this.shotList.clear();
        this.shotList.addAll(follower.shotList());
        notifyDataSetChanged();
    }

    public void addMoreUserShots(List<Shot> shotList) {
        final int currentSize = this.shotList.size() + 1;
        this.shotList.addAll(shotList);
        notifyItemRangeChanged(currentSize - 1, shotList.size());
    }
}

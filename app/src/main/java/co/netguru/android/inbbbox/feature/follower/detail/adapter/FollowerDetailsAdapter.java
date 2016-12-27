package co.netguru.android.inbbbox.feature.follower.detail.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.follower.model.ui.Follower;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.common.view.ShotClickListener;

public class FollowerDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_GRID = 1;
    private static final int TYPE_LIST = 2;

    private static final int HEADER_POSITION = 0;

    private final ShotClickListener shotClickListener;

    @NonNull
    private List<Shot> shotList;
    private Follower follower;
    private boolean isGridMode;

    @Inject
    public FollowerDetailsAdapter(ShotClickListener shotClickListener) {
        this.shotClickListener = shotClickListener;
        shotList = Collections.emptyList();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new FollowerDetailsHeaderViewHolder(parent);
            case TYPE_GRID:
                return new FollowerDetailsGridViewHolder(parent, shotClickListener);
            case TYPE_LIST:
                return new FollowerDetailsListViewHolder(parent, shotClickListener);
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
                ((FollowerDetailsGridViewHolder) holder).bind(shotList.get(getShotListPosition(position)));
                break;
            case TYPE_LIST:
                ((FollowerDetailsListViewHolder) holder).bind(shotList.get(getShotListPosition(position)));
                break;
            default:
                throw new IllegalArgumentException("There is no item type for holder on this position : " + position);
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
        return follower != null ? shotList.size() + 1 : shotList.size();
    }

    public void setGridMode(boolean isGridMode) {
        this.isGridMode = isGridMode;
        notifyDataSetChanged();
    }

    public List<Shot> getData() {
        return shotList;
    }

    public void setFollowerAdapterData(Follower follower) {
        this.follower = follower;
        this.shotList = follower.shotList();
        notifyDataSetChanged();
    }

    public void setUserShots(List<Shot> shotList) {
        this.shotList = shotList;
        notifyDataSetChanged();
    }

    public void addMoreUserShots(List<Shot> shotList) {
        final int currentSize = this.shotList.size() + 1;
        this.shotList.addAll(shotList);
        notifyItemRangeChanged(currentSize - 1, shotList.size());
    }

    private int getShotListPosition(int position) {
        return follower != null ? position - 1 : position;
    }
}

package co.netguru.android.inbbbox.feature.like.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;

import static co.netguru.android.inbbbox.Constants.UNDEFINED;

public class LikesAdapter extends RecyclerView.Adapter<BaseViewHolder<Shot>> {

    private static final int TYPE_GRID = 0;
    private static final int TYPE_LIST = 1;

    private final ShotClickListener likeClickListener;

    @NonNull
    private List<Shot> likeList;
    private boolean isGridMode;

    public LikesAdapter(ShotClickListener likeClickListener) {
        this.likeClickListener = likeClickListener;
        likeList = Collections.emptyList();
    }

    @Override
    public BaseViewHolder<Shot> onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_GRID:
                return new LikesGridViewHolder(parent, likeClickListener);
            case TYPE_LIST:
                return new LikesListViewHolder(parent, likeClickListener);
            default:
                throw new IllegalArgumentException("Cannot create view holder for type : " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<Shot> holder, int position) {
        holder.bind(likeList.get(position));
    }

    @Override
    public int getItemCount() {
        return likeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isGridMode ? TYPE_GRID : TYPE_LIST;
    }

    public List<Shot> getData() {
        return likeList;
    }

    public void setLikeList(@NonNull List<Shot> likeList) {
        this.likeList = likeList;
        notifyDataSetChanged();
    }

    public void addMoreLikes(List<Shot> likeList) {
        final int currentSize = this.likeList.size();
        this.likeList.addAll(likeList);
        notifyItemRangeChanged(currentSize - 1, likeList.size());
    }

    public void addLikeOnTop(Shot likedShot) {
        final int shotPosition = getShotIndexIfExists(likedShot);
        if (shotPosition != UNDEFINED) {
            this.likeList.set(shotPosition, likedShot);
            notifyItemChanged(shotPosition);
        } else {
            this.likeList.add(0, likedShot);
            notifyItemRangeChanged(0, 1);
        }
    }

    public void removeLike(Shot unlikedShot) {
        final int shotPosition = getShotIndexIfExists(unlikedShot);
        if (shotPosition != UNDEFINED) {
            likeList.remove(shotPosition);
            notifyItemRemoved(shotPosition);
        }
    }

    public void setGridMode(boolean isGridMode) {
        this.isGridMode = isGridMode;
        notifyDataSetChanged();
    }

    private int getShotIndexIfExists(Shot likedShot) {
        for (int i = 0; i < likeList.size(); i++) {
            final Shot currentShot = likeList.get(i);
            if (currentShot.id() == likedShot.id()) {
                return i;
            }
        }
        return UNDEFINED;
    }
}

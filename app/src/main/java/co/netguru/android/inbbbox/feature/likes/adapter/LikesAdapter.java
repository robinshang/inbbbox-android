package co.netguru.android.inbbbox.feature.likes.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.feature.common.BaseViewHolder;
import co.netguru.android.inbbbox.model.ui.LikedShot;

@FragmentScope
public class LikesAdapter extends RecyclerView.Adapter<BaseViewHolder<LikedShot>> {

    private static final int TYPE_GRID = 0;
    private static final int TYPE_LIST = 1;

    private final List<LikedShot> likeList;

    private boolean isGridMode;

    @Inject
    LikesAdapter() {
        likeList = new ArrayList<>();
    }

    @Override
    public BaseViewHolder<LikedShot> onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_GRID:
                return new LikesGridViewHolder(parent);
            case TYPE_LIST:
                return new LikesListViewHolder(parent);
            default:
                throw new IllegalArgumentException("Cannot create view holder for type : " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<LikedShot> holder, int position) {
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

    public void setLikeList(List<LikedShot> likeList) {
        this.likeList.clear();
        this.likeList.addAll(likeList);
        notifyDataSetChanged();
    }

    public void addMoreLikes(List<LikedShot> likeList) {
        final int currentSize = this.likeList.size();
        this.likeList.addAll(likeList);
        notifyItemRangeChanged(currentSize - 1, likeList.size());
    }

    public void setGridMode(boolean isGridMode) {
        this.isGridMode = isGridMode;
        notifyDataSetChanged();
    }
}

package co.netguru.android.inbbbox.feature.likes.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.feature.common.BaseViewHolder;
import co.netguru.android.inbbbox.model.ui.Shot;

@FragmentScope
public class LikesAdapter extends RecyclerView.Adapter<BaseViewHolder<Shot>> {

    private static final int TYPE_GRID = 0;
    private static final int TYPE_LIST = 1;

    private final List<Shot> likeList;

    private boolean isGridMode;
    private LikeClickListener likeClickListener;

    @Inject
    public LikesAdapter(LikeClickListener likeClickListener) {
        this.likeClickListener = likeClickListener;
        likeList = new ArrayList<>();
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

    public void setLikeList(List<Shot> likeList) {
        this.likeList.clear();
        this.likeList.addAll(likeList);
        notifyDataSetChanged();
    }

    public void addMoreLikes(List<Shot> likeList) {
        final int currentSize = this.likeList.size();
        this.likeList.addAll(likeList);
        notifyItemRangeChanged(currentSize - 1, likeList.size());
    }

    public void setGridMode(boolean isGridMode) {
        this.isGridMode = isGridMode;
        notifyDataSetChanged();
    }
}

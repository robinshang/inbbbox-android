package co.netguru.android.inbbbox.feature.likes.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.model.ui.LikedShot;

@FragmentScope
public class LikesAdapter extends RecyclerView.Adapter<LikesViewHolder> {

    private List<LikedShot> likeList;

    private boolean isGridMode;

    @Inject
    LikesAdapter() {
        likeList = new ArrayList<>();
    }

    @Override
    public LikesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(isGridMode ? R.layout.like_item_grid_view : R.layout.like_item_list_view , parent, false);
        return new LikesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LikesViewHolder holder, int position) {
        holder.bind(likeList.get(position));
    }

    @Override
    public int getItemCount() {
        return likeList.size();
    }

    public void setLikeList(List<LikedShot> likeList) {
        this.likeList.clear();
        this.likeList.addAll(likeList);
        notifyDataSetChanged();
    }

    public void setGridMode(boolean isGridMode) {
        this.isGridMode = isGridMode;
    }

    public void addMoreLikes(List<LikedShot> likeList) {
        final int currentSize = this.likeList.size();
        this.likeList.addAll(likeList);
        notifyItemRangeChanged(currentSize, likeList.size());
    }
}

package co.netguru.android.inbbbox.feature.likes.adapter;

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
import co.netguru.android.inbbbox.di.scope.FragmentScope;

@FragmentScope
public class LikesAdapter extends RecyclerView.Adapter<LikesViewHolder> {

    private List<Object> likeList;

    @Inject
    LikesAdapter() {
        likeList = new ArrayList<>();
    }

    @Override
    public LikesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(getViewHolderLayout(parent), parent, false);
        return new LikesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LikesViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return likeList.size();
    }

    public void setLikeList(List<Object> likeList) {
        this.likeList = likeList;
        notifyDataSetChanged();
    }

    @LayoutRes
    private int getViewHolderLayout(ViewGroup viewGroup) {
        RecyclerView.LayoutManager layoutManager = ((RecyclerView) viewGroup).getLayoutManager();
        if (layoutManager == null) {
            throw new IllegalStateException("Recycler view should have layout manager already!");
        }
        return layoutManager instanceof GridLayoutManager ? R.layout.like_item_grid_view : R.layout.like_item_list_view;
    }
}

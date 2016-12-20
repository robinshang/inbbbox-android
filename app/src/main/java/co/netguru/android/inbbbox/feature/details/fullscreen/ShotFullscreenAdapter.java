package co.netguru.android.inbbbox.feature.details.fullscreen;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.model.ui.Shot;

public class ShotFullscreenAdapter extends RecyclerView.Adapter<ShotFullscreenViewHolder> {

    @NonNull
    private List<Shot> items = Collections.emptyList();

    @Override
    public ShotFullscreenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shot_fullscreen, parent, false);
        return new ShotFullscreenViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ShotFullscreenViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<Shot> getData() {
        return items;
    }

    public List<Shot> getItems() {
        return items;
    }

    public void setItems(List<Shot> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addMoreItems(List<Shot> items) {
        final int currentSize = this.items.size();
        this.items.addAll(items);
        notifyItemRangeChanged(currentSize - 1, items.size());
    }
}

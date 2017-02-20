package co.netguru.android.inbbbox.feature.shot.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

public class ShotsAdapter extends RecyclerView.Adapter<ShotsViewHolder> {

    private final ShotSwipeListener shotSwipeListener;
    private final DetailsVisibilityChangeEmitter emitter;
    private boolean isDetailsVisible;

    @NonNull
    private List<Shot> items;

    @Inject
    public ShotsAdapter(@NonNull ShotSwipeListener shotSwipeListener,
                        @NonNull DetailsVisibilityChangeEmitter emitter) {
        this.shotSwipeListener = shotSwipeListener;
        this.emitter = emitter;
        items = Collections.emptyList();
        isDetailsVisible = true;
    }

    @Override
    public ShotsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shot_layout, parent, false);
        return new ShotsViewHolder(itemView, shotSwipeListener, emitter);
    }

    @Override
    public void onBindViewHolder(ShotsViewHolder holder, int position) {
        holder.bind(items.get(position));
        holder.onDetailsChangeVisibility(isDetailsVisible);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<Shot> getData() {
        return items;
    }

    public void setItems(List<Shot> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public List<Shot> getItems() {
        return items;
    }

    public void addMoreItems(List<Shot> items) {
        final int currentSize = this.items.size();
        this.items.addAll(items);
        notifyItemRangeChanged(currentSize - 1, items.size());
    }

    public void updateShot(Shot shot) {
        final int position = findShotPosition(shot.id());
        items.set(position, shot);
        notifyItemChanged(position);
    }

    public void setDetailsVisibilityFlag(boolean isVisible) {
        this.isDetailsVisible = isVisible;
    }

    public Shot getShotFromPosition(int shotPosition) {
        return items.get(shotPosition);
    }

    private int findShotPosition(long id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).id() == id) {
                return i;
            }
        }
        throw new IllegalArgumentException("There is no shot with id :" + id);
    }
}

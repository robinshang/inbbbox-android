package co.netguru.android.inbbbox.feature.shots.recycler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.model.ui.Shot;

public class ShotsAdapter extends RecyclerView.Adapter<ShotsViewHolder> {

    private final ShotSwipeListener shotSwipeListener;
    private List<Shot> items;

    @Inject
    public ShotsAdapter(@NonNull ShotSwipeListener shotSwipeListener) {
        this.shotSwipeListener = shotSwipeListener;
    }

    @Override
    public ShotsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shot_layout, parent, false);
        return new ShotsViewHolder(itemView, shotSwipeListener);
    }

    @Override
    public void onBindViewHolder(ShotsViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void setItems(List<Shot> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void changeShotLikeStatus(Shot shot) {
        final int position = findShotPosition(shot.id());
        items.set(position, shot);
        notifyItemChanged(position);
    }

    @Nullable
    public Shot getShotFromPosition(int shotPosition) {
        return items == null ? null : items.get(shotPosition);
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

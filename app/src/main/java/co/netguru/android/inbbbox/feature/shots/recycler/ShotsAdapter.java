package co.netguru.android.inbbbox.feature.shots.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.ui.Shot;

public class ShotsAdapter extends RecyclerView.Adapter<ShotsViewHolder>
        implements ShotsViewHolder.OnShotLeftSwipeListener {

    private List<Shot> items;
    private OnItemLeftSwipeListener onItemLeftSwipeListener = OnItemLeftSwipeListener.NULL;

    @Inject
    public ShotsAdapter() {
        //DI Injection
    }

    @Override
    public ShotsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shot_layout, parent, false);
        return new ShotsViewHolder(itemView, this);
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

    @Override
    public void onLeftSwipe(int position) {
        onItemLeftSwipeListener.onItemLeftSwipe(position);
    }

    public void setOnLeftSwipeListener(OnItemLeftSwipeListener onItemLeftSwipeListener) {
        this.onItemLeftSwipeListener = onItemLeftSwipeListener == null
                ? OnItemLeftSwipeListener.NULL : onItemLeftSwipeListener;
    }

    public void changeShotLikeStatus(Shot shot) {
        final int position = findShotPosition(shot.id());
        items.set(position, shot);
        notifyItemChanged(position);
    }

    private int findShotPosition(int id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).id() == id) {
                return i;
            }
        }
        throw new IllegalArgumentException("There is no shot with id :" + id);
    }

    @FunctionalInterface
    public interface OnItemLeftSwipeListener {
        OnItemLeftSwipeListener NULL = shot -> {
        };

        void onItemLeftSwipe(int itemPosition);
    }
}

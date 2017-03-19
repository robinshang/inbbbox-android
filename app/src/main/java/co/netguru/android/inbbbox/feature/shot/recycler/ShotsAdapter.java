package co.netguru.android.inbbbox.feature.shot.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peekandpop.shalskar.peekandpop.PeekAndPop;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.peekandpop.ShotPeekAndPop;
import timber.log.Timber;

public class ShotsAdapter extends RecyclerView.Adapter<ShotsViewHolder> {

    private final ShotSwipeListener shotSwipeListener;
    private final DetailsVisibilityChangeEmitter emitter;
    private boolean isDetailsVisible;

    private ShotPeekAndPop peekAndPop;

    @NonNull
    private List<Shot> shots;

    @Inject
    public ShotsAdapter(@NonNull ShotSwipeListener shotSwipeListener,
                        @NonNull DetailsVisibilityChangeEmitter emitter, ShotPeekAndPop peekAndPop) {
        this.shotSwipeListener = shotSwipeListener;
        this.emitter = emitter;
        shots = Collections.emptyList();
        isDetailsVisible = true;
        this.peekAndPop = peekAndPop;
    }

    @Override
    public ShotsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shot_layout, parent, false);
        return new ShotsViewHolder(itemView, shotSwipeListener, emitter);
    }

    @Override
    public void onBindViewHolder(ShotsViewHolder holder, int position) {
        peekAndPop.addLongClickView(holder.shotImageView, position);
        holder.bind(shots.get(position));
        holder.onDetailsChangeVisibility(isDetailsVisible);
    }

    @Override
    public void onViewAttachedToWindow(ShotsViewHolder holder) {
        peekAndPop.addOnGeneralActionListener(holder.getPeekAndPopListener());
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(ShotsViewHolder holder) {
        peekAndPop.removeOnGeneralActionListener(holder.getPeekAndPopListener());
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public int getItemCount() {
        return shots.size();
    }

    public List<Shot> getData() {
        return shots;
    }

    public List<Shot> getShots() {
        return shots;
    }

    public void setShots(List<Shot> shots) {
        this.shots = shots;
        notifyDataSetChanged();
    }

    public void addMoreItems(List<Shot> items) {
        final int currentSize = this.shots.size();
        this.shots.addAll(items);
        notifyItemRangeChanged(currentSize - 1, items.size());
    }

    public void updateShot(Shot shot) {
        try {
            tryUpdateShot(shot);
        } catch(IllegalArgumentException e) {
            // it's ok, updated shot doesn't need to be present in this adapter
        }
    }

    private void tryUpdateShot(Shot shot) {
        final int position = findShotPosition(shot.id());
        shots.set(position, shot);
        notifyItemChanged(position);
    }

    public void setDetailsVisibilityFlag(boolean isVisible) {
        this.isDetailsVisible = isVisible;
    }

    public Shot getShotFromPosition(int shotPosition) {
        return shots.get(shotPosition);
    }

    private int findShotPosition(long id) {
        for (int i = 0; i < shots.size(); i++) {
            if (shots.get(i).id() == id) {
                return i;
            }
        }
        throw new IllegalArgumentException("There is no shot with id :" + id);
    }
}

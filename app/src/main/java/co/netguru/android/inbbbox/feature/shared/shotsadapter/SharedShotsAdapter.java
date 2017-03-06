package co.netguru.android.inbbbox.feature.shared.shotsadapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.peekandpop.shalskar.peekandpop.PeekAndPop;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.shared.peekandpop.ShotPeekAndPop;

public class SharedShotsAdapter extends RecyclerView.Adapter<SharedShotViewHolder> {

    private static final int TYPE_GRID_SHOT_VIEW_TYPE = 1;
    private static final int TYPE_LIST_SHOT_VIEW_TYPE = 2;

    private final ShotClickListener shotClickListener;
    private final ShotPeekAndPop peekAndPop;
    private final PeekAndPop.OnGeneralActionListener onGeneralActionListener;

    @NonNull
    private List<Shot> shotList;
    private boolean isGridMode;

    public SharedShotsAdapter(@NonNull ShotClickListener shotClickListener, ShotPeekAndPop peekAndPop,
                              PeekAndPop.OnGeneralActionListener onGeneralActionListener) {
        this.shotClickListener = shotClickListener;
        this.peekAndPop = peekAndPop;
        this.onGeneralActionListener = onGeneralActionListener;
        shotList = Collections.emptyList();

        setupPeekAndPop();
    }

    @Override
    public SharedShotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SharedShotViewHolder(parent, shotClickListener);
    }

    @Override
    public void onBindViewHolder(SharedShotViewHolder holder, int position) {
        peekAndPop.addLongClickView(holder.shotImageView, position);
        holder.bind(shotList.get(position));
    }

    @Override
    public int getItemCount() {
        return shotList.size();
    }

    public List<Shot> getData() {
        return shotList;
    }

    public void setShots(List<Shot> shotsToSet) {
        shotList = shotsToSet;
        notifyDataSetChanged();
    }

    public void addNewShots(Collection<Shot> shotsToAdd) {
        int oldSize = this.shotList.size();
        this.shotList.addAll(shotsToAdd);
        notifyItemRangeInserted(oldSize, shotsToAdd.size() + 1);
    }

    @Override
    public int getItemViewType(int position) {
        return isGridMode ? TYPE_GRID_SHOT_VIEW_TYPE : TYPE_LIST_SHOT_VIEW_TYPE;
    }

    public void setGridMode(boolean isGridMode) {
        this.isGridMode = isGridMode;
        notifyDataSetChanged();
    }

    private void setupPeekAndPop() {
        if(peekAndPop != null) {
            peekAndPop.setOnGeneralActionListener(new PeekAndPop.OnGeneralActionListener() {
                @Override
                public void onPeek(View view, int i) {
                    onGeneralActionListener.onPeek(view, i);
                    peekAndPop.bindPeekAndPop(shotList.get(i));
                }

                @Override
                public void onPop(View view, int i) {
                    onGeneralActionListener.onPop(view, i);
                }
            });
        }
    }
}
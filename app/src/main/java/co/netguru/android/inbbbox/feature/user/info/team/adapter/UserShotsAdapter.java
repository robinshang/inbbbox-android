package co.netguru.android.inbbbox.feature.user.info.team.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;

public class UserShotsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final List<Shot> shots = new ArrayList<>();

    private ShotClickListener listener;

    public UserShotsAdapter(ShotClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserShotHorizontalViewHolder(parent, listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((UserShotHorizontalViewHolder) holder).bind(shots.get(position));
    }

    @Override
    public int getItemCount() {
        return shots.size();
    }

    public List<Shot> getData() {
        return shots;
    }

    public void setShots(@NonNull List<Shot> shots) {
        this.shots.clear();
        this.shots.addAll(shots);
        notifyDataSetChanged();
    }

    public void addMoreShots(List<Shot> shots) {
        final int currentSize = this.shots.size();
        this.shots.addAll(shots);
        notifyItemRangeChanged(currentSize - 1, shots.size());
    }
}

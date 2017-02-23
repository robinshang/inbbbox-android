package co.netguru.android.inbbbox.feature.user.info.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.user.shots.adapter.UserShotsViewHolder;

public class UserShotsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private List<Shot> shots;

    public UserShotsAdapter() {
        shots = Collections.emptyList();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserShotHorizontalViewHolder(parent, null);
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

    public void setShots(List<Shot> shots) {
        this.shots = shots;
        notifyDataSetChanged();
    }

    public void addMoreShots(List<Shot> shots) {
        final int currentSize = this.shots.size();
        this.shots.addAll(shots);
        notifyItemRangeChanged(currentSize - 1, shots.size());
    }
}

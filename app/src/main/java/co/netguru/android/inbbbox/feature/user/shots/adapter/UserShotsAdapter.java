package co.netguru.android.inbbbox.feature.user.shots.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;

public class UserShotsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ShotClickListener shotClickListener;

    @NonNull
    private List<Shot> shotList;

    @Inject
    public UserShotsAdapter(ShotClickListener shotClickListener) {
        this.shotClickListener = shotClickListener;
        shotList = Collections.emptyList();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserShotsViewHolder(parent, shotClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((UserShotsViewHolder) holder).bind(shotList.get(position));
    }

    @Override
    public int getItemCount() {
        return shotList.size();
    }

    public List<Shot> getData() {
        return shotList;
    }

    public void setUserShots(List<Shot> shotList) {
        this.shotList = shotList;
        notifyDataSetChanged();
    }

    public void addMoreUserShots(List<Shot> newShots) {
        final int currentSize = this.shotList.size();
        this.shotList.addAll(newShots);
        notifyItemRangeChanged(currentSize - 1, shotList.size());
    }
}

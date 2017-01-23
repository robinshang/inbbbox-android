package co.netguru.android.inbbbox.feature.onboarding.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.feature.onboarding.OnboardingShot;
import co.netguru.android.inbbbox.feature.onboarding.OnboardingShotSwipeListener;

public class OnboardingShotsAdapter extends RecyclerView.Adapter<OnboardingShotsViewHolder> {

    private final OnboardingShotSwipeListener shotSwipeListener;

    @NonNull
    private List<OnboardingShot> items;

    @Inject
    public OnboardingShotsAdapter(@NonNull OnboardingShotSwipeListener shotSwipeListener) {
        this.shotSwipeListener = shotSwipeListener;
        items = Collections.emptyList();
    }

    @Override
    public OnboardingShotsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        return new OnboardingShotsViewHolder(itemView, shotSwipeListener);
    }

    @Override
    public void onBindViewHolder(OnboardingShotsViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<OnboardingShot> getData() {
        return items;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getLayoutResourceId();
    }

    public void setItems(List<OnboardingShot> items) {
        this.items = items;
        notifyDataSetChanged();
    }

}

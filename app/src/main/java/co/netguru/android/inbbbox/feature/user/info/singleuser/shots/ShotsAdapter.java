package co.netguru.android.inbbbox.feature.user.info.singleuser.shots;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.shared.peekandpop.ShotPeekAndPop;
import co.netguru.android.inbbbox.feature.user.info.team.adapter.UserShotHorizontalViewHolder;

public class ShotsAdapter extends RecyclerView.Adapter<UserShotHorizontalViewHolder> {

    private List<Shot> shotList;

    private ShotClickListener listener;
    private ShotPeekAndPop peekAndPop;

    public ShotsAdapter(ShotClickListener listener, ShotPeekAndPop shotPeekAndPop) {
        this.listener = listener;
        this.peekAndPop = shotPeekAndPop;
    }

    @Override
    public UserShotHorizontalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserShotHorizontalViewHolder(parent, listener);
    }

    @Override
    public void onBindViewHolder(UserShotHorizontalViewHolder holder, int position) {
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

    public void setShots(List<Shot> shotList) {
        this.shotList = shotList;
        notifyDataSetChanged();
    }
}
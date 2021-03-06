package co.netguru.android.inbbbox.feature.user.info.team.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.shared.peekandpop.ShotPeekAndPop;

public class UserShotsAdapter extends RecyclerView.Adapter<UserShotHorizontalViewHolder> {

    private UserWithShots user;

    private ShotClickListener listener;
    private ShotPeekAndPop peekAndPop;
    private int userPosition;

    public UserShotsAdapter(ShotClickListener listener, ShotPeekAndPop shotPeekAndPop) {
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
        holder.shotImageView.setTag(userPosition);
        holder.bind(user.shotList().get(position));
    }

    @Override
    public int getItemCount() {
        return user.shotList().size();
    }

    public List<Shot> getData() {
        return user.shotList();
    }

    public void setUser(UserWithShots user, int userPosition) {
        this.userPosition = userPosition;
        this.user = user;
        notifyDataSetChanged();
    }
}
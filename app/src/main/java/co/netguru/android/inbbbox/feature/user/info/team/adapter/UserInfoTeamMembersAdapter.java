package co.netguru.android.inbbbox.feature.user.info.team.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.user.UserClickListener;
import timber.log.Timber;

public class UserInfoTeamMembersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_USER = 1;

    @NonNull
    private List<UserWithShots> userList;

    private User team;

    private UserClickListener userClickListener;
    private ShotClickListener shotClickListener;

    public UserInfoTeamMembersAdapter(UserClickListener userClickListener,
                                      ShotClickListener shotClickListener) {
        userList = Collections.emptyList();
        this.userClickListener = userClickListener;
        this.shotClickListener = shotClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new UserInfoTeamHeaderViewHolder(parent);
            case VIEW_TYPE_USER:
                return new UserInfoTeamMembersViewHolder(parent, userClickListener,
                        shotClickListener);
            default:
                throw new IllegalArgumentException("Cannot create view holder for type : " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEADER:
                ((UserInfoTeamHeaderViewHolder) holder).bind(team);
                break;
            case VIEW_TYPE_USER:
                ((UserInfoTeamMembersViewHolder) holder).bind(userList.get(position-1));
                break;
            default:
                throw new IllegalArgumentException("Couldn't bind position: "+position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_USER;
        }
    }

    @Override
    public int getItemCount() {
        return userList.size() + 1;
    }

    public void setTeam(User team) {
        this.team = team;
        notifyDataSetChanged();
    }

    public List<UserWithShots> getData() {
        return userList;
    }

    public void setTeamMembers(List<UserWithShots> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    public void addMoreTeamMembers(List<UserWithShots> newUsers) {
        final int currentSize = this.userList.size();
        this.userList.addAll(newUsers);
        notifyItemRangeChanged(currentSize - 1, newUsers.size());

        Timber.d("new members size: "+userList.size());
        for(UserWithShots user : userList) {
            Timber.d("User: "+user.user().name());
        }
    }
}

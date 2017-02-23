package co.netguru.android.inbbbox.feature.user.info.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;

public class UserInfoTeamMembersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_USER = 1;

    @NonNull
    private List<UserWithShots> userList;

    public UserInfoTeamMembersAdapter() {
        userList = Collections.emptyList();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new UserInfoTeamHeaderViewHolder(parent);
            case VIEW_TYPE_USER:
                return new UserInfoTeamMembersViewHolder(parent);
            default:
                throw new IllegalArgumentException("Cannot create view holder for type : " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEADER:
                ((UserInfoTeamHeaderViewHolder) holder).bind(userList.get(position).user());
                break;
            case VIEW_TYPE_USER:
                ((UserInfoTeamMembersViewHolder) holder).bind(userList.get(position));
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
        return userList.size();
    }

    public List<UserWithShots> getData() {
        return userList;
    }

    public void setUserShots(List<UserWithShots> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    public void addMoreUsers(List<UserWithShots> newUsers) {
        final int currentSize = this.userList.size();
        this.userList.addAll(newUsers);
        notifyItemRangeChanged(currentSize - 1, newUsers.size());
    }
}

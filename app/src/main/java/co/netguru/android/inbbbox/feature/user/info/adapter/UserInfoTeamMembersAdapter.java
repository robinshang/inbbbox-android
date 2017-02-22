package co.netguru.android.inbbbox.feature.user.info.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;

public class UserInfoTeamMembersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private List<User> userList;

    public UserInfoTeamMembersAdapter() {
        userList = Collections.emptyList();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserInfoTeamMembersViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((UserInfoTeamMembersViewHolder) holder).bind(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public List<User> getData() {
        return userList;
    }

    public void setUserShots(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    public void addMoreUsers(List<User> newUsers) {
        final int currentSize = this.userList.size();
        this.userList.addAll(newUsers);
        notifyItemRangeChanged(currentSize - 1, newUsers.size());
    }
}

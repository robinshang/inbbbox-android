package co.netguru.android.inbbbox.feature.user.info.team.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.shared.peekandpop.ShotPeekAndPop;
import co.netguru.android.inbbbox.feature.user.UserClickListener;
import co.netguru.android.inbbbox.feature.user.info.LinkClickListener;
import co.netguru.android.inbbbox.feature.user.info.singleuser.UserInfoLinksViewHolder;

public class UserInfoTeamMembersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_LINKS = 2;

    private static final int HEADER_FOOTER_ADD_SIZE = 2;

    @NonNull
    private List<UserWithShots> userList;

    private User team;

    private UserClickListener userClickListener;
    private ShotClickListener shotClickListener;
    private LinkClickListener linkClickListener;
    private ShotPeekAndPop shotPeekAndPop;

    public UserInfoTeamMembersAdapter(UserClickListener userClickListener,
                                      ShotClickListener shotClickListener,
                                      LinkClickListener linkClickListener,
                                      ShotPeekAndPop shotPeekAndPop) {
        userList = Collections.emptyList();
        this.userClickListener = userClickListener;
        this.shotClickListener = shotClickListener;
        this.linkClickListener = linkClickListener;
        this.shotPeekAndPop = shotPeekAndPop;
    }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent,int viewType){
            switch (viewType) {
                case VIEW_TYPE_HEADER:
                    return new UserInfoTeamHeaderViewHolder(parent);
                case VIEW_TYPE_USER:
                    return new UserInfoTeamMembersViewHolder(parent, userClickListener,
                            shotClickListener, shotPeekAndPop);
                case VIEW_TYPE_LINKS:
                    return new UserInfoLinksViewHolder(parent, linkClickListener);
                default:
                    throw new IllegalArgumentException("Cannot create view holder for type : " + viewType);
            }
        }

        @Override
        public void onBindViewHolder (RecyclerView.ViewHolder holder,int position){
            switch (getItemViewType(position)) {
                case VIEW_TYPE_HEADER:
                    ((UserInfoTeamHeaderViewHolder) holder).bind(team);
                    break;
                case VIEW_TYPE_USER:
                    ((UserInfoTeamMembersViewHolder) holder).setUserPosition(position - 1);
                    ((UserInfoTeamMembersViewHolder) holder).bind(userList.get(position - 1));
                    break;
                case VIEW_TYPE_LINKS:
                    ((UserInfoLinksViewHolder) holder).bind(team.links());
                    break;
                default:
                    throw new IllegalArgumentException("Couldn't bind position: " + position);
            }
        }

        @Override
        public void onViewAttachedToWindow (RecyclerView.ViewHolder holder){
            super.onViewAttachedToWindow(holder);
            if (holder instanceof UserInfoTeamMembersViewHolder) {
                shotPeekAndPop.addOnGeneralActionListener(
                        ((UserInfoTeamMembersViewHolder) holder).getPeekAndPopListener());
            }
        }

        @Override
        public void onViewDetachedFromWindow (RecyclerView.ViewHolder holder){
            if (holder instanceof UserInfoTeamMembersViewHolder) {
                shotPeekAndPop.removeOnGeneralActionListener(
                        ((UserInfoTeamMembersViewHolder) holder).getPeekAndPopListener());
            }
            super.onViewDetachedFromWindow(holder);
        }

        @Override
        public int getItemViewType ( int position){
            if (position == 0) {
                return VIEW_TYPE_HEADER;
            } else if (position == userList.size() + 1) {
                return VIEW_TYPE_LINKS;
            } else {
                return VIEW_TYPE_USER;
            }
        }

        @Override
        public int getItemCount () {
            return userList.size() + HEADER_FOOTER_ADD_SIZE;
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
    }
}

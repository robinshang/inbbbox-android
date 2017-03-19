package co.netguru.android.inbbbox.feature.user.info.team.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.peekandpop.shalskar.peekandpop.PeekAndPop;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.peekandpop.ShotPeekAndPop;
import co.netguru.android.inbbbox.feature.user.UserClickListener;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

class UserInfoTeamMembersViewHolder extends BaseViewHolder<UserWithShots> {

    @BindView(R.id.user_image)
    CircleImageView userImage;

    @BindView(R.id.user_name)
    TextView userName;

    @BindView(R.id.user_shots)
    TestRecyclerView userShotsRecyclerView;

    private UserClickListener userClickListener;
    private UserShotsAdapter adapter;

    private User user;
    private int userPosition;

    UserInfoTeamMembersViewHolder(ViewGroup parent, UserClickListener userClickListener,
                                  ShotClickListener shotClickListener, ShotPeekAndPop shotPeekAndPop) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_team_info_item_member, parent, false));

        this.userClickListener = userClickListener;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                userShotsRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);

//        shotPeekAndPop.setOnGeneralActionListener(new PeekAndPop.OnGeneralActionListener() {
//            @Override
//            public void onPeek(View view, int i) {
//                Timber.d("userShotsRecyclerView.requestDisallowInterceptTouchEvent");
//                userShotsRecyclerView.requestDisallowInterceptTouchEvent(true);
//            }
//
//            @Override
//            public void onPop(View view, int i) {
//
//            }
//        });
        adapter = new UserShotsAdapter(shotClickListener, shotPeekAndPop);

        userShotsRecyclerView.setLayoutManager(linearLayoutManager);
        userShotsRecyclerView.setNestedScrollingEnabled(false);
        userShotsRecyclerView.setHasFixedSize(true);
        userShotsRecyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.user_name)
    void onUserNameClick() {
        userClickListener.onUserClick(user);
    }

    @Override
    public void bind(UserWithShots item) {
        this.user = item.user();
        this.userName.setText(item.user().name());
        loadUserImage(item.user().avatarUrl());
        initRecyclerView(item);
    }

    public void setUserPosition(int position) {
        this.userPosition = position;
    }

    private void initRecyclerView(UserWithShots user) {
        if (!user.shotList().isEmpty()) {
            userShotsRecyclerView.setVisibility(View.VISIBLE);
            adapter.setUser(user, userPosition);
        } else {
            userShotsRecyclerView.setVisibility(View.GONE);
        }
    }

    private void loadUserImage(String url) {
        Glide.with(itemView.getContext())
                .load(url)
                .fitCenter()
                .error(R.drawable.ic_ball)
                .into(userImage);
    }
}

package co.netguru.android.inbbbox.feature.user.info.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersShotImageView;
import de.hdodenhof.circleimageview.CircleImageView;

class UserInfoTeamMembersViewHolder extends BaseViewHolder<UserWithShots> {

    @BindView(R.id.user_image)
    CircleImageView userImage;

    @BindView(R.id.user_name)
    TextView userName;

    @BindView(R.id.user_shots)
    RecyclerView userShotsRecyclerView;

    UserInfoTeamMembersViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_info_item_member, parent, false));
    }

    @Override
    public void bind(UserWithShots item) {
        this.userName.setText(item.user().name());
        loadUserImage(item.user().avatarUrl());
        initRecyclerView(item);
    }

    private void initRecyclerView(UserWithShots user) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                userShotsRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        UserShotsAdapter adapter = new UserShotsAdapter();
        adapter.setShots(user.shotList());

        userShotsRecyclerView.setLayoutManager(linearLayoutManager);
        userShotsRecyclerView.setHasFixedSize(true);
        userShotsRecyclerView.setAdapter(adapter);
    }


    private void loadUserImage(String url) {
        Glide.with(itemView.getContext())
                .load(url)
                .fitCenter()
                .error(R.drawable.ic_ball)
                .into(userImage);
    }
}

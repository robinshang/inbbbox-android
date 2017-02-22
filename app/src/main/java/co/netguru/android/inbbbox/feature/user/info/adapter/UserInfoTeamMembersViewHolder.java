package co.netguru.android.inbbbox.feature.user.info.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import co.netguru.android.inbbbox.feature.shared.view.RoundedCornersShotImageView;
import de.hdodenhof.circleimageview.CircleImageView;

class UserInfoTeamMembersViewHolder extends BaseViewHolder<User> {

    @BindView(R.id.user_image)
    CircleImageView userImage;

    @BindView(R.id.user_name)
    TextView userName;

    @BindView(R.id.user_info_recycler_view)
    RecyclerView userShotsRecyclerView;

    UserInfoTeamMembersViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_info_item_member, parent, false));
    }

    @Override
    public void bind(User item) {
        this.userName.setText(item.name());
        loadUserImage(item.avatarUrl());
    }

    private void loadUserImage(String url) {
        Glide.with(itemView.getContext())
                .load(url)
                .fitCenter()
                .error(R.drawable.ic_ball)
                .into(userImage);
    }
}

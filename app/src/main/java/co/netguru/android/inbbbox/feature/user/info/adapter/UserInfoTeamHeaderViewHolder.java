package co.netguru.android.inbbbox.feature.user.info.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;

class UserInfoTeamHeaderViewHolder extends BaseViewHolder<User> {

    @BindView(R.id.user_followers)
    TextView userFollowers;

    @BindView(R.id.user_following)
    TextView userFollowing;

    @BindView(R.id.user_shots)
    TextView userShots;

    @BindView(R.id.user_country)
    TextView userCountry;

    @BindView(R.id.user_description)
    TextView userDescription;

    UserInfoTeamHeaderViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_info_header, parent, false));
    }

    @Override
    public void bind(User item) {
        userShots.setText(String.valueOf(item.shotsCount()));
        userFollowers.setText(String.valueOf(item.followersCount()));
        userFollowing.setText(String.valueOf(item.followingsCount()));
        userCountry.setText(String.valueOf(item.location()));
        userDescription.setText(String.valueOf(item.bio()));
    }

}

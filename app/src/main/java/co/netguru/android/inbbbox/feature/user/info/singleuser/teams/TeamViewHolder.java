package co.netguru.android.inbbbox.feature.user.info.singleuser.teams;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;
import de.hdodenhof.circleimageview.CircleImageView;

class TeamViewHolder extends BaseViewHolder<User> {

    @BindView(R.id.team_image)
    CircleImageView teamImage;

    @BindView(R.id.team_name)
    TextView teamName;

    private TeamClickListener teamClickListener;
    private User team;

    public TeamViewHolder(ViewGroup parent, TeamClickListener teamClickListener) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_info_team, parent, false));
        this.teamClickListener = teamClickListener;
    }

    @OnClick({R.id.team_image, R.id.team_name})
    void onTeamClick() {
        teamClickListener.onTeamClick(team);
    }

    @Override
    public void bind(User team) {
        this.team = team;
        teamName.setText(team.name());
        loadTeamImage(team.avatarUrl());
    }

    private void loadTeamImage(String url) {
        Glide.with(itemView.getContext())
                .load(url)
                .fitCenter()
                .error(R.drawable.ic_ball)
                .into(teamImage);
    }
}

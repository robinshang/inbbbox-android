package co.netguru.android.inbbbox.feature.user.info.singleuser;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.common.exceptions.InterfaceNotImplementedException;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpFragment;
import co.netguru.android.inbbbox.feature.user.UserActivity;
import co.netguru.android.inbbbox.feature.user.info.team.ShotActionListener;
import co.netguru.android.inbbbox.feature.user.info.team.TeamInfoFragment;
import timber.log.Timber;

public class UserInfoFragment extends BaseMvpFragment<UserInfoContract.View, UserInfoContract.Presenter>
        implements UserInfoContract.View {

    public static final String TAG = TeamInfoFragment.class.getSimpleName();
    private static final String KEY_USER = "key_user";

    @BindView(R.id.user_info_recyclerView)
    RecyclerView recyclerView;

    private UserInfoAdapter userInfoAdapter;

    private ShotActionListener shotActionListener;

    public static UserInfoFragment newInstance(User user) {
        final Bundle args = new Bundle();
        args.putParcelable(KEY_USER, user);

        final UserInfoFragment fragment = new UserInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            shotActionListener = (ShotActionListener) context;
        } catch (ClassCastException e) {
            Timber.e(e, "must implement OnFollowedShotActionListener");
            throw new InterfaceNotImplementedException(e, context.toString(), ShotActionListener.class.getSimpleName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    @Override
    public UserInfoContract.Presenter createPresenter() {
        UserInfoModule module = new UserInfoModule(getArguments().getParcelable(KEY_USER));
        return App.getUserComponent(getContext()).plusUserInfoComponent(module).getPresenter();
    }

    @Override
    public void showTeams(List<User> teams) {
        userInfoAdapter.setTeams(teams);
    }

    @Override
    public void showShots(List<Shot> shots) {
        userInfoAdapter.setShots(shots);
    }

    @Override
    public void showMessageOnServerError(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void openTeam(User user) {
        UserActivity.startActivity(getContext(), user);
    }

    @Override
    public void openShot(Shot shot) {
        shotActionListener.showShotDetails(shot, Collections.emptyList(), shot.author().id());
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        userInfoAdapter = new UserInfoAdapter(
                getPresenter()::onShotClick,
                getPresenter()::onTeamClick);
        userInfoAdapter.setUser(getArguments().getParcelable(KEY_USER));

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(userInfoAdapter);
    }
}

package co.netguru.android.inbbbox.feature.user.info.team;

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
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpFragment;
import co.netguru.android.inbbbox.feature.shared.view.LoadMoreScrollListener;
import co.netguru.android.inbbbox.feature.user.UserActivity;
import co.netguru.android.inbbbox.feature.user.info.team.adapter.UserInfoTeamMembersAdapter;
import timber.log.Timber;

public class TeamInfoFragment extends BaseMvpFragment
        <TeamInfoContract.View, TeamInfoContract.Presenter> implements TeamInfoContract.View {

    public static final String TAG = TeamInfoFragment.class.getSimpleName();
    private static final String KEY_USER = "key_user";

    @BindView(R.id.user_info_recycler_view)
    RecyclerView recyclerView;

    private UserInfoTeamMembersAdapter adapter;
    private ShotActionListener shotActionListener;
    private Snackbar loadingMoreSnackbar;

    public static TeamInfoFragment newInstance(User user) {
        final Bundle args = new Bundle();
        args.putParcelable(KEY_USER, user);

        final TeamInfoFragment fragment = new TeamInfoFragment();
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
            throw new InterfaceNotImplementedException(e, context.toString(),
                    ShotActionListener.class.getSimpleName());
        }
    }

    @Override
    public TeamInfoContract.Presenter createPresenter() {
        TeamInfoModule module = new TeamInfoModule(getArguments().getParcelable(KEY_USER));
        return App.getUserComponent(getContext()).plusTeamInfoComponent(module).getPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_team_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycler();
    }

    @Override
    public void showTeamMembers(List<UserWithShots> users) {
        adapter.setTeamMembers(users);
    }

    @Override
    public void showMoreTeamMembers(List<UserWithShots> users) {
        adapter.addMoreTeamMembers(users);
    }

    @Override
    public void openShotDetails(Shot shot) {
        shotActionListener.showShotDetails(shot, Collections.emptyList(), shot.author().id());
    }

    @Override
    public void openUserDetails(User user) {
        UserActivity.startActivity(getContext(), user);
    }

    @Override
    public void showLoadingMoreTeamMembersView() {
        if (loadingMoreSnackbar == null && getView() != null) {
            loadingMoreSnackbar = Snackbar.make(getView(), R.string.loading_more_followers, Snackbar.LENGTH_INDEFINITE);
        }
        loadingMoreSnackbar.show();
    }

    @Override
    public void hideLoadingMoreTeamMembersView() {
        if (loadingMoreSnackbar != null) {
            loadingMoreSnackbar.dismiss();
        }
    }

    private void initRecycler() {
        adapter = new UserInfoTeamMembersAdapter(getPresenter()::onUserClick,
                getPresenter()::onShotClick);
        adapter.setTeam(getArguments().getParcelable(KEY_USER));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(getScrollListener());
    }

    private LoadMoreScrollListener getScrollListener() {
        return new LoadMoreScrollListener(5) {

            @Override
            public void requestMoreData() {
                Timber.d("request more data");
                getPresenter().loadMoreTeamMembers();
            }
        };
    }
}

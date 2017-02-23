package co.netguru.android.inbbbox.feature.user.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpFragment;
import co.netguru.android.inbbbox.feature.user.info.adapter.UserInfoTeamMembersAdapter;
import co.netguru.android.inbbbox.feature.user.shots.UserShotsModule;

public class UserInfoFragment extends BaseMvpFragment<UserInfoContract.View, UserInfoContract.Presenter>
        implements UserInfoContract.View {

    public static final String TAG = UserInfoFragment.class.getSimpleName();
    private static final String KEY_USER = "key_user";

    @BindView(R.id.user_info_recycler_view)
    RecyclerView recyclerView;

    private UserInfoTeamMembersAdapter adapter;

    public static UserInfoFragment newInstance(UserWithShots user) {
        final Bundle args = new Bundle();
        args.putParcelable(KEY_USER, user);

        final UserInfoFragment fragment = new UserInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public UserInfoContract.Presenter createPresenter() {
        UserInfoModule module = new UserInfoModule(getArguments().getParcelable(KEY_USER));
        return App.getUserComponent(getContext()).plusUserInfoComponent(module).getPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycler();
    }

    private void initRecycler() {
        adapter = new UserInfoTeamMembersAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showData(List<UserWithShots> users) {
        adapter.setUserShots(users);
    }
}
